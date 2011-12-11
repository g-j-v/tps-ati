import java.awt.Color;
import java.awt.image.BufferedImage;


public class HSVArray {

	public int[][] intensity;
	
	
	public HSVArray(BufferedImage image) {
		super();
		int height = image.getHeight();
		int width = image.getWidth();
		
		intensity = new int[width][height];
		
		for(int i=0; i < width ; i++ ){
			for(int j=0; i < height ; i++ ){
				int rgb = image.getRGB(i, j); // Returns by default ARGB.
				int red = (rgb >>> 16) & 0xFF;
				int green = (rgb >>> 8) & 0xFF;
				int blue = (rgb >>> 0) & 0xFF;
				float[] hsb = Color.RGBtoHSB(red, green, blue, null);
				float hue = hsb[0]; 
				float saturation = hsb[1];
				float brightness = hsb[2];
				intensity[i][j] = (int) (255.0 * brightness);
			}
		}
			

	}
	
	public int getColor(int x, int y){
		return intensity[x][y];
	}


}
