package com.chess.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.chess.enums.PiecesEnum;

public class MoveTest {
    
    private Player player;
    
    @Before
    public void setUp() {
        player = new Player("TestPlayer", "password123");
    }
    
    @Test
    public void testMoveCreation() {
        Move move = new Move('e', 2, 'e', 4, player);
        
        assertNotNull("Move should not be null", move);
        assertEquals("From file should be 'e'", 'e', move.getFromFile());
        assertEquals("From rank should be 2", 2, move.getFromRank());
        assertEquals("To file should be 'e'", 'e', move.getToFile());
        assertEquals("To rank should be 4", 4, move.getToRank());
        assertEquals("Player should match", player, move.getPlayer());
        assertNull("Promotion piece should be null for regular move", move.getPromotionPiece());
    }
    
    @Test
    public void testMoveWithPromotion() {
        Move move = new Move('e', 7, 'e', 8, player, PiecesEnum.QUEEN);
        
        assertNotNull("Move should not be null", move);
        assertEquals("From file should be 'e'", 'e', move.getFromFile());
        assertEquals("From rank should be 7", 7, move.getFromRank());
        assertEquals("To file should be 'e'", 'e', move.getToFile());
        assertEquals("To rank should be 8", 8, move.getToRank());
        assertEquals("Player should match", player, move.getPlayer());
        assertEquals("Promotion piece should be QUEEN", PiecesEnum.QUEEN, move.getPromotionPiece());
    }
    
    @Test
    public void testMoveWithDifferentPromotionPieces() {
        Move queenPromotion = new Move('a', 7, 'a', 8, player, PiecesEnum.QUEEN);
        Move rookPromotion = new Move('b', 7, 'b', 8, player, PiecesEnum.ROOK);
        Move bishopPromotion = new Move('c', 7, 'c', 8, player, PiecesEnum.BISHOP);
        Move knightPromotion = new Move('d', 7, 'd', 8, player, PiecesEnum.KNIGHT);
        
        assertEquals("Queen promotion should be QUEEN", PiecesEnum.QUEEN, queenPromotion.getPromotionPiece());
        assertEquals("Rook promotion should be ROOK", PiecesEnum.ROOK, rookPromotion.getPromotionPiece());
        assertEquals("Bishop promotion should be BISHOP", PiecesEnum.BISHOP, bishopPromotion.getPromotionPiece());
        assertEquals("Knight promotion should be KNIGHT", PiecesEnum.KNIGHT, knightPromotion.getPromotionPiece());
    }
    
    @Test
    public void testMoveToString() {
        Move move = new Move('e', 2, 'e', 4, player);
        String moveString = move.toString();
        
        assertNotNull("toString should not return null", moveString);
        assertTrue("toString should contain player name", moveString.contains("TestPlayer"));
        assertTrue("toString should contain from position", moveString.contains("e2"));
        assertTrue("toString should contain to position", moveString.contains("e4"));
        assertTrue("toString should contain arrow", moveString.contains("->"));
    }
    
    @Test
    public void testPromotionMoveToString() {
        Move move = new Move('e', 7, 'e', 8, player, PiecesEnum.QUEEN);
        String moveString = move.toString();
        
        assertNotNull("toString should not return null", moveString);
        assertTrue("toString should contain player name", moveString.contains("TestPlayer"));
        assertTrue("toString should contain from position", moveString.contains("e7"));
        assertTrue("toString should contain to position", moveString.contains("e8"));
        assertTrue("toString should contain promotion piece", moveString.contains("=Q"));
    }
    
    @Test
    public void testMoveWithDifferentFiles() {
        Move move1 = new Move('a', 1, 'h', 8, player);
        Move move2 = new Move('h', 1, 'a', 8, player);
        
        assertEquals("First move from file should be 'a'", 'a', move1.getFromFile());
        assertEquals("First move to file should be 'h'", 'h', move1.getToFile());
        assertEquals("Second move from file should be 'h'", 'h', move2.getFromFile());
        assertEquals("Second move to file should be 'a'", 'a', move2.getToFile());
    }
    
    @Test
    public void testMoveWithDifferentRanks() {
        Move move1 = new Move('e', 1, 'e', 8, player);
        Move move2 = new Move('e', 8, 'e', 1, player);
        
        assertEquals("First move from rank should be 1", 1, move1.getFromRank());
        assertEquals("First move to rank should be 8", 8, move1.getToRank());
        assertEquals("Second move from rank should be 8", 8, move2.getFromRank());
        assertEquals("Second move to rank should be 1", 1, move2.getToRank());
    }
    
    @Test
    public void testMoveGetters() {
        Move move = new Move('d', 4, 'd', 5, player, PiecesEnum.ROOK);
        
        // Test all getters
        assertEquals("From file getter", 'd', move.getFromFile());
        assertEquals("From rank getter", 4, move.getFromRank());
        assertEquals("To file getter", 'd', move.getToFile());
        assertEquals("To rank getter", 5, move.getToRank());
        assertEquals("Player getter", player, move.getPlayer());
        assertEquals("Promotion piece getter", PiecesEnum.ROOK, move.getPromotionPiece());
    }
}
