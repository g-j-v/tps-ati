package renderer;

import java.awt.Color;
import java.awt.color.ColorSpace;

import core.PixelRay;
import core.Settings;



public class GrayScaleGenerator extends Renderer {

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_GRAY);
	@Override
	public Color RenderPixel(PixelRay ray) {
		float colors[] = new float [1];
		//colors va de 0 a 255
		//colors[0] = (((float) ray.getPos().x /Settings.resolution.x + ((float) ray.getPos().y )/ Settings.resolution.y) / 2); 		 
		colors[0] = ((float) ray.getPos().x * ray.getPos().y) / (Settings.getResolution().y * Settings.getResolution().x); 		 
		
		Color c = new Color(space, colors, 1 );
		return c;

	}

	
}
