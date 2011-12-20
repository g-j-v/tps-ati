import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.HSVArray;

import core.Background;

import dirUtils.ImageLoader;

public class CommandPannel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton back;
	private JButton forward;
	private JButton play;
	private JButton pause;
	private JSlider colorSlider;
	private JSlider errorSlider;
	private JLabel colorLabel = new JLabel("Color Tolerance");;
	private JLabel errorLabel = new JLabel("Threshold");
	@SuppressWarnings("unused")
	private ImagePanel imagePanel;
	private ImageLoader imageLoader;
	private int currentFrame;
	private JPanel sliders = new JPanel();
	private JPanel buttons = new JPanel();
	Thread t = new Thread();

	public CommandPannel(final ImagePanel imagePanel, ImageLoader loader) {
		setLayout(new GridBagLayout());
		back = new JButton("<<");
		play = new JButton("|>");
		pause = new JButton("[]");
		forward = new JButton(">>");
		colorSlider = new JSlider(JSlider.HORIZONTAL, 0, 255 * 3,
				Background.COLORDIFF);
		colorSlider.setMajorTickSpacing(10);
		colorSlider.setMinorTickSpacing(5);
		colorSlider.setPaintTicks(true);
		// colorSlider.setPaintLabels(true);
		colorSlider.setBackground(new Color(192, 204, 226));
		// colorSlider.setPreferredSize(new Dimension(600, 100));
		GridBagConstraints c = new GridBagConstraints();

		errorSlider = new JSlider(JSlider.HORIZONTAL, 0, 255 * 3,
				Background.THRESHOLD); // THRESHOLD
		errorSlider.setMajorTickSpacing(10);
		errorSlider.setMinorTickSpacing(5);
		errorSlider.setPaintTicks(true);
		// errorSlider.setPaintLabels(true);
		errorSlider.setBackground(new Color(192, 204, 226));
		// errorSlider.setPreferredSize(new Dimension(600, 100));
		colorSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					Background.COLORDIFF = source.getValue();
				}
			};
		});

		errorSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					Background.THRESHOLD = source.getValue();
				}
			};
		});
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		sliders.add(colorLabel);
		sliders.add(colorSlider);

		sliders.add(errorLabel);
		sliders.add(errorSlider);

		add(sliders, c);
		this.imagePanel = imagePanel;
		this.imageLoader = loader;
		this.currentFrame = loader.getInitFrame();

		pause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (t.isAlive())
					t.interrupt();

				try {
					Background b = Background.getBackground();
					b.setCurrent(new HSVArray(imageLoader
							.getFrame(currentFrame)));
					// b.contour.cicle1();
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

		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Background b = Background.getBackground();

				try {
					b.setCurrent(new HSVArray(imageLoader
							.getFrame(currentFrame)));
					b.getInitialContour();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				t = new Thread("t") {
					@Override
					public void run() {
						Background b = Background.getBackground();

						for (int i = currentFrame; i <= imageLoader
								.getMaxFrame(); i++) {
							BufferedImage imagen;
							long startTime = System.currentTimeMillis();
							try {
								imagen = imageLoader.getFrame(i);

								b.setCurrent(new HSVArray(imagen));
								// b.contour.cicle1();
								b.getInitialContour();
								startTime = System.currentTimeMillis()
										- startTime;
								startTime = 41 - startTime;
								System.out.println(startTime);

								if (startTime > 0) {
									try {
										Thread.sleep(startTime);
									} catch (InterruptedException e) {
										break;
									}
								}
								imagePanel.setImage(imagen, i);
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
					b.setCurrent(new HSVArray(imageLoader
							.getFrame(currentFrame)));
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
					b.setCurrent(new HSVArray(imageLoader
							.getFrame(currentFrame)));
					// b.contour.cicle1();
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
		c.fill = GridBagConstraints.NONE;
		buttons.add(back);
		buttons.add(play);

		buttons.add(pause);

		buttons.add(forward);
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		add(buttons, c);

		c.gridx = 2;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		add(imagePanel, c);

	}

	public void setFrame() {

	}

}
