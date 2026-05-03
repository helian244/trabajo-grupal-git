
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BoardPanel extends JPanel {
    private static final int CELL_SIZE = 90;
    private static final int BOARD_SIZE = 6 * CELL_SIZE;

    // Color palette — dark wood + warm cream theme
    private static final Color LIGHT_SQUARE = new Color(0xF0D9B5);
    private static final Color DARK_SQUARE = new Color(0xB58863);
    private static final Color SELECTED_COLOR = new Color(0x7FC97F, true);
    private static final Color HINT_COLOR = new Color(0x6BAF6B, true);
    private static final Color CHECK_COLOR = new Color(0xB4E74C3C, true);
    private static final Color LAST_MOVE_FROM = new Color(0x8CF6F669, true);
    private static final Color LAST_MOVE_TO = new Color(0x64F6F669, true);
    private static final Color COORD_COLOR = new Color(0x7A5230);

    private final GameState gameState;
    private Position selected;
    private List<Position> legalMoves;
    private Position lastFrom, lastTo;
    private final Font pieceFont;

    public BoardPanel(GameState gameState) {
        this.gameState = gameState;
        setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Load a system font that renders chess symbols well
        pieceFont = new Font("Segoe UI Symbol", Font.PLAIN, 58);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    private void handleClick(int px, int py) {
        if (gameState.getStatus() == GameState.Status.CHECKMATE ||
                gameState.getStatus() == GameState.Status.STALEMATE)
            return;

        int col = px / CELL_SIZE;
        int row = py / CELL_SIZE;
        if (col < 0 || col >= 6 || row < 0 || row >= 6)
            return;
        Position clicked = new Position(row, col);

        if (selected == null) {
            // Select a piece
            Piece piece = gameState.getBoard().getPiece(clicked);
            if (piece != null && piece.getColor() == gameState.getCurrentTurn()) {
                selected = clicked;
                legalMoves = gameState.getBoard().getLegalMoves(selected);
            }
        } else {
            if (clicked.equals(selected)) {
                // Deselect
                selected = null;
                legalMoves = null;
            } else if (legalMoves != null && legalMoves.contains(clicked)) {
                // Execute move
                lastFrom = selected;
                lastTo = clicked;
                gameState.tryMove(selected, clicked);
                selected = null;
                legalMoves = null;
                notifyParent();
            } else {
                // Try selecting another piece
                Piece piece = gameState.getBoard().getPiece(clicked);
                if (piece != null && piece.getColor() == gameState.getCurrentTurn()) {
                    selected = clicked;
                    legalMoves = gameState.getBoard().getLegalMoves(selected);
                } else {
                    selected = null;
                    legalMoves = null;
                }
            }
        }
        repaint();
    }

}