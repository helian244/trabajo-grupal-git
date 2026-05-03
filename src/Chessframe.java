
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ChessFrame extends JFrame {

    private GameState gameState;
    private BoardPanel boardPanel;
    private JLabel statusLabel;
    private JLabel turnIndicator;
    private JLabel moveCountLabel;
    private JTextArea moveLog;
    private final List<String> moveHistory = new ArrayList<>();

    public ChessFrame() {
        setTitle("Ajedrez 6×6 — Mini Chess");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initGame();
        buildUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initGame() {
        gameState = new GameState();
    }

    private void buildUI() {
        // Root panel with dark background
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(new Color(0x2C1810));
        setContentPane(root);

        // ---- HEADER ----
        JPanel header = buildHeader();
        root.add(header, BorderLayout.NORTH);

        // ---- CENTER: board + sidebar ----
        JPanel center = new JPanel(new BorderLayout(16, 0));
        center.setBackground(new Color(0x2C1810));
        center.setBorder(new EmptyBorder(12, 16, 12, 16));

        // Board with raised border effect
        boardPanel = new BoardPanel(gameState);
        JPanel boardWrapper = new JPanel(new BorderLayout());
        boardWrapper.setBackground(new Color(0x6B4423));
        boardWrapper.setBorder(new CompoundBorder(
                new EmptyBorder(6, 6, 6, 6),
                BorderFactory.createLineBorder(new Color(0x8B5E3C), 4)));
        boardWrapper.add(boardPanel, BorderLayout.CENTER);
        center.add(boardWrapper, BorderLayout.CENTER);

        // Sidebar
        JPanel sidebar = buildSidebar();
        center.add(sidebar, BorderLayout.EAST);

        root.add(center, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0x1A0D07));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("AJEDREZ  6×6");
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(new Color(0xD4A85A));
        header.add(title, BorderLayout.WEST);

        statusLabel = new JLabel("Blancas al turno");
        statusLabel.setFont(new Font("Georgia", Font.ITALIC, 15));
        statusLabel.setForeground(new Color(0xE8D5B0));
        header.add(statusLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(0x1E0E08));
        sidebar.setBorder(new EmptyBorder(0, 10, 0, 0));
        sidebar.setPreferredSize(new Dimension(200, 0));

        // Turn indicator card
        sidebar.add(buildTurnCard());
        sidebar.add(Box.createVerticalStrut(14));

        // Stats card
        sidebar.add(buildStatsCard());
        sidebar.add(Box.createVerticalStrut(14));

        // Move log card
        sidebar.add(buildMoveLogCard());
        sidebar.add(Box.createVerticalStrut(14));

        // Buttons
        sidebar.add(buildButtons());

        return sidebar;
    }

    private JPanel buildTurnCard() {
        JPanel card = createCard("TURNO");

        turnIndicator = new JLabel(" ? Blancas");
        turnIndicator.setFont(new Font("Georgia", Font.BOLD, 18));
        turnIndicator.setForeground(new Color(0xFFFDE7));
        turnIndicator.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(turnIndicator);

        return card;
    }

    private JPanel buildStatsCard() {
        JPanel card = createCard("ESTAD ?STICAS");

        moveCountLabel = new JLabel("Movimientos: 0");
        moveCountLabel.setFont(new Font("Georgia", Font.PLAIN, 13));
        moveCountLabel.setForeground(new Color(0xB8945A));
        moveCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(moveCountLabel);

        return card;
    }

    private JPanel buildMoveLogCard() {
        JPanel card = createCard("HISTORIAL");
        card.setPreferredSize(new Dimension(190, 260));

        moveLog = new JTextArea();
        moveLog.setEditable(false);
        moveLog.setBackground(new Color(0x0E0604));
        moveLog.setForeground(new Color(0xC8A870));
        moveLog.setFont(new Font("Courier New", Font.PLAIN, 12));
        moveLog.setBorder(new EmptyBorder(4, 6, 4, 4));

        JScrollPane scroll = new JScrollPane(moveLog);
        scroll.setPreferredSize(new Dimension(180, 180));
        scroll.setMaximumSize(new Dimension(190, 200));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0x5A3520)));
        scroll.setBackground(new Color(0x0E0604));
        card.add(scroll);

        return card;
    }

    private JPanel buildButtons() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 8));
        panel.setBackground(new Color(0x1E0E08));
        panel.setMaximumSize(new Dimension(190, 90));

        JButton newGame = styledButton("Nueva partida", new Color(0x4A7C59));
        JButton rules = styledButton("Reglas", new Color(0x5A6E8A));

        newGame.addActionListener(e -> restartGame());
        rules.addActionListener(e -> showRules());

        panel.add(newGame);
        panel.add(rules);
        return panel;
    }

    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(0x160905));
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(0x6B4423), 1),
                new EmptyBorder(8, 10, 10, 10)));
        card.setMaximumSize(new Dimension(200, 300));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Georgia", Font.BOLD, 11));
        lbl.setForeground(new Color(0x8B6939));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lbl);
        card.add(Box.createVerticalStrut(8));
        return card;
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() : getModel().isRollover() ? bg.brighter() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Georgia", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(185, 36));
        return btn;
    }

    public void onMoveExecuted() {
        // Log the move
        int moveNum = gameState.getMoveCount();
        PieceColor moved = gameState.getCurrentTurn().opposite(); // already switched
        String label = (moved == PieceColor.WHITE ? "B" : "N") + moveNum + ".";
        moveHistory.add(label);
        moveLog.setText(String.join("\n", moveHistory));

        // Scroll to bottom
        moveLog.setCaretPosition(moveLog.getDocument().getLength());

        updateSidebar();
        checkGameOver();
    }

    private void updateSidebar() {
        GameState.Status status = gameState.getStatus();
        PieceColor turn = gameState.getCurrentTurn();

        // Turn indicator
        boolean isWhite = turn == PieceColor.WHITE;
        turnIndicator.setText(isWhite ? " ? Blancas" : " ? Negras");
        turnIndicator.setForeground(isWhite ? new Color(0xFFFDE7) : new Color(0x333333));
        // Always visible on dark bg
        turnIndicator.setForeground(isWhite ? new Color(0xFFF8E1) : new Color(0xAAAAAA));

        // Status in header
        statusLabel.setText(gameState.getStatusMessage());
        statusLabel.setForeground(
                status == GameState.Status.CHECK ? new Color(0xFF6B6B)
                        : status == GameState.Status.CHECKMATE ? new Color(0xFF4444)
                                : status == GameState.Status.STALEMATE ? new Color(0xFFD700) : new Color(0xE8D5B0));

        moveCountLabel.setText("Movimientos: " + gameState.getMoveCount());

        boardPanel.repaint();
    }

    private void checkGameOver() {
        GameState.Status status = gameState.getStatus();
        if (status == GameState.Status.CHECKMATE || status == GameState.Status.STALEMATE) {
            String msg = status == GameState.Status.CHECKMATE
                    ? "¡Jaque Mate!\n" + (gameState.getWinner() == PieceColor.WHITE ? "Blancas" : "Negras")
                            + " ganan  ? "
                    : "¡Tablas! — Ahogado";
            int opt = JOptionPane.showOptionDialog(this, msg, "Fin del juego",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, new String[] { "Nueva partida", "Salir" }, "Nueva partida");
            if (opt == 0)
                restartGame();
            else
                System.exit(0);
        }
    }

    private void restartGame() {
        moveHistory.clear();
        moveLog.setText("");
        getContentPane().removeAll();
        initGame();
        buildUI();
        revalidate();
        repaint();
    }

    private void showRules() {
        String rules = """
                ♟ AJEDREZ 6×6 — REGLAS

                Tablero: 6×6 casillas

                Piezas por bando:
                  ♔/♚  Rey   — 1
                  ♕/♛  Reina — 1
                  ♖/♜  Torre — 2
                  ♙/♟  Peón  — 2

                Movimientos:
                  Rey   → 1 casilla en cualquier dirección
                  Reina → n casillas en línea o diagonal
                  Torre → n casillas en línea recta
                  Peón  → avanza 1 (2 en salida); captura
                          en diagonal; ¡promueve a Reina
                          al llegar al extremo!

                Sin enroque.

                Gana quien dé Jaque Mate al rey rival.
                Tablas si el rey no tiene movimientos
                legales pero no está en jaque (ahogado).
                """;
        JOptionPane.showMessageDialog(this, rules, "Reglas del juego",
                JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new ChessFrame();
        });
    }

}