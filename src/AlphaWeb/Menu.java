/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlphaWeb;
import java.util.Scanner;
/**
 *
 * @author malai
 */
public class Menu {

    private Scanner scanner = new Scanner(System.in);

    public void showMenuOptions() {
        System.out.println("\n==== Menu Options ====");
        System.out.println("1. Continue");
        System.out.println("2. Take Hint");
        System.out.println("3. Exit Game");
        
        System.out.print("Choose an option: ");       
        int choice = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        switch (choice) {
            case 1:
                System.out.println("Continuing the game...");
                break;
            case 2:
                System.out.println("Hint option selected.");
                break;
            case 3:
                System.out.println("Exiting the game...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Continuing...");
        }
    }
}
