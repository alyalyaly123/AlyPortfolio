import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphSelectionModel;
//need to give proper locations for buttons etc and draw lines, add edit rules, put all entry onto one frame, keep enter grammar on the side with the rules. show all the rules and disable those that arent valid 
public class ParseTree {
	int nextID;
	Controller controller;
	JFrame frame;
	Tree<Terminal> theTree;
	private static final long serialVersionUID = -2707712944901661771L;

	Terminal currentNode;
	
	public ParseTree(ArrayList<NonTerminal> rules, Controller controller, Tree<Terminal> theTree){
		
		nextID = 1;
		currentNode = rules.get(0);
		if (theTree == null) {
			this.theTree = new Tree<Terminal>(currentNode);
		} else {
		this.theTree = theTree;
		}
		this.controller = controller;
		showTree('S', 0);
	}
	
	public char[] splitter(int rulesIndex) {
		String rules = controller.concurrentRules.get(rulesIndex);
		String[] result = rules.split("");
		char[] temp = null;
		for(int i=3;i<result.length;i++){
			temp[i-3]=result[i].charAt(0);
			
		}
		return temp;
		
	}
	
	public void showTree( char name, int id) {
		//if a Non terminal is clicked and has no rules then he program crashes
		
		JMenuBar menuBar;
		JMenuItem help;
		
		
		Container contentPane;
		frame = new JFrame("Enter Parse Tree");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = frame.getContentPane();
		contentPane.setBackground(new Color(144,153,162));
		
		menuBar= new JMenuBar();
		
		help= new JMenuItem("Help");
		help.setMnemonic(KeyEvent.VK_H);
		
		help.addActionListener((ActionEvent e)->{
			JOptionPane.showMessageDialog(frame, 
			"Parse Tree: Alpha Version 0.1"
			+ "\n Click on enabled buttons to create correct parse tree. A window will pop up if you're successful.",
			"Help/Info",
			JOptionPane.INFORMATION_MESSAGE);
		});
		
		menuBar.add(help);
		frame.setJMenuBar(menuBar);
		
		
		contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.X_AXIS));
		
		
		Box box = Box.createVerticalBox();
		box.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		box.setPreferredSize(new Dimension(500, Toolkit.getDefaultToolkit().getScreenSize().height));
		box.setMaximumSize(new Dimension(500, Toolkit.getDefaultToolkit().getScreenSize().height));
		for(int i = 0;i<controller.concurrentRules.size();i++) {
		
			JButton nextButton = new JButton(controller.concurrentRules.get(i));
		    nextButton.setBackground(Color.white);
			nextButton.setPreferredSize(new Dimension(250,40));
			nextButton.setMaximumSize(new Dimension(250,40));
			nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			box.add(nextButton);
			final int z = i;//this is to identify the buttons
			
			nextButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) 
			    {
					int x = z;
					nextNode(controller.concurrentRules.get(x).substring(3), id);
					frame.dispose();
					showTree(name, id);
					
				}					
			});
		
			//Apply any additional UI changes that you want for disabled buttons here
			if (!controller.concurrentRules.get(i).startsWith("" + name)) {
				nextButton.setEnabled(false);
			}
			//Optional! Use to style allowed buttons
			if (controller.concurrentRules.get(i).startsWith("" + name)) {
			    nextButton.setBackground(Color.green);

			}
			
		}
		JTextField currentWord = new JTextField();
		currentWord.setPreferredSize(new Dimension(250,40));
		currentWord.setMaximumSize(new Dimension(250,40));
		currentWord.setAlignmentX(Component.CENTER_ALIGNMENT);
		currentWord.setHorizontalAlignment(JTextField.CENTER);
		currentWord.setEditable(false);
		currentWord.setBackground(Color.WHITE);
		box.add( currentWord);
		contentPane.add(box);
		
		JTextField theWord = new JTextField();
		theWord.setPreferredSize(new Dimension(250,40));
		theWord.setMaximumSize(new Dimension(250,40));
		theWord.setText("target Word: " + controller.word);
		theWord.setHorizontalAlignment(JTextField.CENTER);
		theWord.setAlignmentX(Component.CENTER_ALIGNMENT);
		theWord.setEditable(false);
		theWord.setBackground(Color.WHITE);
		box.add(theWord);
		
		JButton addRule = new JButton("Add New Rule");
		addRule.setBackground(Color.WHITE);
		/*
		JTextArea currentRuleDisplay; 
		String concurrentRules= controller.returnConcurrentRules().toString();
		String concurrentRulesEdited= concurrentRules.replaceAll(",", "\n");
		currentRuleDisplay= new JTextArea("Rules= \n"+ concurrentRulesEdited);
		currentRuleDisplay.setEditable(false);
		currentRuleDisplay.setBackground(Color.WHITE);
		
		box.add(currentRuleDisplay);
		*/
		box.add(addRule);
		
		addRule.setPreferredSize(new Dimension(250,40));
		addRule.setMaximumSize(new Dimension(250,40));
		addRule.setAlignmentX(Component.CENTER_ALIGNMENT);
		addRule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) 
		    {
				controller.theTree = theTree;
				frame.dispose();
				controller.getGrammar("", "", "start");
			}					
		});
		
		contentPane.add(box);
		JPanel theGrid = new JPanel();
		theGrid.setBackground(new Color(144,153,162));
		theGrid.setLayout(new BoxLayout(theGrid,BoxLayout.Y_AXIS));
		theGrid.add(new JScrollPane(createGraph()));
		
	
		theGrid.setSize(new Dimension(100,100));
		contentPane.add(theGrid);
		frame.pack();
		frame.setVisible(true);

		currentWord.setText("Current Word: " + answer());
		
	}

	
	public void nextNode(String strChildren, int id) {
		char[] children = strChildren.toCharArray();
		ArrayList<Terminal> newChildren = new ArrayList<Terminal>();
		for(int i = 0; i< children.length; i++) {
		
				Terminal newOne = new Terminal(children[i]);
				newChildren.add(newOne);
							
			}
			theTree.addChildren(id, newChildren, nextID);
			nextID += children.length;
			
			
	}
	
	public String answer() {
		ArrayList<Terminal> answers = theTree.answer();
		String answerString = "";
		for (int i = 0; i< answers.size();i++) {
			answerString += answers.get(i).getName();
		}
		answerString = answerString.replaceAll("Ɛ", "");
		if (controller.word.equals(answerString)) {
			JOptionPane.showMessageDialog(null, "Congratulations, you have reached the word");
		}
		
		return answerString;
	}
	
	public JPanel createGraph()
	{
		Tree<Terminal> tt = theTree;
		JPanel ret = new JPanel();
		mxGraph graph = new mxGraph();
		
		
		
		graph.getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {
			     @Override     public void invoke(Object sender, mxEventObject evt) {}});
		int i =0;
		Object parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();
		
		parse(graph,parent, 0);

        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph, SwingConstants.VERTICAL);
        layout.execute(parent);
		graph.getModel().endUpdate();
		
		graph.getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {
			@Override
			public void invoke(Object sender, mxEventObject evt) {
				if (sender instanceof mxGraphSelectionModel) {
					for (Object cell : ((mxGraphSelectionModel)sender).getCells()) {
						String nmeS = graph.getLabel(cell);
						String temp = ((mxCell)graph.getSelectionCell()).getId();
						int id = Integer.parseInt(temp);
//						System.out.println("cell=" + nmeS);
//						System.out.println("cell=" + temp );
						char nme = nmeS.charAt(0);
						frame.dispose();
						showTree(nme, id);
						}
					}
				}
		});
		
		
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setConnectable(false);
		ret.add(graphComponent);
//		JScrollPane panelPane = new JScrollPane(ret);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public Object parse(mxGraph graph,Object parent,int id) {
		int i;
	
		char name = theTree.find(id).getName();
		int noOfChildren = theTree.getNoChildren(id);
		String index = Integer.toString(id);
		Object model = addModel(graph,parent,name,index);
		if(noOfChildren>0) {
			int[] children = theTree.getChildrenID(id);
//			System.out.print(name);
//			System.out.println(children.length);
			
				for (i=0;i<children.length;i++) {
					Object chld = parse(graph,parent,children[i]);
					graph.insertEdge(parent, null, "", model, chld);
				}
			
		}
		return model;
	}
	public Object addModel(mxGraph graph,Object parent, char name,String id) {//
		mxCell curr = (mxCell) graph.insertVertex(parent, null, name, 0, 0, 80,30);
		curr.setId(id);
		//create action listener
		return curr;
	}
}
	
	
	

