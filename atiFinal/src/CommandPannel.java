import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dirUtils.ImageLoader;

public class CommandPannel extends JPanel {
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

				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
					}
				});
				// TODO Auto-generated method stub
				Thread t = new Thread("t") {
					@Override
					public void run() {
						for (int i = currentFrame; i <= imageLoader
								.getMaxFrame(); i++) {
							BufferedImage imagen;
							try {
								imagen = imageLoader.getFrame(i);
								try {
									Thread.sleep(41);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								imagePanel.setImage(imagen);
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

				if (currentFrame == 0) {
					back.setEnabled(false);
					return;
				}
				try {
					System.out.println("new frame" + currentFrame);
					BufferedImage imagen = imageLoader.getFrame(currentFrame);
					imagePanel.setImage(imagen);
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
				if (currentFrame == imageLoader.getMaxFrame()) {
					forward.setEnabled(false);
					return;
				}
				try {
					System.out.println("new frame" + currentFrame);
					BufferedImage imagen = imageLoader.getFrame(currentFrame);
					imagePanel.setImage(imagen);
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
