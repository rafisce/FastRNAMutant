
public class RNAInfo {
	private String structure;
	private String energy;
	
	RNAInfo(String structure,String energy){
		this.structure=structure;
		if (energy.charAt(0)!='(')
			this.energy=energy;
		else
			this.energy=energy.substring(1);
	}
	public String getStructure() {
		return this.structure;
	}
	public String getEnergy() {
		return this.energy;
	}
}
