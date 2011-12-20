import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import core.Background;

import util.HSVArray;

import dirUtils.ImageLoader;

public class ImagePanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	public Image img;
	public HSVArray array;
	public Frame frame;
	public Background background;

	public ImagePanel(String img) {
		this(new ImageIcon(img).getImage());

		try {

			BufferedImage imagen;
			imagen = ImageIO.read(new File(img));
			array = new HSVArray(imagen);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private ImagePanel(Image img) {
		this.img = img;
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		addMouseListener(this);
		setLayout(null);

	}

	public void setImage(BufferedImage img , int frameNumber){
		this.img = img;
		background = Background.getBackground();
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		addMouseListener(this);
		setLayout(null);
		System.out.println(frameNumber);
		if(frameNumber == 1)
		
		 if(frame != null)
		 {
			 Point aux = frame.getMidPoint();
				int auxColor = array.getColor(aux.x, aux.y);
						 array = new HSVArray(img);
			 frame = new Frame(array, aux.x, aux.y, auxColor );
			 System.out.println("new Center x " + aux.x + " y " + aux.y);
				
			 frame.cicle1();
			 frame.clean();
		 }
		 this.repaint();
		 
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
		if (background != null) {
			g.setColor(Color.GREEN);

			g.setColor(Color.RED);
			for (Point p : background.contour.Lin) {
				g.fillRect(p.x, p.y, 1, 1);
				//System.out.println("Lin x " + p.x + " y " + p.y);
			}
			g.setColor(Color.GREEN);
			for (Point p : background.contour.Lout) {
				g.fillRect(p.x, p.y, 1, 1);
				//System.out.println("Lout x " + p.x + " y " + p.y);
			}

		}
	}

	public void mousePressed(MouseEvent e) {
		// System.out.println("Mouse pressed (# of clicks: ");
	}

	public void mouseReleased(MouseEvent e) {
		// System.out.println("Mouse released (# of clicks: ");
		if(background == null)
			background = Background.getBackground();
		background.setColor(e.getX(), e.getY() );
		System.out.println("x " + e.getX() + " y " + e.getY());
		frame = new Frame(array, e.getX(), e.getY(), array.getColor(e.getX(), e.getY()));
		// frame.floodFill(new Point(e.getX(), e.getY()));
//		frame.cicle1();
//		frame.clean();
//		// ImageLoader.Loader.getFrame(i)
//		System.out.println(frame.getMidPoint());
		repaint();
	}

	public void mouseEntered(MouseEvent e) {
		// System.out.println("Mouse entered");
	}

	public void mouseExited(MouseEvent e) {
		// System.out.println("Mouse exited");
	}

	public void mouseClicked(MouseEvent e) {
		// System.out.println("Mouse clicked (# of clicks");
	}

}