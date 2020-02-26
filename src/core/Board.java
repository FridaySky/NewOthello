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
        if (checkUp(selectedRow, selectedCol, currentPlayerColor) ||
            checkUpLeft(selectedRow, selectedCol, currentPlayerColor) ||
            checkLeft(selectedRow, selectedCol, currentPlayerColor) ||
            checkDownLeft(selectedRow, selectedCol, currentPlayerColor) ||
            checkDown(selectedRow, selectedCol, currentPlayerColor) ||
            checkDownRight(selectedRow, selectedCol, currentPlayerColor) ||
            checkRight(selectedRow, selectedCol, currentPlayerColor) ||
            checkUpRight(selectedRow, selectedCol, currentPlayerColor)) {
            
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
     * Determine whether discs can be flipped in the up direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * 
     * @return whether the discs can be flipped in the up direction
     */
    private boolean checkUp(int selectedRow, int selectedCol, Color currentPlayerColor)
    {
        boolean matchFound = false;
        boolean validMove = false;
        
        int tilesToFlip = 0;
        int rowToCheck = selectedRow - 1; // Don't check the current row
        
        // While there are still rows to check and if we haven't found our color on the other end
        while (rowToCheck >= 0 && !matchFound) {
            
            // Invalid move if adjacent tile is empty
            if (board[rowToCheck][selectedCol].getDiscColor() == Constants.EMPTY) {
                return validMove;
            }
                
            
            // If the next square is of the opposite color, that square gets flipped
            else if (board[rowToCheck][selectedCol].getDiscColor() != currentPlayerColor) {
                tilesToFlip++;
            }
                
            
            // Ends the flipping sequence since we've ran into a square that matches the current player's color
            else {
                matchFound = true;
            }
                
            // Check the row above
            rowToCheck--;
        }
        
        // Valid move
        if (matchFound && tilesToFlip > 0) {
            board[selectedRow][selectedCol].setDiscColor(currentPlayerColor);

            while (tilesToFlip > 0) {
                selectedRow--;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selectedCol].setDiscColor(currentPlayerColor);
            }
            validMove = true;
        } else {
            validMove = false;
        }
        
        return validMove;
    }

    /**
     * Determine whether discs can be flipped in the northwest direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * 
     * @return whether the discs can be flipped in the northwest direction
     */
    private boolean checkUpLeft(int selectedRow, int selectedCol, Color color)
    {
        boolean matchFound = false;
        boolean validMove = false;
        
        int tilesToFlip = 0;
        int rowToCheck = selectedRow - 1; // Don't check the current row
        int columnToCheck = selectedCol - 1; // Don't check the current column
        
        
        // While there are still rows to check and if we haven't found our color on the other end
        while (rowToCheck >= 0 && columnToCheck >= 0 && !matchFound) {
            
            // Invalid move if adjacent tile is empty
            if (board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY) {
                return validMove;
            }
            
            // If the next square is of the opposite color, that square gets flipped
            else if (board[rowToCheck][columnToCheck].getDiscColor() != color) {
                tilesToFlip++;
            }
            
            // Ends the flipping sequence since we've ran into a square that matches the current player's color
            else {
                matchFound = true;
            }
                
            // Check the row above and the column to the left
            rowToCheck--;
            columnToCheck--;
        }
        
        // Valid move
        if (matchFound && tilesToFlip > 0) {
            board[selectedRow][selectedCol].setDiscColor(color);
            
            while (tilesToFlip > 0) {
                selectedRow--;
                selectedCol--;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selectedCol].setDiscColor(color);
            }
            validMove = true;
        } else {
            validMove = false;
        }
            
        return validMove;
    }

    /**
     * Determine whether discs can be flipped in the left direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * 
     * @return whether the discs can be flipped in the left direction
     */
    private boolean checkLeft(int selectedRow, int selctedCol, Color color)
    {
        boolean matchFound = false;
        boolean validMove = false;
        
        int tilesToFlip = 0;
        int columnToCheck = selctedCol - 1; // Don't check the current column
        
        
        // While there are still rows to check and if we haven't found our color on the other end
        while (columnToCheck >= 0 && !matchFound) {
            
            // Invalid move if adjacent tile is empty
            if (board[selectedRow][columnToCheck].getDiscColor() == Constants.EMPTY) {
                return validMove;
            }
                
            // If the next square is of the opposite color, that square gets flipped
            else if (board[selectedRow][columnToCheck].getDiscColor() != color) {
                tilesToFlip++;
            }
            
            // Ends the flipping sequence since we've ran into a square that matches the current player's color
            else {
                matchFound = true;
            }
                
            // Check the column to the left
            columnToCheck--;
        }
        
        // Valid move
        if (matchFound && tilesToFlip > 0) {
            board[selectedRow][selctedCol].setDiscColor(color);
            
            while (tilesToFlip > 0) {
                selctedCol--;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selctedCol].setDiscColor(color);
            }
            validMove = true;
        } else {
            validMove = false;
        }

        return validMove;
    }

    /**
     * Determine whether discs can be flipped in the southwest direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * 
     * @return whether the discs can be flipped in the southwest direction
     */
    private boolean checkDownLeft(int selectedRow, int selectedCol, Color color)
    {
        boolean matchFound = false;
        boolean validMove = false;
        
        int tilesToFlip = 0;
        int rowToCheck = selectedRow + 1; // Don't check the current row
        int columnToCheck = selectedCol - 1; // Don't check the current column
        
        
        // While there are still rows to check and if we haven't found our color on the other end
        while (rowToCheck < Constants.ROWS && columnToCheck >= 0 && !matchFound) {
            
            // Invalid move if adjacent tile is empty
            if (board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY) {
                return validMove;
            }
                
            // If the next square is of the opposite color, that square gets flipped
            else if (board[rowToCheck][columnToCheck].getDiscColor() != color) {
                tilesToFlip++;
            }
            
            // Ends the flipping sequence since we've ran into a square that matches the current player's color.
            else {
                matchFound = true;
            }
            
            // Check the row below and the column to the left
            rowToCheck++;
            columnToCheck--;
        }
        
        // Valid move
        if (matchFound && tilesToFlip > 0) {
            board[selectedRow][selectedCol].setDiscColor(color);
            
            while (tilesToFlip > 0) {
                selectedRow++;
                selectedCol--;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selectedCol].setDiscColor(color);
            }
            validMove = true;
        } else {
            validMove = false;
        }
        
        return validMove;
    }
    
    /**
     * Determine whether discs can be flipped in the down direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * 
     * @return whether the discs can be flipped in the down direction
     */
    private boolean checkDown(int selectedRow, int selectedCol, Color color)
    {
        boolean matchFound = false;
        boolean validMove = false;
        
        int tilesToFlip = 0;
        int rowToCheck = selectedRow + 1; // Don't check the current row
        
        // While there are still rows to check and if we haven't found our color on the other end
        while (rowToCheck < Constants.ROWS && !matchFound) {
            
            // Invalid move if adjacent tile is empty
            if (board[rowToCheck][selectedCol].getDiscColor() == Constants.EMPTY) {
                return validMove;
            }
                
            
            // If the next square is of the opposite color, that square gets flipped
            else if (board[rowToCheck][selectedCol].getDiscColor() != color) {
                tilesToFlip++;
            }
                
            // Ends the flipping sequence since we've ran into a square that matches the current player's color
            else {
                matchFound = true;
            }
                
            // Check the row below
            rowToCheck++;
        }
        
        // Valid move
        if (matchFound && tilesToFlip > 0) {
            board[selectedRow][selectedCol].setDiscColor(color);
            
            while (tilesToFlip > 0) {
                selectedRow++;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selectedCol].setDiscColor(color);
            }
            validMove = true;   
        } else {
            validMove = false;
        }
        
        return validMove;
    }
    
    /**
     * Determine whether discs can be flipped in the southeast direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * 
     * @return whether the discs can be flipped in the southeast direction
     */
    private boolean checkDownRight(int selectedRow, int selectedCol, Color color)
    {
        boolean matchFound = false;
        boolean validMove = false;
        
        int tilesToFlip = 0;
        int rowToCheck = selectedRow + 1; // Don't check the current row
        int columnToCheck = selectedCol + 1; // Don't check the current column
        
        
        // While there are still rows and columns to check and if we haven't found our color on the other end
        while (rowToCheck < Constants.ROWS && columnToCheck < Constants.COLUMNS && !matchFound) {
            
            // Invalid move if adjacent tile is empty
            if (board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY) {
                return validMove;
            }
            
            // If the next square is of the opposite color, that square gets flipped
            else if (board[rowToCheck][columnToCheck].getDiscColor() != color) {
                tilesToFlip++;
            }
            
            // Ends the flipping sequence since we've ran into a square that matches the current player's color
            else {
                matchFound = true;
            }
                
            // Check the row below and the column to the right
            rowToCheck++;
            columnToCheck++;
        }
        
        // Valid move
        if (matchFound && tilesToFlip > 0) {
            board[selectedRow][selectedCol].setDiscColor(color);
            
            while (tilesToFlip > 0) {
                selectedRow++;
                selectedCol++;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selectedCol].setDiscColor(color);
            }
            validMove = true; 
        } else {
            validMove = false;
        }

        return validMove;
    }
    
    /**
     * Determine whether discs can be flipped in the right direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * 
     * @return whether the discs can be flipped in the right direction
     */
    private boolean checkRight(int selectedRow, int selectedCol, Color color)
    {
        boolean matchFound = false;
        boolean validMove = false;
        
        int tilesToFlip = 0;
        int columnToCheck = selectedCol + 1; // Don't check the current column
        
        // While there are still columns to check and if we haven't found our color on the other end
        while (columnToCheck < Constants.COLUMNS && !matchFound) {
            
            // Invalid move if adjacent tile is empty
            if (board[selectedRow][columnToCheck].getDiscColor() == Constants.EMPTY) {
                return validMove;
            }
            
            // If the next square is of the opposite color, that square gets flipped
            else if (board[selectedRow][columnToCheck].getDiscColor() != color) {
                tilesToFlip++;
            }
                
            // Ends the flipping sequence since we've ran into a square that matches the current player's color
            else {
                matchFound = true;
            }
            
            // Check the column to the right
            columnToCheck++;
        }
        
        // Valid move
        if (matchFound && tilesToFlip > 0) {
            board[selectedRow][selectedCol].setDiscColor(color);
            
            while (tilesToFlip > 0) {
                selectedCol++;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[selectedRow][selectedCol].setDiscColor(color);
            }
            validMove = true;
        } else {
            validMove = false;
        }
        
        return validMove;
    }
    
    
    /**
     * Determine whether discs can be flipped in the left direction
     * 
     * @param selectedRow the row of the selected tile
     * @param selectedCol the column of the selected tile
     * @param currentPlayerColor the color of the current player
     * 
     * @return whether the discs can be flipped in the left direction
     */
    private boolean checkUpRight(int row, int col, Color color)
    {
        boolean matchFound = false;
        boolean validMove = false;

        int tilesToFlip = 0;
        int rowToCheck = row - 1; // Don't check the current row 
        int columnToCheck = col + 1; // Don't check current column
        
        // While there are still rows and columns to check and if we haven't found our color on the other end
        while (rowToCheck >= 0 && columnToCheck < Constants.COLUMNS && !matchFound) {
            
            // Invalid move if adjacent tile is empty
            if (board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY) {
                return validMove;
            }
                
            // If the next square is of the opposite color, that square gets flipped
            else if (board[rowToCheck][columnToCheck].getDiscColor() != color) {
                tilesToFlip++;
            }
            
            // Ends the flipping sequence since we've ran into a square that matches the current player's color
            else {
                matchFound = true;
            }

            // Check the row above and the column to the right
            rowToCheck--;
            columnToCheck++;
        }
        
        // Valid move
        if (matchFound && tilesToFlip > 0) {
            board[row][col].setDiscColor(color);
            
            while (tilesToFlip > 0) {
                row--;
                col++;
                tilesToFlip--;
                
                // Update the disc color for the flipped discs
                board[row][col].setDiscColor(color);
            }
            validMove = true;
        } else {
            validMove = false;
        }
        
        return validMove;
    }
    
    /**
     * Determine whether the game is over
     * 
     * @param currentPlayerColor the color of the current player
     * @param darkScore the score of the dark player
     * @param lightScore the score of the light player
     * @return whether the game is over
     */
    public boolean gameOver(Color currentPlayerColor, int darkScore, int lightScore)
    {
        // Game over if the board is full
        if (darkScore + lightScore == 64) {
            return true;
        }
            
        // Game over if either player lost all of their chips
        if (darkScore == 0 || lightScore == 0) {
            return true;
        }
            
        //This helper method will check whether the current player still has a valid move to be played
        //and will short-circuit and return false if there is still a valid move to be played.
        if (hasMove(currentPlayerColor)) {
            return false;
        }
            
        //Otherwise, the game has ended.
        else {
            return true;
        }
        
    }///Closes gameOver method.
    
    /*
        This helper method is used in gameOver to check if the current player still has a valid move to be played.
    */
    private boolean hasMove(Color color)
    {
        //Loop through the board and find an empty square on the grid.
        //Run the checkDirection functions from there, and return true if there is a valid move.
        for (int row = 0; row < Constants.ROWS; row++)
        {
            for (int col = 0; col < Constants.COLUMNS; col++)
            {
                if (board[row][col].getDiscColor() == Constants.EMPTY)
                {
                    if (validUp(row, col, color)) //1
                        return true;
                    if (validUpLeft(row, col, color))//2
                        return true;
                    if (validLeft(row, col, color)) //3
                        return true;
                    if (validDownLeft(row, col, color)) //4
                        return true;
                    if (validDown(row, col, color)) //5
                        return true;
                    if (validDownRight(row, col, color)) //6
                        return true;
                    if (validRight(row, col, color)) //7
                        return true;
                    if (validUpRight(row, col, color)) //8
                        return true;
                }//Closes search for empty square.
            }//Closes column loop.
        }//Closes row loop.

        //If there wasn't an empty square, then the game must be over by default.
        //Or if we weren't able to return up there because there were no more valid moves, then also return false.
        return false;
        
    }///Closes hasMove method.
    
    /*
        This method check for whether there is a valid move in this direction in the event a disc is placed at row and col.
    */
    private boolean validUp(int row, int col, Color color)
    {
        //There's no need to check the current square.
        int rowToCheck = row - 1;
        
        //Make sure we are within array boundaries.
        if (rowToCheck >= 0 && rowToCheck < Constants.ROWS)
        {
            //The immediately adjacent square is empty or contains the same colored disc, so there is no valid move in this direction.
            if (board[rowToCheck][col].getDiscColor() == Constants.EMPTY || //first parameter
                board[rowToCheck][col].getDiscColor() == color) //second parameter
                return false;
            
            //Otherwise, that immediately adjacent square must be occupied by an opposing disc.
            else
            {   
                //For as long as we are inbounds and run into discs of the opposing color, then keep checking in this direction.
                do
                {
                    rowToCheck--;
                } while (rowToCheck >= 0 && board[rowToCheck][col].getDiscColor() != Constants.EMPTY //Two parameters.
                         && board[rowToCheck][col].getDiscColor() != color); //One parameter.
                
                //If the current square is out-of-bounds or empty, then the move is not valid.
                if (rowToCheck < 0 || board[rowToCheck][col].getDiscColor() == Constants.EMPTY)
                    return false;
                
                //Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs.
                else
                    return true;
                
            }//Closes opposing disc else statement.
        }//Closes array boundaries if statement.
        
        //We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }///Closes validUp method.
    
    /*
        This method check for whether there is a valid move in this direction if a disc was placed at row and col.
    */
    private boolean validUpLeft(int row, int col, Color color)
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
        
    }///Closes validUpLeft method.
    
    /*
        This method check for whether there is a valid move in this direction if a disc was placed at row and col.
    */
    private boolean validLeft(int row, int col, Color color)
    {
        //There's no need to check the current square.
        int columnToCheck = col - 1;
        
        //Make sure we are within array boundaries.
        if (columnToCheck >= 0 && columnToCheck < Constants.COLUMNS)
        {
            //The immediately adjacent square is empty or contains the same colored disc, so there is no valid move in this direction.
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
                
            }//Closes opposing disc else statement.
        }//Closes array boundaries if statement.
        
        //We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }///Closes validLeft method.
    
    /*
        This method check for whether there is a valid move in this direction if a disc was placed at row and col.
    */
    private boolean validDownLeft(int row, int col, Color color)
    {
        //There's no need to check the current square.
        int rowToCheck = row + 1;
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
                    rowToCheck++;
                    columnToCheck--;
                } while (rowToCheck < Constants.ROWS && columnToCheck >= 0 && //Two parameters.
                         board[rowToCheck][columnToCheck].getDiscColor() != Constants.EMPTY && //One parameter.
                         board[rowToCheck][columnToCheck].getDiscColor() != color); //One parameter.
                
                //If the current square is out-of-bounds or empty, then the move is not valid.
                if (rowToCheck >= Constants.ROWS || columnToCheck < 0 || board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY)
                    return false;
                
                //Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs.
                else
                    return true;
                
            }//Closes opposing disc else statement.
        }//Closes array boundaries if statement.
        
        //We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }///Closes validDownLeft method.
    
    /*
        This method check for whether there is a valid move in this direction if a disc was placed at row and col.
    */
    private boolean validDown(int row, int col, Color color)
    {
        //There's no need to check the current square.
        int rowToCheck = row + 1;
        
        //Make sure we are within array boundaries.
        if (rowToCheck >= 0 && rowToCheck < Constants.ROWS)
        {
            //The immediately adjacent square is empty or contains the same colored disc, so there is no valid move in this direction.
            if (board[rowToCheck][col].getDiscColor() == Constants.EMPTY || //first parameter
                board[rowToCheck][col].getDiscColor() == color) //second parameter
                return false;
            
            //Otherwise, that immediately adjacent square must be occupied by an opposing disc.
            else
            {   
                //For as long as we are inbounds and run into discs of the opposing color, then keep checking in this direction.
                do
                {
                    rowToCheck++;
                } while (rowToCheck < Constants.ROWS && board[rowToCheck][col].getDiscColor() != Constants.EMPTY //Two parameters.
                         && board[rowToCheck][col].getDiscColor() != color); //One parameter.
                
                //If the current square is out-of-bounds or empty, then the move is not valid.
                if (rowToCheck >= Constants.ROWS || board[rowToCheck][col].getDiscColor() == Constants.EMPTY)
                    return false;
                
                //Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs.
                else
                    return true;
                
            }//Closes opposing disc else statement.
        }//Closes array boundaries if statement.
        
        //We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }///Closes validDown method.
    
    /*
        This method check for whether there is a valid move in this direction if a disc was placed at row and col.
    */
    private boolean validDownRight(int row, int col, Color color)
    {
        //There's no need to check the current square.
        int rowToCheck = row + 1;
        int columnToCheck = col + 1;
        
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
                    rowToCheck++;
                    columnToCheck++;
                } while (rowToCheck < Constants.ROWS && columnToCheck < Constants.COLUMNS && //Two parameters.
                         board[rowToCheck][columnToCheck].getDiscColor() != Constants.EMPTY && //One parameter.
                         board[rowToCheck][columnToCheck].getDiscColor() != color); //One parameter.
                
                //If the current square is out-of-bounds or empty, then the move is not valid.
                if (rowToCheck >= Constants.ROWS || columnToCheck >= Constants.COLUMNS || board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY)
                    return false;
                
                //Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs.
                else
                    return true;
                
            }//Closes opposing disc else statement.
        }//Closes array boundaries if statement.
        
        //We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }///Closes validDownRight method.
    
    /*
        This method check for whether there is a valid move in this direction if a disc was placed at row and col.
    */
    private boolean validRight(int row, int col, Color color)
    {
        //There's no need to check the current square.
        int columnToCheck = col + 1;
        
        //Make sure we are within array boundaries.
        if (columnToCheck >= 0 && columnToCheck < Constants.COLUMNS)
        {
            //The immediately adjacent square is empty or contains the same colored disc, so there is no valid move in this direction.
            if (board[row][columnToCheck].getDiscColor() == Constants.EMPTY || //first parameter
                board[row][columnToCheck].getDiscColor() == color) //second parameter
                return false;
            
            //Otherwise, that immediately adjacent square must be occupied by an opposing disc.
            else
            {   
                //For as long as we are inbounds and run into discs of the opposing color, then keep checking in this direction.
                do
                {
                    columnToCheck++;
                } while (columnToCheck < Constants.COLUMNS && board[row][columnToCheck].getDiscColor() != Constants.EMPTY //Two parameters.
                         && board[row][columnToCheck].getDiscColor() != color); //One parameter.
                
                //If the current square is out-of-bounds or empty, then the move is not valid.
                if (columnToCheck >= Constants.COLUMNS || board[row][columnToCheck].getDiscColor() == Constants.EMPTY)
                    return false;
                
                //Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs.
                else
                    return true;
                
            }//Closes opposing disc else statement.
        }//Closes array boundaries if statement.
        
        //We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }///Closes validRight method.
    
    /*
        This method check for whether there is a valid move in this direction if a disc was placed at row and col.
    */
    private boolean validUpRight(int row, int col, Color color)
    {
        //There's no need to check the current square.
        int rowToCheck = row - 1;
        int columnToCheck = col + 1;
        
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
                    columnToCheck++;
                } while (rowToCheck >= 0 && columnToCheck < Constants.COLUMNS && //Two parameters.
                         board[rowToCheck][columnToCheck].getDiscColor() != Constants.EMPTY && //One parameter.
                         board[rowToCheck][columnToCheck].getDiscColor() != color); //One parameter.
                
                //If the current square is out-of-bounds or empty, then the move is not valid.
                if (rowToCheck < 0 || columnToCheck >= Constants.COLUMNS || board[rowToCheck][columnToCheck].getDiscColor() == Constants.EMPTY)
                    return false;
                
                //Otherwise, the current square contains our color, making this a valid direction since we can surround the opposing discs.
                else
                    return true;
                
            }//Closes opposing disc else statement.
        }//Closes array boundaries if statement.
        
        //We are not in bounds, so this is definitely not a valid direction.
        else
            return false;
        
    }///Closes validUpRight method.
    
    /**
     * @return the board
     */
    public Disc[][] getBoard()
    {
        return board;
    }

    /**
     * @param board the board to set
     */
    public void setBoard(Disc[][] board)
    {
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
    public void setDarkCount(int darkCount)
    {
        this.darkCount = darkCount;
    }

    /**
     * @return the lightCount
     */
    public int getLightCount()
    {
        return lightCount;
    }

    /**
     * @param lightCount the lightCount to set
     */
    public void setLightCount(int lightCount)
    {
        this.lightCount = lightCount;
    }

    /**
     * @return the players
     */
    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    /**
     * @param players the players to set
     */
    public void setPlayers(ArrayList<Player> players)
    {
        this.players = players;
    }
    
    private Disc[][] board;
    private int darkCount;
    private int lightCount;
    private ArrayList<Player> players;
}
