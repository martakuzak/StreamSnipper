import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SnipperFrame extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Snipper snipper;
	
	SnipperFrame() {
		this("Stream Snipper");
	}
	
	SnipperFrame(String title) {
		super(title);
		snipper = new Snipper();
		addButton();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,100);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent evt) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Select file");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) { 
			String fileName = chooser.getSelectedFile().getAbsolutePath();
			boolean result = snipper.doEverything(fileName);
			if(!result)
				JOptionPane.showMessageDialog(this,
					    "No Media Data Box found or stream was to large. Make sure to select the right file.",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
			else
				JOptionPane.showMessageDialog(this,
					    "Stream was found and file successfuly created.");
		}
	}
	
	void addButton() {
		JButton button = new JButton("Cut stream in a new file");
		button.addActionListener(this);
		this.add(button);
	}
	

	public static void main(String[] args) {
		SnipperFrame frame = new SnipperFrame();
	}
}
