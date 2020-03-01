package core;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Board
{
    public Board() {
        initObjects();
    }
    
    /**
     * Make the board and set the four initial pieces in the center of the board
     */
    private void initObjects() {
        board = new Disc[Constants.ROWS][Constants.COLUMNS];
        
        // Place an invisible disc at each tile
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLUMNS; col++) {
                board[row][col] = new Disc();
            }
        }
        
        // Get the starting positions of the board
        int topMiddleRow      = Constants.ROWS / 2 - 1;
        int bottomMiddleRow   = Constants.ROWS / 2;
        int leftMiddleColumn  = Constants.COLUMNS / 2 - 1;
        int rightMiddleColumn = Constants.COLUMNS / 2;
    
        // Set disc colors on the starting positions
        board[topMiddleRow][leftMiddleColumn].setDiscColor(Constants.LIGHT);
        board[topMiddleRow][rightMiddleColumn].setDiscColor(Constants.DARK);
        board[bottomMiddleRow][leftMiddleColumn].setDiscColor(Constants.DARK);
        board[bottomMiddleRow][rightMiddleColumn].setDiscColor(Constants.LIGHT);
    }
    
    /**
     * Calculate the player scores
     */
    public void calculateScore() {
        // Initialize scores
        darkCount = 0;
        lightCount = 0;
        
        // Look through board to count number of dark and light discs
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLUMNS; col++) {
                if (board[row][col].getDiscColor() == Constants.DARK) {
                    darkCount++;
                }
                else if (board[row][col].getDiscColor() == Constants.LIGHT) {
                    lightCount++;
                }
            }
        }
        
        // Update scores
        players.get(Constants.PLAYER_ONE).setScore(darkCount);
        players.get(Constants.PLAYER_TWO).setScore(lightCount);
    }
    
    /**
     * This method determines whether the user made a valid move
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * 
     * @return whether the move was valid
     */
    public boolean isValidMove(int selectedRow, int selectedCol, Color currentPlayerColor) {
        boolean isValidMove = false;
        
        // Check all directions to see if the discs can be flipped
        if (tryFlippingNorth(selectedRow, selectedCol, currentPlayerColor, true) ||
            tryFlippingNortheast(selectedRow, selectedCol, currentPlayerColor, true) ||
            tryFlippingEast(selectedRow, selectedCol, currentPlayerColor, true) ||
            tryFlippingSouthEast(selectedRow, selectedCol, currentPlayerColor, true) ||
            tryFlippingSouth(selectedRow, selectedCol, currentPlayerColor, true) ||
            tryFlippingSouthwest(selectedRow, selectedCol, currentPlayerColor, true) ||
            tryFlippingWest(selectedRow, selectedCol, currentPlayerColor, true) ||
            tryFlippingNorthwest(selectedRow, selectedCol, currentPlayerColor, true)) {
            
                isValidMove = true;
                calculateScore();
                isGameOver(currentPlayerColor);
        }

        return isValidMove;
    }
    
    /**
     * 
     * @param currentPlayerColor
     */
    private void isGameOver(Color currentPlayerColor) {
        Color nextColor = getNextColor(currentPlayerColor);

        // Determine if the game is over from the next player's perspective.
        if (gameOver(nextColor, darkCount, lightCount))
        {
            JOptionPane.showMessageDialog(null, "The game is over!");
            if (darkCount > lightCount) {
                JOptionPane.showMessageDialog(null, "Player " + getPlayers().get(Constants.PLAYER_ONE).getName() + " wins!");
            } else if (darkCount < lightCount) {
                JOptionPane.showMessageDialog(null, "Player " + getPlayers().get(Constants.PLAYER_TWO).getName() + " wins!");
            } else {
                JOptionPane.showMessageDialog(null, "The game has ended in a draw!");
            }
        }
    }
    
    /**
     * Get the color of the next player
     * 
     * @param currentPlayerColor the color of the current player
     * 
     * @return the color of the next player
     */
    private Color getNextColor(Color currentPlayerColor) {
        Color nextColor;
        
        if (currentPlayerColor == getPlayers().get(Constants.PLAYER_ONE).getDiscColor()) {
            nextColor = getPlayers().get(Constants.PLAYER_TWO).getDiscColor();
        } else {
            nextColor = getPlayers().get(Constants.PLAYER_ONE).getDiscColor();
        }
            
        return nextColor;
    }
    
    /**
     * Determine whether discs can be flipped in the north direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * @param doFlip control whether to flip tiles
     * 
     * @return whether the discs can be flipped in the north direction
     */
    private boolean tryFlippingNorth(int selectedRow, int selectedCol, Color currentPlayerColor, boolean doFlip) {
        boolean foundSameColorDisc = false;
        boolean isValidMove = false;
        
        int tilesToFlip = 0;
        int rowToCheck = selectedRow - 1; // Don't check the current row
        
        // While there are still rows to check and while we haven't found our color on the other end
        while (rowToCheck >= 0 && !foundSameColorDisc) {
            
            // Stop checking when we stumble upon an empty tile
            if (board[rowToCheck][selectedCol].getDiscColor() == Constants.EMPTY) {
                break;
            }
            
            // If the tile is of the opposite color, it gets flipped
            else if (board[rowToCheck][selectedCol].getDiscColor() != currentPlayerColor) {
                tilesToFlip++;
            }
            
            // End the flipping sequence since the tile matches the current player's color
            else {
                foundSameColorDisc = true;
            }

            // Check the row above
            rowToCheck--;
        }
        
        // Valid move
        if (foundSameColorDisc && tilesToFlip > 0 && doFlip) {
            
            // Place disk on clicked tile
            board[selectedRow][selectedCol].setDiscColor(currentPlayerColor);

            while (tilesToFlip > 0) {
                selectedRow--;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selectedCol].setDiscColor(currentPlayerColor);
            }
            isValidMove = true;
        }
        
        return isValidMove;
    }
    
    /**
     * Determine whether discs can be flipped in the northeast direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * @param doFlip control whether to flip tiles
     * 
     * @return whether the discs can be flipped in the northeast direction
     */
    private boolean tryFlippingNortheast(int selectedRow, int selectedCol, Color currentPlayerColor, boolean doFlip) {
        boolean foundSameColorDisc = false;
        boolean isValidMove = false;

        int tilesToFlip = 0;
        int rowToCheck = selectedRow - 1; // Don't check the current row 
        int columnToCheck = selectedCol + 1; // Don't check current column
        
        // While there are still rows and columns to check and if we haven't found our color on the other end
        while (rowToCheck >= 0 && columnToCheck < Constants.COLUMNS && !foundSameColorDisc) {
            
            // Stop checking when we stumble upon an empty tile
            if (board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY) {
                break;
            }
                
            // IIf the tile is of the opposite color, it gets flipped
            else if (board[rowToCheck][columnToCheck].getDiscColor() != currentPlayerColor) {
                tilesToFlip++;
            }
            
            // End the flipping sequence since the tile matches the current player's color
            else {
                foundSameColorDisc = true;
            }

            // Check the row above and the column to the right
            rowToCheck--;
            columnToCheck++;
        }
        
        // Valid move
        if (foundSameColorDisc && tilesToFlip > 0 && doFlip) {
            
            // Place disk on clicked tile
            board[selectedRow][selectedCol].setDiscColor(currentPlayerColor);
            
            while (tilesToFlip > 0) {
                selectedRow--;
                selectedCol++;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selectedCol].setDiscColor(currentPlayerColor);
            }
            isValidMove = true;
        }
        
        return isValidMove;
    }
    
    /**
     * Determine whether discs can be flipped in the east direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * @param doFlip control whether to flip tiles
     * 
     * @return whether the discs can be flipped in the east direction
     */
    private boolean tryFlippingEast(int selectedRow, int selectedCol, Color color, boolean doFlip) {
        boolean foundSameColorDisc = false;
        boolean isValidMove = false;
        
        int tilesToFlip = 0;
        int columnToCheck = selectedCol + 1; // Don't check the current column
        
        // While there are still columns to check and if we haven't found our color on the other end
        while (columnToCheck < Constants.COLUMNS && !foundSameColorDisc) {
            
            // Stop checking when we stumble upon an empty tile
            if (board[selectedRow][columnToCheck].getDiscColor() == Constants.EMPTY) {
                break;
            }
            
            // IIf the tile is of the opposite color, it gets flipped
            else if (board[selectedRow][columnToCheck].getDiscColor() != color) {
                tilesToFlip++;
            }
                
            // End the flipping sequence since the tile matches the current player's color
            else {
                foundSameColorDisc = true;
            }
            
            // Check the column to the right
            columnToCheck++;
        }
        
        // Valid move
        if (foundSameColorDisc && tilesToFlip > 0 && doFlip) {
            
            // Place disk on clicked tile
            board[selectedRow][selectedCol].setDiscColor(color);
            
            while (tilesToFlip > 0) {
                selectedCol++;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selectedCol].setDiscColor(color);
            }
            isValidMove = true;
        }
        
        return isValidMove;
    }
    
    /**
     * Determine whether discs can be flipped in the southeast direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * @param doFlip control whether to flip tiles
     * 
     * @return whether the discs can be flipped in the southeast direction
     */
    private boolean tryFlippingSouthEast(int selectedRow, int selectedCol, Color color, boolean doFlip) {
        boolean foundSameColorDisc = false;
        boolean isValidMove = false;
        
        int tilesToFlip = 0;
        int rowToCheck = selectedRow + 1; // Don't check the current row
        int columnToCheck = selectedCol + 1; // Don't check the current column
        
        // While there are still rows and columns to check and if we haven't found our color on the other end
        while (rowToCheck < Constants.ROWS && columnToCheck < Constants.COLUMNS && !foundSameColorDisc) {
            
            // Stop checking when we stumble upon an empty tile
            if (board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY) {
                break;
            }
            
            // IIf the tile is of the opposite color, it gets flipped
            else if (board[rowToCheck][columnToCheck].getDiscColor() != color) {
                tilesToFlip++;
            }
            
            // End the flipping sequence since the tile matches the current player's color
            else {
                foundSameColorDisc = true;
            }
                
            // Check the row below and the column to the right
            rowToCheck++;
            columnToCheck++;
        }
        
        // Valid move
        if (foundSameColorDisc && tilesToFlip > 0 && doFlip) {
            
            // Place disk on clicked tile
            board[selectedRow][selectedCol].setDiscColor(color);
            
            while (tilesToFlip > 0) {
                selectedRow++;
                selectedCol++;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selectedCol].setDiscColor(color);
            }
            isValidMove = true; 
        }

        return isValidMove;
    }
    
    /**
     * Determine whether discs can be flipped in the south direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * @param doFlip control whether to flip tiles
     * 
     * @return whether the discs can be flipped in the south direction
     */
    private boolean tryFlippingSouth(int selectedRow, int selectedCol, Color color, boolean doFlip) {
        boolean foundSameColorDisc = false;
        boolean isValidMove = false;
        
        int tilesToFlip = 0;
        int rowToCheck = selectedRow + 1; // Don't check the current row
        
        // While there are still rows to check and while we haven't found our color on the other end
        while (rowToCheck < Constants.ROWS && !foundSameColorDisc) {
            
            // Stop checking when we stumble upon an empty tile
            if (board[rowToCheck][selectedCol].getDiscColor() == Constants.EMPTY) {
                break;
            }
                
            
            // IIf the tile is of the opposite color, it gets flipped
            else if (board[rowToCheck][selectedCol].getDiscColor() != color) {
                tilesToFlip++;
            }
                
            // End the flipping sequence since the tile matches the current player's color
            else {
                foundSameColorDisc = true;
            }
                
            // Check the row below
            rowToCheck++;
        }
        
        // Valid move
        if (foundSameColorDisc && tilesToFlip > 0 && doFlip) {
            
            // Place disk on clicked tile
            board[selectedRow][selectedCol].setDiscColor(color);
            
            while (tilesToFlip > 0) {
                selectedRow++;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selectedCol].setDiscColor(color);
            }
            isValidMove = true;   
        }
        
        return isValidMove;
    }

    /**
     * Determine whether discs can be flipped in the southwest direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * @param doFlip control whether to flip tiles
     * 
     * @return whether the discs can be flipped in the southwest direction
     */
    private boolean tryFlippingSouthwest(int selectedRow, int selectedCol, Color color, boolean doFlip) {
        boolean foundSameColorDisc = false;
        boolean isValidMove = false;
        
        int tilesToFlip = 0;
        int rowToCheck = selectedRow + 1; // Don't check the current row
        int columnToCheck = selectedCol - 1; // Don't check the current column
        
        
        // While there are still rows to check and while we haven't found our color on the other end
        while (rowToCheck < Constants.ROWS && columnToCheck >= 0 && !foundSameColorDisc) {
            
            // Stop checking when we stumble upon an empty tile
            if (board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY) {
                break;
            }
                
            // IIf the tile is of the opposite color, it gets flipped
            else if (board[rowToCheck][columnToCheck].getDiscColor() != color) {
                tilesToFlip++;
            }
            
            // End the flipping sequence since the tile matches the current player's color.
            else {
                foundSameColorDisc = true;
            }
            
            // Check the row below and the column to the left
            rowToCheck++;
            columnToCheck--;
        }
        
        // Valid move
        if (foundSameColorDisc && tilesToFlip > 0 && doFlip) {
            
            // Place disk on clicked tile
            board[selectedRow][selectedCol].setDiscColor(color);
            
            while (tilesToFlip > 0) {
                selectedRow++;
                selectedCol--;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selectedCol].setDiscColor(color);
            }
            isValidMove = true;
        }
        
        return isValidMove;
    }
    
    /**
     * Determine whether discs can be flipped in the west direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * @param doFlip control whether to flip tiles
     * 
     * @return whether the discs can be flipped in the west direction
     */
    private boolean tryFlippingWest(int selectedRow, int selctedCol, Color currentPlayerColor, boolean doFlip) {
        boolean foundSameColorDisc = false;
        boolean isValidMove = false;
        
        int tilesToFlip = 0;
        int columnToCheck = selctedCol - 1; // Don't check the current column
        
        
        // While there are still rows to check and while we haven't found our color on the other end
        while (columnToCheck >= 0 && !foundSameColorDisc) {
            
            // Stop checking when we stumble upon an empty tile
            if (board[selectedRow][columnToCheck].getDiscColor() == Constants.EMPTY) {
                break;
            }
                
            // IIf the tile is of the opposite color, it gets flipped
            else if (board[selectedRow][columnToCheck].getDiscColor() != currentPlayerColor) {
                tilesToFlip++;
            }
            
            // End the flipping sequence since the tile matches the current player's color
            else {
                foundSameColorDisc = true;
            }
                
            // Check the column to the left
            columnToCheck--;
        }
        
        // Valid move
        if (foundSameColorDisc && tilesToFlip > 0 && doFlip) {
            
            // Place disk on clicked tile
            board[selectedRow][selctedCol].setDiscColor(currentPlayerColor);
            
            while (tilesToFlip > 0) {
                selctedCol--;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selctedCol].setDiscColor(currentPlayerColor);
            }
            isValidMove = true;
        }

        return isValidMove;
    }
    
    /**
     * Determine whether discs can be flipped in the northwest direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * @param doFlip control whether to flip tiles
     * 
     * @return whether the discs can be flipped in the northwest direction
     */
    private boolean tryFlippingNorthwest(int selectedRow, int selectedCol, Color color, boolean doFlip) {
        boolean foundSameColorDisc = false;
        boolean isValidMove = false;
        
        int tilesToFlip = 0;
        int rowToCheck = selectedRow - 1; // Don't check the current row
        int columnToCheck = selectedCol - 1; // Don't check the current column
        
        
        // While there are still rows to check and while we haven't found our color on the other end
        while (rowToCheck >= 0 && columnToCheck >= 0 && !foundSameColorDisc) {
            
            // Stop checking when we stumble upon an empty tile
            if (board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY) {
                break;
            }
            
            // IIf the tile is of the opposite color, it gets flipped
            else if (board[rowToCheck][columnToCheck].getDiscColor() != color) {
                tilesToFlip++;
            }
            
            // End the flipping sequence since the tile matches the current player's color
            else {
                foundSameColorDisc = true;
            }
                
            // Check the row above and the column to the left
            rowToCheck--;
            columnToCheck--;
        }
        
        // Valid move
        if (foundSameColorDisc && tilesToFlip > 0 && doFlip) {
            
            // Place disk on clicked tile
            board[selectedRow][selectedCol].setDiscColor(color);
            
            while (tilesToFlip > 0) {
                selectedRow--;
                selectedCol--;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selectedCol].setDiscColor(color);
            }
            isValidMove = true;
        }
            
        return isValidMove;
    }
    
    /**
     * Determine whether the game is over
     * 
     * @param currentPlayerColor the color of the current player
     * @param darkScore the score of the dark player
     * @param lightScore the score of the light player
     * @return whether the game is over
     */
    public boolean gameOver(Color currentPlayerColor, int darkScore, int lightScore) {
        // Game over if the board is full
        if (darkScore + lightScore == 64) {
            return true;
        }
            
        // Game over if either player lost all of their chips
        if (darkScore == 0 || lightScore == 0) {
            return true;
        }
            
        // Check whether the current player still has a valid move left
        if (hasMove(currentPlayerColor)) {
            return false;
        }
            
        //Otherwise, the game has ended.
        else {
            return true;
        }
    }
    
    /**
     * 
     * @param currentPlayerColor the color of the current player
     * @return whether there is still a valid move left to play for the current player
     */
    private boolean hasMove(Color currentPlayerColor) {
        
        // Check all directions from each empty square on the board if there is still a valid move
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLUMNS; col++) {
                
                if (board[row][col].getDiscColor() == Constants.EMPTY) {
                    
                    /**
                     * @note None of these methods have been refactored or restyled to my current standards
                     */
                    if (hasValidMoveNorth(row, col, currentPlayerColor) ||
                        hasValidMoveNorthEast(row, col, currentPlayerColor) ||
                        hasValidMoveEast(row, col, currentPlayerColor) ||
                        hasValidMoveSouthEast(row, col, currentPlayerColor) ||
                        hasValidMoveSouth(row, col, currentPlayerColor) ||
                        hasValidMoveSouthWest(row, col, currentPlayerColor) ||
                        hasValidMoveWest(row, col, currentPlayerColor) ||
                        hasValidMoveNorthWest(row, col, currentPlayerColor)) {
                        
                            return true;
                    }
                }
            }
        }

        // Game is over if there were no empty tiles or if there were no valid moves
        return false;
    }
    
    /**
     * Check whether there is a valid move to the north of a given empty tile
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * 
     * @return whether there is a valid move in the north direction from the given empty tile
     */
    private boolean hasValidMoveNorth(int selectedRow, int selectedCol, Color currentPlayerColor) {
        boolean isValidMove = false;
        
        int rowToCheck = selectedRow - 1; // Don't check the current row
        
        // Check that the row is within boundaries
        if (rowToCheck >= 0 && rowToCheck < Constants.ROWS)
        {
            // Stop checking when we stumble upon an empty tile or contains same color
            if (board[rowToCheck][selectedCol].getDiscColor() == Constants.EMPTY ||
                board[rowToCheck][selectedCol].getDiscColor() == currentPlayerColor) {
                
                    isValidMove = false;
            }
            
            // Valid move since adjacent tile must be occupied by enemy
            else {   
                // For as long as we are inbounds and run into discs of the opposing color, then keep checking in this direction
                do
                {
                    rowToCheck--;
                } while (rowToCheck >= 0 && board[rowToCheck][selectedCol].getDiscColor() != Constants.EMPTY //Two parameters.
                         && board[rowToCheck][selectedCol].getDiscColor() != currentPlayerColor); //One parameter.
                
                // If the current square is out-of-bounds or empty, then the move is not valid
                if (rowToCheck < 0 || board[rowToCheck][selectedCol].getDiscColor() == Constants.EMPTY)
                    return false;
                
                // Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs
                else
                    return true;
                
            }
        }
        
        // We are not in bounds, so this is definitely not a valid direction.
        else
            isValidMove = false;
        
        return isValidMove;
    }
    
    /**
     * Check whether there is a valid move to the northwest of a given empty tile
     * 
     * @param row the row of the selected tile
     * @param col the column of the selected tile
     * @param color the color of the current player
     * 
     * @return whether there is a valid move in the northwest direction from the given empty tile
     */
    private boolean hasValidMoveNorthWest(int row, int col, Color color)
    {
        //There's no need to check the current square.
        int rowToCheck = row - 1;
        int columnToCheck = col - 1;
        
        //Make sure we are within array boundaries.
        if ( (rowToCheck >= 0 && rowToCheck < Constants.ROWS) && (columnToCheck >= 0 && columnToCheck < Constants.COLUMNS) )
        {
            //The immediately adjacent square is empty or contains the same colored disc, so there is no valid move in this direction.
            if (board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY || //first parameter
                board[rowToCheck][columnToCheck].getDiscColor() == color) //second parameter
                return false;
            
            //Otherwise, that immediately adjacent square must be occupied by an opposing disc.
            else
            {   
                //For as long as we are inbounds and run into discs of the opposing color, then keep checking in this direction.
                do
                {
                    rowToCheck--;
                    columnToCheck--;
                } while (rowToCheck >= 0 && columnToCheck >= 0 && //Two parameters.
                         board[rowToCheck][columnToCheck].getDiscColor() != Constants.EMPTY && //One parameter.
                         board[rowToCheck][columnToCheck].getDiscColor() != color); //One parameter.
                
                //If the current square is out-of-bounds or empty, then the move is not valid.
                if (rowToCheck < 0 || columnToCheck < 0 || board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY)
                    return false;
                
                //Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs.
                else
                    return true;
                
            }//Closes opposing disc else statement.
        }//Closes array boundaries if statement.
        
        //We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }
    
    /**
     * Check whether there is a valid move to the west of a given empty tile
     * 
     * @param row the row of the selected tile
     * @param col the column of the selected tile
     * @param color the color of the current player
     * 
     * @return whether there is a valid move in the west direction from the given empty tile
     */
    private boolean hasValidMoveWest(int row, int col, Color color)
    {
        // There's no need to check the current square.
        int columnToCheck = col - 1;
        
        // Make sure we are within array boundaries.
        if (columnToCheck >= 0 && columnToCheck < Constants.COLUMNS)
        {
            // The immediately adjacent square is empty or contains the same colored disc, so there is no valid move in this direction.
            if (board[row][columnToCheck].getDiscColor() == Constants.EMPTY || //first parameter
                board[row][columnToCheck].getDiscColor() == color) //second parameter
                return false;
            
            //Otherwise, that immediately adjacent square must be occupied by an opposing disc.
            else
            {   
                //For as long as we are inbounds and run into discs of the opposing color, then keep checking in this direction.
                do
                {
                    columnToCheck--;
                } while (columnToCheck >= 0 && board[row][columnToCheck].getDiscColor() != Constants.EMPTY //Two parameters.
                         && board[row][columnToCheck].getDiscColor() != color); //One parameter.
                
                //If the current square is out-of-bounds or empty, then the move is not valid.
                if (columnToCheck < 0 || board[row][columnToCheck].getDiscColor() == Constants.EMPTY)
                    return false;
                
                //Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs.
                else
                    return true;
                
            }
        }
        
        //We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }
    
    /**
     * Check whether there is a valid move to the southwest of a given empty tile
     * 
     * @param row the row of the selected tile
     * @param col the column of the selected tile
     * @param color the color of the current player
     * 
     * @return whether there is a valid move in the southwest direction from the given empty tile
     */
    private boolean hasValidMoveSouthWest(int row, int col, Color color)
    {
        // There's no need to check the current square.
        int rowToCheck = row + 1;
        int columnToCheck = col - 1;
        
        // Make sure we are within array boundaries.
        if ( (rowToCheck >= 0 && rowToCheck < Constants.ROWS) && (columnToCheck >= 0 && columnToCheck < Constants.COLUMNS) )
        {
            // The immediately adjacent square is empty or contains the same colored disc, so there is no valid move in this direction.
            if (board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY || //first parameter
                board[rowToCheck][columnToCheck].getDiscColor() == color) //second parameter
                return false;
            
            // Otherwise, that immediately adjacent square must be occupied by an opposing disc.
            else
            {   
                // For as long as we are inbounds and run into discs of the opposing color, then keep checking in this direction.
                do
                {
                    rowToCheck++;
                    columnToCheck--;
                } while (rowToCheck < Constants.ROWS && columnToCheck >= 0 && //Two parameters.
                         board[rowToCheck][columnToCheck].getDiscColor() != Constants.EMPTY && //One parameter.
                         board[rowToCheck][columnToCheck].getDiscColor() != color); //One parameter.
                
                // If the current square is out-of-bounds or empty, then the move is not valid.
                if (rowToCheck >= Constants.ROWS || columnToCheck < 0 || board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY)
                    return false;
                
                // Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs.
                else
                    return true;
                
            }
        }
        
        // We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }
    
    /**
     * Check whether there is a valid move to the south of a given empty tile
     * 
     * @param row the row of the selected tile
     * @param col the column of the selected tile
     * @param color the color of the current player
     * 
     * @return whether there is a valid move in the south direction from the given empty tile
     */
    private boolean hasValidMoveSouth(int row, int col, Color color)
    {
        // There's no need to check the current square.
        int rowToCheck = row + 1;
        
        // Make sure we are within array boundaries.
        if (rowToCheck >= 0 && rowToCheck < Constants.ROWS)
        {
            // The immediately adjacent square is empty or contains the same colored disc, so there is no valid move in this direction.
            if (board[rowToCheck][col].getDiscColor() == Constants.EMPTY || //first parameter
                board[rowToCheck][col].getDiscColor() == color) //second parameter
                return false;
            
            // Otherwise, that immediately adjacent square must be occupied by an opposing disc.
            else
            {   
                // For as long as we are inbounds and run into discs of the opposing color, then keep checking in this direction.
                do
                {
                    rowToCheck++;
                } while (rowToCheck < Constants.ROWS && board[rowToCheck][col].getDiscColor() != Constants.EMPTY //Two parameters.
                         && board[rowToCheck][col].getDiscColor() != color); //One parameter.
                
                // If the current square is out-of-bounds or empty, then the move is not valid.
                if (rowToCheck >= Constants.ROWS || board[rowToCheck][col].getDiscColor() == Constants.EMPTY)
                    return false;
                
                // Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs.
                else
                    return true;
                
            }
        }
        
        // We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }
    
    /**
     * Check whether there is a valid move to the southeast of a given empty tile
     * 
     * @param row the row of the selected tile
     * @param col the column of the selected tile
     * @param color the color of the current player
     * 
     * @return whether there is a valid move in the southeast direction from the given empty tile
     */
    private boolean hasValidMoveSouthEast(int row, int col, Color color)
    {
        // There's no need to check the current square.
        int rowToCheck = row + 1;
        int columnToCheck = col + 1;
        
        // Make sure we are within array boundaries.
        if ( (rowToCheck >= 0 && rowToCheck < Constants.ROWS) && (columnToCheck >= 0 && columnToCheck < Constants.COLUMNS) )
        {
            // The immediately adjacent square is empty or contains the same colored disc, so there is no valid move in this direction.
            if (board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY || //first parameter
                board[rowToCheck][columnToCheck].getDiscColor() == color) //second parameter
                return false;
            
            // Otherwise, that immediately adjacent square must be occupied by an opposing disc.
            else
            {   
                // For as long as we are inbounds and run into discs of the opposing color, then keep checking in this direction.
                do
                {
                    rowToCheck++;
                    columnToCheck++;
                } while (rowToCheck < Constants.ROWS && columnToCheck < Constants.COLUMNS && //Two parameters.
                         board[rowToCheck][columnToCheck].getDiscColor() != Constants.EMPTY && //One parameter.
                         board[rowToCheck][columnToCheck].getDiscColor() != color); //One parameter.
                
                // If the current square is out-of-bounds or empty, then the move is not valid.
                if (rowToCheck >= Constants.ROWS || columnToCheck >= Constants.COLUMNS || board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY)
                    return false;
                
                // Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs.
                else
                    return true;
                
            }
        }
        
        // We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }
    
    /**
     * Check whether there is a valid move to the east of a given empty tile
     * 
     * @param row the row of the selected tile
     * @param col the column of the selected tile
     * @param color the color of the current player
     * 
     * @return whether there is a valid move in the east direction from the given empty tile
     */
    private boolean hasValidMoveEast(int row, int col, Color color)
    {
        // There's no need to check the current square.
        int columnToCheck = col + 1;
        
        // Make sure we are within array boundaries.
        if (columnToCheck >= 0 && columnToCheck < Constants.COLUMNS)
        {
            // The immediately adjacent square is empty or contains the same colored disc, so there is no valid move in this direction.
            if (board[row][columnToCheck].getDiscColor() == Constants.EMPTY || //first parameter
                board[row][columnToCheck].getDiscColor() == color) //second parameter
                return false;
            
            // Otherwise, that immediately adjacent square must be occupied by an opposing disc.
            else
            {   
                // For as long as we are inbounds and run into discs of the opposing color, then keep checking in this direction.
                do
                {
                    columnToCheck++;
                } while (columnToCheck < Constants.COLUMNS && board[row][columnToCheck].getDiscColor() != Constants.EMPTY //Two parameters.
                         && board[row][columnToCheck].getDiscColor() != color); //One parameter.
                
                // If the current square is out-of-bounds or empty, then the move is not valid.
                if (columnToCheck >= Constants.COLUMNS || board[row][columnToCheck].getDiscColor() == Constants.EMPTY)
                    return false;
                
                // Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs.
                else
                    return true;
                
            }
        }
        
        // We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }
    
    /**
     * Check whether there is a valid move to the northeast of a given empty tile
     * 
     * @param row the row of the selected tile
     * @param col the column of the selected tile
     * @param color the color of the current player
     * 
     * @return whether there is a valid move in the northeast direction from the given empty tile
     */
    private boolean hasValidMoveNorthEast(int row, int col, Color color)
    {
        // There's no need to check the current square.
        int rowToCheck = row - 1;
        int columnToCheck = col + 1;
        
        // Make sure we are within array boundaries.
        if ( (rowToCheck >= 0 && rowToCheck < Constants.ROWS) && (columnToCheck >= 0 && columnToCheck < Constants.COLUMNS) )
        {
            // The immediately adjacent square is empty or contains the same colored disc, so there is no valid move in this direction.
            if (board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY || //first parameter
                board[rowToCheck][columnToCheck].getDiscColor() == color) //second parameter
                return false;
            
            // Otherwise, that immediately adjacent square must be occupied by an opposing disc.
            else
            {   
                // For as long as we are inbounds and run into discs of the opposing color, then keep checking in this direction.
                do
                {
                    rowToCheck--;
                    columnToCheck++;
                } while (rowToCheck >= 0 && columnToCheck < Constants.COLUMNS && //Two parameters.
                         board[rowToCheck][columnToCheck].getDiscColor() != Constants.EMPTY && //One parameter.
                         board[rowToCheck][columnToCheck].getDiscColor() != color); //One parameter.
                
                // If the current square is out-of-bounds or empty, then the move is not valid.
                if (rowToCheck < 0 || columnToCheck >= Constants.COLUMNS || board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY)
                    return false;
                
                // Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs.
                else
                    return true;
                
            }
        }
        
        // We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }
    
    /**
     * @return the board
     */
    public Disc[][] getBoard() {
        return board;
    }

    /**
     * @param board the board to set
     */
    public void setBoard(Disc[][] board) {
        this.board = board;
    }

    /**
     * @return the darkCount
     */
    public int getDarkCount()
    {
        return darkCount;
    }

    /**
     * @param darkCount the darkCount to set
     */
    public void setDarkCount(int darkCount) {
        this.darkCount = darkCount;
    }

    /**
     * @return the lightCount
     */
    public int getLightCount() {
        return lightCount;
    }

    /**
     * @param lightCount the lightCount to set
     */
    public void setLightCount(int lightCount) {
        this.lightCount = lightCount;
    }

    /**
     * @return the players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * @param players the players to set
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
    
    private Disc[][] board;
    private int darkCount;
    private int lightCount;
    private ArrayList<Player> players;
}
