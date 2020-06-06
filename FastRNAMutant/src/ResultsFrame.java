
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import java.util.ArrayList;


public class ResultsFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private String seq;

	public ResultsFrame(ArrayList<ArrayList<String>> mut, String sequence) throws Exception {
		super("Results");
		String[] columnNames = { "Mutation Name", "Delta Energy", "Energy (kcal/mol)", "Distance",
				"Dot-bracket representation", "Suboptimal structure" };

		seq = sequence;

		final Object[][] data = new Object[mut.size()+1][6];

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
		ViennaRNA v = new ViennaRNA();
		RNAInfo wt = v.RNAfold(sequence);

		JPanel tablePanel = new JPanel(new GridLayout(1, 0));
		final JTable table = new JTable(new MyTableModel(columnNames, data));
		table.setPreferredScrollableViewportSize(new Dimension(1000, 500));
		// table.setFillsViewportHeight(true);
		table.setFont(new Font("Courier New", Font.PLAIN, 12));
		(table.getColumnModel().getColumn(0)).setPreferredWidth(300);
		(table.getColumnModel().getColumn(1)).setPreferredWidth(110);
		(table.getColumnModel().getColumn(2)).setPreferredWidth(110);
		(table.getColumnModel().getColumn(3)).setPreferredWidth(100);
		(table.getColumnModel().getColumn(4)).setPreferredWidth(700);
		(table.getColumnModel().getColumn(5)).setPreferredWidth(700);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if (event.getValueIsAdjusting()) {
					return;
				}
				int index = table.getSelectionModel().getLeadSelectionIndex();

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

		JScrollPane scrollPane = new JScrollPane(table);
		tablePanel.add(scrollPane);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tablePanel.setOpaque(true);
		setContentPane(tablePanel);
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

}
