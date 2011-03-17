package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;
import core.PixelRay;
import core.Settings;



public class Product extends Filter {

	int x = 0;
	int y = 0;
	public Product() {
		if(Settings.secondaryImage == null)
		{
			throw new IllegalArgumentException("The second image is not loaded");
		}
		x = Math.min(oldImg.getWidth(), Settings.secondaryImage.getWidth());
		y = Math.min(oldImg.getHeight(), Settings.secondaryImage.getHeight());
		
	}

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	@Override
	public Color RenderPixel(PixelRay ray) {
		if(ray.getPos().x > x || ray.getPos().y > y)
			return Color.BLACK;

		float colors[] = new float [3];
		
		int pixel = oldImg.getRGB(ray.getPos().x, ray.getPos().y);
		float red = (pixel >> 16) & 0xff;
		float green = (pixel >> 8) & 0xff;
		float blue = (pixel) & 0xff;
	
		int pixel2 = Settings.secondaryImage.getRGB(ray.getPos().x, ray.getPos().y);
		float red2 = (pixel2 >> 16) & 0xff;
		float green2 = (pixel2 >> 8) & 0xff;
		float blue2 = (pixel2) & 0xff;
	
		colors[0] = red * red2  /65025;
		colors[1] = green * green2  /65025;
		colors[2] = blue * blue2  /65025;
		
		Color c = new Color(space, colors, 1);
	
		return c;

	}

	
}
