import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

//Hey maybe these will work who knows
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerType;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.data.list.PointerTargetTree;
import net.sf.extjwnl.data.relationship.AsymmetricRelationship;
import net.sf.extjwnl.data.relationship.Relationship;
import net.sf.extjwnl.data.relationship.RelationshipFinder;
import net.sf.extjwnl.data.relationship.RelationshipList;
import net.sf.extjwnl.dictionary.Dictionary;

/*
 * Alasdair Dunbar ad568 : Babbling Bard controller 
 * Main class for functionality
 */


public class Controller {
	static ArrayList<String> conversationReturn= new ArrayList<String>();
	private static final String USAGE = "Usage: Examples [properties file]";
	private static final Set<String> HELP_KEYS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
			"--help", "-help", "/help", "--?", "-?", "?", "/?"
			)));
	public static Dictionary dictionary;
	//I think for starters I need to make a metaphor generator similar to metaphor magnets thing.
	//In this case it shall be an insult/compliment generator. So for example "most wizards are wise, but you are as 'wise' as a snail"
	public static void main(String[] args) 
			throws java.io.IOException, java.io.FileNotFoundException, JWNLException{



		// TODO Auto-generated method stub

		Controller control= new Controller(dictionary);
	}

	public Controller(Dictionary dictionary) throws java.io.IOException, java.io.FileNotFoundException, JWNLException {

		dictionary = Dictionary.getDefaultResourceInstance();
		ArrayList<String>setConversations=startConvo();
		setConversation(setConversations);
		Ui ui = new Ui();
		ui.createMe(this);
	
	}


	/*
	 * UI controller returns
	 */
	public static void setConversation(ArrayList<String> convo){
		conversationReturn= convo;
	}
	public static String returnCovnersation(){
		String areaContents= conversationReturn.toString();
		areaContents=areaContents.replace("{" , " \n \n");
		areaContents=areaContents.replace("[","");
		areaContents=areaContents.replace("]","");
		return areaContents;
	}
	public static Dictionary returnDictionary() {
		return dictionary; 
	}


	public static ArrayList<String> humanGeneratedIntro(String personOneName, String personTwoName, String personOneFaction,String personTwoFaction,String personOneOcc, String personTwoOcc) 
			throws IOException, JWNLException {
		Dictionary dictionary = null;
		dictionary = Dictionary.getDefaultResourceInstance();
		ArrayList<String>conversation = new ArrayList<String>();
		ArrayList<String> responsesUsed=new ArrayList<String>();
		ArrayList<Character> peopleList =createCharacters(personOneName,personTwoName,personOneFaction,personTwoFaction,personOneOcc,personTwoOcc);
		Character personOne= peopleList.get(0);
		Character personTwo= peopleList.get(1);
		ArrayList<String> returnConvo = conversational(personOne,personTwo,conversation,responsesUsed,"None",peopleList,dictionary);
		setConversation(returnConvo);


		return returnConvo;
	}


	public static ArrayList<Character> createCharacters(String personOneName, String personTwoName, String personOneFaction,String personTwoFaction,String personOneOcc, String personTwoOcc){
		ArrayList<Character> peopleList = new ArrayList<Character>();

		Character personOne=new Character();
		personOne.setName(personOneName);
		personOne.setFaction(personOneFaction);
		personOne.setOcc(personOneOcc);

		Character personTwo= new Character();
		personTwo.setName(personTwoName);
		personTwo.setFaction(personTwoFaction);
		personTwo.setOcc(personTwoOcc);

		peopleList.add(personOne);
		peopleList.add(personTwo);
		initialOpinion(personOne,personTwo);
		return peopleList;


	}

	public static void opinionSet(Character personOne,Character personTwo, String personOneOp,String personTwoOp) {
		float pOneOp= Integer.parseInt(personOneOp);
		float pTwoOp= Integer.parseInt(personTwoOp);

		personOne.setOpinion(pOneOp);
		personTwo.setOpinion(pTwoOp);

	}

	public static String uiResponse(String personOneName, String personTwoName, String personOneFaction,String personTwoFaction,String personOneOcc, String personTwoOcc, String type) throws IOException, JWNLException {
		Dictionary dictionary = null;
		dictionary = Dictionary.getDefaultResourceInstance();
		String response= "test";
		ArrayList<String> responsesUsed = new ArrayList<String>();
		ArrayList<Character> peopleList =createCharacters(personOneName,personTwoName,personOneFaction,personTwoFaction,personOneOcc,personTwoOcc);
		Character person=peopleList.get(0);
		if(type.equals("insult")) {
			ArrayList<String> insultList= insultGenerator(person, peopleList,"No context",responsesUsed,dictionary);
			response=insultList.get(0);
		}
		else if(type.equals("compliment")) {
			ArrayList<String> complimentList= complimentGenerator(person, peopleList,"No context",responsesUsed, dictionary);
			response=complimentList.get(0);

		}
		else {
			ArrayList<String> responseList= neutralGenerator(person, peopleList,"No context",responsesUsed, dictionary);
			response=responseList.get(0);
		}
		return response;

	}



	/*
	 * *******************************************************************************
	 */

	/*
	 * public static String similarSounds(String adjective) throws IOException {
	 *
		String returned="adjective";
		ArrayList<String> returnThis= new ArrayList<String>();
		//System.out.println(returned.substring(1));

		String address="https://api.datamuse.com/words?sp=?" +adjective; 

		URL myURL = new URL(address);
		URLConnection myURLConnection = myURL.openConnection();
		try {
			myURLConnection.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader input = new BufferedReader (new InputStreamReader(myURLConnection.getInputStream()));
		String totalInput = "";
		String inputLine = "";
		while ((inputLine = input.readLine()) != null) {
			totalInput += inputLine;
			}
		input.close();	



		String expression =  totalInput;
		//System.out.println(expression);
		if(expression=="[]") {
			//System.out.println("return same");

		}
		else {
			ArrayList<String> words= new ArrayList<String>();
			String[] things = expression.split("word");
			for(String s: things) {
				String word= s.substring(s.indexOf(":"), s.indexOf(","));
				words.add(word);
			}

			//System.out.println(words.toString());
		}
		return returned;

	}
	*/
	
	/*
	 * Web services for the jigsaw bard: 
	 */
	//Potentially can just combine these into one mega function to save space.
	public static String returnPoetic(String adjective) throws IOException {

		//System.out.println("SEARCHING Poetic");
		adjective=adjective.trim();
		String returned =adjective;

		String address = "http://afflatus.ucd.ie/jigsaw/index.jsp?q="+adjective;
		URL myURL = new URL(address);
		URLConnection myURLConnection = myURL.openConnection();
		try {
			myURLConnection.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader input = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
		String totalInput = "";
		String inputLine ="";
		while ((inputLine = input.readLine()) != null) { 
			totalInput += inputLine;}
		input.close();	
		
		String expression =  totalInput;
		
		Document doc = Jsoup.parse(expression);
		Elements tables= doc.select("table");
		
		for(Element table: tables) {
		
		}
		

		String  poeticNoun= tables.get(4).text();
		
		if(poeticNoun.isEmpty()) {
		//Changing nothing, if returned item is same as adjective it acts as an error
		}
		else {

			poeticNoun = poeticNoun.replaceAll("\\(.*?\\)", ",");

			HashSet<String> container= new HashSet<String>();

			String[] arrayString= poeticNoun.split(",");
			ArrayList<String> returnArray=new ArrayList<String>(Arrays.asList(arrayString));

			//System.out.println("new poetic "+ poeticNoun);
			poeticNoun=poeticNoun.trim();
			ArrayList<String> trimmedList= new ArrayList<String>();
			for(String s: returnArray) {
				s=s.trim();
				trimmedList.add(s);

			}

			//System.out.println("AGAIN "+ trimmedList.toString());
			//Removing dupes
			Set<String> temp =new HashSet<String>(trimmedList);
			
			ArrayList<String> finalList= new ArrayList<String>(temp);

			Collections.shuffle(finalList);
			//System.out.println(finalList);
			returned= finalList.get(0);
			//System.out.println(returned);
			returned= returned.trim();
		}
		return returned;   	
	}

	public static String returnFitting(String adjective) throws IOException {

		//System.out.println("SEARCHING FITTNG");
		adjective=adjective.trim();
		String returned =adjective;

		String address = "http://afflatus.ucd.ie/jigsaw/index.jsp?q="+adjective;
		URL myURL = new URL(address);
		URLConnection myURLConnection = myURL.openConnection();
		try {
			myURLConnection.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader input = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
		String totalInput = "";
		String inputLine ="";
		while ((inputLine = input.readLine()) != null) {
			totalInput += inputLine;}
		input.close();	

		String expression =  totalInput;

		Document doc = Jsoup.parse(expression);
		Elements tables= doc.select("table");

		for(Element table: tables) {
	
		}
		
		String fittingNoun= tables.get(6).text();
	
		if(fittingNoun.isEmpty()) {
			//Error- Word not included in web service
			}
		else {

			fittingNoun = fittingNoun.replaceAll("\\(.*?\\)", ",");

			HashSet<String> container= new HashSet<String>();

			String[] arrayString= fittingNoun.split(",");
			ArrayList<String> returnArray=new ArrayList<String>(Arrays.asList(arrayString));

			//System.out.println("new ironic "+ fittingNoun);
			fittingNoun=fittingNoun.trim();
			ArrayList<String> trimmedList= new ArrayList<String>();
			for(String s: returnArray) {
				s=s.trim();
				//Removing description word
				String start= s.substring(0,s.indexOf(' ')+1);
				String test=s.substring(s.indexOf(' ')+1);
				//System.out.println("test is "+test);
				String thing=test.substring(test.indexOf(' ')+1);
				//System.out.println("thing is "+thing);
				String vowels= "aeiou";
				String firstLetter= ""+thing.charAt(0);
				if(start.contains("some")) {
					//System.out.println("is fine ignore");
				}else if(vowels.contains(firstLetter)) {

					start="an";
				}
				else {
					start="a";
				}

				trimmedList.add(thing);

			}

			//System.out.println("AGAIN "+ trimmedList.toString());
			//Removing dupes
			Set<String> temp =new HashSet<String>(trimmedList);
			ArrayList<String> finalList= new ArrayList<String>(temp);

			Collections.shuffle(finalList);
			//System.out.println(finalList);
			returned= finalList.get(0);
			//System.out.println(returned +" " +finalList.get(1));
			returned= returned.trim();
		}
		return returned;   	
	}

	public static String returnIronic(String adjective) throws IOException {

		//System.out.println("SEARCHING IRONIC");
		adjective=adjective.trim();
		String returned =adjective;

		String address = "http://afflatus.ucd.ie/jigsaw/index.jsp?q="+adjective;
		URL myURL = new URL(address);
		URLConnection myURLConnection = myURL.openConnection();
		try {
			myURLConnection.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader input = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
		String totalInput = new String();
		String inputLine;
		while ((inputLine = input.readLine()) != null) { 
			totalInput += inputLine;}
		input.close();	

		String expression =  totalInput;

		Document doc = Jsoup.parse(expression);
		Elements tables= doc.select("table");
		//  for(Element table: tables) {
		////System.out.println("THIS TABLE "+table.text()); 	
		//  }


		if(tables.get(8).text().isEmpty()) {
			//System.out.println("No response, return same adjective");
		}
		else {
			String ironic =tables.get(8).text();
			ironic = ironic.replaceAll("\\(.*?\\)", ",");
			//System.out.println("new ironic "+ ironic);
			ironic=ironic.trim();
			String[] arrayString= ironic.split(",");
			ArrayList<String> returnArray=new ArrayList<String>(Arrays.asList(arrayString));

			//System.out.println("AGAIN "+ returnArray.toString());
			Collections.shuffle(returnArray);
			returned= returnArray.get(0);

			returned= returned.trim();

		}
		return returned;   	
	}



	public static String returnCoword(String adjective) throws IOException {
		adjective=adjective.trim();
		String returned =adjective;

		String address = "http://afflatus.ucd.ie/jigsaw/index.jsp?q="+adjective;
		URL myURL = new URL(address);
		URLConnection myURLConnection = myURL.openConnection();
		try {
			myURLConnection.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader input = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
		String totalInput = new String();
		String inputLine="";
		while ((inputLine = input.readLine()) != null) { 
			totalInput += inputLine;}
		input.close();	
		String expression =  totalInput;

		Document doc = Jsoup.parse(expression);
		Element table = doc.select("table").get(1);


		Element row = table.select("tr").get(2);
		if(row.text().isEmpty()) {

		}else {
			String rowText= row.text();
			//System.out.println(row.text());
			rowText=rowText.replaceAll("and", "");
			//System.out.println("temp rowtext "+ rowText);
			rowText=rowText.replaceAll(" ", "");
			//System.out.println("New row Text " + rowText);
			String[] arrayString= rowText.split(adjective);

			ArrayList<String> returnArray=new ArrayList<String>(Arrays.asList(arrayString));
			returnArray.add(adjective);
			returnArray.remove(0);
			//System.out.println("AGAIN "+ returnArray.toString());
			Collections.shuffle(returnArray);
			returned= returnArray.get(0);

			returned= returned;
		}

		return returned;
	}


	/*
	 * Metaphor magnet tool
	 */
	public static ArrayList<String> returnMetaphorPair(String noun) throws IOException {

		//System.out.println("SEARCHING METAPHORS");
		noun=noun.trim();
		String returned =noun;

		String address = "http://ngrams.ucd.ie/metaphor-magnet-acl/q?kw="+noun+"&xml=true";

		URL myURL = new URL(address);
		URLConnection myURLConnection = myURL.openConnection();
		try {
			myURLConnection.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader input = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
		String total = "";
		String inputLine="";
		
		while ((inputLine = input.readLine()) != null) { 
			total += inputLine;
			}
		input.close();	

		String expression =  total;

		Document doc = Jsoup.parse(expression,"", Parser.xmlParser());
		//System.out.println(expression);
		if(expression.contains("img/no-metaphors-found.jpg")) {
			//System.out.println("nothing");
		}
		else {

			String changeThis= "";
			ArrayList<String> wordList = new ArrayList<String>();

			for(Element elm: doc.select("Text")) {
				//System.out.println(elm.text());
				changeThis=changeThis+ elm.text()+",";
			}

			String [] arrayString= changeThis.split(",");
			ArrayList<String> arrayListString=new ArrayList<String>(Arrays.asList(arrayString));
			Collections.shuffle(arrayListString);
			String returnString=arrayListString.get(0);

			String [] retStringAsArray= returnString.split(":");
			ArrayList<String> returnedArray=new ArrayList<String>(Arrays.asList(retStringAsArray));
			return returnedArray;


		}
		ArrayList<String> returnedArray=new ArrayList<String>();
		returnedArray.add(returned);
		return returnedArray;

	}



	/*
	 * Return words, uses wordNet
	 */
	public static String returnSynonyms(IndexWord word) throws JWNLException{
		System.out.println("RETURNING SYYNONYMS for" + word.toString());
		ArrayList<String> wordReturned = new ArrayList<String>();
		if(word.getSenses().isEmpty()){
			//System.out.println(word + " doesn't count as a word just return it");
			return word.toString();

		}
		PointerTargetNodeList meronym = PointerUtils.getDirectHyponyms(word.getSenses().get(0));
		System.out.println("---");

		meronym.print();
		System.out.println("---");
		
		PointerTargetNodeList synonyms = PointerUtils.getSynonyms(word.getSenses().get(0));
		System.out.println("***");

		synonyms.print();
		System.out.println("***");
		//System.out.println("Direct synonyms of \"" + word.getLemma() + "\":");
		//synonyms.print();
		//System.out.println("Direct of \"" + word.getLemma() + "\":");

		synonyms.add(word.getSenses().get(0), null);
	//	synonyms.print();

		PointerTargetNodeList alsoSees = PointerUtils.getAlsoSees(word.getSenses().get(0));
		alsoSees.print();
		//System.out.println("Also related are \"" + word.getLemma() + "\":");
		synonyms.addAll(alsoSees);
//		synonyms.print();

		PointerTargetTree synTree = PointerUtils.getSynonymTree(word.getSenses().get(0),1);
		//System.out.println("Synonym tree of  \"" + word.getLemma() + "\":");
		synonyms.addAll(alsoSees);
		//synonyms.print();

		if(synonyms.size()==0){
			//System.out.println("The only word is"+word.getLemma());
			return word.getLemma();
		}
		int i=0;
		//System.out.println("SYNONYM SIZE" + synonyms.size());
		//Create list of synonyms
		while(i<synonyms.size()){

			PointerTargetNode x = synonyms.get(i);
			String wordAdded = x.toString();
			wordAdded= wordAdded.substring(wordAdded.indexOf("Words:")+6);
			wordAdded=wordAdded.substring(0, wordAdded.indexOf("--"));

			String[] makeAThing= wordAdded.split(",");
			for(String str: makeAThing){
				str.trim();
				wordReturned.add(str);
			}
			i++;
		}
		Collections.shuffle(wordReturned);
		//System.out.println("no of words returned "+ wordReturned.size());
		//System.out.println("Lets see if I can return this a thing "+ wordReturned.toString());
		String aWord= wordReturned.get(0);
		aWord=aWord.trim();
		return aWord;
	}





	public static String returnAntonym(IndexWord word, Dictionary dictionary) throws JWNLException{
		String returned= word.getLemma();
		//System.out.println("Antonyms----");


		ArrayList<String> list= new ArrayList<String>();
		PointerTargetNodeList antonyms = PointerUtils.getAntonyms(word.getSenses().get(0));
		if(antonyms.isEmpty()) {

		}

		else {
			//antonyms.print();

			int i=0;
			ArrayList<String> wordReturned= new ArrayList<String>();

			while(i<antonyms.size()){

				PointerTargetNode x = antonyms.get(i);
				String wordAdded = x.toString();
				wordAdded= wordAdded.substring(wordAdded.indexOf("Words:")+6);
				wordAdded=wordAdded.substring(0, wordAdded.indexOf("--"));

				String[] makeAThing= wordAdded.split(",");
				for(String str: makeAThing){
					wordReturned.add(str);
				}
				i++;
			}
			ArrayList<String> newWordList= new ArrayList<String>(wordReturned);
			for(String words: wordReturned) {

				IndexWord ADJECTIVE= dictionary.getIndexWord(POS.ADJECTIVE, words);
				PointerTargetNodeList synonyms = PointerUtils.getSynonyms(ADJECTIVE.getSenses().get(0));

				while(i<synonyms.size()){

					PointerTargetNode x = synonyms.get(i);
					String wordAdded = x.toString();
					wordAdded= wordAdded.substring(wordAdded.indexOf("Words:")+6);
					wordAdded=wordAdded.substring(0, wordAdded.indexOf("--"));

					String[] makeAThing= wordAdded.split(",");
					for(String str: makeAThing){
						newWordList.add(str);
					}
					i++;
				}

			}
			//System.out.println("THE WORD IS "+newWordList.toString());
			HashSet copyTest= new HashSet(newWordList);
			ArrayList<String> finalList= new ArrayList<String>(copyTest);
			Collections.shuffle(finalList);
			returned= finalList.get(0);


		}
		returned=returned.trim();
		return returned;

	}

	/*
	 * Conversation generation
	 */

	//Begins conversation- Random generator
	public static ArrayList<String> startConvo() throws IOException, JWNLException{
		//Dictionary for wordnet synonyms.
		Dictionary dictionary = null;
		dictionary = Dictionary.getDefaultResourceInstance();



		//Character and faction list generator
		String nameFile="FantasyNames.txt";
		String factionFile="Factions.txt";
		String nameContent = new String(Files.readAllBytes(Paths.get(nameFile)));
		//System.out.println(" WHOLE CONTENT OF THE FILE : " +nameContent);
		List<String> nameList= new ArrayList<String>(Arrays.asList(nameContent.split("\\r?\\n")));
		String factionContent = new String(Files.readAllBytes(Paths.get(factionFile)));
		List<String> factionList= new ArrayList<String>(Arrays.asList(factionContent.split("\\r?\\n")));
		ArrayList<String> conversationHistory= new ArrayList<String>();
		ArrayList<String> converseText= new ArrayList<String>();
		
		
		//Random character creation
		Random random= new Random();
		//System.out.println("Create 2 characters, their views towards eachother will be random (Potentially base om each characters view on occupation?)");
		Character personOne= new Character();
		Character personTwo= new Character();
		String occupationText="occupations.txt";
		String occupationContent = new String(Files.readAllBytes(Paths.get(occupationText)));
		List<String> occupationList= new ArrayList<String>(Arrays.asList(occupationContent.split("\\r?\\n")));
		Collections.shuffle(occupationList);
		personOne.setOcc(occupationList.get(0));
		Collections.shuffle(occupationList);
		personTwo.setOcc(occupationList.get(0));
		personOne.setName(nameList.get(random.nextInt(nameList.size())));
		personTwo.setName(nameList.get(random.nextInt(nameList.size())));
		while(personOne.name.equals(personTwo.name)){
			//System.out.println("Cycling new names!");

			personTwo.setName(nameList.get(random.nextInt(nameList.size())));

		}


		personOne.setFaction(factionList.get(random.nextInt(factionList.size())));
		personTwo.setFaction(factionList.get(random.nextInt(factionList.size())));
		//System.out.println("PERSON ONES NAME "+ personOne.name +"PERSON TWO "+personTwo.name );
		ArrayList<Character> peopleList = new ArrayList<Character>();
		peopleList.add(personOne);
		peopleList.add(personTwo);
		initialOpinion(personOne,personTwo);
		String context= "none";
		ArrayList<String> conversation= conversational(personOne, personTwo, conversationHistory,converseText,context,peopleList,dictionary);
		setConversation(conversation);

		return conversation;



	}



	/*
	 *Set opinions
	 */

	public static void initialOpinion(Character personOne, Character personTwo){
		//starter opinions
		personOne.setOpinion(50);
		personTwo.setOpinion(50);

		if(personOne.occupation.equals(personTwo.occupation)){
			Random rand= new Random();
			int max= 10;
			float change1= rand.nextInt(max-0);
			float change2= rand.nextInt(max-0);
			personOne.setOpinion(personOne.opinion+change1);
			personTwo.setOpinion(personTwo.opinion+change2);
		}


		//System.out.println("p1 faction "+personOne.faction + "p2s is " + personTwo.faction);
		if(personOne.faction.equals("Neutral")||personTwo.faction.equals("Neutral")){
			//Do nothing
		}

		else if(personOne.faction.equals(personTwo.faction)){
			Random rands= new Random();
			int max= 20;
			float change1= rands.nextInt(max-0);
			float change2= rands.nextInt(max-0);
			personOne.setOpinion(personOne.opinion+change1);
			personTwo.setOpinion(personTwo.opinion+change2);
		}
		else{
			Random rands= new Random();
			int max= 20;
			float change1= rands.nextInt(max-0);
			float change2= rands.nextInt(max-0);
			personOne.setOpinion(personOne.opinion-change1);
			personTwo.setOpinion(personTwo.opinion-change2);
		}
		//System.out.println("PersonOnes initial opinion of 2 is : " + personOne.opinion + " Persontwos is "+ personTwo.opinion);
	}

	//Step in conversation
	public static ArrayList<String> conversational(Character personOne, Character personTwo, ArrayList<String> conversationHistory, ArrayList<String> converseText, String context ,ArrayList<Character> peopleList,Dictionary dictionary)
			throws IOException, JWNLException{

		ArrayList<String> responseList=new ArrayList<String>();
		//Greeting messages
		if(conversationHistory.isEmpty()){
			String introduction= "("+personOne.name +" a "+ personOne.occupation+" of the " +personOne.faction + " faction meets " + personTwo.name +" a " +personTwo.faction +" supporting " + personTwo.occupation +")";
			introduction= grammarCheck(introduction);
			conversationHistory.add(introduction);
			String inputOne= "{" +personOne.name + ": Greetings, "+personTwo.occupation +" .I'm " +personOne.name +" you?";
			String inputTwo ="{" +personTwo.name + ": My name is "+ personTwo.name + " Well met" ;
			inputOne=grammarCheck(inputOne);
			inputTwo=grammarCheck(inputTwo);
			//System.out.println(inputOne);
			//System.out.println(inputTwo);

			conversationHistory.add(inputOne);
			conversationHistory.add(inputTwo);
			String newContext= "greeting";
			conversational(personOne,personTwo, conversationHistory,converseText,newContext,peopleList,dictionary);
		}

		else{
			String previous = conversationHistory.get(conversationHistory.size()-1);
			previous= previous.substring(previous.indexOf(":")+1);
			//System.out.println("NEW PREVIOUS IS"+previous);

			if(context.contains("ERROR")){

				//System.out.println("End program--Error"+ personOne.name +": " +personOne.opinion + "..." + personTwo.name +": " + personTwo.opinion	);
				return conversationHistory;
			}
			else if(context.contains("NormalEnd")){
				//System.out.println("End program--Normal end ++++++++++++++++++++++++++++++"+ personOne.name +": " +personOne.opinion + "..." + personTwo.name +": " + personTwo.opinion	);
				return conversationHistory;
			}
			else if(context.contains("HatredEnd")){
				//System.out.println("End program--Hostiliy++++++++++++++++++++"+ personOne.name +": " +personOne.opinion + "..." + personTwo.name +": " + personTwo.opinion	);
				return conversationHistory;
			}
			else {

				String nameDone="testName";
				String response = "testResponse";
				String newContext="testContext";

				if(conversationHistory.get(conversationHistory.size()-1).contains(personOne.name)){
					nameDone= personTwo.name;
					//System.out.println("ITS THE FIRST" + nameDone);
					responseList=calculateResponse( personOne,conversationHistory,converseText, nameDone,peopleList,context,dictionary);
					response= responseList.get(0);
					newContext=responseList.get(1);
					//System.out.println(personOne.name +"Has opinion of "+ personOne.opinion +"while "+personTwo.name + "has "+personTwo.opinion);
				}
				else{
					nameDone= personOne.name;

					responseList=calculateResponse( personTwo, conversationHistory, converseText,nameDone,peopleList,context,dictionary);
					response=responseList.get(0);
					newContext=responseList.get(1);
					//System.out.println("ITS THE SECOND " + nameDone);
					//System.out.println(personOne.name +"Has opinion of "+ personOne.opinion +"while "+personTwo.name + "has "+personTwo.opinion);
				}



				response="{"+ nameDone + ":"+response + "( CharacterOne has opinion " +personOne.opinion +".Character two  has opinion " + personTwo.opinion + ")";
				//System.out.println("The response is " +response );	
				conversationHistory.add(response);
				conversational(personOne,personTwo,conversationHistory,converseText,newContext,peopleList,dictionary);

			}
		}

		return conversationHistory;


	}





	public static ArrayList<String> calculateResponse(Character person,ArrayList<String> conversationHistory,ArrayList<String> responsesUsed, String personName,ArrayList<Character> peopleList, String lastContext, Dictionary dictionary)
			throws IOException, JWNLException{

		ArrayList<String> responseList = new ArrayList<String>();
		String response="test";
		String context= "context";
		//System.out.println(personName);
		//Conversation enders.

		if(conversationHistory.size()>8){
			//System.out.println("CONVERSATION SHOULD END");
			if(person.opinion>50) {
				response="Goodbye my friend";
			}
			else if(30<person.opinion && person.opinion<=50) {
				response="Goodbye";//Cycle responses based on hatred,neutrality/liking
			}
			else {
				response="Thank goodness thats over";
			}
			context= "NormalEnd";
			responseList.add(response);
			responseList.add(context);

			return responseList;
		}


		if(person.opinion<=0){
			//System.out.println("CONVERSATION SHOULD END");
			response="I'm done talking to you";
			context= "HatredEnd";

			responseList.add(response);
			responseList.add(context);
			return responseList;
		}



		//If person is hostile to user
		else if(person.opinion>0 && person.opinion<=30){
			//System.out.println(person.name + " is hostile!");
			int positiveChance=15;
			int neutralChance=30;
			if(lastContext=="insult") {

				positiveChance=positiveChance-5;
			}
			else if(lastContext=="compliment") {
				positiveChance=positiveChance+10;
				neutralChance=neutralChance+10;
			}
			else if(lastContext=="threatInfo") {
				neutralChance=neutralChance+10;
			}

			Random rand= new Random();
			double number= Math.random() *(100-0);
			if(number<positiveChance){
				//System.out.println("Do the positive");
				responseList=complimentGenerator(person,peopleList,lastContext,responsesUsed,dictionary);
				response=responseList.get(0);
				context=responseList.get(1);
				return responseList;
			}
			else if(number>positiveChance && number<neutralChance){
				//System.out.println("Do the neutral");
				responseList=neutralGenerator(person,peopleList,lastContext,responsesUsed,dictionary);
				response=responseList.get(0);
				context=responseList.get(1);
				return responseList;
			}
			else
			{
				//System.out.println("Negative");
				responseList=insultGenerator(person,peopleList,lastContext,responsesUsed,dictionary);
				response=responseList.get(0);
				context=responseList.get(1);
				return responseList;
			}



		}
		//Neutral
		else if(person.opinion>30 && person.opinion<=60){
			//System.out.println(person.name + " is neutral!");
			int positiveChance=30;
			int neutralChance=70;

			if(lastContext=="insult") {
				positiveChance=positiveChance-10;
			}
			else if(lastContext=="compliment") {
				positiveChance=positiveChance+5;
				neutralChance=neutralChance+10;
			}
			else if(lastContext=="threatInfo") {
				neutralChance=neutralChance+10;
			}

			Random rand= new Random();
			double number= Math.random() *(100-0);
			if(number<positiveChance){
				//System.out.println("Do the positive");
				responseList=complimentGenerator(person,peopleList,lastContext,responsesUsed,dictionary);
				response=responseList.get(0);
				context=responseList.get(1);
				return responseList;
			}
			else if(number>positiveChance && number<neutralChance){
				//System.out.println("Do the neutral");
				responseList=neutralGenerator(person,peopleList,lastContext,responsesUsed,dictionary);
				response=responseList.get(0);
				context=responseList.get(1);
				return responseList;
			}
			else
			{
				//System.out.println("Negative");
				responseList=insultGenerator(person,peopleList,lastContext,responsesUsed,dictionary);
				response=responseList.get(0);
				context=responseList.get(1);
				return responseList;
			}



		}
		//If positive
		else if(person.opinion>60){
			//System.out.println(person.name + " is neutral!");

			int positiveChance= 70;
			int neutralChance= 90;

			if(lastContext=="insult") {
				positiveChance=positiveChance-10;
			}
			else if(lastContext=="compliment") {
				positiveChance=positiveChance+5;

			}
			else if(lastContext=="threatInfo") {
				neutralChance=neutralChance+5;
			}


			Random rand= new Random();
			double number= Math.random() *(100-0);
			if(number<positiveChance){
				//System.out.println("Do the positive");
				responseList=complimentGenerator(person,peopleList,lastContext,responsesUsed,dictionary);
				response=responseList.get(0);
				context=responseList.get(1);
				return responseList;
			}
			else if(number>positiveChance && number<neutralChance){
				//System.out.println("Do the neutral");
				responseList= neutralGenerator(person,peopleList,lastContext,responsesUsed,dictionary);
				response=responseList.get(0);
				context=responseList.get(1);
				return responseList;
			}
			else{
				//System.out.println("Negative");
				responseList=insultGenerator(person,peopleList,lastContext,responsesUsed,dictionary);
				response=responseList.get(0);
				context=responseList.get(1);
				return responseList;
			}
		}



		if(conversationHistory.contains(response)){
			//System.out.println("ERROR, REPETITION IN CONVERSATION");
			response="I feel I am repeating myself, I must be going";
			context="ERROR";
			return responseList;
		}




		return responseList;

	}
	//Generates hostile phrases for characters

	public static ArrayList<String> insultGenerator(Character person, ArrayList<Character> peopleList,String lastContext,ArrayList<String> responsesUsed,Dictionary dictionary) throws IOException, JWNLException{
		ArrayList<String> response= new ArrayList<String>();

		String insult="insultText";
		String insultText="InsultFullText.txt";
		String context="insult";
		String insultContent = new String(Files.readAllBytes(Paths.get(insultText)));

		List<String> insultList= new ArrayList<String>(Arrays.asList(insultContent.split("\\r?\\n")));
		Collections.shuffle(insultList);


		String insultAdjectiveText="InsultAdjectives.txt";
		String insultAdjectiveContent = new String(Files.readAllBytes(Paths.get(insultAdjectiveText)));
		List<String> insultAdjectiveList= new ArrayList<String>(Arrays.asList(insultAdjectiveContent.split("\\r?\\n")));



		String insultNounText="insultNouns.txt";

		String insultNounContent = new String(Files.readAllBytes(Paths.get(insultNounText)));

		List<String> insultNounList= new ArrayList<String>(Arrays.asList(insultNounContent.split("\\r?\\n")));

		//Prevents characters insulting members of their own faction for being in their own faction
		if(peopleList.get(0).faction==peopleList.get(1).faction){
			Iterator<String> it= insultList.iterator();
			while (it.hasNext()){
				String insul= it.next();
				if(insul.contains("[faction]")){
					it.remove();
				}
			}
		}
		//Second logic check, characters aren't going to insult eachother for being in the same occupation
		if(peopleList.get(0).occupation==peopleList.get(1).occupation){
			Iterator<String> it= insultList.iterator();
			while (it.hasNext()){
				String insul= it.next();
				if(insul.contains("[occupation]")){
					it.remove();
				}
			}
		}
		
	

		//System.out.println("new insult list "+ insultList.toString());
		
	
		insult= insultList.get(0);
		String fullInsult=insult;


		
		
		while((insult.contains("[noun]")||insult.contains("[nouns]")||insult.contains("[occupation]")||insult.contains("[occupations]"))&&(insult.contains("[metaAdjective]")&&(insult.contains("[metaNoun]")))){
			String noun= "";
			if(insult.contains("[occupation]")||insult.contains("[occupations]")) {
				
				noun=occReturner(peopleList,person,dictionary);
				String originalNoun= noun;
				
			}
			else {
				noun =insultNounList.get(0);
			}
			

			IndexWordSet NOUN= dictionary.lookupAllIndexWords(noun);
			if(NOUN.isValidPOS(POS.NOUN)) {
				noun=returnSynonyms(NOUN.getIndexWord(POS.NOUN));
				noun=noun.trim();
			}
			
			if(insult.contains("[occupations]")||insult.contains("[nouns]")){
				insult.replace("[occupations]", "[noun]");
				insult.replace("[nouns]", "[noun]");

				noun= pluralise(noun);
				
			}
			insult= insult.replace("[noun]", noun);	

			
			ArrayList<String> metaList = new ArrayList<String>(); 
			//MetaphorList cannot handle multiple words.
			if(noun.contains(" ")) {
				String originalNoun= insultNounList.get(0);
				metaList=returnMetaphorPair("%2B"+originalNoun);

			}
			else 
			{
				metaList= returnMetaphorPair("%2B"+noun);
			}
			
			//System.out.println("meta list is "+metaList.toString());
			if(metaList.size()==1) {
				//System.out.println("error");
				insult= insult.replace("[metaAdjective]", "[adjective]");
				insult= insult.replace("[metaNoun]", "[fittingNoun]");
				context="insult";
			}else {
				String metaAdj= metaList.get(0);
				String metaNoun= metaList.get(1);
				System.out.println("OLD META NOUN IS "+metaNoun);
				if(metaNoun.split(" ").length==1) {
				IndexWordSet META= dictionary.lookupAllIndexWords(metaNoun);
				if(META.isValidPOS(POS.NOUN)) {
					metaNoun=returnSynonyms(META.getIndexWord(POS.NOUN));
					//
				}
				else{
					metaNoun=metaNoun.trim();
				}
				System.out.println("NEW meta NOUN IS "+metaNoun);
				}
				insult= insult.replace("[metaAdjective]", metaAdj);
				insult= insult.replace("[metaNoun]", metaNoun);
				context="insult";
				//System.out.println("the RESPONSE now is " + response);
			}

		}
		
		
		String adjective="";
		//Uses a fitting description for adjective.
		if(insult.contains("[adjective]") && insult.contains("[fittingNoun]")){
			while(insult.contains("[adjective]") ||insult.contains("[fittingNoun]")){

				Collections.shuffle(insultAdjectiveList);
				String adjectiveReplace= insultAdjectiveList.get(0);

				IndexWord ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
				String newAdjective = returnSynonyms(ADJECTIVES);

				String fittingNoun= returnFitting(adjectiveReplace);
		
							
				while(fittingNoun.contains(adjectiveReplace)) {
					//System.out.println("shuffling");
					Collections.shuffle(insultAdjectiveList);
					adjectiveReplace= insultAdjectiveList.get(0);
					ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
					newAdjective = returnSynonyms(ADJECTIVES);
					fittingNoun= returnFitting(adjectiveReplace);

				}
				System.out.println("OLD FITTING NOUN IS "+fittingNoun);

				IndexWordSet NOUN= dictionary.lookupAllIndexWords(fittingNoun);
				if(NOUN.isValidPOS(POS.NOUN)) {
					fittingNoun=returnSynonyms(NOUN.getIndexWord(POS.NOUN));
					//
				}
				else{
					fittingNoun=fittingNoun.trim();
					//System.out.println("the insult now is " + response);
				}
				System.out.println("NEW FITTING NOUN IS "+fittingNoun);
				//System.out.println("Fitting noun is " + fittingNoun+ " adjective is "+ newAdjective);
				newAdjective=newAdjective.trim();
				fittingNoun=fittingNoun.trim();
				insult=insult.replace("[adjective]", newAdjective);

				insult=insult.replace("[fittingNoun]", fittingNoun);
				//System.out.println(insult);
			}

		}

		while(insult.contains("[adjective]")&&insult.contains("[poetic]")){

			if(insult.contains("[noun]")) {

				String noun =insultNounList.get(0);
				IndexWordSet NOUN= dictionary.lookupAllIndexWords(noun);
				if(NOUN.isValidPOS(POS.NOUN)) {
					noun=returnSynonyms(NOUN.getIndexWord(POS.NOUN));
					insult= insult.replace("[noun]", noun);	
					//System.out.println("THIS SHOULD WORK TBH");
				}
				else{
					noun=noun.trim();
					insult= insult.replace("[noun]", noun);	
					//System.out.println("the insult now is " + response);
				}
			}
			Collections.shuffle(insultAdjectiveList);
			adjective =insultAdjectiveList.get(0);
			IndexWord ADJECTIVE= dictionary.getIndexWord(POS.ADJECTIVE, adjective);
			String poetic= returnPoetic(adjective);
			while(poetic.contains(adjective)) {
				Collections.shuffle(insultAdjectiveList);
				adjective =insultAdjectiveList.get(0);
				ADJECTIVE= dictionary.getIndexWord(POS.ADJECTIVE, adjective);
				poetic= returnPoetic(adjective);

			}
			String newAdjective = returnSynonyms(ADJECTIVE);

			insult= insult.replace("[adjective]", newAdjective);
			insult= insult.replace("[poetic]", poetic);

			//System.out.println("the response now is " + insult);
		}





		if(insult.contains("[adjective]") && insult.contains("[nounAdjective]") && insult.contains("[secondAdjective]")){
			while(insult.contains("[adjective]") ||insult.contains("[nounAdjective]")||insult.contains("[secondAdjective]")){
				String complimentAdjectiveText="ComplimentAdjectives.txt";
				String complimentAdjectiveContent = new String(Files.readAllBytes(Paths.get(complimentAdjectiveText)));
				List<String> complimentAdjectiveList= new ArrayList<String>(Arrays.asList(complimentAdjectiveContent.split("\\r?\\n")));
				Collections.shuffle(complimentAdjectiveList);
				String adjectiveReplace= complimentAdjectiveList.get(0);

				IndexWord ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
				String newAdjective = returnSynonyms(ADJECTIVES);

				String coNoun= returnIronic(adjectiveReplace);
				while(coNoun.contains(adjectiveReplace)) {

					Collections.shuffle(complimentAdjectiveList);
					adjectiveReplace= complimentAdjectiveList.get(0);

					ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
					newAdjective = returnSynonyms(ADJECTIVES);

					coNoun= returnIronic(adjectiveReplace);

				}
				insult=insult.replace("[adjective]", newAdjective);
				String secondAdjective=returnSynonyms(ADJECTIVES);
				insult=insult.replace("[secondAdjective]", secondAdjective);
				insult=insult.replace("[nounAdjective]", coNoun);
				//System.out.println(insult);

			}

		}
		if(insult.contains("[adjective]") && insult.contains("[coAdjective]")){
			while(insult.contains("[adjective]") ||insult.contains("[coAdjective]")){

				Collections.shuffle(insultAdjectiveList);
				String adjectiveReplace= insultAdjectiveList.get(0);

				IndexWord ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
				String newAdjective = returnSynonyms(ADJECTIVES);

				String coAdjective= returnCoword(adjectiveReplace);
				if(coAdjective.contains(newAdjective)) {
					IndexWord CO= dictionary.getIndexWord(POS.ADJECTIVE, coAdjective);
					coAdjective=returnSynonyms(CO); 
				}
				//System.out.println("co adjective is" + coAdjective +" adjective is "+ newAdjective);
				newAdjective=newAdjective.trim();
				coAdjective=coAdjective.trim();
				insult=insult.replace("[adjective]", newAdjective);

				insult=insult.replace("[coAdjective]", coAdjective);
				//System.out.println(insult);
			}
		}
		
		
		
		
		

		//System for antonyms. I.e: "People call wizards wise but they are in fact [the opposite]!"
		while(insult.contains("[adjective]")&&insult.contains("[adjectiveOpposite]")) {
			String complimentAdjectiveText="ComplimentAdjectives.txt";
			String complimentAdjectiveContent = new String(Files.readAllBytes(Paths.get(complimentAdjectiveText)));
			List<String> complimentAdjectiveList= new ArrayList<String>(Arrays.asList(complimentAdjectiveContent.split("\\r?\\n")));
			Collections.shuffle(complimentAdjectiveList);
			String adjectiveReplace= complimentAdjectiveList.get(0);
			IndexWord ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
			String newAdjective = returnSynonyms(ADJECTIVES);

			String antonym= returnAntonym(ADJECTIVES,dictionary);
			//System.out.println(antonym);
			//IndexWord ANTONYMS= dictionary.getIndexWord(POS.ADJECTIVE, tempAntonym);
			//String antonym= returnSynonyms(ANTONYMS);
			while(antonym.equals(newAdjective)) {
				Collections.shuffle(complimentAdjectiveList);
				adjectiveReplace=complimentAdjectiveList.get(0);
				ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
				newAdjective = returnSynonyms(ADJECTIVES);

				antonym= returnAntonym(ADJECTIVES,dictionary);
				//System.out.println("Antonym is equal to the synonym for some godawful reason " +antonym);

			}

			insult=insult.replace("[adjective]", newAdjective);
			insult=insult.replace("[adjectiveOpposite]", antonym);


		}


		while(insult.contains("[adjective]")){
			Collections.shuffle(insultAdjectiveList);
			adjective =insultAdjectiveList.get(0);
			IndexWord ADJECTIVE= dictionary.getIndexWord(POS.ADJECTIVE, adjective);
			String newAdjective = returnSynonyms(ADJECTIVE);
			//returnSomething(ADJECTIVE);
			insult= insult.replace("[adjective]", newAdjective);





			//System.out.println("the insult now is " + insult);
		}
		//randomises nouns.
		while(insult.contains("[noun]")||insult.contains("[nouns]")){
			Collections.shuffle(insultNounList);
			String noun =insultNounList.get(0);
			IndexWord NOUN= dictionary.getIndexWord(POS.NOUN,noun);
			String newNoun = returnSynonyms(NOUN);
			//returnSomething(NOUN);
			if(insult.contains("[nouns]")) {
				 newNoun=pluralise(noun);
				 insult= insult.replace("[nouns]", "[noun]");
			}
			
			insult= insult.replace("[noun]", newNoun);	
			//System.out.println("the insult now is " + insult);
		}


		//System.out.println("new" + insult);
		while(insult.contains("[faction]")){
			String newFac= factionReturner(peopleList,person,dictionary);
			insult=insult.replace("[faction]", newFac);
		
		}


		while(insult.contains("[occupation]")||insult.contains("[occupations]")){
			String occupation=occReturner(peopleList,person,dictionary);
			String newOcc= occupation;
			if(insult.contains("[occupations]")) {
				newOcc=pluralise(newOcc);
				insult= insult.replace("[occupations]", "[occupation]");
			}
			insult=insult.replace("[occupation]",newOcc);

		}


		//System.out.println("----------------"+insult);
		if(lastContext.contains("insult")) {
			String abrasive= "How dare you!";
			insult = abrasive +" " +insult;
		}
		else if(lastContext.contains("threatInfo")){
			String abrasive= "I couldn't care less..";
			insult = abrasive +" " +insult;
		}
		//Prevents system from using incorrect words
		String blackListText= "blacklist.txt";
		String blackListContent = new String(Files.readAllBytes(Paths.get(blackListText)));
		List<String> blackList= new ArrayList<String>(Arrays.asList(blackListContent.split("\\r?\\n")));
		for(String str:blackList) {
			//System.out.println(str);
			if(insult.contains(str)) {
				//System.out.println("RESPONSE CONTAINS BLACKLISTED WORD " +str);
				return insultGenerator(person,peopleList,lastContext,responsesUsed,dictionary);
				
			}
		}

		//Opinion change
		for(Character c : peopleList){
			if(c.name!=person.name){
				Random rands= new Random();
				int max= 20;
				float change= rands.nextInt(max-0);
				//System.out.println("PERSON : "+ c.name +" IS CHANGED");
				float newOpinion= c.opinion-max;
				c.setOpinion(newOpinion);
			}
		}


		

		insult= grammarCheck(insult);
		//Was considering adding a responses used system but it does not function correctly, ignore for now
		//responsesUsed.add(fullInsult);
		response.add(insult);
		response.add(context);
		return response;

	}

	//Generates a complimeItnt from a list of template compliments
	public static ArrayList<String> complimentGenerator(Character person, ArrayList<Character> peopleList,String lastContext,ArrayList<String> responsesUsed,Dictionary dictionary) throws IOException, JWNLException{
		ArrayList<String> response = new ArrayList<String>();
		String compliment="compliment";
		String complimentText="Compliments.txt";
		String context="compliment";
		String complimentContent = new String(Files.readAllBytes(Paths.get(complimentText)));

		List<String> complimentList= new ArrayList<String>(Arrays.asList(complimentContent.split("\\r?\\n")));
		Collections.shuffle(complimentList);


		String complimentNounText="ComplimentNouns.txt";
		String complimentNounContent = new String(Files.readAllBytes(Paths.get(complimentNounText)));
		List<String> complimentNounList= new ArrayList<String>(Arrays.asList(complimentNounContent.split("\\r?\\n")));
		Collections.shuffle(complimentNounList);


		String complimentAdjectiveText="ComplimentAdjectives.txt";
		String complimentAdjectiveContent = new String(Files.readAllBytes(Paths.get(complimentAdjectiveText)));
		List<String> complimentAdjectiveList= new ArrayList<String>(Arrays.asList(complimentAdjectiveContent.split("\\r?\\n")));
		Collections.shuffle(complimentAdjectiveList);

		if(peopleList.get(0).faction!=peopleList.get(1).faction){
			Iterator<String> it= complimentList.iterator();
			while (it.hasNext()){
				String comp= it.next();
				if(comp.contains("[faction]")){
					it.remove();
				}
			}
		}
		compliment= complimentList.get(0);


		while(((compliment.contains("[noun]")||compliment.contains("[nouns]")||compliment.contains("[occupation]")||compliment.contains("[occupations]"))&&(compliment.contains("[metaAdjective]")&&compliment.contains("[metaNoun]")))){
			String noun= "";
			if(compliment.contains("[occupation]")||compliment.contains("[occupations]")) {
				noun=occReturner(peopleList,person,dictionary);
			}
			else {
				noun =complimentNounList.get(0);
			}
			
			
			IndexWordSet NOUN= dictionary.lookupAllIndexWords(noun);
			if(NOUN.isValidPOS(POS.NOUN)) {
				noun=returnSynonyms(NOUN.getIndexWord(POS.NOUN));
				noun=noun.trim();
					
			}
			if(compliment.contains("[occupations]")||compliment.contains("[nouns]")){
				compliment.replace("[occupations]", "[noun]");
				compliment.replace("[nouns]", "[noun]");
				noun=pluralise(noun);
			}

			
			compliment= compliment.replace("[noun]", noun);

			ArrayList<String> metaList = new ArrayList<String>(); 
			//MetaphorList cannot handle multiple words.
			if(noun.contains(" ")) {
				String originalNoun= complimentNounList.get(0);
				metaList=returnMetaphorPair("%2B"+originalNoun);

			}
			else 
			{
				metaList= returnMetaphorPair("%2B"+noun);
			}
			//System.out.println("meta list is "+metaList.toString());
			if(metaList.size()==1) {
				//System.out.println("error");
				compliment= compliment.replace("[metaAdjective]", "[adjective]");
				compliment= compliment.replace("[metaNoun]", "[fittingNoun]");
				context="compliment";
			}else {
				String metaAdj= metaList.get(0);
				String metaNoun= metaList.get(1);
				

				System.out.println("OLD META NOUN IS "+metaNoun);
				if(metaNoun.split(" ").length==1) {
				IndexWordSet META= dictionary.lookupAllIndexWords(metaNoun);
				if(META.isValidPOS(POS.NOUN)) {
					metaNoun=returnSynonyms(META.getIndexWord(POS.NOUN));
					//
				}
				else{
					metaNoun=metaNoun.trim();
					//System.out.println("the insult now is " + response);
				}
				System.out.println("NEW meta NOUN IS "+metaNoun);
				}
				compliment= compliment.replace("[metaAdjective]", metaAdj);
				compliment= compliment.replace("[metaNoun]", metaNoun);
				context="compliment";
				//System.out.println("the RESPONSE now is " + response);
			}

		}





		String adjective="";
		while(compliment.contains("[adjective]")&&compliment.contains("[poetic]")){

			if(compliment.contains("[noun]") ||compliment.contains("[nouns]")) {
				
				String noun =complimentNounList.get(0);
				IndexWordSet NOUN= dictionary.lookupAllIndexWords(noun);
				if(NOUN.isValidPOS(POS.NOUN)) {
					noun=returnSynonyms(NOUN.getIndexWord(POS.NOUN));
					//System.out.println("THIS SHOULD WORK TBH");
				}
				else{


				}
				if(compliment.contains("[nouns]")) {
					noun=pluralise(noun);
					compliment= compliment.replace("[nouns]", "[noun]");
				}

				compliment= compliment.replace("[noun]", noun);	

			}
			Collections.shuffle(complimentAdjectiveList);
			adjective =complimentAdjectiveList.get(0);
			IndexWord ADJECTIVE= dictionary.getIndexWord(POS.ADJECTIVE, adjective);
			String poetic= returnPoetic(adjective);
			while(poetic.contains(adjective)) {
				Collections.shuffle(complimentAdjectiveList);
				adjective =complimentAdjectiveList.get(0);
				ADJECTIVE= dictionary.getIndexWord(POS.ADJECTIVE, adjective);
				poetic= returnPoetic(adjective);

			}
			String newAdjective = returnSynonyms(ADJECTIVE);
			compliment= compliment.replace("[adjective]", newAdjective);
			compliment= compliment.replace("[poetic]", poetic);
			//System.out.println("the response now is " + compliment);
		}











		if(compliment.contains("[adjective]") && compliment.contains("[coAdjective]")){
			while(compliment.contains("[adjective]") ||compliment.contains("[coAdjective]")){

				Collections.shuffle(complimentAdjectiveList);
				String adjectiveReplace= complimentAdjectiveList.get(0);

				IndexWord ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
				String newAdjective = returnSynonyms(ADJECTIVES);

				String coAdjective= returnCoword(adjectiveReplace);
				if(coAdjective.contains(newAdjective)) {
					Collections.shuffle(complimentAdjectiveList);
					adjectiveReplace= complimentAdjectiveList.get(0);

					 ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
					 newAdjective = returnSynonyms(ADJECTIVES);

					 coAdjective= returnCoword(adjectiveReplace);

					IndexWord CO= dictionary.getIndexWord(POS.ADJECTIVE, coAdjective);
					coAdjective=returnSynonyms(CO); 
				}

				IndexWord CO= dictionary.getIndexWord(POS.ADJECTIVE, coAdjective);
				coAdjective=returnSynonyms(CO);
				//System.out.println("co adjective is" + coAdjective +" adjective is "+ newAdjective);
				newAdjective=newAdjective.trim();
				coAdjective=coAdjective.trim();
				compliment=compliment.replace("[adjective]", newAdjective);

				compliment=compliment.replace("[coAdjective]", coAdjective);
				//System.out.println(compliment);
			}
		}
		//Choosing a fitting noun
		if(compliment.contains("[adjective]") && compliment.contains("[fittingNoun]")){
			while(compliment.contains("[adjective]") ||compliment.contains("[fittingNoun]")){

				Collections.shuffle(complimentAdjectiveList);
				String adjectiveReplace= complimentAdjectiveList.get(0);

				IndexWord ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
				String newAdjective = returnSynonyms(ADJECTIVES);

				String fittingNoun= returnFitting(adjectiveReplace);
				
				
				
				
				
				while(fittingNoun.contains(adjectiveReplace)) {
					//System.out.println("shuffling");
					Collections.shuffle(complimentAdjectiveList);
					adjectiveReplace= complimentAdjectiveList.get(0);
					ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
					newAdjective = returnSynonyms(ADJECTIVES);
					fittingNoun= returnFitting(adjectiveReplace);

				}
				
				System.out.println("OLD FITTING NOUN IS "+fittingNoun);
				if(fittingNoun.split(" ").length==1) {
				IndexWordSet NOUN= dictionary.lookupAllIndexWords(fittingNoun);
				if(NOUN.isValidPOS(POS.NOUN)) {
					fittingNoun=returnSynonyms(NOUN.getIndexWord(POS.NOUN));
					//
				}
				else{
					fittingNoun=fittingNoun.trim();
					//System.out.println("the insult now is " + response);
				}
				System.out.println("NEW FITTING NOUN IS "+fittingNoun);
				}
				//System.out.println("Fitting noun is " + fittingNoun+ " adjective is "+ newAdjective);
				newAdjective=newAdjective.trim();
				fittingNoun=fittingNoun.trim();
				compliment=compliment.replace("[adjective]", newAdjective);
				compliment=compliment.replace("[fittingNoun]", fittingNoun);
				//System.out.println(compliment);
			}

		}

		//System for antonyms. I.e: "People call wizards wise but they are in fact [the opposite]!"
				while(compliment.contains("[adjective]")&&compliment.contains("[adjectiveOpposite]")) {
					String insultAdjectiveText="insultAdjectives.txt";
					String insultAdjectiveContent = new String(Files.readAllBytes(Paths.get(complimentAdjectiveText)));
					List<String> insultAdjectiveList= new ArrayList<String>(Arrays.asList(complimentAdjectiveContent.split("\\r?\\n")));
					Collections.shuffle(insultAdjectiveList);
					String adjectiveReplace= insultAdjectiveList.get(0);
					IndexWord ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
					String newAdjective = returnSynonyms(ADJECTIVES);

					String antonym= returnAntonym(ADJECTIVES,dictionary);
					//System.out.println(antonym);
					//IndexWord ANTONYMS= dictionary.getIndexWord(POS.ADJECTIVE, tempAntonym);
					//String antonym= returnSynonyms(ANTONYMS);
					while(antonym.equals(newAdjective)) {
						Collections.shuffle(insultAdjectiveList);
						adjectiveReplace=insultAdjectiveList.get(0);
						ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
						newAdjective = returnSynonyms(ADJECTIVES);

						antonym= returnAntonym(ADJECTIVES,dictionary);
						//System.out.println("Antonym is equal to the synonym for some godawful reason " +antonym);

					}

					compliment=compliment.replace("[adjective]", newAdjective);
					compliment=compliment.replace("[adjectiveOpposite]", antonym);


				}

		
		
		while(compliment.contains("[adjective]")){
			Collections.shuffle(complimentAdjectiveList);
			String adj= complimentAdjectiveList.get(0);
			IndexWord ADJECTIVE= dictionary.getIndexWord(POS.ADJECTIVE, adj);
			String newAdj= returnSynonyms(ADJECTIVE);
			//returnSomething(ADJECTIVE);
			compliment= compliment.replace("[adjective]",newAdj);
		}


		while(compliment.contains("[noun]")){
			Collections.shuffle(complimentNounList);
			String noun= complimentNounList.get(0);
			IndexWord NOUN= dictionary.getIndexWord(POS.NOUN, noun);
			String newNoun= returnSynonyms(NOUN);
			compliment= compliment.replace("[noun]",newNoun);
		}



		while(compliment.contains("[occupation]")||compliment.contains("[occupations]")){
			
			String occupation = occReturner(peopleList,person,dictionary);
			//System.out.println("occupation is "+ occupation);
			if(compliment.contains("[occupations]")) {
				occupation=pluralise(occupation);
				compliment= compliment.replace("[occupations]", "[occupation]");
			}
			compliment=compliment.replace("[occupation]", occupation);
		}

		while(compliment.contains("[faction]")){

			String newFact= factionReturner(peopleList,person,dictionary);
			compliment=compliment.replace("[faction]", newFact);
		}
		if(lastContext.contains("insult")) {
			compliment = "I'm sorry you feel this way, anyway.."+compliment;
		}
		
		String blackListText= "blacklist.txt";
		String blackListContent = new String(Files.readAllBytes(Paths.get(blackListText)));
		List<String> blackList= new ArrayList<String>(Arrays.asList(blackListContent.split("\\r?\\n")));
		for(String str:blackList) {
			//System.out.println(str);
			if(compliment.contains(str)) {
				//System.out.println("RESPONSE CONTAINS BLACKLISTED WORD " +str);
				return complimentGenerator(person,peopleList,lastContext,responsesUsed,dictionary);
				
			}
		}

		//System.out.println("----------------"+compliment);
		for(Character c : peopleList){
			if(c.name!=person.name){
				Random rands= new Random();
				int max= 20;
				float change= rands.nextInt(max-0);
				//System.out.println("PERSON : "+ c.name +" IS CHANGED");
				float newOpinion= c.opinion+max;
				c.setOpinion(newOpinion);
			}
		}

		



		compliment= grammarCheck(compliment);
		response.add(compliment);
		response.add(context);
		return response;

	}



	//Literally any random chatter, also have different contexts such as threats to areas safety, taxes, magic or some stuff
	public static ArrayList<String> neutralGenerator(Character person, ArrayList<Character> peopleList,String lastContext,ArrayList<String> responsesUsed,Dictionary dictionary) throws IOException, JWNLException{
		String response= "Neutral response";
		String context= "context";
		ArrayList<String> responseList= new ArrayList<String>();
		String neutralText="neutral.txt";

		String neutralContent = new String(Files.readAllBytes(Paths.get(neutralText)));
		List<String> neutralList= new ArrayList<String>(Arrays.asList(neutralContent.split("\\r?\\n")));
		Collections.shuffle(neutralList);

		String threatText="threat.txt";
		String threatContent = new String(Files.readAllBytes(Paths.get(threatText)));
		List<String> threatList= new ArrayList<String>(Arrays.asList(threatContent.split("\\r?\\n")));
		Collections.shuffle(threatList);

		String adjectiveText="adjective.txt";
		String adjectiveContent = new String(Files.readAllBytes(Paths.get(adjectiveText)));
		List<String> adjectiveList= new ArrayList<String>(Arrays.asList(adjectiveContent.split("\\r?\\n")));
		Collections.shuffle(adjectiveList);
		String adjective= adjectiveList.get(0);

		response=neutralList.get(0);

		//System.out.println("The response "+ response);

		while((response.contains("[threat]")||response.contains("[threats]"))&&response.contains("[metaAdjective]")&&response.contains("[metaNoun]")){
			String threat =threatList.get(0);
			IndexWordSet THREAT= dictionary.lookupAllIndexWords(threat);
			if(THREAT.isValidPOS(POS.NOUN)) {
				threat=returnSynonyms(THREAT.getIndexWord(POS.NOUN));
				threat=threat.trim();
					
				//System.out.println("THIS SHOULD WORK TBH");
			}
			
			response= response.replace("[threat]", threat);
			ArrayList<String> metaList = new ArrayList<String>(); 
			//MetaphorList cannot handle multi word threats
			if(threat.contains(" ")) {
				String originalThreat= threatList.get(0);
				metaList=returnMetaphorPair("-"+originalThreat);

			}
			else 
			{
				metaList= returnMetaphorPair("-"+threat);
			}
			if(response.contains("[threats]")) {
				threat= pluralise(threat);
				response= response.replace("[threats]", "[threat]");
			}
			
			//System.out.println("meta list is "+metaList.toString());
			if(metaList.size()==1) {
				//System.out.println("error");
				response= response.replace("[threat]", threat);

				response= response.replace("[metaAdjective]", "[adjective]");
				response= response.replace("[metaNoun]", "[poetic]");
				
				context="threatInfo";
			}else {
				String metaAdj= metaList.get(0);
				String metaNoun= metaList.get(1);
				
				System.out.println("OLD META NOUN IS "+metaNoun);
				if(metaNoun.split(" ").length==1) {
				IndexWordSet META= dictionary.lookupAllIndexWords(metaNoun);
				if(META.isValidPOS(POS.NOUN)) {
					metaNoun=returnSynonyms(META.getIndexWord(POS.NOUN));
					//
				}
				else{
					metaNoun=metaNoun.trim();
					//System.out.println("the insult now is " + response);
				}
				System.out.println("NEW meta NOUN IS "+metaNoun);
				}
				response= response.replace("[threat]", threat);
				response= response.replace("[metaAdjective]", metaAdj);
				
				response= response.replace("[metaNoun]", metaNoun);
				context="threatInfo";
				//System.out.println("the RESPONSE now is " + response);
			}

		}

		
		
		
		if(response.contains("[adjective]") && response.contains("[fittingNoun]")){
			while(response.contains("[adjective]") ||response.contains("[fittingNoun]")){

				Collections.shuffle(adjectiveList);
				String adjectiveReplace= adjectiveList.get(0);

				IndexWord ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
				String newAdjective = returnSynonyms(ADJECTIVES);

				String fittingNoun= returnFitting(adjectiveReplace);
				while(fittingNoun.contains(adjectiveReplace)) {
					//System.out.println("shuffling");
					Collections.shuffle(adjectiveList);
					adjectiveReplace= adjectiveList.get(0);
					ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
					newAdjective = returnSynonyms(ADJECTIVES);
					fittingNoun= returnFitting(adjectiveReplace);

				}
				//System.out.println("Fitting noun is " + fittingNoun+ " adjective is "+ newAdjective);
				newAdjective=newAdjective.trim();
				fittingNoun=fittingNoun.trim();
				response=response.replace("[adjective]", newAdjective);

				response=response.replace("[fittingNoun]", fittingNoun);
				//System.out.println(response);
			}

		}

		
		
		
		//System.out.println("HAS IT ESCAPED?");
		//Poetic/adjective replacement- Jigsaw bard
		while(response.contains("[adjective]")&&response.contains("[poetic]")){

			if(response.contains("[threat]")) {

				String threat =threatList.get(0);
				IndexWordSet THREAT= dictionary.lookupAllIndexWords(threat);
				if(THREAT.isValidPOS(POS.NOUN)) {
					threat=returnSynonyms(THREAT.getIndexWord(POS.NOUN));
					response= response.replace("[threat]", threat);	
					//System.out.println("THIS SHOULD WORK TBH");
				}
				else{

					response= response.replace("[threat]", threat);	
					context="threatInfo";
					//System.out.println("the insult now is " + response);
				}
			}
			Collections.shuffle(adjectiveList);
			adjective =adjectiveList.get(0);
			IndexWord ADJECTIVE= dictionary.getIndexWord(POS.ADJECTIVE, adjective);
			String poetic= returnPoetic(adjective);
			while(poetic.contains(adjective)) {
				Collections.shuffle(adjectiveList);
				adjective =adjectiveList.get(0);
				ADJECTIVE= dictionary.getIndexWord(POS.ADJECTIVE, adjective);
				poetic= returnPoetic(adjective);

			}
			String newAdjective = returnSynonyms(ADJECTIVE);

			response= response.replace("[adjective]", newAdjective);
			response= response.replace("[poetic]", poetic);

			//System.out.println("the response now is " + response);
		}


		
		if(response.contains("[adjective]") && response.contains("[coAdjective]")){
			while(response.contains("[adjective]") ||response.contains("[coAdjective]")){

				Collections.shuffle(adjectiveList);
				String adjectiveReplace= adjectiveList.get(0);

				IndexWord ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
				String newAdjective = returnSynonyms(ADJECTIVES);

				String coAdjective= returnCoword(adjectiveReplace);
				if(coAdjective.contains(newAdjective)) {
					IndexWord CO= dictionary.getIndexWord(POS.ADJECTIVE, coAdjective);
					coAdjective=returnSynonyms(CO); 
				}
				IndexWord CO= dictionary.getIndexWord(POS.ADJECTIVE, coAdjective);
				coAdjective=returnSynonyms(CO); 

				//System.out.println("co adjective is" + coAdjective +" adjective is "+ newAdjective);
				newAdjective=newAdjective.trim();
				coAdjective=coAdjective.trim();
				response=response.replace("[adjective]", newAdjective);

				response=response.replace("[coAdjective]", coAdjective);
				//System.out.println(response);
			}
		}
		
		//System for antonyms. I.e: "People call wizards wise but they are in fact [the opposite]!"
				while(response.contains("[adjective]")&&response.contains("[adjectiveOpposite]")) {
					String responseAdjectiveText="adjective.txt";
					String responseAdjectiveContent = new String(Files.readAllBytes(Paths.get(responseAdjectiveText)));
					List<String> responseAdjectiveList= new ArrayList<String>(Arrays.asList(responseAdjectiveContent.split("\\r?\\n")));
					Collections.shuffle(responseAdjectiveList);
					String adjectiveReplace= responseAdjectiveList.get(0);
					IndexWord ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
					String newAdjective = returnSynonyms(ADJECTIVES);

					String antonym= returnAntonym(ADJECTIVES,dictionary);
					//System.out.println(antonym);
					//IndexWord ANTONYMS= dictionary.getIndexWord(POS.ADJECTIVE, tempAntonym);
					//String antonym= returnSynonyms(ANTONYMS);
					while(antonym.equals(newAdjective)) {
						Collections.shuffle(responseAdjectiveList);
						adjectiveReplace=responseAdjectiveList.get(0);
						ADJECTIVES= dictionary.getIndexWord(POS.ADJECTIVE, adjectiveReplace);
						newAdjective = returnSynonyms(ADJECTIVES);

						antonym= returnAntonym(ADJECTIVES,dictionary);
						//System.out.println("Antonym is equal to the synonym for some godawful reason " +antonym);

					}

					response=response.replace("[adjective]", newAdjective);
					response=response.replace("[adjectiveOpposite]", antonym);


				}

		
		
		
		
		
		//If the response just contains a mention of a threat and nothing else.
		while(response.contains("[threat]")||response.contains("[threats]")){

			String threat =threatList.get(0);
			IndexWordSet THREAT= dictionary.lookupAllIndexWords(threat);
			if(THREAT.isValidPOS(POS.NOUN)) {
				threat=returnSynonyms(THREAT.getIndexWord(POS.NOUN));
				response= response.replace("[threat]", threat);	
				//System.out.println("THIS SHOULD WORK TBH");
			}
	
				if(response.contains("[threats]")) {
					threat=pluralise(threat);
					response= response.replace("[threats]", "[threat]");
				}
				response= response.replace("[threat]", threat);	
				context="threatInfo";
				//System.out.println("the response now is " + response);
			
		}


		while(response.contains("[adjective]")){

			Collections.shuffle(adjectiveList);
			adjective =adjectiveList.get(0);
			IndexWord ADJECTIVE= dictionary.getIndexWord(POS.ADJECTIVE, adjective);

			String newAdjective = returnSynonyms(ADJECTIVE);

			response= response.replace("[adjective]", newAdjective);
			//System.out.println("the response now is " + response);
		}

		//If all current responses fail, the noun will be used as a generic replacement that could potentially still result in a conversation
		while(response.contains("[noun]")||response.contains("[nouns]")){

			String nounText= adjective +"Text";
			if(adjective!="a"&&Files.exists(Paths.get(nounText))) {
				String nounContent = new String(Files.readAllBytes(Paths.get(nounText)));
				List<String> nounList= new ArrayList<String>(Arrays.asList(nounContent.split("\\r?\\n")));
				Collections.shuffle(nounList);
			}
			else {
				nounText="insultNouns.txt";
				String nounContent = new String(Files.readAllBytes(Paths.get(nounText)));
				List<String> nounList= new ArrayList<String>(Arrays.asList(nounContent.split("\\r?\\n")));
				String noun =nounList.get(0);
				IndexWord NOUN= dictionary.getIndexWord(POS.NOUN,noun);
				String newNoun = returnSynonyms(NOUN);
				if(response.contains("[nouns]")) {
					newNoun=pluralise(newNoun);
					response= response.replace("[nouns]", "[noun]");
				}
				//returnSomething(NOUN);

				response= response.replace("[noun]", newNoun);	
				//System.out.println("the insult now is " + response);	
			}

		}

		//Replaces number
		while(response.contains("[number]")) {
			//System.out.println(response);
			Random rand = new Random();
			int numbers=2+rand.nextInt(18);
			String number= ""+numbers;  
			response=response.replace("[number]", number);

		}
		//System.out.println(response);

		while(response.contains("[occupation]")||response.contains("[occupations]")){
			String occupation=occReturner(peopleList,person,dictionary);
			if(response.contains("[occupations]")) {
				occupation=pluralise(occupation);
				response=response.replace("[occupations]", "[occupation]");
			}
			response=response.replace("[occupation]", occupation);
			
		}

		String locationText= "location.txt";
		String locationContent = new String(Files.readAllBytes(Paths.get(locationText)));
		List<String> locationList= new ArrayList<String>(Arrays.asList(locationContent.split("\\r?\\n")));
		Collections.shuffle(locationList);
		while(response.contains("[location]")){
			String location=locationList.get(0);
			IndexWordSet LOC = dictionary.lookupAllIndexWords(location);
			if(LOC.isValidPOS(POS.NOUN)) {
				location= returnSynonyms(LOC.getIndexWord(POS.NOUN));
				response=response.replace("[location]", location);
			}else {

				response=response.replace("[location]", location);
			}
		}
		if(lastContext.contains("insult")) {
			String placate= "Hey! Lets avoid fighting. Anyway changing the subject..";
			response= placate+response;
		}
		String blackListText= "blacklist.txt";
		String blackListContent = new String(Files.readAllBytes(Paths.get(blackListText)));
		List<String> blackList= new ArrayList<String>(Arrays.asList(blackListContent.split("\\r?\\n")));
		for(String str:blackList) {
			//System.out.println(str);
			if(response.contains(str)) {
				//System.out.println("RESPONSE CONTAINS BLACKLISTED WORD " +str);
				return neutralGenerator(person,peopleList,lastContext,responsesUsed,dictionary);
				
			}
		}
		response= grammarCheck(response);
		responseList.add(response);
		responseList.add(context);
		return responseList;

	}

	public static String pluralise(String word) {
		String response=word;
		char finalChar= response.charAt(response.length()-1);
		//System.out.println("RESPONSE Before IS "+ response);
		String replacer="s";
		if(response.endsWith("y")) {
			replacer="ies";
			response=response.replaceAll("y$", replacer);
		}
		else if(response.endsWith("ch")) {
			replacer="es";
			response=response.replaceAll("ch$", replacer);
		}
		else if(response.endsWith("x")) {
			replacer="es";
			response=response.replaceAll("x$", replacer);
		}
		else if(response.endsWith("z")) {
			replacer="es";
			response=response.replaceAll("z$", replacer);
		}
		else if(response.endsWith("o")) {
			replacer="s";
			response=response.replaceAll("o$", replacer);
		}
		else if(response.endsWith("s")) {
			replacer="es";
			response=response+replacer;
		}
		
		else {
			response=response+"s";
		}
		//System.out.println("RESPONSE IS "+ response);
		return response;

	}

	public static String grammarCheck(String response) {
		String resp=response;

		String returnThis= "";
		resp=resp.trim();
		resp=resp.replaceAll("\\s+"," ");
		String [] complimentArray= resp.split(" ");

		//System.out.println("PREVIOUS COMP ARRAY= " +Arrays.toString(complimentArray));
		for(int x=0; x<complimentArray.length-1;x++) {

			String wordOne= complimentArray[x].trim();
			String wordTwo= complimentArray[x+1].trim();
			if(wordOne.isEmpty()) {

			}
			else if(x==0) {
				char first= wordOne.charAt(x);
				String replacement="" + first;
				replacement=replacement.toUpperCase();

				//System.out.println(wordOne.length());
				if(wordOne.length()>1) {


					wordOne= replacement+wordOne.substring(1);
					complimentArray[x]=wordOne;
				}
				else {
					wordOne=replacement;
					complimentArray[x]=wordOne;

				}

			}
			else {
				String vowels= "aeiou";
				String punctuation = "?!.";
				if((wordOne.equals("a")||wordOne.equals("an")) && wordTwo.isEmpty()==false ) {
					String firstLetter= ""+wordTwo.charAt(0);

					if(vowels.contains(firstLetter)) {
						complimentArray[x]= "an";
					}else {
						complimentArray[x]="a";
					}

				}
				else if((punctuation.contains(wordOne)&& wordTwo.isEmpty()==false)||
						(punctuation.contains(""+wordOne.charAt(wordOne.length()-1))&& wordTwo.isEmpty()==false)) {
					String firstChar= ""+ wordTwo.charAt(0);
					firstChar=firstChar.toUpperCase();
					String word= wordTwo;
					if(wordTwo.length()>1) {
						word= firstChar+ wordTwo.substring(1);}
					else {
						word=firstChar;
					}

					complimentArray[x+1]=word;
				}

			}


		}


		for(String s: complimentArray) {
			returnThis=returnThis+" "+s;
		}

		return returnThis;


	}
	
	public static String occReturner(ArrayList<Character> peopleList, Character person, Dictionary dictionary) throws JWNLException {
		String occupation="occ";
		for(Character x: peopleList){
			if(x!=person){
				occupation=x.occupation;
			}
		}
		IndexWordSet OCCUPATION = dictionary.lookupAllIndexWords(occupation);
		String newOcc= occupation;
		if(OCCUPATION.isValidPOS(POS.NOUN)){
			//System.out.println(OCCUPATION.getIndexWord(POS.NOUN).toString());
			newOcc= returnSynonyms(OCCUPATION.getIndexWord(POS.NOUN));
			newOcc=newOcc.trim();

		}else {
			newOcc=newOcc.trim();
		}

		return newOcc;
	}


	public static String factionReturner(ArrayList<Character> peopleList, Character person, Dictionary dictionary) throws JWNLException {
		String faction="faction";
		for(Character x: peopleList){
			if(x!=person){
				faction=x.faction;
			}
		}
		IndexWordSet FACTION = dictionary.lookupAllIndexWords(faction);
		String newFac= faction;
		if(FACTION.isValidPOS(POS.NOUN)){
			//System.out.println(FACTION.getIndexWord(POS.NOUN).toString());
			newFac= returnSynonyms(FACTION.getIndexWord(POS.NOUN));
			newFac=newFac.trim();
		}else {
			newFac=newFac.trim();
		}
		return newFac;
	}



}
