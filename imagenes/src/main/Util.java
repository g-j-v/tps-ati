/**
 * 
 */
package main;

import gui.Frame.AlertType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import core.Settings;

/**
 * @author dazar
 *
 */
public class Util {

	public static Logger logger = Logger.getLogger(Util.class.toString());	
	private static final int MAXRGB = 255;
	public Util() {
		// TODO Auto-generated constructor stub
	}
	
	public static BufferedImage loadImage(String image){
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(image));
		} catch (IOException e) {
			logger.info("Error loading image");
		}
		return img;
	}
	
	
	
	public static void saveImage(String path)
	{
		String extension = "";
		File file = new File(path);
		try {
			if(path.contains(".bmp"))
				extension = "bmp";
			else
				if(path.contains(".png"))
					extension = "png";
				else
					if(path.contains(".raw"))
						extension = "raw";
					else
						if(path.contains(".pgm"))
							extension = "pgm";
						else
							if(path.contains(".ppm"))
								extension = "ppm";
			ImageIO.write(Main.image, extension, file);
			Main.getFrame().ShowDialog("File "+path+" was saved succesfully!", "Image Saved", AlertType.INFO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setPixel(int x, int y, int red, int green, int blue){
		if(x < 0 || x > Settings.getResolution().x 
				|| y < 0 || y > Settings.getResolution().y){
			Main.getFrame().ShowDialog("Coordinates out of range!", "Rendering", AlertType.ERROR);
			return;
		}
		
		if(red < 0 || red > MAXRGB || blue < 0 || blue > MAXRGB || green < 0 || green > MAXRGB ){
			Main.getFrame().ShowDialog("RGB values out of range, must be a value greater than 0 and less than 255!", "Rendering", AlertType.ERROR);
			return;
		}
		
		Main.getImage().setRGB(x, y, 0xFF000000 | (red << 16) | (green << 8) | blue );
		
	}
	
	
}
