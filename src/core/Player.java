package core;

import java.awt.Color;

public class Player {
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the discColor
     */
    public Color getDiscColor() {
        return discColor;
    }

    /**
     * @param discColor the discColor to set
     */
    public void setDiscColor(Color discColor) {
        this.discColor = discColor;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }
    
    private String name;
    private Color discColor;
    private int score;
}
