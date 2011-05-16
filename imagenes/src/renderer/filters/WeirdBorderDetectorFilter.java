package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;

import util.Matrix;

import core.PixelRay;

public class WeirdBorderDetectorFilter extends Filter {


	public static Matrix mat;

	int NLimit = 1;

	int x = 0;
	int y = 0;

	public WeirdBorderDetectorFilter() {

		System.err.println("Entered LLLLLLLap");
		mat = Matrix.laplaciana();
		mat.show();
		
		x = oldImg.getWidth();
		y = oldImg.getHeight();

	}

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);

	@Override
	public Color RenderPixel(PixelRay ray) {

		float colors[] = new float[3];

		int pixel = oldImg.getRGB(ray.getPos().x, ray.getPos().y);

		if (ray.getPos().x < NLimit || ray.getPos().x >= (x - NLimit)
				|| ray.getPos().y < NLimit || ray.getPos().y >= (y - NLimit)) {
			return new Color(pixel);

		} else {

			double sumr = 0;
			double sumb = 0;
			double sumg = 0;
			for (int j = -NLimit; j <= NLimit; j++) {
				for (int i = -NLimit; i <= NLimit; i++) {
					int p = oldImg.getRGB(ray.getPos().x + i, ray.getPos().y
							+ j);
					float red = (p >> 16) & 0xff;
					float green = (p >> 8) & 0xff;
					float blue = (p) & 0xff;

					// get the corresponding filter coefficient
					double c = mat.getData(j + NLimit, i + NLimit);
					sumr = sumr + c * red;
					sumg = sumg + c * green;
					sumb = sumb + c * blue;

				}
			}

			colors[0] = (float) sumr / 255;
			colors[1] = (float) sumg / 255;
			colors[2] = (float) sumb / 255;

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
			return c;

		}

	}

}
