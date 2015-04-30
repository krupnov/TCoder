package topcoder;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*
 * HelloWorldSwing.java requires no other files. 
 */
import javax.swing.*;

public class Fractal {
	
	private static int max_iteration = 1000;
	private static int[] palette = initializePalette();
	private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();
	private static final ExecutorService forPool = Executors.newFixedThreadPool(NUM_CORES * 2);
	private static double zoom = 1;
	private static double zoomCoef = 1.5;
	private static double offset = 3.5 / 2;
	private static double offsetStep = 1;
	private static final JLabel image = new JLabel();;
	
	private static int[] initializePalette() {
		int[] palette = new int[max_iteration + 1];
		for (int i = 0 ; i < palette.length ; ++i) {
			palette[i] = (i * 255 / (palette.length - 1)) % 256; 
			palette[i] = palette[i] | ((2 * i * 255 / (palette.length - 1)) % 256 << 8); 
			palette[i] = palette[i] | (3 * i * 255 / (palette.length - 1) % 256 << 16); 
		}
		return palette;
	}
	
	private static void createAndShowGUI() throws IOException {
		JFrame frame = new JFrame("HelloWorldSwing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		BufferedImage bufferedImage = new BufferedImage(1300, 650, BufferedImage.TYPE_INT_RGB);
		drawFractal(bufferedImage);
		image.setIcon(new ImageIcon(bufferedImage));
		frame.getContentPane().add(image);
		frame.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				boolean action = false;
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					zoom /= zoomCoef;
					action = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					zoom *= zoomCoef;
					action = true;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					action = true;
					offset -= offsetStep;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					action = true;
					offset += offsetStep;
				}
				
				if (action) {
					BufferedImage bufferedImage = new BufferedImage(1300, 650, BufferedImage.TYPE_INT_RGB);
					drawFractal(bufferedImage);
					image.setIcon(new ImageIcon(bufferedImage));
				}
				System.out.println("Got key event!" + e.getKeyCode());
			}
		});
		
		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	private static void drawFractal(final BufferedImage bufferedImage) {
		ArrayList<Callable<Void>> tasks = new ArrayList<Callable<Void>>(bufferedImage.getWidth());
		for (int pX = 0 ; pX < bufferedImage.getWidth(); ++pX) {
			final int localX = pX;
			tasks.add(new Callable<Void>() {
				
				@Override
				public Void call() throws Exception {
					processColumn(bufferedImage, localX);
					return null;
				}
			});
		}
		try {
			forPool.invokeAll(tasks);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void processColumn(BufferedImage bufferedImage, int pX) {
		for (int pY = 0 ; pY < bufferedImage.getHeight() ; ++pY) {
			double x0 = zoom*(((double)pX / bufferedImage.getWidth() * 3.5) - offset);
			double y0 = zoom*(1 - ((double)pY / bufferedImage.getHeight() * 2));
			double x = 0.0;
			double y = 0.0;
			int iteration = 0;
			while (x*x + y*y < (1 << 16) && iteration < max_iteration - 1) {
				double xTemp = x*x - y*y + x0;
				y = 2*x*y + y0;
				x = xTemp;
				iteration++;
			}
			int color = palette[iteration];
			if (iteration < max_iteration) {
				double zn = Math.sqrt(x*x + y*y);
				double nu = Math.log(Math.log(zn) / Math.log(2)) / Math.log(2);
				double iterationAprox = iteration + 1 - nu;
				if (iterationAprox < palette.length - 1) {
					int color1 = palette[(int)iterationAprox];
					int color2 = palette[(int)iterationAprox + 1];
					
					iterationAprox = iterationAprox - (int)iterationAprox;
					color = (int) (color1 + (color2 - color1) * iterationAprox);
				}
			}
			bufferedImage.setRGB(pX, pY, color);
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
