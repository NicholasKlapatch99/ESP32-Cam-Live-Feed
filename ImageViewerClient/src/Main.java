/**
 * @author Nicholas Klapatch
 * @version April 19, 2025
 * Main class for web client to view images. The main characteristic of this program
 * is that it is an HTTP Client that processes synchronous HTTP GET requests. An example of how to begin building one can
 * be found at: https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html.
 * It also displays the image and temperature data in a JFrame.
 */

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import javax.imageio.ImageIO;
import java.net.URI;
import java.time.Duration;
import java.util.Date;

public class Main {
	/**
	 * Convert a String of a temperature in Celsius to a String in Farenheit.
	 * @param temp The temperature input String in Celsius.
	 * @return The temperature converted to Farenheit.
	 */
	public static String celciusToFarenheit(String temp) {
		double celsius = Double.parseDouble(temp);
		double farenheit = (celsius * (9.0 / 5.0)) + 32;
		return String.valueOf(farenheit) + "° F";
	}
	
	/**
	 * Get image from server.
	 * @param client The HTTP client requesting the image.
	 * @return The BufferedImage from the ESP32 camera.
	 * @throws IOException Thrown if the image cannot be read.
	 * @throws InterruptedException  Thrown if main thread is interrupted while getting image.
	 */
	public static BufferedImage getImageFromServer(HttpClient client) throws IOException, InterruptedException {
		BufferedImage image = null;
		// Create the HTTP GET request for the image.
		for(int i = 0; i < 100; i++) {	
			try {
				HttpRequest imageRequest = HttpRequest.newBuilder()
				        .uri(URI.create("http://#.#.#.#/")) // Replace with the IP address that your server is running on.
				        .timeout(Duration.ofSeconds(1))
				        .header("Content-Type", "image/jpeg")
				        .GET()
				        .build();
				
				// Send the HTTP request. JPEGs are processed by the server as byte arrays.
				HttpResponse<byte[]> response = client.send(imageRequest, BodyHandlers.ofByteArray());
				byte[] imageByteArray = response.body();
				ByteArrayInputStream stream = new ByteArrayInputStream(imageByteArray);
				image = ImageIO.read(stream);
				return image;
			}
			catch(java.net.http.HttpTimeoutException e) {
				System.out.println("Image timeout");
			}
		}
		return null;
	}
	
	/**
	 * Get temperature data from server.
	 * @param client The HTTP client requesting the image.
	 * @return The String containing the temperature (in Celsius) from the ESP32 camera.
	 * @throws IOException Thrown if the data cannot be read.
	 * @throws InterruptedException  Thrown if main thread is interrupted while getting data.
	 */
	public static String getTemperatureReadingFromServer(HttpClient client) throws IOException, InterruptedException {
		String temperatureData;
		for(int i = 0; i < 100; i++) {
			// Create the HTTP GET request for the sensor data.
			try {
				HttpRequest textRequest = HttpRequest.newBuilder()
				        .uri(URI.create("http://#.#.#.#/")) // Replace with the IP address that your server is running on.
				        .timeout(Duration.ofSeconds(1))
				        .header("Content-Type", "text/plain; charset=UTF-8")
				        .GET()
				        .build();
				
				// Send the HTTP request.
				HttpResponse<String> response = client.send(textRequest, BodyHandlers.ofString());
				temperatureData = response.body();
				return temperatureData;
			}
			catch(java.net.http.HttpTimeoutException e) {
				System.out.println("Text timeout");
			}
		}	
		return "No reading";
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		BufferedImage image;
		String temperatureData;
		ImageViewer img;
		
		// Create the HttpClient.
		HttpClient client = HttpClient.newBuilder()
				.version(Version.HTTP_1_1)
				.followRedirects(Redirect.NORMAL)
				.connectTimeout(Duration.ofSeconds(10))
				.proxy(ProxySelector.of(new InetSocketAddress("#.#.#.#", 8000))) // // Replace with the IP address that your server is running on.
		        .build();
		
		// Get initial image and temperature readings.
		image = getImageFromServer(client);
		temperatureData = getTemperatureReadingFromServer(client);	
		
		// Display initial image and temperature. Return (terminate program) otherwise.
		if(image != null && temperatureData != null) {
			img = new ImageViewer(image, celciusToFarenheit(temperatureData) + " - " + new Date());
		}
		else {
			return;
		}
		
		// Keep reading images indefinitely. Program ends when JFrame closes.
		while(true) {
			Thread.sleep(2000);
			image = getImageFromServer(client);
			temperatureData = getTemperatureReadingFromServer(client);	
			if(image != null && temperatureData != null) {
				img.updateImageViewer(image, celciusToFarenheit(temperatureData) + " - " + new Date());
			}
		}
	}
}