

import java.util.Objects;

/**
 * Represents a position on the 6x6 chessboard.
 * Positions use row (0-5) and column (0-5) coordinates.
 * 
 */
public class Position {
    /** Row index: 0 = top (row 6), 5 = bottom (row 1) */
    public final int row;
    
    /** Column index: 0 = file a, 5 = file f */
    public final int col;

    /**
     * Constructs a new Position.
     * @param row Row index (0-5)
     * @param col Column index (0-5)
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Checks if this position is within the 6x6 board bounds.
     * @return true if row and col are between 0 and 5 inclusive
     */
    public boolean isValid() {
        return row >= 0 && row < 6 && col >= 0 && col < 6;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position pos = (Position) o;
        return row == pos.row && col == pos.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Converts position to chess notation.
     * Example: row=5, col=0 returns "a1"; row=0, col=5 returns "f6"
     * @return Chess notation string (e.g., "a1", "f6")
     */
    @Override
    public String toString() {
        char file = (char) ('a' + col);
        int rank = 6 - row;
        return "" + file + rank;
    }
}
