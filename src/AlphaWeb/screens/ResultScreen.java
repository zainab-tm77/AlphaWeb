package AlphaWeb.screens;

import AlphaWeb.core.HighScore;
import AlphaWeb.utils.FontManager;
import AlphaWeb.utils.MusicPlayer;
import AlphaWeb.utils.NavigationManager;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ResultScreen extends JFrame {
    private final int score, totalTime;
    private final String level, result, playerName, theme, category; // Added category field
    private final boolean hasCompletedMaster;

    public ResultScreen(int score, String level, String result, List<HighScore.ScoreEntry> highScores, String playerName, int totalTime, boolean hasCompletedMaster, String theme, String category) {
        this.score = score;
        this.level = level;
        this.result = result;
        this.playerName = playerName;
        this.totalTime = totalTime;
        this.hasCompletedMaster = level.equals("Master") && result.equals("Win");
        this.theme = theme;
        this.category = category; // Store the category
        setTitle("AlphaWeb - Game Over");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        if (!musicPlayer.isPlaying()) {
            musicPlayer.play("/AlphaWeb/utils/music.wav");
        } else {
            musicPlayer.setPaused(false);
        }

        BackgroundPanel panel = new BackgroundPanel("/AlphaWeb/assets/background2.png");
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel (NORTH)
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        String status = result.equals("Win") ? "Victory!" : "Game Over";
        JLabel titleLabel = new JLabel(status + " Your Adventure Ends Here", SwingConstants.CENTER);
        titleLabel.setFont(FontManager.getFont(50f));
        titleLabel.setForeground(result.equals("Win") ? Color.BLUE : Color.green);
        titlePanel.add(titleLabel);

        // Score Panel (Current Player's Score)
        JPanel scorePanel = new JPanel();
        scorePanel.setOpaque(false);
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel currentScoreLabel = new JLabel("Your Score: " + score, SwingConstants.CENTER);
        currentScoreLabel.setFont(FontManager.getFont(50f));
        currentScoreLabel.setForeground(Color.PINK);
        currentScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel levelLabel = new JLabel("Level: " + level, SwingConstants.CENTER);
        levelLabel.setFont(FontManager.getFont(30f));
        levelLabel.setForeground(Color.YELLOW);
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scorePanel.add(Box.createVerticalStrut(30));
        scorePanel.add(currentScoreLabel);
        scorePanel.add(Box.createVerticalStrut(30));
        scorePanel.add(levelLabel);
        scorePanel.add(Box.createVerticalStrut(30));

        // Scoreboard Table in JScrollPane
        String[] columnNames = {"Name", "Score", "Time", "Level", "Master"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable scoreboardTable = new JTable(tableModel);
        scoreboardTable.setFont(FontManager.getFont(22f));
        scoreboardTable.setRowHeight(40);
        scoreboardTable.setOpaque(false);
        scoreboardTable.setForeground(Color.WHITE);
        scoreboardTable.setGridColor(new Color(150, 150, 150));
        scoreboardTable.setShowGrid(true);

        // Alternate row colors
        scoreboardTable.setDefaultRenderer(Object.class, new CenterTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.WHITE);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(50, 50, 50) : new Color(70, 70, 70));
                }
                return c;
            }
        });

        // Center-align table cells
        for (int i = 0; i < scoreboardTable.getColumnCount(); i++) {
            scoreboardTable.getColumnModel().getColumn(i).setCellRenderer(new CenterTableCellRenderer());
        }

        // Table header styling
        JTableHeader header = scoreboardTable.getTableHeader();
        header.setFont(FontManager.getFont(22f));
        header.setForeground(Color.YELLOW);
        header.setBackground(new Color(30, 30, 30));

        // Populate the table with high scores, skipping score 0
        int currentRank = 1;
        int lastScore = -1;
        for (int i = 0; i < highScores.size(); i++) {
            HighScore.ScoreEntry entry = highScores.get(i);
            if (entry.getScore() == 0) continue; // Skip entries with score 0
            if (i > 0 && entry.getScore() < lastScore) {
                currentRank = i + 1;
            }
            lastScore = entry.getScore();
            String displayName = entry.getName();
            if (entry.hasCompletedMaster()) {
                displayName = displayName;
            }
            tableModel.addRow(new Object[]{
                displayName,
                entry.getScore(),
                entry.getTime() + "s",
                entry.getLevel(),
                entry.hasCompletedMaster() ? "Yes" : "No"
            });
        }

        // JScrollPane for Scoreboard Table
        JScrollPane scrollPane = new JScrollPane(scoreboardTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.setMaximumSize(new Dimension(600, 300));
        scrollPane.setMinimumSize(new Dimension(600, 300));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        // Apply custom scroll bar UI to vertical scrollbar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new CustomScrollBarUI());
        verticalScrollBar.setOpaque(true);
        verticalScrollBar.setBackground(new Color(0, 0, 0, 0));

        // Main content panel to stack scorePanel and scrollPane vertically
        JPanel mainContentStackPanel = new JPanel();
        mainContentStackPanel.setOpaque(false);
        mainContentStackPanel.setLayout(new BoxLayout(mainContentStackPanel, BoxLayout.Y_AXIS));
        mainContentStackPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainContentStackPanel.add(scorePanel);
        mainContentStackPanel.add(Box.createVerticalStrut(20));
        mainContentStackPanel.add(scrollPane);

        // Wrapper panel for centering the mainContentStackPanel using GridBagLayout
        JPanel centerWrapperPanel = new JPanel(new GridBagLayout());
        centerWrapperPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        centerWrapperPanel.add(mainContentStackPanel, gbc);

        // Buttons Panel (SOUTH)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.setOpaque(false);

        RoundedShadowButton playAgain = new RoundedShadowButton("Play Again");
        playAgain.setFont(FontManager.getFont(40f));
        playAgain.setPreferredSize(new Dimension(250, 100));
        playAgain.addActionListener(e -> {
            // Restart with the same category and level, using the preserved theme
            new GameScreen(category, level, theme, this.playerName).setVisible(true);
            dispose();
        });

        RoundedShadowButton exit = new RoundedShadowButton("Exit");
        exit.setFont(FontManager.getFont(40f));
        exit.setPreferredSize(new Dimension(250, 100));
        exit.addActionListener(e -> NavigationManager.exit());

        buttonsPanel.add(playAgain);

        // Only add "Next Level" button if the player won
        if (result.equals("Win")) {
            RoundedShadowButton nextLevel = new RoundedShadowButton("Next Level");
            nextLevel.setFont(FontManager.getFont(40f));
            nextLevel.setPreferredSize(new Dimension(250, 100));
            nextLevel.addActionListener(e -> {
                String nextLevelStr = getNextLevel(level);
                if ("Category".equals(nextLevelStr)) {
                    NavigationManager.goToCategory(this, this.playerName);
                } else {
                    new GameScreen(category, nextLevelStr, theme, this.playerName).setVisible(true);
                    dispose();
                }
            });
            buttonsPanel.add(nextLevel);
        }

        buttonsPanel.add(exit);

        // Add components to the main 'panel' (BorderLayout)
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(centerWrapperPanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        setContentPane(panel);
        setVisible(true);
    }

    private String getNextLevel(String currentLevel) {
        return switch (currentLevel.toLowerCase()) {
            case "easy" -> "Medium";
            case "medium" -> "Hard";
            case "hard" -> "Master";
            case "master" -> "Category";
            default -> "Easy";
        };
    }

    private static class CenterTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        public CenterTableCellRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setForeground(Color.WHITE);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? new Color(50, 50, 50) : new Color(70, 70, 70));
            }
            return c;
        }
    }

    private static class CustomScrollBarUI extends BasicScrollBarUI {
        private static final int THUMB_RADIUS = 10;

        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = Color.RED;
            this.trackColor = Color.WHITE;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(thumbColor);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, THUMB_RADIUS, THUMB_RADIUS);
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(trackColor);
            g2.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, THUMB_RADIUS, THUMB_RADIUS);
            g2.dispose();
        }
    }
}