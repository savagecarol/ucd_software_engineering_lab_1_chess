package com.chess.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PiecesEnumTest {
    
    @Test
    public void testPieceValues() {
        assertEquals("EMPTY should have value 0", 0, PiecesEnum.EMPTY.getValue());
        assertEquals("PAWN should have value 1", 1, PiecesEnum.PAWN.getValue());
        assertEquals("ROOK should have value 5", 5, PiecesEnum.ROOK.getValue());
        assertEquals("KNIGHT should have value 3", 3, PiecesEnum.KNIGHT.getValue());
        assertEquals("BISHOP should have value 3", 3, PiecesEnum.BISHOP.getValue());
        assertEquals("QUEEN should have value 9", 9, PiecesEnum.QUEEN.getValue());
        assertEquals("KING should have value 10", 10, PiecesEnum.KING.getValue());
    }
    
    @Test
    public void testPieceSymbols() {
        assertEquals("EMPTY should have symbol '#'", "#", PiecesEnum.EMPTY.getSymbol());
        assertEquals("PAWN should have symbol 'P'", "P", PiecesEnum.PAWN.getSymbol());
        assertEquals("ROOK should have symbol 'R'", "R", PiecesEnum.ROOK.getSymbol());
        assertEquals("KNIGHT should have symbol 'N'", "N", PiecesEnum.KNIGHT.getSymbol());
        assertEquals("BISHOP should have symbol 'B'", "B", PiecesEnum.BISHOP.getSymbol());
        assertEquals("QUEEN should have symbol 'Q'", "Q", PiecesEnum.QUEEN.getSymbol());
        assertEquals("KING should have symbol 'K'", "K", PiecesEnum.KING.getSymbol());
    }
    
    @Test
    public void testAllPiecesExist() {
        // Test that all expected pieces are defined
        PiecesEnum[] expectedPieces = {
            PiecesEnum.EMPTY, PiecesEnum.PAWN, PiecesEnum.ROOK,
            PiecesEnum.KNIGHT, PiecesEnum.BISHOP, PiecesEnum.QUEEN, PiecesEnum.KING
        };
        
        for (PiecesEnum piece : expectedPieces) {
            assertNotNull("Piece " + piece + " should not be null", piece);
        }
    }
    
    @Test
    public void testValueOrdering() {
        // Test that piece values follow expected ordering
        assertTrue("EMPTY should have lowest value", PiecesEnum.EMPTY.getValue() < PiecesEnum.PAWN.getValue());
        assertTrue("PAWN should have lower value than ROOK", PiecesEnum.PAWN.getValue() < PiecesEnum.ROOK.getValue());
        assertTrue("KNIGHT should have same value as BISHOP", PiecesEnum.KNIGHT.getValue() == PiecesEnum.BISHOP.getValue());
        assertTrue("BISHOP should have lower value than QUEEN", PiecesEnum.BISHOP.getValue() < PiecesEnum.QUEEN.getValue());
        assertTrue("QUEEN should have lower value than KING", PiecesEnum.QUEEN.getValue() < PiecesEnum.KING.getValue());
    }
    
    @Test
    public void testSymbolUniqueness() {
        // Test that all symbols are unique
        String[] symbols = {
            PiecesEnum.EMPTY.getSymbol(),
            PiecesEnum.PAWN.getSymbol(),
            PiecesEnum.ROOK.getSymbol(),
            PiecesEnum.KNIGHT.getSymbol(),
            PiecesEnum.BISHOP.getSymbol(),
            PiecesEnum.QUEEN.getSymbol(),
            PiecesEnum.KING.getSymbol()
        };
        
        for (int i = 0; i < symbols.length; i++) {
            for (int j = i + 1; j < symbols.length; j++) {
                assertNotEquals("Symbols should be unique: " + symbols[i] + " vs " + symbols[j], 
                               symbols[i], symbols[j]);
            }
        }
    }
    
    @Test
    public void testSymbolLength() {
        // Test that all symbols are single characters
        for (PiecesEnum piece : PiecesEnum.values()) {
            assertEquals("Symbol should be single character for " + piece, 
                        1, piece.getSymbol().length());
        }
    }
    
    @Test
    public void testEnumValues() {
        // Test that enum values() method works correctly
        PiecesEnum[] pieces = PiecesEnum.values();
        assertEquals("Should have 7 piece types", 7, pieces.length);
        
        // Test that all expected pieces are in the values array
        assertTrue("Should contain EMPTY", java.util.Arrays.asList(pieces).contains(PiecesEnum.EMPTY));
        assertTrue("Should contain PAWN", java.util.Arrays.asList(pieces).contains(PiecesEnum.PAWN));
        assertTrue("Should contain ROOK", java.util.Arrays.asList(pieces).contains(PiecesEnum.ROOK));
        assertTrue("Should contain KNIGHT", java.util.Arrays.asList(pieces).contains(PiecesEnum.KNIGHT));
        assertTrue("Should contain BISHOP", java.util.Arrays.asList(pieces).contains(PiecesEnum.BISHOP));
        assertTrue("Should contain QUEEN", java.util.Arrays.asList(pieces).contains(PiecesEnum.QUEEN));
        assertTrue("Should contain KING", java.util.Arrays.asList(pieces).contains(PiecesEnum.KING));
    }
}
