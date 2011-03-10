package gui;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 private JTextArea textArea = null;

	private JScrollPane scrollPane = null;
	private JDesktopPane pane =null;

	public LogFrame(JDesktopPane pane, String title, int width, int height) {
	    super(title, true, true, true , true);
	    //setMaximumSize(new Dimension(width, height));
	    textArea = new JTextArea();
	    this.pane = pane;
	    scrollPane = new JScrollPane(textArea);
	    scrollPane.setVisible(true);
	    add(scrollPane);
	    //setVisible(true);
	  }

	  /**
	   * This method appends the data to the text area.
	   * 
	   * @param data
	   *            the Logging information data
	   */
	  public void showInfo(String data) {
	    textArea.append(data);
	    pane.validate();
	  }
	}





