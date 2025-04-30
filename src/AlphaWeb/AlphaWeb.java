/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlphaWeb;
import java.util.Scanner;
/**
 *
 * @author Zainab Tahir 24L-0001
 * Hameeza Mehmood 24L-0032
 * Abdul Rafay 24L-0049
 * Shafia Haider 24L-0057
 */
public class AlphaWeb {
    private Scanner scanner;
    
    public void displayHomeScreen(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to AlphaWeb!");
        System.out.println("Press 1 to Play");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        if (choice == 1) {
            Category category = new Category();
            category.showCategories();
        } else {
            System.out.println("Invalid choice. Exiting...");
            System.exit(0);
        }    
    }
    
    public static void main(String[] args) {
        AlphaWeb alphaWeb = new AlphaWeb();
        alphaWeb.displayHomeScreen();
    }
}

