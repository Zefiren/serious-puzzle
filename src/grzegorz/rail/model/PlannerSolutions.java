package grzegorz.rail.model;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import grzegorz.rail.view.MenuController;

public final class PlannerSolutions {

	private static Scenario scenario;
	private static SolutionManager solutionMgr;

	private static void createSteps(int scenarioChosen) {
		// if (!checkExistsProblemFile(scenarioChosen)) {
		String problem = generateProblem(scenarioChosen);
		// }
		// if(!checkExistsPlanFile(scenarioChosen))
	}

	private static String generateProblem(int scenarioChosen) {
		List<TrackSection> tsList = new ArrayList<TrackSection>();
		for (int x = 0; x < scenario.getWidth(); x++) {
			for (int y = 0; y < scenario.getHeight(); y++) {
				if (scenario.getTrack(new Point(x, y)) != null) {
					TrackSection ts = scenario.getTrack(new Point(x, y));
					tsList.add(ts);
				}
			}
		}
		Map<TrackSection, Integer> tcInBlock = new HashMap<TrackSection, Integer>();
		int block = 0;
		// tcInBlock.put(tsList.get(0), block);
		generateBlocks(tcInBlock, tsList.get(0), block);
		tcInBlock.forEach((k, v) -> System.out.println("TC" + k.getTsID() + " , B" + v));
		List<Integer> sortedUniqueBlocks = tcInBlock.values().stream().distinct().sorted().collect(Collectors.toList());

		List<Signal> sortedUniqueSignals = scenario.getSignals().values().stream().distinct()
				.sorted(new Comparator<Signal>() {

					@Override
					public int compare(Signal s1, Signal s2) {
						return s1.getId() < s2.getId() ? -1 : s1.getId() == s2.getId() ? 0 : 1;
					}
				}).collect(Collectors.toList());

		List<TrackSection> sortedTsList = tsList.stream().sorted(new Comparator<TrackSection>() {

			@Override
			public int compare(TrackSection ts1, TrackSection ts2) {
				return ts1.getTsID() < ts2.getTsID() ? -1 : ts1.getTsID() == ts2.getTsID() ? 0 : 1;
			}
		}).collect(Collectors.toList());

		StringBuilder problemString = new StringBuilder();
		problemString.append(
				"(define (problem problem1) \n" + "    (:domain domain1) \n" + " \n" + "    (:objects \n" + "        "
						+ scenario.getTrains().stream().map(tr -> "train" + tr.getTrainID())
								.collect(Collectors.joining(" "))
						+ " - veh \n" + "        "
						+ sortedTsList.stream().map(ts -> "tc" + ts.getTsID()).collect(Collectors.joining(" "))
						+ " - loc \n" + "        "
						+ sortedUniqueBlocks.stream().map(b -> "b" + b.toString()).collect(Collectors.joining(" "))
						+ "  - block \n" + "        "
						+ sortedUniqueSignals.stream().map(sig -> "s" + sig.getId()).collect(Collectors.joining(" "))
						+ " - sigItem \n" + "    ) \n" + " \n" + "    (:init \n");
		scenario.getTrains().forEach(tr -> problemString.append("        (train train" + tr.getTrainID() + ")\n"));
		problemString.append("\n");
		sortedTsList.stream().forEach(ts -> problemString.append("        (tc tc" + ts.getTsID() + ")\n"));
		problemString.append("\n");
		sortedUniqueBlocks.stream().forEach(b -> problemString.append("        (block b" + b.toString() + ")\n"));
		problemString.append("\n");
		sortedUniqueSignals.stream().forEach(sig -> problemString.append("        (sigDef s" + sig.getId() + ")\n"));
		problemString.append("\n");
		tcInBlock.forEach(
				(ts, b) -> problemString.append("        (trackBlock tc" + ts.getTsID() + " b" + b.toString() + ")\n"));
		problemString.append("\n");
		sortedUniqueBlocks.stream().forEach(b -> problemString.append("        (safeBlock b" + b.toString() + ")\n"));
		problemString.append("\n");

		Map<TrackSection, HashSet<TrackSection>> trackAdded = new HashMap<TrackSection, HashSet<TrackSection>>();
		sortedTsList.stream().forEach(ts -> {
			if (trackAdded.containsKey(ts)) {
				if (ts.getLeftTrack() != null) {
					boolean notSwitchExtra = true;
					if (ts.getLeftTrack().getClass() == Switch.class) {
						if (((Switch) ts.getLeftTrack()).getExtraTrack() == ts) {
							notSwitchExtra = false;
						}
					}
					if (notSwitchExtra) {
						if (!trackAdded.get(ts).contains(ts.getLeftTrack())) {
							problemString.append(
									"        (track tc" + ts.getTsID() + " tc" + ts.getLeftTrack().getTsID() + ")\n");
							trackAdded.get(ts).add(ts.getLeftTrack());

							if (trackAdded.containsKey(ts.getLeftTrack())) {
								trackAdded.get(ts.getLeftTrack()).add(ts);
							} else {
								trackAdded.put(ts.getLeftTrack(), new HashSet<TrackSection>());
								trackAdded.get(ts.getLeftTrack()).add(ts);

							}
						}
					}
				}
				if (ts.getRightTrack() != null) {
					boolean notSwitchExtra = true;
					if (ts.getRightTrack().getClass() == Switch.class) {
						if (((Switch) ts.getRightTrack()).getExtraTrack() == ts) {
							notSwitchExtra = false;
						}
					}
					if (notSwitchExtra) {
						if (!trackAdded.get(ts).contains(ts.getRightTrack())) {
							problemString.append(
									"        (track tc" + ts.getTsID() + " tc" + ts.getRightTrack().getTsID() + ")\n");
							trackAdded.get(ts).add(ts.getRightTrack());
							if (trackAdded.containsKey(ts.getRightTrack())) {
								trackAdded.get(ts.getRightTrack()).add(ts);

							} else {
								trackAdded.put(ts.getRightTrack(), new HashSet<TrackSection>());
								trackAdded.get(ts.getRightTrack()).add(ts);
							}
						}
					}
				}
			} else {
				trackAdded.put(ts, new HashSet<TrackSection>());
				if (ts.getLeftTrack() != null) {
					boolean notSwitchExtra = true;
					if (ts.getLeftTrack().getClass() == Switch.class) {
						if (((Switch) ts.getLeftTrack()).getExtraTrack() == ts) {
							notSwitchExtra = false;
						}
					}
					if (notSwitchExtra) {
						problemString.append(
								"        (track tc" + ts.getTsID() + " tc" + ts.getLeftTrack().getTsID() + ")\n");
						trackAdded.get(ts).add(ts.getLeftTrack());
						if (trackAdded.containsKey(ts.getLeftTrack())) {
							trackAdded.get(ts.getLeftTrack()).add(ts);
						} else {
							trackAdded.put(ts.getLeftTrack(), new HashSet<TrackSection>());
							trackAdded.get(ts.getLeftTrack()).add(ts);

						}
					}
				}
				if (ts.getRightTrack() != null) {
					boolean notSwitchExtra = true;
					if (ts.getRightTrack().getClass() == Switch.class) {
						if (((Switch) ts.getRightTrack()).getExtraTrack() == ts) {
							notSwitchExtra = false;
						}
					}
					if (notSwitchExtra) {
						problemString.append(
								"        (track tc" + ts.getTsID() + " tc" + ts.getRightTrack().getTsID() + ")\n");

						trackAdded.get(ts).add(ts.getRightTrack());
						if (trackAdded.containsKey(ts.getRightTrack())) {
							trackAdded.get(ts.getRightTrack()).add(ts);

						} else {
							trackAdded.put(ts.getRightTrack(), new HashSet<TrackSection>());
							trackAdded.get(ts.getRightTrack()).add(ts);
						}
					}
				}

			}
		});

		problemString.append("\n");

		sortedTsList.stream().forEach(ts -> {
			if (ts.getClass() != Switch.class) {
				return;
			} else {
				Switch s = (Switch) ts;
				if (s.getSwitchDirection() == Direction.left) {
					problemString.append("        (switch tc" + s.getTsID() + " tc" + s.getLeftTrack().getTsID() + " tc"
							+ s.getExtraTrack().getTsID() + " tc" + s.getLeftTrack().getTsID() + ")\n");
					if (!s.isDiverging()) {
						problemString.append(
								"        (disconnected tc" + s.getTsID() + " tc" + s.getExtraTrack().getTsID() + ")\n");
					} else {
						problemString.append(
								"        (disconnected tc" + s.getTsID() + " tc" + s.getLeftTrack().getTsID() + ")\n");
					}
				} else {
					problemString.append("        (switch tc" + s.getTsID() + " tc" + s.getRightTrack().getTsID()
							+ " tc" + s.getExtraTrack().getTsID() + " tc" + s.getRightTrack().getTsID() + ")\n");
					if (!s.isDiverging()) {
						problemString.append(
								"        (disconnected tc" + s.getTsID() + " tc" + s.getExtraTrack().getTsID() + ")\n");
					} else {
						problemString.append(
								"        (disconnected tc" + s.getTsID() + " tc" + s.getRightTrack().getTsID() + ")\n");
					}
				}
			}
		});
		sortedUniqueSignals.stream()
				.forEach(sig -> problemString.append("        (signal s" + sig.getId() + " tc"
						+ sig.getSignalTC().getTsID() + " tc" + sig.getProtectingTC().getTsID() + " b"
						+ tcInBlock.get(sig.getProtectingTC()) + " DANGER)\n"));
		problemString.append("\n");

		StringBuilder goal = new StringBuilder();
		goal.append("    (:goal\n" + "        (and\n");
		scenario.getTrains().forEach(tr -> {
			problemString.append("        (at train" + tr.getTrainID() + " tc" + tr.getSource().getTsID() + ")\n");
			problemString
					.append("        (inBlock train" + tr.getTrainID() + " b" + tcInBlock.get(tr.getSource()) + ")\n");
			problemString.append("        (last train" + tr.getTrainID() + " tc" + tr.getSource().getTsID() + ")\n");
			problemString.append("        (fullBlock b" + tcInBlock.get(tr.getSource()) + ")\n");
			problemString.append("\n");

			goal.append("            (at train" + tr.getTrainID() + " tc" + tr.getDestination().getTsID() + ")\n");
			goal.append("            (not(crash train" + tr.getTrainID() + "))\n");
		});
		problemString.append("    )\n");
		goal.append("        )\n" + "    )\n");
		problemString.append(goal.toString());
		problemString.append(")");

		boolean oldProblemIdentical = makeProblemFile(scenarioChosen, problemString.toString());
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// if (oldProblemIdentical)
				makeSolutionFile(scenarioChosen);
				makePlannerSolution(sortedTsList, sortedUniqueSignals, scenarioChosen);

			}

		});
		thread.start();

		return problemString.toString();

	}

	private static int getPredicateID(String[] predicates, int predicateNumber, int substringIndex) {
		String sig = predicates[predicateNumber];

		return Integer.parseInt(sig.substring(substringIndex));
	}

	private static void makePlannerSolution(List<TrackSection> sortedTsList, List<Signal> sortedUniqueSignals,
			int scenarioChosen) {
		HashMap<Integer, TrackSection> tracksByID = new HashMap<Integer, TrackSection>();
		HashMap<Integer, Signal> signalsByID = new HashMap<Integer, Signal>();
		HashMap<Integer, Train> trainsByID = new HashMap<Integer, Train>();

		sortedTsList.forEach(ts -> tracksByID.put(ts.getTsID(), ts));
		sortedUniqueSignals.forEach(sig -> signalsByID.put(sig.getId(), sig));
		scenario.getTrains().forEach(tr -> trainsByID.put(tr.getTrainID(), tr));
		System.out.println("MAKING PLANNER SOLUTION\nREADING FILE NOW");
		File f = new File(MenuController.class.getResource(".").getPath().replace("bin", "src") + "plan"
				+ scenarioChosen + ".json");
		try {
			List<String> actions = Files.readAllLines(Paths.get(f.getAbsolutePath()));
			Integer lastTrainID = null;
			Integer lastTargetID = null;
			Integer lastStepIndex = null;
			SolutionCmd lastStep = null;
			boolean setStepsDetected = false;
			boolean moveMade = false;
			int actionIndex = 0;
			HashMap<Integer, SolutionCmd> skippedSteps = new HashMap<Integer, SolutionCmd>();

			List<SolutionCmd> sol = new LinkedList<SolutionCmd>();
			for (String act : actions) {
				String action = act.replaceAll("[()]", "");
				String[] predicates = action.split(" ");
				String type = predicates[0];
				System.out.println(action);
				boolean skip = false;
				boolean readyToAdd = false;

				if (type.equals("drive")) {
					System.out.println("drive step");
					moveMade = true;
					if (lastTrainID == null) {
						lastTrainID = getPredicateID(predicates, 1, 5);
						lastTargetID = getPredicateID(predicates, 3, 2);
						lastStepIndex = sol.size();
					}
					if (lastTrainID != getPredicateID(predicates, 1, 5) || setStepsDetected
							|| actionIndex == (actions.size() - 1)) {
						if (lastTrainID == getPredicateID(predicates, 1, 5)) {
							lastTargetID = getPredicateID(predicates, 3, 2);
							lastStepIndex = sol.size() + skippedSteps.size();
						}
						Train tr = trainsByID.get(lastTrainID);
						TrackSection target = tracksByID.get(lastTargetID);
						SolutionCmd step = new SolutionCmd(target, tr, lastStepIndex);
						if (lastStep != null) {
							if (lastStep.targetTrain == step.targetTrain && lastStep.target == step.target) {
								skip = true;
							}
						}
						if (!skip) {
							if (skippedSteps.containsKey(tr.getTrainID())) {
								// skippedSteps.forEach((k,step)->
								// step.setStepNumber(step.getStepNumber().get()-1));
								for (Map.Entry<Integer, SolutionCmd> entry : skippedSteps.entrySet()) {
									entry.getValue().setStepNumber(entry.getValue().getStepNumber().get() - 1);
								}
								step.setStepNumber(lastStepIndex - 1);
							}

							skippedSteps.put(tr.getTrainID(), step);
							lastStep = step;
						}
						skip = false;
						// }
						if (lastTrainID != getPredicateID(predicates, 1, 5)) {
							lastTrainID = getPredicateID(predicates, 1, 5);
							lastTargetID = getPredicateID(predicates, 3, 2);
							lastStepIndex = sol.size() + skippedSteps.size();
						}
						if (actionIndex == (actions.size() - 1)) {
							lastTrainID = getPredicateID(predicates, 1, 5);
							lastTargetID = getPredicateID(predicates, 3, 2);
							lastStepIndex = sol.size();
							Train trL = trainsByID.get(lastTrainID);
							TrackSection targetL = tracksByID.get(lastTargetID);
							SolutionCmd stepL = new SolutionCmd(targetL, trL, lastStepIndex);
							skippedSteps.put(trL.getTrainID(), stepL);
						}
					} else {
						lastTargetID = getPredicateID(predicates, 3, 2);
						lastStepIndex = sol.size() + skippedSteps.size();
						System.out.println("TARGET ID:" + lastTargetID);

					}

				} else if (type.equals("set-signal")) {
					System.out.println("signal step");
					Signal sig = signalsByID.get(getPredicateID(predicates, 4, 1));
					boolean newValue = (predicates[5].equals("danger")) ? true : false;
					SolutionCmd step = new SolutionCmd(sig, newValue);
					sol.add(step);
					if (moveMade) {
						readyToAdd = true;
					}
					setStepsDetected = true;
					moveMade = false;
					// }
				} else if (type.equals("set-switch")) {
					System.out.println("switch step");
					Switch s = (Switch) tracksByID.get(getPredicateID(predicates, 1, 2));
					Boolean newValue = null;

					if (getPredicateID(predicates, 4, 2) != s.getExtraTrack().getTsID()) {
						newValue = true;
					} else {
						newValue = false;
					}

					SolutionCmd step = new SolutionCmd(s, newValue);
					sol.add(step);
					// if (moveMade) {
					if (moveMade) {
						readyToAdd = true;
					}
					setStepsDetected = true;
					moveMade = false;
					// }
				}
				if ((setStepsDetected && readyToAdd) || actionIndex == actions.size() - 1) {
					// sol.add(lastStepIndex, step);

					Train tr = trainsByID.get(lastTrainID);
					TrackSection target = tracksByID.get(lastTargetID);
					SolutionCmd step = new SolutionCmd(target, tr, lastStepIndex);
					if (lastStep == null) {
						skippedSteps.put(tr.getTrainID(), step);
						lastStep = step;
					}
					// skippedSteps.remove(lastTrainID);
					if (!skippedSteps.isEmpty()) {
						skippedSteps
								.forEach((trainKey, stepToAdd) -> sol.add(stepToAdd.getStepNumber().get(), stepToAdd));
						skippedSteps.clear();
						setStepsDetected = false;
						readyToAdd = false;
					}
					// if (lastTrainID != getPredicateID(predicates, 1, 5) && actionIndex ==
					// (actions.size() - 1)) {
					// lastTrainID = getPredicateID(predicates, 1, 5);
					// lastTargetID = getPredicateID(predicates, 3, 2);
					// lastStepIndex = sol.size();
					// Train trLast = trainsByID.get(lastTrainID);
					// TrackSection targetLast = tracksByID.get(lastTargetID);
					// SolutionCmd stepLast = new SolutionCmd(targetLast, trLast, 0);
					// sol.add(lastStepIndex, stepLast);
					//
					// }
				}
				actionIndex++;
			}
			;

			for (SolutionCmd solutionCmd : sol) {
				solutionMgr.addStep(solutionCmd);
			}

		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// read action and detect action
		// if train has to wait after moving, create occ step
	}

	private static boolean checkExistsProblemFile(int scenarioChosen) {
		File f = new File(MenuController.class.getResource(".").getPath().replace("bin", "src") + "problem"
				+ scenarioChosen + ".pddl");
		System.out.println(f.getAbsolutePath());
		System.out.println(f.exists());
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean checkExistsPlanFile(int scenarioChosen) {
		File f = new File(MenuController.class.getResource(".").getPath().replace("bin", "src") + "plan"
				+ scenarioChosen + ".json");
		System.out.println(f.getAbsolutePath());
		System.out.println(f.exists());
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean makeProblemFile(int scenarioChosen, String problemContent) {
		File f = new File(MenuController.class.getResource(".").getPath().replace("bin", "src") + "problem"
				+ scenarioChosen + ".pddl");
		System.out.println(f.getAbsolutePath());

		try {
			Files.write(Paths.get(f.getAbsolutePath()), problemContent.getBytes());

			// if (checkExistsProblemFile(scenarioChosen)) {
			// byte[] oldFile = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
			// if (Arrays.equals(oldFile, problemContent.getBytes()))
			// System.out.println("IDENTICAL");
			// return true;
			// } else {
			// System.out.println("NOT NOT NOT IDENTICAL");
			//
			// System.out.println("hello" + f.getAbsolutePath());
			// return false;
			// }
		} catch (IOException e) {
			System.out.println("Path does not exist!");
			e.printStackTrace();
		}
		return false;

	}

	private static void generateBlocks(Map<TrackSection, Integer> tcInBlock, TrackSection ts, int block) {
		tcInBlock.put(ts, block);
		if (tcInBlock.containsKey(ts.getTrack(Direction.right))) {
			if ((ts.getSignal(Direction.left) == null
					&& ts.getTrack(Direction.right).getSignal(Direction.right) == null)) {
				if (tcInBlock.get(ts.getTrack(Direction.right)) != block) {
					Integer oldBlock = tcInBlock.get(ts.getTrack(Direction.right));
					for (Map.Entry<TrackSection, Integer> entry : tcInBlock.entrySet()) {
						if (entry.getValue() == oldBlock) {
							entry.setValue(block);
						}
					}
				}
			}
		}

		if (tcInBlock.containsKey(ts.getTrack(Direction.left))) {
			if (ts.getSignal(Direction.right) == null
					&& ts.getTrack(Direction.left).getSignal(Direction.left) == null) {
				if (tcInBlock.get(ts.getTrack(Direction.left)) != block) {
					Integer oldBlock = tcInBlock.get(ts.getTrack(Direction.left));
					for (Map.Entry<TrackSection, Integer> entry : tcInBlock.entrySet()) {
						if (entry.getValue() == oldBlock) {
							entry.setValue(block);
						}
					}
				}
			}
		}

		if (ts.getTrack(Direction.right) != null && !tcInBlock.containsKey(ts.getTrack(Direction.right))) {
			if (ts.getSignal(Direction.left) != null
					|| ts.getTrack(Direction.right).getSignal(Direction.right) != null) {
				int newBlock = block + 1;
				while (tcInBlock.containsValue(newBlock)) {
					newBlock++;
				}
				generateBlocks(tcInBlock, ts.getTrack(Direction.right), newBlock);

			} else {
				generateBlocks(tcInBlock, ts.getTrack(Direction.right), block);
			}
		}
		System.out.println("WE REACHED HERE GOING LEFT " + ts.getTsID());
		if (ts.getTrack(Direction.left) != null && !tcInBlock.containsKey(ts.getTrack(Direction.left))) {
			if (ts.getSignal(Direction.right) != null
					|| ts.getTrack(Direction.left).getSignal(Direction.left) != null) {
				int newBlock = block + 1;
				while (tcInBlock.containsValue(newBlock)) {
					newBlock++;
				}
				generateBlocks(tcInBlock, ts.getTrack(Direction.left), newBlock);
			} else {
				// tcInBlock.put(ts.getTrack(Direction.left), block);
				generateBlocks(tcInBlock, ts.getTrack(Direction.left), block);
			}
		}
		System.out.println("WE REACHED HERE GOING SWITCH " + ts.getTsID());
		if (ts.getClass() == Switch.class) {
			Switch s = (Switch) ts;
			if (s.getExtraTrack() != null && !tcInBlock.containsKey(s.getExtraTrack())) {
				if (s.getSwitchDirection() == Direction.left) {
					if (s.getExtraTrack().getSignal(Direction.right) != null) {
						int newBlock = block + 1;
						while (tcInBlock.containsValue(newBlock)) {
							newBlock++;
						}
						generateBlocks(tcInBlock, s.getExtraTrack(), newBlock);
					} else {
						generateBlocks(tcInBlock, s.getExtraTrack(), block);
					}
				} else {
					if (s.getExtraTrack().getSignal(Direction.right) != null) {
						int newBlock = block + 1;
						while (tcInBlock.containsValue(newBlock)) {
							newBlock++;
						}
						generateBlocks(tcInBlock, s.getExtraTrack(), newBlock);
					} else {
						generateBlocks(tcInBlock, s.getExtraTrack(), block);
					}
				}
			} else if (tcInBlock.containsKey(s.getExtraTrack())) {
				if (tcInBlock.get(s.getExtraTrack()) != tcInBlock.get(ts)) {
					if ((s.getSwitchDirection() == Direction.left
							&& s.getExtraTrack().getSignal(Direction.right) == null)
							|| (s.getSwitchDirection() == Direction.right
									&& s.getExtraTrack().getSignal(Direction.left) == null)) {
						Integer replacementBlock = tcInBlock.get(ts);
						Integer oldBlock = tcInBlock.get(s.getExtraTrack());
						for (Map.Entry<TrackSection, Integer> entry : tcInBlock.entrySet()) {
							if (entry.getValue() == oldBlock) {
								entry.setValue(replacementBlock);
							}
						}
					}
				}

			}
		}
	}

	private static void makeSolutionFile(int scenarioChosen) {
		// final File f = new
		// File(getClass().getProtectionDomain().getClassLoader().get.getPath());
		String s = null;
		File f = new File(MenuController.class.getResource(".").getPath().replace("bin", "src"));
		try {

			Process p = Runtime.getRuntime().exec(
					"py planner.py domain.txt problem" + scenarioChosen + ".pddl plan" + scenarioChosen + ".json",
					null, f);
			// Process p = Runtime.getRuntime().exec("py src/grzegorz/rail/view/planner.py
			// src/grzegorz/rail/view/domain.txt
			// src/grzegorz/rail/view/problem"+scenarioChosen+".pddl
			// src/grzegorz/rail/view/plan.json");

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
