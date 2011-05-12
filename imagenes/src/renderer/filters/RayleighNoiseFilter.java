package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;

import core.PixelRay;

public class RayleighNoiseFilter extends Filter {

	public static float psi = 2;

	public RayleighNoiseFilter() {

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

		colors[2] = colors[2]
				* (float) (psi * Math.sqrt(-2 * Math.log(1 - Math.random())));

		pixel = Color.HSBtoRGB(colors[0], colors[1], colors[2] ); // TODO:
																		// chequear
																		// este
																		// método.
		Color c = new Color(pixel);

		return c;

	}

}
