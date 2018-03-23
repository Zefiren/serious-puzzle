package grzegorz.rail.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Animator {
	private final int solutionSize;
	private final SolutionManager solution;

	private boolean animationPlaying;
	private BooleanProperty animationHasNext;
	private BooleanProperty animationHasBackStep;
	private int stepIndex;
	private float timeSinceStepChange;
	private float lastNanoTime;
	private boolean movingStep;

	public Animator(int solSize, SolutionManager solution) {
		// TODO Auto-generated constructor stub
		solutionSize = solSize;
		this.solution = solution;
		animationPlaying = false;
		stepIndex = 0;
		animationHasBackStep = new SimpleBooleanProperty(false);
		animationHasNext = new SimpleBooleanProperty(true);
		lastNanoTime = 0;
		movingStep = false;
	}



	public boolean isPlaying() {
		return animationPlaying;
	}

	public void animationEnablePlay() {
		lastNanoTime = 0;
		animationPlaying = true;
	}

	public boolean animationPlay(float currentNanoTime) {
		if (stepIndex > (solutionSize - 1) && !movingStep) return true;
		if (lastNanoTime == 0) lastNanoTime = currentNanoTime;
		timeSinceStepChange += currentNanoTime - lastNanoTime;
		if (timeSinceStepChange > 1000000000) {
			animationNextStep();
			if (stepIndex > (solutionSize - 1) && !movingStep) return false;
			timeSinceStepChange -= 1000000000;
		}
		lastNanoTime = currentNanoTime;
		return true;
	}

	public void animationPause() {
		animationPlaying = false;
	}

	public void animationNextStep() {
		System.out.println("performed step " + (stepIndex + 1) + ". " + solution.getStep(stepIndex).getStep().getValue());
		solution.getStep(stepIndex).performStep();
		stepIndex++;
		updateStepsAvailable();
	}

	private void updateStepsAvailable() {
		singleNextStepAvailable();
		singleLastStepAvailable();
	}

	public void animationLastStep() {
		stepIndex--;
		solution.getStep(stepIndex).undoStep();
	}

	private void singleNextStepAvailable() {
		System.out.println("next not available "+(animationPlaying || movingStep || stepIndex >= solutionSize - 1));
		if (animationPlaying || movingStep || stepIndex >= solutionSize - 1) {
			animationHasNext.setValue(false);
		} else {
			animationHasNext.setValue(true);
		}
	}

	private void singleLastStepAvailable() {
		if (animationPlaying || movingStep || stepIndex <= 0) {
			animationHasBackStep.setValue(false);
		} else {
			animationHasBackStep.setValue(true);

		}
	}

	public BooleanProperty animationHasNextProperty() {
		return animationHasNext;
	}

	public void setanimationHasNextProperty(BooleanProperty animationFinished) {
		this.animationHasNext = animationFinished;
	}

	public BooleanProperty animationHasBackStepProperty() {
		return animationHasBackStep;
	}

	public void setanimationHasBackStepProperty(BooleanProperty animationAtStart) {
		this.animationHasBackStep = animationAtStart;
	}

}
