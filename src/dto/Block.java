package dto;

import enums.ColourEnum;
import enums.PiecesEnum;

public class Block {
    private PiecesEnum piece;
    private ColourEnum player;
    private ColourEnum colour;      
    private char file;  
    private int rank;   

    public Block(PiecesEnum piece, ColourEnum colour, ColourEnum player, char file, int rank) {
        this.piece = piece;
        this.colour = colour;
        this.player = player;
        this.file = file;
        this.rank = rank;
    }

    public char getFile() { return file; }
    public int getRank() { return rank; }
    public PiecesEnum getPiece() { return piece; }
    public void setPiece(PiecesEnum piece) { this.piece = piece; }
    public ColourEnum getPlayer() { return player; }
    public void setPlayer(ColourEnum player) { this.player = player; }
    public ColourEnum getColour() { return colour; }
    public void setColour(ColourEnum colour) { this.colour = colour; }
}
