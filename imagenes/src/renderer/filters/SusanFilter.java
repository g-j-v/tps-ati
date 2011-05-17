package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;

import util.Matrix;
import core.PixelRay;

public class SusanFilter extends Filter {

	private static int N = 7;
	private static int M = 7;
	public static Matrix mat = new Matrix(N, M);
	float tole = 0.1f;
	int MLimit = 3;
	int NLimit = 3;

	int x = 0;
	int y = 0;
	public static int mode = 1;
	public SusanFilter() {
		MLimit = (M - 1) / 2;
		NLimit = (N - 1) / 2;
		x = oldImg.getWidth();
		y = oldImg.getHeight();

	}

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);

	@Override
	public Color RenderPixel(PixelRay ray) {
		float colors[] = new float[3];

		int pixel = oldImg.getRGB(ray.getPos().x, ray.getPos().y);

		if (ray.getPos().x < N || ray.getPos().x > (x - N)
				|| ray.getPos().y < M || ray.getPos().y > (y - M)) {
			return new Color(pixel);

		} else {

			float mired = (pixel >> 16) & 0xff;
			float migreen = (pixel >> 8) & 0xff;
			float miblue = (pixel) & 0xff;
			Color.RGBtoHSB((int) mired, (int) migreen, (int) miblue, colors);

			float whiteval = colors[2];
			float total = 0f;
			float count = 0f;
			for (int j = -NLimit; j <= NLimit; j++) {
				for (int i = -MLimit; i <= MLimit; i++) {
					if (Math.abs(j) + Math.abs(i) <= 4) {
						total++;
						int p = oldImg.getRGB(ray.getPos().x + i,
								ray.getPos().y + j);
						float red = (p >> 16) & 0xff;
						float green = (p >> 8) & 0xff;
						float blue = (p) & 0xff;
						Color.RGBtoHSB((int) red, (int) green, (int) blue,
								colors);// an array of three elements containing
										// the hue, saturation and brightness
										// (in that order)
						if (Math.abs(colors[2] - whiteval) < tole)
							count++;
					}
				}
			}


			
			if(mode == 1){
				if(Math.abs(count/total - 0.5) < tole )
					return	Color.WHITE;
			}else 
				if(Math.abs(count/total - 0.25) < (tole +0.1) )
					return	Color.RED;
			
			return new Color(pixel);//Color.BLACK;


		}

	}

}
