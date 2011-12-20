package core;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
		return Math.abs(back.getColor(x, y) - current.getColor(x, y)) > THRESHOLD  && Math.abs(current.getColor(x, y) - COLOR) < COLORDIFF;
	}
	
	public boolean isBackground(int x, int y) {
		return Math.abs(back.getColor(x, y) - current.getColor(x, y)) < THRESHOLD  || Math.abs(current.getColor(x, y) - COLOR) > COLORDIFF; 
	}
	
	
	public Contour getInitialContour()
	{
		int[][] aux = new int[WIDTH][HEIGHT];
		for(int i = 0; i < WIDTH; i++){
			for(int j = 0; j < HEIGHT; j++){
				aux[i][j] = 0;
			}
		}
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
					aux[i][j] = 255;
					if(isBackground(i + 1, j) || isBackground(i - 1, j) || isBackground(i, j + 1) || isBackground(i, j - 1))
					{
						contour.Lin.add(new Point(i, j));
					}
				}
	
			}
		}
		
		int intValues[][] = new int[WIDTH][HEIGHT];

		for(int i=1; i < WIDTH - 1 ; i++ ){
			for(int j=1; j < HEIGHT - 1; j++ ){
				int[][] mask = new int[3][3];
				for (int x = i -1, maskx = 0; x < i - 1 + mask.length; x++, maskx++) {
					for (int y = j-1, masky = 0; y < j - 1 + mask[0].length; y++, masky++) {
						//System.out.println("(" + x + "," + y +")");
						mask[maskx][masky] = aux[x][y];
					}
				}
				int value = calculateValue(mask);
				intValues[i][j] = value;
			}
		}
		

		ArrayList<Point> linCopy = new ArrayList<Point>();
		for (Point obj : contour.Lin) {
			linCopy.add((Point) obj.clone());
		}
		for (Point point : linCopy) {
			if ((intValues[point.x][point.y] < 50)) {
				contour.Lin.remove(point);
			}
		}
		
		ArrayList<Point> loutCpy = new ArrayList<Point>();
		for (Point obj : contour.Lout) {
			loutCpy.add((Point) obj.clone());
		}
		for (Point point : loutCpy) {
			if ((intValues[point.x][point.y] < 50)) {
				contour.Lout.remove(point);
			}
		}


		return contour;
	}

	protected static int calculateValue(int[][] mask) {
			int ans = 0;
			for (int i = 0; i < mask.length; i++) {
				for (int j = 0; j < mask[i].length; j++) {
					ans += mask[i][j];
				}
			}
			return (int) Math.floor(ans * 1/9);
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
