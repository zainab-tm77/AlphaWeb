/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlphaWeb;
/**
 *
 * @author malai
 */
public class MediumLevel extends Level {
    public MediumLevel() {
        super("Medium", 120, 20); // 2 minutes timer, base score 20
    }

    @Override
    public int calculateScore(int foundWordsCount) {
        return baseScore * foundWordsCount;
    }
}
