# Chess Game Demo Guide

## How to Run the Chess Game

### **1. Compile the Game**
```bash
cd /Users/apple/Documents/ucd/software_engineering_project
javac -d out/production/chess -cp src src/MainGame.java src/dto/*.java src/enums/*.java src/service/*.java src/service/impl/*.java src/storage/*.java src/utility/*.java src/validator/*.java
```

### **2. Run the Game**
```bash
java -cp out/production/chess MainGame
```

## **Main Menu Options**

```
========================================================
                      CHESS MENU                        
========================================================
 1. Start a new game
 2. Create a player
 3. Continue previous game
 4. review finished games
 5. Leaderboard
 6. Quit
========================================================
```

## **Step-by-Step Demo**

### **Step 1: Create Players**
1. Choose option `2` to create a player
2. Enter player name (e.g., "alice")
3. Enter password (minimum 6 characters)
4. Repeat for second player (e.g., "bob")

### **Step 2: Start a New Game**
1. Choose option `1` to start a new game
2. Enter first player credentials (alice)
3. Enter second player credentials (bob)
4. Confirm players and start game

### **Step 3: Play Chess**
1. The board will display with pieces
2. Enter moves in format: `e2 e4` (from to)
3. Use option `2` for hints to see valid moves
4. Use option `3` to quit game

### **Step 4: Special Features**

#### **Pawn Promotion**
- When pawn reaches 8th rank (white) or 1st rank (black)
- Choose promotion piece: Queen, Rook, Bishop, or Knight

#### **Castling**
- Move king 2 squares toward rook
- King-side: `e1 g1` (white) or `e8 g8` (black)
- Queen-side: `e1 c1` (white) or `e8 c8` (black)

#### **En Passant**
- Capture pawn that just moved 2 squares
- Move diagonally behind the pawn

### **Step 5: Game Management**
- **Continue Game**: Option `3` to resume unfinished games
- **Review Games**: Option `4` to replay finished games
- **Leaderboard**: Option `5` to see player rankings

## **Example Game Session**

```
▶ Enter your move (e.g., e2 e4): e2 e4
▶ Move successful!

▶ Enter your move (e.g., e2 e4): e7 e5
▶ Move successful!

▶ Enter your move (e.g., e2 e4): f1 c4
▶ Move successful!

▶ Enter your move (e.g., e2 e4): b8 c6
▶ Move successful!
```

## **Features Demonstrated**

✅ **Complete Chess Implementation**
- All piece movements
- Special moves (castling, en passant, promotion)
- Check and checkmate detection
- Turn-based gameplay

✅ **Game Persistence**
- Games saved automatically after each move
- JSON format: `storage/player1-player2:timestamp.json`
- Resume unfinished games
- Replay finished games

✅ **Player Management**
- Secure authentication with password hashing
- Player statistics and ratings
- Leaderboard system

✅ **User Interface**
- Visual chess board with colored squares
- Interactive menus and prompts
- Hint system for valid moves
- Move history display

## **File Structure**
```
storage/
├── players.csv          # Player data
└── *.json              # Game files (created during play)

out/production/chess/   # Compiled classes
src/                    # Source code
├── dto/               # Data Transfer Objects
├── enums/             # Enumerations
├── service/           # Business logic
├── storage/           # Data persistence
├── utility/           # User interface
└── validator/         # Input validation
```

The chess game is fully functional and ready to play! 🎯♟️
