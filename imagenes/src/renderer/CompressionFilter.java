package renderer;

import gui.LoggerWindowHandler;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.logging.Logger;

import main.Main;
import core.PixelRay;
import core.Settings;



public class CompressionFilter extends Renderer {
	private Logger logger = LoggerWindowHandler.getLogger("CompressionFilter");	
	
	Random r = new Random();
	int max = 0;
	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	double c = 0;
	protected BufferedImage oldImg;
	
	public CompressionFilter() {
		float colors[] = new float [3];
		
		if(! Settings.isImageLoaded())
			Main.createImage();
		
		oldImg = Main.getImage();
		
		for (int x = 0; x < oldImg.getWidth(); x++) {
			for (int y = 0; y < oldImg.getHeight(); y++) {
				int pixel = oldImg.getRGB(x, y);
					if(pixel < max )
					max = pixel;
					
			}
		}
		max = max * -1;
		c = 255 / (Math.log(1+ max));
	}
	
	@Override
	public Color RenderPixel(PixelRay ray) {

		float colors[] = new float [3];
		int pixel = oldImg.getRGB(ray.getPos().x, ray.getPos().y);
		float red = (pixel >> 16) & 0xff;
		float green = (pixel >> 8) & 0xff;
		float blue = (pixel) & 0xff;
		Color.RGBtoHSB((int)red, (int)green, (int)blue, colors);//an array of three elements containing the hue, saturation and brightness (in that order)

		colors[2] = ((Double) (c * Math.log(1 -pixel))).floatValue();
		pixel = Color.HSBtoRGB(colors[0], colors[1], colors[2]);		
		Color col = new Color(pixel);
	
		return col;
	}

	
}
