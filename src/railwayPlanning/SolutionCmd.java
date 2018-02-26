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
		this.oldValue = !newValue;
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
			return "Switch : " + ((Switch) target).getTsID() + " -> " + newValue ;
		case SignalChange:
			return "Signal : " + ((Signal) target).getId() + " ->" + newValue ;
		case CheckLocation:
			return "Command: Track Section" + ((TrackSection) target).getTsID() + " has to be occupied by Train" + targetTrain.getTrainID() +"\n ";
		default:
			return "no known type";
		}
		
		
	}
	
	public void undoStep(){
		switch (type) {
		case SwitchChange:
			System.out.println(((Switch)target).getTsID()+ " is the id");
			((Switch)target).setDiverging(oldValue);
			break;
		case SignalChange:
			System.out.println(((Signal)target).getId()+ " is the id");
			((Signal)target).setClear(oldValue);
			break;
		case CheckLocation:
			System.out.println("");
			break;
		default:
			System.out.println("");
		}
	}
}
