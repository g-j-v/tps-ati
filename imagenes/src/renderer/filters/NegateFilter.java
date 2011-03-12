package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;
import core.PixelRay;



public class NegateFilter extends Filter {

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	@Override
	public Color RenderPixel(PixelRay ray) {
		float colors[] = new float [3];
		
		int pixel = oldImg.getRGB(ray.getPos().x, ray.getPos().y);
		float red = (pixel >> 16) & 0xff;
		float green = (pixel >> 8) & 0xff;
		float blue = (pixel) & 0xff;
		
		
		//colors va de 0 a 255 pero en realidad de 0 a 1.
		colors[0] = (255 - red) /255; 		 
		colors[1] = (255 - green)/255 ;
		colors[2] = (255 - blue)/255; 		 
		//System.out.println("red " + colors[0] + "green " + colors[1] + "blue " + colors[2]  );
		Color c = new Color(space, colors, 1 );
		return c;

	}

	
}
