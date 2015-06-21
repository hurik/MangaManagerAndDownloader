package de.andreasgiemza.mangadownloader.gui.dialogs;

import java.awt.Color;
import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Loading extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;

	public Loading(Frame parent, boolean modal, int x, int y, int width,
			int height) {
		super(parent, modal);
		initComponents();

		setBackground(new Color(0, 0, 0, 0));
		setLocation(x + (width / 2) - (getWidth() / 2), y + (height / 2)
				- (getHeight() / 2));
	}

	public void startRunnable(Runnable runnable) {
		new Thread(runnable).start();

		setVisible(true);
	}

	private void initComponents() {
		JLabel loadingImage = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("Loading");
		setUndecorated(true);
		setResizable(false);
		getContentPane().setLayout(new java.awt.GridLayout(1, 0));

		loadingImage.setBackground(new Color(0, 0, 0, 0));
		loadingImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		loadingImage.setIcon(new ImageIcon(getClass().getClassLoader()
				.getResource("images/loading.gif")));

		getContentPane().add(loadingImage);
		pack();
	}
}
