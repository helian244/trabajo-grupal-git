
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

    private void notifyParent() {
        Container parent = getParent();
        while (parent != null) {
            if (parent instanceof ChessFrame cf) {
                cf.onMoveExecuted();
                break;
            }
            parent = parent.getParent();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Board board = gameState.getBoard();
        Position whiteKing = board.findKing(PieceColor.WHITE);
        Position blackKing = board.findKing(PieceColor.BLACK);

        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                int x = c * CELL_SIZE;
                int y = r * CELL_SIZE;
                Position pos = new Position(r, c);

                // Base square color
                Color baseColor = ((r + c) % 2 == 0) ? LIGHT_SQUARE : DARK_SQUARE;
                g2.setColor(baseColor);
                g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // Last move highlight
                if (pos.equals(lastFrom)) {
                    g2.setColor(LAST_MOVE_FROM);
                    g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                } else if (pos.equals(lastTo)) {
                    g2.setColor(LAST_MOVE_TO);
                    g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }

                // Selected highlight
                if (pos.equals(selected)) {
                    g2.setColor(SELECTED_COLOR);
                    g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }

                // King in check highlight
                boolean whiteInCheck = gameState.getBoard().isKingInCheck(PieceColor.WHITE);
                boolean blackInCheck = gameState.getBoard().isKingInCheck(PieceColor.BLACK);
                if ((pos.equals(whiteKing) && whiteInCheck) || (pos.equals(blackKing) && blackInCheck)) {
                    drawCheckHighlight(g2, x, y);
                }

                // Legal move dots
                if (legalMoves != null && legalMoves.contains(pos)) {
                    Piece target = board.getPiece(pos);
                    if (target != null) {
                        // Capture: ring around the square
                        g2.setColor(HINT_COLOR);
                        g2.setStroke(new BasicStroke(4f));
                        g2.drawRect(x + 3, y + 3, CELL_SIZE - 6, CELL_SIZE - 6);
                        g2.setStroke(new BasicStroke(1f));
                    } else {
                        // Empty: small circle
                        g2.setColor(HINT_COLOR);
                        int dotSize = 28;
                        g2.fillOval(x + (CELL_SIZE - dotSize) / 2, y + (CELL_SIZE - dotSize) / 2, dotSize, dotSize);
                    }
                }

                // Coordinate labels (column letters at bottom row, row numbers at left column)
                drawCoordinates(g2, r, c, x, y);

                // Draw piece
                Piece piece = board.getPiece(r, c);
                if (piece != null)
                    drawPiece(g2, piece, x, y);
            }
        }

        // Board border
        g2.setColor(new Color(0x6B4423));
        g2.setStroke(new BasicStroke(3f));
        g2.drawRect(1, 1, BOARD_SIZE - 2, BOARD_SIZE - 2);
    }

    private void drawCheckHighlight(Graphics2D g2, int x, int y) {
        // Red radial glow
        RadialGradientPaint rgp = new RadialGradientPaint(
                new Point(x + CELL_SIZE / 2, y + CELL_SIZE / 2),
                CELL_SIZE / 2f,
                new float[] { 0f, 1f },
                new Color[] { CHECK_COLOR, new Color(0x00E74C3C, true) });
        g2.setPaint(rgp);
        g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        g2.setPaint(null);
    }

    private void drawCoordinates(Graphics2D g2, int r, int c, int x, int y) {
        g2.setFont(new Font("Georgia", Font.BOLD, 11));
        g2.setColor(COORD_COLOR);
        if (c == 0) {
            // Row number
            g2.drawString(String.valueOf(6 - r), x + 3, y + 14);
        }
        if (r == 5) {
            // Column letter
            g2.drawString(String.valueOf((char) ('a' + c)), x + CELL_SIZE - 12, y + CELL_SIZE - 4);
        }
    }

    private void drawPiece(Graphics2D g2, Piece piece, int x, int y) {
        String symbol = piece.getSymbol();
        g2.setFont(pieceFont);
        FontMetrics fm = g2.getFontMetrics();
        int sw = fm.stringWidth(symbol);
        int sh = fm.getAscent();
        int tx = x + (CELL_SIZE - sw) / 2;
        int ty = y + (CELL_SIZE + sh) / 2 - 8;

        // Shadow for depth
        g2.setColor(new Color(0, 0, 0, 60));
        g2.drawString(symbol, tx + 2, ty + 2);

        // Piece color
        g2.setColor(piece.getColor() == PieceColor.WHITE
                ? new Color(0xFFFDE7)
                : new Color(0x1A1A1A));
        g2.drawString(symbol, tx, ty);
    }
}