package core;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Game {

    public Game()
    {
        initObjects();
    }
    
    /**
     * Start the game
     */
    private void initObjects() {
        board = new Board();
        createPlayers();
        board.setPlayers(playerList);
        printPlayers();
        
        // First player starts the game
        currentPlayer = playerList.get(Constants.PLAYER_ONE);
    }
    
    /**
     * Ask players for their names, and assign them disc colors
     */
    private void createPlayers()
    {
        playerList = new ArrayList<>();
        
        for (int playerIdx = 0; playerIdx < Constants.MAX_PLAYERS; playerIdx++)
        {
            Player player = new Player();
            
            String playerName = JOptionPane.showInputDialog(null, "Enter player's name");
            player.setName(playerName);
            
            if (playerIdx == Constants.PLAYER_ONE) {
                player.setDiscColor(Constants.DARK);
            } else if (playerIdx == Constants.PLAYER_TWO) {
                player.setDiscColor(Constants.LIGHT);
            }
            
            // Players start with two chips
            player.setScore(Constants.TWO);
            
            playerList.add(player);
        }
    }
    
    /**
     * Calculates and set scores for players
     */
    public void calculateScore() {
        board.calculateScore();
        playerList.get(Constants.PLAYER_ONE).setScore(board.getDarkCount());
        playerList.get(Constants.PLAYER_TWO).setScore(board.getLightCount());
    }
    
    /**
     * Print player information to the system
     */
    private void printPlayers() {
        System.out.println("The game has the following players:");
        playerList.stream().forEach((player) -> {
            System.out.println("Player " + player.getName() + " is playing disc color " + player.getDiscColor());
        });
    }

    /**
     * @return the players
     */
    public ArrayList<Player> getPlayerList()
    {
        return playerList;
    }

    /**
     * @param players the players to set
     */
    public void setPlayerList(ArrayList<Player> players)
    {
        this.playerList = players;
    }

    /**
     * @return the board
     */
    public Board getBoard()
    {
        return board;
    }

    /**
     * @param board the board to set
     */
    public void setBoard(Board board)
    {
        this.board = board;
    }

    /**
     * @return the currentPlayer
     */
    public Player getCurrentPlayer()
    {
        return currentPlayer;
    }

    /**
     * @param currentPlayer the currentPlayer to set
     */
    public void setCurrentPlayer(Player currentPlayer)
    {
        this.currentPlayer = currentPlayer;
    }
    
    private ArrayList<Player> playerList;
    public Board board;
    private Player currentPlayer;
}
