package grzegorz.rail.model;

public class Switch extends TrackSection {
	private int switchID;
	private Direction switchDirection;
	private Direction turnDirection;
	private boolean isDiverging;
	private TrackSection extraTrack;


//	private Polygon extraTrackGraphic;
//	private Point switchTextPlace;
//	private Rectangle switchLabelBox;



	/**
	 * @param isRightDirection
	 * @param isTurnRight
	 * @param extraTrack
	 */
	public Switch(
		int tsID,
		int switchID,
		TrackSection leftTrack,
		TrackSection rightTrack,
		Direction switchDirection,
		Direction turnDirection,
		TrackSection extraTrack)
		{
		super(tsID,  leftTrack,  rightTrack);
		this.switchID = switchID;
		this.switchDirection = switchDirection;
		this.turnDirection = turnDirection;
		this.extraTrack = extraTrack;
		isDiverging = false;

//		initTrackGraphic();
	}


	public int getSwitchID() {
		return switchID;
	}


	public void setSwitchID(int switchID) {
		this.switchID = switchID;
	}


	public Direction getSwitchDirection() {
		return switchDirection;
	}


	public void setSwitchDirection(Direction switchDirection) {
		this.switchDirection = switchDirection;
	}


	public Direction getTurnDirection() {
		return turnDirection;
	}


	public void setTurnDirection(Direction turnDirection) {
		this.turnDirection = turnDirection;
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

//
//	public void setExtraTrackGraphicPoints(int x1, int y1, int x2, int y2,int x3, int y3) {
//		extraTrackGraphic.addPoint(x1, y1);
//		extraTrackGraphic.addPoint(x2,y2);
//		extraTrackGraphic.addPoint(x3, y3);
//	}

	public Switch getInteractable() {
		return this;
	}




}
