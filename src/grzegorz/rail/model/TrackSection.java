package grzegorz.rail.model;

import java.awt.Point;


public class TrackSection  extends Interactable<TrackSection>{


	private int tsID;
	private String label;
	private boolean isEndTrack;
	private boolean isRightEnding;
	private TrackSection leftTrack;
	private TrackSection rightTrack;

	private Signal leftSignal;
	private Signal rightSignal;

	private Point location;


	/**
	 * @param tsID
	 * @param label
	 * @param isEndTrack
	 * @param leftTrack
	 * @param rightTrack
	 */
	public TrackSection(int tsID, String label, boolean isRightEnding, TrackSection nonEnding) {
		this.tsID = tsID;
		this.label = label;
		this.isRightEnding  = isRightEnding;
		isEndTrack = true;
		if(isRightEnding)
			this.leftTrack = nonEnding;
		else
			this.rightTrack = nonEnding;

//		initTrackGraphic();
	}

	public TrackSection(int tsID, TrackSection leftTrack, TrackSection rightTrack) {
		this.tsID = tsID;
		this.leftTrack = leftTrack;
		this.rightTrack = rightTrack;

//		initTrackGraphic();
	}

	public TrackSection(int tsID) {
		this.tsID = tsID;

//		initTrackGraphic();
	}
//
//	private void initTrackGraphic() {
//		setTrackGraphic(new Line2D.Double(0,0,Surface.trackLengthStraight,0));
//		setTrackColour(Color.BLUE);
////		labelBox = new Rectangle((int)(Surface.trackLengthStraight*0.375),10,40,20);
////		textPlace = new Point((int)(Surface.trackLengthStraight*0.5)-labelBox.width/2,25);
//	}

	/**
	 * @return the tsID
	 */
	public int getTsID() {
		return tsID;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @param isEndTrack the isEndTrack to set
	 */
	public void setEndTrack(boolean isEndTrack) {
		this.isEndTrack = isEndTrack;
	}

	/**
	 * @param isRightEnding the isRightEnding to set
	 */
	public void setRightEnding(boolean isRightEnding) {
		this.isRightEnding = isRightEnding;
	}

	/**
	 * @return the isEndTrack
	 */
	public boolean isEndTrack() {
		return isEndTrack;
	}
	/**
	 * @return the leftTrack
	 */
	public TrackSection getLeftTrack() {
		return leftTrack;
	}
	/**
	 * @return the rightTrack
	 */
	public TrackSection getRightTrack() {
		return rightTrack;
	}

	/**
	 * @param leftTrack the leftTrack to set
	 */
	public void setLeftTrack(TrackSection leftTrack) {
		this.leftTrack = leftTrack;
	}

	/**
	 * @param rightTrack the rightTrack to set
	 */
	public void setRightTrack(TrackSection rightTrack) {
		this.rightTrack = rightTrack;
	}

	/**
	 * @return the track wanted based on direction
	 */
	public TrackSection getTrack(Direction direction) {
		if(direction == Direction.right) {
			return getRightTrack();
		}
		if(direction == Direction.left) {
			return getLeftTrack();
		}
		return null;
	}

	/**
	 * @return the rightTrack
	 */
	public void setTrack(Direction direction, TrackSection track) {
		if(direction == Direction.right) {
			setRightTrack(track);
		}
		if(direction == Direction.left) {
			setLeftTrack(track);
		};
	}

	public boolean isRightEnding() {
		return isRightEnding;
	}




	public TrackSection getInteractable() {
		return this;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Signal getSignal(Direction direction) {
		if(direction == Direction.left) {
			return getLeftSignal();
		}
		if(direction == Direction.right) {
			return getRightSignal();
		}
		return null;

	}

	public void setSignal(Direction direction, Signal signal) {
		if(direction == Direction.left) {
			setLeftSignal(signal);
		}
		if(direction == Direction.right) {
			setRightSignal(signal);
		}

	}

	public Signal getLeftSignal() {
		return leftSignal;
	}

	public void setLeftSignal(Signal leftSignal) {
		this.leftSignal = leftSignal;
	}

	public Signal getRightSignal() {
		return rightSignal;
	}

	public void setRightSignal(Signal rightSignal) {
		this.rightSignal = rightSignal;
	}







}
