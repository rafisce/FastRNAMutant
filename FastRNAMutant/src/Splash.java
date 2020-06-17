
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.OverlayLayout;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class Splash extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private int sum;
	private ImageIcon icon;
	private JLabel label = new JLabel("");
	private JLabel label1 = new JLabel("");
	volatile boolean shutdown = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		Splash g = new Splash("/load.gif");
		Thread t1 = new Thread(g);
		t1.start();
		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(1000);
				System.out.println(i);
				g.progress(i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		g.stopSplash();
	}

	/**
	 * Create the frame.
	 */

	public Splash() {
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/load.gif"));
		setBounds(100, 100, icon.getIconWidth(), icon.getIconHeight());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		label.setIcon(icon);
		label.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
		contentPane.add(label);
		getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
		

	}

	public Splash(String str) {

		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon icon = new ImageIcon(this.getClass().getResource(str));
		setBounds(100, 100, icon.getIconWidth(), icon.getIconHeight());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		contentPane = new JPanel();
		LayoutManager overlay = new OverlayLayout(contentPane);
		contentPane.setLayout(overlay);
		label1 = new JLabel("");
		label1.setForeground(Color.black);
		label1.setFont(new Font("Serif", Font.PLAIN, 20));
		label1.setAlignmentX(0.5f);
		label1.setAlignmentY(0.5f);
		contentPane.add(label1);
		label = new JLabel(icon);
		label.setAlignmentX(0.5f);
		label.setAlignmentY(0.85f);
		contentPane.add(label);
		add(contentPane);
		getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
		setVisible(true);
		
	}

	public void progress(int min) {

		label1.setText(min + " from " + sum + " completed");
	}

	public void setSum(int count) {
		sum = count;
	}

	public synchronized void change(String str) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(str));
		this.label.setIcon(icon);
	}

	public synchronized void stopSplash() {
		setVisible(false);
	}

	public synchronized void startSplash(int count) {
		setSum(count);
		label1.setText(0 + " from " + count + " completed");
		setVisible(true);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		System.out.println("splash thread started");
		while (!shutdown) {
			// Do something
		}
		setVisible(false);
	}

	public synchronized void terminate() {
		shutdown = true;
	}

}
