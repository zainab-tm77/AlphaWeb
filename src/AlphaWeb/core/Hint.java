package AlphaWeb.core;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Hint {
    public ArrayList<String> remainingWords;
    private char[][] grid;
    private JLabel[][] cellLabels;
    private List<int[]> wordPositions;
    private List<String> originalWords; // Store original word list for position mapping

    public Hint(ArrayList<String> wordList, char[][] grid, JLabel[][] cellLabels, List<int[]> wordPositions) {
        this.remainingWords = new ArrayList<>();
        for (String word : wordList) {
            this.remainingWords.add(word.toUpperCase());
        }
        this.originalWords = new ArrayList<>();
        for (String word : wordList) {
            this.originalWords.add(word.toUpperCase());
        }
        this.grid = grid;
        this.cellLabels = cellLabels;
        this.wordPositions = wordPositions;
    }

    public void showHint(JFrame parent) {
        if (remainingWords.isEmpty()) {
            return; // Silently return if no hints are available
        }
        Random rand = new Random();
        String hintWord = remainingWords.get(rand.nextInt(remainingWords.size()));

        // Find the word's position by matching with originalWords
        for (int i = 0; i < originalWords.size(); i++) {
            if (originalWords.get(i).equals(hintWord)) {
                if (i < wordPositions.size()) {
                    int[] pos = wordPositions.get(i); // [row, col, direction, length]
                    highlightWord(pos[0], pos[1], pos[2], pos[3]);
                    remainingWords.remove(hintWord);
                    return;
                } else {
                    System.err.println("Position index out of bounds for word: " + hintWord);
                    return;
                }
            }
        }
        System.err.println("Word not found in original list: " + hintWord);
    }

    private void highlightWord(int row, int col, int direction, int length) {
        List<JLabel> highlighted = new ArrayList<>();
        int r = row, c = col;
        for (int i = 0; i < length; i++) {
            if (r < grid.length && c < grid[r].length && r >= 0 && c >= 0 && cellLabels[r][c] != null) {
                highlighted.add(cellLabels[r][c]);
                cellLabels[r][c].setBackground(Color.YELLOW);
            }
            // Update position based on direction
            switch (direction) {
                case 0: r--; break; // Up
                case 1: r++; break; // Down
                case 2: c--; break; // Left
                case 3: c++; break; // Right
                case 4: r--; c--; break; // Up-Left
                case 5: r--; c++; break; // Up-Right
                case 6: r++; c--; break; // Down-Left
                case 7: r++; c++; break; // Down-Right
            }
        }
        new javax.swing.Timer(5000, e -> highlighted.forEach(label -> {
            if (label.getBackground().equals(Color.YELLOW)) {
                label.setBackground(Color.WHITE);
            }
        })).start();
    }
}
