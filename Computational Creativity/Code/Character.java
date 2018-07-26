
public class Character {
	public String name; //Name affects nothing but is necessary enough
	public String occupation; //Occupation will affect its general title and potentially insults
	public String faction; //Potentially, neutral, King, Pretender, will add more if more variety needed but atm just an opinion buff during conversations. 
	//Atm one can assume its just a minor split and not an all out war if NPCs are talking to eachother.
	public float opinion; // Opinion of person you're talking to, needs to be changable if insulted/complimented
	
							 
	
	public Character(){
		name= "Test name";
		occupation= "test";
		opinion=50;
	}
	
	
	
	public void setName(String newName){
		name = newName;
	}
	public void setOcc(String newOccupation){
		occupation=newOccupation;
	}
	public void setFaction(String newFact){
		faction = newFact;
	}
	
	public void setOpinion(float changedOpinion){
		if(changedOpinion>=100) {
			opinion=100;
		}else {
		opinion= changedOpinion;
		}
		
	}
	public float getOpinion(){
		return opinion;
	}
	
	
}
