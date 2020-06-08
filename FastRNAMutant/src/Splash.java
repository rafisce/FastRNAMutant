
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Splash extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Thread splashThread;
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

}
