/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlphaWeb; 
import java.util.Scanner;
import java.util.ArrayList;
/**
 *
 * @author malai
 */
public class Levels {
    private Scanner scanner;
    private ArrayList<String> words;
    
    public Levels(ArrayList<String> words){
        this.words = words;
        scanner = new Scanner(System.in);
    }
    public void showLevels() {

        System.out.println("\n--- Select Difficulty ---");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        System.out.println("4. Back to Categories");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        ArrayList<String> filteredWords = new ArrayList<>();

        switch (choice) {
            case 1:
                for(String word : words){
                    if(word.length()>= 4 && word.length() <= 6){
                       filteredWords.add(word);
                    }
                }
                break;
            case 2:
                for(String word : words){
                    if(word.length()>= 7 && word.length() <= 9){
                       filteredWords.add(word);
                    }
                }
                break;
            case 3:
                for(String word : words){
                    if(word.length()>= 10 && word.length() <= 12){
                       filteredWords.add(word);
                    }
                }
                break;
            default:
                System.out.println("Invalid choice, please try again.");
                Category category = new Category();
                category.showCategories();
                return;
        }
        
        if(filteredWords.isEmpty()){
            System.out.println("No words are available for this level. please select another level. ");
            showLevels();
        }else{
            System.out.println("\nStarting Game at selected level.");
            Game game = new Game(filteredWords);
            game.startGame();
        }
    }

    // This method takes you back to the Category screen.
    public void backToCategory() {
        Category category = new Category();
        category.showCategories();
    }
}