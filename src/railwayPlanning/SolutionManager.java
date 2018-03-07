package railwayPlanning;

import java.util.Iterator;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SolutionManager {
	ObservableList<SolutionCmd> solution;
	Boolean editedSinceLastRender;
	Boolean saved;

	public SolutionManager() {
		solution = FXCollections.observableArrayList();
		saved = false;
	}

	public void addStep(SolutionCmd newStep){
		newStep.setStepNumber(solution.size()+1);
		solution.add(newStep);
		saved = false;
		editedSinceLastRender = false;
	}

	public void removeStep(SolutionCmd cmd){
		int index = cmd.getStepNumber().getValue() - 1;
		cmd.undoStep();

		solution.remove(cmd);

		for(int row = index; row< solution.size(); row++){
			solution.get(row).setStepNumber(row+1);
		}
		editedSinceLastRender = true;
		saved = false;
	}

	public void removeSteps(List<SolutionCmd> steps){
		int min = steps.get(0).getStepNumber().getValue()-1;
		Iterator<SolutionCmd> it = solution.iterator();
		System.out.println(solution.size());
		System.out.println(solution.get(0).getStep().getValue());
//		solution.remove(0);
//		it.next();
//		it.remove();
		while(it.hasNext()) {
			SolutionCmd step = it.next();
			System.out.println("checking "+step.getStep().getValue());
			if(steps.contains(step)) {
				System.out.println("REMOVING "+step.getStep().getValue());
				steps.remove(step);
				it.remove();
			}
			System.out.println("next");
		}
//		steps.forEach(step ->
//		{
//			step.undoStep();
//			System.out.println("REMOVING "+step.getStep().getValue());
//			solution.remove(step);
//		});
//		for(int i = indices.length-1; i>= 0; i--){
//			solution.get(indices[i]).un doStep();
//			solution.remove(indices[i]);
//
//		}
		System.out.println("hey " + solution.size());
		for(int row = min; row< solution.size(); row++){
			solution.get(row).setStepNumber(row+1);
		}
		editedSinceLastRender = true;
		saved = false;
	}



	public void save(){
		saved = true;
	}

	public ObservableList<SolutionCmd> getSolution() {
		return solution;
	}

	public void setSolution(ObservableList<SolutionCmd> solution) {
		this.solution = solution;
	}
}
