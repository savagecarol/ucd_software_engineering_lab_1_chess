package com.chess.dto;

import com.chess.enums.ColourEnum;
import com.chess.enums.PiecesEnum;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private String gameId;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private final ChessBoard board;
    private ColourEnum currentTurn;
    private boolean isFinished;
    private boolean whiteInCheck;
    private boolean blackInCheck;
    private final List<Move> moveHistory;

    public Game(Player whitePlayer, Player blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.board = new ChessBoard();
        this.currentTurn = ColourEnum.WHITE;
        this.isFinished = false;
        this.whiteInCheck = false;
        this.blackInCheck = false;
        this.moveHistory = new ArrayList<>();
        this.gameId = generateGameId();
    }

    private String generateGameId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return whitePlayer.getName() + ":" + blackPlayer.getName() + "-" + timestamp;
    }

    public String getGameId() {
        return gameId;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public ColourEnum getCurrentTurn() {
        return currentTurn;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isWhiteInCheck() {
        return whiteInCheck;
    }

    public boolean isBlackInCheck() {
        return blackInCheck;
    }

    public Player getCurrentPlayer() {
        return currentTurn == ColourEnum.WHITE ? whitePlayer : blackPlayer;
    }

    public Player getOpponent() {
        return currentTurn == ColourEnum.WHITE ? blackPlayer : whitePlayer;
    }

    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    public void switchTurn() {
        currentTurn = (currentTurn == ColourEnum.WHITE) ? ColourEnum.BLACK : ColourEnum.WHITE;
    }

    public boolean makeMove(Move move) {
        if (isFinished) {
            System.out.println(" ▶ The game is already over.");
            return false;
        }

        if (!move.getPlayer().getId().equals(getCurrentPlayer().getId())) {
            System.out.println(" ▶ It's not your turn.");
            return false;
        }

        // Check if the move is valid according to chess rules
        if (!isValidMove(move)) {
            System.out.println(" ▶ Invalid move according to chess rules.");
            return false;
        }

        // Check if the move would put own king in check
        if (wouldPutKingInCheck(move)) {
            System.out.println(" ▶ This move would put your king in check.");
            return false;
        }

        boolean success = board.movePiece(move);
        if (!success) return false;

        moveHistory.add(move);
        updateCheckStatus();
        
        // Check for checkmate or stalemate
        if (isCheckmate()) {
            System.out.println(" ▶ Checkmate! " + getOpponent().getName() + " wins!");
            isFinished = true;
        } else if (isStalemate()) {
            System.out.println(" ▶ Stalemate! The game is a draw.");
            isFinished = true;
        }
        
        switchTurn();
        return true;
    }

    private void updateCheckStatus() {
        whiteInCheck = isKingInCheck(ColourEnum.WHITE);
        blackInCheck = isKingInCheck(ColourEnum.BLACK);
    }

    private boolean isValidMove(Move move) {
        // Check if the piece at the source belongs to the current player
        Block fromBlock = board.getBlock(move.getFromFile(), move.getFromRank());
        if (fromBlock == null || fromBlock.getPlayer() != getCurrentTurn()) {
            return false;
        }
        
        // Get all possible moves for the piece at the source position
        List<Move> possibleMoves = board.getPossibleMoves(move.getFromFile(), move.getFromRank(), moveHistory, move.getPlayer());
        
        // Check if the move is in the list of possible moves
        for (Move possibleMove : possibleMoves) {
            if (possibleMove.getToFile() == move.getToFile() && 
                possibleMove.getToRank() == move.getToRank()) {
                return true;
            }
        }
        return false;
    }

    private boolean wouldPutKingInCheck(Move move) {
        // Temporarily make the move
        Block fromBlock = board.getBlock(move.getFromFile(), move.getFromRank());
        Block toBlock = board.getBlock(move.getToFile(), move.getToRank());
        
        // Store original state
        PiecesEnum originalFromPiece = fromBlock.getPiece();
        ColourEnum originalFromPlayer = fromBlock.getPlayer();
        PiecesEnum originalToPiece = toBlock.getPiece();
        ColourEnum originalToPlayer = toBlock.getPlayer();
        
        // Make the move temporarily
        toBlock.setPiece(fromBlock.getPiece());
        toBlock.setPlayer(fromBlock.getPlayer());
        fromBlock.setPiece(PiecesEnum.EMPTY);
        fromBlock.setPlayer(ColourEnum.BLANK);
        
        // Check if king is in check after the move
        boolean kingInCheck = isKingInCheck(move.getPlayer().getId().equals(whitePlayer.getId()) ? ColourEnum.WHITE : ColourEnum.BLACK);
        
        // Restore original state
        fromBlock.setPiece(originalFromPiece);
        fromBlock.setPlayer(originalFromPlayer);
        toBlock.setPiece(originalToPiece);
        toBlock.setPlayer(originalToPlayer);
        
        return kingInCheck;
    }

    private boolean isKingInCheck(ColourEnum player) {
        // Find the king's position
        char kingFile = ' ';
        int kingRank = 0;
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Block block = board.getBlock((char)('a' + j), i + 1);
                if (block.getPiece() == PiecesEnum.KING && block.getPlayer() == player) {
                    kingFile = (char)('a' + j);
                    kingRank = i + 1;
                    break;
                }
            }
        }
        
        if (kingFile == ' ') return false; // King not found
        
        // Check if any opponent piece can attack the king
        ColourEnum opponent = (player == ColourEnum.WHITE) ? ColourEnum.BLACK : ColourEnum.WHITE;
        Player opponentPlayer = (player == ColourEnum.WHITE) ? blackPlayer : whitePlayer;
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Block block = board.getBlock((char)('a' + j), i + 1);
                if (block.getPlayer() == opponent) {
                    List<Move> possibleMoves = board.getPossibleMoves((char)('a' + j), i + 1, moveHistory, opponentPlayer);
                    for (Move move : possibleMoves) {
                        if (move.getToFile() == kingFile && move.getToRank() == kingRank) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }

    private boolean isCheckmate() {
        ColourEnum currentPlayer = getCurrentTurn();
        if (!isKingInCheck(currentPlayer)) {
            return false;
        }
        
        // Check if there are any legal moves for the current player
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Block block = board.getBlock((char)('a' + j), i + 1);
                if (block.getPlayer() == currentPlayer) {
                    Player currentPlayerObj = (currentPlayer == ColourEnum.WHITE) ? whitePlayer : blackPlayer;
                    List<Move> possibleMoves = board.getPossibleMoves((char)('a' + j), i + 1, moveHistory, currentPlayerObj);
                    for (Move move : possibleMoves) {
                        if (!wouldPutKingInCheck(move)) {
                            return false; // Found a legal move
                        }
                    }
                }
            }
        }
        
        return true; // No legal moves found
    }

    private boolean isStalemate() {
        ColourEnum currentPlayer = getCurrentTurn();
        if (isKingInCheck(currentPlayer)) {
            return false; // Not stalemate if in check
        }
        
        // Check if there are any legal moves for the current player
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Block block = board.getBlock((char)('a' + j), i + 1);
                if (block.getPlayer() == currentPlayer) {
                    Player currentPlayerObj = (currentPlayer == ColourEnum.WHITE) ? whitePlayer : blackPlayer;
                    List<Move> possibleMoves = board.getPossibleMoves((char)('a' + j), i + 1, moveHistory, currentPlayerObj);
                    for (Move move : possibleMoves) {
                        if (!wouldPutKingInCheck(move)) {
                            return false; // Found a legal move
                        }
                    }
                }
            }
        }
        
        return true; // No legal moves found
    }

    public void endGame() {
        isFinished = true;
    }

    // Additional methods for JSON serialization and game management
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setCurrentTurn(ColourEnum currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }

    public void setWhiteInCheck(boolean whiteInCheck) {
        this.whiteInCheck = whiteInCheck;
    }

    public void setBlackInCheck(boolean blackInCheck) {
        this.blackInCheck = blackInCheck;
    }

    public void printMoveHistory() {
        if (moveHistory.isEmpty()) {
            System.out.println(" ▶ No moves have been made yet.");
            return;
        }
        System.out.println(" ▶ Move History:");
        for (int i = 0; i < moveHistory.size(); i++) {
            Move move = moveHistory.get(i);
            System.out.printf("%2d. %s%n", i + 1, move);
        }
    }
}
