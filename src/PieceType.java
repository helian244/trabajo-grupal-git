

/**
 * Represents the type of a chess piece.
 * Each type has different movement rules.
 * 
 */
public enum PieceType {
    /** King - moves 1 square in any direction */
    KING,
    
    /** Queen - moves any number of squares in 8 directions */
    QUEEN,
    
    /** Rook - moves any number of squares horizontally/vertically */
    ROOK,
    
    /** Pawn - moves forward 1 (or 2 from start), captures diagonally */
    PAWN
}
