package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

import util.Matrix;

import core.PixelRay;

public class LaplacianBorderDetectorFilter extends Filter {

	public static Matrix mat;

	int NLimit = 1;

	int x = 0;
	int y = 0;

	BufferedImage newImg;
	// float[][] newImg;
	Boolean[][] borders;

	public LaplacianBorderDetectorFilter() {

		System.err.println("Entered LLLLLLLap");
		mat = Matrix.laplaciana();
		mat.show();

		x = oldImg.getWidth();
		y = oldImg.getHeight();

		System.err.println("x es igual a" + x);
		System.err.println("y es igual a" + y);

		newImg = new BufferedImage(x, y, oldImg.getType());
		// newImg = new float[x][y];

		borders = new Boolean[x][y];

		System.err.println("La matriz borders tiene " + borders.length
				+ "filas y " + borders[0].length + "columnas");
		applyLaplaciano();
	}

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);

	@Override
	public Color RenderPixel(PixelRay ray) {
		int x1 = ray.getPos().getX();
		int y1 = ray.getPos().getY();

		if (x1 == 0 || y1 == 0 || x1 == x - 1 || y1 == y - 1) {
			return Color.black;
		} else {
			boolean esborde = borders[x1][y1];
			if (esborde) {
				return Color.black;
			} else {
				return Color.white;
			}
		}
		// return new Color(newImg.getRGB(x1, y1));
	}

	private void applyLaplaciano() {

		float colors[] = new float[3];

		for (int k = 1; k < x - 1; k++) {
			newImg.setRGB(k, 0, oldImg.getRGB(k, 0));
			newImg.setRGB(k, y - 1, oldImg.getRGB(k, y - 1));
			// newImg[k][0] = 0;
			// newImg[k][y-1] = 0;
			for (int l = 1; l < y - 1; l++) {
				newImg.setRGB(0, l, oldImg.getRGB(0, l));
				newImg.setRGB(x - 1, l, oldImg.getRGB(x - 1, l));
				// newImg[0][l] = 0;
				// newImg[x-1][l] = 0;
				double sumr = 0;
				double sumb = 0;
				double sumg = 0;
				for (int j = -NLimit; j <= NLimit; j++) {
					for (int i = -NLimit; i <= NLimit; i++) {
						int p = oldImg.getRGB(k + i, l + j);
						float red = (p >> 16) & 0xff;
						float green = (p >> 8) & 0xff;
						float blue = (p) & 0xff;

						// get the corresponding filter coefficient
						double c = mat.getData(j + NLimit, i + NLimit);
						sumr = sumr + c * red;
						sumg = sumg + c * green;
						sumb = sumb + c * blue;

					}
				}

				colors[0] = (float) sumr / 255;
				colors[1] = (float) sumg / 255;
				colors[2] = (float) sumb / 255;

				if (colors[0] > 1)
					colors[0] = 1;
				if (colors[0] < 0)
					colors[0] = 0;
				if (colors[1] > 1)
					colors[1] = 1;
				if (colors[1] < 0)
					colors[1] = 0;
				if (colors[2] > 1)
					colors[2] = 1;
				if (colors[2] < 0)
					colors[2] = 0;

				newImg.setRGB(k, l, new Color(space, colors, 1).getRGB());
				// newImg[k][l]=colors[2];

				System.out.println(colors[2]);

				// borders[k][l] = (Math.signum(newImg.getRGB(k, l)
				// - newImg.getRGB(k - 1, l)) < 0 || Math.signum(-newImg
				// .getRGB(k, l)
				// + newImg.getRGB(k - 1, l)) < 0)
				// && (Math.signum(newImg.getRGB(k, l)
				// + newImg.getRGB(k, l - 1)) < 0 || Math
				// .signum(-newImg.getRGB(k, l)
				// + newImg.getRGB(k, l - 1)) < 0);
				borders[k][l] = Math.signum(newImg.getRGB(k, l)
						* newImg.getRGB(k - 1, l)) < 0
						|| Math.signum(newImg.getRGB(k, l)
								* newImg.getRGB(k, l - 1)) < 0;
				// borders[k][l] = Math.signum(newImg[k][l]*newImg[k-1][l]) < 0
				// || Math.signum(newImg[k][l]*newImg[k][l-1])<0;
			}
		}

	}
}
