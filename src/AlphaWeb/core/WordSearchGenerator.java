package AlphaWeb.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordSearchGenerator {

    private final String[] words;
    private final int gridSize;
    private char[][] grid;
    private List<String> placedWords;
    private List<int[]> wordPositions;
    private final Random random;
    private static final int MAX_ATTEMPTS = 10;

    public WordSearchGenerator(String[] words, int gridSize) {
        this.words = Arrays.stream(words)
            .map(word -> word.replaceAll("[^A-Za-z]", "").toUpperCase()) // Remove hyphens, etc.
            .filter(word -> !word.isEmpty())
            .toArray(String[]::new);
        this.gridSize = gridSize;
        this.random = new Random();
        this.placedWords = new ArrayList<>();
        this.wordPositions = new ArrayList<>();
    }

    public char[][] generate() {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                initializeGrid();
                placedWords.clear();
                wordPositions.clear();
                List<String> wordList = new ArrayList<>(Arrays.asList(words));
                Collections.shuffle(wordList, random); // Shuffle to vary placement order
                for (String word : wordList) {
                    if (!placeWord(word)) {
                        System.err.println("Failed to place word: " + word + " (Attempt " + attempt + ")");
                        break;
                    }
                    placedWords.add(word);
                }
                if (placedWords.size() == words.length) {
                    fillEmptyCells();
                    System.out.println("Successfully placed all words: " + placedWords);
                    return grid;
                }
            } catch (Exception e) {
                System.err.println("Error during grid generation (Attempt " + attempt + "): " + e.getMessage());
            }
        }
        System.err.println("Failed to place all words after " + MAX_ATTEMPTS + " attempts. Using partial grid with: " + placedWords);
        fillEmptyCells();
        return grid;
    }

    private void initializeGrid() {
        grid = new char[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = '\0';
            }
        }
    }

    private boolean placeWord(String word) {
        int maxTries = gridSize * gridSize;
        for (int tryCount = 0; tryCount < maxTries; tryCount++) {
            int row = random.nextInt(gridSize);
            int col = random.nextInt(gridSize);
            int[] directions = {0, 1, 2, 3, 4, 5, 6, 7}; // Up, Down, Left, Right, Diagonals
            shuffleArray(directions);
            for (int dir : directions) {
                if (canPlaceWord(word, row, col, dir)) {
                    placeWordAt(word, row, col, dir);
                    wordPositions.add(new int[]{row, col, dir, word.length()});
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canPlaceWord(String word, int row, int col, int direction) {
        int len = word.length();
        int endRow = row, endCol = col;

        switch (direction) {
            case 0: endRow -= len - 1; break; // Up
            case 1: endRow += len - 1; break; // Down
            case 2: endCol -= len - 1; break; // Left
            case 3: endCol += len - 1; break; // Right
            case 4: endRow -= len - 1; endCol -= len - 1; break; // Up-Left
            case 5: endRow -= len - 1; endCol += len - 1; break; // Up-Right
            case 6: endRow += len - 1; endCol -= len - 1; break; // Down-Left
            case 7: endRow += len - 1; endCol += len - 1; break; // Down-Right
        }

        if (endRow < 0 || endRow >= gridSize || endCol < 0 || endCol >= gridSize) {
            return false;
        }

        int r = row, c = col;
        for (char ch : word.toCharArray()) {
            if (grid[r][c] != '\0' && grid[r][c] != ch) {
                return false;
            }
            switch (direction) {
                case 0: r--; break;
                case 1: r++; break;
                case 2: c--; break;
                case 3: c++; break;
                case 4: r--; c--; break;
                case 5: r--; c++; break;
                case 6: r++; c--; break;
                case 7: r++; c++; break;
            }
        }
        return true;
    }

    private void placeWordAt(String word, int row, int col, int direction) {
        int r = row, c = col;
        for (char ch : word.toCharArray()) {
            grid[r][c] = ch;
            switch (direction) {
                case 0: r--; break;
                case 1: r++; break;
                case 2: c--; break;
                case 3: c++; break;
                case 4: r--; c--; break;
                case 5: r--; c++; break;
                case 6: r++; c--; break;
                case 7: r++; c++; break;
            }
        }
    }

    private void fillEmptyCells() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] == '\0') {
                    grid[i][j] = (char) ('A' + random.nextInt(26));
                }
            }
        }
    }

    private void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public String[] getWords() {
        return placedWords.toArray(new String[0]);
    }

    public List<int[]> getWordPositions() {
        return wordPositions;
    }
}
