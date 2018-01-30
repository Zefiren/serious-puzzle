package railwayPlanning;

public class Signal {
	
	private int id;
	private TrackSection signalTC;
	private TrackSection protectingTC;
	private boolean isClear;

	public Signal(int id, TrackSection signalTC, TrackSection protectingTC, boolean isClear) {
		super();
		this.setId(id);
		this.setSignalTC(signalTC);
		this.setProtectingTC(protectingTC);
		this.setClear(isClear);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TrackSection getSignalTC() {
		return signalTC;
	}

	public void setSignalTC(TrackSection signalTC) {
		this.signalTC = signalTC;
	}

	public TrackSection getProtectingTC() {
		return protectingTC;
	}

	public void setProtectingTC(TrackSection protectingTC) {
		this.protectingTC = protectingTC;
	}

	public boolean isClear() {
		return isClear;
	}

	public void setClear(boolean isClear) {
		this.isClear = isClear;
	}
}
