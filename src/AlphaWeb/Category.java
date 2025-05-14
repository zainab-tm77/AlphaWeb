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
        
        System.out.println("hello world");
    }
    public void showCategories() {

        System.out.println("\n--- CATEGORIES ---");
        System.out.println("1. Movie");
        System.out.println("2. Choose Theme");
        
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        if(choice == 1) {
            Movie movie = new Movie();
            movie.showMovieLevels();
        }else if(choice == 2){
            Theme theme = new Theme();
            theme.selectTheme();
        }else{
            System.out.println("Invalid choice!");
            AlphaWeb alphaWeb = new AlphaWeb();
            alphaWeb.displayHomeScreen();
        }
    }

}    
       