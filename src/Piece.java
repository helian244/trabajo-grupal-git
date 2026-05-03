

/**
 * Represents a chess piece with a type and color.
 * Immutable class - once created, piece cannot change type or color.
 * 
 */
public class Piece {
    private final PieceType type;
    private final PieceColor color;

    /**
     * Constructs a new Piece.
     * @param type The type of piece (KING, QUEEN, ROOK, PAWN)
     * @param color The color of piece (WHITE or BLACK)
     */
    public Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
    }

    /** @return The type of this piece */
    public PieceType getType() {
        return type;
    }

    /** @return The color of this piece */
    public PieceColor getColor() {
        return color;
    }

    /**
     * Returns a Unicode symbol representing this piece.
     * White pieces: ♔, ♕, ♖, ♙
     * Black pieces: ♚, ♛, ♜, ♟
     * 
     * @return Unicode chess symbol as a String
     */
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

    /**
     * Returns a string representation for debugging.
     * Example: "WHITE_KING", "BLACK_PAWN"
     * 
     * @return Color and type in format "COLOR_TYPE"
     */
    @Override
    public String toString() {
        return color + "_" + type;
    }
}