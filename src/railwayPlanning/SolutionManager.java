package railwayPlanning;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class SolutionManager {
	List<SolutionCmd> solution;
	DefaultTableModel listMod;
	Boolean editedSinceLastRender;
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
		editedSinceLastRender = false;
	}
	
	public void removeStep(int index){
		solution.get(index).undoStep();
		
		solution.remove(index);
		listMod.removeRow(index);

		for(int row = index; row< listMod.getRowCount(); row++){
			listMod.setValueAt(row+1, row, 0);
		}
		editedSinceLastRender = true;
		saved = false;
	}
	
	public void removeSteps(int[] indices, int num){
		int min = indices[0];
		for(int i = indices.length-1; i>= 0; i--){
			System.out.println("removing from index"+ i + " : " + listMod.getValueAt(i, 1));
			solution.get(indices[i]).undoStep();
			solution.remove(indices[i]);
			listMod.removeRow(indices[i]);
			
		}
		for(int row = min; row< listMod.getRowCount(); row++){
			listMod.setValueAt(row+1, row, 0);
		}
		editedSinceLastRender = true;
		saved = false;
	}
	
	public void save(){
		saved = true;
	}
}
