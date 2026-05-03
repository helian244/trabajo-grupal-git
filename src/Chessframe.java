
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
}