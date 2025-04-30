package AlphaWeb;
import java.util.Scanner;
/**
 *
 * @author Zainab
 */
public class Category {
    private Scanner scanner;
    
    public Category(){
        scanner = new Scanner(System.in);
    }
    public void showCategories() {

        System.out.println("\n--- Categories ---");
        System.out.println("1. Movie");
        System.out.println("2. Choose Theme");
        System.out.println("Enter your choice:");

        int choice = scanner.nextInt();

        if(choice == 1) {
            Movie movie = new Movie();
            movie.showMovieLevels();
        }else{
            System.out.println("Invalid choice!");
            AlphaWeb alphaWeb = new AlphaWeb();
            alphaWeb.displayHomeScreen();
        }
    }

}    
       