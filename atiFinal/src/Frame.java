import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 */

/**
 * @author Dani
 *
 */

/**
 * ￼￼Algoritmo 3 Contraer el contorno en el pixel x ￼1: 2: 3: 4: 5: 6: 7:
 * eliminar x de Lin agregar x a Lout establecerφ(x)=1 forally∈N4(x)/φ(y)=−3do
 * agregar y a Lin establecer φ (y) = −1 end for
 */
public class Frame {

	public ArrayList<Point> Lin = new ArrayList<Point>();
	public ArrayList<Point> Lout = new ArrayList<Point>();
	public HashMap<Point, Boolean> process = new HashMap<Point, Boolean>();
	public BufferedImage image;
	HSVArray brightness;
	public int color;
	public int tolerance = 10;
	public boolean cambio = true;
	public int ITERACIONES = 10;
	
	
	public Frame(HSVArray image, int x, int y) {
		super();
		this.brightness = image;
		this.color = brightness.getColor(x, y);
		Lin.add(new Point(x,y));

		Lout.add(new Point(x-1,y));
		Lout.add(new Point(x+1,y));
		Lout.add(new Point(x,y-1));
		Lout.add(new Point(x,y+1));

		
	}

	
	public void cicle1(){
		int counter =0 ;
		cambio = true;
		while(counter < ITERACIONES && cambio)
		{
			cambio = false;
			counter++;
			expand();
			contract();
					
		}
	}
	
	
	/**
	 * Algoritmo 2 Expandir el contorno en el pixel x ￼1: 2: 3: 4: 5: 6: 7:
	 * eliminar x de Lout agregar x a Lin establecerφ(x)=−1
	 * forally∈N4(x)/φ(y)=3do agregar y a Lout establecer φ (y) = 1 end for
	 */
	
	public void expand() {
		ArrayList<Point> loutCopy = new ArrayList<Point>();
		for (Point obj : Lout)
			loutCopy.add((Point) obj.clone());

		for (Point point : loutCopy) {
			if (isImage(point)) {
				cambio = true;
				Lout.remove(point);
				Lin.add(point);
				Point aux = new Point(point.x - 1, point.y);
				if (isBackground(aux))
					Lout.add(aux);
				aux = new Point(point.x + 1, point.y);
				if (isBackground(aux))
					Lout.add(aux);
				aux = new Point(point.x, point.y);
				if (isBackground(aux))
					Lout.add(aux);
				aux = new Point(point.x - 1, point.y);
				if (isBackground(aux))
					Lout.add(aux);

			}

		}

		loutCopy = new ArrayList<Point>();
		for (Point obj : Lin){
			loutCopy.add((Point) obj.clone());	
		}
		for (Point point : loutCopy) {
			if (! isLin(point)) {
				Lin.remove(point);
			}
		}
		
		
	}

	public void contract() {
		ArrayList<Point> linCopy = new ArrayList<Point>();
		for (Point obj : Lin)
			linCopy.add((Point) obj.clone());

		for (Point point : linCopy) {
			if (isBackground(point)) {
				cambio = true;
				
				Lin.remove(point);
				Lout.add(point);
				Point aux = new Point(point.x - 1, point.y);
				if (isImage(aux))
					Lin.add(aux);
				aux = new Point(point.x + 1, point.y);
				if (isImage(aux))
					Lin.add(aux);
				aux = new Point(point.x, point.y);
				if (isImage(aux))
					Lin.add(aux);
				aux = new Point(point.x - 1, point.y);
				if (isImage(aux))
					Lin.add(aux);

			}

		}
		
		linCopy = new ArrayList<Point>();
		for (Point obj : Lout){
			linCopy.add((Point) obj.clone());	
		}
		for (Point point : linCopy) {
			if (! isLout(point)) {
				Lout.remove(point);
			}
		}


	}

	/**
	 * Lout es un pixel del fondo que toca a alguno de la imagen.
	 * 
	 * Lin es un pixel de la region que toca a uno del fondo.
	 */
	private boolean isImage(Point point) {
		if (Math.abs(color - brightness.getColor(point.x, point.y)) < tolerance)
			return true;
		return false;
	}

	private boolean isBackground(Point point) {
		if (Math.abs(color - brightness.getColor(point.x, point.y)) > tolerance)
			return true;
		return false;
	}

	
	private boolean isLout(Point point) {
		if(isImage(point))
			return false;
		int count = 0;
		Point aux = new Point(point.x - 1, point.y);
		if (isImage(aux))
			count++;
		aux = new Point(point.x + 1, point.y);
		if (isImage(aux))
			count++;
		aux = new Point(point.x, point.y);
		if (isImage(aux))
			count++;
		aux = new Point(point.x - 1, point.y);
		if (isImage(aux))
			count++;

		if(count == 0)
			return false;
		return true;
	}

	
	private boolean isLin(Point point) {
		if(isBackground(point))
			return false;
		int count = 0;
		Point aux = new Point(point.x - 1, point.y);
		if (isBackground(aux))
			count++;
		aux = new Point(point.x + 1, point.y);
		if (isBackground(aux))
			count++;
		aux = new Point(point.x, point.y);
		if (isBackground(aux))
			count++;
		aux = new Point(point.x - 1, point.y);
		if (isBackground(aux))
			count++;

		if(count == 0)
			return false;
		return true;
	}
}
