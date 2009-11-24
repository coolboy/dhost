/*
 * ConnectDlg.java
 *
 * Created on __DATE__, __TIME__
 */

package dhost.ui;

/**
 *
 * @author  __USER__
 */
public class ConnectDlg extends javax.swing.JDialog {

	/** Creates new form ConnectDlg */
	public ConnectDlg(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}

	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		addrTF = new javax.swing.JTextField();
		goB = new javax.swing.JButton();

		setTitle("Please input the remote address");
		setMinimumSize(new java.awt.Dimension(320, 60));
		setModal(true);
		setName("connDlg");
		getContentPane().setLayout(new java.awt.GridLayout(2, 0));

		jLabel1.setText("Address:");
		getContentPane().add(jLabel1);

		addrTF.setToolTipText("Please input the network address here");
		getContentPane().add(addrTF);

		goB.setText("Go!");
		goB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				goBActionPerformed(evt);
			}
		});
		getContentPane().add(goB);

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	private void goBActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		String addr = this.goB.getText();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				ConnectDlg dialog = new ConnectDlg(new javax.swing.JFrame(),
						true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JTextField addrTF;
	private javax.swing.JButton goB;
	private javax.swing.JLabel jLabel1;
	// End of variables declaration//GEN-END:variables

}