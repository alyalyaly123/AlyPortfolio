import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class WordEntry {

	public static void enterWord(Controller controller){
		JFrame frame;
		JMenuBar menuBar;
		JMenuItem help;
		Container contentPane;
		frame = new JFrame("Enter Word");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuBar= new JMenuBar();
		help = new JMenuItem("Help");
		help.setMnemonic(KeyEvent.VK_H);
		
		help.addActionListener((ActionEvent e)->{
			JOptionPane.showMessageDialog(frame, 
			"Pass Tree"
			+ "\n Now  input the word you wish reach with the parse tree.",
			"Help/Info",
			JOptionPane.INFORMATION_MESSAGE);
		});
		 
		menuBar.add(help);
		
		frame.setJMenuBar(menuBar);
		contentPane = frame.getContentPane();
		contentPane.setBackground(new Color(144,153,162));
		
		contentPane.setLayout(new FlowLayout());
		
		Box box = Box.createVerticalBox();
		JLabel title = new JLabel("Please enter your word");
		title.setFont(new Font("SansSerif", Font.BOLD,25));
		box.add(Box.createVerticalStrut(10));
		box.add(title);
		
		
		JTextField theWord = new JTextField(); 
		box.add(theWord);

		JButton finishedButton = new JButton("Finished");
		box.add(Box.createVerticalStrut(20));
		box.add(finishedButton);
		box.add(Box.createVerticalStrut(10));
		
		finishedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) 
		    {
				frame.dispose();
				controller.storeWord(theWord.getText());
			}					
		});
		finishedButton.setBackground(Color.white);
		contentPane.add(box);
		frame.pack();
		frame.setVisible(true);
		
	}
	
}
