import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {

	public static void main(String[] args) throws IOException {
		Socket s = new Socket("localhost", GameServer.T3_PORT);
		InputStream instream = s.getInputStream();
		OutputStream outstream = s.getOutputStream();
		Scanner in = new Scanner(instream);
		PrintWriter out = new PrintWriter(outstream);
		Scanner input = new Scanner(System.in);

		System.out.println("Welcome to a multi-player Tic Tac Toe game.");
		System.out.println("In turn, each player will place their mark ");
		System.out.println("by entering 2 numbers that represent a row and a column.\n");
		System.out.print("Please enter your name: ");
		String name = input.next();
		String command = "JOIN " + name + "\n";
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
			while (true) {
				if (!illegalMove) {
					response = in.nextLine();
				}
				if (!response.startsWith("OPPONENT_MOVED") && !response.startsWith("WINNER")
						&& !response.startsWith("Board")) {
					illegalMove = false;
					System.out.print("Enter your player number, and then the row and column "
							+ "you \nwish to place your mark <player number> <row> <column>: ");
					int number = input.nextInt();
					int row = input.nextInt();
					int column = input.nextInt();
					while (number != playerNum) {
						System.out.println("Illegal player number!");
						System.out.print("Please reenter your player number, row and column: ");
						number = input.nextInt();
						row = input.nextInt();
						column = input.nextInt();
					}
					command = "CHOOSE " + playerNum + " " + row + " " + column + "\n";
					System.out.println("Sending: " + command);
					out.println(command);
					out.flush();
					response = in.nextLine();
				}
				if (response.startsWith("Player")) {
					rowOne = in.nextLine();
					rowTwo = in.nextLine();
					rowThree = in.nextLine();
					System.out.println(rowOne + "\n" + rowTwo + "\n" + rowThree);
				} else if (response.startsWith("Board")) {
					System.out.println("Receiving: " + response);
					command = "QUIT\n";
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
					command = "QUIT\n";
					System.out.print("Sending: " + command);
					out.print(command);
					out.flush();
					break;
				} else if (response.startsWith("ILLEGAL")) {
					illegalMove = true;
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
