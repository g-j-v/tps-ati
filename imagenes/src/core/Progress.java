package core;

import gui.Frame;
import gui.LoggerWindowHandler;

import java.util.logging.Logger;

import main.Main;


public class Progress {

	
	private static  Logger logger = LoggerWindowHandler.getLogger("ProgressThread");

	private static int count = 0;
	
	private static float size = Settings.getResolution().x * Settings.getResolution().y;

	synchronized public static void pixelRendered()
	{
		count++;
		
		if(count % (size/10) != 0){
			Frame.progressBar.setValue((int) (count/size * 100));

			return;
		}
		logger.info("Progress = " + (int) (count/size * 100) + "%");

	}
	
	synchronized public static void resetProgress()
	{
		count = 0;
		Frame.progressBar.setValue(0);
		size = Main.getImage().getHeight() * Main.getImage().getWidth();
	}
}
