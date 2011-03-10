package core;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.vecmath.Point2i;

public class PixelRay {

	private Point2i pos;
	private Color color;
	private AtomicBoolean rendered;

	public PixelRay(Point2i pos)
	{
		this.pos = new Point2i(pos);
		rendered = new AtomicBoolean(); 
	}

	public synchronized boolean isRendered(){
		return rendered.getAndSet(true);
	}

	public Point2i getPos() {
		return pos;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	
}
