import java.util.ArrayList;

public class RNAMultiInfo {
	
	private ArrayList<String> structure;
	private ArrayList<String> energy;
	
	RNAMultiInfo(){
		this.structure=new ArrayList<String>();
		this.energy=new ArrayList<String>();
	}
	public void setStructure(String str){
		this.structure.add(str);
	}
	
	public void setEnergy(String str){
		this.energy.add(str);
	}

	public String getStructure(int index){
		return this.structure.get(index);
	}
	
	public String getEnergy(int index){
		return this.energy.get(index);
	}
	
	public int getSize(){
		return this.structure.size();
	}
}
