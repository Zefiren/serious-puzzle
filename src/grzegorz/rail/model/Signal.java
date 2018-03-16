package grzegorz.rail.model;

public class Signal extends Interactable<Signal>{

	private int id;
	private TrackSection signalTC;
	private TrackSection protectingTC;
	private boolean isClear;
	private Direction direction;

//	private SignalGraphic signalGraphic;

	public Signal(int id, TrackSection signalTC, TrackSection protectingTC, Direction direction) {
		super();
		this.setId(id);
		this.setSignalTC(signalTC);
		this.setProtectingTC(protectingTC);
		this.setDirection(direction);
//		signalGraphic = new SignalGraphic(50, facingLeft);
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

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public Signal getInteractable() {
		// TODO Auto-generated method stub
		return this;
	}


}
