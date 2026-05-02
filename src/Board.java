import java.util.ArrayList;
import java.util.List;

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
        grid[0][0] = new Piece(PieceType.ROOK,  PieceColor.BLACK);
        grid[0][1] = new Piece(PieceType.QUEEN, PieceColor.BLACK);
        grid[0][2] = new Piece(PieceType.KING,  PieceColor.BLACK);
        grid[0][3] = null;
        grid[0][4] = new Piece(PieceType.ROOK,  PieceColor.BLACK);
        grid[0][5] = null;       
        // Black pawns - row 1
        grid[1][0] = new Piece(PieceType.PAWN, PieceColor.BLACK);
        grid[1][5] = new Piece(PieceType.PAWN, PieceColor.BLACK); 
    }
