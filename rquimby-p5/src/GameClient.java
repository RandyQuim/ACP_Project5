import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * This program tests the game server. Represents the client side of a socket.
 *
 * @author Randy Quimby
 * @version 1.0
 *
 *          COP4027 Project#: 5 File Name: GameClient.java
 */
public class GameClient {
	private static final int T3_PORT = 8888;

	public static void main(String[] args) throws IOException {
		Socket s = new Socket("localhost", T3_PORT);
		InputStream instream = s.getInputStream();
		OutputStream outstream = s.getOutputStream();
		Scanner in = new Scanner(instream);
		PrintWriter out = new PrintWriter(outstream);
		Scanner input = new Scanner(System.in);

		System.out.println("Welcome to a multi-player Tic Tac Toe game.");
		System.out.println("In turn, each player will place their mark ");
		System.out.println("by entering 2 numbers that represent a row and a column.\n");
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

		String response = in.nextLine();
		int playerNum = in.nextInt();
		System.out.println("Receiving: " + response + "\n");
		if (playerNum == 2)
			System.out.println("Awaiting player 1 to make their move...");
		in.nextLine();

		String rowOne = "";
		String rowTwo = "";
		String rowThree = "";
		boolean illegalMove = false;
		try {
			// Loop tests command messages from the server
			while (true) {
				if (!illegalMove) {
					response = in.nextLine();
				}
				if (!response.startsWith("OPPONENT_MOVED") && !response.startsWith("WINNER")
						&& !response.startsWith("Board") && !response.startsWith("QUIT")) {
					System.out.print("Enter 'choose', your player number, and then the row\n"
							+ "and column you wish to place your mark <choose command>\n"
							+ "<player number> <row> <column> (or type 'quit' to quit): ");
					String choice = input.next();
					if (!choice.toUpperCase().equals("CHOOSE") && !choice.toUpperCase().equals("QUIT")) {
						choice = "CHOOSE";
					}
					if (choice.toUpperCase().equals("CHOOSE")) {
						int number = input.nextInt();
						int row = input.nextInt();
						int column = input.nextInt();
						/*
						 * Player numbers are generated automatically by the
						 * program. However, the following code was implemented
						 * to ensure instructions were followed.
						 */
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
					} else {
						command = choice.toUpperCase() + " " + playerNum;
					}
					out.println(command);
					out.flush();
					response = in.nextLine();
				}
				if (response.startsWith("Player")) {
					rowOne = in.nextLine();
					rowTwo = in.nextLine();
					rowThree = in.nextLine();
					System.out.println(rowOne + "\n" + rowTwo + "\n" + rowThree);
					illegalMove = false;
				} else if (response.startsWith("Board")) {
					System.out.println("Receiving: " + response);
					command = "QUIT " + playerNum;
					System.out.print("Sending: " + command);
					out.print(command);
					out.flush();
					break;
				} else if (response.startsWith("OPPONENT_MOVED")) {
					rowOne = in.nextLine();
					rowTwo = in.nextLine();
					rowThree = in.nextLine();
					System.out.println(rowOne + "\n" + rowTwo + "\n" + rowThree);
					System.out.println("Opponent made their move!");
				} else if (response.startsWith("We")) {
					System.out.println("Must Wait");
					illegalMove = true;
				} else if (response.startsWith("Position")) {
					illegalMove = true;
				} else if (response.startsWith("WINNER")) {
					System.out.println("Receiving: " + in.nextLine());
					command = "QUIT " + playerNum;
					System.out.print("Sending: " + command);
					out.print(command);
					out.flush();
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
		} finally {
			s.close();
			in.close();
			input.close();
		}
	}

}