package com.framelessboard;


import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.geom.*;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;



public class CanvasPanel extends JPanel {
    
    public AppFrame myApp;
    
    public Color currentColor;
    
    CanvasPanel(AppFrame myApp) {
        this.myApp = myApp;
        this.currentColor = Color.BLUE;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(currentColor);
        Graphics2D g2 = (Graphics2D) g;
        JSONObject newMsg = new JSONObject();
        newMsg.put("Object", myApp.getDrawType());
        int width = 0;
        int height = 0;
        JSONObject testMsg;
        JSONObject testAction;
        //this will be for freehand - use switch cases of if elses to decide how to repaint.
        switch (myApp.getDrawType()) {
            case "Freehand":
                for (int i = 1; i < myApp.getPoints().size(); i++) {
                    Point p1 = myApp.getPoints().get(i - 1);
                    Point p2 = myApp.getPoints().get(i);
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
                addFreeAction(newMsg, (ArrayList<Point>) myApp.getPoints());
                break;
            case "Eraser":
                g.setColor(Color.WHITE);
                for (int i = 1; i < myApp.getPoints().size(); i++) {
                    Point p1 = myApp.getPoints().get(i - 1);
                    Point p2 = myApp.getPoints().get(i);
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
                addFreeAction(newMsg, (ArrayList<Point>) myApp.getPoints());
                break;
            case "Rectangle":
                width = Math.abs(myApp.lastReleaseX - myApp.lastPressX);
                height = Math.abs(myApp.lastReleaseY - myApp.lastPressY);
                if (myApp.lastPressX <= myApp.lastReleaseX && myApp.lastPressY <= myApp.lastReleaseY) {
                    g.drawRect(myApp.lastPressX, myApp.lastPressY, width, height);
                }
                else {
                    g.drawRect(myApp.lastReleaseX, myApp.lastReleaseY, width, height);
                }
                addShapeAction(newMsg, myApp.lastPressX, myApp.lastPressY, myApp.lastReleaseX, myApp.lastReleaseY);
                break;
            case "Ellipse":
                width = Math.abs(myApp.lastReleaseX - myApp.lastPressX);
                height = Math.abs(myApp.lastReleaseY - myApp.lastPressY);
                if (myApp.lastPressX <= myApp.lastReleaseX && myApp.lastPressY <= myApp.lastReleaseY) {
                    g2.draw(new Ellipse2D.Double(myApp.lastPressX, myApp.lastPressY, width, height));
                }
                else {
                    g2.draw(new Ellipse2D.Double(myApp.lastReleaseX, myApp.lastReleaseY, width, height));
                }
                addShapeAction(newMsg, myApp.lastPressX, myApp.lastPressY, myApp.lastReleaseX, myApp.lastReleaseY);
                break;
            case "Circle":
                width = Math.abs(myApp.lastReleaseX - myApp.lastPressX);
                height = Math.abs(myApp.lastReleaseY - myApp.lastPressY);
                if (width <= height) {
                    width = height;
                }
                else {
                    height = width;
                }
                if (myApp.lastPressX <= myApp.lastReleaseX && myApp.lastPressY <= myApp.lastReleaseY) {
                    g2.draw(new Ellipse2D.Double(myApp.lastPressX, myApp.lastPressY, width, height));
                }
                else {
                    g2.draw(new Ellipse2D.Double(myApp.lastReleaseX, myApp.lastReleaseY, width, height));
                }
                addShapeAction(newMsg, myApp.lastPressX, myApp.lastPressY, myApp.lastReleaseX, myApp.lastReleaseY);
                break;
            case "Draw all":
                //TODO: ADD Loop
                break;
            case "DrawShape":
                testMsg = new JSONObject();
                testMsg.put("Object", "Rectangle");
                addShapeAction(testMsg, 349,253,536,401);
                drawAction(g, testMsg.toString());
                break;
            case "DrawFreeHand":
                testMsg = new JSONObject();
                testMsg.put("Object", "Freehand");
                ArrayList<Point> points = new ArrayList<Point>();
                points.add(new Point(400,200));
                points.add(new Point(500,250));
                points.add(new Point(400,300));
                points.add(new Point(200,150));
                addFreeAction(testMsg, points);
                System.out.println(testMsg);
                drawAction(g, testMsg.toString());
                break;
            default:
                System.out.println("Uhh what drawing is that?");
                break;
        }
        System.out.println("Message:" + newMsg.toString());
    }



    public void addShapeAction(JSONObject newMsg, int lpx, int lpy, int lrx, int lry){
        JSONObject newAction = new JSONObject();
        newAction.put("lpx", lpx);
        newAction.put("lpy", lpy);
        newAction.put("lrx", lrx);
        newAction.put("lry", lry);
        newMsg.put("Action", newAction);
        System.out.println(newMsg);
    }

    public void addFreeAction(JSONObject newMsg, ArrayList<Point> points){
        JSONObject newAction = new JSONObject();
        ArrayList<ArrayList<Integer>> newPoints = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            ArrayList<Integer> point = new ArrayList<Integer>(2);
            point.add(p.x);
            point.add(p.y);
            newPoints.add(point);
        }
        newAction.put("Points", newPoints);
        newMsg.put("Action", newAction);
    }


    public void drawAction(Graphics g, String msg) {
        g.setColor(currentColor);
        Graphics2D g2 = (Graphics2D) g;
        JSONObject newMsg = new JSONObject(msg);
        String object = newMsg.getString("Object");
        JSONObject newAction = newMsg.getJSONObject("Action");
        int width = 0;
        int height = 0;
        int lrx = 0;
        int lry = 0;
        int lpx = 0;
        int lpy = 0;
        JSONArray points;
        //this will be for freehand - use switch cases of if elses to decide how to repaint.
        switch (object) {
            case "Freehand":
                points = (JSONArray) newAction.get("Points");
                for (int i = 1; i < points.length(); i++) {
                    JSONArray p1 = (JSONArray) points.get(i-1);
                    JSONArray p2 = (JSONArray) points.get(i);
                    g.drawLine( (Integer) p1.get(0), (Integer) p1.get(1), (Integer) p2.get(0), (Integer) p2.get(1));
                }
                break;
            case "Eraser":
                g.setColor(Color.WHITE);
                points = (JSONArray) newAction.get("Points");
                for (int i = 1; i < points.length(); i++) {
                    JSONArray p1 = (JSONArray) points.get(i-1);
                    JSONArray p2 = (JSONArray) points.get(i);
                    g.drawLine( (Integer) p1.get(0), (Integer) p1.get(1), (Integer) p2.get(0), (Integer) p2.get(1));
                }
                break;
            case "Rectangle":
                lrx = newAction.getInt("lrx");
                lry = newAction.getInt("lry");
                lpx = newAction.getInt("lpx");
                lpy = newAction.getInt("lpy");
                width = Math.abs(lrx - lpx);
                height = Math.abs(lry - lpx);
                if (lpx <= lrx && lpy <= lry) {
                    g.drawRect(lpx, lpy, width, height);
                }
                else {
                    g.drawRect(lrx, lry, width, height);
                }
                break;
            case "Ellipse":
                lrx = newAction.getInt("lrx");
                lry = newAction.getInt("lry");
                lpx = newAction.getInt("lpx");
                lpy = newAction.getInt("lpy");
                width = Math.abs(lrx - lpx);
                height = Math.abs(lry - lpx);
                if (lpx <= lrx && lpy <= lry) {
                    g2.draw(new Ellipse2D.Double(lpx, lpy, width, height));
                }
                else {
                    g2.draw(new Ellipse2D.Double(lrx, lry, width, height));
                }
                break;
            case "Circle":
                lrx = newAction.getInt("lrx");
                lry = newAction.getInt("lry");
                lpx = newAction.getInt("lpx");
                lpy = newAction.getInt("lpy");
                width = Math.abs(lrx - lpx);
                height = Math.abs(lry - lpx);
                if (width <= height) {
                    width = height;
                }
                else {
                    height = width;
                }
                if (lpx <= lrx && lpy <= lry) {
                    g2.draw(new Ellipse2D.Double(lpx, lpy, width, height));
                }
                else {
                    g2.draw(new Ellipse2D.Double(lrx, lry, width, height));
                }
                break;
            default:
                System.out.println("Uhh what drawing is that?");
                break;
        }
    }
}