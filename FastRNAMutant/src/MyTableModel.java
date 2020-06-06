import javax.swing.table.AbstractTableModel;


    class MyTableModel extends AbstractTableModel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String[] columnNames; 
        private Object[][] data; 
        
        
        MyTableModel(String[] names, Object[][] data){
        	this.data = data;
        	this.columnNames = names;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        /*public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }*/


        public boolean isCellEditable(int row, int col) {
        	return false;
        }



    }


