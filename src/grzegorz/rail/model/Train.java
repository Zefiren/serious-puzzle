package grzegorz.rail.model;

public class Train {

	private int trainID;
	private TrackSection source;
	private TrackSection destination;
	private TrackSection location;
	private TrackSection prevLocation;
	private Direction headingDirection;
	private boolean crashed;
	/**
	 * @param trainID
	 * @param source
	 * @param destination
	 * @param location
	 */
	public Train(int trainID, TrackSection source, TrackSection destination, Direction headingDirection) {
		super();
		this.trainID = trainID;
		this.source = source;
		this.destination = destination;
		this.location = source;
		this.headingDirection = headingDirection;
		crashed = false;
	}


	/**
	 * @return the source
	 */
	public TrackSection getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(TrackSection source) {
		this.source = source;
	}
	/**
	 * @return the destination
	 */
	public TrackSection getDestination() {
		return destination;
	}
	/**
	 * @param destination the destination to set
	 */
	public void setDestination(TrackSection destination) {
		this.destination = destination;
	}
	/**
	 * @return the location
	 */
	public TrackSection getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(TrackSection location) {
		if (location != null) {
			setPrevLocation(this.location);
			this.location = location;
		}
	}
	public TrackSection getPrevLocation() {
		return prevLocation;
	}


	private void setPrevLocation(TrackSection prevLocation) {
		this.prevLocation = prevLocation;
	}


	/**
	 * @return the trainID
	 */
	public int getTrainID() {
		return trainID;
	}

	/**
	 * @return the trainID
	 */
	public Direction getHeadingDirection() {
		return headingDirection;
	}


	/**
	 * @return the trainID
	 */
	public void setHeadingDirection(Direction headingDirection ) {
		this.headingDirection = headingDirection;
	}


	public boolean isCrashed() {
		return crashed;
	}


	public void setCrashed(boolean crashed) {
		this.crashed = crashed;
	}


}
