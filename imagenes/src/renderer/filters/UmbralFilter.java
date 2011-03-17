package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;
import core.PixelRay;
import core.Settings;



public class UmbralFilter extends Filter {

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	@Override
	public Color RenderPixel(PixelRay ray) {
		float colors[] = new float [3];
		
		int pixel = oldImg.getRGB(ray.getPos().x, ray.getPos().y);
		float red = (pixel >> 16) & 0xff;
		float green = (pixel >> 8) & 0xff;
		float blue = (pixel) & 0xff;
		Color.RGBtoHSB((int)red, (int)green, (int)blue, colors);//an array of three elements containing the hue, saturation and brightness (in that order)
		colors[2] = colors[2] * 255;
		if(colors[2] < Settings.umbral)
			return Color.BLACK;
		else
			return Color.WHITE;

	}

	
}
