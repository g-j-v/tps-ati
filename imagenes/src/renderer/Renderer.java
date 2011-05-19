package renderer;

import gui.Frame;
import gui.Frame.AlertType;
import gui.LoggerWindowHandler;

import java.awt.Color;
import java.util.Collection;
import java.util.Random;
import java.util.logging.Logger;

import javax.vecmath.Point2i;

import main.Main;
import core.Camera;
import core.Camera.ImageScan;
import core.PixelRay;
import core.Progress;
import core.RenderTimer;
import core.Settings;
import core.Settings.BucketTypes;



public abstract class Renderer {

	private Random r = new Random(System.currentTimeMillis());
	protected Point2i res;
	private static Camera cam = new Camera();
	private static Thread[] threads;
	private static boolean interrupt;
	private RenderTimer timer;
	private static Logger logger = LoggerWindowHandler.getLogger("Renderer");
	
	private static Collection<PixelRay> orderedPixelRays;
	public void Render() throws InterruptedException {
		res = Settings.getResolution();
		
		timer = null;
		interrupt = false;

		RenderThread[] renderThreads = createRendererThreads();
		threads = new Thread[renderThreads.length];
	
		
		for (int i = 0; i < renderThreads.length; i++) 
			threads[i] = new Thread(renderThreads[i]);
		
		if (Main.getFrame().getMenu().getRenderTime().isSelected())
			timer = new RenderTimer();
	
		logger.info("Starting render");
		Progress.resetProgress();
		
			
		
		for (int i = 0; i < renderThreads.length; i++) {
			threads[i].start();
		}
		

		try {
			for (int i = 0; i < renderThreads.length; i++)
				threads[i].join();

		} catch (InterruptedException e) {
			interrupt = true;
			e.printStackTrace();

		}
		
		if(Main.getFrame().getMenu().getAaEnabled().isSelected() && !interrupt)
		{
			if(!Main.getFrame().getMenu().getRenderTime().isEnabled())
			{
				Main.getFrame().ShowDialog("Starting AntiAliasing", "AntiAliasing", AlertType.INFO);
			
			}
	//		AntiAliasing.ManageAntiAliasing();
		}

		Main.getFrame().UpdatePanel();
		endRender();

	}

	public void interruptThreads() {
		logger.info("Interrupting " + threads.length + " Threads");
		interrupt = true;
		if (threads.length > 10000)
			Main.getFrame().ShowDialog(
					"Please wait while the threads are being terminated...",
					"Wait!", AlertType.INFO);
		for (int i = 0; i < threads.length; i++) {
			if (threads[i] != null)
				threads[i].interrupt();
		}
		
		

	}

	public void endRender() {

		Frame f = Main.getFrame();

		f.getButtons().reset();
		if (interrupt) {
			if (timer != null)
				Main.getFrame().ShowDialog(
						"Render interrupted after "
								+ (float) (timer.getTime() / 1000.0)
								+ " seconds\n" + threads.length
								+ " threads have been terminated!",
						"Render Interrupted!", AlertType.ERROR);
			else
				Main.getFrame().ShowDialog(
						"All " + threads.length
								+ " threads have been terminated!",
						"Render Interrupted!", AlertType.ERROR);

		} else if (timer != null)
			Main.getFrame().ShowDialog(
					"Render took " + (float) (timer.getTime() / 1000.0)
							+ " seconds", "Render Complete!", AlertType.INFO);
		else
			Main.getFrame()
					.ShowDialog(
							"You can check the RenderTime option to see how long it took!",
							"Render Complete!", AlertType.INFO);
		logger.info("All threads are done");
		interrupt = false;
	}



	class RenderThread implements Runnable {

		Point2i start;
		int threadNumber;


		Collection<PixelRay> pixRays = null;
		
		public RenderThread(Point2i s, int tN) {
			start = new Point2i(s);
			threadNumber = tN;

			
			if (interrupt)
				return;

			float bucket = Settings.bucket;

			if (bucket == 0)
				bucket = 99999;

			switch (Settings.bucketType) {
			case ROWS: {
				try {

					pixRays = cam.firePixelRays(
							start,
							new Point2i((int) Settings.getResolution().x,
									(int) Math.min(start.y + bucket,
											Settings.getResolution().y)),
							ImageScan.ROWS);

				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

			}
				break;
			case COLS: {
				try {

					pixRays = cam.firePixelRays(
							start,
							new Point2i((int) Math.min(start.x + bucket,
									Settings.getResolution().x),
									Settings.getResolution().y), ImageScan.COLS);


				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
				break;
			case AREA: {
				Point2i finish = new Point2i((int) (start.x + bucket),
						(int) (start.y + bucket));

				if (finish.x >= Settings.getResolution().x)
					finish.x = (int) Settings.getResolution().x;
				if (finish.y >= Settings.getResolution().y)
					finish.y = (int) Settings.getResolution().y;

				try {
					pixRays = cam.firePixelRays(start, finish,
							ImageScan.ROWS);


				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
				break;
			case ORDER:
			{
				pixRays = orderedPixelRays;
			}
			break;

			}

		}

		@Override
		public void run() {
			for (PixelRay pixRay : pixRays) {

				//Should be true only with ordered Order
				if(pixRay.isRendered())
					continue;
				

			
				if (Renderer.interrupt)
					return;
		
				pixRay.setColor(RenderPixel(pixRay));
				try {
						Main.getImage().setRGB((int) pixRay.getPos().x,
								(int) pixRay.getPos().y,
								pixRay.getColor().getRGB());
						UpdateProgress();
					if (Main.getFrame() != null)
						Main.getFrame().UpdatePanel();
				} catch (Exception e) {
					logger.severe("Invalid Pixel Pos " + pixRay.getPos());
					logger.severe("Image is :" + Main.getImage().getWidth()
							+ "x" + Main.getImage().getHeight());
					e.printStackTrace();

					return;
				}
				

			}

		}


	}

	public abstract Color RenderPixel(PixelRay pixRay);

	
	private void UpdateProgress()
	{
		if (Main.getFrame() != null && Main.getFrame().getMenu().getProgress().isSelected())
			Progress.pixelRendered();

	}


	private RenderThread[] createRendererThreads() {
		Point2i dim = Settings.getResolution();
		int bucket = Settings.bucket;
		int threadqty = 1;
		if (Settings.bucket == 0)
		{
			threadqty = 1;
		}
		else {
			switch (Settings.bucketType) {
			case AREA: {
				int paddedX = (int) Math.ceil(dim.x / bucket);
				paddedX *= bucket;
				int paddedY = (int) Math.ceil(dim.y / bucket);
				paddedY *= bucket;
				float area = paddedX * paddedY;

				threadqty = (int) Math.ceil(area
						/ ((double) Settings.bucket * Settings.bucket));
				
			}
				break;
			case ROWS: {
				threadqty = (int) Math.ceil(dim.y / Settings.bucket);
				
			}
				break;
			case COLS: {
				threadqty = (int) Math.ceil(dim.x / Settings.bucket);
				
			}
				break;
			
				
			case ORDER:{
				threadqty = (int) Math.ceil(dim.x / Settings.bucket);
				
			}
				break;
			}
		}
		
		if(Settings.bucketType == BucketTypes.ORDER)
		{
			try {
				orderedPixelRays = cam.firePixelRays(new Point2i(0,0), new Point2i(Settings.getResolution().x,
						Settings.getResolution().y),ImageScan.ROWS);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		RenderThread renderThreads[] = new RenderThread[threadqty];
		Point2i threadStartingPoint = null;

		Integer[] randIndexes = new Integer[threadqty];
		for (int i = 0; i < threadqty; i++)
			randIndexes[i] = i;
		randomShuffle(randIndexes);

		for (int i = 0; i < threadqty; i++) {
			threadStartingPoint = determineStartingPoint(i, threadqty,
					randIndexes);
			renderThreads[i] = new RenderThread(threadStartingPoint, i);
		}
		return renderThreads;
	}


	private void randomShuffle(Integer[] path) {

		for (int i = path.length; i > 0;) {
			int p = r.nextInt(i--);
			int tmp = path[p];
			path[p] = path[i];
			path[i] = tmp;
		}
	}

	private Point2i determineStartingPoint(int threadIndex, int threadqty,
			Integer[] randIndexes) {
		Point2i dim = Settings.getResolution();
		float bucket = Settings.bucket;
		Point2i startPoint = null;
		if (Settings.bucketType == BucketTypes.ORDER)
			startPoint = new Point2i(0, 0);

		else
			switch (Settings.bucketType) {
			case ROWS: {
				int startingRow = threadIndex * Settings.bucket;
				startPoint = new Point2i(0, startingRow);
			}
				break;
			case COLS: {
				int startCol = threadIndex * Settings.bucket;
				startPoint = new Point2i(startCol, 0);
			}
				break;
			case AREA: {
				int actualIndex = randIndexes[threadIndex];

				Point2i newDimCoord = new Point2i((int)Math.ceil((dim.x / bucket)),(int)Math.ceil(( dim.y / bucket)));
				Point2i newStart = new Point2i( actualIndex % newDimCoord.x , (int)Math.floor(actualIndex / newDimCoord.x));		
				startPoint = new Point2i((int)(newStart.x * bucket),(int)(newStart.y * bucket));
				
			}
				break;

			}

		return startPoint;
	}

	public static boolean isInterrupt() {
		return interrupt;
	}

}
