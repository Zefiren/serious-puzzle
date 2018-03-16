package grzegorz.rail.model;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class SolutionCellRenderer extends JLabel implements TableCellRenderer {
	  /**
	 * 
	 */
	private static final long serialVersionUID = -7644727625576296131L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	      boolean hasFocus, int rowIndex, int vColIndex) {
	    setText(value.toString());

	    setToolTipText((String) value);

	    
	    JComponent component = (JComponent)table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, value, false, false, -1, -2);
        component.setBackground(new Color(250, 250, 250));
        component.setBorder(BorderFactory.createEmptyBorder());
	    return component;
	  }
	}