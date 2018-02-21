package railwayPlanning;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class Switch extends TrackSection {
	
	private boolean isRightDirection;
	private boolean isTurnRight;
	private boolean isDiverging;
	private TrackSection extraTrack;

	
	private Rectangle labelBox;
	private Line2D.Double trackGraphic;
	private Polygon extraTrackGraphic;
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
		int y,ystart, xstart, xmid, xend;
		if(isRightDirection){
			xstart = 0;
			xmid = 30;
			xend = 200;

		}else{
			ystart = 10;
			xstart = 200;
			xmid = 170;
			xend = 0;
			
		}
		
		if(isTurnRight()){
			ystart = 10;
			y = 100;
		}
		else{
			ystart = -10;
			y = -100;
		}

		setExtraTrackGraphic(new Polygon(new int[] {xstart,xmid,xend},new int[] {ystart,y,y},3));//0, ystart, 200,y
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

	public Polygon getExtraTrackGraphic() {
		return extraTrackGraphic;
	}


	public void setExtraTrackGraphic(Polygon extraTrackGraphic) {
		this.extraTrackGraphic = extraTrackGraphic;
	}
	

	public void setExtraTrackGraphicPoints(int x1, int y1, int x2, int y2,int x3, int y3) {
		extraTrackGraphic.addPoint(x1, y1);
		extraTrackGraphic.addPoint(x2,y2);
		extraTrackGraphic.addPoint(x3, y3);
	}

}
