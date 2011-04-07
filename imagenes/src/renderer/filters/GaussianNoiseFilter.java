package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;

import core.PixelRay;

public class GaussianNoiseFilter extends Filter {

	public static float deviation = 1;
	public static float vmedio = 1;

	public GaussianNoiseFilter() {

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
//		System.out.println("original" + colors[2]);

		colors[2] = colors[2]*255
				+ (float) (deviation * Math.sqrt(-2 * Math.log(Math.random()))
						* Math.cos(2 * Math.PI * Math.random()) + vmedio);
//		System.out.println("mod" + colors[2]);
		pixel = Color.HSBtoRGB(colors[0], colors[1], colors[2] / 255);
		Color c = new Color(pixel);

		return c;

	}

}
