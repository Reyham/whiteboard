import org.json.JSONObject;
import java.awt.geom.*;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class CustomDrawing {
    
    public JSONObject drawing;
    
    // Constructor for shapes - lines, rectangles, ellipses and circles.
    CustomDrawing(String drawType, String color, int lpx, int lpy, int rpx, int rpy) {
        drawing.put("Object", drawType);
        JSONObject attributes = new JSONObject();
        attributes.put("color", color);
        attributes.put("lpx", lpx);
        attributes.put("lpy", lpy);
        attributes.put("lrx", lrx);
        attributes.put("lry", lry);
        drawing.put("Action", attributes);
    }
    
    // Constructor for freehand drawings - freehand lines and erasers.
    CustomDrawing(String drawType, String color, ArrayList<ArrayList<Integer>> points) {
        drawing.put("Object", drawType);
        JSONObject attributes = new JSONObject();
        attributes.put("color", color);
        attributes.put("Points", points);
        drawing.put("Action", attributes);
    }
    
    // Constructor for text drawings.
    CustomDrawing(String drawType, String color, int lpx, int lpy, String text) {
        drawing.put("Object", drawType);
        JSONObject attributes = new JSONObject();
        attributes.put("color", color);
        attributes.put("lpx", lpx);
        attributes.put("lpy", lpy);
        attributes.put("text", text);
    }
    
    public JSONObject getDrawing() {
        return drawing;
    }
    
}