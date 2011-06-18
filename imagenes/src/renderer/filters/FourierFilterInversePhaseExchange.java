package renderer.filters;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

import core.PixelRay;
import core.Settings;
import fourier.ComplexNumber;
import fourier.FFT;
import fourier.InverseFFT;
import fourier.TwoDArray;

public class FourierFilterInversePhaseExchange extends Filter {

	protected BufferedImage auxImg;
	public static int black;
	public int orig[] = null;
	public int width, height;
	private FFT fft, fft2;
	private MemoryImageSource foutput;
	private BufferedImage finalimg;
	private InverseFFT inverse;

	//Agarro la phase de la primera y la pongo en la actual.
	public FourierFilterInversePhaseExchange() {
		if(Settings.secondaryImage == null)
		{
			throw new IllegalArgumentException("The second image is not loaded");
		}
		width = Math.min(oldImg.getWidth(), Settings.secondaryImage.getWidth());
		height = Math.min(oldImg.getHeight(), Settings.secondaryImage.getHeight());
		BufferedImage otherImg = Settings.secondaryImage;

		
		
		inverse = new InverseFFT();
		orig = new int[width * height];
		PixelGrabber grabber = new PixelGrabber(oldImg, 0, 0, width, height,
				orig, 0, width);
		try {
			grabber.grabPixels();
		} catch (InterruptedException e2) {
			System.out.println("error: " + e2);
		}
		int other[] = new int[width * height];
		 grabber = new PixelGrabber(otherImg, 0, 0, width, height,
				 other, 0, width);
		try {
			grabber.grabPixels();
		} catch (InterruptedException e2) {
			System.out.println("error: " + e2);
		}
		
		fft = new FFT(orig, width, height);
		fft2 = new FFT(other, width, height);

		TwoDArray input = fft.intermediate;
		TwoDArray intermediate = new TwoDArray(input.width, input.height);
		TwoDArray input2 = fft2.intermediate;
		
		for (int i = 0; i < input.size; ++i) {
			ComplexNumber [] col1 = input.getColumn(i);
			ComplexNumber [] col2 = input2.getColumn(i); 
			
			intermediate.putColumn(i, exchangePhase(col1, col2));
		}
		System.out.println(width + " * " + height);
		

		TwoDArray output = inverse.transform(intermediate);
		foutput = new MemoryImageSource(width, height,
				inverse.getPixels(output), 0, width);

		finalimg = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
		Image piximg = Toolkit.getDefaultToolkit().createImage(foutput);
		finalimg.getGraphics().drawImage(piximg, 0, 0, null);
	}

	public ComplexNumber[] exchangePhase(ComplexNumber[] x, ComplexNumber[] x2) {
		int n = x.length;
		ComplexNumber[] result = new ComplexNumber[n];

		for (int i = 0; i < n; ++i) {
	//		System.out.println("Magnitud " + x[i].magnitude() +" Angulo " + x2[i].phaseAngle());
			result[i] = ComplexNumber.ComplexNumberPolar(x[i].magnitude(),x2[i].phaseAngle());
	//		System.out.println("Magnitud " + result[i].magnitude() + " Angulo " + result[i].phaseAngle());
		}

		return result;
	}

	@Override
	public Color RenderPixel(PixelRay ray) {

		return new Color(finalimg.getRGB(ray.getPos().x, ray.getPos().y));
	}

}
