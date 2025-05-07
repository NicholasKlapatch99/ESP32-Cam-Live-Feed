/**
 * @author Nicholas Klapatch
 * @version April 18, 2025
 * 
 * ImageHandler implements HttpHandler. This class processes HTTP exchanges for JPEGs and Strings
 * sent from the ESP32 camera.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Implementation of HttpHandler interface. This Handler processes POST and GET requests
 * for when HttpClients send the HttpServer that uses this HttpHandler HttpRequests.
 * Oracle provides an example of how to use this library and how it works at
 * https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/package-summary.html.
 */
public class ImageHandler implements HttpHandler {

	byte[] mostRecentFileBytes;
	String mostRecentTextData;
	String textResponse;
	
	/**
	 * Overrides the only method of HttpHandler to ensure that JPEGs and text are properly
	 * handled.
	 * @param exchange The HttpExchange requested by the client. 
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		byte[] fileBytes = null;
		String response = "No message";
		
		// Handle JPEG requests.
		if(exchange.getRequestMethod().equals("POST") && exchange.getRequestHeaders().getFirst("Content-Type").equals("image/jpeg")) {
			InputStream is = exchange.getRequestBody();
			fileBytes = is.readAllBytes();
			mostRecentFileBytes = fileBytes;
			exchange.sendResponseHeaders(200, 0);
			OutputStream os = exchange.getResponseBody();
			os.close();
			is.close();
		}

		if(exchange.getRequestMethod().equals("GET") && exchange.getRequestHeaders().getFirst("Content-Type").equals("image/jpeg")) {
			exchange.sendResponseHeaders(200, mostRecentFileBytes.length);
			OutputStream os = exchange.getResponseBody();
			os.write(mostRecentFileBytes);
			os.close();
		}
		
		// Handle sensor data requests.		
		if(exchange.getRequestMethod().equals("POST") && exchange.getRequestHeaders().getFirst("Content-Type").equals("text/plain; charset=UTF-8")) {
			InputStream is = exchange.getRequestBody();
			StringBuilder s = new StringBuilder();
			int i;
			while((i = is.read()) != -1) {
				char c = (char) i;
				s.append(c);
			}
			is.close();
			mostRecentTextData = s.toString();
			response = "This posted message has been stored in the mostRecentTextData string.";
			exchange.sendResponseHeaders(200, response.length());
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
		
		if(exchange.getRequestMethod().equals("GET") && exchange.getRequestHeaders().getFirst("Content-Type").equals("text/plain; charset=UTF-8")) {
			if(mostRecentTextData == null) {
				response = "No new message has been posted yet";
			}
			else {
				response = mostRecentTextData;
			}
			exchange.sendResponseHeaders(200, response.length());
			OutputStream os = exchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}
}