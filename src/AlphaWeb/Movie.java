/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlphaWeb;
import java.util.ArrayList;
/**
 *
 * @author malai
 */
public class Movie{
    
    public void showMovieLevels() {
        ArrayList<String> movieLevels = new ArrayList<>();
        movieLevels.add("FROZEN");
        movieLevels.add("JACKSPARROW");
        movieLevels.add("AVATAR");
        movieLevels.add("GLADIATOR");
        movieLevels.add("JOKER");

        Levels levels = new Levels(movieLevels);
        levels.showLevels();
    }
}