import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PlayerService implements Runnable {
	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	private Board board;

	public PlayerService(Socket s, Board board) {
		this.socket = s;
		this.board = board;
	}

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

	private void doService() {
		while (true) {
			if (!in.hasNext())
				return;
			String command = in.next();
			if (command.equals("QUIT"))
				return;
			else
				executeCommand(command);
		}
	}

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

	private void executeMove() {
		int playerNum = in.nextInt();
		int row = in.nextInt();
		int column = in.nextInt();
		if (!board.isTwoPlayers()) {
			out.println("We do not have two players yet");
		} else {
			if (board.isWithinRange(row, column)) {
				if (board.isNotTaken(row, column)) {
					board.placeMark(playerNum, row, column);
					out.println("Player " + playerNum + " has chosen [" + row + "] [" + column + "]. Player "
							+ board.otherPlayerNumber(playerNum) + "'s turn...");
					out.println(board.displayBoard());
					board.getOpponent().otherPlayerMoved();
					if (board.isWinner()) {
						out.println("WINNER \nPlayer " + playerNum + " WINS!");
						board.getOpponent().out.println("WINNER \nPlayer " + playerNum + " WINS!");
						board.reset();
					}
					if (board.boardFull()) {
						out.println("Board is full!  Its a draw!");
						board.getOpponent().out.println("Board is full!  Its a draw!");
						board.reset();
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
	}

	public void otherPlayerMoved() {
		board.getOpponent().out.println("OPPONENT_MOVED" + " \n" + board.displayBoard());
		board.getOpponent().out.println("Player " + board.getPlayer());
	}
}
