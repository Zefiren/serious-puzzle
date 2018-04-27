package grzegorz.rail.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import grzegorz.rail.model.SolutionCmd.CommandType;
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

	public void addStep(SolutionCmd newStep) {
		newStep.setStepNumber(solution.size() + 1);
		solution.add(newStep);
		saved = false;
		editedSinceLastRender = false;
	}

	public void removeStep(SolutionCmd cmd) {
		int index = cmd.getStepNumber().getValue() - 1;
		Interactable<?> target = cmd.target;
		cmd.undoStep();

		solution.remove(cmd);

		for (int row = index; row < solution.size(); row++) {
			solution.get(row).setStepNumber(row + 1);
			if(solution.get(row).target == target){
				solution.get(row).oldValue.set(!solution.get(row).oldValue.get());
				solution.get(row).newValue.set(!solution.get(row).newValue.get());
			}
		}
		editedSinceLastRender = true;
		saved = false;
	}

	public void removeSteps(List<SolutionCmd> steps) {
		int min = steps.get(0).getStepNumber().getValue() - 1;
		Iterator<SolutionCmd> it = solution.iterator();
		System.out.println(solution.size());
		System.out.println(solution.get(0).getStep().getValue());

		Map<Interactable<?>, Integer> modified = new HashMap<Interactable<?>, Integer>();
		for (int i = steps.size() - 1; i >= 0; i--) {
			SolutionCmd step = steps.get(i);
			step.undoStep();
			solution.remove(step);

			if (steps.get(i).getType() != CommandType.CheckLocation) {
				Interactable<?> stepTarget = step.target;
				if (modified.get(stepTarget) == null) {
					modified.put(stepTarget, 1);
				}else {
					modified.put(steps.get(i).target, modified.get(stepTarget) + 1);
				}
			}
			System.out.println("next");
		}

		System.out.println("hey " + solution.size());
		for (int row = min; row < solution.size(); row++) {
			solution.get(row).setStepNumber(row + 1);
			if(modified.containsKey(solution.get(row).target))
				if((modified.get(solution.get(row).target) % 2) != 0) {
					solution.get(row).oldValue.set(!solution.get(row).oldValue.get());
					solution.get(row).newValue.set(!solution.get(row).newValue.get());
				}
		}
		editedSinceLastRender = true;
		saved = false;
	}

	public int getLength() {
		return solution.size();
	}

	public SolutionCmd getStep(int index) {
		if (index < getLength()) return solution.get(index);
		else return null;
	}

	public void save() {
		saved = true;
	}

	public ObservableList<SolutionCmd> getSolution() {
		return solution;
	}

	public void setSolution(ObservableList<SolutionCmd> solution) {
		this.solution = solution;
	}
}
