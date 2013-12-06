package cs355.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import cs355.model.ImageManager;

public class ViewRefresherImpl implements ViewRefresher {

	private ImageManager imageManager;
	
	public ViewRefresherImpl() {
		imageManager = ImageManager.getInstance();
	}

	@Override
	public void refreshView(Graphics2D g2d) {
		if (imageManager.getBufferedImage() == null)
			return;
		BufferedImage openImage = imageManager.getBufferedImage();
		WritableRaster raster = openImage.getRaster();
		List<int[]> pixels = new ArrayList<int[]>();
		for (int x = 0; x < raster.getWidth(); x++) {
			for (int y = 0; y < raster.getHeight(); y++) {
				int[] pixel = new int[3];
				raster.getPixel(x, y, pixel);
				pixels.add(pixel);
			}
		}
		g2d.drawImage(imageManager.getBufferedImage(), null, 0, 0);
	}
}
