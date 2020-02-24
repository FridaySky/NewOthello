/*
 * Kevin Tran
 * COP 3330, Section 0001
 * University of Central Florida
 */
package userInterface;

///Imports.
import core.Constants;
import core.Disc;
import core.Game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Kevin Tran
 */
public class BoardUi extends JPanel
{
    //Member variables.
    private JButton[][] board;
    private BoardListener listener;
    private Game game;
    private GameUi gameUi;
    
    //Constructor that links project code and deals with board user interface.
    public BoardUi(Game game, GameUi gameUi)
    {
        this.game = game;
        this.gameUi = gameUi;
        initComponents();
        listener.updateUi();
    }
    
    /*
        Create the board UI.
    */
    private void initComponents()
    {
        //Set UI size in dimensions of pixels.
        this.setPreferredSize(new Dimension(300, 300));
        this.setMinimumSize(new Dimension(300, 300));

        //Since flow layout isn't going to work here, we explicitly set to GridLayout.
        this.setLayout(new GridLayout(Constants.ROWS, Constants.COLUMNS));

        //Instantiate member variables!
        board = new JButton[Constants.ROWS][Constants.COLUMNS];
        listener = new BoardListener();

        //Loop through the board.
        for (int row = 0; row < Constants.ROWS; row++)
        {
            for (int col = 0; col < Constants.COLUMNS; col++)
            {
                //Create an instance of class JButton!
                board[row][col] = new JButton();
                
                //Call the method putClientProperty.
                board[row][col].putClientProperty("row", row);
                board[row][col].putClientProperty("col", col);
                board[row][col].putClientProperty("color", Constants.EMPTY);
                
                //Let's have a background color! It'll look nice :D
                board[row][col].setBackground(Color.GREEN);

                //Register the action listener for the JButton!
                board[row][col].addActionListener(listener);

                //Let's finish up by calling method .add()!
                this.add(board[row][col]);
            }//Closes inner for loop.
        }//Closes outer for loop.
    }//Closes initComponents.
    
    /*
        Inner class BoardListener directs the game.
    */
    private class BoardListener implements ActionListener
    {
        @Override
        /*
            This method interacts with the players.
        */
        public void actionPerformed(ActionEvent ae)
        {
            //Checks if the ActionEvent variable's source is of type JButton.
            if (ae.getSource() instanceof JButton)
            {
                //Create an instance of class JButton!
                JButton button = (JButton) ae.getSource();
                
                //Make variables to store the properties of JButton!
                int row = (int) button.getClientProperty("row");
                int col = (int) button.getClientProperty("col");
                Color color = (Color) button.getClientProperty("color");
                    
                //If the move is valid, update the UI, change the player, and determine if the game is over after the move.
                if(isValidMove(row, col, game.getCurrentPlayer().getDiscColor()))
                {
                    //Update the UI and change the player!
                    updateUi();
                    changePlayer();
                    
                }//Closes if statement.
                
                //Else if the move is not valid, then inform the player.
                else
                    JOptionPane.showMessageDialog(button, "The move was not valid! Please select again.");
            }//Closes outer if statement!
        }///Closes actionPerformed.
        
        /*
            Finishing method for isValidMove.
        */
        private boolean isValidMove(int row, int col, Color color)
        {
            //Initialize.
            boolean valid = false;
            
            //Check to see if the clicked square is not empty. If it is not empty, the move is not valid.
            if (board[row][col].getClientProperty("color") != Constants.EMPTY)
                valid = false;
            
            //Check if the move if valid.
            else if (game.getBoard().isValidMove(row, col, color))
                valid = true;
            
            return valid;
        }///Closes isValidMove method.
        
        /*
            Change the player's turn.
        */
        private void changePlayer()
        {
            //Change the player's turn.
            if (game.getCurrentPlayer() == game.getPlayerList().get(Constants.PLAYER_ONE))
                game.setCurrentPlayer(game.getPlayerList().get(Constants.PLAYER_TWO));
            else
                game.setCurrentPlayer(game.getPlayerList().get(Constants.PLAYER_ONE));

        }///Closes changePlayers method.
        
        /*
            Update the user interface.
        */
        private void updateUi()
        {
            //Get the array of the disc.
            Disc[][] discs = game.getBoard().getBoard();
            ImageIcon disc = null;
            
            //Loop through the board.
            for (int row = 0; row < Constants.ROWS; row++)
            {
                for (int col = 0; col < Constants.COLUMNS; col++)
                {
                    //Put a dark disc on the board.
                    if(discs[row][col].getDiscColor() == Constants.DARK)
                    {
                        disc = new ImageIcon(getClass().getResource("BlackPuckFinalized.png"));
                        disc = imageResize(disc);
                        board[row][col].setIcon(disc);
                        board[row][col].putClientProperty("color", Constants.DARK);
                    }
                    
                    //Put a light disc on the board.
                    else if (discs[row][col].getDiscColor() == Constants.LIGHT)
                    {
                        disc = new ImageIcon(getClass().getResource("WhitePillFinalized.png"));
                        disc = imageResize(disc);
                        board[row][col].setIcon(disc);
                        board[row][col].putClientProperty("color", Constants.LIGHT);
                    }
                }//Closes inner for loop.
            }//Closes outer for loop.
            
            //Update the scores for the game UI.
            gameUi.getScoreOne().setText(String.valueOf(game.getPlayerList().get(Constants.PLAYER_ONE).getScore()));
            gameUi.getScoreTwo().setText(String.valueOf(game.getPlayerList().get(Constants.PLAYER_TWO).getScore()));
            
        }///Closes updateUi function.
        
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
    }///Closes inner class BoardListener.
}//Closes class BoardUi.
