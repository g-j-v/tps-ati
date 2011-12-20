package util;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
		
		int intValues[][] = new int[width][height];

		for(int i=1; i < width - 1 ; i++ ){
			for(int j=1; j < height - 1; j++ ){
				int[][] mask = new int[3][3];
				for (int x = i -1, maskx = 0; x < i - 1 + mask.length; x++, maskx++) {
					for (int y = j-1, masky = 0; y < j - 1 + mask[0].length; y++, masky++) {
						//System.out.println("(" + x + "," + y +")");
						mask[maskx][masky] = intensity[x][y];
					}
				}
				int value = calculateValue(mask);
				intValues[i][j] = value;
			}
		}
		
		for(int i=2; i < width - 2 ; i++ ){
			for(int j=2; j < height - 2; j++ ){
				intensity[i][j] = intValues[i][j];
			}
		}

	}
	
	protected static int calculateValue(int[][] mask) {
	/*	int ans = 0;
		for (int i = 0; i < mask.length; i++) {
			for (int j = 0; j < mask[i].length; j++) {
				ans += mask[i][j];
			}
		}
		return (int) Math.floor(ans * 1/9);*/
		List<Integer> values = new ArrayList<Integer>(mask.length
				* mask[0].length);
		for (int i = 0; i < mask.length; i++) {
			for (int j = 0; j < mask[0].length; j++) {
				values.add(mask[i][j]);
			}
		}
		Collections.sort(values);
		int median = 0;
		if (values.size() % 2 == 0) {
			int v1 = values.get(values.size() / 2);
			int v2 = values.get(values.size() / 2 - 1);
			median = (int) (Math.floor((double) (v1 + v2)) / 2);
		} else {
			median = values.get(values.size() / 2);
		}
		return median;
	}
	
	public int getColor(int x, int y){
		return intensity[x][y];
	}
	
	


}
