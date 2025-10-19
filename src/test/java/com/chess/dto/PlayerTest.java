package com.chess.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
    
    private Player player;
    
    @Before
    public void setUp() {
        player = new Player("TestPlayer", "password123");
    }
    
    @Test
    public void testPlayerCreation() {
        assertNotNull("Player should not be null", player);
        assertEquals("Name should match", "TestPlayer", player.getName());
        assertNotNull("ID should not be null", player.getId());
        assertTrue("ID should not be empty", !player.getId().isEmpty());
    }
    
    @Test
    public void testInitialStatistics() {
        assertEquals("Initial wins should be 0", 0, player.getWin());
        assertEquals("Initial draws should be 0", 0, player.getDraw());
        assertEquals("Initial losses should be 0", 0, player.getLoss());
        assertEquals("Initial pending should be 0", 0, player.getPending());
        assertEquals("Initial rating should be 100", 100, player.getRating());
        assertEquals("Initial total points should be 0", 0, player.getTotalPoints());
        assertEquals("Initial games played should be 0", 0, player.getGamesPlayed());
        assertEquals("Initial pieces captured should be 0", 0, player.getPiecesCaptured());
        assertEquals("Initial special moves should be 0", 0, player.getSpecialMoves());
    }
    
    @Test
    public void testPasswordValidation() {
        assertTrue("Correct password should be valid", player.validatePassword("password123"));
        assertFalse("Wrong password should be invalid", player.validatePassword("wrongpassword"));
        assertFalse("Empty password should be invalid", player.validatePassword(""));
        
        // Test null password - this should throw an exception or return false
        try {
            boolean result = player.validatePassword(null);
            assertFalse("Null password should be invalid", result);
        } catch (Exception e) {
            // Expected behavior - null password should cause an exception
            assertTrue("Null password should cause exception", e instanceof NullPointerException);
        }
    }
    
    @Test
    public void testAddWin() {
        player.addWin();
        assertEquals("Wins should be 1", 1, player.getWin());
        assertEquals("Games played should be 1", 1, player.getGamesPlayed());
    }
    
    @Test
    public void testAddDraw() {
        player.addDraw();
        assertEquals("Draws should be 1", 1, player.getDraw());
        assertEquals("Games played should be 1", 1, player.getGamesPlayed());
    }
    
    @Test
    public void testAddLoss() {
        player.addLoss();
        assertEquals("Losses should be 1", 1, player.getLoss());
        assertEquals("Games played should be 1", 1, player.getGamesPlayed());
    }
    
    @Test
    public void testAddPoints() {
        player.addPoints(50);
        assertEquals("Total points should be 50", 50, player.getTotalPoints());
        
        player.addPoints(25);
        assertEquals("Total points should be 75", 75, player.getTotalPoints());
    }
    
    @Test
    public void testAddCapturedPiece() {
        player.addCapturedPiece();
        assertEquals("Pieces captured should be 1", 1, player.getPiecesCaptured());
        
        player.addCapturedPiece();
        player.addCapturedPiece();
        assertEquals("Pieces captured should be 3", 3, player.getPiecesCaptured());
    }
    
    @Test
    public void testAddSpecialMove() {
        player.addSpecialMove();
        assertEquals("Special moves should be 1", 1, player.getSpecialMoves());
        
        player.addSpecialMove();
        assertEquals("Special moves should be 2", 2, player.getSpecialMoves());
    }
    
    @Test
    public void testAddPending() {
        player.addPending();
        assertEquals("Pending should be 1", 1, player.getPending());
        
        player.addPending();
        player.addPending();
        assertEquals("Pending should be 3", 3, player.getPending());
    }
    
    @Test
    public void testCompletePending() {
        player.addPending();
        player.addPending();
        player.completePending();
        assertEquals("Pending should be 1", 1, player.getPending());
        
        player.completePending();
        assertEquals("Pending should be 0", 0, player.getPending());
        
        // Should not go below 0
        player.completePending();
        assertEquals("Pending should not go below 0", 0, player.getPending());
    }
    
    @Test
    public void testSetters() {
        player.setWin(5);
        player.setDraw(3);
        player.setLoss(2);
        player.setPending(1);
        player.setRating(1200);
        player.setTotalPoints(100);
        player.setGamesPlayed(10);
        player.setPiecesCaptured(15);
        player.setSpecialMoves(3);
        
        assertEquals("Win should be 5", 5, player.getWin());
        assertEquals("Draw should be 3", 3, player.getDraw());
        assertEquals("Loss should be 2", 2, player.getLoss());
        assertEquals("Pending should be 1", 1, player.getPending());
        assertEquals("Rating should be 1200", 1200, player.getRating());
        assertEquals("Total points should be 100", 100, player.getTotalPoints());
        assertEquals("Games played should be 10", 10, player.getGamesPlayed());
        assertEquals("Pieces captured should be 15", 15, player.getPiecesCaptured());
        assertEquals("Special moves should be 3", 3, player.getSpecialMoves());
    }
    
    @Test
    public void testToString() {
        String playerString = player.toString();
        assertNotNull("toString should not return null", playerString);
        assertTrue("toString should contain player name", playerString.contains("TestPlayer"));
        assertTrue("toString should contain statistics", playerString.contains("W="));
    }
}
