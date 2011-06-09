package renderer.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;

import util.CannyEdgeDetector;
import core.PixelRay;

public class CannyFilterAux extends Filter {

	BufferedImage edges ;
	public CannyFilterAux() {
	
		//create the detector
		CannyEdgeDetector detector = new CannyEdgeDetector();

		//adjust its parameters as desired
		detector.setLowThreshold(2.0f);
		detector.setHighThreshold(2.5f);

		//apply it to an image
		detector.setSourceImage(oldImg);
		detector.process();
		edges = detector.getEdgesImage();
		
		}
		
	

	

	@Override
	public Color RenderPixel(PixelRay ray) {
		return new Color(edges.getRGB(ray.getPos().x, ray.getPos().y));
	}

}
