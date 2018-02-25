package railwayPlanning;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SolutionCellRenderer  extends DefaultTableCellRenderer {
	
		/**
	 * 
	 */
	private static final long serialVersionUID = -4748552178668340389L;

		public SolutionCellRenderer() {
	         setOpaque(true);
	         setFont(new Font("Arial", Font.PLAIN, 14));
//	         stepNumber.setFont(new Font("Arial", Font.BOLD, 20));
	         
	         setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, Color.blue));
	         
         }

	     public Component getListCellRendererComponent(JList<?> list,
	                                                   Object value,
	                                                   int index,
	                                                   boolean isSelected,
	                                                   boolean cellHasFocus) {

	         setText(value.toString());
	         
	         
	         Color background;
	         Color foreground;
	
	         // check if this cell represents the current DnD drop location
	         JList.DropLocation dropLocation = list.getDropLocation();
	         if (dropLocation != null
	                 && !dropLocation.isInsert()
	                 && dropLocation.getIndex() == index) {

	             background = Color.BLUE;
	             foreground = Color.WHITE;

	         // check if this cell is selected
	         } else if (isSelected) {
	             background = Color.GREEN;
	             foreground = Color.WHITE;

	         // unselected, and not the DnD drop location
	         } else {
	             background = null;
	             foreground = Color.BLACK;
	         };

	         setBackground(background);
	         setForeground(foreground);

	         return this;
	     }
	 }