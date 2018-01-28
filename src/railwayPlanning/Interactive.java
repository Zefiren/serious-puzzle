package railwayPlanning;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class Interactive  extends JPanel implements MouseListener{
	private static final long serialVersionUID = 1L;

	private Shape clickable;
	private Color colour;
	
	public Interactive(Shape shape, Color col) {
		clickable = shape;
		colour = col;
		
		addMouseListener(this);
		Rectangle bounds = shape.getBounds();
		setPreferredSize(new Dimension(bounds.getSize()));
		System.out.println(bounds);
	}

	@Override
	public void mouseClicked(MouseEvent e) {	
		if(clickable.contains(e.getPoint())) {
			System.out.println("you clicked me"+e.getPoint());
			colour = new Color(255-colour.getRed(), 255-colour.getGreen(), 255-colour.getBlue());
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        System.out.println("class is" + clickable.getClass());
        if(clickable.getClass() == Polygon.class) {
        	g2d.drawPolygon(((Polygon) clickable));
        	Color tmp = g2d.getColor();
        	
        	g2d.setColor(colour);
        	g2d.fill(clickable);
        	
        	g2d.setColor(tmp);
        	repaint();
        }
        else {
        	System.out.println("hello there");
        	System.out.println(g2d.getTransform().getTranslateX() + ", "+ g2d.getTransform().getTranslateY());
        	g2d.draw(clickable);
        }
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
}
