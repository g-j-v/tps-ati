package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

import core.PixelRay;

public class AnisotropicFilter extends Filter {

	public static int steps;
	public static double kappa;

	int h = oldImg.getHeight();
	int w = oldImg.getWidth();

	BufferedImage newImg = new BufferedImage(h, w, oldImg.getType());
	BufferedImage tmpImg = new BufferedImage(h, w, oldImg.getType());
	float lambda = (float) 0.25;

	public AnisotropicFilter() {

		newImg.setData(oldImg.getData());
		tmpImg.setData(oldImg.getData());

		float colors[] = new float[3];
		for (int k = 1; k <= steps; k++) {
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {

					int p = newImg.getRGB(i, j);
					float red = (p >> 16) & 0xff;

					int dN;
					if (i == 0) {
						dN = 0;
					} else {
						dN = newImg.getRGB(i - 1, j);
					}
					float dNred = (float) ((dN >> 16) & 0xff) - red;
					float cN = g(dNred);

					int dS;
					if (i == h - 1) {
						dS = 0;
					} else {
						dS = newImg.getRGB(i + 1, j);
					}
					float dSred = (float) ((dS >> 16) & 0xff) - red;
					float cS = g(dSred);

					int dE;
					if (j == w - 1) {
						dE = 0;
					} else {
						dE = newImg.getRGB(i, j + 1);
					}
					float dEred = (float) ((dE >> 16) & 0xff) - red;
					float cE = g(dEred);

					int dO;
					if (j == 0) {
						dO = 0;
					} else {
						dO = newImg.getRGB(i, j - 1);
					}
					float dOred = (float) ((dO >> 16) & 0xff) - red;
					float cO = g(dOred);

					red = red
							+ lambda
							* (dNred * cN + dSred * cS + dEred * cE + dOred
									* cO);

					colors[0] = (float) red / 255;
					colors[1] = (float) red / 255;
					colors[2] = (float) red / 255;

					if (colors[0] > 1)
						colors[0] = 1;
					if (colors[0] < 0)
						colors[0] = 0;
					if (colors[1] > 1)
						colors[1] = 1;
					if (colors[1] < 0)
						colors[1] = 0;
					if (colors[2] > 1)
						colors[2] = 1;
					if (colors[2] < 0)
						colors[2] = 0;

					Color c = new Color(space, colors, 1);
					tmpImg.setRGB(i, j, c.getRGB());

				}
			}

			newImg = tmpImg;
		}

	}

	private float g(float dNred) {
		if (kappa == 0) {
			return 1;
		}

		float ret = (float) Math.pow(Math.E, -Math.pow((dNred / kappa), 2));
		System.err.println("un pow " + Math.pow((dNred / kappa), 2) + " y e "
				+ Math.E + " elevados " + ret + " y dnred " + dNred);
		return ret;
	}

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);

	@Override
	public Color RenderPixel(PixelRay ray) {

		int pixel = newImg.getRGB(ray.getPos().x, ray.getPos().y);

		Color c = new Color(pixel);

		return c;

	}

}
