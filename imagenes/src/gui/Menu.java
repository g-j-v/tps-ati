package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import main.Main;

@SuppressWarnings("serial")
public class Menu extends JMenuBar {



	JRadioButtonMenuItem	 grayScale;
	JRadioButtonMenuItem	 colorScale;
	JRadioButtonMenuItem	 binaryImage;
	JMenuItem openImage;
	JMenuItem saveImage;
	JMenuItem setSize;

	JMenuItem copySubImage;
	JMenuItem getPixelColor;
	JMenuItem setPixelColor;
	JRadioButtonMenuItem renderTime;
	JRadioButtonMenuItem progress;
	JRadioButtonMenuItem softShadows;
	JRadioButtonMenuItem cellShading;
	
	//MultiThreading
	JRadioButtonMenuItem rowOrder;
	JRadioButtonMenuItem colOrder;
	JRadioButtonMenuItem orderedOrder;
	
	JRadioButtonMenuItem randomOrder;
	JMenuItem bucketSize;
	JRadioButtonMenuItem fitToSize;
	
	//Antialiasing
	JRadioButtonMenuItem aaEnabled;


	JRadioButtonMenuItem stochastic;
	JRadioButtonMenuItem adaptive;
	JMenuItem aaBounds;
	
	JMenuItem adaptiveThreshold;
	
	
	//DOF
	JRadioButtonMenuItem dofEnabled;
	JMenuItem dofDispersion;
	JMenuItem dofIterations;
	JMenuItem dofsharpPlane;
	
	public Menu() {

		add(createScaleMenu());
		add(createFileMenu());
		add(createOptionsMenu());
		add(createRenderingOptions());
		add(createAAOptions());
		add(createDOFOptions());
	

	}


	private JMenu createScaleMenu() {
	
		JMenu renderMenu = new JMenu("Renderer");
		ButtonGroup group = new ButtonGroup();
		grayScale = new JRadioButtonMenuItem("Gray Gradient");
		colorScale = new JRadioButtonMenuItem("Color Gradient");
		binaryImage = new JRadioButtonMenuItem("Binary Image");
		
		grayScale.addActionListener(new Listener.engineListener());
		colorScale.addActionListener(new Listener.engineListener());
		binaryImage.addActionListener(new Listener.engineListener());
		group.add(colorScale);
		group.add(binaryImage);
		group.add(grayScale);
		renderMenu.add(colorScale);
		renderMenu.add(grayScale);
		renderMenu.add(binaryImage);
		return renderMenu;
	}
	
	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");

		openImage = new JMenuItem("Open Image");
		saveImage = new JMenuItem("Save Image");
		saveImage.setEnabled(false);
		openImage.addActionListener(new Listener.openImageListener());
		saveImage.addActionListener(new Listener.saveImageListener());
		fileMenu.add(openImage);
		fileMenu.add(saveImage);
		return fileMenu;
	}



	private JMenu createOptionsMenu() {
		JMenu Options;
		
		
		
		Options = new JMenu("Image");
		
		
		//Display Options **************************************************
		setSize = new JMenuItem("Set Image Size");
		copySubImage = new JMenuItem("Copy Sub Image ");
		setPixelColor = new JMenuItem("Set pixel color");
		getPixelColor= new JMenuItem("Get pixel color");
		setPixelColor.addActionListener(new Listener.setPixelColorListener());
		copySubImage.addActionListener(new Listener.setCopySubImageListener());
		setSize.addActionListener(new Listener.setSizeListener());
		getPixelColor.addActionListener(new Listener.getPixelColorListener());
		renderTime = new JRadioButtonMenuItem("Render Time");
		renderTime.setSelected(true);
		setPixelColor.setEnabled(false);
		copySubImage.setEnabled(false);
		setSize.setEnabled(false);
		getPixelColor.setEnabled(false);
		progress = new JRadioButtonMenuItem("Show progress");
		progress.setSelected(true);
		softShadows = new JRadioButtonMenuItem("Soft Shadows");
		Options.add(setSize);
		Options.add(copySubImage);

		Options.add(getPixelColor);

		Options.add(setPixelColor);
		Options.add(renderTime);
		Options.add(progress);
		Options.add(softShadows);
		
		fitToSize = new JRadioButtonMenuItem("Fit image to Panel");
		fitToSize.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Main.update();
			}
		});
		Options.add(fitToSize);
		


		return Options;
	}
	
	private JMenu createRenderingOptions()
	{
		JMenu Options;
	
		Options = new JMenu("Rendering");
		
		
		//MultiThreading Options *********************************************
		
		ButtonGroup group = new ButtonGroup();
		
		
		rowOrder = new JRadioButtonMenuItem("Row order");
		colOrder = new JRadioButtonMenuItem("Col order");
		orderedOrder = new JRadioButtonMenuItem("Ordered order");		
		randomOrder = new JRadioButtonMenuItem("Random order");
		
		rowOrder.addActionListener(new Listener.bucketOrderListener());
		colOrder.addActionListener(new Listener.bucketOrderListener());
		randomOrder.addActionListener(new Listener.bucketOrderListener());
		orderedOrder.addActionListener(new Listener.bucketOrderListener());
		rowOrder.setEnabled(false);
		colOrder.setEnabled(false);
		randomOrder.setEnabled(false);
		orderedOrder.setEnabled(false);
		group.add(rowOrder);
		group.add(colOrder);
		group.add(orderedOrder);
		group.add(randomOrder);
		
		bucketSize = new JMenuItem("Set Bucket Size");
		bucketSize.setEnabled(false);
		bucketSize.addActionListener(new Listener.setBucketListener());
	
		
		Options.add(bucketSize);
		Options.add(rowOrder);
		Options.add(colOrder);
		Options.add(orderedOrder);
		Options.add(randomOrder);
		
		
		return Options;
	}
	
	private JMenu createAAOptions()
	{
		JMenu Options;
		
		Options = new JMenu("AntiAliasing");
		
		//AA options ********************************************************************
		
		aaEnabled = new JRadioButtonMenuItem("AntiAliasing");
		aaEnabled.addActionListener(new Listener.aaEnabled());
		aaEnabled.setEnabled(false);
		Options.add(aaEnabled);
		
		aaBounds = new JMenuItem("Set AntiAliasing Bounds");
		stochastic = new JRadioButtonMenuItem("Stochastic");
		adaptive = new JRadioButtonMenuItem("Adaptive");
		adaptiveThreshold = new JMenuItem("Set Adaptive Threshold");
		
		aaBounds.addActionListener(new Listener.aaBoundsListener());
		stochastic.addActionListener(new Listener.aaStochasticListener());
		adaptive.addActionListener(new Listener.aaAdaptiveListener());
		adaptiveThreshold.addActionListener(new Listener.adaptiveThresholdListener());
		
		aaBounds.setEnabled(false);
		stochastic.setEnabled(false);
		adaptive.setEnabled(false);
		adaptiveThreshold.setEnabled(false);
		Options.add(aaBounds);
		
		Options.add(stochastic);
		Options.add(adaptive);
		Options.add(adaptiveThreshold);
		return Options;
		
	}
	
	private JMenu createDOFOptions()
	{
		JMenu Options;
		
		Options = new JMenu("Depth of Field");
		dofEnabled = new JRadioButtonMenuItem("Enable Depth Of Field");
		dofIterations = new JMenuItem("Set Iterations");
		dofDispersion = new JMenuItem("Set Dispersion");
		dofsharpPlane = new JMenuItem("Set Sharp Plane");
		
		dofEnabled.setEnabled(false);
		dofIterations.setEnabled(false);
		dofDispersion.setEnabled(false);
		dofsharpPlane.setEnabled(false);
		
		dofEnabled.addActionListener(new Listener.dofEnabledListener());
		dofIterations.addActionListener(new Listener.setDOFIterationsListener());
		dofDispersion.addActionListener(new Listener.setDOFDispersionListener());
		dofsharpPlane.addActionListener(new Listener.setDOFSharpPlane());
		
		Options.add(dofEnabled);
		Options.add(dofIterations);
		Options.add(dofDispersion);
		Options.add(dofsharpPlane);
		return Options;
		
	}
	

	
	public boolean engineSelected()
	{
		return colorScale.isSelected() || grayScale.isSelected() || binaryImage.isSelected(); 
	}
	
	public JRadioButtonMenuItem getRayTracer() {
		return grayScale;
	}

	public JRadioButtonMenuItem getRayCaster() {
		return colorScale;
	}

	public JMenuItem getOpenScene() {
		return openImage;
	}

	public JMenuItem getSaveImage() {
		return saveImage;
	}

	public JMenuItem getSetSize() {
		return setSize;
	}

	public JMenuItem getCopySubImage() {
		return copySubImage;
	}
	
	public JMenuItem getPixelColor() {
		return getPixelColor;
	}
	
	public JMenuItem getSetPixelColor() {
		return setPixelColor;
	}

	public JRadioButtonMenuItem getRenderTime() {
		return renderTime;
	}

	public JRadioButtonMenuItem getProgress() {
		return progress;
	}
	
	public JRadioButtonMenuItem getSoftShadows() {
		return softShadows;
	}
	
	public JRadioButtonMenuItem getRowOrder() {
		return rowOrder;
	}

	public JRadioButtonMenuItem getColOrder() {
		return colOrder;
	}

	public JRadioButtonMenuItem getRandomOrder() {
		return randomOrder;
	}

	public JMenuItem getBucketSize() {
		return bucketSize;
	}

	public JRadioButtonMenuItem getFitToSize() {
		return fitToSize;
	}
	
	public JRadioButtonMenuItem getAaEnabled() {
		return aaEnabled;
	}


	public JRadioButtonMenuItem getStochastic() {
		return stochastic;
	}


	public JRadioButtonMenuItem getAdaptive() {
		return adaptive;
	}


	public JMenuItem getAaBounds() {
		return aaBounds;
	}


	public JRadioButtonMenuItem getOrderOrder() {
		return orderedOrder;
	}
	
	
	
}