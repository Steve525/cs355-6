package cs355.main;

import java.awt.Color;

import cs355.controller.CS355Controller;
import cs355.controller.CS355ControllerImpl;
import cs355.controller.MouseListener;
import cs355.ta.GUIFunctions;
import cs355.view.ViewRefresher;
import cs355.view.ViewRefresherImpl;


/**
 *
 * @author <Put your name here>
 */
public class CS355 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
    	CS355Controller controller = CS355ControllerImpl.getInstance();	// singleton
    	ViewRefresher viewRefresher = new ViewRefresherImpl();
    	MouseListener customListener = new MouseListener();
    	GUIFunctions.createCS355Frame(controller, viewRefresher, customListener, customListener);
        GUIFunctions.refresh();
        GUIFunctions.changeSelectedColor(Color.WHITE);
        GUIFunctions.setHScrollBarMin(0);
        GUIFunctions.setVScrollBarMin(0);
        GUIFunctions.setHScrollBarMax(512);
        GUIFunctions.setVScrollBarMax(512);
        GUIFunctions.setHScrollBarKnob(256);
        GUIFunctions.setVScrollBarKnob(256);
    }
}