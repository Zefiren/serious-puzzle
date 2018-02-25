package railwayPlanning;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

public class SolutionManager {
	List<SolutionCmd> solution;
	DefaultTableModel listMod;
	Boolean saved;
	
	public SolutionManager() {
		solution = new ArrayList<SolutionCmd>();
		saved = false;
	}
	
	public void addStep(SolutionCmd newStep){
		solution.add(newStep);
		Object[] newRow = {listMod.getRowCount()+1,newStep};
		listMod.addRow(newRow);
		saved = false;
	}
	
	public void removeStep(SolutionCmd newStep){
		solution.remove(newStep);
		saved = false;
	}
	
	
	
	public void save(){
		saved = true;
	}
}
