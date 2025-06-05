package AlphaWeb.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class WordListLoader {
    public static List<String> loadWords(String category, String level) {
        List<String> words = new ArrayList<>();
        String fileName = String.format("/AlphaWeb/data/%s_%s.txt", category, level);

        InputStream is = WordListLoader.class.getResourceAsStream(fileName); // Get the InputStream first

        if (is == null) { // Check if it's null immediately
            String errorMsg = String.format("Word list file not found: %s. Using default words.", fileName);
            System.err.println(errorMsg);
            // Optionally, show a JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            return defaultWords(category, level); // Return default words if file not found
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) { // Now 'is' is guaranteed not null
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    words.add(line.toUpperCase());
                }
            }
        } catch (IOException e) {
            String errorMsg = String.format("Failed to read %s: %s. Using default words.", fileName, e.getMessage());
            System.err.println(errorMsg);
            // Optionally, show a JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            return defaultWords(category, level);
        } finally {
            // It's good practice to close the InputStream if not handled by try-with-resources,
            // but in this case, try-with-resources *is* used for the BufferedReader,
            // which will close the underlying InputStream as well.
            // So, no explicit 'is.close()' needed here.
        }

        if (words.isEmpty()) {
            String errorMsg = String.format("No words found in %s. Using default words.", fileName);
            System.err.println(errorMsg);
            // Optionally, show a JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            return defaultWords(category, level);
        }

        // Limit words
        int maxWords = level.equals("Master") ? 5 : 10;
        if (words.size() > maxWords) {
            words = words.subList(0, maxWords);
        }
        System.out.println(String.format("Loaded words from %s: %s", fileName, words));
        return words;
    }

    // Inside WordListLoader.java, defaultWords method:
    private static List<String> defaultWords(String category, String level) {
        Map<String, Map<String, List<String>>> defaultWordSets = new HashMap<>();

        // Movie
        defaultWordSets.put("Movie", new HashMap<>());
        defaultWordSets.get("Movie").put("Easy", Arrays.asList("JAWS", "CARS", "SOUL", "TOY", "FROZEN"));
        defaultWordSets.get("Movie").put("Medium", Arrays.asList("BRAVE", "TANGLED", "ZOOTOPIA", "SHREK", "RATATOUILLE"));
        defaultWordSets.get("Movie").put("Hard", Arrays.asList("INCEPTION", "INTERSTELLAR", "TRANSFORMERS", "JURASSIC", "MATRIX"));
        defaultWordSets.get("Movie").put("Master", Arrays.asList("AVATAR", "TITANIC", "GODFATHER", "STARWARS", "ALIEN"));

        // Country
        defaultWordSets.put("Country", new HashMap<>());
        defaultWordSets.get("Country").put("Easy", Arrays.asList("USA", "CANADA", "BRAZIL", "JAPAN", "FRANCE"));
        defaultWordSets.get("Country").put("Medium", Arrays.asList("GERMANY", "AUSTRALIA", "MEXICO", "ARGENTINA", "THAILAND"));
        defaultWordSets.get("Country").put("Hard", Arrays.asList("INDONESIA", "PHILIPPINES", "NETHERLANDS", "PORTUGAL", "MADAGASCAR"));
        defaultWordSets.get("Country").put("Master", Arrays.asList("SWEDEN", "NORWAY", "FINLAND", "DENMARK", "ICELAND"));

        // Food
        defaultWordSets.put("Food", new HashMap<>());
        defaultWordSets.get("Food").put("Easy", Arrays.asList("PIZZA", "BURGER", "SUSHI", "PASTA", "RICE"));
        defaultWordSets.get("Food").put("Medium", Arrays.asList("SALAD", "TACOS", "RAMEN", "CURRY", "FRIES"));
        defaultWordSets.get("Food").put("Hard", Arrays.asList("SPAGHETTI", "SUKIYAKI", "PAELLAVAL", "CHEESECAKE", "RISOTTO"));
        defaultWordSets.get("Food").put("Master", Arrays.asList("QUICHE", "CROISSANT", "TIRAMISU", "PAELLA", "DIMSUM"));

        // Animal
        defaultWordSets.put("Animal", new HashMap<>());
        defaultWordSets.get("Animal").put("Easy", Arrays.asList("CAT", "DOG", "BIRD", "FISH", "BEAR"));
        defaultWordSets.get("Animal").put("Medium", Arrays.asList("TIGER", "ZEBRA", "PANDA", "EAGLE", "SHARK"));
        defaultWordSets.get("Animal").put("Hard", Arrays.asList("CROCODILE", "ELEPHANT", "GIRAFFE", "HIPPOPOTAMUS", "RHINOCEROS"));
        defaultWordSets.get("Animal").put("Master", Arrays.asList("CHEETAH", "LEOPARD", "JAGUAR", "HYENA", "WILDEBEEST"));

        // Determine the actual category map to use, defaulting to "Movie" if the requested category isn't found
        Map<String, List<String>> categoryMap = defaultWordSets.getOrDefault(category, defaultWordSets.get("Movie"));
        if (categoryMap == null) { // Fallback if "Movie" also somehow doesn't exist (highly unlikely but defensive)
            System.err.println("Critical error: Default 'Movie' category words not found. Using a hardcoded fallback.");
            return Arrays.asList("ERROR", "WORDS", "DEFAULT");
        }

        // Determine the actual word list for the level within that category
        List<String> words = categoryMap.getOrDefault(level, categoryMap.get("Easy")); // Fallback to "Easy" if level not found
        if (words == null || words.isEmpty()) { // Final safeguard if even the fallback level is empty/null
            System.err.println("Critical error: Default words for category '" + category + "' and level '" + level + "' (or Easy fallback) are missing. Using a hardcoded fallback.");
            return Arrays.asList("ERROR", "WORDS", "DEFAULT");
        }

        return new ArrayList<>(words);
    }
}