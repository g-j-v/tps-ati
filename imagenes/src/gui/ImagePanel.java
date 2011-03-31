package gui;
import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BufferedImage  image;
	  public ImagePanel(BufferedImage image) {
		  this.image = image;
	  }

	  public void paint(Graphics g) {
	    g.drawImage( image, 0, 0, null);
	  }

}
