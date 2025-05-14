/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AlphaWeb;
/**
 *
 * @author malai
 */
public class EasyLevel extends Level {
    public EasyLevel() {
        super("Easy", 180, 10); // 3 minutes timer, base score 10
    }
    
    @Override
    public int calculateScore(int foundWordsCount) {
        return baseScore * foundWordsCount;
    }
}