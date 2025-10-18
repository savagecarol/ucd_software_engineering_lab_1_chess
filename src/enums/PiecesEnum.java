package enums;

public enum PiecesEnum {
    EMPTY(0, "#"),
    PAWN(1, "P"),
    ROOK(5, "R"),
    KNIGHT(3, "N"),
    BISHOP(3, "B"),
    QUEEN(9, "Q"),
    KING(10, "K");

    private final int value;
    private final String symbol;

    PiecesEnum(int value, String symbol) {
        this.value = value;
        this.symbol = symbol;
    }

    public int getValue() {
        return value;
    }

    public String getSymbol() {
        return symbol;
    }
}
