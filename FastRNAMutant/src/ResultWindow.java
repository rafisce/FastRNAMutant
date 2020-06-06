import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


@SuppressWarnings("serial")
public class ResultWindow extends JFrame {
	
	

	ArrayList<ArrayList<String>> mutation;
	String seq;

	ResultWindow(ArrayList<ArrayList<String>> mut, String sequence)
    {
		
		this.mutation = mut;
		this.seq = sequence;
        setTitle("Best Mutations");
        setLayout(new FlowLayout());
        setJTable();
        setSize(700,200);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	//sequence mutation converter function
	public String convertSequence(String regular) {
		String converted = seq;
		while (!regular.equals(""))
		{
			String currentOperation = regular.replaceAll(regular.replaceAll("^[A|U|C|G]\\d+[A|U|C|G]", ""),"");
			regular = regular.substring(currentOperation.length());
			Integer index = Integer.parseInt(currentOperation.replaceAll("\\D+",""));
			converted = converted.substring(0,index)+currentOperation.substring(currentOperation.length() - 1)+converted.substring(index+1);
				
		}
		
		return converted;
	}
    private void setJTable()
    {
        
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        
        
        tableModel.addColumn("Mutation Name");
        tableModel.addColumn("Denta Energy");
        tableModel.addColumn("Energy (kcl/mol)");
        tableModel.addColumn("Distance");
        tableModel.addColumn("Dot-bracket represntation");
        tableModel.addColumn("Suboptimal structure");
        
        table.getColumnModel().getColumn(0).setPreferredWidth(160);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(3).setPreferredWidth(160);
        table.getColumnModel().getColumn(4).setPreferredWidth(160);
        table.getColumnModel().getColumn(5).setPreferredWidth(160);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        for(ArrayList<String> row:mutation) {
        	

        		model.addRow(new Object[]{row.get(0),row.get(1),row.get(4),row.get(6),row.get(5),row.get(3)});
        	
        	
        }
        
        JScrollPane js = new JScrollPane(table);
        js.setPreferredSize(new Dimension(400,150));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int row = table.rowAtPoint(evt.getPoint());
		        //int col = table.columnAtPoint(evt.getPoint());
		        if (row >= 0 ) {
		        	System.out.println(row);
		        	
		        	EventQueue.invokeLater(new Runnable() {
		    			public void run() {
		    				try {
		    					
		    					
//		    					MutationInfo window = new MutationInfo(seq,convertSequence(mutation.get(row).get(0)));
//		    					window.frame.pack();
//		    					window.frame.setLocationRelativeTo(null);
//		    					window.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		    					window.frame.setVisible(true);
//		    					window.frame.toFront();
		    				} catch (Exception e) {
		    					e.printStackTrace();
		    				}
		    			}
		    		});

		        }
		    }
		});
        add(js);
        this.setVisible(true);
    } 
    
    
}

//	JButton btnApply;
//	MainPanel mainPanel;
//
//	public ResultWindow(ArrayList<ArrayList<String>> mut, String sequence) {
//		
//		this.setTitle("Best Mutations");
//		this.enable();
//		mainPanel = new MainPanel(mut,this,sequence);
//		this.add(mainPanel);
//		this.setBounds(200, 200, 635, 340);
//		
//	}
//
//	public ArrayList<String> getPost() {
//		return mainPanel.getPost2();
//	}
//	
//	public void invisible() {
//		this.setVisible(false);
//	}
//
//	@SuppressWarnings("serial")
//	public static class MainPanel extends JPanel implements ActionListener, TableModelListener {
//
//		JButton btnApply;
//		TablePanel tablePanel;
//		JLabel title;
//		ArrayList<String> post2;
//		ResultWindow DCB;
//		boolean isARowChecked = false;
//
//		public MainPanel(ArrayList<ArrayList<String>> mut, ResultWindow resultWindow, String sequence) {
//			setLayout(null);
//			
//			//title = new JLabel("Best Mutation List:");
//			//title.setBounds(50, 10, 300, 20);
//			//this.add(title);
//
//			tablePanel = new TablePanel(this,sequence,mut); // this
//			this.add(tablePanel);
//			tablePanel.setBounds(10, 30, 600, 200);
//			tablePanel.setData(mut);
//			btnApply = new JButton("Apply");
//			btnApply.setBounds(300, 240, 80, 20);
//			//add(btnApply);
//			btnApply.setActionCommand(AC.applyButton);
//			btnApply.addActionListener(this);
//			DCB=resultWindow;
//
//		}
//
//		public ArrayList<String> getPost2() {
//			return post2;
//		}
//
//		private static class AC {
//
//			public static final String applyButton = "AC_applyButton";
//
//		}
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			String ac = e.getActionCommand();
//			post2 = new ArrayList<String>();
//			DefaultTableModel mod = tablePanel.GetModel();
//			for (int i = 0; i < mod.getRowCount(); i++) {
//				if (mod.getValueAt(i, 2).equals(true)) {
//					post2.add(mod.getValueAt(i, 1).toString());
//				}
//			}
//			DCB.setVisible(false);
//		}
//
//		@Override
//		public void tableChanged(TableModelEvent e) {
//
//		} // end changed listener
//
//	} // end main panel class
//
//	public static class TablePanel extends JPanel implements TableModelListener {
//		private JTable table;
//		JLabel busyLabel;
//		DefaultTableModel model;
//		private String seq;
//
//		int rowSelected = -1;
//
//		ArrayList rowsSelectedList = new ArrayList();
//		boolean somethingChkd = false;
//
//		Object[] columnNames = { "Name","Energy", "Info" };
//		Object[][] data = {};
//
//		public TablePanel(TableModelListener t, String sequence, ArrayList<ArrayList<String>> mut) {//
//           
//			seq = sequence;
//			model = new DefaultTableModel(data, columnNames);
//			table = new JTable(model) {
//
//				@Override
//				public Class getColumnClass(int column) {
//					switch (column) {
//					case 0:
//						return String.class;
//					case 1:
//						return String.class;
//					case 2:
//						return String.class;
//					default:
//						return String.class;
//					}
//				}
//
//				@Override
//				public boolean isCellEditable(int row, int cols) {
//
//					if (cols == 0 | cols == 1 |cols==3) {
//						return false;
//					}
//
//					return true;
//
//				}
//			};
//
//			table.getModel().addTableModelListener(t);// t
//			table.setPreferredScrollableViewportSize(new Dimension(500, 170));
//			table.getColumnModel().getColumn(0).setPreferredWidth(30);
//			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//			centerRenderer.setHorizontalAlignment( JLabel.LEFT );
//			table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
//			table.getColumnModel().getColumn(0).setPreferredWidth(800);
//			table.setFillsViewportHeight(true);
//			table.setRowSelectionAllowed(true);
//			table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//			table.getTableHeader().setReorderingAllowed(false);
//			table.addMouseListener(new java.awt.event.MouseAdapter() {
//			    @Override
//			    public void mouseClicked(java.awt.event.MouseEvent evt) {
//			        int row = table.rowAtPoint(evt.getPoint());
//			        int col = table.columnAtPoint(evt.getPoint());
//			        if (row >= 0 && col >= 0) {
//			        	System.out.println(row+" "+col);
//			        	
//			        	EventQueue.invokeLater(new Runnable() {
//			    			public void run() {
//			    				try {
//			    					MutationInfo window = new MutationInfo(sequence,convertSequence(mut.get(row).get(0)));
//			    					//window.frame.pack();
//			    					window.frame.setLocationRelativeTo(null);
//			    					window.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
//			    					window.frame.setVisible(true);
//			    					window.frame.toFront();
//			    				} catch (Exception e) {
//			    					e.printStackTrace();
//			    				}
//			    			}
//			    		});
//
//			        }
//			    }
//			});
//
//			// Create the scroll pane and add the table to it.
//			JScrollPane scrollPane = new JScrollPane(table);
//			Border blackline = BorderFactory.createTitledBorder(sequence);
//			scrollPane.setBorder(blackline);
//			
//			// Add the scroll pane to this panel.
//			add(scrollPane);
//
//		}// end constructor
//		
//		
//		//sequence mutation converter function
//		public String convertSequence(String regular) {
//			String converted = seq;
//			while (!regular.equals(""))
//			{
//				String currentOperation = regular.replaceAll(regular.replaceAll("^[A|U|C|G]\\d+[A|U|C|G]", ""),"");
//				regular = regular.substring(currentOperation.length());
//				Integer index = Integer.parseInt(currentOperation.replaceAll("\\D+",""));
//				converted = converted.substring(0,index)+currentOperation.substring(currentOperation.length() - 1)+converted.substring(index+1);
//					
//			}
//			
//			return converted;
//		}
//
//		public void setData(ArrayList<ArrayList<String>> mut) {
//
//			int rc = model.getRowCount();
//
//			for (int i = rc - 1; i >= 0; i--) {
//				this.model.removeRow(i);
//			}
//
//			// data = objArray.clone();
//            int index=1;
//			for (ArrayList<String> Row : mut) {
//				
//				model.addRow(new Object[] { Row.get(0),Row.get(1),"Details"});
//				
//				
//
//			}
//
//		}
//
//		public DefaultTableModel GetModel() {
//			return model;
//		}
//
//		@Override
//		public void tableChanged(TableModelEvent e) {
//
//		}// end table changed event
//		
//		
//
//	} // end TablePanelClass

//}
