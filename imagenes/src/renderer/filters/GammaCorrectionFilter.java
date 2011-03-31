package renderer.filters;

import gui.LoggerWindowHandler;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.util.logging.Logger;

import core.PixelRay;
import core.Settings;



public class GammaCorrectionFilter extends Filter {


	public static float alpha =2;
	private Logger logger = LoggerWindowHandler.getLogger("ContrastFilter");	
	float p1, p2, p3;
	public GammaCorrectionFilter() {
		p1 = (float)(Settings.contrastS1) / Settings.contrastLow;
		p2 = ((float)(Settings.contrastS2 - Settings.contrastS1)) / (Settings.contrastHigh - Settings.contrastLow);
		p3 = ((float)(255 - Settings.contrastS2)) / (255 - Settings.contrastHigh);
		logger.info("P1 " + p1);
		logger.info("P2 " + p2);
		logger.info("P3 " + p3);
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
		//System.out.println("original" + colors[2]);
				
		colors[2] = (float) Math.pow(colors[2], alpha);		
		//System.out.println("mod" + colors[2]);
		pixel = Color.HSBtoRGB(colors[0], colors[1], colors[2]);		
		Color c = new Color(pixel);
	
		return c;


	}

	
}
