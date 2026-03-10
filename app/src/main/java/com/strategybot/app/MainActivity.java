package com.strategybot.app;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint({"SetJavaScriptEnabled", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Keep screen on while trading
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Full screen immersive (works across all API levels we support)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }

        // Enable WebView debugging in debug builds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();

        // ── JavaScript & DOM ──
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        // Allow file:// URLs to reach WSS:// (needed for local HTML + WebSocket)
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);

        // ── Display ──
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);

        // ── Cache ──
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // ── Media ──
        settings.setMediaPlaybackRequiresUserGesture(false);

        // ── Mixed content (allow WSS from file://) ──
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // ── Hardware acceleration ──
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        // ── JS Bridge ──
        webView.addJavascriptInterface(new AndroidBridge(), "AndroidBridge");

        // ── Chrome client (needed for console.log, alerts, etc.) ──
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // Keep all navigation inside the WebView
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Mobile UX tweaks injected after load
                view.evaluateJavascript(
                    "(function(){" +
                    "  var meta = document.querySelector('meta[name=viewport]');" +
                    "  if(meta) meta.content='width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no';" +
                    "  document.body.style.webkitUserSelect='none';" +
                    "  document.body.style.webkitTouchCallout='none';" +
                    "  if(window.AndroidBridge) window.AndroidBridge.showToast('Strategy Bot Ready');" +
                    "})();",
                    null
                );
            }
        });

        // Load the trading bot HTML from assets
        webView.loadUrl("file:///android_asset/public/index.html");
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    // ── JS ↔ Native Bridge ─────────────────────────────────────
    public class AndroidBridge {

        @JavascriptInterface
        public void showToast(String message) {
            new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show()
            );
        }

        @JavascriptInterface
        public String getAppVersion() {
            return "1.0.0";
        }

        @JavascriptInterface
        public String getPlatform() {
            return "android";
        }

        @JavascriptInterface
        public int getSdkVersion() {
            return Build.VERSION.SDK_INT;
        }

        @SuppressWarnings("deprecation")
        @JavascriptInterface
        public void vibrate() {
            android.os.Vibrator v = (android.os.Vibrator)
                getSystemService(VIBRATOR_SERVICE);
            if (v != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(android.os.VibrationEffect.createOneShot(
                        100, android.os.VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(100);
                }
            }
        }
    }
}
