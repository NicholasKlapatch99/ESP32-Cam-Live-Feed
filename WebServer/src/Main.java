/**
 * @author Nicholas Klapatch
 * @version April 18, 2025
 * Driver file for the HttpServer. 
 * This code is based on the example that Oracle provides at:
 * https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/package-summary.html.
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class Main {
	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress("#.#.#.#", 8000), 0); // Replace with the IP address that your server is running on.
		server.createContext("/" , new ImageHandler());
		server.setExecutor(null);
		server.start();
		System.out.println("Server started");
	}
}