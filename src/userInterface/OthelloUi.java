/*
 * Kevin Tran
 * COP 3330, Section 0001
 * University of Central Florida
 */
package userInterface;

///Imports.
import core.Game;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Kevin Tran
 */
public class OthelloUi extends JFrame
{
    //Member variables.
    private Game game;
    private GameUi gameUi;
    private BoardUi boardUi;
    
    //Constructor that links project code and calls initComponents.
    public OthelloUi(Game game)
    {
        this.game = game;
        initComponents();
    }
    
    /*
        Backend and specifications of Othello UI.
    */
    private void initComponents()
    {
        //Window dimensions.
        this.setPreferredSize(new Dimension(600, 600));
        this.setMinimumSize(new Dimension(600, 600));
        
        //Shut down application upon closing the UI.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Instantiate member variables!
        gameUi = new GameUi(game);
        boardUi = new BoardUi(game, gameUi);
        
        //JFrame uses BorderLayout so we have to specify where we want the components!
        this.add(gameUi, BorderLayout.NORTH);
        this.add(boardUi, BorderLayout.CENTER);
        
        //Call the method setVisible.
        this.setVisible(true);
        
    }///Closes method initComponents.
}///Closes class OthelloUi.
