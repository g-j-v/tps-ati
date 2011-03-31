package renderer.filters;

import gui.ImagePanel;
import gui.LoggerWindowHandler;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.swing.JFrame;

import main.Main;

import renderer.Renderer;


public abstract class Filter extends Renderer {

	protected BufferedImage oldImg;
	private Logger logger = LoggerWindowHandler.getLogger("Filter");	
	
	public Filter() {
		oldImg = Main.getImage();
		if(oldImg == null)
		{
			logger.severe("There is no image loaded.");
			throw new IllegalArgumentException("No image loaded.");
		}
		JFrame frame = new JFrame("Old Image");
		//frame.setBounds(50, 50, 400, 400);
		frame.setPreferredSize(new Dimension(oldImg.getWidth(), oldImg.getHeight()));
		frame.add(new ImagePanel(oldImg));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	
	}
	
}
