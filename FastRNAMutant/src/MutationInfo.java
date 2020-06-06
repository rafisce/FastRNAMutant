import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class MutationInfo {

	protected JFrame frame;
	private String bef;
	private String aft;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MutationInfo window = new MutationInfo("", "");
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
	public MutationInfo(String before, String after) {
		bef = before;
		aft = after;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 986, 761);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(47, 28, 429, 367);
		frame.getContentPane().add(panel);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(498, 28, 423, 374);
		frame.getContentPane().add(panel_1);

		ViennaRNA v = new ViennaRNA();

		PStoImage p = new PStoImage();

		RNAInfo beforInfo = v.RNAfold(bef);
		p.creatRnaImage("before");
		RNAInfo afterInfo = v.RNAfold(aft);
		p.creatRnaImage("after");
		TitledBorder blackline = BorderFactory.createTitledBorder("Non Muteted: " + bef);
		blackline.setTitleColor(Color.BLUE);
		panel.setBorder(blackline);
		ImageIcon beforImg = new ImageIcon("images/before.jpg");
		panel.setSize(beforImg.getIconWidth() + 20, beforImg.getIconHeight() + 40);
		JLabel label = new JLabel(beforImg);
		panel.add(label);

		TitledBorder blackline2 = BorderFactory.createTitledBorder("Muteted: " + aft);
		blackline2.setTitleColor(new Color(0, 153, 51));
		panel_1.setBorder(blackline2);
		ImageIcon afterImg = new ImageIcon("images/after.jpg");
		panel_1.setSize(afterImg.getIconWidth() + 20, afterImg.getIconHeight() + 40);
		JLabel label_1 = new JLabel(afterImg);
		panel_1.add(label_1);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(51, 553, 903, 105);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(new GridLayout(2, 2));

		JLabel lblNewLabel_1 = new JLabel("Energy: " + beforInfo.getEnergy());
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 29));
		lblNewLabel_1.setForeground(Color.BLUE);
		panel_2.add(lblNewLabel_1);

		JLabel lblNewLabel_5 = new JLabel("Energy: " + afterInfo.getEnergy());
		lblNewLabel_5.setFont(new Font("Times New Roman", Font.PLAIN, 29));
		lblNewLabel_5.setForeground(new Color(0, 153, 51));
		panel_2.add(lblNewLabel_5);

		JLabel lblNewLabel_4 = new JLabel("Structure: " + beforInfo.getStructure());
		lblNewLabel_4.setFont(new Font("Times New Roman", Font.PLAIN, 29));
		lblNewLabel_4.setForeground(Color.BLUE);
		panel_2.add(lblNewLabel_4);

		JLabel lblNewLabel = new JLabel("Structure: " + afterInfo.getStructure());
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 29));
		lblNewLabel.setForeground(new Color(0, 153, 51));
		panel_2.add(lblNewLabel);

	}
}
