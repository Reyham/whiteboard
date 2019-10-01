package com.framelessboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class AppFrame implements Runnable {
    
    public JFrame frame;
    public CanvasPanel myCanvasPanel;
    
    public String currentDrawType;
    
    // these variables to be used for freehand.
    public List<CustomDrawing> drawings;
    private List<Point> freeHandBuffer;
    
    // these variables to be used for shapes.
    public int lastPressX;
    public int lastPressY;
    public int lastReleaseX;
    public int lastReleaseY;
    
    AppFrame() {
        this.drawings = new ArrayList<CustomDrawing>();
        this.freeHandBuffer = new ArrayList<Point>();
        this.currentDrawType = "Freehand";
    }
    
    public List<Point> getPoints() {

        return freeHandBuffer;
    }
    
    public void drawPoint(int x, int y) {
        freeHandBuffer.add(new Point(x, y));
    }
    
    public void storePoints() {
        CustomDrawing drawing = new CustomDrawing(freeHandBuffer);
        drawings.add(drawing);
        freeHandBuffer.clear();
    }
    
    public void drawShapeStart(int x, int y) {
        this.lastPressX = x;
        this.lastPressY = y;
    }
    
    public void drawShapeEnd(int x, int y) {
        this.lastReleaseX = x;
        this.lastReleaseY = y;
    }
    
    public void storeShape() {
        CustomDrawing drawing = new CustomDrawing(this.lastPressX, this.lastPressY, this.lastReleaseX, this.lastReleaseY, this.currentDrawType);
        drawings.add(drawing);
    }
    
    
    @Override
    public void run() {
        frame = new JFrame("MyWhiteboard");
        
        myCanvasPanel = new CanvasPanel(this);
        myCanvasPanel.setBackground( new Color(100, 250, 250, 250) );


        //This has to be changed according to tool bar clicks.
        /////////////////////////////////////////////////////////
        FreehandListener myListener = new FreehandListener(this);
        ShapeListener myListener2 = new ShapeListener(this);
        /////////////////////////////////////////////////////////
        
        myCanvasPanel.addMouseMotionListener(myListener);
        myCanvasPanel.addMouseListener(myListener);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(myCanvasPanel);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
    
    public CanvasPanel getCanvasPanel() {
        return myCanvasPanel;
    }
    
    public String getDrawType() {
        return currentDrawType;
    }
    
}