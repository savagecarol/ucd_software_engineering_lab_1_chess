package com.chess.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

import com.chess.enums.ColourEnum;
import com.chess.enums.PiecesEnum;

public class BlockTest {
    
    private Block block;
    
    @Before
    public void setUp() {
        block = new Block(PiecesEnum.PAWN, ColourEnum.WHITE, ColourEnum.WHITE, 'e', 4);
    }
    
    @Test
    public void testBlockCreation() {
        assertNotNull("Block should not be null", block);
        assertEquals("Piece should be PAWN", PiecesEnum.PAWN, block.getPiece());
        assertEquals("Player should be WHITE", ColourEnum.WHITE, block.getPlayer());
        assertEquals("Colour should be WHITE", ColourEnum.WHITE, block.getColour());
        assertEquals("File should be 'e'", 'e', block.getFile());
        assertEquals("Rank should be 4", 4, block.getRank());
    }
    
    @Test
    public void testEmptyBlock() {
        Block emptyBlock = new Block(PiecesEnum.EMPTY, ColourEnum.BLACK, ColourEnum.BLANK, 'a', 1);
        
        assertEquals("Piece should be EMPTY", PiecesEnum.EMPTY, emptyBlock.getPiece());
        assertEquals("Player should be BLANK", ColourEnum.BLANK, emptyBlock.getPlayer());
        assertEquals("Colour should be BLACK", ColourEnum.BLACK, emptyBlock.getColour());
        assertEquals("File should be 'a'", 'a', emptyBlock.getFile());
        assertEquals("Rank should be 1", 1, emptyBlock.getRank());
    }
    
    @Test
    public void testSetPiece() {
        block.setPiece(PiecesEnum.QUEEN);
        assertEquals("Piece should be QUEEN after setting", PiecesEnum.QUEEN, block.getPiece());
        
        block.setPiece(PiecesEnum.ROOK);
        assertEquals("Piece should be ROOK after setting", PiecesEnum.ROOK, block.getPiece());
        
        block.setPiece(PiecesEnum.EMPTY);
        assertEquals("Piece should be EMPTY after setting", PiecesEnum.EMPTY, block.getPiece());
    }
    
    @Test
    public void testSetPlayer() {
        block.setPlayer(ColourEnum.BLACK);
        assertEquals("Player should be BLACK after setting", ColourEnum.BLACK, block.getPlayer());
        
        block.setPlayer(ColourEnum.BLANK);
        assertEquals("Player should be BLANK after setting", ColourEnum.BLANK, block.getPlayer());
        
        block.setPlayer(ColourEnum.WHITE);
        assertEquals("Player should be WHITE after setting", ColourEnum.WHITE, block.getPlayer());
    }
    
    @Test
    public void testSetColour() {
        block.setColour(ColourEnum.BLACK);
        assertEquals("Colour should be BLACK after setting", ColourEnum.BLACK, block.getColour());
        
        block.setColour(ColourEnum.WHITE);
        assertEquals("Colour should be WHITE after setting", ColourEnum.WHITE, block.getColour());
    }
    
    @Test
    public void testAllPieces() {
        // Test setting all different pieces
        PiecesEnum[] pieces = {PiecesEnum.PAWN, PiecesEnum.ROOK, PiecesEnum.KNIGHT, 
                              PiecesEnum.BISHOP, PiecesEnum.QUEEN, PiecesEnum.KING, PiecesEnum.EMPTY};
        
        for (PiecesEnum piece : pieces) {
            block.setPiece(piece);
            assertEquals("Piece should be " + piece, piece, block.getPiece());
        }
    }
    
    @Test
    public void testAllPlayers() {
        // Test setting all different players
        ColourEnum[] players = {ColourEnum.WHITE, ColourEnum.BLACK, ColourEnum.BLANK};
        
        for (ColourEnum player : players) {
            block.setPlayer(player);
            assertEquals("Player should be " + player, player, block.getPlayer());
        }
    }
    
    @Test
    public void testAllColours() {
        // Test setting all different colours
        ColourEnum[] colours = {ColourEnum.WHITE, ColourEnum.BLACK};
        
        for (ColourEnum colour : colours) {
            block.setColour(colour);
            assertEquals("Colour should be " + colour, colour, block.getColour());
        }
    }
    
    @Test
    public void testDifferentPositions() {
        // Test different file and rank combinations
        char[] files = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int[] ranks = {1, 2, 3, 4, 5, 6, 7, 8};
        
        for (char file : files) {
            for (int rank : ranks) {
                Block testBlock = new Block(PiecesEnum.PAWN, ColourEnum.WHITE, ColourEnum.WHITE, file, rank);
                assertEquals("File should be " + file, file, testBlock.getFile());
                assertEquals("Rank should be " + rank, rank, testBlock.getRank());
            }
        }
    }
    
    @Test
    public void testGetters() {
        // Test all getters return the expected values
        assertEquals("getPiece() should return PAWN", PiecesEnum.PAWN, block.getPiece());
        assertEquals("getPlayer() should return WHITE", ColourEnum.WHITE, block.getPlayer());
        assertEquals("getColour() should return WHITE", ColourEnum.WHITE, block.getColour());
        assertEquals("getFile() should return 'e'", 'e', block.getFile());
        assertEquals("getRank() should return 4", 4, block.getRank());
    }
    
    @Test
    public void testBlockWithKing() {
        Block kingBlock = new Block(PiecesEnum.KING, ColourEnum.BLACK, ColourEnum.BLACK, 'e', 8);
        
        assertEquals("Piece should be KING", PiecesEnum.KING, kingBlock.getPiece());
        assertEquals("Player should be BLACK", ColourEnum.BLACK, kingBlock.getPlayer());
        assertEquals("Colour should be BLACK", ColourEnum.BLACK, kingBlock.getColour());
        assertEquals("File should be 'e'", 'e', kingBlock.getFile());
        assertEquals("Rank should be 8", 8, kingBlock.getRank());
    }
    
    @Test
    public void testBlockWithQueen() {
        Block queenBlock = new Block(PiecesEnum.QUEEN, ColourEnum.WHITE, ColourEnum.WHITE, 'd', 1);
        
        assertEquals("Piece should be QUEEN", PiecesEnum.QUEEN, queenBlock.getPiece());
        assertEquals("Player should be WHITE", ColourEnum.WHITE, queenBlock.getPlayer());
        assertEquals("Colour should be WHITE", ColourEnum.WHITE, queenBlock.getColour());
        assertEquals("File should be 'd'", 'd', queenBlock.getFile());
        assertEquals("Rank should be 1", 1, queenBlock.getRank());
    }
}
