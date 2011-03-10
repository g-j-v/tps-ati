package renderer;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.util.Random;

import core.PixelRay;



public class BinaryImage extends Renderer {

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_GRAY);
	Random r = new Random();
	@Override
	public Color RenderPixel(PixelRay ray) {
		float colors[] = new float [1];
		//colors va de 0 a 255
		//colors[0] = (((float) ray.getPos().x /Settings.resolution.x + ((float) ray.getPos().y )/ Settings.resolution.y) / 2); 		 
		if(r.nextInt() <= 0)
			colors[0] = 0;
		else
			colors[0] = 1;
				
		
		Color c = new Color(space, colors, 1 );
		return c;

	}

	
}
