package AlphaWeb.screens;

import AlphaWeb.core.HighScore;
import AlphaWeb.core.WordSearchGenerator;
import AlphaWeb.core.Hangman;
import AlphaWeb.core.Score;
import AlphaWeb.core.Timer;
import AlphaWeb.core.Hint;
import AlphaWeb.core.WordBox;
import AlphaWeb.data.WordListLoader;
import AlphaWeb.utils.FontManager;
import AlphaWeb.utils.MusicPlayer;
import AlphaWeb.utils.NavigationManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameScreen extends JFrame {
    private final String category, theme;
    private String level;
    private char[][] grid;
    private List<String> wordList;
    private List<int[]> wordPositions;
    private JLabel[][] cellLabels;
    private final List<JLabel> selectedCells = new ArrayList<>();
    private final Map<String, Color> foundWordColors = new HashMap<>();
    private Hangman hangman;
    private Score score;
    private Timer gameTimer;
    private Hint hint;
    private WordBox wordBox;
    private JLabel scoreLabel, timerLabel, topScoreLabel, wordsLeftLabel, categoryLabel, levelLabel;
    private JPanel gridPanel, foundWordsBox, hangmanPanel;
    private JLabel hangmanImageLabel;
    private RoundedShadowButton hintButton;
    private int startRow = -1, startCol = -1;
    private String dragDirection = null;
    private final Color[] highlightColors = {
        Color.PINK, Color.CYAN, Color.ORANGE, Color.MAGENTA,
        Color.YELLOW, Color.LIGHT_GRAY, Color.GREEN, Color.BLUE
    };
    private javax.swing.Timer uiTimer;
    private MusicPlayer musicPlayer;
    private boolean isPaused = false;
    private static final int GRID_SIZE = 12;
    private static final Random random = new Random();
    private Point lastPoint;
    private String playerName;
    private int totalTime;
    private int masterWords;
    private String nextLevel;

    public GameScreen(String category, String level, String theme, String playerName) {
        this.category = category;
        this.level = level;
        this.theme = theme.replace(".png", "");
        setTitle("AlphaWeb - Game");
        setSize(1200, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        if (this.playerName == null || this.playerName.trim().isEmpty()) {
             this.playerName = playerName;
        }
        
        this.totalTime = 0;
        this.masterWords = 0;

        musicPlayer = MusicPlayer.getInstance();
        if (!musicPlayer.isPlaying()) {
            musicPlayer.play("/AlphaWeb/utils/music.wav");
        }

        initializeGame();
        setupUI();
        gameTimer.start();
        startUITimer();
        hint = new Hint(new ArrayList<>(wordList), grid, cellLabels, wordPositions);
        updateHintRemainingWords();
        System.out.println("GameScreen initialized, theme: " + theme + ", category: " + category);
    }

    public GameScreen(String category, String level, String theme, Score score, int totalTime, String playerName) {
        this(category, level, theme, playerName);
        this.score = score;
        this.totalTime = totalTime;
        this.playerName = playerName;
    }

    private void initializeGame() {
        wordList = WordListLoader.loadWords(category, level);
        if (wordList == null) {
            System.err.println("Word list is null for category: " + category + ", level: " + level);
            wordList = new ArrayList<>();
        }
        WordSearchGenerator generator = new WordSearchGenerator(wordList.toArray(new String[0]), GRID_SIZE);
        grid = generator.generate();
        wordList = Arrays.asList(generator.getWords());
        wordPositions = generator.getWordPositions();
        hangman = new Hangman();
        score = new Score();
        gameTimer = new Timer();
        wordBox = new WordBox();
    }
    
    private JPanel createGlassPane() {
        JPanel glassPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        glassPane.setOpaque(false);
        glassPane.setLayout(null);
        return glassPane;
    }

    private void setupUI() {
        BackgroundPanel panel = new BackgroundPanel("/AlphaWeb/assets/background3.png");
        panel.setLayout(new BorderLayout(20, 0)); 
        panel.add(createTopPanel(), BorderLayout.NORTH);
        panel.add(createMainPanel(), BorderLayout.CENTER);
        panel.add(createBottomPanel(), BorderLayout.SOUTH);
        setContentPane(panel);
        setGlassPane(createGlassPane());
        getGlassPane().setVisible(false);
        setVisible(true);
        SwingUtilities.invokeLater(this::updateHangmanImage);

        if (level.equals("Master") && hintButton != null) {
            hintButton.setEnabled(false);
        }
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setOpaque(false);

        topScoreLabel = new JLabel("Score: " + score.getScore(), SwingConstants.LEFT);
        topScoreLabel.setFont(FontManager.getFont(30f));
        topScoreLabel.setForeground(Color.GREEN);

        timerLabel = new JLabel("Time: " + gameTimer.getSeconds() + "s", SwingConstants.CENTER);
        timerLabel.setFont(FontManager.getFont(60f));
        timerLabel.setForeground(Color.ORANGE);

        JPanel stackedRightPanel = new JPanel();
        stackedRightPanel.setOpaque(false);
        stackedRightPanel.setLayout(new BoxLayout(stackedRightPanel, BoxLayout.Y_AXIS));

        RoundedShadowButton menuButton = new RoundedShadowButton("Menu");
        menuButton.setFont(FontManager.getFont(25f));
        menuButton.setForeground(Color.WHITE);
        menuButton.setPreferredSize(new Dimension(120, 70));
        menuButton.setMaximumSize(new Dimension(120, 70));
        menuButton.setMinimumSize(new Dimension(120, 70));
        menuButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuButton.setBackground(Color.BLUE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                menuButton.setBackground(new Color(255, 255, 102));
            }
        });
        menuButton.addActionListener(e -> showMenuDialog());

        categoryLabel = new JLabel("Category: " + category, SwingConstants.RIGHT);
        categoryLabel.setFont(FontManager.getFont(25f));
        categoryLabel.setForeground(Color.ORANGE);
        categoryLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        levelLabel = new JLabel("Level: " + level, SwingConstants.RIGHT);
        levelLabel.setFont(FontManager.getFont(25f));
        levelLabel.setForeground(Color.ORANGE);
        levelLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        stackedRightPanel.add(menuButton);
        stackedRightPanel.add(Box.createVerticalStrut(10));
        stackedRightPanel.add(categoryLabel);
        stackedRightPanel.add(levelLabel);

        JPanel containerForEast = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        containerForEast.setOpaque(false);
        containerForEast.add(stackedRightPanel);

        panel.add(topScoreLabel, BorderLayout.WEST);
        panel.add(timerLabel, BorderLayout.CENTER);
        panel.add(containerForEast, BorderLayout.EAST);

        return panel;
    }

    private void showMenuDialog() {
        JPanel glassPane = (JPanel) getGlassPane();
        glassPane.setVisible(true);
        glassPane.revalidate();
        glassPane.repaint();

        JDialog dialog = new JDialog(this, "Menu", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new GridLayout(3, 1, 15, 15));
        dialog.setUndecorated(true);
        dialog.getContentPane().setBackground(new Color(0, 0, 0, 0));

        JButton musicButton = new JButton("Music ON/OFF");
        musicButton.setFont(FontManager.getFont(22f));
        musicButton.setForeground(Color.RED);
        musicButton.setBackground(Color.GREEN);
        musicButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        musicButton.addActionListener(e -> {
            musicPlayer.toggle();
            dialog.dispose();
            glassPane.setVisible(false);
            glassPane.revalidate();
            glassPane.repaint();
        });
        musicButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                musicButton.setBackground(Color.YELLOW);
                musicButton.setForeground(Color.BLACK);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                musicButton.setBackground(Color.GREEN);
                musicButton.setForeground(Color.RED);
            }
        });

        JButton resumeButton = new JButton("Resume");
        resumeButton.setFont(FontManager.getFont(22f));
        resumeButton.setForeground(Color.BLACK);
        resumeButton.setBackground(new Color(173, 216, 230));
        resumeButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        resumeButton.addActionListener(e -> {
            togglePause(resumeButton);
            dialog.dispose();
            glassPane.setVisible(false);
            glassPane.revalidate();
            glassPane.repaint();
        });
        resumeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                resumeButton.setBackground(Color.YELLOW);
                resumeButton.setForeground(Color.BLACK);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                resumeButton.setBackground(new Color(173, 216, 230));
                resumeButton.setForeground(Color.BLACK);
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(FontManager.getFont(22f));
        backButton.setForeground(Color.RED);
        backButton.setBackground(new Color(255, 105, 180));
        backButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        backButton.addActionListener(e -> {
            dialog.dispose();
            glassPane.setVisible(false);
            NavigationManager.returnToHowToPlay(GameScreen.this);
        });
                backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(Color.YELLOW);
                backButton.setForeground(Color.BLACK);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(new Color(255, 105, 180));
                backButton.setForeground(Color.RED);
            }
        });

        dialog.add(musicButton);
        dialog.add(resumeButton);
        dialog.add(backButton);

        dialog.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setOpaque(false);
        main.setPreferredSize(new Dimension(1200, 600));
        main.setMaximumSize(new Dimension(1200, 600));
        main.setMinimumSize(new Dimension(1200, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 30, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(450, 450));
        leftPanel.setMaximumSize(new Dimension(450, 450));
        leftPanel.setMinimumSize(new Dimension(450, 450));

        gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 3, 3));
        gridPanel.setOpaque(false);
        gridPanel.setPreferredSize(new Dimension(400, 400));
        gridPanel.setMaximumSize(new Dimension(400, 400));
        gridPanel.setMinimumSize(new Dimension(400, 400));
        cellLabels = new JLabel[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JLabel label = new JLabel(String.valueOf(grid[i][j]), SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(Color.WHITE);
                label.setFont(FontManager.getFont(25f));
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cellLabels[i][j] = label;
                gridPanel.add(label);
            }
        }

        addMouseInteraction();

        leftPanel.add(gridPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(450, 450));
        rightPanel.setMaximumSize(new Dimension(450, 450));
        rightPanel.setMinimumSize(new Dimension(450, 450));

        hangmanPanel = new JPanel();
        hangmanPanel.setOpaque(false);
        hangmanPanel.setLayout(null);
        hangmanPanel.setPreferredSize(new Dimension(400, 500));
        hangmanPanel.setMaximumSize(new Dimension(400, 500));
        hangmanPanel.setMinimumSize(new Dimension(400, 500));
        setupHangmanParts();
        rightPanel.add(hangmanPanel, BorderLayout.CENTER);

        JPanel centerSpacer = new JPanel();
        centerSpacer.setOpaque(false);
        centerSpacer.setPreferredSize(new Dimension(50, 0));
        centerSpacer.setMaximumSize(new Dimension(50, Integer.MAX_VALUE));
        centerSpacer.setMinimumSize(new Dimension(50, 0));

        JPanel rightPanelContainer = new JPanel(new BorderLayout());
        rightPanelContainer.setOpaque(false);
        rightPanelContainer.setBorder(BorderFactory.createEmptyBorder(0, 150, 0, 0));
        rightPanelContainer.add(rightPanel, BorderLayout.CENTER);

        gbc.gridx = 0; gbc.weightx = 0.45;
        main.add(leftPanel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.1;
        main.add(centerSpacer, gbc);

        gbc.gridx = 2; gbc.weightx = 0.45;
        main.add(rightPanelContainer, gbc);

        return main;
    }
    
    private JPanel createBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel left = new JPanel(new GridLayout(1, 1));
        left.setOpaque(false);
        left.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        hintButton = new RoundedShadowButton("Hint");
        hintButton.addActionListener(e -> showHint());
        hintButton.setFont(FontManager.getFont(22f));
        hintButton.setPreferredSize(new Dimension(100, 60));
        left.add(hintButton);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        wordsLeftLabel = new JLabel("Words Left: " + getWordsLeft(), SwingConstants.RIGHT);
        wordsLeftLabel.setFont(FontManager.getFont(25f));
        wordsLeftLabel.setForeground(Color.blue);
        right.add(wordsLeftLabel);

        foundWordsBox = new ShadowPanel();
        foundWordsBox.setOpaque(false);
        foundWordsBox.setBorder(null);
        foundWordsBox.setLayout(new BoxLayout(foundWordsBox, BoxLayout.X_AXIS));
        foundWordsBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        foundWordsBox.setPreferredSize(new Dimension(400, 100));
        foundWordsBox.add(Box.createHorizontalGlue());

        bottom.add(left, BorderLayout.WEST);
        bottom.add(right, BorderLayout.EAST);
        bottom.add(foundWordsBox, BorderLayout.CENTER);

        return bottom;
    }

    private void setupHangmanParts() {
        hangmanImageLabel = new JLabel();
        hangmanImageLabel.setOpaque(false);
        int yOffset = 0;
        int shiftUp = 20;
        hangmanImageLabel.setBounds(0, yOffset - shiftUp, 400, 500);
        hangmanPanel.add(hangmanImageLabel);
    }

    private void updateHangmanImage() {
        if (hangmanImageLabel == null) {
            System.err.println("hangmanImageLabel is null in updateHangmanImage");
            return;
        }

        int partsAdded = hangman.getPartsAdded();
        if (partsAdded == 0) {
            hangmanImageLabel.setIcon(null);
            hangmanImageLabel.setText("");
            hangmanPanel.revalidate();
            hangmanPanel.repaint();
            return;
        }

        String path;
        if (partsAdded == 1) {
            path = "/AlphaWeb/assets/shared_stand.png";
        } else {
            path = "/AlphaWeb/assets/" + theme + "_mistake" + partsAdded + ".png";
        }

        ImageIcon icon = null;
        java.net.URL resource = getClass().getResource(path);
        if (resource != null) {
            icon = new ImageIcon(resource);
            Image scaled = icon.getImage().getScaledInstance(400, 500, Image.SCALE_SMOOTH);
            hangmanImageLabel.setIcon(new ImageIcon(scaled));
            hangmanImageLabel.setText("");
        } else {
            System.err.println("Hangman image not found: " + path);
            hangmanImageLabel.setIcon(null);
            hangmanImageLabel.setText("X");
            hangmanImageLabel.setForeground(Color.RED);
        }

        hangmanPanel.revalidate();
        hangmanPanel.repaint();
    }

    private void addMouseInteraction() {
        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isPaused) {
                    selectedCells.forEach(label -> label.setBackground(Color.WHITE));
                    selectedCells.clear();
                    dragDirection = null;
                    startRow = startCol = -1;
                    lastPoint = e.getPoint();
                    selectCellAt(e.getPoint(), true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isPaused) {
                    processSelectedWord();
                }
            }
        });

        gridPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isPaused) {
                    Point currentPoint = e.getPoint();
                    selectCellAt(currentPoint, false);
                    lastPoint = currentPoint;
                }
            }
        });
    }

    // In the selectCellAt method, replace with this improved version:
    private void selectCellAt(Point point, boolean isStart) {
        Component comp = gridPanel.getComponentAt(point);
        if (!(comp instanceof JLabel label)) {
            return;
        }

        int index = Arrays.asList(gridPanel.getComponents()).indexOf(label);
        if (index == -1) return;
        int row = index / GRID_SIZE, col = index % GRID_SIZE;

        if (isStart) {
            // Clear previous selection
            selectedCells.forEach(l -> l.setBackground(Color.WHITE));
            selectedCells.clear();

            // Start new selection
            startRow = row;
            startCol = col;
            dragDirection = null;
            selectedCells.add(label);
            label.setBackground(Color.CYAN);
        } else {
            // If we haven't established a direction yet (first drag movement)
            if (selectedCells.size() == 1) {
                // Calculate direction from start cell to current cell
                int dx = col - startCol;
                int dy = row - startRow;

                // Only allow straight lines or 45-degree diagonals
                if (dx != 0 && dy != 0 && Math.abs(dx) != Math.abs(dy)) {
                    return; // Invalid direction
                }

                // Determine primary direction
                if (dx == 0 && dy != 0) {
                    dragDirection = dy > 0 ? "DOWN" : "UP";
                } else if (dy == 0 && dx != 0) {
                    dragDirection = dx > 0 ? "RIGHT" : "LEFT";
                } else if (dx != 0 && dy != 0) {
                    // Diagonal
                    if (dx > 0 && dy > 0) dragDirection = "DOWN_RIGHT";
                    else if (dx < 0 && dy > 0) dragDirection = "DOWN_LEFT";
                    else if (dx > 0 && dy < 0) dragDirection = "UP_RIGHT";
                    else if (dx < 0 && dy < 0) dragDirection = "UP_LEFT";
                }

                // Add the second cell in the direction
                if (dragDirection != null && !selectedCells.contains(label)) {
                    selectedCells.add(label);
                    label.setBackground(Color.CYAN);
                }
            } 
            // If we have a direction, continue selecting in that direction
            else if (dragDirection != null) {
                // Check if we're moving in the same direction or reversing
                int lastIndex = Arrays.asList(gridPanel.getComponents()).indexOf(selectedCells.get(selectedCells.size() - 1));
                int lastRow = lastIndex / GRID_SIZE;
                int lastCol = lastIndex % GRID_SIZE;

                int dx = col - lastCol;
                int dy = row - lastRow;

                // Check if we're moving in the opposite direction (reverse selection)
                boolean isReversing = false;
                if (dragDirection.equals("RIGHT") && dx < 0) isReversing = true;
                else if (dragDirection.equals("LEFT") && dx > 0) isReversing = true;
                else if (dragDirection.equals("DOWN") && dy < 0) isReversing = true;
                else if (dragDirection.equals("UP") && dy > 0) isReversing = true;
                else if (dragDirection.equals("DOWN_RIGHT") && (dx < 0 || dy < 0)) isReversing = true;
                else if (dragDirection.equals("DOWN_LEFT") && (dx > 0 || dy < 0)) isReversing = true;
                else if (dragDirection.equals("UP_RIGHT") && (dx < 0 || dy > 0)) isReversing = true;
                else if (dragDirection.equals("UP_LEFT") && (dx > 0 || dy > 0)) isReversing = true;

                if (isReversing) {
                    // If we're reversing, remove the last cell
                    if (selectedCells.size() > 1) {
                        JLabel lastLabel = selectedCells.remove(selectedCells.size() - 1);
                        lastLabel.setBackground(Color.WHITE);
                    }
                } else {
                    // If we're continuing in the same direction, add the next cell
                    if (isValidNextCell(row, col) && !selectedCells.contains(label)) {
                        selectedCells.add(label);
                        label.setBackground(Color.CYAN);
                    }
                }
            }
        }
    }

    private boolean isValidNextCell(int row, int col) {
        if (selectedCells.isEmpty() || dragDirection == null) {
            return false;
        }

        int lastIndex = Arrays.asList(gridPanel.getComponents()).indexOf(selectedCells.get(selectedCells.size() - 1));
        int lastRow = lastIndex / GRID_SIZE;
        int lastCol = lastIndex % GRID_SIZE;

        // Must be adjacent in the current direction
        switch (dragDirection) {
            case "RIGHT": return row == lastRow && col == lastCol + 1;
            case "LEFT": return row == lastRow && col == lastCol - 1;
            case "DOWN": return col == lastCol && row == lastRow + 1;
            case "UP": return col == lastCol && row == lastRow - 1;
            case "DOWN_RIGHT": return row == lastRow + 1 && col == lastCol + 1;
            case "DOWN_LEFT": return row == lastRow + 1 && col == lastCol - 1;
            case "UP_RIGHT": return row == lastRow - 1 && col == lastCol + 1;
            case "UP_LEFT": return row == lastRow - 1 && col == lastCol - 1;
            default: return false;
        }
    }

    private String getDirection(Point start, Point end) {
        int dx = end.x - start.x, dy = end.y - start.y;
        if (dx == 0 && dy == 0) return null;
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? "RIGHT" : "LEFT";
        } else if (Math.abs(dy) > Math.abs(dx)) {
            return dy > 0 ? "DOWN" : "UP";
        } else {
            if (dx > 0 && dy > 0) return "DOWN_RIGHT";
            if (dx < 0 && dy > 0) return "DOWN_LEFT";
            if (dx > 0 && dy < 0) return "UP_RIGHT";
            if (dx < 0 && dy < 0) return "UP_LEFT";
        }
        return null;
    }

    private void processSelectedWord() {
        if (selectedCells.isEmpty()) return;

        String word = selectedCells.stream().map(JLabel::getText).reduce("", String::concat).toUpperCase();
        String reversedWord = new StringBuilder(word).reverse().toString();
        String matched = null;

        // Check if either the forward or reversed word is in the word list and not already found
        for (String w : wordList) {
            if (!foundWordColors.containsKey(w)) {
                if (w.equalsIgnoreCase(word) || w.equalsIgnoreCase(reversedWord)) {
                    matched = w;
                    break;
                }
            }
        }

        if (matched != null) {
            Color color = highlightColors[foundWordColors.size() % highlightColors.length];
            foundWordColors.put(matched, color);
            selectedCells.forEach(label -> label.setBackground(color));
            wordBox.addWord(matched);

            Component[] components = foundWordsBox.getComponents();
            int lastGlueIndex = -1;
            for (int i = 0; i < components.length; i++) {
                if (components[i] == Box.createHorizontalGlue()) {
                    lastGlueIndex = i;
                }
            }
            if (lastGlueIndex != -1) {
                for (int i = lastGlueIndex; i < components.length; i++) {
                    foundWordsBox.remove(lastGlueIndex);
                }
            }

            if (foundWordColors.size() > 1) {
                JLabel spaceLabel = new JLabel("    ");
                spaceLabel.setForeground(Color.BLUE);
                foundWordsBox.add(spaceLabel);
            }
            JLabel wordLabel = new JLabel(matched, SwingConstants.CENTER);
            wordLabel.setFont(FontManager.getFont(25f));
            wordLabel.setForeground(Color.MAGENTA);
            foundWordsBox.add(wordLabel);
            foundWordsBox.add(Box.createHorizontalGlue());
            foundWordsBox.revalidate();
            foundWordsBox.repaint();

            int points = switch (level) {
                case "Easy" -> 50;
                case "Medium" -> 70;
                case "Hard" -> 100;
                case "Master" -> {
                    masterWords++;
                    yield 1000;
                }
                default -> 0;
            };
            score.increaseScore(points);
            updateScoreLabels();
            wordsLeftLabel.setText("Words Left: " + getWordsLeft());
            updateHintRemainingWords();

            if (level.equals("Master") && masterWords == 5) {
                endGame(true);
            } else if (foundWordColors.size() == wordList.size()) {
                endGame(true);
            }
        } else {
            selectedCells.forEach(label -> label.setBackground(Color.WHITE));
            // Only penalize if the word is not a valid prefix of any remaining word
            boolean isValidPrefix = wordList.stream()
                .filter(w -> !foundWordColors.containsKey(w))
                .anyMatch(w -> w.toUpperCase().startsWith(word) || w.toUpperCase().startsWith(reversedWord));
            if (!isValidPrefix && word.length() > 1) { // Only penalize for non-prefixes with length > 1
                hangman.addPart();
                updateHangmanImage();
                updateScoreLabels();
            }
        }
        selectedCells.clear();
        if (hangman.isDead()) {
            endGame(false);
        }
    }

    private void startUITimer() {
        uiTimer = new javax.swing.Timer(1000, e -> {
            if (!isPaused) {
                timerLabel.setText("Time: " + gameTimer.getSeconds() + "s");
                updateScoreLabels();
                if (level.equals("Master") && gameTimer.getSeconds() > 180) {
                    endGame(false);
                }
            }
        });
        gameTimer.start();
        uiTimer.start();
    }

    private void togglePause(JButton resumeButton) {
        isPaused = !isPaused;
        resumeButton.setText(isPaused ? "Resume" : "Pause");
        gridPanel.setEnabled(!isPaused);
        
        if (isPaused) {
            gameTimer.stop();
        } else {
            gameTimer.start();
        }
    }

    private void showHint() {
        if (!isPaused && !level.equals("Master")) {
            score.decreaseScore(5);
            updateScoreLabels();
            if (hint != null) {
                hint.showHint(this);
            } else {
                System.err.println("Hint is null in showHint");
            }
        }
    }

    private int getWordsLeft() {
        return wordList.size() - foundWordColors.size();
    }

    private void updateHintRemainingWords() {
        if (hint != null) {
            List<String> unfoundWords = new ArrayList<>();
            for (String word : wordList) {
                if (!foundWordColors.containsKey(word.toUpperCase())) {
                    unfoundWords.add(word.toUpperCase());
                }
            }
            hint.remainingWords.clear();
            hint.remainingWords.addAll(unfoundWords);
        } else {
            System.err.println("Hint is null in updateHintRemainingWords");
        }
    }

    private void endGame(boolean win) {
        gameTimer.stop();
        if (uiTimer != null) uiTimer.stop();
        if (musicPlayer != null) {
            musicPlayer.setPaused(true);
        } else {
            System.err.println("musicPlayer is null in endGame");
        }

        totalTime += gameTimer.getSeconds();

        if (level.equals("Master") && (gameTimer.getSeconds() > 180 || !win)) {
            win = false;
        }

        int finalScore = score.getScore();
        if (win && totalTime <= 300) {
            finalScore += 1000;
        }
        int timePenalty = Math.max(0, (totalTime - 300) / 60) * 20;
        finalScore -= timePenalty;

        HighScore highScore = new HighScore();
        if (foundWordColors.size() > 0) {
            highScore.addScore(playerName, finalScore, totalTime, level);
        }

        if (win && !level.equals("Master")) {
            nextLevel = switch (level) {
                case "Easy" -> "Medium";
                case "Medium" -> "Hard";
                case "Hard" -> "Master";
                default -> null;
            };
            if (nextLevel != null) {
                if (nextLevel.equals("Master")) {
                    startMiniPuzzle();
                } else {
                    dispose();
                    new GameScreen(category, nextLevel, theme + ".png", score, totalTime, playerName);
                }
                return;
            }
        }

        boolean hasCompletedMaster = level.equals("Master") && win && masterWords >= 5;
        new ResultScreen(finalScore, level, win ? "Win" : "Loss", highScore.getScores(), playerName, totalTime, hasCompletedMaster, theme, category).setVisible(true);
        dispose();
    }

    private void startMiniPuzzle() {
        wordList = WordListLoader.loadWords(category, "Master");
        if (wordList == null || wordList.size() < 5) {
            System.err.println("Master word list invalid for category: " + category);
            wordList = WordListLoader.loadWords(category, "Easy").subList(0, 5);
        }
        gameTimer = new Timer();
        grid = new WordSearchGenerator(wordList.toArray(new String[0]), GRID_SIZE).generate();
        wordPositions = new WordSearchGenerator(wordList.toArray(new String[0]), GRID_SIZE).getWordPositions();
        foundWordColors.clear();
        masterWords = 0;
        level = "Master";
        setupUI();
        gameTimer.start();
        startUITimer();
        updateHintRemainingWords();
    }

    private void updateScoreLabels() {
        if (topScoreLabel != null) {
            topScoreLabel.setText("Score: " + score.getScore());
        }
    }

    private Color getRandomFunkyColor() {
        Color[] funkyColors = {
            new Color(255, 0, 0),
            new Color(0, 255, 0),
            new Color(255, 105, 180),
            new Color(0, 255, 255),
            new Color(255, 165, 0),
            new Color(255, 20, 147)
        };
        return funkyColors[random.nextInt(funkyColors.length)];
    }

    private class ShadowPanel extends JPanel {
        private static final int SHADOW_OFFSET = 5;
        private static final Color SHADOW_COLOR = new Color(49, 151, 134, 200);
        private static final Color FILL_COLOR = new Color(255, 255, 102);
        private static final int ARC_SIZE = 50;

        @Override
        protected void paintComponent(Graphics g) {
            int width = getWidth();
            int height = getHeight();

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(SHADOW_COLOR);
            g2d.fillRoundRect(SHADOW_OFFSET, SHADOW_OFFSET, width - 10, height - 10, ARC_SIZE, ARC_SIZE);

            g2d.setColor(FILL_COLOR);
            g2d.fillRoundRect(0, 0, width - 10, height - 10, ARC_SIZE, ARC_SIZE);

            g2d.dispose();
            super.paintComponent(g);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            size.width += SHADOW_OFFSET;
            size.height += SHADOW_OFFSET;
            return size;
        }
    }
}