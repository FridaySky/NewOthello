package userInterface;

import core.Game;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

public class OthelloUi extends JFrame {
    
    public OthelloUi(Game game) {
        this.game = game;
        initComponents();
    }
    
    /**
     * Handle backend implementations of the application
     */
    private void initComponents() {
        
        // Set window dimensions
        this.setPreferredSize(new Dimension(600, 600));
        this.setMinimumSize(new Dimension(600, 600));
        
        // Shut down application after closing the game window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JFrame uses BorderLayout so we have to specify where we want the components!
        gameUi = new GameUi(game);
        boardUi = new BoardUi(game, gameUi);
        this.add(gameUi, BorderLayout.NORTH);
        this.add(boardUi, BorderLayout.CENTER);
        
        // Show the window
        this.setVisible(true);
    }
    
    private Game game;
    private GameUi gameUi;
    private BoardUi boardUi;
}
