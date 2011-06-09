package renderer.filters;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import core.PixelRay;

import util.Matrix;

public class Canny4Filter extends Filter {

	Matrix matriz;
	int h = oldImg.getHeight();
	int w = oldImg.getWidth();
	int N = 5;
	int NLimit = 2;

	float tMax = (float) 0.30;
	float tmin = (float) 0.20;

	private BufferedImage suavizada = new BufferedImage(w, h, oldImg.getType());
	private Float[][] magnitud = new Float[w][h];
	private Float[][] direccion = new Float[w][h];
	private Integer[][] sectores = new Integer[w][h];
	private BufferedImage suprimida = new BufferedImage(w, h, oldImg.getType());
	private Float[][] bordes = new Float[w][h];
	private BufferedImage histerica = new BufferedImage(w, h, oldImg.getType());

	ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);

	public Canny4Filter() {

		matriz = Matrix.canny();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				magnitud[i][j] = (float) 0;
				direccion[i][j] = (float) 0;
				sectores[i][j] = 0;
				bordes[i][j] = (float) 0;
			}
		}

		matriz.show();
		// convoluciono con la gaussiana

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {

				float colors[] = new float[3];

				int pixel = oldImg.getRGB(i, j);

				if (i < N || i > (w - N) || j < N || j > (h - N)) {
					suavizada.setRGB(i, j, pixel);

				} else {

					double sumr = 0;
					double sumb = 0;
					double sumg = 0;
					for (int k = -NLimit; k <= NLimit; k++) {
						for (int l = -NLimit; l <= NLimit; l++) {
							int p = oldImg.getRGB(i + l, j + k);
							float red = (p >> 16) & 0xff;
							float green = (p >> 8) & 0xff;
							float blue = (p) & 0xff;

							// get the corresponding filter coefficient
							double c = matriz.getData(k + NLimit, l + NLimit);
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

					Color c = new Color(space, colors, 1);
					suavizada.setRGB(i, j, c.getRGB());

				}
			}
		}

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {

				if (i < 1 || i >= (w - 1) || j < 1 || j >= (h - 1)) {

					magnitud[i][j] = fetchColor(oldImg, i, j);

				} else {
					// utilizo derivadas como f'(x) = (f(x+1)-f(x-1))/2

					double up = fetchColor(oldImg, i, j - 1);
					double down = fetchColor(oldImg, i, j + 1);
					double left = fetchColor(oldImg, i - 1, j);
					double right = fetchColor(oldImg, i + 1, j);

					double gx = (double) (right - left) / 2;
					double gy = (double) (down - up) / 2;

					magnitud[i][j] = (float) (Math.abs(gx) + Math.abs(gy));
					direccion[i][j] = (float) (gx == 0 ? 0 : Math.atan(gy / gx));
					sectores[i][j] = calcularSector(direccion[i][j]);

				}
			}
		}

		// supresión de no máximos

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (i < 1 || i >= (w - 1) || j < 1 || j >= (h - 1)) {
					double res = magnitud[i][j];

					if (res > 1) {
						res = 1;
					}
					suprimida.setRGB(i, j, new Color((int) (res * 254),
							(int) (res * 254), (int) (res * 254)).getRGB());
				} else {

					suprimirNoMáximos(i, j);
				}
			}
		}

		// umbralización con histéresis
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (bordes[i][j] == 0 && fetchColor(suprimida, i, j) >= tMax) {
					followTrace(suprimida, i, j);
				}

			}
		}

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				float tmp = bordes[i][j];
				histerica.setRGB(i, j, new Color((float) tmp, (float) tmp,
						(float) tmp).getRGB());
			}
		}

	}

	private void followTrace(BufferedImage imagen, int i, int j) {
		int x0 = i == 0 ? i : i - 1;
		int x2 = i == w - 1 ? i : i + 1;
		int y0 = j == 0 ? j : j - 1;
		int y2 = j == h - 1 ? j : j + 1;

		bordes[i][j] = magnitud[i][j];
		for (int x = x0; x <= x2; x++) {
			for (int y = y0; y <= y2; y++) {
				if ((y != j || x != i) && bordes[x][y] == 0
						&& magnitud[x][y] >= tmin) {
					followTrace(imagen, x, y);
					return;
				}
			}
		}

	}

	private float fetchColor(BufferedImage img, int i, int j) {
		float colors[] = new float[3];

		int p = img.getRGB(i, j);
		float red = (p >> 16) & 0xff;
		float green = (p >> 8) & 0xff;
		float blue = (p) & 0xff;
		Color.RGBtoHSB((int) red, (int) green, (int) blue, colors);

		return colors[2];
	}

	private void suprimirNoMáximos(int i, int j) {
		float vecino1;
		float vecino2;

		float res;

		switch (sectores[i][j]) {
		case 0:
			// System.out.println(0);
			vecino1 = magnitud[i + 1][j];
			vecino2 = magnitud[i - 1][j];
			break;
		case 1:
			// System.out.println(1);
			vecino1 = magnitud[i + 1][j - 1];
			vecino2 = magnitud[i - 1][j + 1];
			break;
		case 2:
			// System.out.println(2);
			vecino1 = magnitud[i][j - 1];
			vecino2 = magnitud[i][j + 1];
			break;
		case 3:
			// System.out.println(3);
			vecino1 = magnitud[i - 1][j - 1];
			vecino2 = magnitud[i + 1][j + 1];
			break;
		default:
			vecino1 = (float) 0.0;
			vecino2 = (float) 0.0;
			break;
		}

		res = magnitud[i][j];
		if (res < vecino1 || res < vecino2) {
			res = (float) 0.0;
		}

		if (res > 1) {
			res = 1;
		}
		
		int[] colors = { (int) (res * 254), (int) (res * 254),
				(int) (res * 254) };
//		System.out.println( colors[0]);
		suprimida.setRGB(i, j, new Color(colors[0], colors[0], colors[0]).getRGB());
	}

	private Integer calcularSector(Float direccion2) {
		// Converting into degrees from radians, and moving to lie between 0 and
		// 360
		direccion2 = (float) Math.toDegrees(direccion2);
		direccion2 = direccion2 + 270;
		direccion2 = direccion2 % 360;

		if ((direccion2 >= 337.5) || (direccion2 < 22.5)
				|| ((direccion2 >= 157.5) && (direccion2 < 202.5))) {
			return 0;
		}
		if (((direccion2 >= 22.5) && (direccion2 < 67.5))
				|| ((direccion2 >= 202.5) && (direccion2 < 247.5))) {
			return 1;
		}
		if (((direccion2 >= 67.5) && (direccion2 < 112.5))
				|| ((direccion2 >= 247.5) && (direccion2 < 292.5))) {
			return 2;
		}
		if (((direccion2 >= 112.5) && (direccion2 < 157.5))
				|| ((direccion2 >= 292.5) && (direccion2 < 337.5))) {
			return 3;
		}
		return 0;

	}

	@Override
	public Color RenderPixel(PixelRay pixRay) {

		// System.out.println(suprimida.getRGB(pixRay.getPos().getX(), pixRay
		// .getPos().getY()));
		return new Color(histerica.getRGB(pixRay.getPos().getX(), pixRay
				.getPos().getY()));
	}

}
