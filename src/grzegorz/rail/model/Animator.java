package grzegorz.rail.model;

import java.util.HashMap;
import java.util.Map;

import grzegorz.rail.model.SolutionCmd.CommandType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;

public class Animator {
	private final int solutionSize;
	private final SolutionManager solution;
	private final Scenario scenario;

	private boolean animationPlaying;
	private boolean animationCrashed = false;
	private boolean endReached;
	private boolean succeeded;
	private BooleanProperty animationHasNext;
	private BooleanProperty animationHasBackStep;
	private SimpleIntegerProperty stepIndex;
	private float timeSinceStepChange;
	private float lastNanoTime;
	private boolean movingStep;
	boolean movementMade;

	private Map<Pair<Integer, Train>, TrackSection> movementStepLocations = new HashMap<Pair<Integer, Train>, TrackSection>();

	public Animator(int solSize, SolutionManager solution, Scenario scenario) {
		// TODO Auto-generated constructor stub
		solutionSize = solSize;
		this.solution = solution;
		this.scenario = scenario;
		animationPlaying = false;
		stepIndex = new SimpleIntegerProperty(0);
		animationHasBackStep = new SimpleBooleanProperty(false);
		animationHasNext = new SimpleBooleanProperty(true);
		lastNanoTime = 0;
		movingStep = false;
		updateStepsAvailable();
	}

	public IntegerProperty stepIndexProperty() {
		return stepIndex;
	}

	public boolean isPlaying() {
		return animationPlaying;
	}

	public boolean hasCrashed() {
		return animationCrashed;
	}
	
	public boolean hasSucceeded() {
		return succeeded;
	}
	
	public boolean isEndReached() {
		return endReached;
	}
	
	
	public void animationEnablePlay() {
		lastNanoTime = 0;
		animationPlaying = true;
	}

	public boolean animationPlay(float currentNanoTime) {
		movementMade = false;
		if (stepIndex.get() > (solutionSize - 1) && !movingStep) {
			updateStepsAvailable();
			return true;
		}
		if (lastNanoTime == 0) lastNanoTime = currentNanoTime;
		timeSinceStepChange += currentNanoTime - lastNanoTime;
		if (timeSinceStepChange > 1000000000) {
			animationNextStep();
			if (stepIndex.get() > (solutionSize - 1) || (movingStep && !movementMade)) {
				animationPlaying = false;
				updateStepsAvailable();
				return false;
			}
			timeSinceStepChange -= 1000000000;
		}
		lastNanoTime = currentNanoTime;
		return true;
	}

	public void animationPause() {
		animationPlaying = false;
		updateStepsAvailable();
	}

	public void animationNextStep() {
		if (!movingStep) animationNextSolutionStep();
		else {
			animationNextMovement();
			boolean checkStep = solution.getStep(stepIndex.get()).performStep();
			if (checkStep) {
				stepIndex.set(stepIndex.get() + 1);

				movingStep = false;
				return;
			}
		}
	}

	private void animationNextSolutionStep() {
		System.out.println("performed step " + (stepIndex.get() + 1) + ". " + solution.getStep(stepIndex.get()).getStep().getValue());
		boolean performed = solution.getStep(stepIndex.get()).performStep();
		stepIndex.set(stepIndex.get() + 1);
		;
		if (!performed) {
			movingStep = true;
			stepIndex.set(stepIndex.get() - 1);
			movementMade = true;
		}
		updateStepsAvailable();
		boolean success = checkForGoal();
		if(success)
			succeeded = true;
		System.out.println("Succes is: " + success);
	}

	private boolean checkForGoal() {
		if (stepIndex.get() == solutionSize) {
			for (Train tr : scenario.getTrains()) {
				if (tr.getLocation() != tr.getDestination()) return false;
			}
			;
			return true;
		}
		return false;
	}

	// when simulating train movement steps, add location at the beginning of
	// current step
	// find out which direction train is travelling
	// check if signal on current track circuit exists
	// check if signal shows proceed
	// check if track circuit allows driving - switch is set in correct position to
	// allow
	public void animationNextMovement() {
		scenario.getTrains().forEach(tr -> {
			if(tr.isCrashed())
				return;
			Direction trainHeading = tr.getHeadingDirection();
			TrackSection loc = tr.getLocation();
			if (loc.getTrack(trainHeading) == null)

				return;
			if (!movementStepLocations.containsKey(new Pair<Integer, Train>(stepIndex.get(), tr))) {
				System.out.println("putting : ( " + stepIndex + ", " + tr.getTrainID() + " )");
				movementStepLocations.put(new Pair<Integer, Train>(stepIndex.get(), tr), loc);
			}
			// if the train is on a switch, follow tracks
			// otherwise check signal or switch setting
			if (tr.getLocation().getClass() == Switch.class) {
				Switch s = (Switch) tr.getLocation();
				// switches have no signals so only heading matters
				if (s.getSwitchDirection() == trainHeading) {
					if (s.isDiverging()) {
						tr.setLocation(s.getExtraTrack());
						movementMade = true;
					} else {
						tr.setLocation(s.getTrack(trainHeading));
						movementMade = true;
					}
				} else {
					tr.setLocation(s.getTrack(trainHeading));
					movementMade = true;
				}
			} else {
				System.out.println(loc.getSignal(trainHeading) + " signal found");
				Direction signalFacingDirection;
				if (trainHeading == Direction.left) signalFacingDirection = Direction.right;
				else signalFacingDirection = Direction.left;
				if (loc.getTrack(trainHeading).getClass() == Switch.class) {
					System.out.println("next track is switch");
					Switch sw = (Switch) loc.getTrack(trainHeading);
					if (sw.getSwitchDirection() != trainHeading) {
						if ((sw.getTrack(signalFacingDirection) == loc && sw.isDiverging()) || (sw.getExtraTrack() == loc && !sw.isDiverging()) ) {
							if (loc.getSignal(signalFacingDirection) == null) {
								tr.setLocation(sw);
								tr.setCrashed(true);
								animationCrashed = true;
								movementMade = true;
							} else {
								if (loc.getSignal(signalFacingDirection).isClear()) {
									tr.setLocation(sw);
									tr.setCrashed(true);
									animationCrashed = true;
									movementMade = true;
								}
							}
						}
					}
				}
				if (loc.getSignal(signalFacingDirection) == null) {
					tr.setLocation(loc.getTrack(trainHeading));
					movementMade = true;
				} else {
					if (loc.getSignal(signalFacingDirection).isClear()) {
						tr.setLocation(loc.getTrack(trainHeading));
						movementMade = true;
					}
				}
			}
			scenario.getTrains().forEach(otherTrain -> {
				if(otherTrain == tr)
					return;

				if(tr.getLocation() == otherTrain.getLocation())
				{
					tr.setCrashed(true);
					otherTrain.setCrashed(true);
					animationCrashed = true;

				}
			});
		});
		if (!movementMade) {
			System.out.println("no movement made, stop animation");
			endReached = true;
		}
	}

	private void updateStepsAvailable() {
		singleNextStepAvailable();
		singleLastStepAvailable();
	}

	public void animationLastStep() {
		if (!movingStep) {
			stepIndex.set(stepIndex.get() - 1);
			;
			solution.getStep(stepIndex.get()).undoStep();
			if (solution.getStep(stepIndex.get()).getType() == CommandType.CheckLocation) movingStep = true;
			updateStepsAvailable();
		} else {

			scenario.getTrains().forEach(tr -> {
				tr.setCrashed(false);
				System.out.println("getting : ( " + stepIndex + ", " + tr.getTrainID() + " )");
				System.out.println(movementStepLocations.get(new Pair<Integer, Train>(stepIndex.get(), tr)));
				tr.setLocation(movementStepLocations.get(new Pair<Integer, Train>(stepIndex.get(), tr)));
			});
			movingStep = false;
			updateStepsAvailable();
		}
	}

	private void singleNextStepAvailable() {
		System.out.println("next not available " + (animationPlaying || stepIndex.get() >= solutionSize - 1));
		if (animationPlaying || (stepIndex.get() > solutionSize - 1 && movingStep == false) || stepIndex.get() > solutionSize - 1) {
			animationHasNext.setValue(false);
		} else {
			animationHasNext.setValue(true);
		}
	}

	private void singleLastStepAvailable() {
		System.out.println("last not available " + (animationPlaying || stepIndex.get() <= 0));
		if (animationPlaying || stepIndex.get() <= 0) {
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
