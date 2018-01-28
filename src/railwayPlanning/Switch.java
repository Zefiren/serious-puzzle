package railwayPlanning;

public class Switch extends TrackSection {
	
	private boolean isRightDirection;
	private boolean isTurnRight;
	private TrackSection extraTrack;

	/**
	 * @param extraTrack the extraTrack to set
	 */
	public void setExtraTrack(TrackSection extraTrack) {
		this.extraTrack = extraTrack;
	}

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
	}
	
	/**
	 * @return the isRightDirection
	 */
	public boolean isRightDirection() {
		return isRightDirection;
	}
	/**
	 * @return the isTurnRight
	 */
	public boolean isTurnRight() {
		return isTurnRight;
	}
	/**
	 * @return the extraTrack
	 */
	public TrackSection getExtraTrack() {
		return extraTrack;
	}

	
}
