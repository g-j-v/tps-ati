package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;

import core.PixelRay;

public class SaltnPepperNoiseFilter extends Filter {

	public static float p0 = 1;
	public static float p1 = 1;

	public SaltnPepperNoiseFilter() {

	}

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);

	@Override
	public Color RenderPixel(PixelRay ray) {
		float colors[] = new float[3];

		int pixel = oldImg.getRGB(ray.getPos().x, ray.getPos().y);
		float red = (pixel >> 16) & 0xff;
		float green = (pixel >> 8) & 0xff;
		float blue = (pixel) & 0xff;
		Color.RGBtoHSB((int) red, (int) green, (int) blue, colors);// an array
																	// of three
																	// elements
																	// containing
																	// the hue,
																	// saturation
																	// and
																	// brightness
																	// (in that
																	// order)
		double rnd = Math.random() ;
		if(rnd >= p1){
			colors[2] = 1; 
		} else if (rnd <= p0){
			colors[2] = 0;
		}
		
		pixel = Color.HSBtoRGB(colors[0], colors[1], colors[2]);
		Color c = new Color(pixel);

		return c;

	}

}
