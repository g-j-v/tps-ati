import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dirUtils.ImageLoader;

public class MainClass {

	/**
	 * Main class of the program
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("This is my first JFrame");
		frame.setSize(300, 200);
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(frame);
		File selFile = fc.getSelectedFile();
		ImagePanel panel2 = new ImagePanel(selFile.getAbsolutePath());
		ImageLoader loader = new ImageLoader(selFile);
		JPanel panel = new CommandPannel(panel2,loader);
		
		
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
