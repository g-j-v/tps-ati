package util;

public class Pixel {

	public int red;
	public int blue;
	public int green;
	
	
	public Pixel(int red, int blue, int green) {
		super();
		this.red = red;
		this.blue = blue;
		this.green = green;
	}
	
	public Pixel(int rgb) {
		super();
		red = (rgb >>> 16) & 0xFF;
		green = (rgb >>> 8) & 0xFF;
		blue = (rgb >>> 0) & 0xFF;
	}
	
}
