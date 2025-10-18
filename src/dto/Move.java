package dto;

import enums.PiecesEnum;

public class Move {
    private final char fromFile;
    private final int fromRank;
    private final char toFile;
    private final int toRank;
    private final Player player;
    private final PiecesEnum promotionPiece;

    public Move(char fromFile, int fromRank, char toFile, int toRank, Player player) {
        this.fromFile = fromFile;
        this.fromRank = fromRank;
        this.toFile = toFile;
        this.toRank = toRank;
        this.player = player;
        this.promotionPiece = null;
    }

    public Move(char fromFile, int fromRank, char toFile, int toRank, Player player, PiecesEnum promotionPiece) {
        this.fromFile = fromFile;
        this.fromRank = fromRank;
        this.toFile = toFile;
        this.toRank = toRank;
        this.player = player;
        this.promotionPiece = promotionPiece;
    }

    public char getFromFile() { return fromFile; }
    public int getFromRank() { return fromRank; }
    public char getToFile() { return toFile; }
    public int getToRank() { return toRank; }
    public Player getPlayer() { return player; }
    public PiecesEnum getPromotionPiece() { return promotionPiece; }

    @Override
    public String toString() {
        String moveStr = String.format("%s: %c%d -> %c%d",
                player.getName(), fromFile, fromRank, toFile, toRank);
        if (promotionPiece != null) {
            moveStr += "=" + promotionPiece.getSymbol();
        }
        return moveStr;
    }
}
