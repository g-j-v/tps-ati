package renderer.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import javax.vecmath.Point2i;

import util.sobel;
import core.PixelRay;

public class CannyFilter3 extends Filter {

	public static float filterSigma = 3;

	private GaussianFilter gf;
	private int w = oldImg.getWidth();
	private int h = oldImg.getHeight();
	private BufferedImage newImg = new BufferedImage(w, h, oldImg.getType());

	public int orig[] = null;
	double direction[] = null;
	int sectors[] = null;

	private sobel sobelObject;

	public CannyFilter3() {

		applyGaussian();
		applySobel();
		findDirections();

	}


	@Override
	public Color RenderPixel(PixelRay pixRay) {

		return new Color(newImg.getRGB(pixRay.getPos().getX(), pixRay.getPos()
				.getY()));
	}

	private void applyGaussian() {

		gf = new GaussianFilter(filterSigma);

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color c = gf.RenderPixel(new PixelRay(new Point2i(i, j)));
				newImg.setRGB(i, j, c.getRGB());
			}
		}
	}

	private void applySobel() {
		orig = new int[w * h];
		PixelGrabber grabber = new PixelGrabber(oldImg, 0, 0, w, h, orig, 0, w);
		try {
			grabber.grabPixels();
		} catch (InterruptedException e2) {
			System.out.println("error: " + e2);
		}

		sobelObject = new sobel();

		sobelObject.init(orig, w, h);
		orig = sobelObject.process();
	}

	private void findDirections() {
		direction = new double[w * h];
		direction = sobelObject.getDirection();
		
		sectors = new int[w*h];
	}
	
	// Function to check which sector the line is in (see Machine Vision pg 171)
	private int sector(double theta) {

		// Converting into degrees from radians, and moving to lie between 0 and
		// 360
		theta = Math.toDegrees(theta);
		theta = theta + 270;
		theta = theta % 360;

		if ((theta >= 337.5) || (theta < 22.5)
				|| ((theta >= 157.5) && (theta < 202.5))) {
			return 0;
		}
		if (((theta >= 22.5) && (theta < 67.5))
				|| ((theta >= 202.5) && (theta < 247.5))) {
			return 1;
		}
		if (((theta >= 67.5) && (theta < 112.5))
				|| ((theta >= 247.5) && (theta < 292.5))) {
			return 2;
		}
		if (((theta >= 112.5) && (theta < 157.5))
				|| ((theta >= 292.5) && (theta < 337.5))) {
			return 3;
		}
		return 0;
		
	}
}
