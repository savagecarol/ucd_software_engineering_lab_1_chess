#!/bin/bash

# Chess Game Runner Script
echo "=== Chess Game ==="
echo "Starting the chess game..."
echo ""

# Check if JAR exists
if [ ! -f "target/chess-game-1.0.0.jar" ]; then
    echo "JAR file not found. Building project..."
    mvn clean package
fi

# Run the game
echo "Running chess game..."
java -jar target/chess-game-1.0.0.jar
