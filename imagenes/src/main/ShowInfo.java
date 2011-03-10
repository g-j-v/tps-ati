package main;

import javax.imageio.ImageIO;

public class ShowInfo {

	/*

reader BMP
reader bmp
reader jpeg
reader wbmp
reader gif
reader png
reader JPG
reader jpg
reader WBMP
reader JPEG
writer BMP
writer bmp
writer jpeg
writer wbmp
writer png
writer JPG
writer PNG
writer jpg
writer WBMP
writer JPEG
	 */

	static public void main(String args[]) throws Exception {
		String names[] = ImageIO.getReaderFormatNames();
		for (int i = 0; i < names.length; ++i) {
			System.out.println("reader " + names[i]);
		}

		names = ImageIO.getWriterFormatNames();
		for (int i = 0; i < names.length; ++i) {
			System.out.println("writer " + names[i]);
		}
	}
}
