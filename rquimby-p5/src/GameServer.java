import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
	public static final int T3_PORT = 8888;

	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(T3_PORT);
		System.out.println("Waiting for clients to connect...");
		Board board = new Board();

		while (true) {
	         Socket s = server.accept();
	         System.out.println("Client connected.");
	         PlayerService service = new PlayerService(s, board);
	         Thread t = new Thread(service);
	         t.start();
	      }
	}

}
