package swing;


import java.awt.image.BufferedImage;



import java.io.IOException;

/*
 * HelloWorldSwing.java requires no other files. 
 */
import javax.swing.*;

public class Fractal {
	
	private static int max_iteration = 1000;
	private static int[] palette = initializePalette();
	
	private static int[] initializePalette() {
		int[] palette = new int[max_iteration];
		for (int i = 0 ; i < max_iteration ; ++i) {
			palette[i] = (10 + 15*i * 255 / (max_iteration - 1)) % 256; 
			palette[i] = palette[i] | ((20 + 20*i * 255 / (max_iteration - 1)) % 256 << 8); 
			palette[i] = palette[i] | (50 + 50*i * 255 / (max_iteration - 1) % 256 << 16); 
		}
		return palette;
	}
	
	private static void createAndShowGUI() throws IOException {
		JFrame frame = new JFrame("HelloWorldSwing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Add the ubiquitous "Hello World" label.
//		frame.getContentPane().setLayout(new GridLayout(2, 1));
		BufferedImage bufferedImage = new BufferedImage(1500, 750, BufferedImage.TYPE_INT_RGB);
		drawFractal(bufferedImage);
		JLabel image = new JLabel(new ImageIcon(bufferedImage));
		frame.getContentPane().add(image);
		
		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	private static void drawFractal(BufferedImage bufferedImage) {
		int[] histogram = new int[max_iteration];
		long total = 0;
		int[][] buffer = new int[bufferedImage.getWidth()][bufferedImage.getHeight()];
		for (int pX = 0 ; pX < bufferedImage.getWidth() ; ++pX) {
			for (int pY = 0 ; pY < bufferedImage.getHeight() ; ++pY) {
				double x0 = ((double)pX / bufferedImage.getWidth() * 3.5) - 2.5;
				double y0 = 1 - ((double)pY / bufferedImage.getHeight() * 2);
				double x = 0.0;
				double y = 0.0;
				int iteration = 0;
				while (x*x + y*y < 4 && iteration < max_iteration - 1) {
					double xTemp = x*x - y*y + x0;
					y = 2*x*y + y0;
					x = xTemp;
					iteration++;
				}
				histogram[iteration]++;
				total++;
				buffer[pX][pY] = iteration;
//				bufferedImage.setRGB(pX, pY, palette[iteration]);
			}
		}
		
		for (int pX = 0 ; pX < bufferedImage.getWidth() ; ++pX) {
			for (int pY = 0 ; pY < bufferedImage.getHeight() ; ++pY) {
				double hue = 0.0;
				for (int i = 0 ; i < buffer[pX][pY] ; ++i) {
					hue += (double)histogram[i] / total;
				}
				
				int color = palette[(int)((max_iteration - 1) * hue)];
				bufferedImage.setRGB(pX, pY, color);
			}
		}
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
		public void run() {
			try {
					createAndShowGUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
