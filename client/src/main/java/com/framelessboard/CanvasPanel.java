package com.framelessboard;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
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
        int width = 0;
        int height = 0;
        //this will be for freehand - use switch cases of if elses to decide how to repaint.
        switch (myApp.getDrawType()) {
        case "Freehand":
            for (int i = 1; i < myApp.getPoints().size(); i++) {
                Point p1 = myApp.getPoints().get(i - 1);
                Point p2 = myApp.getPoints().get(i);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            break;
        case "Eraser":
            g.setColor(Color.WHITE);
            for (int i = 1; i < myApp.getPoints().size(); i++) {
                Point p1 = myApp.getPoints().get(i - 1);
                Point p2 = myApp.getPoints().get(i);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
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
            break;
        case "Line":
            g2.draw(new Line2D.Double(myApp.lastPressX, myApp.lastPressY, myApp.lastReleaseX, myApp.lastReleaseY));
            break;
        case "Text":
            g2.drawString("YEET", myApp.lastPressX, myApp.lastPressY);
            break;
        case "DrawAll":
            //g.drawFile();
            try {
                myApp.open(myApp.fileName);
                for (CustomDrawing cd: myApp.drawings){
                    drawAction(g, cd);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            break;
        default:
            System.out.println("Uhh what drawing is that?");
            break;
        }
    }

    public void drawAction(Graphics g, CustomDrawing drawing) {
        Graphics2D g2 = (Graphics2D) g;
        String object = drawing.getDrawing().getString("Object");
        JSONObject newAction = drawing.getDrawing().getJSONObject("Action");
        String color = newAction.getString("color");
        g.setColor(Color.getColor(color));
        int width = 0;
        int height = 0;
        int lrx = 0;
        int lry = 0;
        int lpx = 0;
        int lpy = 0;
        String text;
        JSONArray points;
        //this will be for freehand - use switch cases of if elses to decide how to repaint.
        switch (object) {
            case "Freehand":
            case "Eraser":
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
            case "Line":
                lrx = newAction.getInt("lrx");
                lry = newAction.getInt("lry");
                lpx = newAction.getInt("lpx");
                lpy = newAction.getInt("lpy");
                g2.draw(new Line2D.Double(lpx, lpy, lrx, lry));
                break;
            case "Text":
                text = newAction.getString("text");
                lpx = newAction.getInt("lpx");
                lpy = newAction.getInt("lpy");
                g2.drawString(text, lpx, lpy);
                break;
            default:
                System.out.println("Uhh what drawing is that?");
                break;
        }
    }
}