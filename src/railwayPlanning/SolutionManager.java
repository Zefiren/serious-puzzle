package railwayPlanning;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

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

	public void removeStep(int index){
		solution.get(index).undoStep();

		solution.remove(index);

		for(int row = index; row< solution.size(); row++){
			solution.get(row).setStepNumber(row+1);
		}
		editedSinceLastRender = true;
		saved = false;
	}

	public void removeSteps(int[] indices, int num){
		int min = indices[0];
		for(int i = indices.length-1; i>= 0; i--){
			solution.get(indices[i]).undoStep();
			solution.remove(indices[i]);

		}
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
