package cs355.model;

import java.awt.image.BufferedImage;

public class ImageManager {
	
	public int height;
	public int width;
	public int[][] pixels;
	
	public BufferedImage openImage;
	
	public ImageManager(int height, int width, int[][] pixels) {
		this.height = height;
		this.width = width;
		this.pixels = pixels;
	}
	
	public ImageManager() {
	}
	
	private static ImageManager instance = null;
	
	public static ImageManager getInstance() {
		if (instance == null)
			instance = new ImageManager();
		return instance;
	}
	
	public void setBufferedImage(BufferedImage openImage) {
		this.openImage = openImage;
	}
	
	public BufferedImage getBufferedImage() { return this.openImage; }

}
