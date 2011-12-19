package core;
import java.awt.Point;
import java.util.ArrayList;


public class Contour {
	
	public ArrayList<Point> Lin;
	public ArrayList<Point> Lout;

	public Contour() {
		Lin = new ArrayList<Point>();
		Lout = new ArrayList<Point>();
	}

	public Contour(ArrayList<Point> lin, ArrayList<Point> lout) {
		super();
		Lin = lin;
		Lout = lout;
	}
	
	
	
}
