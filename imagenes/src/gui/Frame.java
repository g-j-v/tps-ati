package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class Frame extends JFrame implements WindowListener {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 760;
	private int PANELWIDTH = 906;
	private int PANELHEIGHT = 680;
	private int LOGWIDTH = 380;
	private int LOGHEIGHT = 680;
	private int LOGX = 950;
	private int LOGY = 280;
	private Menu menu;
	private Panel panel;
	private ButtonPanel buttons;

	private static  Logger logger;
	private static JInternalFrame logFrame;
	private static JDesktopPane pane;

	public Frame() {
		setTitle("Renderer Grupo 1");
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		menu = new Menu();
		setJMenuBar(menu);
	
		createLoggerFrame();
	
		createPanels();
		addWindowListener(this);
		pack();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		logFrame.setVisible(true);
		

	}

	private void createLoggerFrame()
	{
		pane = new JDesktopPane();
		pane.setBounds(LOGX, LOGY, LOGWIDTH,LOGHEIGHT);
		pane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		
		//pane.setMinimumSize(new Dimension(LOGWIDTH,LOGHEIGHT));
		pane.setBackground(Color.BLACK);
	
		add(pane);
		
		logger = LoggerWindowHandler.getLogger("Frame");
		logFrame = LoggerWindowHandler.getInternalFrame(Frame.pane);

		pane.add(logFrame);
		//logFrame.setPreferredSize(new Dimension(LOGWIDTH, LOGHEIGHT));

		logFrame.pack();
		
		try {
			logFrame.setMaximum(true);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void createPanels()
	{
		panel = new Panel();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		panel.setBorder(border);

	//	panel.setMaximumSize(new Dimension(PANELWIDTH, PANELHEIGHT));
		panel.setPreferredSize(new Dimension(PANELWIDTH, PANELHEIGHT));
		
		buttons = new ButtonPanel();

		add(buttons,BorderLayout.NORTH);
		add(panel, BorderLayout.WEST);
	}
	public void UpdatePanel() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				panel.repaint();
			}

		});
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		
		logFrame.dispose();
		dispose();
		logger.info("Renderer is exiting!");
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				System.exit(0);
			}

		});

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public Menu getMenu() {
		return menu;
	}

	public Panel getPanel() {
		return panel;
	}

	public ButtonPanel getButtons() {
		return buttons;
	}

	public enum AlertType {
		ERROR, INFO, CONFIRM
	}

	public void ShowDialog(String message, String title, AlertType type) {
		if (type == AlertType.CONFIRM)
			JOptionPane.showConfirmDialog(this, message);
		if (type == AlertType.INFO)
			JOptionPane.showMessageDialog(this, message, title,
					JOptionPane.INFORMATION_MESSAGE);
		if (type == AlertType.ERROR)
			JOptionPane.showMessageDialog(this, message, title,
					JOptionPane.ERROR_MESSAGE);
	}
	
	public static Logger getLogger() {
		return logger;
	}

}
