package renderer.filters;

import gui.histogram.HistPanel;
import gui.histogram.Histogram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.color.ColorSpace;

import javax.swing.JFrame;

import main.Main;
import core.PixelRay;
import core.Settings;



public class HisteresisFilter extends Filter {
	
	public HisteresisFilter() {
		
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
		
		
		//colors va de 0 a 255 pero en realidad de 0 a 1.
		//System.out.println("red " + colors[0] + "green " + colors[1] + "blue " + colors[2]  );
		Color c = new Color(pixel);
		return c;

	}
}
