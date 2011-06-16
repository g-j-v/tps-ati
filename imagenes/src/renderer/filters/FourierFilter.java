package renderer.filters;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

import javax.vecmath.Point2i;

import util.CircleHough;
import util.hystThresh;
import util.nonmax;
import util.sobel;
import core.PixelRay;
import fourier.FFT;
import fourier.FreqFilter;
import fourier.InverseFFT;
import fourier.TwoDArray;

public class FourierFilter extends Filter {

	protected BufferedImage auxImg;
	public static int black;
	public CircleHough circleHoughObject;
	public int orig[] = null;
	public int width, height;
	private FFT fft;
	private boolean lowpass = true;
	private MemoryImageSource foutput;
	private BufferedImage finalimg;
	private InverseFFT inverse;
	static sobel sobelObject;
	static nonmax nonMaxSuppressionObject;
	static hystThresh hystThreshObject;
	public static int radius = 15;
	public static int circles = 5;


	public FourierFilter() {
		height = oldImg.getHeight();
		width = oldImg.getWidth();

		inverse = new InverseFFT();
		orig = new int[width * height];
		PixelGrabber grabber = new PixelGrabber(oldImg, 0, 0, width, height,
				orig, 0, width);
		try {
			grabber.grabPixels();
		} catch (InterruptedException e2) {
			System.out.println("error: " + e2);
		}
	
		fft = new FFT(orig, width, height);
		TwoDArray output = inverse.transform(fft.intermediate);
		//fft.intermediate = FreqFilter.filter(fft.intermediate,lowpass ,radius);
//	foutput = new MemoryImageSource(width, height, inverse.getPixels(output), 0, width);
		foutput = new MemoryImageSource(width, height, fft.getPixels(), 0, width);

		      finalimg = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
		      Image piximg = Toolkit.getDefaultToolkit().createImage(foutput);
		        finalimg.getGraphics().drawImage(piximg, 0, 0, null);
	}

	@Override
	public Color RenderPixel(PixelRay ray) {
		
		 return new Color(finalimg.getRGB(ray.getPos().x, ray.getPos().y));
	}

}
