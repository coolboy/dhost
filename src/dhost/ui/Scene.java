/*
 * Scene.java
 *
 * Created on __DATE__, __TIME__
 */

package dhost.ui;

import java.lang.System;

import dhost.examples.gamedemo.*

;

/**
 *
 * @author  __USER__
 */
@SuppressWarnings({ "unused", "serial" })
public class Scene extends javax.swing.JFrame {

	/** Creates new form Scene */
	public Scene() {
		initComponents();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		canvas = new dhost.examples.gamedemo.GameController().getGamePanel();
		OutputTF = new javax.swing.JTextField();
		MenuBar = new javax.swing.JMenuBar();
		FileM = new javax.swing.JMenu();
		ConnectMI = new javax.swing.JMenuItem();
		StartMI = new javax.swing.JMenuItem();
		ExitMI = new javax.swing.JMenuItem();
		HelpM = new javax.swing.JMenu();
		About = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Dhost");
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		setMinimumSize(new java.awt.Dimension(640, 480));
		getContentPane().setLayout(new java.awt.GridLayout(0, 1));
		getContentPane().add(canvas);

		OutputTF.setText("Ready");
		getContentPane().add(OutputTF);

		FileM.setText("File");

		ConnectMI.setText("Connect");
		ConnectMI.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ConnectMIActionPerformed(evt);
			}
		});
		FileM.add(ConnectMI);

		StartMI.setText("Start");
		StartMI.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				StartMIActionPerformed(evt);
			}
		});
		FileM.add(StartMI);

		ExitMI.setText("Exit");
		ExitMI.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ExitMIActionPerformed(evt);
			}
		});
		FileM.add(ExitMI);

		MenuBar.add(FileM);

		HelpM.setText("Help");

		About.setText("About");
		About.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				AboutActionPerformed(evt);
			}
		});
		HelpM.add(About);

		MenuBar.add(HelpM);

		setJMenuBar(MenuBar);

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	private void StartMIActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void AboutActionPerformed(java.awt.event.ActionEvent evt) {
		abtDlg.setVisible(true);
	}

	private void ExitMIActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	private void ConnectMIActionPerformed(java.awt.event.ActionEvent evt) {
		connDlg.setVisible(true);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Scene().setVisible(true);
			}
		});
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JMenuItem About;
	private javax.swing.JMenuItem ConnectMI;
	private javax.swing.JMenuItem ExitMI;
	private javax.swing.JMenu FileM;
	private javax.swing.JMenu HelpM;
	private javax.swing.JMenuBar MenuBar;
	private javax.swing.JTextField OutputTF;
	private javax.swing.JMenuItem StartMI;
	private dhost.examples.gamedemo.GamePanel canvas;
	// End of variables declaration//GEN-END:variables

	private ConnectDlg connDlg = new ConnectDlg(this, true);
	private AboutDlg abtDlg = new AboutDlg(this, true);
}