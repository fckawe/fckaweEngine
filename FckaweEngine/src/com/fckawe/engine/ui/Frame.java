package com.fckawe.engine.ui;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fckawe.engine.core.Session;

public class Frame extends JFrame implements WindowListener {

	private static final long serialVersionUID = -2039970114154356523L;

	public Frame(final UserInterface ui) {
		// TODO: app icon
		// String appIcon =
		// Session.getSession().getConfiguration().getAppIcon();
		// if(appIcon != null) {
		// Toolkit tk = Toolkit.getDefaultToolkit();
		// Image img = tk.createImage(ClassLoader.getSystemResource(appIcon));
		// setIconImage(img);
		// }

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(ui);
		setContentPane(panel);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		ui.requestFocus();
	}

	@Override
	public void windowActivated(final WindowEvent e) {
	}

	@Override
	public void windowClosed(final WindowEvent e) {
	}

	@Override
	public void windowClosing(final WindowEvent e) {
		Session.getSession().getHeart().requestExit();
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
	}

	@Override
	public void windowIconified(final WindowEvent e) {
	}

	@Override
	public void windowOpened(final WindowEvent e) {
	}

}
