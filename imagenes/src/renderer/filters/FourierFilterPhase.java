package renderer.filters;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

import core.PixelRay;
import fourier.FFT;
import fourier.InverseFFT;

public class FourierFilterPhase extends Filter {

	protected BufferedImage auxImg;
	public static int black;
	public int orig[] = null;
	public int width, height;
	private FFT fft;
	private MemoryImageSource foutput;
	private BufferedImage finalimg;
	private InverseFFT inverse;

	public FourierFilterPhase() {
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
		foutput = new MemoryImageSource(width, height, fft.getPixelsPhase(), 0, width);

		finalimg = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
		Image piximg = Toolkit.getDefaultToolkit().createImage(foutput);
		finalimg.getGraphics().drawImage(piximg, 0, 0, null);
	}

	@Override
	public Color RenderPixel(PixelRay ray) {

		return new Color(finalimg.getRGB(ray.getPos().x, ray.getPos().y));
	}

}
