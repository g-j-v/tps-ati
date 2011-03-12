package renderer.filters;

import gui.LoggerWindowHandler;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

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
	}
	
}
