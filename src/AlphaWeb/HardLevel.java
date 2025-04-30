/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlphaWeb;
/**
 *
 * @author malai
 */
public class HardLevel extends Level {
    public HardLevel() {
        super("Hard", 90, 30); // 1.5 minutes timer, base score 30
    }

    @Override
    public int calculateScore(int foundWordsCount) {
        return baseScore * foundWordsCount;
    }
}
