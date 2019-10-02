package com.framelessboard;

import org.json.JSONObject;

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

    public JSONObject addShapeAction(String color){
        JSONObject newMsg = new JSONObject();
        newMsg.put("Object", getDrawType());
        JSONObject newAction = new JSONObject();
        newAction.put("lpx", this.lastPressX);
        newAction.put("lpy", this.lastPressY);
        newAction.put("lrx", this.lastReleaseX);
        newAction.put("lry", this.lastReleaseY);
        newAction.put("color", color);
        newMsg.put("Action", newAction);
        System.out.println(newMsg);
        return newMsg;
    }

    public JSONObject addFreeAction(String color){
        JSONObject newMsg = new JSONObject();
        newMsg.put("Object", getDrawType());
        JSONObject newAction = new JSONObject();
        ArrayList<ArrayList<Integer>> newPoints = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < freeHandBuffer.size(); i++) {
            Point p = freeHandBuffer.get(i);
            ArrayList<Integer> point = new ArrayList<Integer>(2);
            point.add(p.x);
            point.add(p.y);
            newPoints.add(point);
        }
        newAction.put("Points", newPoints);
        newAction.put("color", color);
        newMsg.put("Action", newAction);
        System.out.println(newMsg);
        return newMsg;
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