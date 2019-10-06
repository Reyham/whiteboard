package com.framelessboard;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
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
    public HTTPConnect myHTTPConeect;
    public String username;
    
    public String fileName;
    public String currentDrawType;
    public String currentColor = "BLUE";
    
    // these variables to be used for freehand.
    public List<CustomDrawing> drawings;
    private List<Point> freeHandBuffer;
    
    // these variables to be used for shapes.
    public int lastPressX;
    public int lastPressY;
    public int lastReleaseX;
    public int lastReleaseY;
    
    // these variables to be used for texts.
    public String textBuffer;
    
    AppFrame() {
        this.drawings = new ArrayList<CustomDrawing>();
        this.freeHandBuffer = new ArrayList<Point>();
        this.textBuffer = "YEET";
        this.fileName = null;
        this.currentDrawType = "Freehand";
        this.username = "abc";
        this.myHTTPConeect = new HTTPConnect();
        this.myHTTPConeect.establishConnect(this.username);
    }
    
    public List<Point> getPoints() {
        return freeHandBuffer;
    }
    
    public void drawPoint(int x, int y) {
        freeHandBuffer.add(new Point(x, y));
    }
    
    public void drawShapeStart(int x, int y) {
        this.lastPressX = x;
        this.lastPressY = y;
    }
    
    public void drawShapeEnd(int x, int y) {
        this.lastReleaseX = x;
        this.lastReleaseY = y;
    }
    
    public void storePoints() {
        ArrayList<ArrayList<Integer>> newPoints = new ArrayList<ArrayList<Integer>>();
        
        for (int i = 0; i < freeHandBuffer.size(); i++) {
            Point p = freeHandBuffer.get(i);
            ArrayList<Integer> point = new ArrayList<Integer>(2);
            point.add(p.x);
            point.add(p.y);
            newPoints.add(point);
        }
        System.out.println(currentDrawType);
        System.out.println(currentColor);
        System.out.println(newPoints);
        CustomDrawing drawing = new CustomDrawing(currentDrawType, currentColor, newPoints);
        myHTTPConeect.putCanvas(drawing.drawing);
        drawings.add(drawing);
        freeHandBuffer.clear();
    }
    
    public void storeShape() {
        CustomDrawing drawing = new CustomDrawing(currentDrawType, currentColor, lastPressX, lastPressY, lastReleaseX, lastReleaseY);
        myHTTPConeect.putCanvas(drawing.drawing);
        drawings.add(drawing);
    }
    
    public void storeText() {
        CustomDrawing drawing = new CustomDrawing(currentDrawType, currentColor, lastPressX, lastPressY, textBuffer);
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
        /*
        Tool bar clicks should also change:
        currentDrawType based on what shape/line/etc the user clicks on.
        currentColor based on what color user selects.
        fileName based on what name the user saves the file with - call the save() or saveAsDialog() functions.
        
        the mouse listeners below must change too:
        e.g. remove myListener and add myListener2 if a user clicks on rectangle,
        or remove myListener2 and add myListener if a user clisk on eraser.
        */
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
    
    public void save() throws FileNotFoundException {
        if (this.fileName == null) {
            //saveAsDialog();
        }
        else {
            try (PrintWriter out = new PrintWriter(this.fileName)) {
                List<JSONObject> jsonFormat = new ArrayList<>();
                for (CustomDrawing d : this.drawings) {
                    jsonFormat.add(d.getDrawing());
                }
                out.println(jsonFormat);
            }
        }
    }
    
    public void saveAsDialog() throws FileNotFoundException {
        // do the pop up thing
        save();
    }
    
    public void open(String file) throws FileNotFoundException {
        try {
            JSONArray a = new JSONArray(new FileReader(file));
            this.drawings.clear();
            for (Object o : a)
            {
                JSONObject d = (JSONObject) o;
                
                String drawType = (String) d.get("Object");
                
                JSONObject action = (JSONObject) d.get("Action");
                
                String color = (String) action.get("color");
                
                if (drawType.equals("Freehand") || drawType.equals("Eraser")) {
                    ArrayList<ArrayList<Integer>> points = (ArrayList<ArrayList<Integer>>) action.get("Points");
                    CustomDrawing newDrawing = new CustomDrawing(drawType, color, points);
                    drawings.add(newDrawing);
                }
                else if (drawType.equals("Text")) {
                    int lpx = (int) action.get("lpx");
                    int lpy = (int) action.get("lpy");
                    String text = (String) action.get("text");
                    CustomDrawing newDrawing = new CustomDrawing(drawType, color, lpx, lpy, text);
                    drawings.add(newDrawing);
                }
                else {
                    int lpx = (int) action.get("lpx");
                    int lpy = (int) action.get("lpy");
                    int rpx = (int) action.get("rpx");
                    int rpy = (int) action.get("rpy");
                    CustomDrawing newDrawing = new CustomDrawing(drawType, color, lpx, lpy, rpx, rpy);
                    drawings.add(newDrawing);
                }
                
            }
            
            this.getCanvasPanel().repaint();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    
}