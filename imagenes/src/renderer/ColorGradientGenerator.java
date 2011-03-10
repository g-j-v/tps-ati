package renderer;

import java.awt.Color;
import java.awt.color.ColorSpace;

import core.PixelRay;



public class ColorGradientGenerator extends Renderer {

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	@Override
	public Color RenderPixel(PixelRay ray) {
		float colors[] = new float [3];
		//colors va de 0 a 255
		colors[0] = (float) ray.getPos().x /res.x ; 		 
		colors[1] = ((float) ray.getPos().y )/ res.y;
		colors[2] = (( 1 - (float) ray.getPos().x /res.x + ( (float) ray.getPos().y )/ res.y) / 2); 		 
		
		Color c = new Color(space, colors, 1 );
		return c;

	}

	
}
