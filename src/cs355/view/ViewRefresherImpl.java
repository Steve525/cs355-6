package cs355.view;

import java.awt.Graphics2D;

import cs355.model.ImageManager;

public class ViewRefresherImpl implements ViewRefresher {

	private ImageManager imageManager;
	
	public ViewRefresherImpl() {
		imageManager = ImageManager.getInstance();
	}

	@Override
	public void refreshView(Graphics2D g2d) {
		g2d.drawImage(imageManager.getBufferedImage(), null, 0, 0);
	}
}
