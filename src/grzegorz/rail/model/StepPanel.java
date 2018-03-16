package grzegorz.rail.model;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StepPanel extends JPanel  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7973403235705391068L;
	JLabel label;
	JLabel stepNumber;
	
	public StepPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		label = new JLabel();
		
		stepNumber = new JLabel();
		stepNumber.setHorizontalAlignment(JLabel.CENTER);
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		add(stepNumber,c);
		
		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 0;
		add(label,c);
		
	}

	
}
