package railwayPlanning;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SolutionCmd {
	enum CommandType {
			SwitchChange,
			SignalChange,
			CheckLocation
	};
	CommandType type;

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
		if(target.getInteractable().getClass() == Signal.class)
			type = CommandType.SignalChange;
		if(target.getInteractable().getClass() == Switch.class){
			type = CommandType.SwitchChange;
			System.out.println("switch id " + ((Switch)target).getTsID());
		}
	}

	public SolutionCmd(Interactable<?> target, Boolean newValue, Integer stepNum) {
		super();
		this.target = target;
		this.oldValue = new SimpleBooleanProperty(!newValue);
		this.newValue = new SimpleBooleanProperty(newValue);
		this.stepNumber = new SimpleIntegerProperty(stepNum);
		if(target.getInteractable().getClass() == Signal.class)
			type = CommandType.SignalChange;
		if(target.getInteractable().getClass() == Switch.class){
			type = CommandType.SwitchChange;
			System.out.println("switch id " + ((Switch)target).getTsID());
		}
	}

	public SolutionCmd(Interactable<?> target, Train targetTrain, int stepNum) {
		super();
		this.target = target;
		this.targetTrain = targetTrain;
		type = CommandType.CheckLocation;
	}


	public StringProperty getStep() {
		// TODO Auto-generated method stub
		switch (type) {
		case SwitchChange:
			return new SimpleStringProperty("Switch : " + ((Switch) target).getTsID() + " -> " + newValue.getValue() );
		case SignalChange:
			return new SimpleStringProperty("Signal : " + ((Signal) target).getId() + " ->" + newValue.getValue() );
		case CheckLocation:
			return new SimpleStringProperty("Track Section :" + ((TrackSection) target).getTsID() + " to be occupied by Train" + targetTrain.getTrainID());
		default:
			return new SimpleStringProperty("no known type");
		}


	}

	public void undoStep(){
		switch (type) {
		case SwitchChange:
			System.out.println(((Switch)target).getTsID()+ " is the id");
			((Switch)target).setDiverging(oldValue.getValue());
			break;
		case SignalChange:
			System.out.println(((Signal)target).getId()+ " is the id");
			((Signal)target).setClear(oldValue.getValue());
			break;
		case CheckLocation:
			System.out.println("");
			break;
		default:
			System.out.println("");
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
}
