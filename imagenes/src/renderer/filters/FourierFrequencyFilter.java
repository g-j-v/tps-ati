package renderer.filters;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.Arrays;

import util.Matrix;
import core.PixelRay;
import fourier.ComplexNumber;
import fourier.FFT;
import fourier.InverseFFT;
import fourier.TwoDArray;

public class FourierFrequencyFilter extends Filter {

	public static int d0 = 3;
	public static Matrix filter;

	protected BufferedImage auxImg;
	public static int black;
	public int orig[] = null;
	public TwoDArray midway = null;
	public int width, height;
	private FFT fft;
	private InverseFFT invfft;
	private MemoryImageSource foutput;
	private MemoryImageSource partialOutput;
	private BufferedImage finalimg;

	public FourierFrequencyFilter() {
		height = oldImg.getHeight();
		width = oldImg.getWidth();

		orig = new int[width * height];

		filter.show();

		PixelGrabber grabber = new PixelGrabber(oldImg, 0, 0, width, height,
				orig, 0, width);
		try {
			grabber.grabPixels();
		} catch (InterruptedException e2) {
			System.out.println("error: " + e2);
		}

		fft = new FFT(orig, width, height);

		int i2, j2;

		for (int j = 0; j < width; ++j) {
			for (int i = 0; i < height; ++i) {
				if (i >= height / 2)
					i2 = i - height;
				else
					i2 = i;
				if (j >= width / 2)
					j2 = j - width;
				else
					j2 = j;

				if (Math.abs(i2) <= filter.getHeight() / 2
						&& Math.abs(j2) <= filter.getWidth() / 2) {
					double mult = filter.getData(i2 + filter.getHeight() / 2,
							j2 + filter.getWidth() / 2);
					fft.output.values[i][j] = new ComplexNumber(
							fft.output.values[i][j].real * mult,
							fft.output.values[i][j].imaginary * mult);
				} else
					fft.output.values[i][j] = new ComplexNumber(0, 0);
			}
		}

		invfft = new InverseFFT();
		TwoDArray answer = invfft.transform(fft.output);

		foutput = new MemoryImageSource(width, height,
				invfft.getPixels(answer), 0, width);

		finalimg = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
		Image piximg = Toolkit.getDefaultToolkit().createImage(foutput);
		finalimg.getGraphics().drawImage(piximg, 0, 0, null);
	}

	@Override
	public Color RenderPixel(PixelRay ray) {

		return new Color(finalimg.getRGB(ray.getPos().x, ray.getPos().y));
	}

}
