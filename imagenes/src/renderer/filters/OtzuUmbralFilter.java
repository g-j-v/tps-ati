package renderer.filters;

import gui.histogram.HistPanel;
import gui.histogram.Histogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.PixelGrabber;

import javax.swing.JFrame;


import core.PixelRay;

public class OtzuUmbralFilter extends Filter {

	// http://www.labbookpages.co.uk/software/imgProc/otsuThreshold.html
	// Hay un bug que hay que revisar.
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
		byte[] srcData = new byte[w * h];
		int[] orig = new int[w * h];
		PixelGrabber grabber = new PixelGrabber(oldImg, 0, 0, w, h,
				orig, 0, w);
		try {
			grabber.grabPixels();
		} catch (InterruptedException e2) {
			System.out.println("error: " + e2);
		}
		for(int i=0;i < w*h ;i++){
			int pixel = orig[i];
			float red = (pixel >> 16) & 0xff;
			float green = (pixel >> 8) & 0xff;
			float blue = (pixel) & 0xff;
			float[] colors = new float[3];
			Color.RGBtoHSB((int)red, (int)green, (int)blue, colors);//an array of three elements containing the hue, saturation and brightness (in that order)
			colors[2] = colors[2] * 255;
			srcData[i] = (byte) colors[2];
		}
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
		for (int t=0 ; t<256 ; t++) 
			sum += t * histogram.getBins()[t];

		float sumB = 0;
		int wB = 0;
		int wF = 0;

		float varMax =0;// Float.MAX_VALUE;
		threshold = 0;
		 wB += histogram.getBins()[0];  
		for (int t=1 ; t<256 ; t++) {
		   wB += histogram.getBins()[t];          // Weight Background
		   if (wB == 0) continue;

		   wF = (int) (N - wB);                 // Weight Foreground
		   if (wF == 0) break;
		   float WB = wB/N;
		   float WF = wF/N;
		   
		   sumB += (float) (t * histogram.getBins()[t]);

		   float mB = sumB / wB;            // Mean Background
		   float mF = (sum - sumB) / wF;    // Mean Foreground

		   float osumB = 0, osumF = 0, oB = 0, oF = 0;
		   for(int i =0; i < 256 ; i++)
		   {
			   if(i <= t) 
				   osumB += (i *( histogram.getBins()[i] * mB));
			   else
				   osumF += (i *( histogram.getBins()[i] * mF));
						   
		   }
		   oB = osumB / wB;
		   oF = osumF / wF;
		   
		   
		   // Calculate Between Class Variance
		   float varBetween = Math.abs(WB * (oB*oB) + WF *(oF*oF));
	//		System.out.println("var " +varBetween);
	//		System.out.println("WB " +WB);
	//		System.out.println("WF " +WF);
		//	System.out.println("oF " +oF);
		//	System.out.println("oB " +oB);
		//	System.out.println("osumF " +osumF);
		//	System.out.println("oBsum " +osumB);
			
		   // Check if new maximum found
		   if (varBetween > varMax) {
				System.out.println("var " +varBetween);
				System.out.println("t " + t);

		      varMax = varBetween;
		      threshold = t;
		   }
		}
	

		System.out.println(threshold);
	}

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
		colors[2] = colors[2] * 255;
		if (colors[2] < threshold)
			return Color.BLACK;
		else
			return Color.WHITE;

	}

}
