package core;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;


public class Contour {
	
	public ArrayList<Point> Lin;
	public ArrayList<Point> Lout;
	Background background;
	
	public boolean cambio = true;
	public static int ITERACIONES = 20;
	public static int MAXX, MAXY;
	public HashMap<Point, Boolean> processExpand = new HashMap<Point, Boolean>();
	public HashMap<Point, Boolean> processContract = new HashMap<Point, Boolean>();
	public ArrayList<Point> cola;
	
	public Contour(Background background) {
		Lin = new ArrayList<Point>();
		Lout = new ArrayList<Point>();
		this.background = background;
	}

	public Contour(ArrayList<Point> lin, ArrayList<Point> lout) {
		super();
		Lin = lin;
		Lout = lout;
	}
	
	
	
	private boolean isLout(Point point) {
		if (background.isImage(point.x, point.y))
			return false;
		int count = 0;
		Point aux = new Point(point.x - 1, point.y);
		if (background.isImage(aux.x, aux.y))
			count++;
		aux = new Point(point.x + 1, point.y);
		if (background.isImage(aux.x, aux.y))
			count++;
		aux = new Point(point.x, point.y - 1);
		if (background.isImage(aux.x, aux.y))
			count++;
		aux = new Point(point.x, point.y + 1);
		if (background.isImage(aux.x, aux.y))
			count++;

		if (count == 0)
			return false;
		return true;
	}

	private boolean isLin(Point point) {
		if (background.isBackground(point.x, point.y))
			return false;
		int count = 0;
		Point aux = new Point(point.x - 1, point.y);

		if (background.isBackground(aux.x, aux.y))
						count++;
		aux = new Point(point.x + 1, point.y);
		if (background.isBackground(aux.x, aux.y))
				count++;
		aux = new Point(point.x, point.y + 1);
		if (background.isBackground(aux.x, aux.y))
				count++;
		aux = new Point(point.x, point.y - 1);
		if (background.isBackground(aux.x, aux.y))
			count++;

		if (count == 0)
			return false;
		return true;
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
	
	
	
	public void cicle1() {
		int counter = 0;
		processExpand = new HashMap<Point, Boolean>();
		processContract = new HashMap<Point, Boolean>();
		cola = new ArrayList<Point>();
		cambio = true;
		while (counter < ITERACIONES && cambio) {
			cambio = false;
			counter++;
			expand();
		//	contract();

		}
		clean();
	}

	/**
	 * Algoritmo 2 Expandir el contorno en el pixel x ￼1: 2: 3: 4: 5: 6: 7:
	 * eliminar x de Lout agregar x a Lin establecerφ(x)=−1
	 * forally∈N4(x)/φ(y)=3do agregar y a Lout establecer φ (y) = 1 end for
	 */

	public void expand() {
		ArrayList<Point> loutCopy = new ArrayList<Point>();
		for (Point obj : Lout)
			if (processExpand.get(obj) == null) {
				processExpand.put(obj, true);
				loutCopy.add((Point) obj.clone());
				
		}
		

		int counter = 0;
		for (Point point : loutCopy) {
				if (!isLout(point) /*&& background.changed(point.x, point.y)*/) {
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
				}else{
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
				counter++;

		}
		System.out.println(counter);
		
		/*
		 * loutCopy = new ArrayList<Point>(); for (Point obj : Lin){
		 * loutCopy.add((Point) obj.clone()); } for (Point point : loutCopy) {
		 * if (! isLin(point)) { Lin.remove(point); } }
		 */

	}
/*
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
