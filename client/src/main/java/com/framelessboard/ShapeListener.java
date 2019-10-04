package com.framelessboard;

import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

public class ShapeListener extends MouseInputAdapter {
    public AppFrame myApp;
    
    ShapeListener(AppFrame myApp) {
        this.myApp = myApp;
    }
    
    @Override
    public void mousePressed(MouseEvent event) {
        myApp.drawShapeStart(event.getX(), event.getY());
    }
    
    @Override
    public void mouseReleased(MouseEvent event) {
        myApp.drawShapeEnd(event.getX(), event.getY());
        myApp.storeShape();
        myApp.getCanvasPanel().repaint();
        System.out.println(myApp.getDrawType() + " " + myApp.drawings.size());
    }
}