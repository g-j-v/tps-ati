package util;
import java.awt.Color;
import java.awt.image.BufferedImage;


public class HSVArray {

	public int[][] intensity;
	public int height;
	public int width;
	
	
	public HSVArray(BufferedImage image) {
		super();
		height = image.getHeight();
		width = image.getWidth();
		
		intensity = new int[width][height];
		
		for(int i=0; i < width ; i++ ){
			for(int j=0; j < height ; j++ ){
				int rgb = image.getRGB(i, j); // Returns by default ARGB.
				int red = (rgb >>> 16) & 0xFF;
				int green = (rgb >>> 8) & 0xFF;
				int blue = (rgb >>> 0) & 0xFF;
				float[] hsb = Color.RGBtoHSB(red, green, blue, null);
				float hue = hsb[0]; 
				float saturation = hsb[1];
				float brightness = hsb[2];
//				intensity[i][j] = (int)( (255.0 * hue) + (255.0 * saturation)+ (255.0 * brightness)  ) /3;
				intensity[i][j] = red + blue + green;
				
				//System.out.println(intensity[i][j]);
			}
		}
			

	}
	
	public int getColor(int x, int y){
		return intensity[x][y];
	}


}
