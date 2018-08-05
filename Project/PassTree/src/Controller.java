import java.util.ArrayList;
import java.util.Collections;

public class Controller {

	ArrayList<NonTerminal> rules = new ArrayList<NonTerminal>();
	String word;
	ArrayList<String> concurrentRules = new ArrayList<String>();
	Tree<Terminal> theTree;
	public static void main(String args[]) {
		Controller me = new Controller();
	}

	public Controller() {
		getGrammar("", "", "start");

	}

	public  void getGrammar(String ruleStart, String ruleEnd, String endType) {
		GrammarEntry grammar = new GrammarEntry();
		if (endType == "start") {
			grammar.enterGrammar(this);
		} else if (endType == "next") {
			char theStart = ruleStart.charAt(0);
			storeRule(theStart, ruleEnd);
			grammar.enterGrammar(this);
		} else if (endType == "finished") {
			if (!ruleEnd.isEmpty()) {
				char theStart = ruleStart.charAt(0);
				storeRule(theStart, ruleEnd);
			}			
			getWord();
		}

	}

	public ArrayList<String> returnConcurrentRules() {
		Collections.sort(concurrentRules);
		return concurrentRules;
	}

	public void storeRule(char ruleStart, String theRules) {
		theRules = theRules.replace(" ", "Ɛ");
		int i = checkExists(ruleStart);
		if (i >= 0) {
			rules.get(i).addDestination(theRules);
		} else {
			NonTerminal newOne = new NonTerminal(ruleStart, theRules);
			rules.add(newOne);
		}
		concurrentRules.add(ruleStart + "->" + theRules);
		Collections.sort(concurrentRules);


	}

	public int checkExists(char name) {
		boolean exists = false;
		int i = 0;


		while (i < rules.size() && exists == false) {
			if (rules.get(i).getName() == name) {
				exists = true;
			} else {
				i++;
			}
		}
		if (exists) {
			return i;
		} else {
			return -1;
		}
	}

	public void getWord() {
		WordEntry word = new WordEntry();
		word.enterWord(this);
	}

	public void storeWord(String theWord) {
		word = theWord;
		ParseTree theParseTree = new ParseTree(rules, this, theTree);
	}




}
