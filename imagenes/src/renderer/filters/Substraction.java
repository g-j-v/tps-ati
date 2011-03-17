package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;
import core.PixelRay;
import core.Settings;



public class Substraction extends Filter {

	float beta = 1 - Settings.alpha;
	float alpha = Settings.alpha;
	int x = 0;
	int y = 0;
	public Substraction() {
		if(Settings.secondaryImage == null)
		{
			throw new IllegalArgumentException("The second image is not loaded");
		}
		x = Math.min(oldImg.getWidth(), Settings.secondaryImage.getWidth());
		y = Math.min(oldImg.getHeight(), Settings.secondaryImage.getHeight());
		
	}
	//zs = (Λ -1)/2 +(x – y)/2.
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
	
		colors[0] = (127.5f + (red - red2 )/2) / 255;
		colors[1] = (127.5f + (green - green2 )/2) / 255;
		colors[2] =	(127.5f + (blue - blue2 )/2) / 255;
		
		Color c = new Color(space, colors, 1);
	
		return c;

	}

	
}
