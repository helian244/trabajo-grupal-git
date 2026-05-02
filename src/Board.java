import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Position;

public class Board {
    private final Piece[][] grid = new Piece[6][6];

    public Board() {
        setupInitialPosition();
    }

    // Copy constructor for simulation
    public Board(Board other) {
        for (int r = 0; r < 6; r++)
            for (int c = 0; c < 6; c++)
                grid[r][c] = other.grid[r][c];
    }

    private void setupInitialPosition() {
        // Black pieces - row 0 (top)
        grid[0][0] = new Piece(PieceType.ROOK, PieceColor.BLACK);
        grid[0][1] = new Piece(PieceType.QUEEN, PieceColor.BLACK);
        grid[0][2] = new Piece(PieceType.KING, PieceColor.BLACK);
        grid[0][3] = null;
        grid[0][4] = new Piece(PieceType.ROOK, PieceColor.BLACK);
        grid[0][5] = null;
        // Black pawns - row 1
        grid[1][0] = new Piece(PieceType.PAWN, PieceColor.BLACK);
        grid[1][5] = new Piece(PieceType.PAWN, PieceColor.BLACK);

        // White pieces - row 5 (bottom)
        grid[5][0] = null;
        grid[5][1] = new Piece(PieceType.ROOK, PieceColor.WHITE);
        grid[5][2] = new Piece(PieceType.KING, PieceColor.WHITE);
        grid[5][3] = new Piece(PieceType.QUEEN, PieceColor.WHITE);
        grid[5][4] = new Piece(PieceType.ROOK, PieceColor.WHITE);
        grid[5][5] = null;
        // White pawns - row 4
        grid[4][0] = new Piece(PieceType.PAWN, PieceColor.WHITE);
        grid[4][5] = new Piece(PieceType.PAWN, PieceColor.WHITE);
    }

    public Piece getPiece(int row, int col) {
        return grid[row][col];
    }

    public Piece getPiece(Position pos) {
        return grid[pos.row][pos.col];
    }

    public void setPiece(Position pos, Piece p) {
        grid[pos.row][pos.col] = p;
    }

    public void clearCell(Position pos) {
        grid[pos.row][pos.col] = null;
    }

    // Returns all legal moves for a piece at pos (filters moves leaving own king in
    // check)
    public List<Position> getLegalMoves(Position pos) {
        Piece piece = getPiece(pos);
        if (piece == null)
            return List.of();

        List<Position> raw = getRawMoves(pos, piece);
        List<Position> legal = new ArrayList<>();
        for (Position to : raw) {
            if (!moveLeavesKingInCheck(pos, to, piece.getColor())) {
                legal.add(to);
            }
        }
        return legal;
    }

    // Raw moves without check filtering
    private List<Position> getRawMoves(Position pos, Piece piece) {
        return switch (piece.getType()) {
            case KING -> getKingMoves(pos, piece.getColor());
            case QUEEN -> getQueenMoves(pos, piece.getColor());
            case ROOK -> getRookMoves(pos, piece.getColor());
            case PAWN -> getPawnMoves(pos, piece.getColor());
        };
    }

    private List<Position> getKingMoves(Position pos, PieceColor color) {
        List<Position> moves = new ArrayList<>();
        int[] d = { -1, 0, 1 };
        for (int dr : d)
            for (int dc : d) {
                if (dr == 0 && dc == 0)
                    continue;
                Position to = new Position(pos.row + dr, pos.col + dc);
                if (to.isValid() && !isFriendly(to, color))
                    moves.add(to);
            }
        return moves;
    }

    private List<Position> getRookMoves(Position pos, PieceColor color) {
        List<Position> moves = new ArrayList<>();
        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] d : dirs) {
            int r = pos.row + d[0], c = pos.col + d[1];
            while (r >= 0 && r < 6 && c >= 0 && c < 6) {
                Position to = new Position(r, c);
                if (isFriendly(to, color))
                    break;
                moves.add(to);
                if (isEnemy(to, color))
                    break;
                r += d[0];
                c += d[1];
            }
        }
        return moves;
    }

    private List<Position> getQueenMoves(Position pos, PieceColor color) {
        List<Position> moves = new ArrayList<>();
        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
        for (int[] d : dirs) {
            int r = pos.row + d[0], c = pos.col + d[1];
            while (r >= 0 && r < 6 && c >= 0 && c < 6) {
                Position to = new Position(r, c);
                if (isFriendly(to, color))
                    break;
                moves.add(to);
                if (isEnemy(to, color))
                    break;
                r += d[0];
                c += d[1];
            }
        }
        return moves;
    }

    private List<Position> getPawnMoves(Position pos, PieceColor color) {
        List<Position> moves = new ArrayList<>();
        int dir = (color == PieceColor.WHITE) ? -1 : 1; // white moves up, black moves down

        // Forward step
        Position fwd = new Position(pos.row + dir, pos.col);
        if (fwd.isValid() && getPiece(fwd) == null) {
            moves.add(fwd);
            // Double step from starting row
            int startRow = (color == PieceColor.WHITE) ? 4 : 1;
            if (pos.row == startRow) {
                Position dbl = new Position(pos.row + 2 * dir, pos.col);
                if (dbl.isValid() && getPiece(dbl) == null)
                    moves.add(dbl);
            }
        }
        // Diagonal captures
        for (int dc : new int[] { -1, 1 }) {
            Position cap = new Position(pos.row + dir, pos.col + dc);
            if (cap.isValid() && isEnemy(cap, color))
                moves.add(cap);
        }
        return moves;
    }

    private boolean isFriendly(Position pos, PieceColor color) {
        Piece p = getPiece(pos);
        return p != null && p.getColor() == color;
    }

    private boolean isEnemy(Position pos, PieceColor color) {
        Piece p = getPiece(pos);
        return p != null && p.getColor() != color;
    }

    public boolean movePiece(Position from, Position to) {
        Piece piece = getPiece(from);
        if (piece == null)
            return false;
        List<Position> legal = getLegalMoves(from);
        if (!legal.contains(to))
            return false;
        setPiece(to, piece);
        clearCell(from);
        return true;
    }

    // Simulate move and check if own king is in check
    private boolean moveLeavesKingInCheck(Position from, Position to, PieceColor color) {
        Board sim = new Board(this);
        sim.setPiece(to, sim.getPiece(from));
        sim.clearCell(from);
        return sim.isKingInCheck(color);
    }

    public boolean isKingInCheck(PieceColor color) {
        Position kingPos = findKing(color);
        if (kingPos == null)
            return false;
        return isSquareAttackedBy(kingPos, color.opposite());
    }

    public boolean isCheckmate(PieceColor color) {
        if (!isKingInCheck(color))
            return false;
        return hasNoLegalMoves(color);
    }

    public boolean isStalemate(PieceColor color) {
        if (isKingInCheck(color))
            return false;
        return hasNoLegalMoves(color);
    }

    private boolean hasNoLegalMoves(PieceColor color) {
        for (int r = 0; r < 6; r++)
            for (int c = 0; c < 6; c++) {
                Piece p = grid[r][c];
                if (p != null && p.getColor() == color) {
                    if (!getLegalMoves(new Position(r, c)).isEmpty())
                        return false;
                }
            }
        return true;
    }

    public boolean isSquareAttackedBy(Position target, PieceColor attacker) {
        for (int r = 0; r < 6; r++)
            for (int c = 0; c < 6; c++) {
                Piece p = grid[r][c];
                if (p != null && p.getColor() == attacker) {
                    List<Position> raw = getRawMoves(new Position(r, c), p);
                    if (raw.contains(target)) return true;
                }
            }
        return false;
    }

    public Position findKing(PieceColor color) {
        for (int r = 0; r < 6; r++)
            for (int c = 0; c < 6; c++)
                if (grid[r][c] != null
                    && grid[r][c].getType() == PieceType.KING
                    && grid[r][c].getColor() == color)
                    return new Position(r, c);
        return null;
    }

    // Check if a pawn reached the opposite end (for promotion)
    public Position getPawnPromotion(PieceColor color) {
        int row = (color == PieceColor.WHITE) ? 0 : 5;
        for (int c = 0; c < 6; c++) {
            Piece p = grid[row][c];
            if (p != null && p.getType() == PieceType.PAWN && p.getColor() == color)
                return new Position(row, c);
        }
        return null;
    }

    public void promotePawn(Position pos, PieceColor color) {
        grid[pos.row][pos.col] = new Piece(PieceType.QUEEN, color);
    }

}
