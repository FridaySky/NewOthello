package othello;

import core.Game;
import userInterface.OthelloUi;

public class Othello {
    public static void main(String[] args){
        new OthelloUi(new Game());
    }
}