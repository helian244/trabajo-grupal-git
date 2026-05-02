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
