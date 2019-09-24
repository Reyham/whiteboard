import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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
        //this will be for freehand - use switch cases of if elses to decide how to repaint.
        switch (myApp.getDrawType()) {
        case "Freehand":
            for (int i = 1; i < myApp.getPoints().size(); i++) {
                Point p1 = myApp.getPoints().get(i - 1);
                Point p2 = myApp.getPoints().get(i);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            break;
        case "Rectangle":
            int width = myApp.lastReleaseX - myApp.lastPressX;
            int height = myApp.lastReleaseY - myApp.lastPressY;
            g.drawRect(myApp.lastPressX, myApp.lastPressY, width, height);
            break;
        default:
            System.out.println("Uhh what drawing is that?");
            break;
        }
    }    
}