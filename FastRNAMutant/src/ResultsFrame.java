
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.TableView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import java.util.ArrayList;


public class ResultsFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private String seq;
	
	JPanel ObestPanel = new JPanel(new GridLayout(1, 0));
	JPanel TopObestPanel = new JPanel(new GridLayout(1, 0));
	
	
	JTabbedPane tabbedPane = new JTabbedPane();

	public ResultsFrame(ArrayList<ArrayList<String>> mut,ArrayList<ArrayList<String>> mut2, String sequence) throws Exception {
		super("Results");
		String[] columnNames = { "Mutation Name", "Delta Energy", "Energy (kcal/mol)", "Distance",
				"Dot-bracket representation", "Suboptimal structure" };

		seq = sequence;
			
		

		final Object[][] data = new Object[mut.size()+1][6];
		final Object[][] data2 = new Object[mut2.size()+1][6];
		

		int count = 0;
		for (ArrayList<String> row : mut) {

			data[count][0] = flip(row.get(0));
			data[count][1] = row.get(1);
			data[count][2] = row.get(4);
			data[count][3] = row.get(6);
			data[count][4] = row.get(5);
			data[count][5] = row.get(3);
			count++;

		}
		
		int count2 = 0;
		for (ArrayList<String> row2 : mut2) {

			data2[count2][0] = flip(row2.get(0));
			data2[count2][1] = row2.get(1);
			data2[count2][2] = row2.get(4);
			data2[count2][3] = row2.get(6);
			data2[count2][4] = row2.get(5);
			data2[count2][5] = row2.get(3);
			count2++;

		}
		ViennaRNA v = new ViennaRNA();
		RNAInfo wt = v.RNAfold(sequence);

		
		
		final JTable OBestTable = new JTable(new MyTableModel(columnNames, data));
		final JTable TopOBestTable = new JTable(new MyTableModel(columnNames, data2));
		OBestTable.setPreferredScrollableViewportSize(new Dimension(1300, 500));
		// table.setFillsViewportHeight(true);
		OBestTable.setFont(new Font("Courier New", Font.PLAIN, 12));
		(OBestTable.getColumnModel().getColumn(0)).setPreferredWidth(300);
		(OBestTable.getColumnModel().getColumn(1)).setPreferredWidth(110);
		(OBestTable.getColumnModel().getColumn(2)).setPreferredWidth(110);
		(OBestTable.getColumnModel().getColumn(3)).setPreferredWidth(100);
		(OBestTable.getColumnModel().getColumn(4)).setPreferredWidth(700);
		(OBestTable.getColumnModel().getColumn(5)).setPreferredWidth(700);
		
		OBestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		OBestTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if (event.getValueIsAdjusting()) {
					return;
				}
				int index = OBestTable.getSelectionModel().getLeadSelectionIndex();

				data[mut.size()][0] = "WT";
				data[mut.size()][1] = v.RNAeval(sequence, wt.getStructure());
				data[mut.size()][2] = wt.getEnergy();
				data[mut.size()][3] = "";
				data[mut.size()][4] = wt.getStructure();
				data[mut.size()][5] = wt.getStructure();

				System.out.println(data[index][0]);
				if ((data[index][0]).equals("WT"))
					return;
				(new MutationFrame(data[index], convertSequence(data[index][0].toString()), data[data.length - 1],
						sequence)).setVisible(true);
				/*
				 * File f = new File("rna.ps"); f.delete();
				 */
			}
		});
		
		TopOBestTable.setFont(new Font("Courier New", Font.PLAIN, 12));
		TopOBestTable.setPreferredScrollableViewportSize(new Dimension(1300, 500));
		(TopOBestTable.getColumnModel().getColumn(0)).setPreferredWidth(300);
		(TopOBestTable.getColumnModel().getColumn(1)).setPreferredWidth(110);
		(TopOBestTable.getColumnModel().getColumn(2)).setPreferredWidth(110);
		(TopOBestTable.getColumnModel().getColumn(3)).setPreferredWidth(100);
		(TopOBestTable.getColumnModel().getColumn(4)).setPreferredWidth(700);
		(TopOBestTable.getColumnModel().getColumn(5)).setPreferredWidth(700);
		
		TopOBestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TopOBestTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if (event.getValueIsAdjusting()) {
					return;
				}
				int index = TopOBestTable.getSelectionModel().getLeadSelectionIndex();

				data2[mut2.size()][0] = "WT";
				data2[mut2.size()][1] = v.RNAeval(sequence, wt.getStructure());
				data2[mut2.size()][2] = wt.getEnergy();
				data2[mut2.size()][3] = "";
				data2[mut2.size()][4] = wt.getStructure();
				data2[mut2.size()][5] = wt.getStructure();

				System.out.println(data2[index][0]);
				if ((data2[index][0]).equals("WT"))
					return;
				(new MutationFrame(data2[index], convertSequence(data2[index][0].toString()), data2[data2.length - 1],
						sequence)).setVisible(true);
				/*
				 * File f = new File("rna.ps"); f.delete();
				 */
			}
		});
		
		resizeColumnWidth(OBestTable);
		resizeColumnWidth(TopOBestTable);

		JScrollPane scrollPane = new JScrollPane(OBestTable);
		JScrollPane scrollPane2 = new JScrollPane(TopOBestTable);
		ObestPanel.add(scrollPane);
		TopObestPanel.add(scrollPane2);
		tabbedPane.add("O Best",ObestPanel);
		tabbedPane.add("Top O Best",TopObestPanel);
		add(tabbedPane);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ObestPanel.setOpaque(true);
		TopObestPanel.setOpaque(true);
		setContentPane(tabbedPane);
		pack();
		setLocationRelativeTo(null);
	}

	public String convertSequence(String regular) {
		String converted = seq;
		while (!regular.equals("")) {
			String currentOperation = regular.replaceAll(regular.replaceAll("^[A|U|C|G]\\d+[A|U|C|G]", ""), "");
			if(regular.contains("-")) {
			regular = regular.substring(currentOperation.length()+1);
			}
			else
			{
				regular = regular.substring(currentOperation.length());
			}
			Integer index = Integer.parseInt(currentOperation.replaceAll("\\D+", ""));
			converted = converted.substring(0, index) + currentOperation.substring(currentOperation.length() - 1)
					+ converted.substring(index + 1);

		}

		return converted;
	}
	
	public String flip(String regular) {
		String converted = "";
		while (!regular.equals("")) {
			String currentOperation = regular.replaceAll(regular.replaceAll("^[A|U|C|G]\\d+[A|U|C|G]", ""), "");
			regular = regular.substring(currentOperation.length());
			converted = currentOperation+"-"+converted;
		}

		return converted.substring(0, converted.length() - 1);
	}
	
	public void resizeColumnWidth(JTable table) {
	    final TableColumnModel columnModel = table.getColumnModel();
	    for (int column = 0; column < table.getColumnCount(); column++) {
	        int width = 15; // Min width
	        for (int row = 0; row < table.getRowCount(); row++) {
	            TableCellRenderer renderer = table.getCellRenderer(row, column);
	            Component comp = table.prepareRenderer(renderer, row, column);
	            width = Math.max(comp.getPreferredSize().width +1 , width);
	        }
	        if(width > 300)
	            width=300;
	        columnModel.getColumn(column).setPreferredWidth(width);
	    }
	}
	
}
