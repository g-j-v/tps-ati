package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;
import core.PixelRay;


public class DotProduct extends Filter {

	int x = 0;
	int y = 0;
	public static float alpha = 1.1f;
	public DotProduct() {
	
		x =oldImg.getWidth();
		y = oldImg.getHeight();
		
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
		
	
		colors[0] = red * alpha  /255;
		colors[1] = green * alpha  /255;
		colors[2] = blue * alpha  /255;
		if(colors[0] > 1)
			colors[0] = 1;
		if(colors[1] > 1)
			colors[1] = 1;
		if(colors[2] > 1)
			colors[2] = 1;
		
		Color c = new Color(space, colors, 1);
	
		return c;

	}

	
}
