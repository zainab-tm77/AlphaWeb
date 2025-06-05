// In AlphaWeb.core.HighScore.java

package AlphaWeb.core;

import java.io.*;
import java.util.*;

public class HighScore {
    private List<ScoreEntry> scores;

    public HighScore() {
        scores = new ArrayList<>();
        loadScores();
    }

    public void addScore(String playerName, int score, int time, String level) {
        ScoreEntry existing = scores.stream()
                .filter(e -> e.getName().equals(playerName))
                .findFirst()
                .orElse(null);

        boolean currentCompletionIsMaster = level.equals("Master");

        if (existing != null) {
            if (score > existing.getScore()) {
                scores.remove(existing);
                scores.add(new ScoreEntry(playerName, score, time, level));
            } else if (score == existing.getScore() && currentCompletionIsMaster && !existing.hasCompletedMaster()) {
                scores.remove(existing);
                scores.add(new ScoreEntry(playerName, score, time, level));
            }
        } else {
            scores.add(new ScoreEntry(playerName, score, time, level));
        }

        scores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        saveScores();
    }

    public List<ScoreEntry> getScores() {
        // This will now return all unique scores, sorted.
        return new ArrayList<>(scores);
    }

    // --- loadScores(), saveScores(), and ScoreEntry inner class remain unchanged ---

    private void loadScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader("highScore.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    int time = Integer.parseInt(parts[2]);
                    String level = parts[3];
                    boolean completedMaster = Boolean.parseBoolean(parts[4]);
                    scores.add(new ScoreEntry(name, score, time, level, completedMaster));
                } else if (parts.length == 4) {
                     String name = parts[0];
                     int score = Integer.parseInt(parts[1]);
                     int time = Integer.parseInt(parts[2]);
                     String level = parts[3];
                     scores.add(new ScoreEntry(name, score, time, level));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
            scores.clear();
        }
    }

    private void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("highScore.txt"))) {
            for (ScoreEntry entry : scores) {
                writer.write(String.format("%s,%d,%d,%s,%b%n",
                        entry.getName(), entry.getScore(), entry.getTime(), entry.getLevel(), entry.hasCompletedMaster()));
            }
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

   // In AlphaWeb.core.ScoreEntry.java

    public class ScoreEntry {
        private final String playerName; // This is your actual field
        private final int score;
        private final int time;
        private final String level;
        private boolean completedMaster;

        public ScoreEntry(String playerName, int score, int time, String level) {
            this.playerName = playerName;
            this.score = score;
            this.time = time;
            this.level = level;
            this.completedMaster = level.equals("Master");
        }

        public ScoreEntry(String playerName, int score, int time, String level, boolean completedMaster) {
            this.playerName = playerName;
            this.score = score;
            this.time = time;
            this.level = level;
            this.completedMaster = completedMaster;
        }

        public String getName() { return playerName; } // <--- CORRECTED THIS LINE
        public int getScore() { return score; }
        public int getTime() { return time; }
        public String getLevel() { return level; }
        public boolean hasCompletedMaster() { return completedMaster; }
    }
}