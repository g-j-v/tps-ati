package main;



import gui.Frame;
import gui.LoggerWindowHandler;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import core.Settings;

import renderer.Renderer;





public class Main {
	
	private static Frame frame;
	public static Renderer renderer;
	public static BufferedImage image;
	private static Logger logger = LoggerWindowHandler.getLogger("Main");

	public static void main(String[] args) throws Exception
	{
	
		logger.setLevel(Level.ALL);
		createGUI();
	}
	
	
	private static void createGUI()
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run() {
				frame = new Frame();			
			}
			
		});
	}
	
	
	public static BufferedImage getImage() {
		return image;
	}
	
	public static void createImage()
	{
		image = new BufferedImage((int)Settings.getResolution().x, (int) Settings.getResolution().y, BufferedImage.TYPE_INT_RGB);
	}

	public static void setImage(BufferedImage image) {
		Main.image = image;
	}

	public static Frame getFrame() {
		return frame;
	}

	public static Renderer getRenderer() {
		return renderer;
	}
	

	public static void update(){
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				Main.getFrame().getPanel().repaint();
			}

		});
	}
	
}
