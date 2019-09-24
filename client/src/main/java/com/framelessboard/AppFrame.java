import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class AppFrame implements Runnable {
    
    public JFrame frame;
    public CanvasPanel myCanvasPanel;
    
    public List<CustomDrawing> drawings;
    private List<Point> freeHandBuffer;
    
    AppFrame() {
        this.drawings = new ArrayList<CustomDrawing>();
        this.freeHandBuffer = new ArrayList<Point>();
    }
    
    public List<Point> getPoints() {
        return freeHandBuffer;
    }
    
    public void storePoints() {
        CustomDrawing drawing = new CustomDrawing(freeHandBuffer);
        drawings.add(drawing);
        freeHandBuffer.clear();
    }
    
    public void drawPoint(int x, int y) {
        freeHandBuffer.add(new Point(x, y));
    }
    
    @Override
    public void run() {
        frame = new JFrame("MyWhiteboard");
        
        myCanvasPanel = new CanvasPanel(this);
        myCanvasPanel.setBackground( new Color(100, 250, 250, 250) );
        
        FreehandListener myListener = new FreehandListener(this);
        
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
    
    
    public void drawRectangle() {
        
    }
    
    public void drawCircle() {
        
    }
    
    public void drawOval() {
        
    }
    
    public void drawLine() {
        
    }
    
}