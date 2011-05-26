package renderer.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.vecmath.Point2i;

import core.PixelRay;

public class CannyFilter extends Filter {

	@Override
	public Color RenderPixel(PixelRay pixRay) {
		// TODO Auto-generated method stub
		return null;
	}

/*	public static float filterSigma = 2;

	private GaussianFilter gf;
	private int w = oldImg.getWidth();
	private int h = oldImg.getHeight();
	private BufferedImage newImg = new BufferedImage(w, h, oldImg.getType());

	int[][] p_2d = new int[w][h];
	int[][] q_2d = new int[w][h];
	int[][] m_2d = new int[w][h];
	double[][] theta_2d = new double[w][h];
	int[][] tmp = new int[w][h];
	int[][] sector = new int[w][h];
	int[][] nms = new int[w][h];

	public CannyFilter() {

		gf = new GaussianFilter(filterSigma);

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color c = gf.RenderPixel(new PixelRay(new Point2i(i, j)));
				newImg.setRGB(i, j, c.getRGB());
			}
		}

		// aproximaciones de las derivadas
		for (int i = 0; i < (w - 1); i++) {
			for (int j = 0; j < (h - 1); j++) {
				p_2d[i][j] = (tmp[i][j + 1] - tmp[i][j] + tmp[i + 1][j + 1] - tmp[i + 1][j]) / 2;
				q_2d[i][j] = (tmp[i][j] - tmp[i + 1][j] + tmp[i][j + 1] - tmp[i + 1][j + 1]) / 2;
				m_2d[i][j] = (int) Math.sqrt(Math.pow((double) p_2d[i][j], 2)
						+ Math.pow((double) q_2d[i][j], 2));
				theta_2d[i][j] = Math.atan2((double) (q_2d[i][j]),
						(double) (p_2d[i][j]));
			}
		}

		// Resize image
		w--;
		h--;

		// Apply the nonmaxima suppression

		// First calculate which sector each line appears in

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				sector[i][j] = sector(theta_2d[i][j]);
			}
		}

		// Then apply non maximal suppression
		for (int i = 0; i < (w - 1); i++) {
			nms[i][0] = 0;
			nms[i][h - 1] = 0;
		}
		for (int j = 0; j < (h - 1); j++) {
			nms[0][j] = 0;
			nms[w - 1][j] = 0;
		}
		for (int i = 1; i < (w - 1); i++) {
			for (int j = 1; j < (h - 1); j++) {
				nms[i][j] = suppress(m_2d, sector[i][j], i, j);
			}
		}

		// Resize again!
		w = w - 2;
		h = h - 2;

		// Track the image
		tracked = apply_track(nms, w, h, lowthresh, highthresh);

		// Calculate the output array
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				result = tracked[i][j];
				result = (int) (result * scale);
				result = result + offset;
				if (result > 255) {
					result = 255;
				}
				if (result < 0) {
					result = 0;
				}
				dest_1d[(i + (j * (w + 3)))] = 0xff000000 | result << 16
						| result << 8 | result;
			}
		}

		// Change the sizes back
		w = w + 3;
		h = h + 3;

		return dest_1d;

	}

	@Override
	protected Color RenderPixel(PixelRay pixRay) {
		// TODO Auto-generated method stub
		return null;
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

	// Function to apply non maxima suppression to the image array
	private int suppress(int[][] m_2d, int sector, int i, int j) {

		int tmp = m_2d[i][j];

		// if (318 < i && i < 322 && 113 < j && j <
		// 117)System.out.println("ij("+i+","+j+") sector: "+sector+" neigh: "+m_2d[i-1][j-1]+" "+m_2d[i-1][j]+" "+m_2d[i-1][j+1]+" "+m_2d[i][j-1]+" "+m_2d[i][j]+" "+m_2d[i][j+1]+" "+m_2d[i+1][j-1]+" "+m_2d[i+1][j]+" "+m_2d[i+1][j+1]);

		if (sector == 0) {
			if ((m_2d[i + 1][j] >= tmp) || (m_2d[i - 1][j] > tmp)) {
				return 0;
			} else {
				return tmp;
			}
		}
		if (sector == 1) {
			if ((m_2d[i + 1][j + 1] >= tmp) || (m_2d[i - 1][j - 1] > tmp)) {
				return 0;
			} else {
				return tmp;
			}
		}
		if (sector == 2) {
			if ((m_2d[i][j + 1] >= tmp) || (m_2d[i][j - 1] > tmp)) {
				return 0;
			} else {
				return tmp;
			}
		}
		if (sector == 3) {
			if ((m_2d[i + 1][j - 1] >= tmp) || (m_2d[i - 1][j + 1] > tmp)) {
				return 0;
			} else {
				return tmp;
			}
		}
		System.out.println("Canny - Unidentified sector " + sector + " at ij: "
				+ i + " " + j);
		return 0;
	}
	*/
}
