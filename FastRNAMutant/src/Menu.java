
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class Menu {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField textField_4;
	private JTextField textField_5;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu window = new Menu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Menu() {
		initialize();
	}
	//
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("FastRNAmutants");
		frame.setBounds(490, 200, 560, 471);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textField.setBounds(24, 57, 480, 31);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Centroid");
		rdbtnNewRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		buttonGroup.add(rdbtnNewRadioButton);
		//rdbtnNewRadioButton.setSelected(true);
		rdbtnNewRadioButton.setBounds(24, 331, 103, 21);
		frame.getContentPane().add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Far");
		rdbtnNewRadioButton_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		buttonGroup.add(rdbtnNewRadioButton_1);
		rdbtnNewRadioButton_1.setBounds(24, 354, 103, 21);
		frame.getContentPane().add(rdbtnNewRadioButton_1);
		
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton.setBounds(310, 346, 121, 37);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				if(!textField.getText().equals("") && CheckRNAInput(textField.getText())
						&& !textField_1.getText().equals("") && CheckNumberInput(textField_1.getText())
						&& !textField_2.getText().equals("") && CheckNumberInput(textField_2.getText())
						&& !textField_3.getText().equals("") && CheckNumberInput(textField_3.getText())
						&& !textField_4.getText().equals("") && CheckNumberInput(textField_4.getText())
						&& !textField_5.getText().equals("") && CheckNumberInput(textField_5.getText())
						&& (Integer.parseInt(textField_1.getText())<=textField.getText().length())
						&& (Integer.parseInt(textField_1.getText())>=1)
						&& (rdbtnNewRadioButton.isSelected()||rdbtnNewRadioButton_1.isSelected())) {
					ViennaRNA viennaRNA=new ViennaRNA();
					RNAInfo rnainfo=viennaRNA.RNAfold(textField.getText());
					System.out.println("Structure of optimal:  "+rnainfo.getStructure());
					System.out.println("Energy of optimal:  "+rnainfo.getEnergy());
					
					RNAMultiInfo rnamultiinfo =viennaRNA.RNAsubopt(textField.getText(),Double.parseDouble(textField_4.getText()));
					System.out.println("\nRNAsubOptimal: ");
					for (int i=0;i<rnamultiinfo.getSize();i++)
						System.out.println(rnamultiinfo.getStructure(i)+"    "+rnamultiinfo.getEnergy(i));
					
					//String distance= viennaRNA.RNAdistance("((..))", "......");
					//System.out.println("\nThe distance is: " +distance);
					//String eval= viennaRNA.RNAeval(textField.getText(), rnainfo.getStructure());
					//System.out.println("\nThe eval is: " +eval);
					//PredictMutations algPredict=new PredictMutations(textField.getText(),Integer.parseInt(textField_1.getText()),Integer.parseInt(textField_2.getText()),Integer.parseInt(textField_3.getText()),1);
					int centroidOrFar;
					if (rdbtnNewRadioButton.isSelected())
						centroidOrFar=1;
					else
						centroidOrFar=0;
					
					
					new Thread() {
					      public void run() {
					    	  PredictMutations algPredict=new PredictMutations(textField.getText(),Integer.parseInt(textField_1.getText()),Integer.parseInt(textField_2.getText()),Integer.parseInt(textField_3.getText()),Double.parseDouble(textField_4.getText()),Integer.parseInt(textField_5.getText()),centroidOrFar);
								algPredict.start(frame);
					      }   
					    }.start();
					//PStoImage gui = new PStoImage();
			        //gui.createUI();
				}
				else {
					String text="";
					if(textField.getText().equals("")) {
						text=text+"Please enter RNA sequence\n";
					}
					else if(!textField.getText().equals("") && !CheckRNAInput(textField.getText())) {
						text=text+"The letters of RNA sequence are A/C/G/U\n";
					}
					
					if(textField_1.getText().equals("")) {
						text=text+"Please enter number of mutations\n";
					}
					else if(!textField_1.getText().equals("") && !CheckNumberInput(textField_1.getText())) {
						text=text+"Number of mutations is invalid\n";
					}
					
					if(!textField.getText().equals("") && CheckRNAInput(textField.getText())
							&& !textField_1.getText().equals("") && CheckNumberInput(textField_1.getText())
							&& ((Integer.parseInt(textField_1.getText())>textField.getText().length()) ||
							(Integer.parseInt(textField_1.getText())<1))) {
						text=text+"Number of mutations is from 1 to "+String.valueOf(textField.getText().length())+ "\n";
					}
					
					if(textField_2.getText().equals("")) {
						text=text+"Please enter ammount of mutations groups\n";
					}
					else if(!textField_2.getText().equals("") && !CheckNumberInput(textField_2.getText())) {
						text=text+"Number of ammount of mutations groups is invalid\n";
					}
					
					if (!textField_2.getText().equals("") && CheckNumberInput(textField_2.getText())
							&& Integer.parseInt(textField_2.getText())<1) {
						text=text+"The minimum number for ammount of mutations groups is 1\n";
					}
					
					if(textField_3.getText().equals("")) {
						text=text+"Please enter k of clustering\n";
					}
					else if(!textField_3.getText().equals("") && !CheckNumberInput(textField_3.getText())) {
						text=text+"Number of k of clustering is invalid\n";
					}
					
					if (!textField_3.getText().equals("") && CheckNumberInput(textField_3.getText())
							&& Integer.parseInt(textField_3.getText())<1) {
						text=text+"The minimum number for clusteriong is 1\n";
					}
					
					if(textField_4.getText().equals("")) {
						text=text+"Please enter e-range\n";
					}
					else if(!textField_4.getText().equals("") && !CheckNumberDoubleInput(textField_4.getText())) {
						text=text+"Number of e-range is invalid\n";
					}
					
					if(textField_5.getText().equals("")) {
						text=text+"Please enter distance for filtering\n";
					}
					else if(!textField_5.getText().equals("") && !CheckNumberInput(textField_5.getText())) {
						text=text+"Number of distance for filtering is invalid\n";
					}
					
					if (!(rdbtnNewRadioButton.isSelected()||rdbtnNewRadioButton_1.isSelected())) {
						text=text+"Please choose centroid of far\n";
					}
					
					UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Tahoma", Font.BOLD, 18)));       
					JOptionPane.showMessageDialog(null,text);
						
				}

			}
		});
		frame.getContentPane().add(btnNewButton);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textField_1.setBounds(331, 111, 96, 27);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textField_2.setBounds(331, 148, 96, 28);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textField_3.setBounds(331, 186, 96, 29);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblRnaSequnce = new JLabel("Please insert RNA sequence here:");
		lblRnaSequnce.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblRnaSequnce.setBounds(24, 35, 328, 19);
		frame.getContentPane().add(lblRnaSequnce);
	
		
		JLabel lblNumberOfMutation = new JLabel("Number of mutation");
		lblNumberOfMutation.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNumberOfMutation.setBounds(32, 111, 192, 19);
		frame.getContentPane().add(lblNumberOfMutation);
		
		JLabel lblKForClustering = new JLabel("K for clustering");
		lblKForClustering.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblKForClustering.setBounds(32, 187, 154, 25);
		frame.getContentPane().add(lblKForClustering);
		
		JLabel lblGroups = new JLabel("Ammount of mutations groups");
		lblGroups.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblGroups.setBounds(32, 142, 286, 31);
		frame.getContentPane().add(lblGroups);
		
		textField_4 = new JTextField();
		textField_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textField_4.setBounds(331, 225, 96, 31);
		frame.getContentPane().add(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("E-range");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(31, 227, 193, 25);
		frame.getContentPane().add(lblNewLabel);
		
		textField_5 = new JTextField();
		textField_5.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textField_5.setBounds(331, 266, 96, 31);
		frame.getContentPane().add(textField_5);
		textField_5.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Distance for filtering");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(32, 266, 206, 31);
		frame.getContentPane().add(lblNewLabel_1);
	}
	public boolean CheckRNAInput(String str) {
		for(int i=0;i<str.length();i++) {
			if (!(str.charAt(i)=='A' || str.charAt(i)=='C' || str.charAt(i)=='G' || str.charAt(i)=='U'))
				return false;
		}
		return true;
	}
	public boolean CheckNumberInput(String str) {
		for(int i=0;i<str.length();i++) {
			if (str.charAt(i)<48 || str.charAt(i)>57)
				return false;
		}
		return true;
	}
	public boolean CheckNumberDoubleInput(String str) {
		int counter=0;
		if (str.charAt(str.length()-1)=='.')
			return false;
		for(int i=0;i<str.length();i++) {
			if (str.charAt(i)>=48 && str.charAt(i)<=57 )
			{}
			else if (str.charAt(i)=='.' && (str.charAt(i+1)>=48 && str.charAt(i+1)<=57)) {
				if (counter==0)
					counter=1;
				else
					return false;
			}
			else if (str.charAt(i)=='.' && !(str.charAt(i+1)>=48 && str.charAt(i+1)<=57)) {
				return false;
			}
			else if (str.charAt(i)<48 || str.charAt(i)>57)
				return false;
		}
		return true;
	}
}
