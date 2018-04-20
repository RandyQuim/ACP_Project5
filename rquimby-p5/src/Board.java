
/**
 * A class to represent the game board and implement the game conditions and
 * restrictions
 *
 * @author Randy Quimby
 * @version 1.0
 *
 *          COP4027 Project#: 5 File Name: Board.java
 */
public class Board {
	/**
	 * A condition on whether there exists two players or not
	 */
	private boolean twoPlayers;
	/**
	 * Assigns player number (1 or 2)
	 */
	private int player;
	/**
	 * The game board
	 */
	private int[][] board;
	/**
	 * The PlayerService object representing opponent threads
	 */
	private PlayerService opponent;

	/**
	 * Constructs a Board object and assigns initial values to the board and
	 * player number
	 */
	public Board() {
		this.player = 1;
		this.board = new int[3][3];
	}

	/**
	 * Sets the PlayerService object to the parameter value
	 *
	 * @param opponent the opponent of the current player
	 */
	public void setOpponent(PlayerService opponent) {
		this.opponent = opponent;
	}

	/**
	 * Returns the PlayerService object
	 *
	 * @return the opponent of the current player
	 */
	public PlayerService getOpponent() {
		return opponent;
	}

	/**
	 * Returns the player number
	 *
	 * @return the player number
	 */
	public int getPlayer() {
		return player;
	}

	/**
	 * Sets the player number to the parameter value
	 *
	 * @param aPlayer the player number
	 */
	public void setPlayer(int aPlayer) {
		if (aPlayer == 1)
			this.player = aPlayer;
		else {
			this.player++;
		}
	}

	/**
	 * Returns the condition determining whether there are two players
	 *
	 * @return the condition of correct number of players
	 */
	public boolean isTwoPlayers() {
		return twoPlayers;
	}

	/**
	 * Sets the condition determining whether there are two players to the
	 * parameter value
	 *
	 * @param twoPlayers the condition of correct number of players
	 */
	public void setTwoPlayers(boolean twoPlayers) {
		this.twoPlayers = twoPlayers;
	}

	/**
	 * Determines if the game board is full (a draw)
	 *
	 * @return the boolean value of a draw
	 */
	public boolean boardFull() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 0)
					return false;
			}
		}

		return true;
	}

	/**
	 * Determines if the game has a winner (three marks in a row, column, or
	 * diagonally)
	 *
	 * @return the boolean value of a winner
	 */
	public boolean isWinner() {
		if (board[0][0] == board[1][0] && board[1][0] == board[2][0] && (board[0][0] != 0))
			return true;
		else if (board[0][1] == board[1][1] && board[1][1] == board[2][1] && (board[0][1] != 0))
			return true;
		else if (board[0][2] == board[1][2] && board[1][2] == board[2][2] && (board[0][2] != 0))
			return true;
		else if (board[0][0] == board[0][1] && board[0][1] == board[0][2] && (board[0][0] != 0))
			return true;
		else if (board[1][0] == board[1][1] && board[1][1] == board[1][2] && (board[1][0] != 0))
			return true;
		else if (board[2][0] == board[2][1] && board[2][1] == board[2][2] && (board[2][0] != 0))
			return true;
		else if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && (board[0][0] != 0))
			return true;
		else if (board[2][0] == board[1][1] && board[1][1] == board[0][2] && (board[2][0] != 0))
			return true;
		else
			return false;
	}

	/**
	 * Determines if a spot on the board has or has not already been taken
	 *
	 * @param row the row of the player mark
	 * @param column the column of the player mark
	 * @return the condition of the board position
	 */
	public boolean isNotTaken(int row, int column) {
		if (board[row][column] != 0) {
			return false;
		}

		return true;
	}

	/**
	 * Determines if user input numbers are within range (0-2)
	 *
	 * @param row the user input numerical representation of a row
	 * @param column the user input numerical representation of column
	 * @return the condition of a number within range
	 */
	public boolean isWithinRange(int row, int column) {
		if (row > 2 || row < 0 || column > 2 || column < 0) {
			return false;
		}

		return true;
	}

	/**
	 * Places a player mark in the designated area (determined by row and column)
	 *
	 * @param playerNum the player number
	 * @param row the row chosen by the user
	 * @param column the column chosen by the user
	 */
	public void placeMark(int playerNum, int row, int column) {
		board[row][column] = playerNum;
	}

	/**
	 * Displays the board
	 *
	 * @return the string representation of the board
	 */
	public String displayBoard() {
		String grid = board[0][0] + " " + board[0][1] + " " + board[0][2] + "\n" + board[1][0] + " " + board[1][1] + " "
				+ board[1][2] + "\n" + board[2][0] + " " + board[2][1] + " " + board[2][2];
		return grid;
	}

	/**
	 * Resets the board to default values
	 */
	public void reset() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = 0;
			}
		}

	}

	/**
	 * Determines and sets the opponent's number for output to the console
	 *
	 * @param playerNum the current player's number
	 * @return the opponent's number
	 */
	public int otherPlayerNumber(int playerNum) {
		int otherPlayerNum = 0;
		if (playerNum == 1) {
			otherPlayerNum = 2;
		} else
			otherPlayerNum = 1;
		return otherPlayerNum;
	}

}
