package railwayPlanning;

public class Switch extends TrackSection {
	private int switchID;
	private boolean isRightDirection;
	private boolean isTurnRight;
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
		boolean isRightDirection,
		boolean isTurnRight,
		TrackSection extraTrack)
		{
		super(tsID,  leftTrack,  rightTrack);
		this.switchID = switchID;
		this.isRightDirection = isRightDirection;
		this.isTurnRight = isTurnRight;
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


	public boolean isRightDirection() {
		return isRightDirection;
	}
//
//	private void initTrackGraphic() {
//		setTrackGraphic(new Line2D.Double(0,0,Surface.trackLengthStraight,0));
//		setTrackColour(Color.BLUE);
//		int y,ystart, xstart, xmid, xend;
//		if(isRightDirection){
//			xstart = 0;
//			xmid = 30;
//			xend = 200;
//
//		}else{
//			ystart = 10;
//			xstart = 200;
//			xmid = 170;
//			xend = 0;
//
//		}
//
//		if(isTurnRight()){
//			ystart = 10;
//			y = 100;
//		}
//		else{
//			ystart = -10;
//			y = -100;
//		}
//		int labelY=0 ;
//		System.out.println("switch init:" + ystart + " and " + y);
//		if(y > ystart)
//			labelY = y;
//		else
//			labelY = ystart;
//		labelBox = new Rectangle((int)(Surface.trackLengthStraight*0.375),labelY + 10,40,20);
//		textPlace = new Point((int)(Surface.trackLengthStraight*0.5)-labelBox.width/2,labelY + 25);
//
//		switchLabelBox = new Rectangle((int)(Surface.trackLengthStraight*0.375),labelY + 30,40,20);
//		switchTextPlace = new Point((int)(Surface.trackLengthStraight*0.5)-labelBox.width/2,labelY + 45);
//
//		setExtraTrackGraphic(new Polygon(new int[] {xstart,xmid,xend},new int[] {ystart,y,y},3));//0, ystart, 200,y
//		setTrackColour(Color.BLUE);
//	}


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
