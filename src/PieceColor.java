

/**
 * Represents the color of a chess piece.
 * Used to determine turn order and piece ownership.
 * 
 */
public enum PieceColor {
    /** White pieces (usually move first) */
    WHITE,
    
    /** Black pieces */
    BLACK;

    /**
     * Returns the opposite color.
     * Used when switching turns after a move.
     * 
     * @return BLACK if current is WHITE, WHITE if current is BLACK
     */
    public PieceColor opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
}
