#!/bin/bash
# Quick development script - JVM target only

echo "🚀 Starting JVM development build..."
time ./gradlew :composeApp:jvmRun --quiet

echo "✅ JVM app started!"