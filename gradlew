#!/bin/sh
#
# Gradle start up script for UN*X
#
APP_HOME=$(cd "$(dirname "$0")" && pwd)
APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

die() {
    echo
    echo "$*"
    echo
    exit 1
} >&2

# Find java
if [ -n "$JAVA_HOME" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
    [ -x "$JAVACMD" ] || die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
else
    JAVACMD="java"
    command -v java >/dev/null 2>&1 || die "ERROR: JAVA_HOME not set and 'java' not in PATH"
fi

CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

set -- \
    "-Dorg.gradle.appname=$APP_BASE_NAME" \
    -classpath "$CLASSPATH" \
    org.gradle.wrapper.GradleWrapperMain \
    "$@"

eval "set -- $(
    printf '%s\n' "$DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS" |
    xargs -n1 |
    sed 's~[^-[:alnum:]+,./:=@_]~\\&~g;' |
    tr '\n' ' '
)" '"$@"'

exec "$JAVACMD" "$@"
