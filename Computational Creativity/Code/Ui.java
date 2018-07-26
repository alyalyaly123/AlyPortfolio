import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import net.sf.extjwnl.JWNLException;

public class Ui {
	public JFrame frame;
	public JLabel title;
	public JTextArea convoSpace;
	public JButton random;
	public JButton start;
	public JButton returnCompliment;
	public JButton returnInsult;
	public JButton returnBabble;
	
	public JLabel personOne;
	public JLabel personTwo;
	public JLabel occLabel;
	public JLabel nameLabel;
	public JLabel factionLabel;
	public JTextField nameOne;
	public JTextField nameTwo;
	public JTextField occOne;
	public JTextField occTwo;
	public JTextField factionOne;
	public JTextField factionTwo;
	
	
	
	public Ui() {
	}
	
	
public void createMe(Controller control){
	Container contentPane;
	
	
	JFrame frame= new JFrame("Babbling bard");
	JLabel title= new JLabel("Babbling bard AI generation");
	title.setFont(new Font("SansSerif", Font.BOLD,25));

	
	JLabel personOne= new JLabel("Person One");
	JLabel personTwo= new JLabel("Person Two");
	JLabel nameLabel= new JLabel("Names");
	nameLabel.setFont(new Font("SansSerif", Font.BOLD,20));

	JLabel factionLabel =new JLabel("Faction");
	factionLabel.setFont(new Font("SansSerif", Font.BOLD,20));

	JLabel occLabel= new JLabel("Occupation");
	occLabel.setFont(new Font("SansSerif", Font.BOLD,20));
	JTextField nameOne= new JTextField();
	JTextField nameTwo= new JTextField();

	JTextField factionOne= new JTextField();
	JTextField factionTwo= new JTextField();
	JTextField occupationOne =new JTextField();
	JTextField occupationTwo =new JTextField();
	
	String areaContents= control.returnCovnersation().toString();
	convoSpace= new JTextArea("Conversation: \n "+ areaContents);
	convoSpace.setFont(new Font("SansSerif", Font.PLAIN ,15));

	/*
	 * Text area setters
	 */
	
	JButton random = new JButton("Generate random conversation");
	random.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				control.startConvo();
			} catch (IOException | JWNLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//ArrayList<String> t= control.returnCovnersation();
			String contents= control.returnCovnersation();
			//contents=contents.replace("{" , " \n \n");
			//contents=contents.replace("[","");
		    //contents=contents.replace("]","");
			convoSpace.setText(contents);
			
		}					
	});
	//Submit custom characters- custom names/factions/occupations.
	JButton submit = new JButton("Submit your characters!");
	submit.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(nameOne.getText().isEmpty() 
					|| nameTwo.getText().isEmpty() 
					||factionOne.getText().isEmpty()
					||factionTwo.getText().isEmpty()
					||occupationOne.getText().isEmpty()
					||occupationTwo.getText().isEmpty()) {
				JOptionPane.showMessageDialog(frame, 
						"Babbling bard v0.5"
						+ "\n Please fill in all fields!",
						"Alert",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
			else {
			String firstName= nameOne.getText();
			String secondName= nameTwo.getText();
			String firstFaction= factionOne.getText();
			String secondFaction= factionTwo.getText();
			String firstOcc= occupationOne.getText();
			String secondOcc= occupationTwo.getText();

				try {
					ArrayList<String> customArray= control.humanGeneratedIntro(firstName, secondName, firstFaction, secondFaction,firstOcc,secondOcc);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JWNLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			String contents= control.returnCovnersation().toString();
		//	contents=contents.replace("{" , " \n");
			//contents=contents.replace("[","");
			//contents=contents.replace("]","");
			convoSpace.setText(contents);
			}
		}					
	});
	
	JButton returnCompliment= new JButton("Generate compliment!");
	returnCompliment.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(nameOne.getText().isEmpty() 
					||nameTwo.getText().isEmpty() 
					||factionOne.getText().isEmpty()
					||factionTwo.getText().isEmpty()
					||occupationOne.getText().isEmpty()
					||occupationTwo.getText().isEmpty()) {
				JOptionPane.showMessageDialog(frame, 
						"Babbling bard v0.5"
						+ "\n Please fill in all fields!",
						"Alert",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
			else {
			String firstName= nameOne.getText();
			String secondName= nameTwo.getText();
			String firstFaction= factionOne.getText();
			String secondFaction= factionTwo.getText();
			String firstOcc= occupationOne.getText();
			String secondOcc= occupationTwo.getText();
				String response= "Test";
				try {
					response= control.uiResponse(firstName, secondName, firstFaction, secondFaction,firstOcc,secondOcc,"compliment");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JWNLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			String contents=response;
			contents=contents.replace("{" , " \n");
			contents=contents.replace("[","");
			contents=contents.replace("]","");
			convoSpace.setText(contents);
			}
		}					
	});
	JButton returnInsult= new JButton("Generate Insult!");
	returnInsult.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(nameOne.getText().isEmpty() 
					|| nameTwo.getText().isEmpty() 
					||factionOne.getText().isEmpty()
					||factionTwo.getText().isEmpty()
					||occupationOne.getText().isEmpty()
					||occupationTwo.getText().isEmpty()) {
				JOptionPane.showMessageDialog(frame, 
						"Babbling bard v0.5"
						+ "\n Please fill in all fields!",
						"Alert",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
			else {
			String firstName= nameOne.getText();
			String secondName= nameTwo.getText();
			String firstFaction= factionOne.getText();
			String secondFaction= factionTwo.getText();
			String firstOcc= occupationOne.getText();
			String secondOcc= occupationTwo.getText();
				String response= "Test";
				try {
					response= control.uiResponse(firstName, secondName, firstFaction, secondFaction,firstOcc,secondOcc,"insult");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JWNLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			String contents=response;
			contents=contents.replace("{" , " \n");
			contents=contents.replace("[","");
			contents=contents.replace("]","");
			convoSpace.setText(contents);
			}
		}					
	});
	
	JButton returnNeutral= new JButton("Generate Neutral!");
	returnNeutral.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(nameOne.getText().isEmpty() 
					||nameTwo.getText().isEmpty() 
					||factionOne.getText().isEmpty()
					||factionTwo.getText().isEmpty()
					||occupationOne.getText().isEmpty()
					||occupationTwo.getText().isEmpty()) {
				JOptionPane.showMessageDialog(frame, 
						"Babbling bard v0.5"
						+ "\n Please fill in all fields!",
						"Alert",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
			else {
			String firstName= nameOne.getText();
			String secondName= nameTwo.getText();
			String firstFaction= factionOne.getText();
			String secondFaction= factionTwo.getText();
			String firstOcc= occupationOne.getText();
			String secondOcc= occupationTwo.getText();
				String response= "Test";
				try {
					response= control.uiResponse(firstName, secondName, firstFaction, secondFaction,firstOcc,secondOcc,"neutral");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JWNLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			String contents=response;
		    //contents=contents.replace("{" , " \n");
			//contents=contents.replace("[","");
			//contents=contents.replace("]","");
			convoSpace.setText(contents);
			}
		}					
	});
	
	/*
	 * ******************************************************
	 */
	
	
	
	
	JButton help= new JButton("Help");
	
	help.addActionListener((ActionEvent e)->{
		JOptionPane.showMessageDialog(frame, 
		"Babbling bard program: v0.5"
		+ "\n Click generate random for an instant demo version of the system, or input your own characters to test out the rest of the functionality!"
		+ "Edit the text files containing the templates for insults/compliments neutral etc to add your own template phrase!",
		
		"Help/Info",
		JOptionPane.INFORMATION_MESSAGE);
	});

	
	
	
	contentPane=frame.getContentPane();
	GridLayout experimentLayout = new GridLayout(0,2);

	contentPane.setLayout(new FlowLayout());
	

	
	Box box= Box.createVerticalBox();
	Box boxButtons= Box.createHorizontalBox();

	Box box2= Box.createHorizontalBox();

	box.add(title);
	
	box.add(nameLabel);
	box.add(new JLabel("Character One"));
	box.add(nameOne);
	box.add(new JLabel("Character Two"));
	box.add(nameTwo);
	
	box.add(factionLabel);
	box.add(new JLabel("Character One"));
	box.add(factionOne);
	box.add(new JLabel("Character Two"));
	box.add(factionTwo);
	box.add(occLabel);
	box.add(new JLabel("Character One"));
	box.add(occupationOne);
	box.add(new JLabel("Character Two"));
	box.add(occupationTwo);
	box.add(Box.createVerticalStrut(20));
	box.add(boxButtons);
	boxButtons.add(submit);
	boxButtons.add(returnCompliment);
	boxButtons.add(returnInsult);
	boxButtons.add(returnNeutral);
	box.add(Box.createVerticalStrut(20));

	box.add(convoSpace);
	box.add(Box.createVerticalStrut(20));
	
	box2.add(random);
	box2.add(help);
	box.add(box2);
	frame.add(box);
	


	frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

	frame.pack();
	frame.setVisible(true);
		
	}


	
	
}
