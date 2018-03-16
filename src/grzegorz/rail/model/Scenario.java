package grzegorz.rail.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Scenario {

	private Map<Point,TrackSection> trackLayout = new HashMap<Point, TrackSection>();
	private Map<TrackSection,Signal> signals = new HashMap<TrackSection, Signal>();
	private List<Train> trains = new ArrayList<Train>();
	private int width;
	private int height;

	public Scenario(int width, int height) {
		// TODO Auto-generated constructor stub
		this.width = width;
		this.height = height;
	}

	//Add and Get TrackSections based on location
	public void addTrack(TrackSection ts) {
		trackLayout.put(ts.getLocation(), ts);
	}

	public TrackSection getTrack(Point loc) {
		return trackLayout.get(loc);
	}

	//Add and Get Signals based on TrackSection
	public void addSignal(Signal sig) {
		signals.put(sig.getSignalTC(), sig);
	}

	public Signal getSignal(TrackSection ts) {
		return signals.get(ts);
	}

	//WIDTH
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}


	//HEIGHT
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	//Get counts of tracks and signals
	public int TrackCount() {
		return trackLayout.size();
	}

	public int SignalCount() {
		return signals.size();
	}

	//Get TrackSection and Signal Sets for iterating over
	public Set<Entry<Point, TrackSection>> getTrackSet() {
		return trackLayout.entrySet();
	}

	public Set<Entry<TrackSection, Signal>> getSignalSet() {
		return signals.entrySet();
	}

	//Get and set trains in a scenario
	public List<Train> getTrains() {
		return trains;
	}

	public void setTrains(List<Train> trains) {
		this.trains = trains;
	}


	public void addTrain(Train tr) {
		trains.add(tr);
	}

	public Train removeTrain(int index) {
		return trains.remove(index);
	}

	public boolean removeTrain(Train tr) {
		return trains.remove(tr);
	}

	public Train getTrain(int index) {
		return trains.get(index);
	}

	public void setTrain(int index, Train tr) {
		trains.set(index, tr);
	}
}
