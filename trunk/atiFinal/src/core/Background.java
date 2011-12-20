package core;

import java.awt.Point;
import java.awt.image.BufferedImage;

import util.HSVArray;

public class Background {
	
	HSVArray back;
	HSVArray current;
	HSVArray last;
	
	static int THRESHOLD = 100;
	static int COLORDIFF = 50;
	
	
	static int COLOR = 0;
	static int WIDTH = 10;
	static int HEIGHT = 10;
	public Contour contour;
	static Background background;
	private Background(BufferedImage image) {
	back = new HSVArray(image);
	last = back;
	}
	
	public static Background getBackground() {
		return background;
	}
	public static Background getBackground(BufferedImage image) {
		Contour.MAXX = image.getWidth();
		Contour.MAXY = image.getHeight();
		background = new Background(image);
		WIDTH = image.getWidth();
		HEIGHT = image.getHeight();
		return background;
		
	}
	
	public boolean isImage(int x, int y) {
		return Math.abs(back.getColor(x, y) - current.getColor(x, y)) > THRESHOLD  /*&& Math.abs(current.getColor(x, y) - COLOR) < COLORDIFF*/;
	}
	
	public boolean isBackground(int x, int y) {
		return Math.abs(back.getColor(x, y) - current.getColor(x, y)) < THRESHOLD  /*&& Math.abs(current.getColor(x, y) - COLOR) > COLORDIFF*/; 
	}
	
	
	public Contour getInitialContour()
	{
		contour = new Contour(this);
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
		
		//contour.cicle1();
		return contour;
	}

	public HSVArray getCurrent() {
		return current;
	}

	public void setCurrent(HSVArray current) {
		last = current;
		this.current = current;
	}

	public void setColor(int x, int y) {
		COLOR = current.getColor(x, y);
	}

	public boolean changed(int x, int y) {
		return Math.abs(last.getColor(x, y) - current.getColor(x, y)) > THRESHOLD; 
	}

	
	
	
}
