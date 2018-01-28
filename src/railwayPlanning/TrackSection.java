package railwayPlanning;

import java.awt.Color;
import java.awt.geom.Line2D;

import javax.swing.JLabel;


public class TrackSection  {
	
	
	private int tsID;
	private String label;
	private boolean isEndTrack;
	private boolean isRightEnding;
	private TrackSection leftTrack;
	private TrackSection rightTrack;
	
	private JLabel tcLabel;
	private Line2D.Double trackGraphic;
	private Color trackColour;


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
		
		initTrackGraphic();
	}
	
	public TrackSection(int tsID, TrackSection leftTrack, TrackSection rightTrack) {
		this.tsID = tsID;
		this.leftTrack = leftTrack;
		this.rightTrack = rightTrack;
		
		initTrackGraphic();
	}
	
	public TrackSection(int tsID) {
		this.tsID = tsID;
		
		initTrackGraphic();
	}
	
	private void initTrackGraphic() {
		setTrackGraphic(new Line2D.Double(0,0,200,0));
		setTrackColour(Color.BLACK);
		tcLabel = new JLabel("TC"+tsID);
	}
	
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

	public boolean isRightEnding() {
		return isRightEnding;
	}

	public Line2D.Double getTrackGraphic() {
		return trackGraphic;
	}

	public void setTrackGraphic(Line2D.Double trackGraphic) {
		this.trackGraphic = trackGraphic;
	}

	public void setTrackGraphicPoints(int x1, int y1, int x2, int y2) {
		trackGraphic.setLine(x1, y1, x2, y2);
	}

	
	public Color getTrackColour() {
		return trackColour;
	}

	public void setTrackColour(Color trackColour) {
		this.trackColour = trackColour;
	}

	public JLabel getTcLabel() {
		return tcLabel;
	}

	public void setTcLabel(JLabel tcLabel) {
		this.tcLabel = tcLabel;
	}
	
	


}
