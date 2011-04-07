package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;

import core.PixelRay;

public class ExponencialNoiseFilter extends Filter {

	public static float lambda = 2;

	public ExponencialNoiseFilter() {

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
				* (float) (-1 / lambda * Math.log(Math.random() + 0.0001));
		pixel = Color.HSBtoRGB(colors[0], colors[1], colors[2]);
		Color c = new Color(pixel);

		return c;

	}

}
