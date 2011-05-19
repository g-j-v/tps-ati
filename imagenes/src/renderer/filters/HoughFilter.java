package renderer.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Vector;

import util.HoughLine;
import util.HoughTransform;
import core.PixelRay;
import core.Settings;



public class HoughFilter extends Filter {

	public static int DotPerLine = 30;
	protected BufferedImage auxImg;
	public static int black;
	public HoughFilter() {
	
		auxImg = new BufferedImage((int)Settings.getResolution().x, (int) Settings.getResolution().y, BufferedImage.TYPE_INT_ARGB);
		black = auxImg.getRGB(0, 0);
        // create a hough transform object with the right dimensions 
        HoughTransform h = new HoughTransform(oldImg.getWidth(), oldImg.getHeight()); 
 
        // add the points from the image (or call the addPoint method separately if your points are not in an image 
        h.addPoints(oldImg); 
 
        // get the lines out 
        Vector<HoughLine> lines = h.getLines(DotPerLine); 
 
        // draw the lines back onto the image 
        for (int j = 0; j < lines.size(); j++) { 
            HoughLine line = lines.elementAt(j); 
            line.draw(auxImg, Color.RED.getRGB()); 
        } 
	}
	
	@Override
	public Color RenderPixel(PixelRay ray) {
		
		
		int pixel = oldImg.getRGB(ray.getPos().x, ray.getPos().y);
		int auxpixel = auxImg.getRGB(ray.getPos().x, ray.getPos().y);
		
		if(auxpixel == black)
			return new Color(pixel);
		else
			return new Color(auxpixel);

	}

	
}
