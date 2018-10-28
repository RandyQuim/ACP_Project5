import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server that executes the Tic-Tac-Toe game Protocol.
 *
 * @author Randy Quimby
 * @version 1.0
 *
 *          COP4027 Project#: 5 File Name: GameServer.java
 */
public class GameServer {
	/**
	 * The port number (Represents three T's for Tic Tac Toe)
	 */
	private static final int T3_PORT = 8888;

	public static void main(String[] args) throws IOException {
		ServerSocket server = null;
		try {
			server = new ServerSocket(T3_PORT);
			System.out.println("Waiting for clients to connect...");
			Board board = new Board();
			while (true) {
				Socket s = server.accept();
				System.out.println("Client connected.");
				PlayerService service = new PlayerService(s, board);
				Thread t = new Thread(service);
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			server.close();
		}
	}

}
