package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;

import util.Matrix;
import core.PixelRay;

public class ApplyMatrixFilter extends Filter {

	
	public static int N1 = 5;
	public static int N2 = 5;
	public static int N3 = 5;
	public static int N4 = -3;
	public static int N5 = 0;
	public static int N6 = -3;
	public static int N7 = -3;
	public static int N8 = -3;
	public static int N9 = -3;

	private static int N = 3;
	private static int M = 3;
	public static Matrix mat = new Matrix(N, M); 
	
	int MLimit = 3;
	int NLimit = 3;

	int x = 0;
	int y = 0;

	
	public ApplyMatrixFilter() {
		MLimit = (M - 1) / 2;
		NLimit = (N - 1) / 2;
		x = oldImg.getWidth();
		y = oldImg.getHeight();
		mat.setData(0, 0, N1);
		mat.setData(0, 1, N2);
		mat.setData(0, 2, N3);
		mat.setData(1, 0, N4);
		mat.setData(1, 1, N5);
		mat.setData(1, 2, N6);
		mat.setData(2, 0, N7);
		mat.setData(2, 1, N8);
		mat.setData(2, 2, N9);
	}

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);

	@Override
	public Color RenderPixel(PixelRay ray) {
		float colors[] = new float[3];

		int pixel = oldImg.getRGB(ray.getPos().x, ray.getPos().y);

		if(ray.getPos().x < N || ray.getPos().x >(x - N) || ray.getPos().y < M || ray.getPos().y >(y - M))
		{		
			return new Color(pixel);

		}else{

            double sumr = 0;
            double sumb = 0;
            double sumg = 0;
			for (int j = -NLimit; j <= NLimit; j++) {
				for (int i = -MLimit; i <= MLimit; i++) {
					int p = oldImg.getRGB(ray.getPos().x + i, ray.getPos().y + j);
		            float red = (p >> 16) & 0xff;
		    		float green = (p >> 8) & 0xff;
		    		float blue = (p) & 0xff;

					// get the corresponding filter coefficient
					double c = mat.getData(j + NLimit, i + MLimit);
					sumr = sumr + c * red;
					sumg = sumg + c * green;
					sumb = sumb + c * blue;

				}
			}

			colors[0] = (float) sumr /255 ;
			colors[1] = (float) sumg /255;
			colors[2] = (float) sumb /255;
			
			if(colors[0] > 1)
				colors[0] = 1;
			if(colors[0] < 0)
				colors[0] = 0;
			if(colors[1] > 1)
				colors[1] = 1;
			if(colors[1] < 0)
				colors[1] = 0;
			if(colors[2] > 1)
				colors[2] = 1;
			if(colors[2] < 0)
				colors[2] = 0;
			
			Color c = new Color(space, colors, 1);
			return c;
			
		}
		
	}

}
