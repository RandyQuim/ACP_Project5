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
		// complete try / catch
		try {
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
			in.nextLine();
			String rowOne = "";
			String rowTwo = "";
			String rowThree = "";
			boolean flag = false;

			// Players are FORCED to alternate turns with this coding scheme.
			// Therefore, no need to output message "Not player <int> turn". Is
			// that ok?

			// Also, illegal player number?? Players are supposed to manually
			// put in their player num??????  With this code, player numbers
			// are assigned (1 and 2 players for every 2 clients joining).
			while (true) {
				// OK, check the rest... "we" works now, ILLEGAL works now, position is working, OPPONENT and PLAYEr working....
				if (!flag) {
					response = in.nextLine();
				}
				if (!response.startsWith("OPPONENT_MOVED") && !response.startsWith("WINNER")) {
					flag = false;
					System.out.print("Enter the row and column you wish to place your mark: ");
					int row = input.nextInt();
					int column = input.nextInt();
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
					//BOARD FULL is not working... I think its the output... not methods
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
					flag = true;
				} else if (response.startsWith("Position")) {
					flag = true;
				} else if (response.startsWith("WINNER")) {
					// requires slight fix maybe?
					System.out.println("Receiving: " + in.nextLine());
					command = "QUIT\n";
					System.out.print("Sending: " + command);
					out.print(command);
					out.flush();
					break;
				} else if (response.startsWith("ILLEGAL")){
					flag = true;
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
