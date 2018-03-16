package grzegorz.rail.model;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

public class SignalGraphic {
	private Ellipse2D.Double circleLeft, circleRight;
	private Color colourClear = Color.GREEN;
	private Color colourDanger = Color.RED;
	private Point graphicPosition;
	private int width;

	
	private Rectangle labelBox;
	private Point textPlace;
	private Boolean facingLeft;
	
	public SignalGraphic(int widthSize, Boolean facingLeft) {
		width = widthSize;
		setCircleLeft(new Ellipse2D.Double(0, 0,width/2 , width /2));
		setCircleRight(new Ellipse2D.Double(width/2, 0,width/2 , width /2));
		
		labelBox = new Rectangle(75,10,60,20);
		textPlace = new Point(100,30);
		this.facingLeft = facingLeft;
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
	
	public Ellipse2D.Double getCircleProceed() {
		if(facingLeft)
			return circleLeft;
		else
			return circleRight;
	}
	public void setCircleProceed(Ellipse2D.Double circleProceed) {
		if(facingLeft)
			this.circleLeft = circleProceed;
		else
			this.circleRight = circleProceed;
	}
	public Ellipse2D.Double getCircleDanger() {
		if(!facingLeft)
			return circleLeft;
		else
			return circleRight;	
	}
	
	public void setCircleDanger(Ellipse2D.Double circleDanger) {
		if(!facingLeft)
			this.circleLeft= circleDanger;
		else
			this.circleRight = circleDanger;
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
