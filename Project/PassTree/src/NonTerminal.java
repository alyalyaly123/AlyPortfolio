import java.util.ArrayList;
import java.util.Arrays;

public class NonTerminal extends Terminal {
	
	private ArrayList<String> destinat= new ArrayList<String>();
	
	   public NonTerminal(char namePeram, String destinationsPeram)   {
		   super(namePeram);
		   destinat.add( destinationsPeram);

	   }
	   
	
	public void addDestination(String destination){
		destinat.add(destination);
	}
	public ArrayList<String> getDestination(){
		return destinat;
	}
}
