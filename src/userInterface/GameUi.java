/*
 * Kevin Tran
 * COP 3330, Section 0001
 * University of Central Florida
 */
package userInterface;

///Imports.
import core.Constants;
import core.Game;
import core.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Kevin Tran
 */
public class GameUi extends JPanel
{
    //Member variables.
    private Game game;
    private JLabel nameOne;
    private JLabel nameTwo;
    private JLabel scoreOne;
    private JLabel scoreTwo;
    
    //Constructor that links project code and calls initComponents.
    public GameUi(Game game)
    {
        this.game = game;
        initComponents();
    }
    
    /*
        Defines information for the players' discs.
    */
    private void initComponents()
    {
        //Set dimensions of grid.
        this.setPreferredSize(new Dimension(800, 80));
        this.setMinimumSize(new Dimension(800, 80));
        this.setBackground(Color.ORANGE);
        
        //discOne is the dark color, and discTwo is the light color.
        ImageIcon discOne = new ImageIcon(getClass().getResource("BlackPuckFinalized.png"));
        discOne = imageResize(discOne);
        
        //Player one's information to be displayed at the top.
        nameOne = new JLabel();
        nameOne.setIcon(discOne);
        nameOne.setText(getGame().getPlayerList().get(Constants.PLAYER_ONE).getName());
        nameOne.setMinimumSize(new Dimension(200, 50));
        nameOne.setPreferredSize(new Dimension(200, 50));
        nameOne.setBackground(Color.LIGHT_GRAY);
        nameOne.setFont(new Font("Serif", Font.BOLD, 22));
        scoreOne = new JLabel();
        scoreOne.setFont(new Font("Serif", Font.BOLD, 22));
        
        //discOne is the dark color, and discTwo is the light color.
        ImageIcon discTwo = new ImageIcon(getClass().getResource("WhitePillFinalized.png"));
        discTwo = imageResize(discTwo);
        
        //Player two's information to be displayed at the top.
        nameTwo = new JLabel();
        nameTwo.setIcon(discTwo);
        nameTwo.setText(getGame().getPlayerList().get(Constants.PLAYER_TWO).getName());
        nameTwo.setMinimumSize(new Dimension(200, 50));
        nameTwo.setPreferredSize(new Dimension(200, 50));
        nameTwo.setBackground(Color.LIGHT_GRAY);
        nameTwo.setFont(new Font("Serif", Font.BOLD, 22));
        scoreTwo = new JLabel();
        scoreTwo.setFont(new Font("Serif", Font.BOLD, 22));
        
        //Add all four JLabel instances to the JPanel.
        this.add(nameOne);
        this.add(scoreOne);
        this.add(nameTwo);
        this.add(scoreTwo);
    }///Closes initComponents method.

    /*
        This method serves to resize the images used for the discs.
    */
    private ImageIcon imageResize(ImageIcon icon)
    {
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImage);
        return icon;
    }///Closes imageIcon method.

    /**
     * @return the game
     */
    public Game getGame()
    {
        return game;
    }

    /**
     * @param game the game to set
     */
    public void setGame(Game game)
    {
        this.game = game;
    }

    /**
     * @return the scoreOne
     */
    public JLabel getScoreOne()
    {
        return scoreOne;
    }

    /**
     * @param scoreOne the scoreOne to set
     */
    public void setScoreOne(JLabel scoreOne)
    {
        this.scoreOne = scoreOne;
    }

    /**
     * @return the scoreTwo
     */
    public JLabel getScoreTwo()
    {
        return scoreTwo;
    }

    /**
     * @param scoreTwo the scoreTwo to set
     */
    public void setScoreTwo(JLabel scoreTwo)
    {
        this.scoreTwo = scoreTwo;
    }
    
}///Closes class GameUi.
