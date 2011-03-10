package gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ButtonPanel extends JPanel {

	private JButton Render;
	private boolean rendering;

	public ButtonPanel()
	{
		
		Render = new JButton("Render",new ImageIcon(ClassLoader.getSystemResource("eye.png")));

		rendering = false;
		Render.addMouseListener(new Listener.renderListener());
		Render.setEnabled(false);
		add(Render);
	}

	public JButton getRender() {
		return Render;
	}

	public boolean isRendering() {
		return rendering;
	}

	public void setRendering(boolean rendering) {
		this.rendering = rendering;
	}
	
	public void enableRender()
	{
		Render.setEnabled(true);
	}
	
	public void reset()
	{
		
		rendering = false;
		
		Render.setIcon(new ImageIcon(ClassLoader.getSystemResource("eye.png")));
		Render.setText("Render");
		
	}
	


}