package dirUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageLoader {
	private static String frameWord = "Frame";
	private String path;
	private String ext;
	private int initialFrame;
	private int maxFrame;
	List<String> images = new ArrayList<String>();
	public ImageLoader(File file) {
		try {
			this.path = file.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] aux = path.split("\\.");
		ext = aux[aux.length - 1];
		System.out.println(path);
		System.out.println(ext);
		
		String name = file.getName();
		
		System.out.println(name);
		System.out.println("---" + name.substring(5));
		initialFrame = Integer.parseInt(name.substring(5).split("\\.")[0]);
		System.out.println(initialFrame + "init frame");
		File dir = file.getParentFile();
		try {
			this.path = dir.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(dir.isDirectory()){
			for(String f: dir.list()){
				if(f.matches("Frame[0-9]+\\.[a-zA-Z]+")){
					images.add(f);
				}
			}
		}
		Collections.sort(images, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				if(o1.length() < o2.length())
					return -1;
				else if( o1.length() == o2.length())
					return o1.compareTo(o2);
				else
					return 1;
			}
		});
		this.maxFrame = images.size();
		System.out.println(images);
	}
	
	public int getInitFrame(){
		return initialFrame;
	}
	public int getMaxFrame(){
		return maxFrame;
	}
	
	public BufferedImage getFrame(int i) throws IOException{
		System.out.println("path" +path + "/" + images.get(i - 1) );
		return ImageIO.read(new File(path + "/" + images.get(i - 1)));
	}
}
