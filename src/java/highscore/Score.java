/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package highscore;

/**
 *
 * @author Mikkel
 */
public class Score {
    
    private String user;
    private int score;
    private int wins;
    private int losses;
    private int maxCombo;
    
    public Score() {
        
    }
    
//    public void saveOnWin(int score, int combo)

    public String getUser() {
        return user;
    }

    public int getScore() {
        return score;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getMaxCombo() {
        return maxCombo;
    }
    
    
    
}
