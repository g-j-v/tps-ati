package renderer.filters;

import gui.histogram.HistPanel;
import gui.histogram.Histogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.color.ColorSpace;

import javax.swing.JFrame;

import core.PixelRay;
import core.Settings;



public class EqualizeFilter extends Filter {
	Histogram histogram;
	HistPanel panel;
	double smin;
	int h = oldImg.getHeight();
	int w = oldImg.getWidth();
	
	public EqualizeFilter() {
		JFrame frame = new JFrame("Histogram Equalization");
		histogram = new Histogram("Frequency" ,"Grey level",Settings.bins, 0, 1);
		panel = new HistPanel(histogram);
		frame.setSize(600, 400);
		panel.setSize(600, 400);
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		

		float colors[] = new float [3];
		
		for (int i = 0; i < w ; i++){
			for (int j = 0; j < h ; j++){
				
				int pixel = oldImg.getRGB(i, j);
				float red = (pixel >> 16) & 0xff;
				float green = (pixel >> 8) & 0xff;
				float blue = (pixel) & 0xff;
				
				Color.RGBtoHSB((int)red, (int)green, (int)blue, colors);//an array of three elements containing the hue, saturation and brightness (in that order)
				
				
				histogram.add(colors[2]);
			}
		}
		
		smin = histogram.sumUp(0)/((double)h*w);
	
	}
	
	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	@Override
	public Color RenderPixel(PixelRay ray) {
		float colors[] = new float [3];
		
		int pixel = oldImg.getRGB(ray.getPos().x, ray.getPos().y);
		float red = (pixel >> 16) & 0xff;
		float green = (pixel >> 8) & 0xff;
		float blue = (pixel) & 0xff;
		
		Color.RGBtoHSB((int)red, (int)green, (int)blue, colors);//an array of three elements containing the hue, saturation and brightness (in that order)
		
		colors[2] = colors[2] * 255;
		
		double sk = histogram.sumUp((int)(colors[2]*histogram.getBins().length/(double)255))/(double)(h*w);
		
		colors[2] = (int)((sk-smin)/(1-smin)*255 + 0.5);
		
		if(colors[2] > 255)
			colors[2] = 255;
		colors[2] = colors[2] /255;
		
		pixel = Color.HSBtoRGB(colors[0], colors[1], colors[2]);		
		Color c = new Color(pixel);
	
		return c;


	}

	
}
