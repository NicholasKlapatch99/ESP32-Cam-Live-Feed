/**
 * @author Nicholas Klapatch
 * @version April 18, 2025
 * 
 * Implements a JFrame/JPanel for viewing images sent to the HttpServer from the ESP32 camera
 * and overlaying data sent from the DHT-11 sensor. Information for building a GUI like this can be 
 * found at https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
 * This program uses GridBagLayout, but there are more options available from the Swing library,
 * as well as documentation and tutorials for how to use them. 
 */

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Implements a JFrame for viewing the image and sensor data that the ESP-32 camera sent to the web server.
 */
public class ImageViewer {
	
	JFrame frame = new JFrame();
	JPanel panel = new JPanel(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();	
	JLabel imageLabel = new JLabel();
	JLabel textLabel = new JLabel();
	
	/**
	 * Constructor for creating the JFrame to view the image and sensor data.
	 * @param image An image. 
	 * @param imageText Text data to be viewed on the image.
	 * @throws IOException Exception thrown if the image source is not valid.
	 */
	public ImageViewer(BufferedImage image, String imageText) throws IOException {
		this.frame = new JFrame();
		this.frame.setSize(640, 520);
		this.frame.setResizable(false);
		ImageIcon icon = new ImageIcon(image);
		this.imageLabel.setIcon(icon);
		this.textLabel.setText(imageText);
		this.textLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		
		// Display text on top of image.
		this.c.fill = GridBagConstraints.HORIZONTAL;
		this.c.gridx = 0;
		this.c.gridy = 0;
		this.c.ipady = 25;
		this.panel.add(textLabel, c);

		// Display image under text (ipady value indicates this in GridBagLayout).
		this.c.fill = GridBagConstraints.HORIZONTAL;
		this.c.gridx = 0;
		this.c.gridy = 1;
		this.c.ipady = 5;
		this.panel.add(imageLabel, c);
		
		this.frame.add(panel);
		this.frame.setVisible(true);
		
		// Closing window terminates program.
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Update the image in the JFrame based on readings from the HTTP Server.
	 * @param image An image. 
	 * @param imageText Text data to be viewed on the image.
	 * @throws IOException Exception thrown if the image source is not valid.
	 */
	public void updateImageViewer(BufferedImage image, String imageText) throws IOException {		
		// This follows the same idea as the constructor, except prior values (except the JFrame) are cleared.
		this.panel = null;
		this.c = null;
		this.imageLabel = null;
		this.textLabel = null;
		
		this.panel = new JPanel(new GridBagLayout());
		this.c = new GridBagConstraints();	
		this.imageLabel = new JLabel();
		this.textLabel = new JLabel();
		
		ImageIcon icon = new ImageIcon(image);
		this.imageLabel.setIcon(icon);
		this.textLabel.setText(imageText);
		this.textLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		
		this.c.fill = GridBagConstraints.HORIZONTAL;
		this.c.gridx = 0;
		this.c.gridy = 0;
		this.c.ipady = 25;
		this.panel.add(textLabel, c);

		this.c.fill = GridBagConstraints.HORIZONTAL;
		this.c.gridx = 0;
		this.c.gridy = 1;
		this.c.ipady = 5;
		this.panel.add(imageLabel, c);
		
		this.frame.add(panel);
		this.frame.setVisible(true);
		
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}