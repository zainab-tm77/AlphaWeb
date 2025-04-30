package AlphaWeb;
import java.util.Scanner;

/**
 *
 * @author Shafia 0057
 */
public class Result {

    private int finalScore;
    private Scanner scanner;
    
    public Result(int score) {
        this.finalScore = score;
        this.scanner = new Scanner(System.in);
    }
    

    public void showResult() {
        System.out.println("\n=== GAME OVER ===");
        System.out.println("Your Final Score: " + finalScore);

        System.out.println("\nWould you like to:");
        System.out.println("1. Play Again");
        System.out.println("2. Go to Next Level");
        System.out.println("3. Exit");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                // Restart the game from AlphaWeb Home
                AlphaWeb alphaWeb = new AlphaWeb();
                alphaWeb.displayHomeScreen();
                break;
            case 2:
                // Go to Levels again
                Movie movie = new Movie();
                movie.showMovieLevels();
                break;
            case 3:
                System.out.println("Thanks for playing AlphaWeb! See you soon 🎯");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu...");
                AlphaWeb alphaWebDefault = new AlphaWeb();
                alphaWebDefault.displayHomeScreen();
                break;
        }
    }
}