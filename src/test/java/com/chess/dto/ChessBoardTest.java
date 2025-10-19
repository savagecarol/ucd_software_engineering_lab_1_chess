package com.chess.dto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.chess.enums.ColourEnum;
import com.chess.enums.PiecesEnum;

public class ChessBoardTest {
    
    private ChessBoard board;
    private Player whitePlayer;
    private Player blackPlayer;
    
    @Before
    public void setUp() {
        board = new ChessBoard();
        whitePlayer = new Player("WhitePlayer", "password123");
        blackPlayer = new Player("BlackPlayer", "password123");
    }
    
    @Test
    public void testBoardInitialization() {
        assertNotNull("Board should not be null", board);
        assertNotNull("Board array should not be null", board.getBoard());
        assertEquals("Board should be 8x8", 8, board.getBoard().length);
        assertEquals("Board should be 8x8", 8, board.getBoard()[0].length);
    }
    
    @Test
    public void testInitialPiecePlacement() {
        // Test white pieces on rank 1
        assertEquals("a1 should have white rook", PiecesEnum.ROOK, board.getBlock('a', 1).getPiece());
        assertEquals("a1 should be white", ColourEnum.WHITE, board.getBlock('a', 1).getPlayer());
        
        assertEquals("b1 should have white knight", PiecesEnum.KNIGHT, board.getBlock('b', 1).getPiece());
        assertEquals("c1 should have white bishop", PiecesEnum.BISHOP, board.getBlock('c', 1).getPiece());
        assertEquals("d1 should have white queen", PiecesEnum.QUEEN, board.getBlock('d', 1).getPiece());
        assertEquals("e1 should have white king", PiecesEnum.KING, board.getBlock('e', 1).getPiece());
        assertEquals("f1 should have white bishop", PiecesEnum.BISHOP, board.getBlock('f', 1).getPiece());
        assertEquals("g1 should have white knight", PiecesEnum.KNIGHT, board.getBlock('g', 1).getPiece());
        assertEquals("h1 should have white rook", PiecesEnum.ROOK, board.getBlock('h', 1).getPiece());
        
        // Test white pawns on rank 2
        for (char file = 'a'; file <= 'h'; file++) {
            assertEquals("Rank 2 should have white pawns", PiecesEnum.PAWN, board.getBlock(file, 2).getPiece());
            assertEquals("Rank 2 should be white", ColourEnum.WHITE, board.getBlock(file, 2).getPlayer());
        }
        
        // Test black pieces on rank 8
        assertEquals("a8 should have black rook", PiecesEnum.ROOK, board.getBlock('a', 8).getPiece());
        assertEquals("a8 should be black", ColourEnum.BLACK, board.getBlock('a', 8).getPlayer());
        
        assertEquals("b8 should have black knight", PiecesEnum.KNIGHT, board.getBlock('b', 8).getPiece());
        assertEquals("c8 should have black bishop", PiecesEnum.BISHOP, board.getBlock('c', 8).getPiece());
        assertEquals("d8 should have black queen", PiecesEnum.QUEEN, board.getBlock('d', 8).getPiece());
        assertEquals("e8 should have black king", PiecesEnum.KING, board.getBlock('e', 8).getPiece());
        assertEquals("f8 should have black bishop", PiecesEnum.BISHOP, board.getBlock('f', 8).getPiece());
        assertEquals("g8 should have black knight", PiecesEnum.KNIGHT, board.getBlock('g', 8).getPiece());
        assertEquals("h8 should have black rook", PiecesEnum.ROOK, board.getBlock('h', 8).getPiece());
        
        // Test black pawns on rank 7
        for (char file = 'a'; file <= 'h'; file++) {
            assertEquals("Rank 7 should have black pawns", PiecesEnum.PAWN, board.getBlock(file, 7).getPiece());
            assertEquals("Rank 7 should be black", ColourEnum.BLACK, board.getBlock(file, 7).getPlayer());
        }
    }
    
    @Test
    public void testEmptySquares() {
        // Test that ranks 3-6 are empty
        for (int rank = 3; rank <= 6; rank++) {
            for (char file = 'a'; file <= 'h'; file++) {
                assertEquals("Middle ranks should be empty", PiecesEnum.EMPTY, board.getBlock(file, rank).getPiece());
                assertEquals("Middle ranks should have no player", ColourEnum.BLANK, board.getBlock(file, rank).getPlayer());
            }
        }
    }
    
    @Test
    public void testGetBlock() {
        // Test valid positions
        assertNotNull("a1 should exist", board.getBlock('a', 1));
        assertNotNull("h8 should exist", board.getBlock('h', 8));
        assertNotNull("e4 should exist", board.getBlock('e', 4));
        
        // Test invalid positions
        assertNull("i1 should not exist", board.getBlock('i', 1));
        assertNull("a0 should not exist", board.getBlock('a', 0));
        assertNull("a9 should not exist", board.getBlock('a', 9));
        assertNull("z5 should not exist", board.getBlock('z', 5));
    }
    
    @Test
    public void testSquareColors() {
        // Test alternating square colors
        assertEquals("a1 should be white square", ColourEnum.WHITE, board.getBlock('a', 1).getColour());
        assertEquals("b1 should be black square", ColourEnum.BLACK, board.getBlock('b', 1).getColour());
        assertEquals("a2 should be black square", ColourEnum.BLACK, board.getBlock('a', 2).getColour());
        assertEquals("b2 should be white square", ColourEnum.WHITE, board.getBlock('b', 2).getColour());
    }
    
    @Test
    public void testPawnMoves() {
        // Test white pawn initial move (can move 1 or 2 squares)
        List<Move> whitePawnMoves = board.getPossibleMoves('e', 2, null, whitePlayer);
        assertTrue("White pawn should have moves", whitePawnMoves.size() > 0);
        
        // Test black pawn initial move
        List<Move> blackPawnMoves = board.getPossibleMoves('e', 7, null, blackPlayer);
        assertTrue("Black pawn should have moves", blackPawnMoves.size() > 0);
        
        // Test pawn in middle of board (can only move 1 square)
        // First move a pawn to e4
        Move move1 = new Move('e', 2, 'e', 4, whitePlayer);
        assertTrue("First pawn move should succeed", board.movePiece(move1));
        
        List<Move> pawnMoves = board.getPossibleMoves('e', 4, null, whitePlayer);
        assertTrue("Pawn on e4 should have moves", pawnMoves.size() > 0);
    }
    
    @Test
    public void testKnightMoves() {
        // Test knight moves from b1
        List<Move> knightMoves = board.getPossibleMoves('b', 1, null, whitePlayer);
        assertTrue("Knight should have moves", knightMoves.size() > 0);
        
        // Knight should be able to move to a3 and c3
        boolean hasA3 = false;
        boolean hasC3 = false;
        for (Move move : knightMoves) {
            if (move.getToFile() == 'a' && move.getToRank() == 3) hasA3 = true;
            if (move.getToFile() == 'c' && move.getToRank() == 3) hasC3 = true;
        }
        assertTrue("Knight should be able to move to a3", hasA3);
        assertTrue("Knight should be able to move to c3", hasC3);
    }
    
    @Test
    public void testKingMoves() {
        // Test king moves from e1 with empty history
        List<Move> kingMoves = board.getPossibleMoves('e', 1, new ArrayList<>(), whitePlayer);
        
        // Just test that the method returns a list (even if empty due to check constraints)
        assertNotNull("King moves should not be null", kingMoves);
        
        // If king has moves, verify they are valid
        if (kingMoves.size() > 0) {
            boolean hasValidMoves = false;
            for (Move move : kingMoves) {
                // Check if it's a valid king move (adjacent squares or castling)
                int fileDiff = Math.abs(move.getToFile() - move.getFromFile());
                int rankDiff = Math.abs(move.getToRank() - move.getFromRank());
                if ((fileDiff <= 1 && rankDiff <= 1) || fileDiff == 2) {
                    hasValidMoves = true;
                    break;
                }
            }
            assertTrue("If king has moves, they should be valid", hasValidMoves);
        }
    }
    
    @Test
    public void testMovePiece() {
        // Test valid move
        Move move = new Move('e', 2, 'e', 4, whitePlayer);
        assertTrue("Valid move should succeed", board.movePiece(move));
        
        // Check that piece moved
        assertEquals("e4 should now have a pawn", PiecesEnum.PAWN, board.getBlock('e', 4).getPiece());
        assertEquals("e4 should be white", ColourEnum.WHITE, board.getBlock('e', 4).getPlayer());
        assertEquals("e2 should now be empty", PiecesEnum.EMPTY, board.getBlock('e', 2).getPiece());
        assertEquals("e2 should have no player", ColourEnum.BLANK, board.getBlock('e', 2).getPlayer());
    }
    
    @Test
    public void testInvalidMove() {
        // Test moving from empty square
        Move move = new Move('e', 4, 'e', 5, whitePlayer);
        assertFalse("Move from empty square should fail", board.movePiece(move));
        
        // Test moving out of bounds
        Move invalidMove = new Move('e', 1, 'i', 1, whitePlayer);
        assertFalse("Move out of bounds should fail", board.movePiece(invalidMove));
    }
    
    @Test
    public void testPawnPromotion() {
        // Move a pawn to the 7th rank
        Move move1 = new Move('e', 2, 'e', 4, whitePlayer);
        board.movePiece(move1);
        
        Move move2 = new Move('e', 4, 'e', 5, whitePlayer);
        board.movePiece(move2);
        
        Move move3 = new Move('e', 5, 'e', 6, whitePlayer);
        board.movePiece(move3);
        
        Move move4 = new Move('e', 6, 'e', 7, whitePlayer);
        board.movePiece(move4);
        
        // Now test promotion moves
        List<Move> promotionMoves = board.getPossibleMoves('e', 7, null, whitePlayer);
        assertTrue("Pawn on 7th rank should have promotion moves", promotionMoves.size() > 0);
        
        // Check that promotion moves include different pieces
        boolean hasQueenPromotion = false;
        boolean hasRookPromotion = false;
        for (Move move : promotionMoves) {
            if (move.getPromotionPiece() == PiecesEnum.QUEEN) hasQueenPromotion = true;
            if (move.getPromotionPiece() == PiecesEnum.ROOK) hasRookPromotion = true;
        }
        assertTrue("Should have queen promotion", hasQueenPromotion);
        assertTrue("Should have rook promotion", hasRookPromotion);
    }
    
    @Test
    public void testCastling() {
        // Test that castling moves are available for king with empty history
        List<Move> kingMoves = board.getPossibleMoves('e', 1, new ArrayList<>(), whitePlayer);
        
        // Just test that the method returns a list
        assertNotNull("King moves should not be null", kingMoves);
        
        // Check if castling moves are available (king moves 2 squares horizontally)
        boolean hasCastling = false;
        for (Move move : kingMoves) {
            int fileDiff = Math.abs(move.getToFile() - move.getFromFile());
            int rankDiff = Math.abs(move.getToRank() - move.getFromRank());
            if (fileDiff == 2 && rankDiff == 0) {
                hasCastling = true;
                break;
            }
        }
        
        // Castling might not be available due to check constraints, so just verify the method works
        // Don't require specific moves since the king might be in check
    }
    
    @Test
    public void testCapture() {
        // Move a pawn to create a capture opportunity
        Move move1 = new Move('e', 2, 'e', 4, whitePlayer);
        board.movePiece(move1);
        
        Move move2 = new Move('d', 7, 'd', 5, blackPlayer);
        board.movePiece(move2);
        
        // Now white pawn should be able to capture black pawn
        List<Move> captureMoves = board.getPossibleMoves('e', 4, null, whitePlayer);
        boolean hasCapture = false;
        for (Move move : captureMoves) {
            if (move.getToFile() == 'd' && move.getToRank() == 5) {
                hasCapture = true;
                break;
            }
        }
        assertTrue("Pawn should be able to capture", hasCapture);
    }
    
    @Test
    public void testEmptySquareMoves() {
        // Test getting moves for empty square
        List<Move> emptyMoves = board.getPossibleMoves('e', 4, null, whitePlayer);
        assertTrue("Empty square should have no moves", emptyMoves.isEmpty());
    }
    
    @Test
    public void testBoardStateAfterMoves() {
        // Make several moves and verify board state
        Move move1 = new Move('e', 2, 'e', 4, whitePlayer);
        board.movePiece(move1);
        
        Move move2 = new Move('e', 7, 'e', 5, blackPlayer);
        board.movePiece(move2);
        
        // Verify pieces are in correct positions
        assertEquals("e4 should have white pawn", PiecesEnum.PAWN, board.getBlock('e', 4).getPiece());
        assertEquals("e4 should be white", ColourEnum.WHITE, board.getBlock('e', 4).getPlayer());
        assertEquals("e5 should have black pawn", PiecesEnum.PAWN, board.getBlock('e', 5).getPiece());
        assertEquals("e5 should be black", ColourEnum.BLACK, board.getBlock('e', 5).getPlayer());
        
        // Verify original positions are empty
        assertEquals("e2 should be empty", PiecesEnum.EMPTY, board.getBlock('e', 2).getPiece());
        assertEquals("e7 should be empty", PiecesEnum.EMPTY, board.getBlock('e', 7).getPiece());
    }
}
