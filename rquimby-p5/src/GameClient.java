
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Performs the client side of communication with a server. Analyzes input
 * messages from the server and generates commands for output.
 * 
 * @author Randy Quimby
 * @version 1.0
 * 
 *          COP4027 Project#: 5 File Name: GameClient.java
 */
public class GameClient {
	/**
	 * Constant for the port number
	 */
	private static final int T3_PORT = 8888;
	/**
	 * The socket for connecting over a network
	 */
	private Socket s;
	/**
	 * Acquires input from the server
	 */
	private Scanner in;
	/**
	 * Sends output commands to the server
	 */
	private PrintWriter out;
	/**
	 * The object for acquiring user input
	 */
	private Scanner input;
	/**
	 * The command to the server
	 */
	private String command;
	/**
	 * The server response
	 */
	private String response;
	/**
	 * The player number
	 */
	private int playerNum;

	/**
	 * Constructs a GameClient object that initiates a connection with the
	 * server and applies the socket to an input and output stream with the
	 * server
	 */
	public GameClient() {
		try {
			s = new Socket("localhost", T3_PORT);
			InputStream instream = s.getInputStream();
			OutputStream outstream = s.getOutputStream();
			in = new Scanner(instream);
			out = new PrintWriter(outstream);
			input = new Scanner(System.in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initiates a Tic-Tac-Toe game
	 */
	public void initGame() {
		joinGame();
		response = in.nextLine();
		playerNum = in.nextInt();
		System.out.println("Receiving: " + response + "\n");
		if (playerNum == 2)
			System.out.println("Awaiting player 1 to make their move...");
		in.nextLine();

		try {
			serverClientCommunication();
			s.close();
			in.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loops until game comes to completion and examines server response
	 * messages
	 */
	private void serverClientCommunication() {
		boolean illegalMove = false;
		// Loop tests command messages from the server
		while (true) {
			if (!illegalMove) {
				response = in.nextLine();
			}
			nextMove();
			if (response.startsWith("Player")) {
				displayRows();
				illegalMove = false;
			} else if (response.startsWith("Board")) {
				System.out.println("Receiving: " + response);
				quit();
				break;
			} else if (response.startsWith("OPPONENT_MOVED")) {
				displayRows();
				System.out.println("Opponent made their move!");
			} else if (response.startsWith("We")) {
				System.out.println("Must Wait");
				illegalMove = true;
			} else if (response.startsWith("Position")) {
				illegalMove = true;
			} else if (response.startsWith("WINNER")) {
				System.out.println("Receiving: " + in.nextLine());
				quit();
				break;
			} else if (response.startsWith("ILLEGAL")) {
				illegalMove = true;
			} else if (response.startsWith("QUIT")) {
				System.out.println("Receiving: " + in.nextLine());
				System.out.println("End program...");
				break;
			}

			System.out.println("Receiving: " + response + "\n");
		}
	}

	/**
	 * Determines if the server response allows for the current payer's next move
	 */
	private void nextMove() {
		if (!response.startsWith("OPPONENT_MOVED") && !response.startsWith("WINNER")
				&& !response.startsWith("Board") && !response.startsWith("QUIT")) {
			response = analyzeCommand();
		}
	}

	/**
	 * Quits the game
	 */
	private void quit() {
		command = "QUIT " + playerNum;
		System.out.print("Sending: " + command);
		out.print(command);
		out.flush();
	}

	/**
	 * Displays the Tic-Tac-Toe board
	 */
	private void displayRows() {
		String rowOne = in.nextLine();
		String rowTwo = in.nextLine();
		String rowThree = in.nextLine();
		System.out.println(rowOne + "\n" + rowTwo + "\n" + rowThree);
	}

	/**
	 * Analyzes the user input command and sends the command to the server.
	 * Returns the server response from the command.
	 * 
	 * @return the response from the server
	 */
	private String analyzeCommand() {
		moveDisplay();
		String choice = input.next();
		if (!choice.toUpperCase().equals("CHOOSE") && !choice.toUpperCase().equals("QUIT")) {
			choice = "CHOOSE";
		}
		if (choice.toUpperCase().equals("CHOOSE")) {
			chooseMove(choice);
		} else {
			command = choice.toUpperCase() + " " + playerNum;
		}
		out.println(command);
		out.flush();
		response = in.nextLine();
		return response;
	}

	/**
	 * Determines if user input was legal
	 * 
	 * @param choice the user input command
	 */
	private void chooseMove(String choice) {
		int number = input.nextInt();
		int row = input.nextInt();
		int column = input.nextInt();
		while (number != playerNum) {
			System.out.println("Illegal player number!");
			System.out.print("Please reenter your player number, row and column: ");
			if (!input.hasNextInt()) {
				// Disregards if user reenters the command
				input.next();
			}
			number = input.nextInt();
			row = input.nextInt();
			column = input.nextInt();
		}
		command = choice.toUpperCase() + " " + playerNum + " " + row + " " + column + "\n";
		System.out.println("Sending: " + command);
	}

	/**
	 * Displays a prompt for a user input command to move or quit
	 */
	private void moveDisplay() {
		System.out.print("Enter 'choose', your player number, and then the row\n"
				+ "and column you wish to place your mark <choose command>\n"
				+ "<player number> <row> <column> (or type 'quit' to quit): ");
	}

	/**
	 * Allows a user to join a game and sends the command to the server. Checks
	 * for illegal, missing, or misspelled commands
	 */
	private void joinGame() {
		instructionDisplay();
		boolean flag;
		String command = "";
		do {
			flag = false;
			System.out.print("Please enter the 'join' command followed \nby your name <join command> <name>: ");
			command = input.next().toUpperCase() + " " + input.next() + "\n";
			if (!command.startsWith("JOIN")) {
				flag = true;
				System.out.println("Must type 'join' and then your name (IE join Randy)");
			}
		} while (flag);
		System.out.print("Sending: " + command);
		out.print(command);
		out.flush();
	}

	/**
	 * Displays the instructions for the game
	 */
	private void instructionDisplay() {
		System.out.println("Welcome to a multi-player Tic Tac Toe game. In turn,");
		System.out.println("each player will place their mark by entering 2 numbers");
		System.out.println("that represent a row and a column.\n");
	}
}
