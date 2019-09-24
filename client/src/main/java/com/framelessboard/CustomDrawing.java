import java.awt.geom.*;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomDrawing implements Serializable{
    
    public Object drawing; // too generic should be changed.
    
    CustomDrawing(Object drawing) {
        this.drawing = drawing;
    }
    
    CustomDrawing(int x1, int y1, int x2, int y2, String shape) {
        int width = x2 - x1;
        int height = y2 - y1;
        if (shape.equals("Rectangle")) {
            this.drawing = new Rectangle(x1, y1, width, height);
        }
    }
    
    public Object getDrawing() {
        return drawing;
    }
    
    /*
    List<Point> x;
    
    {
        idk.
    }
    
    Shape;
    {
        start point x;
        start point y;
        
        end point x;
        end point y;
        
        type: rectangle, ellipse, circle, line, Freehand;
        
        color: color;
        
    }
    
    */
    
}