package cs355.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cs355.model.ImageManager;
import cs355.ta.GUIFunctions;

public class CS355ControllerImpl implements CS355Controller {

	private static CS355ControllerImpl instance = null;
	
	public ButtonSelected buttonSelected;
	public Color colorSelected;
	public boolean on3D = true;
	
	public float x = 0;
	public float y = 0;
	public float z = 0;
	
	public float fovy = 60f;
	public float aspect = 800/600;
	public float zNear = 0.1f;
	public float zFar = 100f;
	
	private ImageManager imageManager;

	protected CS355ControllerImpl() {
		imageManager = ImageManager.getInstance();
	}
	
	public static CS355ControllerImpl getInstance() {
		if (instance == null) {
			instance = new CS355ControllerImpl();
		}
		return instance;
	}

	@Override
	public void colorButtonHit(Color c) {
		if (c != null) {
			GUIFunctions.changeSelectedColor(c);
			colorSelected = c;
			GUIFunctions.refresh();
		}
	}

	@Override
	public void triangleButtonHit() {
		this.buttonSelected = ButtonSelected.DRAW_TRIANGLE;
	}

	@Override
	public void squareButtonHit() {
		this.buttonSelected = ButtonSelected.DRAW_SQUARE;
	}

	@Override
	public void rectangleButtonHit() {
		this.buttonSelected = ButtonSelected.DRAW_RECTANGLE;
	}

	@Override
	public void circleButtonHit() {
		this.buttonSelected = ButtonSelected.DRAW_CIRCLE;
	}

	@Override
	public void ellipseButtonHit() {
		this.buttonSelected = ButtonSelected.DRAW_ELLIPSE;
	}

	@Override
	public void lineButtonHit() {
		this.buttonSelected = ButtonSelected.DRAW_LINE;
	}

	@Override
	public void selectButtonHit() {
		this.buttonSelected = ButtonSelected.SELECT;
	}

	@Override
	public void zoomInButtonHit() {
		this.buttonSelected = ButtonSelected.ZOOM_IN;
	}

	@Override
	public void zoomOutButtonHit() {
		this.buttonSelected = ButtonSelected.ZOOM_OUT;
	}

	@Override
	public void hScrollbarChanged(int value) {
	}

	@Override
	public void vScrollbarChanged(int value) {
	}

	// Initially it is set to render in 3D
	@Override
	public void toggle3DModelDisplay() {
		on3D = !on3D;
	}

	@Override
	public void keyPressed(Iterator<Integer> iterator) {
	}

	@Override
	public void doEdgeDetection() {
		if (imageManager.getBufferedImage() == null) return;
		WritableRaster raster = imageManager.getBufferedImage().getRaster();
		double[][] dx = new double[][] {{-1, 0, 1},
										{-2, 0, 2},
										{-1, 0, 1}};
		double[][] dy = new double[][] {{-1, -2, -1},
										{ 0,  0,  0},
										{ 1,  2,  1}};
		double[][] newPixels = new double[raster.getWidth()][raster.getHeight()];
		for (int x = 1; x < raster.getWidth() - 1; x++) {
			for (int y = 1; y < raster.getHeight() - 1; y++) {
				double[][] N = new double[3][3];
				N[2][2] = raster.getPixel(x+1, y+1, new double[3])[0];
				N[0][2] = raster.getPixel(x+1, y-1, new double[3])[0];
				N[2][1] = raster.getPixel(x, y+1, new double[3])[0];
				N[0][1] = raster.getPixel(x, y-1, new double[3])[0];
				N[2][0] = raster.getPixel(x-1, y+1, new double[3])[0];
				N[0][0] = raster.getPixel(x-1, y-1, new double[3])[0];
				N[1][1] = raster.getPixel(x, y, new double[3])[0];
				N[1][2] = raster.getPixel(x+1, y, new double[3])[0];
				N[1][0] = raster.getPixel(x-1, y, new double[3])[0];
				double xGradient = multipleAndSumXderivativeKernel(dx, N)/8;
				double yGradient = multipleAndSumXderivativeKernel(dy, N)/8;
				double magnitude = Math.sqrt(xGradient*xGradient + yGradient*yGradient);
				double[] pixel = new double[3];
//				magnitude += 128;
				pixel[0] = magnitude;
				if (pixel[0] > 255)
					pixel[0] = 255;
				else if (pixel[0] < 0)
					pixel[0] = 0;
				pixel[1] = pixel[2] = pixel[0];
				newPixels[x][y] = pixel[0];
			}
		}
		for (int x = 1; x < raster.getWidth() - 1; x++) {
			for (int y = 1; y < raster.getHeight() - 1; y++) {
				double[] pixel = new double[3];
				pixel[0] = pixel[1] = pixel[2] = newPixels[x][y];
				raster.setPixel(x, y, pixel);
			}
		}
		GUIFunctions.refresh();
	}
	
	private double multipleAndSumXderivativeKernel(double[][] D, double[][] N) {
		double a = 0;
		for (int i = 0; i < D.length; i++)
			   for (int j = 0; j < D.length; j++)
			         a += D[i][j] * N[i][j];
		return a;
	}

	@Override
	public void doSharpen() {
		if (imageManager.getBufferedImage() == null) return;
		WritableRaster raster = imageManager.getBufferedImage().getRaster();
		double[][] newPixels = new double[raster.getWidth()][raster.getHeight()];
		for (int x = 1; x < raster.getWidth() - 1; x++) {
			for (int y = 1; y < raster.getHeight() - 1; y++) {
				double weighted = 0;
				weighted += raster.getPixel(x, y+1, new double[3])[0];
				weighted += raster.getPixel(x, y-1, new double[3])[0];
				weighted += raster.getPixel(x+1, y, new double[3])[0];
				weighted += raster.getPixel(x-1, y, new double[3])[0];
				weighted *= -1;
				weighted += 6*raster.getPixel(x, y, new double[3])[0];
				weighted /= 2;
				if (weighted > 255)
					weighted = 255;
				else if (weighted < 0)
					weighted = 0;
				newPixels[x][y] = weighted;
			}
		}
		for (int x = 1; x < raster.getWidth() - 1; x++) {
			for (int y = 1; y < raster.getHeight() - 1; y++) {
				double[] pixel = new double[3];
				pixel[0] = pixel[1] = pixel[2] = newPixels[x][y];
				raster.setPixel(x, y, pixel);
			}
		}
		GUIFunctions.refresh();
	}

	@Override
	public void doMedianBlur() {
		if (imageManager.getBufferedImage() == null) return;
		WritableRaster raster = imageManager.getBufferedImage().getRaster();
		double[][] newPixels = new double[raster.getWidth()][raster.getHeight()];
		for (int x = 1; x < raster.getWidth() - 1; x++) {
			for (int y = 1; y < raster.getHeight() - 1; y++) {
				List<Double> mr_rogers = new ArrayList<Double>();
				mr_rogers.add(raster.getPixel(x+1, y+1, new double[3])[0]);
				mr_rogers.add(raster.getPixel(x+1, y-1, new double[3])[0]);
				mr_rogers.add(raster.getPixel(x, y+1, new double[3])[0]);
				mr_rogers.add(raster.getPixel(x, y-1, new double[3])[0]);
				mr_rogers.add(raster.getPixel(x-1, y+1, new double[3])[0]);
				mr_rogers.add(raster.getPixel(x-1, y-1, new double[3])[0]);
				mr_rogers.add(raster.getPixel(x, y, new double[3])[0]);
				mr_rogers.add(raster.getPixel(x+1, y, new double[3])[0]);
				mr_rogers.add(raster.getPixel(x-1, y, new double[3])[0]);
				Collections.sort(mr_rogers);
				double[] pixel = new double[3];
				pixel[0] = mr_rogers.get(mr_rogers.size()/2);
				if (pixel[0] > 255)
					pixel[0] = 255;
				else if (pixel[0] < 0)
					pixel[0] = 0;
				newPixels[x][y] = pixel[0];
			}
		}
		for (int x = 1; x < raster.getWidth() - 1; x++) {
			for (int y = 1; y < raster.getHeight() - 1; y++) {
				double[] pixel = new double[3];
				pixel[0] = pixel[1] = pixel[2] = newPixels[x][y];
				raster.setPixel(x, y, pixel);
			}
		}
		GUIFunctions.refresh();
	}

	@Override
	public void doUniformBlur() {
		if (imageManager.getBufferedImage() == null) return;
		WritableRaster raster = imageManager.getBufferedImage().getRaster();
		double[][] newPixels = new double[raster.getWidth()][raster.getHeight()];
		for (int x = 1; x < raster.getWidth() - 1; x++) {
			for (int y = 1; y < raster.getHeight() - 1; y++) {
				double total = 0;
				total += raster.getPixel(x+1, y+1, new double[3])[0];
				total += raster.getPixel(x+1, y-1, new double[3])[0];
				total += raster.getPixel(x, y+1, new double[3])[0];
				total += raster.getPixel(x, y-1, new double[3])[0];
				total += raster.getPixel(x-1, y+1, new double[3])[0];
				total += raster.getPixel(x-1, y-1, new double[3])[0];
				total += raster.getPixel(x, y, new double[3])[0];
				total += raster.getPixel(x+1, y, new double[3])[0];
				total += raster.getPixel(x-1, y, new double[3])[0];
				double average = total/9;
				newPixels[x][y] = average;
			}
		}
		for (int x = 1; x < raster.getWidth() - 1; x++) {
			for (int y = 1; y < raster.getHeight() - 1; y++) {
				double[] pixel = new double[3];
				pixel[0] = pixel[1] = pixel[2] = newPixels[x][y];
				raster.setPixel(x, y, pixel);
			}
		}
		GUIFunctions.refresh();
	}
	
	@Override
	public void doChangeContrast(int contrastAmountNum) {
		if (imageManager.getBufferedImage() == null)
			return;
		double c = contrastAmountNum;
		double q = (c + 100) / 100;
		WritableRaster raster = imageManager.getBufferedImage().getRaster();
		double[][] newPixels = new double[raster.getWidth()][raster.getHeight()];
		for (int x = 0; x < raster.getWidth(); x++) {
			for (int y = 0; y < raster.getHeight(); y++) {
				double[] pixel = new double[3];
				raster.getPixel(x, y, pixel);
				pixel[0] = q*q*q*q * (pixel[0] - 128) + 128;
				if (pixel[0] > 255)
					pixel[0] = 255;
				else if (pixel[0] < 0)
					pixel[0] = 0;
				newPixels[x][y] = pixel[0];
			}
		}
		for (int x = 1; x < raster.getWidth() - 1; x++) {
			for (int y = 1; y < raster.getHeight() - 1; y++) {
				double[] pixel = new double[3];
				pixel[0] = pixel[1] = pixel[2] = newPixels[x][y];
				raster.setPixel(x, y, pixel);
			}
		}
		GUIFunctions.refresh();
	}

	@Override
	public void doChangeBrightness(int brightnessAmountNum) {
		if (imageManager.getBufferedImage() == null)
			return;
		WritableRaster raster = imageManager.getBufferedImage().getRaster();
		for (int x = 0; x < raster.getWidth(); x++) {
			for (int y = 0; y < raster.getHeight(); y++) {
				double[] pixel = new double[3];
				raster.getPixel(x, y, pixel);
				if (pixel[0] + brightnessAmountNum > 255)
					pixel[0] = 255;
				else if (pixel[0] + brightnessAmountNum < -255)
					pixel[0] = -255;
				else
					pixel[0] += brightnessAmountNum;
				pixel[1] = pixel[0]; pixel[2] = pixel[0];
				raster.setPixel(x, y, pixel);
			}
		}
		GUIFunctions.refresh();
	}

	@Override
	public void doLoadImage(BufferedImage openImage) {
		imageManager.setBufferedImage(openImage);
		GUIFunctions.refresh();
	}

	@Override
	public void toggleBackgroundDisplay() {
	}

}
