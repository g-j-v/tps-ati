package core;

import gui.LoggerWindowHandler;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.vecmath.Point2i;

import main.Main;



public class Settings {

	private static Point2i resolution = new Point2i(640, 480);
	public static int bins = 256;
	public static int contrastHigh = 150;
	public static int contrastLow = 50;
	public static int contrastS2 = 150;
	public static int contrastS1 = 50;
	public static int bucket = 128;
	public static double alpha = 0.5f;
	public static BucketTypes bucketType = BucketTypes.ORDER;
	@SuppressWarnings("unused")
	private static Logger logger = LoggerWindowHandler.getLogger("SunflowSceneParser");
	public static String scenePath;
	public static BufferedImage secondaryImage;
	public static BufferedImage auxImage;
	public static float umbral = 127;


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
