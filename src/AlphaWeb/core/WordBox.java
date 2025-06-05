package AlphaWeb.core;

import java.util.ArrayList;
import java.util.List;

public class WordBox {
    private List<String> words;

    public WordBox(List<String> words) {
        this.words = new ArrayList<>(words);
    }

    public WordBox() {
        this.words = new ArrayList<>(); // Initialize an empty list
    }

    public void removeWord(String word) {
        words.remove(word);
    }

    public String getWordsLeft() {
        return String.valueOf(words.size()); // Return as String for compatibility
    }

    public void addWord(String matched) {
        if (!words.contains(matched)) { // Avoid duplicates
            words.add(matched);
        }
    }
}