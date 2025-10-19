package com.chess.dto;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.chess.enums.ColourEnum;
import com.chess.enums.PiecesEnum;

public class GameTest {
    
    private Player whitePlayer;
    private Player blackPlayer;
    private Game game;
    
    @Before
    public void setUp() {
        whitePlayer = new Player("WhitePlayer", "password123");
        blackPlayer = new Player("BlackPlayer", "password123");
        game = new Game(whitePlayer, blackPlayer);
    }
    
    @Test
    public void testGameCreation() {
        assertNotNull("Game should not be null", game);
        assertNotNull("Game ID should not be null", game.getGameId());
        assertTrue("Game ID should not be empty", !game.getGameId().isEmpty());
        assertEquals("White player should match", whitePlayer, game.getWhitePlayer());
        assertEquals("Black player should match", blackPlayer, game.getBlackPlayer());
    }
    
    @Test
    public void testInitialGameState() {
        assertEquals("Initial turn should be WHITE", ColourEnum.WHITE, game.getCurrentTurn());
        assertFalse("Game should not be finished initially", game.isFinished());
        assertFalse("White should not be in check initially", game.isWhiteInCheck());
        assertFalse("Black should not be in check initially", game.isBlackInCheck());
        assertTrue("Move history should be empty initially", game.getMoveHistory().isEmpty());
    }
    
    @Test
    public void testCurrentPlayer() {
        assertEquals("Current player should be white initially", whitePlayer, game.getCurrentPlayer());
        assertEquals("Opponent should be black initially", blackPlayer, game.getOpponent());
        
        game.switchTurn();
        assertEquals("Current player should be black after switch", blackPlayer, game.getCurrentPlayer());
        assertEquals("Opponent should be white after switch", whitePlayer, game.getOpponent());
    }
    
    @Test
    public void testSwitchTurn() {
        assertEquals("Initial turn should be WHITE", ColourEnum.WHITE, game.getCurrentTurn());
        
        game.switchTurn();
        assertEquals("Turn should be BLACK after switch", ColourEnum.BLACK, game.getCurrentTurn());
        
        game.switchTurn();
        assertEquals("Turn should be WHITE after second switch", ColourEnum.WHITE, game.getCurrentTurn());
    }
    
    @Test
    public void testValidMove() {
        // Test a valid pawn move
        Move move = new Move('e', 2, 'e', 4, whitePlayer);
        boolean result = game.makeMove(move);
        
        assertTrue("Valid move should succeed", result);
        assertEquals("Turn should switch after move", ColourEnum.BLACK, game.getCurrentTurn());
        assertEquals("Move should be added to history", 1, game.getMoveHistory().size());
        assertEquals("Move in history should match", move, game.getMoveHistory().get(0));
    }
    
    @Test
    public void testInvalidMoveWrongPlayer() {
        // Try to move with wrong player
        Move move = new Move('e', 2, 'e', 4, blackPlayer);
        boolean result = game.makeMove(move);
        
        assertFalse("Move with wrong player should fail", result);
        assertEquals("Turn should not change", ColourEnum.WHITE, game.getCurrentTurn());
        assertTrue("Move history should remain empty", game.getMoveHistory().isEmpty());
    }
    
    @Test
    public void testMoveAfterGameFinished() {
        // End the game
        game.endGame();
        assertTrue("Game should be finished", game.isFinished());
        
        // Try to make a move
        Move move = new Move('e', 2, 'e', 4, whitePlayer);
        boolean result = game.makeMove(move);
        
        assertFalse("Move should fail after game is finished", result);
    }
    
    @Test
    public void testGameIdGeneration() {
        String gameId = game.getGameId();
        assertNotNull("Game ID should not be null", gameId);
        assertTrue("Game ID should contain white player name", gameId.contains("WhitePlayer"));
        assertTrue("Game ID should contain black player name", gameId.contains("BlackPlayer"));
        assertTrue("Game ID should contain timestamp", gameId.contains("-"));
    }
    
    @Test
    public void testGameIdUniqueness() {
        // Create games with different players to ensure different IDs
        Player player3 = new Player("Player3", "password123");
        Game game2 = new Game(player3, blackPlayer);
        String gameId1 = game.getGameId();
        String gameId2 = game2.getGameId();
        
        // Game IDs should be different due to different player names
        assertNotEquals("Game IDs should be unique", gameId1, gameId2);
        
        // Also test that both game IDs contain expected components
        assertTrue("Game ID 1 should contain player names", gameId1.contains("WhitePlayer") && gameId1.contains("BlackPlayer"));
        assertTrue("Game ID 2 should contain player names", gameId2.contains("Player3") && gameId2.contains("BlackPlayer"));
    }
    
    @Test
    public void testSetters() {
        // Test setting game properties
        game.setGameId("test-game-id");
        game.setCurrentTurn(ColourEnum.BLACK);
        game.setFinished(true);
        game.setWhiteInCheck(true);
        game.setBlackInCheck(false);
        
        assertEquals("Game ID should be set", "test-game-id", game.getGameId());
        assertEquals("Current turn should be set", ColourEnum.BLACK, game.getCurrentTurn());
        assertTrue("Game should be finished", game.isFinished());
        assertTrue("White should be in check", game.isWhiteInCheck());
        assertFalse("Black should not be in check", game.isBlackInCheck());
    }
    
    @Test
    public void testMoveHistory() {
        // Make several moves
        Move move1 = new Move('e', 2, 'e', 4, whitePlayer);
        game.makeMove(move1);
        
        Move move2 = new Move('e', 7, 'e', 5, blackPlayer);
        game.makeMove(move2);
        
        List<Move> history = game.getMoveHistory();
        assertEquals("Should have 2 moves in history", 2, history.size());
        assertEquals("First move should be move1", move1, history.get(0));
        assertEquals("Second move should be move2", move2, history.get(1));
    }
    
    @Test
    public void testPrintMoveHistory() {
        // Test with empty history
        game.printMoveHistory(); // Should not throw exception
        
        // Test with moves
        Move move = new Move('e', 2, 'e', 4, whitePlayer);
        game.makeMove(move);
        game.printMoveHistory(); // Should not throw exception
    }
    
    @Test
    public void testEndGame() {
        assertFalse("Game should not be finished initially", game.isFinished());
        
        game.endGame();
        assertTrue("Game should be finished after endGame()", game.isFinished());
    }
    
    @Test
    public void testBoardAccess() {
        ChessBoard board = game.getBoard();
        assertNotNull("Board should not be null", board);
        
        // Test that board is properly initialized
        assertEquals("e1 should have white king", PiecesEnum.KING, board.getBlock('e', 1).getPiece());
        assertEquals("e1 should be white", ColourEnum.WHITE, board.getBlock('e', 1).getPlayer());
        assertEquals("e8 should have black king", PiecesEnum.KING, board.getBlock('e', 8).getPiece());
        assertEquals("e8 should be black", ColourEnum.BLACK, board.getBlock('e', 8).getPlayer());
    }
    
    @Test
    public void testInvalidMoveFromEmptySquare() {
        // Try to move from empty square
        Move move = new Move('e', 4, 'e', 5, whitePlayer);
        boolean result = game.makeMove(move);
        
        assertFalse("Move from empty square should fail", result);
        assertEquals("Turn should not change", ColourEnum.WHITE, game.getCurrentTurn());
        assertTrue("Move history should remain empty", game.getMoveHistory().isEmpty());
    }
    
    @Test
    public void testMultipleMoves() {
        // Make several valid moves
        Move move1 = new Move('e', 2, 'e', 4, whitePlayer);
        assertTrue("First move should succeed", game.makeMove(move1));
        assertEquals("Turn should be black", ColourEnum.BLACK, game.getCurrentTurn());
        
        Move move2 = new Move('e', 7, 'e', 5, blackPlayer);
        assertTrue("Second move should succeed", game.makeMove(move2));
        assertEquals("Turn should be white", ColourEnum.WHITE, game.getCurrentTurn());
        
        Move move3 = new Move('d', 2, 'd', 4, whitePlayer);
        assertTrue("Third move should succeed", game.makeMove(move3));
        assertEquals("Turn should be black", ColourEnum.BLACK, game.getCurrentTurn());
        
        assertEquals("Should have 3 moves in history", 3, game.getMoveHistory().size());
    }
    
    @Test
    public void testGameStateConsistency() {
        // Make a move and verify game state
        Move move = new Move('e', 2, 'e', 4, whitePlayer);
        game.makeMove(move);
        
        // Verify board state
        ChessBoard board = game.getBoard();
        assertEquals("e4 should have pawn", PiecesEnum.PAWN, board.getBlock('e', 4).getPiece());
        assertEquals("e4 should be white", ColourEnum.WHITE, board.getBlock('e', 4).getPlayer());
        assertEquals("e2 should be empty", PiecesEnum.EMPTY, board.getBlock('e', 2).getPiece());
        
        // Verify game state
        assertEquals("Turn should be black", ColourEnum.BLACK, game.getCurrentTurn());
        assertEquals("Current player should be black", blackPlayer, game.getCurrentPlayer());
        assertEquals("Opponent should be white", whitePlayer, game.getOpponent());
    }
    
    @Test
    public void testPlayerReferences() {
        // Test that player references are maintained
        assertEquals("White player reference should be maintained", whitePlayer, game.getWhitePlayer());
        assertEquals("Black player reference should be maintained", blackPlayer, game.getBlackPlayer());
        
        // Test that player objects are the same instances
        assertSame("White player should be same instance", whitePlayer, game.getWhitePlayer());
        assertSame("Black player should be same instance", blackPlayer, game.getBlackPlayer());
    }
}
