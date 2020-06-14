import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ViennaRNA {

	ViennaRNA(){}
	
	public RNAInfo RNAfold(String sequence) {	
		ArrayList<String> lines = new ArrayList<String>();
		try {
		    Process process = Runtime.getRuntime().exec("RNAfold");
		    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		    writer.write(sequence);
		    writer.close();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		    String line;
		    while ((line = reader.readLine()) != null) {
		    	//System.out.println(line);
	    		lines.add(line);
		    }
		    reader.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}

		String[] parts = lines.get(1).split(" ");
		RNAInfo rnainfo=new RNAInfo(parts[0],parts[parts.length-1].substring(0, parts[parts.length-1].length()-1));
		return rnainfo;   
	}
	
	public RNAMultiInfo RNAsubopt(String sequence,double e_range) {
		RNAMultiInfo rnamultiinfo=new RNAMultiInfo();
		ArrayList<String> lines = new ArrayList<String>();
		try {
		    Process process = Runtime.getRuntime().exec("RNAsubopt.exe --deltaEnergy="+String.valueOf(e_range)+" --noLP");
		    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		    writer.write(sequence);
		    writer.close();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		    String line;
		    int flag=0;
		    while ((line = reader.readLine()) != null) {
		    	if (flag==0) {
		    		//System.out.println(line);
		    		flag=1;
		    	}
		    	else
		    	{
		    		lines.add(line);
		    		String[] parts = line.split(" ");
		    		rnamultiinfo.setStructure(parts[0]);
		    		rnamultiinfo.setEnergy(parts[parts.length-1]);	
		    	} 
		    }
		    reader.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return rnamultiinfo;
	}
	
	public String RNAdistance(String str1,String str2) {
		int sum=0;
		ArrayList<Integer> stack=new ArrayList<Integer>();
		int[] arr1=new int[str1.length()];
		int[] arr2=new int[str1.length()];
		
		for (int i = 0; i < str1.length(); i++) {
			arr1[i] = -1;
			arr2[i] = -1;
		}
		
		for (int i = 0; i < str1.length(); i++) {
			if (str1.charAt(i) == '(') 
				stack.add(i);
			if (str1.charAt(i) == ')') {
				int place = stack.get(stack.size()-1);
				stack.remove(stack.size()-1);
				arr1[place] = i;
			}
		}
		
		for (int i = 0; i < str2.length(); i++) {
			if (str2.charAt(i) == '(') 
				stack.add(i);
			if (str2.charAt(i) == ')') {
				int place = stack.get(stack.size()-1);
				stack.remove(stack.size()-1);
				arr2[place] = i;
			}
		}
		
		for (int i = 0; i < str1.length(); i++) {
			if ((arr1[i] == -1 && arr2[i] != -1) || (arr1[i] != -1 && arr2[i] == -1)) 
				sum++;
			else if (arr1[i] != arr2[i]) 
				sum += 2;
		}
		
		return String.valueOf(sum);
	}
	
	/*
	public String RNAdistance(String str1,String str2) {
		ArrayList<String> lines = new ArrayList<String>();
		String distance="";
		try {
		    Process process = Runtime.getRuntime().exec("RNAdistance.exe");
		    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		    writer.write(str1 + "\n");
		    writer.write(str2+ "\n");
		    writer.close();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		    String line;
		    while ((line = reader.readLine()) != null) {
		    		lines.add(line);
		    		String[] parts = line.split(" ");
		    		return parts[1];
	    	} 
		    	
		    reader.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return distance;
	}
	*/
	
	public String RNAeval(String str1,String str2) {
		ArrayList<String> lines = new ArrayList<String>();
		String eval="";
		try {//
		    Process process = Runtime.getRuntime().exec("RNAeval.exe");
		    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		    writer.write(str1 + "\n");
		    writer.write(str2);
		    writer.close();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		    String line;
		    int flag=0;
		    while ((line = reader.readLine()) != null) {
		    	if (flag==0) {
		    		flag=1;
		    	}
		    	else if(flag!=0) {
		    		lines.add(line);
		    		String[] parts = line.split(" ");
		    		if (parts[parts.length-1].substring(0,parts[parts.length-1].length()-1).charAt(0)=='(')
		    			return parts[parts.length-1].substring(1,parts[parts.length-1].length()-1);
		    		return parts[parts.length-1].substring(0,parts[parts.length-1].length()-1);
		    	}
	    	} 
		    	
		    reader.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return eval;
	}
	
	public String RNAfoldConstraint(String str1,String str2) {
		ArrayList<String> lines = new ArrayList<String>();
		String eval="";
		try {//
		    Process process = Runtime.getRuntime().exec("RNAfold -C --enforceConstraint");
		    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		    writer.write(str1 + "\n");
		    writer.write(str2);
		    writer.close();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		    String line;
		    int flag=0;
		    while ((line = reader.readLine()) != null) {
		    	if (flag==0) {
		    		flag=1;
		    	}
		    	else if(flag!=0) {
		    		lines.add(line);
		    		String[] parts = line.split(" ");
		    		//return parts[0];
		    		
		    		if (parts[parts.length-1].substring(0,parts[parts.length-1].length()-1).charAt(0)=='(')
		    			return parts[parts.length-1].substring(1,parts[parts.length-1].length()-1);
		    		return parts[parts.length-1].substring(0,parts[parts.length-1].length()-1);
		    		
		    	}
	    	} 
		    	
		    reader.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return eval;
	}
	
}
