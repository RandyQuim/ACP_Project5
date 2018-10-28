import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Executes Game Protocol commands from a socket. A class for each thread
 * running the game.
 *
 * @author Randy Quimby
 * @version 1.0
 *
 *          COP4027 Project#: 5 File Name: PlayerService.java
 */
public class PlayerService implements Runnable {
	/**
	 * The regular socket for network communication
	 */
	private Socket socket;
	/**
	 * The stream for input
	 */
	private Scanner in;
	/**
	 * The stream for output
	 */
	private PrintWriter out;
	/**
	 * The game board
	 */
	private Board board;
	/**
	 * The lock for player turns
	 */
	private ReentrantLock lock;

	/**
	 * Constructs a PlayerService object with a regular socket for network
	 * communication and sets a Board object shared by every client to the
	 * instance field
	 *
	 * @param s the regular socket for server communication
	 * @param board the game board
	 */
	public PlayerService(Socket s, Board board) {
		this.socket = s;
		this.board = board;
		this.lock = new ReentrantLock();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			try {
				in = new Scanner(socket.getInputStream());
				out = new PrintWriter(socket.getOutputStream());
				doService();
			} finally {
				socket.close();
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Runs threads until game comes to completion (until quit command via
	 * winner, draw, or manual player quit)
	 */
	private void doService() {
		while (true) {
			if (!in.hasNext())
				return;
			String command = in.next();
			if (command.equals("QUIT")) {
				int player = in.nextInt();
				out.println("QUIT \nPlayer chose quit");
				board.getOpponent().out
						.println("QUIT \nPlayer " + player + " quit the game");
				out.flush();
				board.getOpponent().out.flush();
				return;
			} else
				executeCommand(command);
		}
	}

	/**
	 * Executes the initial commands and delegates them accordingly
	 *
	 * @param command the command from the client to be executed
	 */
	private void executeCommand(String command) {
		if (command.equals("JOIN")) {
			String name = in.next();
			if (board.getPlayer() == 1) {
				out.println("Hello " + name + " you are player " + board.getPlayer());
				out.println(board.getPlayer());
				out.println("PLAYER_ONE_GO");
				board.setPlayer(0);
			} else {
				board.setTwoPlayers(true);
				out.println("Hello " + name + " you are player " + board.getPlayer() + ".  " + "Let the game begin!");
				out.println(board.getPlayer());
				board.setPlayer(1);
				board.setOpponent(this);
			}
		} else if (command.equals("CHOOSE")) {
			executeMove();
		}

		out.flush();
	}

	/**
	 * Attempts to execute a player move. Several checks are put in place to
	 * ensure the move is legal and to determine a draw or winner.
	 */
	private void executeMove() {
		lock.lock();
		int playerNum = in.nextInt();
		int row = in.nextInt();
		int column = in.nextInt();
		if (!board.isTwoPlayers()) {
			out.println("We do not have two players yet!");
		} else {
			if (board.isWithinRange(row, column)) {
				if (board.isNotTaken(row, column)) {
					processMove(playerNum, row, column);
					if (board.isWinner()) {
						processWin(playerNum);
					}
					if (board.boardFull()) {
						processDraw();
					}
					board.getOpponent().out.flush();
					board.setOpponent(this);
				} else {
					out.println("Position [" + row + "] " + "[" + column + "] is taken, try again.");
				}
			} else {
				out.println("ILLEGAL Board Position (numbers not within range)!");
			}
		}

		lock.unlock();
	}

	/**
	 * Processes a draw to all clients
	 */
	private void processDraw() {
		out.println("Board is full!  Its a draw!");
		board.getOpponent().out.println("Board is full!  Its a draw!");
		board.reset();
	}

	/**
	 * Processes a win condition to all clients
	 *
	 * @param playerNum the player number
	 */
	private void processWin(int playerNum) {
		out.println("WINNER \nPlayer " + playerNum + " WINS!");
		board.getOpponent().out.println("WINNER \nPlayer " + playerNum + " WINS!");
		board.reset();
	}

	/**
	 * Processes the player move to all clients
	 *
	 * @param playerNum the player number
	 * @param row the row number of the board
	 * @param column the column number of the board
	 */
	private void processMove(int playerNum, int row, int column) {
		board.placeMark(playerNum, row, column);
		out.println("Player " + playerNum + " has chosen [" + row + "] [" + column + "]. Player "
				+ board.otherPlayerNumber(playerNum) + "'s turn...");
		out.println(board.displayBoard());
		board.getOpponent().otherPlayerMoved();
	}

	/**
	 * Accesses other clients' command prompt (accesses other threads/player
	 * proxies) to update the game board when current player makes a legal move
	 */
	private void otherPlayerMoved() {
		board.getOpponent().out.println("OPPONENT_MOVED" + " \n" + board.displayBoard());
		board.getOpponent().out.println("Player " + board.getPlayer());
	}
}
