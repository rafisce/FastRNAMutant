import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MutationFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int count = 0;
	private JPanel contentPane = new JPanel();
	ImageIcon icon1 = null;

	public MutationFrame(Object[] data, String sequence, Object[] wt, String wt_seq) {
		super("" + data[0]);

		count++;
		try {
			Process process = Runtime.getRuntime().exec("RNAplot.exe");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			writer.write(sequence + "\r\n");
			writer.write("" + data[4]);
			writer.close();
			process.waitFor();

			changeName("rna.ps","mut.ps");
			
			String[] params = new String[4];
			params[0] = "magick.exe";
			params[1] = "convert";
			params[2] = "mut.ps";
			params[3] = "mut" + count + ".jpg";

			process = Runtime.getRuntime().exec(params);
			process.waitFor();
			
			
			Process process2 = Runtime.getRuntime().exec("RNAplot.exe");
			BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(process2.getOutputStream()));
			writer2.write(wt_seq + "\r\n");
			writer2.write("" + wt[4]);
			writer2.close();
			process2.waitFor();
			
			String[] params2 = new String[4];
			params2[0] = "magick.exe";
			params2[1] = "convert";
			params2[2] = "rna.ps";
			params2[3] = "mut2.jpg";

			process2 = Runtime.getRuntime().exec(params2);
			process2.waitFor();
			
			
			

		} catch (Exception e) {
			System.out.println(e);
		}
		
	

		setSize(new java.awt.Dimension(1000, 800));
		contentPane.setLayout(null);
		contentPane.setBounds(new Rectangle(0, 0, 900, 800));

		ImageIcon imgIcon = new ImageIcon("mut" + count + ".jpg");
		Image img = imgIcon.getImage();
		Image newImage = img.getScaledInstance(450, 450, Image.SCALE_DEFAULT);
		icon1 = new ImageIcon(newImage);

		JLabel label1 = new JLabel(icon1);
		JLabel label2 = new JLabel("Mutation " + data[0]);
		label1.setBounds(new Rectangle(10, 40, 460, 460));
		label2.setBounds(new Rectangle(20, 10, 200, 30));

		imgIcon = new ImageIcon("mut2.jpg");
		img = imgIcon.getImage();
		newImage = img.getScaledInstance(450, 450, Image.SCALE_DEFAULT);
		ImageIcon icon2 = new ImageIcon(newImage);

		JLabel label3 = new JLabel(icon2);
		JLabel label4 = new JLabel("WT");
		label3.setBounds(new Rectangle(500, 40, 460, 460));
		label4.setBounds(new Rectangle(510, 10, 200, 30));

		contentPane.add(label1);
		contentPane.add(label2);
		contentPane.add(label3);
		contentPane.add(label4);

		File f = new File("mut" + count + ".jpg");
		f.delete();
		File f1 = new File("mut.ps");
		f1.delete();
		File f2 = new File("rna.ps");
		f2.delete();
		File f3 = new File("mut2.jpg");
		f3.delete();
		
		JTextArea textArea = new JTextArea();
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBounds(new Rectangle(10, 520, 950, 230));
		textArea.setEditable(false);
		String distanceType = "Mutation to Wild-Type base pair distance between the corresponding dot-brackets: ";
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		textArea.setText("Wild-Type Sequence:\n" + wt_seq + "\n\n" + "Mutation Sequence:\n" + sequence + "\n\n"
				+ "Wild-Type Free Energy: " + wt[2] + "  kcal/mol\n" + "Mutation  Free Energy: " + data[2]
				+ "  kcal/mol\n\n" + "Wild-Type Dot-Bracket Representation:\n " + wt[4] + "\n"
				+ "Mutation Dot-Bracket Representation:\n " + data[4] + "\n" + distanceType + data[3] + "\n");
		contentPane.add(scroll);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(contentPane);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
	}

	public void makeWT() {

	}
	
	public void changeName(String oldName,String newName) throws IOException {
		File file = new File(oldName);

		// File (or directory) with new name
		File file2 = new File(newName);

		if (file2.exists())
			throw new java.io.IOException("file exists");

		// Rename file (or directory)
		boolean success = file.renameTo(file2);
		

		if (!success) {
			// File was not successfully renamed
		}
	}


}
