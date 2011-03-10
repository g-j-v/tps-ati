package core;

import gui.LoggerWindowHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.vecmath.Point2i;
import javax.vecmath.Point3f;

public class Camera {

	protected Point3f startingPoint;

	@SuppressWarnings("unused")
	private static  Logger logger = LoggerWindowHandler.getLogger("Camera");


	public enum ImageScan
	{
		COLS,
		ROWS
	}
	

	public PixelRay firePixelRay(int i, int j) throws Exception
	{
		if(i < 0 || i > Settings.getResolution().x)
			throw new Exception("i :"+i+" cant be below 0 or bigger than the x resolution of the image");
		if(j < 0 || j > Settings.getResolution().y)
			throw new Exception("j :"+j+" cant be below 0 or bigger than the y resolution");
		
		return new PixelRay(new Point2i(i,j));
		
	}
	
	public Collection<PixelRay> firePixelRays(Point2i from, Point2i to, ImageScan scan) throws Exception
	{
		Collection<PixelRay> pixelRays = new ArrayList<PixelRay>();
		if(scan == ImageScan.ROWS )
		{
			for(int j = from.y; j < to.y; j++ )
				for(int i = from.x; i < to.x; i++ )
				{
					
					pixelRays.add(firePixelRay(i,j));
				}
		}
		else
		{
			for(int i = from.x; i < to.x; i ++)
				for(int j = from.y; j < to.y; j ++)
					pixelRays.add(firePixelRay(i,j));
		}
		return pixelRays;
		
	}
	

	



}
