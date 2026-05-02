

public class Piece {
    private final PieceType type;
    private final PieceColor color;

    public Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
    }

    public PieceType getType() {
        return type;
    }

    public PieceColor getColor() {
        return color;
    }

    public String getSymbol() {
        if (color == PieceColor.WHITE) {
            return switch (type) {
                case KING -> "♔";
                case QUEEN -> "♕";
                case ROOK -> "♖";
                case PAWN -> "♙";
            };
        } else {
            return switch (type) {
                case KING -> "♚";
                case QUEEN -> "♛";
                case ROOK -> "♜";
                case PAWN -> "♟";
            };
        }
    }

    @Override
    public String toString() {
        return color + "_" + type;
    }
}



