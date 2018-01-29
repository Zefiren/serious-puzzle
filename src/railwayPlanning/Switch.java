package railwayPlanning;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class Switch extends TrackSection {
	
	private boolean isRightDirection;
	private boolean isTurnRight;
	private boolean isDiverging;
	private TrackSection extraTrack;

	
	private Rectangle labelBox;
	private Line2D.Double trackGraphic;
	private Line2D.Double extraTrackGraphic;
	private Color trackColour;
	private Point textPlace;
	
	

	/**
	 * @param isRightDirection
	 * @param isTurnRight
	 * @param extraTrack
	 */
	public Switch(
		int tsID, 
		TrackSection leftTrack, 
		TrackSection rightTrack, 
		boolean isRightDirection, 
		boolean isTurnRight, 
		TrackSection extraTrack) 
		{
		super(tsID,  leftTrack,  rightTrack);
		this.isRightDirection = isRightDirection;
		this.isTurnRight = isTurnRight;
		this.extraTrack = extraTrack;
		isDiverging = false;
		
		initTrackGraphic();
	}
	
	
	public boolean isRightDirection() {
		return isRightDirection;
	}
	
	private void initTrackGraphic() {
		setTrackGraphic(new Line2D.Double(0,0,200,0));
		setTrackColour(Color.BLUE);
		labelBox = new Rectangle(75,10,50,20);
		textPlace = new Point(100,30);		
		int y,ystart;
		if(isRightDirection){
			if(isTurnRight())
				ystart = 5;
			else
				ystart = -5;
		}else{
			ystart = 200;
		}
		if(isTurnRight())
			y = 100;
		else
			y = -100;
		setExtraTrackGraphic(new Line2D.Double(0, ystart, 200,y));
		setTrackColour(Color.BLUE);
		labelBox = new Rectangle(75,10,50,20);
		textPlace = new Point(100,30);
	}

	public boolean isTurnRight() {
		return isTurnRight;
	}

	public boolean isDiverging() {
		return isDiverging;
	}


	public void setDiverging(boolean isDiverging) {
		this.isDiverging = isDiverging;
	}


	public TrackSection getExtraTrack() {
		return extraTrack;
	}
	

	public void setExtraTrack(TrackSection extraTrack) {
		this.extraTrack = extraTrack;
	}

	public Line2D.Double getExtraTrackGraphic() {
		return extraTrackGraphic;
	}


	public void setExtraTrackGraphic(Line2D.Double extraTrackGraphic) {
		this.extraTrackGraphic = extraTrackGraphic;
	}
	

	public void setExtraTrackGraphicPoints(int x1, int y1, int x2, int y2) {
		extraTrackGraphic.setLine(x1, y1, x2, y2);
	}

}
