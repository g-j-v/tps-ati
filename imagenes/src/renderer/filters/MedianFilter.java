package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.util.ArrayList;
import java.util.Collections;

import core.PixelRay;

public class MedianFilter extends Filter {

	int MLimit = 1;
	int NLimit = 1;

	int x = 0;
	int y = 0;
	int M = 3;
	int N = 3;
	
	
	public MedianFilter() {
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

			ArrayList<Float> rlist = new ArrayList<Float>();
			ArrayList<Float> glist = new ArrayList<Float>();
			ArrayList<Float> blist = new ArrayList<Float>();
            double sumr = 0;
            double sumb = 0;
            double sumg = 0;
			for (int j = -NLimit; j <= NLimit; j++) {
				for (int i = -MLimit; i <= MLimit; i++) {
					int p = oldImg.getRGB(ray.getPos().x + i, ray.getPos().y + j);
		            float red = (p >> 16) & 0xff;
		    		float green = (p >> 8) & 0xff;
		    		float blue = (p) & 0xff;
		    		rlist.add(red);
		    		blist.add(blue);
		    		glist.add(green);
				}
			}

			Collections.sort(rlist);
			Collections.sort(blist);
			Collections.sort(glist);
			sumr = rlist.get(4);
			sumg = glist.get(4);
			sumb = blist.get(4);
			
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
