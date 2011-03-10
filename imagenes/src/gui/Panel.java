package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import core.Settings;


import main.Main;

@SuppressWarnings("serial")
public class Panel extends JPanel {


	public Panel()
	{
		super();
		setOpaque(true);
		setDoubleBuffered(true);
		setBackground(Color.BLACK);
		
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(Main.getImage() != null)
		{

			if(!Main.getFrame().getMenu().getFitToSize().isSelected())
				g.drawImage(Main.getImage(),0, 0, new ImObserver());
			else
				g.drawImage(Main.getImage(),0,0, getWidth(),getHeight(), 0, 0,(int) Settings.getResolution().x,(int)Settings.getResolution().y, new ImObserver());
			
		}
		else
		{
			int type = AlphaComposite.SRC_OVER;
			AlphaComposite alpha = AlphaComposite.getInstance(type, 0.1f);
			Graphics2D g2D = (Graphics2D)g;
			g2D.setComposite(alpha);
			BufferedImage image;
			try {
				image = ImageIO.read(ClassLoader.getSystemResource("back.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			int x = (int) ((getWidth() - image.getWidth()) / 2.0f);
			int y = (int) ((getHeight() - image.getHeight()) / 2.0f);
			g2D.drawImage(image, x, y, this);
		}
		
		
		
		
	}
	
	class ImObserver implements ImageObserver{

		@Override
		public boolean imageUpdate(Image img, int infoflags, int x, int y,
				int width, int height) {
			if(infoflags == ALLBITS)
				return true;
			else
				return false;
		}
		
	}
	
	
}
