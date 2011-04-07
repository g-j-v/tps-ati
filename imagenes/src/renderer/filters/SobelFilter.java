package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;

import util.Matrix;
import core.PixelRay;

public class SobelFilter extends Filter {

	public static int N = 3;
	public static int M = 3;
	public static Matrix mat1 = new Matrix(N, M); 
	public static Matrix mat2 = new Matrix(N, M); 
	
	int MLimit = 3;
	int NLimit = 3;

	int x = 0;
	int y = 0;

	
	public SobelFilter() {
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

		if(ray.getPos().x < N || ray.getPos().x >(x - N) || ray.getPos().y < M || ray.getPos().y >(y - M))
		{		
			return new Color(pixel);

		}else{

            double sum1r = 0;
            double sum1b = 0;
            double sum1g = 0;
            double sum2r = 0;
            double sum2b = 0;
            double sum2g = 0;
            
            
			for (int j = -NLimit; j <= NLimit; j++) {
				for (int i = -MLimit; i <= MLimit; i++) {
					int p = oldImg.getRGB(ray.getPos().x + i, ray.getPos().y + j);
		            float red = (p >> 16) & 0xff;
		    		float green = (p >> 8) & 0xff;
		    		float blue = (p) & 0xff;

					// get the corresponding filter coefficient
					double c = mat1.getData(j + NLimit, i + MLimit);
					sum1r = sum1r + c * red;
					sum1g = sum1g + c * green;
					sum1b = sum1b + c * blue;

					c = mat2.getData(j + NLimit, i + MLimit);
					sum2r = sum2r + c * red;
					sum2g = sum2g + c * green;
					sum2b = sum2b + c * blue;

				}
			}

			colors[0] = (float) sum1r /255 + (float)sum2r /255 ;
			colors[1] = (float) sum1g /255 + (float)sum2g / 255;
			colors[2] = (float) sum1b /255 + (float)sum2b /255 ;
			
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
