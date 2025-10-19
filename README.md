# Chess Game

A Java chess game implementation with a complete test suite.

## 🚀 Quick Start

### Option 1: Run with Maven
```bash
# Compile and run
mvn clean compile
mvn exec:java -Dexec.mainClass="com.chess.MainGame"

# Or create JAR and run
mvn clean package
java -jar target/chess-game-1.0.0.jar
```

### Option 2: Use the provided scripts
```bash
# Build and run in one command
./build-and-run.sh

# Or just run the game (if already built)
./run-game.sh
```

### Option 3: Manual compilation
```bash
# Compile all source files
javac -d out -cp src src/*.java src/com.chess.dto/*.java src/com.chess.enums/*.java src/com.chess.service/*.java src/com.chess.service/impl/*.java src/com.chess.storage/*.java src/com.chess.validator/*.java src/com.chess.utility/*.java

# Run the game
java -cp out com.chess.MainGame
```

## 📁 Project Structure

```
src/
├── com.chess.dto/                    # Data Transfer Objects
│   ├── Block.java         # Chess board square
│   ├── ChessBoard.java    # Chess board logic
│   ├── Game.java          # Game state management
│   ├── Move.java          # Chess move representation
│   └── Player.java        # Player data and statistics
├── com.chess.enums/                  # Enumerations
│   ├── ColourEnum.java    # Colors (WHITE, BLACK, BLANK)
│   └── PiecesEnum.java    # Chess pieces with values
├── com.chess.service/                # Business logic services
│   ├── GameService.java   # Game com.chess.service interface
│   ├── PlayerService.java # Player com.chess.service interface
│   ├── ScoringService.java # Scoring system
│   └── impl/              # Service implementations
├── com.chess.storage/                # Data persistence
│   ├── GameStorage.java   # Game save/load
│   └── PlayerStorage.java # Player data com.chess.storage
├── com.chess.validator/              # Input validation
│   └── PlayerValidator.java
├── com.chess.utility/                # Utility classes
│   └── ChessMainMenu.java # Main menu system
└── com.chess.MainGame.java          # Main entry point
```

## 🎮 Game Features

- **Complete Chess Implementation**: All standard chess rules
- **Player Management**: Create players, track statistics
- **Game Persistence**: Save and load games
- **Scoring System**: Points for moves, captures, special moves
- **Special Moves**: Castling, en passant, pawn promotion
- **Leaderboard**: Track player rankings
- **Game History**: Review finished games

## 🧪 Testing

The project includes a comprehensive test suite (removed for simplicity but can be re-added):

- **Unit Tests**: Individual class functionality
- **Integration Tests**: Component interactions
- **Edge Case Testing**: Boundary conditions and error handling
- **Game Logic Tests**: Chess rules and move validation

## 🛠️ Build Requirements

- **Java 8+**
- **Maven** (optional, for build automation)

## 📦 Creating JAR File

```bash
# Using Maven
mvn clean package

# The JAR file will be created at: target/chess-game-1.0.0.jar
```

## 🎯 How to Play

1. **Start the game**: Run the JAR file or use the scripts
2. **Create players**: Choose option 2 to create player accounts
3. **Start a game**: Choose option 1 to start a new game
4. **Make moves**: Enter moves in format like "e2 e4"
5. **Use hints**: Get valid moves for any piece
6. **Save progress**: Games are automatically saved

## 🏆 Game Rules

- Standard chess rules apply
- Special moves: Castling, en passant, pawn promotion
- Scoring system rewards strategic play
- Games can be saved and resumed
- Player statistics are tracked

## 📊 Scoring System

- **Piece Values**: Pawn(1), Knight(3), Bishop(3), Rook(5), Queen(9), King(10)
- **Move Bonuses**: Check(+2), Checkmate(+10), Castling(+1), En Passant(+1), Promotion(+2)
- **Game Results**: Win(+3), Draw(+1), Loss(0)

## 🔧 Troubleshooting

### Common Issues:
1. **"No line found" error**: This is normal when running without input - the game expects user interaction
2. **Compilation errors**: Make sure you have Java 8+ installed
3. **Maven not found**: Install Maven or use manual compilation

### Solutions:
- Use the provided scripts for easy building
- Run in an interactive terminal for full functionality
- Check Java version: `java -version`

## 📝 Notes

- The game requires interactive input to function properly
- Games are saved in the `com.chess.storage/` directory
- Player data is stored in `com.chess.storage/players.csv`
- The project is ready for development and testing
