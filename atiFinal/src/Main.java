import java.io.File;


import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 */

/**
 * @author Dani
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame();

		
		//Create a file chooser
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(frame);
		File selFile = fc.getSelectedFile();
		JPanel panel = new ImagePanel(selFile.getAbsolutePath());
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

}
