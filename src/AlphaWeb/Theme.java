/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlphaWeb;
import java.util.Scanner;
/**
 *
 * @author Shafia 0057
 */
public class Theme {
    private Scanner scanner;
    
    public Theme(){
        scanner = new Scanner(System.in);
    }
    public void selectTheme() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Select a Theme ---");
        System.out.println("1. Classic Hangman");
        System.out.println("2. Pirate Adventure");
        System.out.println("3. Space Galaxy");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // clear newline

        switch (choice) {
            case 1:
                System.out.println("Classic Hangman Theme Selected!");
                break;
            case 2:
                System.out.println("Pirate Adventure Theme Selected!");
                break;
            case 3:
                System.out.println("Space Galaxy Theme Selected!");
                break;
            default:
                System.out.println("Invalid Theme. Default Theme Selected!");
        }
    }
}