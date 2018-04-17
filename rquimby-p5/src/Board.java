
public class Board {
	private boolean twoPlayers;
	private int player;
	private int[][] board;
	private PlayerService opponent;

	public Board() {
		this.player = 1;
		this.board = new int[3][3];
		this.opponent = null;
	}

	public void setOpponent(PlayerService opponent) {
		this.opponent = opponent;
	}

	public PlayerService getOpponent() {
		return opponent;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int aPlayer) {
		if (aPlayer == 1)
			this.player = aPlayer;
		else {
			this.player++;
		}
	}

	public boolean isTwoPlayers() {
		return twoPlayers;
	}

	public void setTwoPlayers(boolean twoPlayers) {
		this.twoPlayers = twoPlayers;
	}

	public boolean boardFull() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 0)
					return false;
			}
		}

		return true;
	}

	public boolean isWinner() {
		if (board[0][0] == board[1][0] && board[1][0] == board[2][0] && (board[0][0] == 1 || board[0][0] == 2))
			return true;
		else if (board[0][1] == board[1][1] && board[1][1] == board[2][1] && (board[0][1] == 1 || board[0][1] == 2))
			return true;
		else if (board[0][2] == board[1][2] && board[1][2] == board[2][2] && (board[0][2] == 1 || board[0][2] == 2))
			return true;
		else if (board[0][0] == board[0][1] && board[0][1] == board[0][2] && (board[0][0] == 1 || board[0][0] == 2))
			return true;
		else if (board[1][0] == board[1][1] && board[1][1] == board[1][2] && (board[1][0] == 1 || board[1][0] == 2))
			return true;
		else if (board[2][0] == board[2][1] && board[2][1] == board[2][2] && (board[2][0] == 1 || board[2][0] == 2))
			return true;
		else if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && (board[0][0] == 1 || board[0][0] == 2))
			return true;
		else if (board[2][0] == board[1][1] && board[1][1] == board[0][2] && (board[2][0] == 1 || board[2][0] == 2))
			return true;
		else
			return false;
	}

	public boolean isNotTaken(int row, int column) {
		if (board[column][row] == 1 || board[column][row] == 2) {
			return false;
		}

		return true;
	}

	public boolean isWithinRange(int row, int column) {
		if (row > 2 || row < 0 || column > 2 || column < 0){
			return false;
		}

		return true;
	}

	public void placeMark(int playerNum, int row, int column) {
		board[row][column] = playerNum;
	}

	public String displayBoard() {
		String grid = board[0][0] + " " + board[0][1] + " " + board[0][2] + "\n" + board[1][0] + " " + board[1][1] + " "
				+ board[1][2] + "\n" + board[2][0] + " " + board[2][1] + " " + board[2][2];
		return grid;
	}

}
