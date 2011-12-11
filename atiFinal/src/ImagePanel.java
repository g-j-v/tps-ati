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
import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

public class ImagePanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	public Image img;
	public HSVArray array;
	public Frame frame;

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

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
		if(frame != null)
			for (Point p : frame.Lout) {
				g.fillRect(p.x, p.y, 20, 20);				
			}
	}

	public void mousePressed(MouseEvent e) {
		// System.out.println("Mouse pressed (# of clicks: ");
	}

	public void mouseReleased(MouseEvent e) {
		// System.out.println("Mouse released (# of clicks: ");
		System.out.println("x " + e.getX() + " y " + e.getY());
		 frame = new Frame(array, e.getX(), e.getY());

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