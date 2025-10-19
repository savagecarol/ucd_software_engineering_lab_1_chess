#!/bin/bash

# Chess Game Build and Run Script
echo "=== Chess Game Build and Run ==="
echo ""

# Clean and compile
echo "1. Cleaning and compiling project..."
mvn clean compile

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful"
else
    echo "✗ Compilation failed"
    exit 1
fi

# Create JAR
echo ""
echo "2. Creating JAR file..."
mvn package

if [ $? -eq 0 ]; then
    echo "✓ JAR creation successful"
    echo "✓ JAR file created: target/chess-game-1.0.0.jar"
else
    echo "✗ JAR creation failed"
    exit 1
fi

# Test JAR
echo ""
echo "3. Testing JAR file..."
echo "Running: java -jar target/chess-game-1.0.0.jar"
echo "Note: The game will start and show the menu. Press Ctrl+C to exit."
echo ""

java -jar target/chess-game-1.0.0.jar
