package core;

import java.awt.Point;
import java.awt.image.BufferedImage;

import util.HSVArray;

public class Background {
	
	HSVArray back;
	HSVArray current;
	static int TOLERANCE = 10;
	static int WIDTH = 10;
	static int HEIGHT = 10;
	
	static Background background;
	private Background(BufferedImage image) {
	back = new HSVArray(image);
	}
	
	public static Background getBackground() {
		return background;
	}
	public static Background getBackground(BufferedImage image) {
		background = new Background(image);
		WIDTH = image.getWidth();
		HEIGHT = image.getHeight();
		return background;
		
	}
	
	private boolean isImage(int x, int y) {
		return back.getColor(x, y) - current.getColor(x, y) > TOLERANCE;
	}
	
	private boolean isBackground(int x, int y) {
		return back.getColor(x, y) - current.getColor(x, y) < TOLERANCE; 
	}
	
	
	public Contour getInitialContour()
	{
		Contour contour = new Contour();
		for (int i = 1; i < WIDTH -1; i++) {
			for (int j = 1; j < HEIGHT-1; j++) {
				if(isBackground(i, j))
				{
					if(isImage(i + 1, j) || isImage(i - 1, j) || isImage(i, j + 1) || isImage(i, j - 1))
					{
						contour.Lout.add(new Point(i, j));
					}
				}
				if(isImage(i, j))
				{
					if(isBackground(i + 1, j) || isBackground(i - 1, j) || isBackground(i, j + 1) || isBackground(i, j - 1))
					{
						contour.Lin.add(new Point(i, j));
					}
				}
	
			}
		}
		
		
		return contour;
	}

	public HSVArray getCurrent() {
		return current;
	}

	public void setCurrent(HSVArray current) {
		this.current = current;
	}

	
	
	
}
