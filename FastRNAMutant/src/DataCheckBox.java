import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DataCheckBox extends JDialog {

	JButton btnApply;
	MainPanel mainPanel;

	public DataCheckBox(ArrayList<String> pre) {
		this.setTitle("Choose Sequence");
		this.setModal(true);
		mainPanel = new MainPanel(pre, this);
		this.add(mainPanel);
		this.setBounds(200, 200, 635, 340);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);
	}

	public ArrayList<String> getPost() {
		return mainPanel.getPost2();
	}

	public void invisible() {
		this.setVisible(false);
	}

	public static class MainPanel extends JPanel implements ActionListener, TableModelListener {

		JButton btnApply;
		TablePanel tablePanel;
		JLabel title;
		ArrayList<String> post2;
		DataCheckBox DCB;
		boolean isARowChecked = false;

		public MainPanel(ArrayList<String> pre, DataCheckBox dataCheckBox) {
			setLayout(null);
			Splash s = new Splash();
			title = new JLabel("Choose Sequences from the table:");
			title.setBounds(50, 10, 300, 20);
			this.add(title);

			tablePanel = new TablePanel(this); // this
			this.add(tablePanel);
			tablePanel.setBounds(10, 30, 600, 200);
			tablePanel.setData(pre);
			btnApply = new JButton("Apply");
			btnApply.setBounds(300, 240, 80, 20);
			add(btnApply);
			btnApply.setActionCommand(AC.applyButton);
			btnApply.addActionListener(this);
			DCB = dataCheckBox;

		}

		public ArrayList<String> getPost2() {
			return post2;
		}

		private static class AC {

			public static final String applyButton = "AC_applyButton";

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String ac = e.getActionCommand();
			post2 = new ArrayList<String>();
			DefaultTableModel mod = tablePanel.GetModel();
			for (int i = 0; i < mod.getRowCount(); i++) {
				if (mod.getValueAt(i, 2).equals(true)) {
					post2.add(mod.getValueAt(i, 1).toString());
				}
			}
			DCB.setVisible(false);
			
			

		}

		@Override
		public void tableChanged(TableModelEvent e) {

		} // end changed listener

	} // end main panel class

	public static class TablePanel extends JPanel implements TableModelListener {
		private JTable table;
		JLabel busyLabel;
		DefaultTableModel model;

		int rowSelected = -1;

		ArrayList rowsSelectedList = new ArrayList();
		boolean somethingChkd = false;

		Object[] columnNames = { "No.", "Sequence", "Check" };
		Object[][] data = {};

		public TablePanel(TableModelListener t) {//

			model = new DefaultTableModel(data, columnNames);
			table = new JTable(model) {

				@Override
				public Class getColumnClass(int column) {
					switch (column) {
					case 0:
						return String.class;
					case 1:
						return String.class;
					case 2:
						return Boolean.class;
					default:
						return String.class;
					}
				}

				@Override
				public boolean isCellEditable(int row, int cols) {

					if (cols == 0 | cols == 1 | cols == 3) {
						return false;
					}

					return true;

				}
			};

			table.getModel().addTableModelListener(t);// t
			table.setPreferredScrollableViewportSize(new Dimension(500, 170));
			table.getColumnModel().getColumn(0).setPreferredWidth(30);
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(1).setPreferredWidth(800);
			table.setFillsViewportHeight(true);
			table.setRowSelectionAllowed(true);
			table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			table.getTableHeader().setReorderingAllowed(false);

			// Create the scroll pane and add the table to it.
			JScrollPane scrollPane = new JScrollPane(table);

			// Add the scroll pane to this panel.
			add(scrollPane);

		}// end constructor

		public void setData(ArrayList<String> objArray) {

			int rc = model.getRowCount();

			for (int i = rc - 1; i >= 0; i--) {
				this.model.removeRow(i);
			}

			// data = objArray.clone();
			int index = 1;
			for (String seq : objArray) {
				model.addRow(new Object[] { index, seq, true });
				index++;

			}

		}

		public DefaultTableModel GetModel() {
			return model;
		}

		@Override
		public void tableChanged(TableModelEvent e) {

		}// end table changed event

	} // end TablePanelClass

}
