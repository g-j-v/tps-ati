import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import util.HSVArray;

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
	public ArrayList<Point> region = new ArrayList<Point>();

	public HashMap<Point, Boolean> process = new HashMap<Point, Boolean>();
	public HashMap<Point, Boolean> processExpand = new HashMap<Point, Boolean>();
	public HashMap<Point, Boolean> processContract = new HashMap<Point, Boolean>();
	public BufferedImage image;
	HSVArray brightness;
	public int color;
	public int tolerance = 5;
	public boolean cambio = true;
	public int ITERACIONES = 1000;
	public int MAXX, MAXY;

	public Frame(HSVArray image, int x, int y, int color) {
		super();
		this.brightness = image;
		MAXX = brightness.width;
		MAXY = brightness.height;

		this.color = color;
		Lin.add(new Point(x, y));

		Lout.add(new Point(x - 1, y));
		Lout.add(new Point(x + 1, y));
		Lout.add(new Point(x, y - 1));
		Lout.add(new Point(x, y + 1));

	}

	public void cicle1() {
		int counter = 0;
		cambio = true;
		while (counter < ITERACIONES && cambio) {
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
			if (processExpand.get(point) == null) {
				processExpand.put(point, true);
				if (isImage(point)) {
					cambio = true;
					Lout.remove(point);
					Lin.add(point);
					Point aux = new Point(point.x - 1, point.y);
					if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX
							&& aux.y < MAXY && processExpand.get(aux) == null)
						Lout.add(aux);
					aux = new Point(point.x + 1, point.y);
					if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX
							&& aux.y < MAXY && processExpand.get(aux) == null)
						Lout.add(aux);
					aux = new Point(point.x, point.y + 1);
					if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX
							&& aux.y < MAXY && processExpand.get(aux) == null)
						Lout.add(aux);
					aux = new Point(point.x, point.y - 1);
					if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX
							&& aux.y < MAXY && processExpand.get(aux) == null)
						Lout.add(aux);

				}

			}

		}
		/*
		 * loutCopy = new ArrayList<Point>(); for (Point obj : Lin){
		 * loutCopy.add((Point) obj.clone()); } for (Point point : loutCopy) {
		 * if (! isLin(point)) { Lin.remove(point); } }
		 */

	}

	public void contract() {
		ArrayList<Point> linCopy = new ArrayList<Point>();
		for (Point obj : Lin)
			linCopy.add((Point) obj.clone());

		for (Point point : linCopy) {
			if (processContract.get(point) == null) {
				processContract.put(point, true);

				if (isImage(point)) {
					cambio = true;
					if (isBackground(point)) {
						Lin.remove(point);
						Lout.add(point);
					}
					Point aux = new Point(point.x - 1, point.y);
					if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX
							&& aux.y < MAXY && isImage(aux)
							&& processContract.get(point) == null)
						Lin.add(aux);
					aux = new Point(point.x + 1, point.y);
					if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX
							&& aux.y < MAXY && isImage(aux)
							&& processContract.get(point) == null)
						Lin.add(aux);
					aux = new Point(point.x, point.y + 1);
					if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX
							&& aux.y < MAXY && isImage(aux)
							&& processContract.get(point) == null)
						Lin.add(aux);
					aux = new Point(point.x, point.y - 1);
					if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX
							&& aux.y < MAXY && isImage(aux)
							&& processContract.get(point) == null)
						Lin.add(aux);

				}
			}

		}
		/*
		 * linCopy = new ArrayList<Point>(); for (Point obj : Lout){
		 * linCopy.add((Point) obj.clone()); } for (Point point : linCopy) { if
		 * (! isLout(point)) { Lout.remove(point); } }
		 */
	}

	public void cleanLin() {
		ArrayList<Point> linCopy = new ArrayList<Point>();
		for (Point obj : Lin) {
			linCopy.add((Point) obj.clone());
		}
		for (Point point : linCopy) {
			if (!isLin(point)) {
				Lin.remove(point);
			}
		}

	}

	public void cleanLout() {
		ArrayList<Point> linCopy = new ArrayList<Point>();
		for (Point obj : Lout) {
			linCopy.add((Point) obj.clone());
		}
		for (Point point : linCopy) {
			if (!isLout(point)) {
				Lout.remove(point);
			}
		}

	}

	public void clean() {
		cleanLin();
		cleanLout();
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
		if (isImage(point))
			return false;
		int count = 0;
		Point aux = new Point(point.x - 1, point.y);
		if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX && aux.y < MAXY
				&& isImage(aux))
			count++;
		aux = new Point(point.x + 1, point.y);
		if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX && aux.y < MAXY
				&& isImage(aux))
			count++;
		aux = new Point(point.x, point.y - 1);
		if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX && aux.y < MAXY
				&& isImage(aux))
			count++;
		aux = new Point(point.x, point.y + 1);
		if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX && aux.y < MAXY
				&& isImage(aux))
			count++;

		if (count == 0)
			return false;
		return true;
	}

	private boolean isLin(Point point) {
		if (isBackground(point))
			return false;
		int count = 0;
		Point aux = new Point(point.x - 1, point.y);

		if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX && aux.y < MAXY
				&& isBackground(aux))
			count++;
		aux = new Point(point.x + 1, point.y);
		if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX && aux.y < MAXY
				&& isBackground(aux))
			count++;
		aux = new Point(point.x, point.y + 1);
		if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX && aux.y < MAXY
				&& isBackground(aux))
			count++;
		aux = new Point(point.x, point.y - 1);
		if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX && aux.y < MAXY
				&& isBackground(aux))
			count++;

		if (count == 0)
			return false;
		return true;
	}

	public void floodFill(Point point) {
		System.out.println("x " + point.x + " y " + point.y + " color "
				+ brightness.getColor(point.x, point.y));
		if (process.get(point) == null) {
			process.put(point, true);
			if (isImage(point)) {
				region.add(point);
				Point aux = new Point(point.x - 1, point.y);
				if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX && aux.y < MAXY) {
					if (isBackground(aux))
						Lin.add(point);
					floodFill(aux);
				}
				aux = new Point(point.x + 1, point.y);
				if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX && aux.y < MAXY) {
					if (isBackground(aux))
						Lin.add(point);
					floodFill(aux);
				}
				aux = new Point(point.x, point.y - 1);
				if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX && aux.y < MAXY) {
					if (isBackground(aux))
						Lin.add(point);
					floodFill(aux);
				}
				aux = new Point(point.x, point.y + 1);
				if (aux.x >= 0 && aux.y >= 0 && aux.x < MAXX && aux.y < MAXY) {
					if (isBackground(aux))
						Lin.add(point);
					floodFill(aux);
				}
			} else {
				Lout.add(point);
			}
		}

	}

	public Point getMidPoint() {
		int x = 0, y = 0, n = 0;

		for (Point point : Lin) {
			x += point.x;
			y += point.y;
			n++;
		}
		if (n != 0) {
			x = x / n;
			y = y / n;
		} else {
			x = 0;
			y = 0;
		}
		return new Point(x, y);
	}
}
