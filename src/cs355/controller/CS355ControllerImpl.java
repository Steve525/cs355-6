package cs355.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Iterator;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void vScrollbarChanged(int value) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSharpen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doMedianBlur() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doUniformBlur() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doChangeContrast(int contrastAmountNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doChangeBrightness(int brightnessAmountNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doLoadImage(BufferedImage openImage) {
		WritableRaster raster = openImage.getRaster();
		ColorModel colorModel = openImage.getColorModel();
		imageManager.setBufferedImage(openImage);
	}

	@Override
	public void toggleBackgroundDisplay() {
		// TODO Auto-generated method stub
		
	}

}
