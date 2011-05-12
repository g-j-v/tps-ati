package renderer.filters;

import gui.histogram.HistPanel;
import gui.histogram.Histogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import core.PixelRay;

public class OtzuUmbralFilter extends Filter {
	
	//http://www.labbookpages.co.uk/software/imgProc/otsuThreshold.html
	//Hay un bug que hay que revisar.
	Histogram histogram;
	HistPanel panel;
	double smin;
	int h = oldImg.getHeight();
	int w = oldImg.getWidth();
	float alphaT = 0.1f;
	private float N;
	private float[] probability;
	private int bins = 256;
	float threshold;
	public OtzuUmbralFilter() {
		N = w* h;
		probability = new float[256];
		JFrame frame = new JFrame("Histogram");
		histogram = new Histogram("Frequency", "Grey level", bins, 0,
				255);
		panel = new HistPanel(histogram);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		float colors[] = new float[3];

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int pixel = oldImg.getRGB(i, j);
				float red = (pixel >> 16) & 0xff;
				float green = (pixel >> 8) & 0xff;
				float blue = (pixel) & 0xff;
				Color.RGBtoHSB((int) red, (int) green, (int) blue, colors);// an
				histogram.add(colors[2] * 255);
			}
		}
		
		for(int i = 0; i < bins; i++)
		{
			probability[i] = histogram.getBins()[i]/ N;
		}
		
		float sum = 0;
		for (int t=0 ; t<256 ; t++) sum += t * histogram.getBins()[t];

		float sumB = 0;
		int wB = 0;
		int wF = 0;

		float varMax = 0;
		threshold = 0;

		for (int t=0 ; t<256 ; t++) {
		   wB += histogram.getBins()[t];          // Weight Background
		   if (wB == 0) continue;

		   wF = (int) (N - wB);                 // Weight Foreground
		   if (wF == 0) break;

		   sumB += (float) (t * histogram.getBins()[t]);

		   float mB = sumB / wB;            // Mean Background
		   float mF = (sum - sumB) / wF;    // Mean Foreground

		   // Calculate Between Class Variance
		   float varBetween = (float)wB * (float)wF * (mB - mF) * (mB - mF);

		   // Check if new maximum found
		   if (varBetween > varMax) {
		      varMax = varBetween;
		      threshold = t;
		   }
		}
	}


	@Override
	public Color RenderPixel(PixelRay ray) {
		float colors[] = new float [3];
		
		int pixel = oldImg.getRGB(ray.getPos().x, ray.getPos().y);
		float red = (pixel >> 16) & 0xff;
		float green = (pixel >> 8) & 0xff;
		float blue = (pixel) & 0xff;
		Color.RGBtoHSB((int)red, (int)green, (int)blue, colors);//an array of three elements containing the hue, saturation and brightness (in that order)
		colors[2] = colors[2] * 255;
		if(colors[2] < threshold)
			return Color.BLACK;
		else
			return Color.WHITE;

	}

}
