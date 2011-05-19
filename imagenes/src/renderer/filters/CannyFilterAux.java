package renderer.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import javax.vecmath.Point2i;

import util.CannyEdgeDetector;
import util.CircleHough;
import util.hystThresh;
import util.nonmax;
import util.sobel;

import core.PixelRay;

public class CannyFilterAux extends Filter {

	BufferedImage edges ;
	public CannyFilterAux() {
	
		//create the detector
		CannyEdgeDetector detector = new CannyEdgeDetector();

		//adjust its parameters as desired
		detector.setLowThreshold(0.5f);
		detector.setHighThreshold(1f);

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
