package core;

import gui.LoggerWindowHandler;

import java.util.logging.Logger;

import javax.vecmath.Point2i;

import main.Main;



public class Settings {

	private static Point2i resolution = new Point2i(640, 480);
	public static int bins = 256;
	public static int contrastHigh = 150;
	public static int contrastLow = 50;
	public static int bucket = 512;
	public static BucketTypes bucketType = BucketTypes.ORDER;
	@SuppressWarnings("unused")
	private static Logger logger = LoggerWindowHandler.getLogger("SunflowSceneParser");
	public static String scenePath;


	public static boolean isImageLoaded() {
		return Main.image != null;
	}

	public enum BucketTypes {
		ROWS, COLS, AREA, ORDER
	}
	
	public static Point2i getResolution() {
		//logger.info("Resolution " + resolution );
		
		if(isImageLoaded()){
		//	logger.info("Image Resolution " + new Point2i(Main.getImage().getWidth(), Main.getImage().getHeight()) );
			
			return new Point2i(Main.getImage().getWidth(), Main.getImage().getHeight());
			
		}
		return resolution;
	}
	
	public static void setResolution(Point2i resolution) {
		//logger.info("Resolution " + resolution );
		
		Settings.resolution = resolution;
		Main.image = null;
		Main.createImage();
	}
}
