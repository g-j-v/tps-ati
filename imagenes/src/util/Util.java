/**
 * 
 */
package util;

import gui.Frame.AlertType;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Main;

import core.Settings;

/**
 * @author dazar
 * 
 */
public class Util {

	public static Logger logger = Logger.getLogger(Util.class.toString());
	private static final int MAXRGB = 255;

	public Util() {
		// TODO Auto-generated constructor stub
	}

	public static BufferedImage loadImage(String image) {
		BufferedImage img = null;
		if (image.contains(".raw") || image.contains(".RAW")) {
			createRawDialog(image);
			return img;
		}
		if (image.contains(".pgm") || image.contains(".PGM")) {
			return loadPgmImage(image);
		}
		if (image.contains(".ppm") || image.contains(".PPM")) {
			return LoadPpmImage(image);
		}

		try {
			img = ImageIO.read(new File(image));
		} catch (IOException e) {
			logger.info("Error loading image");
		}
		return img;
	}

	public static BufferedImage LoadPpmImage(String image) {
		PPMFile f = new PPMFile(image);
		char[][] bytes = f.GetBytes();
		// construct Image from binary PPM color data.
		int width = bytes[0].length / 3;
		int height = bytes.length;
		int pix[] = new int[width * height];
		int index = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < bytes[0].length; x += 3) {
				pix[index++] = 255 << 24 /* alpha */| bytes[y][x] << 16 /* R */
						| bytes[y][x + 1] << 8 /* G */| bytes[y][x + 2] /* B */;
			}
		}
		// BufferedImage img = (new Component()).createImage(new
		// MemoryImageSource(width, height, pix, 0, width));
		// repaint();

		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		img.setRGB(0, 0, width, height, pix, 0, 0);
	
		img.setRGB(0, 0, width, height, pix, 0, width);

		return img;

	}

	private static void createRawDialog(String image) {
		final String lastImageUrl = image;
		if (Main.getFrame().getButtons().isRendering()) {
			Main.getFrame().ShowDialog(
					"Cant change image while rendering! Press Stop!",
					"Rendering", AlertType.ERROR);
			return;
		}
		final JFrame frame = new JFrame("Set Size");
		final JFormattedTextField tfW = new JFormattedTextField(
				NumberFormat.getNumberInstance());
		final JFormattedTextField tfH = new JFormattedTextField(
				NumberFormat.getNumberInstance());
		tfW.setValue(Settings.getResolution().x);
		tfH.setValue(Settings.getResolution().y);
		JButton confirm = new JButton("Confirm");
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int w = Integer.valueOf(tfW.getValue().toString());
				int h = Integer.valueOf(tfH.getValue().toString());
				// BufferedImage im = new BufferedImage(w, h,
				// BufferedImage.TYPE_4BYTE_ABGR);
				/*
				 * DataBuffer db = new DataBufferByte(loadRawImage(lastImageUrl,
				 * w, h), w*h); SampleModel sm = new
				 * SinglePixelPackedSampleModel(DataBuffer.TYPE_BYTE, w, h, new
				 * int[] {0xFF}); WritableRaster raster =
				 * Raster.createWritableRaster(sm, db, null); Main.setImage(new
				 * BufferedImage(BufferedImage., raster, false, null));
				 */

				BufferedImage img = new BufferedImage(w, h,
						BufferedImage.TYPE_BYTE_GRAY);

				// Get the writable raster so that data can be changed.
				WritableRaster wr = img.getRaster();

				// Now write the byte data to the raster
				wr.setDataElements(0, 0, w, h, loadRawImage(lastImageUrl, w, h));
				Main.setImage(img);
				frame.dispose();
			}
		});
		tfW.setColumns(10);
		tfH.setColumns(10);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));
		panel.add(new JLabel("Width:"));
		panel.add(tfW);
		panel.add(new JLabel("Height"));
		panel.add(tfH);

		frame.add(confirm, BorderLayout.SOUTH);
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	private static BufferedImage loadPgmImage(String image) {
		final String lastImageUrl = image;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(lastImageUrl);
			Scanner scan = new Scanner(fileInputStream);
			// Discard the magic number
			scan.nextLine();
			// Discard the comment line
			// scan.nextLine();
			// Read pic width, height and max value
			int picWidth = scan.nextInt();
			int picHeight = scan.nextInt();
			@SuppressWarnings("unused")
			int maxvalue = scan.nextInt();

			fileInputStream.close();

			// Now parse the file as binary data
			fileInputStream = new FileInputStream(lastImageUrl);
			DataInputStream dis = new DataInputStream(fileInputStream);

			// look for 4 lines (i.e.: the header) and discard them
			int numnewlines = 3;
			while (numnewlines > 0) {
				char c;
				do {
					c = (char) (dis.readUnsignedByte());
				} while (c != '\n');
				numnewlines--;
			}

			byte resp[] = new byte[picWidth * picHeight];
			int k = 0;
			for (int i = 0; i < picWidth; i++) {
				for (int j = 0; j < picHeight; j++) {
					resp[k++] = (byte) dis.read();
					// System.out.print(resp[k-1] + " ");
				}
				// System.out.println();

			}
			BufferedImage img = new BufferedImage(picWidth, picHeight,
					BufferedImage.TYPE_BYTE_GRAY);

			// Get the writable raster so that data can be changed.
			WritableRaster wr = img.getRaster();

			// Now write the byte data to the raster
			wr.setDataElements(0, 0, picWidth, picHeight, resp);
			return img;
		} catch (FileNotFoundException e) {
			System.out.println(e);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}

		return null;
	}

	// Dado el nombre del archivo, su ancho y alto, devuelve un arreglo de
	// floats
	// donde se ha cargado la info de pixels
	// Esta funci—n lee pixeles de longitud 1 byte, y no espera ningun header en
	// el archivo. Es decir, sirve para cargar archivos RAW en escala de grises.

	private static byte[] loadRawImage(String filename, int w, int h) {
		byte resp[] = new byte[w * h];

		try {
			FileInputStream inp = new FileInputStream(filename);

			int k = 0;
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					resp[k++] = (byte) inp.read();
				}
			}

		} catch (IOException ie) {
			System.out.println("Error:" + ie.getMessage());
		}
		return resp;
	}

	public static void saveImage(String path) {
		String extension = "";
		File file = new File(path);
		try {
			if (path.contains(".bmp"))
				extension = "bmp";
			else if (path.contains(".png"))
				extension = "png";
			else
				extension = "jpg";
			ImageIO.write(Main.image, extension, file);
			Main.getFrame().ShowDialog(
					"File " + path + " was saved succesfully!", "Image Saved",
					AlertType.INFO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setPixel(int x, int y, int red, int green, int blue) {
		if (x < 0 || x >= Settings.getResolution().x || y < 0
				|| y >= Settings.getResolution().y) {
			Main.getFrame().ShowDialog("Coordinates out of range!",
					"Rendering", AlertType.ERROR);
			return;
		}

		if (red < 0 || red > MAXRGB || blue < 0 || blue > MAXRGB || green < 0
				|| green > MAXRGB) {
			Main.getFrame()
					.ShowDialog(
							"RGB values out of range, must be a value greater than 0 and less than 255!",
							"Rendering", AlertType.ERROR);
			return;
		}

		Main.getImage().setRGB(x, y,
				0xFF000000 | (red << 16) | (green << 8) | blue);

	}

	public static BufferedImage getSubimage(BufferedImage image, int x, int y,
			int w, int h) throws IllegalArgumentException {
		if (x < 0 || x >= Settings.getResolution().x || y < 0
				|| y >= Settings.getResolution().y) {
			Main.getFrame().ShowDialog("Coordinates out of range!",
					"Copying sub image", AlertType.ERROR);
			throw new IllegalArgumentException();
		}

		if (w < 0 || w + x >= Settings.getResolution().x || h < 0
				|| h + y >= Settings.getResolution().y) {
			Main.getFrame().ShowDialog("Width or height out of range!",
					"Copying sub image", AlertType.ERROR);
			throw new IllegalArgumentException();
		}
		return image.getSubimage(x, y, w, h);
	}

}
