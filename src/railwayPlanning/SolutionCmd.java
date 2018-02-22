package railwayPlanning;

public class SolutionCmd {
	enum CommandType {
			SwitchChange,
			SignalChange,
			CheckLocation
	};
	CommandType type;
	
	Interactable<?> target;
	Boolean oldValue;
	Boolean newValue;
	
	Train targetTrain;

	public SolutionCmd(Interactable<?> target, Boolean newValue) {
		super();
		this.target = target;
		this.newValue = newValue;
		if(target.getInteractable().getClass() == Signal.class)
			type = CommandType.SignalChange;
		if(target.getInteractable().getClass() == Switch.class){
			type = CommandType.SwitchChange;
			System.out.println("switch id " + ((Switch)target).getTsID());
		}
	}
	
	public SolutionCmd(Interactable<?> target, Train targetTrain) {
		super();
		this.target = target;
		this.targetTrain = targetTrain;
		type = CommandType.CheckLocation; 
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		switch (type) {
		case SwitchChange:
			return "Command: Switch " + ((Switch) target).getTsID() + " set to " + newValue +"\n ";
		case SignalChange:
			return "Command: Signal " + ((Signal) target).getId() + " set to " + newValue +"\n ";
		case CheckLocation:
			return "Command: Track Section" + ((TrackSection) target).getTsID() + " has to be occupied by Train" + targetTrain.getTrainID() +"\n ";
		default:
			return "no known type";
		}
		
		
	}
}
