package renderer.filters;

import gui.histogram.HistPanel;
import gui.histogram.Histogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import core.PixelRay;

public class OtzuUmbralFilter extends Filter {
	Histogram histogram;
	HistPanel panel;
	double smin;
	int h = oldImg.getHeight();
	int w = oldImg.getWidth();
	float alphaT = 0.1f;
	private int[][] data;
	private Color[][] aux;
	private float N;
	private float[] probability;
	private int bins = 256;
	public OtzuUmbralFilter() {
		N = w* h;
		probability = new float[256];
		data = new int[w][h];
		aux = new Color[w][h];
		JFrame frame = new JFrame("Histogram");
		histogram = new Histogram("Frequency", "Grey level", bins, 0,
				255);
		panel = new HistPanel(histogram);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		float colors[] = new float[3];

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int pixel = oldImg.getRGB(i, j);
				float red = (pixel >> 16) & 0xff;
				float green = (pixel >> 8) & 0xff;
				float blue = (pixel) & 0xff;
				Color.RGBtoHSB((int) red, (int) green, (int) blue, colors);// an
				histogram.add(colors[2] * 255);
				data[i][j] = (int) (colors[2] * 255);

			}
		}
		for(int i = 0; i < bins; i++)
		{
			probability[i] = histogram.getBins()[i]/ N;
		}
		
		float G1;
		float G2;
		float sumG1;
		float sumG2;
		float m1;
		float m2;
		float Tlast = 1;
		float Tactual = 127;
		float deltaT = Math.abs(Tactual - Tlast);
		while (deltaT > alphaT) {
			G1 = 0;
			G2 = 0;
			sumG1 = 0;
			sumG2 = 0;
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					if (data[i][j] < Tactual) {
						aux[i][j] = Color.BLACK;
						G1++;
						sumG1 += data[i][j];
					} else {
						G2++;
						aux[i][j] = Color.WHITE;
						sumG2 += data[i][j];
					}
				}
			}
			m1 = 1 / G1 * sumG1;
			m2 = 1 / G2 * sumG2;
			Tactual = (m1 + m2) / 2;
			deltaT = Math.abs(Tactual - Tlast);
			Tlast = Tactual;
			System.out.println(Tactual);
		}
	}


	@Override
	public Color RenderPixel(PixelRay ray) {
		return aux[ray.getPos().x][ray.getPos().y];

	}

}
