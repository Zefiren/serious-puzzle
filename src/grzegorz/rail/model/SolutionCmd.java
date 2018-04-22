package grzegorz.rail.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SolutionCmd {
	public enum CommandType {
		SwitchChange, SignalChange, CheckLocation
	};

	private CommandType type;

	Interactable<?> target;
	BooleanProperty oldValue;
	BooleanProperty newValue;
	IntegerProperty stepNumber;

	Train targetTrain;



	public SolutionCmd(Interactable<?> target, Boolean newValue) {
		super();
		this.target = target;
		this.oldValue = new SimpleBooleanProperty(!newValue);
		this.newValue = new SimpleBooleanProperty(newValue);
		if (target.getInteractable().getClass() == Signal.class) setType(CommandType.SignalChange);
		if (target.getInteractable().getClass() == Switch.class) {
			setType(CommandType.SwitchChange);
			System.out.println("switch id " + ((Switch) target).getTsID());
		}
	}

	public SolutionCmd(Interactable<?> target, Boolean newValue, Integer stepNum) {
		super();
		this.target = target;
		this.oldValue = new SimpleBooleanProperty(!newValue);
		this.newValue = new SimpleBooleanProperty(newValue);
		this.stepNumber = new SimpleIntegerProperty(stepNum);
		if (target.getInteractable().getClass() == Signal.class) setType(CommandType.SignalChange);
		if (target.getInteractable().getClass() == Switch.class) {
			setType(CommandType.SwitchChange);
			System.out.println("switch id " + ((Switch) target).getSwitchID());
		}
	}

	public SolutionCmd(Interactable<?> target, Train targetTrain, int stepNum) {
		super();
		this.target = target;
		this.targetTrain = targetTrain;
		this.stepNumber = new SimpleIntegerProperty(stepNum);
		setType(CommandType.CheckLocation);
	}

	public StringProperty getStep() {
		switch (getType()) {
		case SwitchChange:
			if (newValue.getValue()) {
				return new SimpleStringProperty("Switch : " + ((Switch) target).getSwitchID() + " -> Open" );
			}else {
				return new SimpleStringProperty("Switch : " + ((Switch) target).getSwitchID() + " -> Closed");
			}
		case SignalChange:
			if (newValue.getValue()) {
				return new SimpleStringProperty("Signal : " + ((Signal) target).getId() + " -> Proceed" );
			}else {
				return new SimpleStringProperty("Signal : " + ((Signal) target).getId() + " -> Stop" );
			}
		case CheckLocation:
			return new SimpleStringProperty("Occupies : TC" + ((TrackSection) target).getTsID() + " by Train " + targetTrain.getDestination().getLabel());
		default:
			return new SimpleStringProperty("no known type");
		}

	}

	public void undoStep() {
		switch (getType()) {
		case SwitchChange:
			System.out.println(((Switch) target).getSwitchID() + " is the id");
			((Switch) target).setDiverging(oldValue.getValue());
			break;
		case SignalChange:
			System.out.println(((Signal) target).getId() + " is the id");
			((Signal) target).setClear(oldValue.getValue());
			break;
		case CheckLocation:
			System.out.println("");
			break;
		default:
			System.out.println("");
		}
	}

	public boolean performStep() {
		switch (getType()) {
		case SwitchChange:
			System.out.println(((Switch) target).getSwitchID() + " is the id");
			((Switch) target).setDiverging(newValue.getValue());
			return true;
		case SignalChange:
			System.out.println(((Signal) target).getId() + " is the id set to" + newValue);
			((Signal) target).setClear(newValue.getValue());
			return true;
		case CheckLocation:
			System.out.println(" ");
			if (targetTrain.getLocation() == target) return true;
			else return false;
		default:
			System.out.println("");
			return true;
		}
	}

	public String getNewValueString() {
		switch (getType()) {
		case SwitchChange:
			if (newValue.getValue()) {
				return "Open";
			}else {
				return "Closed";
			}
		case SignalChange:
			if (newValue.getValue()) {
				return "Proceed";
			}else {
				return "Stop";
			}
		case CheckLocation:
			return "";
		default:
			return "";
		}
	}

	public IntegerProperty getStepNumber() {
		return stepNumber;
	}

	public StringProperty getStepNumberString() {
		return new SimpleStringProperty(stepNumber.getValue() + "");
	}

	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = new SimpleIntegerProperty(stepNumber);
	}

	public StringProperty getStepType() {
		return new SimpleStringProperty( (getType() == CommandType.CheckLocation) ? " WHEN" : " SET" ) ;
	}

	public CommandType getType() {
		return type;
	}

	public void setType(CommandType type) {
		this.type = type;
	}


}
