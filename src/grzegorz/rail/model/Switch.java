package grzegorz.rail.model;

public class Switch extends TrackSection {
	private int switchID;
	private Direction switchDirection;
	private Direction turnDirection;
	private boolean isDiverging;
	private TrackSection extraTrack;




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


	public Switch getInteractable() {
		return this;
	}




}
