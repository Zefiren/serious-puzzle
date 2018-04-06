package grzegorz.rail.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public final class PlannerSolutions {

	private static Scenario scenario;
	private static SolutionManager solutionMgr;

	

	private static void createSteps(int scenarioChosen) {
		switch (scenarioChosen) {
		case 0: {
			
			return;
		}
		case 1: {
			TrackSection tcA = scenario.getTrack(new Point(0,0));
			TrackSection tc1 = scenario.getTrack(new Point(1,0));
			TrackSection tc2 = scenario.getTrack(new Point(2,0));
			TrackSection tcB = scenario.getTrack(new Point(3,0));
			
			Signal s1 = tcA.getLeftSignal();
			Train tr1 = scenario.getTrain(0);
			
			solutionMgr.addStep(new SolutionCmd(s1, true));
			solutionMgr.addStep(new SolutionCmd(tcB,tr1,0));			
			return;
		}
		default: {
			
			return;
		}
		}
	}

	public static SolutionManager createSolution(int scenarioChosen) {
		solutionMgr = new SolutionManager();
		createSteps(scenarioChosen);
		return solutionMgr;
	}

	public static void setScenario(Scenario scen) {
		scenario = scen;
	}
}
