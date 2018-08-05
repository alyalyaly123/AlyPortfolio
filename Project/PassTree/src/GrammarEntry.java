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
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GrammarEntry {
	
	public void enterGrammar(Controller controller){
		JFrame frame;
		JMenuBar menuBar;
		JMenu file;
		//Mode is currently unneeded
		//added due to potential of new modes as development continues
		JMenu mode;
		JMenuItem help;
		JMenuItem draftMode;
		JMenuItem draftModeTwo;
		
		Container contentPane;
		frame = new JFrame("Enter Grammar");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuBar= new JMenuBar();
		
		help= new JMenuItem("Help");
		help.setMnemonic(KeyEvent.VK_H);
		
		help.addActionListener((ActionEvent e)->{
			JOptionPane.showMessageDialog(frame, 
			"Parse Tree: Alpha Version 0.1"
			+ "\n Input your grammar which you will use to build the parse tree.",
			"Help/Info",
			JOptionPane.INFORMATION_MESSAGE);
		});
		
		menuBar.add(help);
		frame.setJMenuBar(menuBar);
		
		contentPane = frame.getContentPane();
		contentPane.setBackground(new Color(144,153,162));
		
		contentPane.setLayout(new FlowLayout());
		
		Box box = Box.createVerticalBox();
		JLabel title = new JLabel("Please enter your context free grammar (use a space to represent Ɛ)");
		title.setFont(new Font("SansSerif", Font.BOLD,25));
		box.add(Box.createVerticalStrut(10));
		box.add(title);
		
		String concurrentRules= controller.returnConcurrentRules().toString();
		concurrentRules= concurrentRules.replaceAll(",", "\n");
		concurrentRules = concurrentRules.replaceFirst("\\[", "");
		concurrentRules = concurrentRules.substring(0,  concurrentRules.length()-1);
		JTextArea currentRuleDisplay; 
		currentRuleDisplay= new JTextArea("Rules= \n"+ concurrentRules);
		currentRuleDisplay.setEditable(false);
		currentRuleDisplay.setBackground(Color.WHITE);
		box.add(Box.createVerticalStrut(10));
		box.add(currentRuleDisplay);
		box.add(Box.createVerticalStrut(10));
		JTextField ruleHead = new JTextField();
		JLabel pointer = new JLabel(" → ");
		pointer.setFont(new Font("SansSerif", Font.BOLD,25));
		JTextField newRule = new JTextField(); 
		Box box2 = Box.createHorizontalBox();
		
		box2.add(ruleHead);
		box2.add(pointer);
		box2.add(newRule);
		box.add(box2);
		
		JButton nextButton = new JButton("Next");
		nextButton.setBackground(Color.white);
		JButton finishedButton = new JButton("Finished");
		finishedButton.setBackground(Color.white);
		
		
		
		box.add(Box.createVerticalStrut(20));
		box.add(nextButton);
		box.add(Box.createVerticalStrut(20));
		box.add(finishedButton);
		
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) 
		    {
				if(ruleHead.getText().isEmpty()|| newRule.getText().isEmpty()){
					JOptionPane.showMessageDialog(frame,"Please input a rule");
				}
				else if(ruleHead.getText().equals(ruleHead.getText().toLowerCase())){
					JOptionPane.showMessageDialog(frame,"Please input a non terminal!");}
				else if (controller.returnConcurrentRules().contains(ruleHead.getText()+ "->"+ newRule.getText())){
					JOptionPane.showMessageDialog(frame,"Redundant rule! Rule already input.");
				}
				else if(ruleHead.getText().equals(newRule.getText())){
					JOptionPane.showMessageDialog(frame,"Redundant rule! Rule leads to self.");
				}
				else{

				frame.dispose();
				controller.getGrammar(ruleHead.getText() ,newRule.getText(), "next");}
			}					
		});
		final String finalRules = concurrentRules;
		finishedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) 
		    {
				if(ruleHead.getText().isEmpty() && newRule.getText().isEmpty() && !finalRules.isEmpty()){
					frame.dispose();
					controller.getGrammar(ruleHead.getText() , newRule.getText(), "finished");
				}
				else if(ruleHead.getText().isEmpty()|| newRule.getText().isEmpty()){
					JOptionPane.showMessageDialog(frame,"Please input a rule");
				}
				else if(ruleHead.getText().equals(ruleHead.getText().toLowerCase())){
					JOptionPane.showMessageDialog(frame,"Please input a non terminal!");}
				else if (controller.returnConcurrentRules().contains(ruleHead.getText()+ "->"+ newRule.getText())){
					JOptionPane.showMessageDialog(frame,"Redundant rule! Rule already input.");
				}
				else if(ruleHead.getText().equals(newRule.getText())){
					JOptionPane.showMessageDialog(frame,"Redundant rule! Rule leads to self.");
				}
				else{

					frame.dispose();
					controller.getGrammar(ruleHead.getText() , newRule.getText(), "finished");
			}		
				
			}					
		});
		
		contentPane.add(box);
		frame.pack();
		frame.setVisible(true);
		
	}
	
}
