package userInterface;

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


public class BoardUi extends JPanel {
    
    public BoardUi(Game game, GameUi gameUi) {
        this.game = game;
        this.gameUi = gameUi;
        initComponents();
        listener.updateUi();
    }
    
    /**
     * Create the board UI
     */
    private void initComponents()
    {
        // Set UI size in dimensions of pixels
        this.setPreferredSize(new Dimension(300, 300));
        this.setMinimumSize(new Dimension(300, 300));

        // Since flow layout isn't going to work here, we explicitly set to GridLayout
        this.setLayout(new GridLayout(Constants.ROWS, Constants.COLUMNS));

        board = new JButton[Constants.ROWS][Constants.COLUMNS];
        listener = new BoardListener();

        // Loop through the board
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLUMNS; col++) {
                
                board[row][col] = new JButton();
                
                board[row][col].putClientProperty("row", row);
                board[row][col].putClientProperty("col", col);
                board[row][col].putClientProperty("color", Constants.EMPTY);
                
                board[row][col].setBackground(Color.GREEN);

                board[row][col].addActionListener(listener);

                this.add(board[row][col]);
            }
        }
    }
    
    /**
     * Direct the game
     */
    private class BoardListener implements ActionListener
    {
        @Override
        /**
         * Interact with the players
         * 
         * @param ae the user-triggered ActionEvent
         */
        public void actionPerformed(ActionEvent ae) {
            
            if (ae.getSource() instanceof JButton) {
                JButton button = (JButton) ae.getSource();
                
                int row = (int) button.getClientProperty("row");
                int col = (int) button.getClientProperty("col");
                Color color = (Color) button.getClientProperty("color");
                    
                // If the move is valid, update the UI, change the player, and determine if the game is over after the move
                if(isValidMove(row, col, game.getCurrentPlayer().getDiscColor())) {
                    updateUi();
                    changePlayer();
                }
                
                // Else if the move is not valid, then inform the player
                else {
                    JOptionPane.showMessageDialog(button, "The move was not valid! Please select again.");
                }
            }
        }
        
        /**
         * Determine whether the selected tile is valid
         * 
         * @param selectedRow the row of the clicked tile
         * @param selectedCol the column of the clicked tile
         * @param currentPlayerColor the color othe player that made the click on the tile
         * @return
         */
        private boolean isValidMove(int selectedRow, int selectedCol, Color currentPlayerColor) {
            boolean valid = false;
            
            // If the selected tile is not empty, then the move is automatically invalid
            if (board[selectedRow][selectedCol].getClientProperty("color") != Constants.EMPTY)
                valid = false;
            
            // Otherwise see if the selected tile is valid
            else if (game.getBoard().isValidMove(selectedRow, selectedCol, currentPlayerColor)) {
                valid = true;
            }
            
            return valid;
        }
        
        /**
         * Change the player's turn
         */
        private void changePlayer() {
            if (game.getCurrentPlayer() == game.getPlayerList().get(Constants.PLAYER_ONE)) {
                game.setCurrentPlayer(game.getPlayerList().get(Constants.PLAYER_TWO));
            }
            
            else {
                game.setCurrentPlayer(game.getPlayerList().get(Constants.PLAYER_ONE));
            }
        }
        
        /**
         * Update the user interface
         */
        private void updateUi() {
            Disc[][] discs = game.getBoard().getBoard();
            ImageIcon disc = null;
            
            // Loop through the board
            for (int row = 0; row < Constants.ROWS; row++) {
                for (int col = 0; col < Constants.COLUMNS; col++) {
                    
                    // If tile is marked as dark player's tile, then update the tile UI to reflect that
                    if (discs[row][col].getDiscColor() == Constants.DARK) {
                        disc = new ImageIcon(getClass().getResource("BlackPuckFinalized.png"));
                        disc = imageResize(disc);
                        board[row][col].setIcon(disc);
                        board[row][col].putClientProperty("color", Constants.DARK);
                    }
                    
                    // If tile is marked as light player's tile, then update the tile UI to reflect that
                    else if (discs[row][col].getDiscColor() == Constants.LIGHT) {
                        disc = new ImageIcon(getClass().getResource("WhitePillFinalized.png"));
                        disc = imageResize(disc);
                        board[row][col].setIcon(disc);
                        board[row][col].putClientProperty("color", Constants.LIGHT);
                    }
                }
            }
            
            // Show updated scores
            gameUi.getScoreOne().setText(String.valueOf(game.getPlayerList().get(Constants.PLAYER_ONE).getScore()));
            gameUi.getScoreTwo().setText(String.valueOf(game.getPlayerList().get(Constants.PLAYER_TWO).getScore()));
        }
        
        /**
         * Resize image
         * 
         * @param icon the original image
         * @return the resized image
         */
        private ImageIcon imageResize(ImageIcon icon) {
            Image image = icon.getImage();
            Image newImage = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImage);
            return icon;
        }
    }
    
    private JButton[][] board;
    private BoardListener listener;
    private Game game;
    private GameUi gameUi;
}
