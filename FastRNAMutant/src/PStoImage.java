import java.io.File;
import java.io.FileOutputStream;


import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;
import org.ghost4j.document.PSDocument;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.Graphics;

import java.awt.Image;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;

import javax.swing.JFrame;

import javax.swing.JPanel;

import org.ghost4j.renderer.SimpleRenderer;


/**
 * Example showing how to convert a Postscript document to PDF using the high
 * level API.
 * 
 * @author Gilles Grousset (gi.grousset@gmail.com)
 */
public class PStoImage {
	JFrame frame;

	/*
	 * public static void main(String[] args) { PStoImage gui = new PStoImage();
	 * gui.createUI(); }
	 */

	public void createUI() {
		Image img = creatRnaImage("before");
		JPanel panelImg = new JPanel() {
			public void paintComponent(Graphics g) {
				Image img = creatRnaImage("before");
				Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
				setPreferredSize(size);
				setMinimumSize(size);
				setMaximumSize(size);
				setSize(size);
				setLayout(null);
				g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);

			}
		};

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.CENTER, panelImg);
		frame.setSize(img.getWidth(frame) + 10, img.getHeight(frame) + 40);
		frame.setVisible(true);
	}

	public Image creatDotImage() {
		FileOutputStream fos = null;
		try {
			BasicConfigurator.configure();
			PSDocument document = new PSDocument();
			document.load(new File("dot.ps"));

			// create renderer
			SimpleRenderer renderer = new SimpleRenderer();

			// set resolution (in DPI)
			renderer.setResolution(50);

			// render
			List<Image> images = renderer.render(document);
			Image img = images.get(0);
			BufferedImage buffered = (BufferedImage) img;

			return buffered.getSubimage(25, 110, 350, 350);
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		} finally {
			IOUtils.closeQuietly(fos);
		}
		return null;
	}

	public Image creatRnaImage(String state) {
		FileOutputStream fos = null;
		try {
			BasicConfigurator.configure();
			PSDocument document = new PSDocument();
			document.load(new File("rna.ps"));

			// create renderer
			SimpleRenderer renderer = new SimpleRenderer();

			// set resolution (in DPI)
			renderer.setResolution(40);

			// render
			List<Image> images = renderer.render(document);
			Image img = images.get(0);
			BufferedImage buffered = (BufferedImage) img;
			if (state.equals("before")) {
				File outputfile = new File("images/before.jpg");
				ImageIO.write(buffered, "jpg", outputfile);
			} else {
				File outputfile = new File("images/after.jpg");
				ImageIO.write(buffered, "jpg", outputfile);
			}
			//
			return buffered;
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		} finally {
			IOUtils.closeQuietly(fos);
		}
		return null;
	}
}