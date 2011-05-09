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


	ButtonGroup rendererGroup;
	
	JRadioButtonMenuItem	 grayScale;
	JRadioButtonMenuItem	 colorScale;
	JRadioButtonMenuItem	 binaryImage;
	JRadioButtonMenuItem	 compresion;
	
	
	JMenuItem secondaryImage;
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
	JRadioButtonMenuItem negateFilter;


	JRadioButtonMenuItem equalize;
	JRadioButtonMenuItem gamma ;
	JRadioButtonMenuItem histogram;
	JRadioButtonMenuItem sum;
	JRadioButtonMenuItem contrast;
	JRadioButtonMenuItem umbral;	
	JRadioButtonMenuItem product;
	JRadioButtonMenuItem substraction;
	JRadioButtonMenuItem dotProduct;
	JRadioButtonMenuItem applyMatrix;
	JRadioButtonMenuItem median;
	JRadioButtonMenuItem sobel;
	
	//DOF
	JRadioButtonMenuItem matrix;
	JRadioButtonMenuItem exponencialN;
	JRadioButtonMenuItem rayleighN;
	JRadioButtonMenuItem gaussianN;
	JRadioButtonMenuItem saltnpepperN;
	JMenuItem dofDispersion;
	JMenuItem dofIterations;
	JMenuItem dofsharpPlane;
	
	public Menu() {

		add(createScaleMenu());
		add(createFileMenu());
		add(createOptionsMenu());
		add(createRenderingOptions());
		add(createFilterOptions());
		add(createDOFOptions());
	

	}


	private JMenu createScaleMenu() {
	
		JMenu renderMenu = new JMenu("Renderer");
		rendererGroup = new ButtonGroup();
		grayScale = new JRadioButtonMenuItem("Gray Gradient");
		colorScale = new JRadioButtonMenuItem("Color Gradient");
		binaryImage = new JRadioButtonMenuItem("Binary Image");
		compresion = new JRadioButtonMenuItem("Compresion de rango"); 
		
		grayScale.addActionListener(new Listener.engineListener());
		colorScale.addActionListener(new Listener.engineListener());
		binaryImage.addActionListener(new Listener.engineListener());
		compresion.addActionListener(new Listener.engineListener());
		
		rendererGroup.add(colorScale);
		rendererGroup.add(binaryImage);
		rendererGroup.add(grayScale);
		rendererGroup.add(compresion);

		renderMenu.add(colorScale);
		renderMenu.add(grayScale);
		renderMenu.add(binaryImage);
		renderMenu.add(compresion); 
		return renderMenu;
	}
	
	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");

		openImage = new JMenuItem("Open Image");
		saveImage = new JMenuItem("Save Image");
		secondaryImage  = new JMenuItem("Copy Image to secondary variable");
		saveImage.setEnabled(false);
		openImage.addActionListener(new Listener.openImageListener());
		saveImage.addActionListener(new Listener.saveImageListener());
		secondaryImage.addActionListener(new Listener.saveAsSecondaryImageListener());
		fileMenu.add(openImage);
		fileMenu.add(saveImage);
		fileMenu.add(secondaryImage);

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
	
	private JMenu createFilterOptions()
	{
		JMenu Options;
		
		Options = new JMenu("Filter");
		
		//AA options ********************************************************************
		
		negateFilter = new JRadioButtonMenuItem("Negate");
		Options.add(negateFilter);
		rendererGroup.add(negateFilter);
		umbral = new JRadioButtonMenuItem("Umbralization");
		contrast = new JRadioButtonMenuItem("Increase contrast");
		gamma = new JRadioButtonMenuItem("Gamma Correction");
		exponencialN= new JRadioButtonMenuItem("Add Exponential Noise");
		gaussianN= new JRadioButtonMenuItem("Add Gaussian Noise");
		rayleighN= new JRadioButtonMenuItem("Add Rayleigh Noise");
		saltnpepperN= new JRadioButtonMenuItem("Add Salt N' Pepper Noise");
		histogram = new JRadioButtonMenuItem("Histogram");
		equalize = new JRadioButtonMenuItem("Equalize");
		sum = new JRadioButtonMenuItem("Sum");
		product = new JRadioButtonMenuItem("Product");
		dotProduct = new JRadioButtonMenuItem("Dot Product");
		applyMatrix = new JRadioButtonMenuItem("Apply Matrix");
		median = new JRadioButtonMenuItem("Median mask");
		sobel = new JRadioButtonMenuItem("Borders");
		
		substraction = new JRadioButtonMenuItem("Substraction");
		rendererGroup.add(equalize);
		rendererGroup.add(histogram);
		rendererGroup.add(contrast);
		rendererGroup.add(gamma);
		rendererGroup.add(substraction);
		rendererGroup.add(product);
		rendererGroup.add(dotProduct);
		rendererGroup.add(applyMatrix);
		rendererGroup.add(median);
		rendererGroup.add(exponencialN);
		rendererGroup.add(rayleighN);
		rendererGroup.add(gaussianN);
		rendererGroup.add(saltnpepperN);

		rendererGroup.add(sobel);
		rendererGroup.add(sum);
		rendererGroup.add(umbral);

		umbral.addActionListener(new Listener.umbralListener());

		contrast.addActionListener(new Listener.contrastListener());
		gamma.addActionListener(new Listener.GammaListener());
		exponencialN.addActionListener(new Listener.ExponencialNoiseListener());
		rayleighN.addActionListener(new Listener.RayleighNoiseListener());
		gaussianN.addActionListener(new Listener.GaussianNoiseListener());
		saltnpepperN.addActionListener(new Listener.SaltnPepperNoiseListener());
		histogram.addActionListener(new Listener.histogramListener());
		equalize.addActionListener(new Listener.equalizeListener());
		sum.addActionListener(new Listener.sumListener());
		dotProduct.addActionListener(new Listener.dotProductListener());
		applyMatrix.addActionListener(new Listener.MatrixListener());
		sobel.addActionListener(new Listener.BorderListener());
		Options.add(contrast);
		Options.add(gamma);
		Options.add(histogram);
		Options.add(equalize);
		Options.add(sum);
		Options.add(product);
		Options.add(dotProduct);
		Options.add(substraction);
		Options.add(umbral);
		Options.add(median);
		Options.add(applyMatrix);
		Options.add(sobel);
		Options.add(exponencialN);
		Options.add(rayleighN);
		Options.add(gaussianN);
		Options.add(saltnpepperN);
		
		return Options;
		
	}
	
	private JMenu createDOFOptions()
	{
		JMenu Options;
		
		Options = new JMenu("TP3");
		matrix = new JRadioButtonMenuItem("Borders detectors");
		dofIterations = new JRadioButtonMenuItem("Umbralization global");
		dofDispersion = new JRadioButtonMenuItem("Umbralization Otzu");
		dofsharpPlane = new JRadioButtonMenuItem("Set Sharp Plane");
		
		matrix.addActionListener(new Listener.matrixListener());
		dofIterations.addActionListener(new Listener.setDOFIterationsListener());
		dofDispersion.addActionListener(new Listener.setDOFDispersionListener());
		dofsharpPlane.addActionListener(new Listener.setDOFSharpPlane());
		
		rendererGroup.add(matrix);

		Options.add(matrix);
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
		return negateFilter;
	}


	public JRadioButtonMenuItem getStochastic() {
		return histogram;
	}
	
	
	public JRadioButtonMenuItem getEqualized() {
		return equalize;
	}


	public JRadioButtonMenuItem getAdaptive() {
		return sum;
	}


	public JMenuItem getAaBounds() {
		return contrast;
	}


	public JRadioButtonMenuItem getOrderOrder() {
		return orderedOrder;
	}
	
	
	
}
