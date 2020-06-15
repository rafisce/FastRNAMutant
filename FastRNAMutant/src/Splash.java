
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
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
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class Splash extends JFrame implements Runnable, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Thread splashThread;
	private int sum;
	private ProgressMonitor pbar;
	static int counter = 0;
	private ImageIcon icon;
	private JLabel label = new JLabel("");
	volatile boolean shutdown = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		Splash g = new Splash("/load.gif");
		Thread t1 = new Thread(g);
		t1.start();

		try {
			Thread.sleep(5000);

			g.change("/filter.gif");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		;

	}

	public Splash(String str) {

		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon icon = new ImageIcon(this.getClass().getResource(str));
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
		setVisible(true);

	}

	public Splash(int count) {
		//progress bar
		sum = count;
		setSize(250, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pbar = new ProgressMonitor(null, "", "Initializing . . .", 0, count);
		Timer timer = new Timer(500, this);
		timer.start();
	}

	public void progress() {
		counter++;
	}

	public synchronized void change(String str) {
		ImageIcon icon = new ImageIcon(this.getClass().getResource(str));
		this.label.setIcon(icon);
	}

	public synchronized void stopSplash() {
		this.setVisible(false);
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Update());
	}

	class Update implements Runnable {
		public void run() {
			if (pbar.isCanceled()) {
				pbar.close();
				System.exit(1);
			}
			pbar.setProgress(counter);
			pbar.setNote("Operation is " + counter + " out of " + sum);
		
		}
	}

}
