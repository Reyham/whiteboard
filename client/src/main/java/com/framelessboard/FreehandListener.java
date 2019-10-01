package com.framelessboard;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

public class FreehandListener extends MouseInputAdapter {
    public AppFrame myApp;
    
    FreehandListener(AppFrame myApp) {
        this.myApp = myApp;
    }
    
    @Override
    public void mouseDragged(MouseEvent event) {
        myApp.drawPoint(event.getX(), event.getY());
        myApp.getCanvasPanel().repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        myApp.storePoints();
        System.out.println(myApp.getDrawType() + " " + myApp.drawings.size());
    }
}