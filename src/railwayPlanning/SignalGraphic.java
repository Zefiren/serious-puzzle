package railwayPlanning;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class SignalGraphic {
	private Ellipse2D.Double circleLeft, circleRight;
	private Color colourClear = Color.GREEN;
	private Color colourDanger = Color.RED;
	private Point graphicPosition;
	private int width;

	
	private Rectangle labelBox;
	private Point textPlace;
	
	public SignalGraphic(int widthSize) {
		width = widthSize;
		setCircleLeft(new Ellipse2D.Double(0, 0,width/2 , width /2));
		setCircleRight(new Ellipse2D.Double(width/2, 0,width/2 , width /2));
		
		labelBox = new Rectangle(75,10,60,20);
		textPlace = new Point(100,30);
		// TODO Auto-generated constructor stub
	}
	public Ellipse2D.Double getCircleLeft() {
		return circleLeft;
	}
	public void setCircleLeft(Ellipse2D.Double circleLeft) {
		this.circleLeft = circleLeft;
	}
	public Ellipse2D.Double getCircleRight() {
		return circleRight;
	}
	public void setCircleRight(Ellipse2D.Double circleRight) {
		this.circleRight = circleRight;
	}
	public Color getColourDanger() {
		return colourDanger;
	}
	public void setColourDanger(Color colourDanger) {
		this.colourDanger = colourDanger;
	}
	public Color getColourClear() {
		return colourClear;
	}
	public void setColourClear(Color colourClear) {
		this.colourClear = colourClear;
	}
	public Rectangle getLabelBox() {
		return labelBox;
	}
	public void setLabelBox(Rectangle labelBox) {
		this.labelBox = labelBox;
	}
	public Point getTextPlace() {
		return textPlace;
	}
	public void setTextPlace(Point textPlace) {
		this.textPlace = textPlace;
	}
	
	public Point getSignalPosition() {
		return graphicPosition;
	}
	
	public void setSignalPosition(Point position) {
		graphicPosition = position;
		setCircleLeft(new Ellipse2D.Double(graphicPosition.x, graphicPosition.y,width/2 , width /2));
		setCircleRight(new Ellipse2D.Double(graphicPosition.x + width/2, graphicPosition.y,width/2 , width /2));
	}
	public Point getLabelBoxPoint() {
		return labelBox.getLocation();
	}
	
	public void setLabelBoxPoint(Point pos) {
		labelBox.setLocation(pos);
	}
	
	
}