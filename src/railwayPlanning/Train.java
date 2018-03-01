package railwayPlanning;

import java.awt.Rectangle;

public class Train {
	
	private int trainID;
	private TrackSection source;
	private TrackSection destination;
	private TrackSection location;
	private Rectangle trainBox;
	/**
	 * @param trainID
	 * @param source
	 * @param destination
	 * @param location
	 */
	public Train(int trainID, TrackSection source, TrackSection destination, TrackSection location) {
		super();
		this.trainID = trainID;
		this.source = source;
		this.destination = destination;
		this.location = location;
		setTrainBox(new Rectangle(0, 0, 40, 30));
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
		this.location = location;
	}
	/**
	 * @return the trainID
	 */
	public int getTrainID() {
		return trainID;
	}


	public Rectangle getTrainBox() {
		return trainBox;
	}


	public void setTrainBox(Rectangle trainBox) {
		this.trainBox = trainBox;
	}
}
