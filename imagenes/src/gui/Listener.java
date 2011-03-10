package gui;

import gui.Frame.AlertType;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.vecmath.Point2i;

import core.Settings;
import core.Settings.BucketTypes;

import renderer.BinaryImage;
import renderer.ColorGradientGenerator;
import renderer.GrayScaleGenerator;
import renderer.Renderer;

import main.Main;
import main.Util;

public class Listener {

	private static SwingWorker worker;
	private static  Logger logger = LoggerWindowHandler.getLogger("Listener");
	
	public static class  renderListener implements MouseListener
	{

		static Object doWork() throws Exception {
				Main.getRenderer().Render();
			return "Render done";
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			
			
			ButtonPanel  b = Main.getFrame().getButtons();
			if(!b.getRender().isEnabled())
				return;
			Menu m = Main.getFrame().getMenu();

			if(m.colorScale.isSelected())
				Main.renderer = new ColorGradientGenerator();
			else if(m.grayScale.isSelected())
				Main.renderer = new GrayScaleGenerator();
			if(m.binaryImage.isSelected())
				Main.renderer = new BinaryImage();
	
			if(!b.isRendering())
			{
				if(!Renderer.isInterrupt())
				{
					Main.createImage();
					System.out.println(Main.getImage().getHeight() + " " + Main.getImage().getWidth());
					JButton render = b.getRender();
					//render.setIcon(new ImageIcon("TPE01/icons/stop.png"));
										
					render.setIcon(new ImageIcon(ClassLoader.getSystemResource("stop.png")));
					render.setText("Stop");
					b.setRendering(true);
				}
				if(worker == null)
				{
					worker = new SwingWorker()
					{
	
						@Override
						public Object construct() {
							try {
								return doWork();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return null;
							}
						}
						
						public void finished(){
							enableSavingImage();
							if(worker !=  null)
								worker.interrupt();
							worker = null;
						}
						
					};
					worker.start();
				}
				else
				{
					if(Renderer.isInterrupt())
					{
						Main.getFrame().ShowDialog("Wait, threads have not been terminated yet!", "Still interrupting!", AlertType.ERROR);
					}
					else
						worker.start();
						
				}
				
			}
			else
			{

				
				b.setRendering(false);
				
				logger.warning("Render has been interrupted!");
				Main.getRenderer().interruptThreads();
				enableSavingImage();
				
			}		
		}





		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public static class dofEnabledListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
	

	public static class adaptiveThresholdListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFrame frame = new JFrame("Set Adaptive Threshold");

			final SpinnerNumberModel spBlueModel = new SpinnerNumberModel(0,-1,500,1);
			final JSpinner spBlue = new JSpinner(spBlueModel);
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
								
				}
			});
			JLabel bluelabel = new JLabel("Threshold:");
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 2));
			panel.add(bluelabel);
			panel.add(spBlue);
			frame.add(confirm, BorderLayout.SOUTH);
			frame.add(panel, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
			
		}
		
	}

	public static class aaBoundsListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			JFrame frame = new JFrame("Set AA Bounds");
			final SpinnerNumberModel spMaxModel = new SpinnerNumberModel(0,-4,4,1);
			final SpinnerNumberModel spMinModel = new SpinnerNumberModel(0,-4,4,1);
			final JSpinner spMin = new JSpinner(spMinModel);
			final JSpinner spMax = new JSpinner(spMaxModel);
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Settings.AAMin = (Integer) spMin.getValue();
					Settings.AAMax = (Integer) spMax.getValue();
					
					
				}
			});
			JLabel maxlabel = new JLabel("Max AntiAliasing:");
			JLabel minlabel = new JLabel("Min AntiAliasing:");
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 2));
			panel.add(minlabel);
			panel.add(spMin);
			panel.add(maxlabel);
			panel.add(spMax);
			frame.add(confirm, BorderLayout.SOUTH);
			frame.add(panel, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		
	}
		
	public static class aaStochasticListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public static class aaAdaptiveListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	public static class engineListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Main.getFrame().getButtons().enableRender();
			enableOptions();
		}
		
	}
	
	public static class aaEnabled implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(Main.getFrame().getMenu().aaEnabled.isSelected())
				enableAAOptions();
			else
				disableAAOptions();
			
		}
		
	}
	
	public static class bucketOrderListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(Main.getFrame().getButtons().isRendering())
			{	
				Main.getFrame().ShowDialog("Cant change parameters while rendering! Press Stop!", "Rendering", AlertType.ERROR);			
				return;
			}
			JRadioButtonMenuItem row, col, rand, ord;
			row = Main.getFrame().getMenu().getRowOrder();
			rand = Main.getFrame().getMenu().getRandomOrder();
			col = Main.getFrame().getMenu().getColOrder();
			ord = Main.getFrame().getMenu().getOrderOrder();
			
			if(!row.isEnabled())
				return;
			if(row.isSelected())
				Settings.bucketType = BucketTypes.ROWS;
			else if(col.isSelected())
				Settings.bucketType = BucketTypes.COLS;
			else if(ord.isSelected())
				Settings.bucketType = BucketTypes.ORDER;
			else if(rand.isSelected())
				Settings.bucketType = BucketTypes.AREA;
			else
				Settings.bucketType = BucketTypes.ROWS;
			
		}
		
	}
	
	public static class setBucketListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(Main.getFrame().getButtons().isRendering())
			{
				Main.getFrame().ShowDialog("Cant change parameters while rendering! Press Stop!", "Rendering", AlertType.ERROR);
				return;
			}
			JMenuItem bucket = Main.getFrame().getMenu().getBucketSize(); 
			if(!bucket.isEnabled())
				return;
			JFrame frame = new JFrame("Set Bucket Size");
			final JFormattedTextField tf = new JFormattedTextField(NumberFormat.getIntegerInstance());
			tf.setValue(Settings.bucket);
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Settings.bucket = (Integer.valueOf( tf.getValue().toString()));
					
				}
			});
			tf.setColumns(10);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(1, 2));
			panel.add(new JLabel("Bucket:"));
			panel.add(tf);
			frame.add(confirm, BorderLayout.SOUTH);
			frame.add(panel, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		
	}
	
	public static class setCopySubImageListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(Main.getFrame().getButtons().isRendering())
			{
				Main.getFrame().ShowDialog("Cant change parameters while rendering! Press Stop!", "Rendering", AlertType.ERROR);
				return;
			}
			JMenuItem pixelColor = Main.getFrame().getMenu().getSetPixelColor(); 
			if(!pixelColor.isEnabled())
				return;
			JFrame frame = new JFrame("Set Pixel Color");
			final JFormattedTextField fromx = new JFormattedTextField(NumberFormat.getNumberInstance());
			final JFormattedTextField fromyy = new JFormattedTextField(NumberFormat.getNumberInstance());
			final JFormattedTextField width = new JFormattedTextField(NumberFormat.getNumberInstance());
			final JFormattedTextField height = new JFormattedTextField(NumberFormat.getNumberInstance());
			fromx.setValue(0);
			fromyy.setValue(0);
			width.setValue(100);
			height.setValue(100);
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
						if(!Settings.isImageLoaded())
							return;
					
						
						  int w = Integer.valueOf( width.getValue().toString());
						  int h = Integer.valueOf( height.getValue().toString());
						  int X = Integer.valueOf( fromx.getValue().toString());
						  int Y = Integer.valueOf( fromyy.getValue().toString());
					    try {
					    	Main.image = Util.getSubimage(Main.getImage(), X, Y, w, h);  
						} catch (IllegalArgumentException e) {

						}
						
						Main.update();
						}
					
			});
			fromx.setColumns(10);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(4, 2));
			panel.add(new JLabel("X:"));
			panel.add(fromx);
			panel.add(new JLabel("Y:"));
			panel.add(fromyy);
			panel.add(new JLabel("Width:"));
			panel.add(width);
			panel.add(new JLabel("Height:"));
			panel.add(height);

			frame.add(confirm, BorderLayout.SOUTH);
			frame.add(panel, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
		}
		
	}
	
	public static class setPixelColorListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(Main.getFrame().getButtons().isRendering())
			{
				Main.getFrame().ShowDialog("Cant change parameters while rendering! Press Stop!", "Rendering", AlertType.ERROR);
				return;
			}
			JMenuItem pixelColor = Main.getFrame().getMenu().getSetPixelColor(); 
			if(!pixelColor.isEnabled())
				return;
			JFrame frame = new JFrame("Set Pixel Color");
			final JFormattedTextField x = new JFormattedTextField(NumberFormat.getNumberInstance());
			final JFormattedTextField y = new JFormattedTextField(NumberFormat.getNumberInstance());
			final JFormattedTextField r = new JFormattedTextField(NumberFormat.getNumberInstance());
			final JFormattedTextField g = new JFormattedTextField(NumberFormat.getNumberInstance());
			final JFormattedTextField b = new JFormattedTextField(NumberFormat.getNumberInstance());
			x.setValue(0);
			y.setValue(0);
			r.setValue(0);
			g.setValue(0);
			b.setValue(0);
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
						if(!Settings.isImageLoaded())
							return;
					
						
						  int red = Integer.valueOf( r.getValue().toString());
						  int green = Integer.valueOf( g.getValue().toString());
						  int blue = Integer.valueOf( b.getValue().toString());
						  int X = Integer.valueOf( x.getValue().toString());
						  int Y = Integer.valueOf( y.getValue().toString());
					
						  Util.setPixel(X, Y, red, green, blue);
						  
					//	Main.getImage().setRGB(X, Y, 0xFF000000 | (red << 16) | (green << 8) | blue );
					/*	for (int x = 10; x < 50; x++) {
							for (int y = 10; y < 50; y++) {
								Main.getImage().setRGB(x, y, 0xFF000000 | (red << 16) | (green << 8) | blue );
							}
						}
					*/	Main.update();
						}
					
			});
			x.setColumns(10);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(5, 2));
			panel.add(new JLabel("X:"));
			panel.add(x);
			panel.add(new JLabel("Y:"));
			panel.add(y);
			panel.add(new JLabel("Red:"));
			panel.add(r);
			panel.add(new JLabel("Green:"));
			panel.add(g);
			panel.add(new JLabel("Blue:"));
			panel.add(b);

			frame.add(confirm, BorderLayout.SOUTH);
			frame.add(panel, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
		}
		
	}
	
	
	public static class getPixelColorListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(Main.getFrame().getButtons().isRendering())
			{
				Main.getFrame().ShowDialog("Cant change parameters while rendering! Press Stop!", "Rendering", AlertType.ERROR);
				return;
			}
			JMenuItem pixelColor = Main.getFrame().getMenu().getSetPixelColor(); 
			if(!pixelColor.isEnabled())
				return;
			JFrame frame = new JFrame("Get Pixel Color");
			final JFormattedTextField x = new JFormattedTextField(NumberFormat.getNumberInstance());
			final JFormattedTextField y = new JFormattedTextField(NumberFormat.getNumberInstance());
			x.setValue(0);
			y.setValue(0);
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
						if(!Settings.isImageLoaded())
							return;
					
						
						  int X = Integer.valueOf( x.getValue().toString());
						  int Y = Integer.valueOf( y.getValue().toString());
					
						  int pixel = Main.getImage().getRGB(X, Y);
						  int alpha = (pixel >> 24) & 0xff;
						    int red = (pixel >> 16) & 0xff;
						    int green = (pixel >> 8) & 0xff;
						    int blue = (pixel) & 0xff;
							Main.getFrame().ShowDialog("alpha " + alpha + "\nRed " + red + "\nGreen " + green + "\nBlue " + blue, "ARGB", AlertType.INFO);
							logger.info("Pixel color of position (" +X+", " + Y +")\n alpha " + alpha + "\nRed " + red + "\nGreen " + green + "\nBlue " + blue);
							 
						}
					
			});
			x.setColumns(10);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 2));
			panel.add(new JLabel("X:"));
			panel.add(x);
			panel.add(new JLabel("Y:"));
			panel.add(y);
			frame.add(confirm, BorderLayout.SOUTH);
			frame.add(panel, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
		}
		
	}
	public static class setSizeListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(Main.getFrame().getButtons().isRendering())
			{
				Main.getFrame().ShowDialog("Cant change parameters while rendering! Press Stop!", "Rendering", AlertType.ERROR);
				return;
			}
			JMenuItem size = Main.getFrame().getMenu().getSetSize(); 
			if(!size.isEnabled())
				return;
			JFrame frame = new JFrame("Set Size");
			final JFormattedTextField tfW = new JFormattedTextField(NumberFormat.getNumberInstance());
			final JFormattedTextField tfH = new JFormattedTextField(NumberFormat.getNumberInstance());
			tfW.setValue(Settings.getResolution().x);
			tfH.setValue(Settings.getResolution().y);
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Point2i s = new Point2i(
							Integer.valueOf(tfW.getValue().toString()),
							Integer.valueOf(tfH.getValue().toString()));
					Settings.setResolution(s);
					Main.createImage();
					Main.update();
					
					
				}
			});
			tfW.setColumns(10);
			tfH.setColumns(10);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 2));
			panel.add(new JLabel("Width:"));
			panel.add(tfW);
			panel.add(new JLabel("Height"));
			panel.add(tfH);
			
			frame.add(confirm, BorderLayout.SOUTH);
			frame.add(panel, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
		}
		
	}
	
	public static class openImageListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(Main.getFrame().getButtons().isRendering())
			{	
				Main.getFrame().ShowDialog("Cant open another image while rendering this one! Press Stop!", "Rendering", AlertType.ERROR);		
				return;
			}
			final JFileChooser fc = new JFileChooser();

			int returnVal = fc.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				
				try {
					logger.info("Loading Image!");
					Main.image = Util.loadImage(file.getAbsolutePath());
					Settings.setResolution(new Point2i(Main.getImage().getWidth(), Main.getImage().getHeight()));
					//SunflowScene.startScene(file.getAbsolutePath());
					logger.info("Image loaded");
					tryToEnableRender();
					enableSavingImage();
					enableOptions();
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							Main.getFrame().getPanel().repaint();
						}

					});
				} catch (Exception e1) {
					e1.printStackTrace();
					logger.severe(e1.getMessage());
					logger.info("Failed to load image");
				}

			}
			
		}
		
	}
	
	private static void enableOptions()
	{
		Menu m = Main.getFrame().getMenu();
		m.getSetPixelColor().setEnabled(true);
		m.getPixelColor().setEnabled(true);
		m.getCopySubImage().setEnabled(true);

		m.getSetSize().setEnabled(true);
		m.getRowOrder().setEnabled(true);
		m.colOrder.setEnabled(true);
		m.randomOrder.setEnabled(true);
		m.bucketSize.setEnabled(true);
		m.aaEnabled.setEnabled(true);
		m.dofEnabled.setEnabled(true);
		m.orderedOrder.setEnabled(true);
	}
	
	private static void enableAAOptions()
	{
		Menu m = Main.getFrame().getMenu();
		m.aaBounds.setEnabled(true);
		m.adaptive.setEnabled(true);
		m.stochastic.setEnabled(true);
		m.adaptiveThreshold.setEnabled(true);
	}
	
	private static void disableAAOptions()
	{
		Menu m = Main.getFrame().getMenu();
		m.aaBounds.setEnabled(false);
		m.adaptive.setEnabled(false);
		m.stochastic.setEnabled(false);
	}
	
	private static void enableDOFOptions()
	{
		Menu m = Main.getFrame().getMenu();
		m.dofDispersion.setEnabled(true);
		m.dofIterations.setEnabled(true);
		m.dofsharpPlane.setEnabled(true);
	}
	
	private static void disableDOFOptions()
	{
		Menu m = Main.getFrame().getMenu();
		m.dofDispersion.setEnabled(false);
		m.dofIterations.setEnabled(false);
		m.dofsharpPlane.setEnabled(false);
		
	}
	
	public static class setDOFIterationsListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {

			JMenuItem iterations = Main.getFrame().getMenu().dofIterations; 
			if(!iterations.isEnabled())
				return;
			JFrame frame = new JFrame("Set Iterations");
			final JFormattedTextField iterTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
			
			
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
				
			
					 
					
				}
			});
			iterTF.setColumns(10);
			
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(1, 2));
			panel.add(new JLabel("Iterations:"));
			panel.add(iterTF);
			
			
			frame.add(confirm, BorderLayout.SOUTH);
			frame.add(panel, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}
	
	public static class setDOFDispersionListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {

			JMenuItem iterations = Main.getFrame().getMenu().dofDispersion; 
			if(!iterations.isEnabled())
				return;
			JFrame frame = new JFrame("Set Dispersion Factor");
			final JFormattedTextField dispTF = new JFormattedTextField(NumberFormat.getNumberInstance());
			
			
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
				
					
				}
			});
			dispTF.setColumns(10);
			
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(1, 2));
			panel.add(new JLabel("Dispersion:"));
			panel.add(dispTF);
			
			
			frame.add(confirm, BorderLayout.SOUTH);
			frame.add(panel, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}
	
	public static class setDOFSharpPlane implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			
		JMenuItem sharpPlane = Main.getFrame().getMenu().dofsharpPlane; 
		if(!sharpPlane.isEnabled())
			return;
		
		JFrame frame = new JFrame("Set Sharp Plane");
		final JFormattedTextField xTF = new JFormattedTextField(NumberFormat.getNumberInstance());
		final JFormattedTextField yTF = new JFormattedTextField(NumberFormat.getNumberInstance());
		final JFormattedTextField zTF = new JFormattedTextField(NumberFormat.getNumberInstance());

		JButton confirm = new JButton("Confirm");
		confirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
			

				
				
				
			}
		});
		xTF.setColumns(10);
		yTF.setColumns(10);
		zTF.setColumns(10);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));
		panel.add(new JLabel("X:"));
		panel.add(xTF);
		panel.add(new JLabel("Y:"));
		panel.add(yTF);
		
		panel.add(new JLabel("Z:"));
		panel.add(zTF);
		
		frame.add(confirm, BorderLayout.SOUTH);
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
		
	}
	
	private static void tryToEnableRender()
	{
		if(!Settings.isImageLoaded())
			return;
		Main.getFrame().getButtons().enableRender();
		
	}
	
	private static void enableSavingImage()
	{
		Main.getFrame().getMenu().getSaveImage().setEnabled(true);
	}
	
	public static class saveImageListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(Main.getFrame().getButtons().isRendering())
				return;
			JMenuItem save = Main.getFrame().getMenu().getSaveImage();
			if(!save.isEnabled())
				return;
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(null);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				Util.saveImage(file.getAbsolutePath());
			}
			
		}
		
	}
}
