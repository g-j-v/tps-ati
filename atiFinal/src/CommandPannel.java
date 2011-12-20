import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;

import util.HSVArray;

import core.Background;

import dirUtils.ImageLoader;

public class CommandPannel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JButton back;
	private JButton forward;
	private JButton play;

	private ImagePanel imagePanel;
	private ImageLoader imageLoader;
	private int currentFrame;

	public CommandPannel(final ImagePanel imagePanel, ImageLoader loader) {
		back = new JButton("<<");
		play = new JButton("|>");
		forward = new JButton(">>");
		this.imagePanel = imagePanel;
		this.imageLoader = loader;
		this.currentFrame = loader.getInitFrame();

		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Background b = Background.getBackground();
				
				try {
					b.setCurrent(new HSVArray(imageLoader.getFrame(currentFrame)));
					b.getInitialContour();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Thread t = new Thread("t") {
					@Override
					public void run() {
						Background b = Background.getBackground();

						for (int i = currentFrame; i <= imageLoader
								.getMaxFrame(); i++) {
							BufferedImage imagen;
							try {
								imagen = imageLoader.getFrame(i);
								
									b.setCurrent(new HSVArray(imagen));
									//b.contour.cicle1();
									b.getInitialContour();
								try {
									Thread.sleep(41);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								imagePanel.setImage(imagen , i);
								repaint();

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};

				};
				t.start();
			}
		});

		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("old frame" + currentFrame);
				if (!forward.isEnabled()) {
					forward.setEnabled(true);
				}
				currentFrame--;

				
				try {
					Background b = Background.getBackground();
					b.setCurrent(new HSVArray(imageLoader.getFrame(currentFrame)));
					b.getInitialContour();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (currentFrame == 0) {
					back.setEnabled(false);
					return;
				}
				try {
					System.out.println("new frame" + currentFrame);
					BufferedImage imagen = imageLoader.getFrame(currentFrame);
					imagePanel.setImage(imagen, currentFrame);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		forward.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!back.isEnabled()) {
					back.setEnabled(true);
				}
				currentFrame++;
				try {
					Background b = Background.getBackground();
					b.setCurrent(new HSVArray(imageLoader.getFrame(currentFrame)));
					//b.contour.cicle1();
					b.getInitialContour();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				
				if (currentFrame == imageLoader.getMaxFrame()) {
					forward.setEnabled(false);
					return;
				}
				try {
					System.out.println("new frame" + currentFrame);
					BufferedImage imagen = imageLoader.getFrame(currentFrame);
					imagePanel.setImage(imagen, currentFrame);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		this.add(back);
		this.add(play);
		this.add(forward);

	}

	public void setFrame() {

	}

}
