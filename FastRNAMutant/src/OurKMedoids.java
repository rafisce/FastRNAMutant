
import java.util.HashMap;
import java.util.Random;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;



public class OurKMedoids implements Clusterer{
	private OurDistanceMeasure dm;
	  
	  private int numberOfClusters;
	  
	  private Random rg;
	  
	  private int maxIterations;
	  
	  public OurKMedoids() {
	    this(4, 100, (OurDistanceMeasure)new OurDistance());
	  }
	  
	  public OurKMedoids(int numberOfClusters, int maxIterations, OurDistanceMeasure dm) {
	    this.numberOfClusters = numberOfClusters;
	    this.maxIterations = maxIterations;
	    this.dm = dm;
	    this.rg = new Random(System.currentTimeMillis());
	  }
	  
	  public Dataset[] cluster(Dataset data) {
	    Instance[] medoids = new Instance[this.numberOfClusters];
	    DefaultDataset[] arrayOfDefaultDataset = new DefaultDataset[this.numberOfClusters];
	    for (int i = 0; i < this.numberOfClusters; i++) {
	      int random = this.rg.nextInt(data.size());
	      medoids[i] = data.instance(random);
	    } 
	    boolean changed = true;
	    int count = 0;
	    double now=0;
	    while (changed && count < this.maxIterations) {
	    	now++;
	    	System.out.println((now/maxIterations)*100+"% ");
	      changed = false;
	      count++;
	      int[] assignment = assign(medoids, data);
	      changed = recalculateMedoids(assignment, medoids, (Dataset[])arrayOfDefaultDataset, data);
	    } 
	    return (Dataset[])arrayOfDefaultDataset;
	  }
	  
	  private int[] assign(Instance[] medoids, Dataset data) {
	    int[] out = new int[data.size()];
	    for (int i = 0; i < data.size(); i++) {
	      double bestDistance = this.dm.measure(data.instance(i), medoids[0]);
	      int bestIndex = 0;
	      for (int j = 1; j < medoids.length; j++) {
	        double tmpDistance = this.dm.measure(data.instance(i), medoids[j]);
	        if (this.dm.compare(tmpDistance, bestDistance)) {
	          bestDistance = tmpDistance;
	          bestIndex = j;
	        } 
	      } 
	      out[i] = bestIndex;
	    } 
	    return out;
	  }
	  
	  private boolean recalculateMedoids(int[] assignment, Instance[] medoids, Dataset[] output, Dataset data) {
	    boolean changed = false;
	    for (int i = 0; i < this.numberOfClusters; i++) {
	      output[i] = (Dataset)new DefaultDataset();
	      for (int j = 0; j < assignment.length; j++) {
	        if (assignment[j] == i)
	          output[i].add(data.instance(j)); 
	      } 
	      if (output[i].size() == 0) {
	        medoids[i] = data.instance(this.rg.nextInt(data.size()));
	        changed = true;
	      } else {
	    	  HashMap<String, Double> mapData=new  HashMap<String, Double>();
	        Instance centroid = OurDatasetTools.OurAverage(output[i],this.dm,mapData);
	        Instance oldMedoid = medoids[i];
	        //medoids[i] = data.kNearest(1, centroid, this.dm).iterator().next();
	        //We make our KNearst
	        double[] distanceArray = new double[data.size()];
	        for (int j=0;j<data.size();j++) {
	        	distanceArray[j]=this.dm.measure(data.instance(j), centroid);
	        }
	        int[] indexSort=new int[data.size()];
	        for (int k=0;k<indexSort.length;k++)
	        	indexSort[k]=k;
	        for (int n = 0; n < distanceArray.length; n++) {
	            for (int m = 0; m < distanceArray.length; m++) {
	                if (distanceArray[n] < distanceArray[m]) {
	                    double tempValue = distanceArray[n];
	                    distanceArray[n] = distanceArray[m];
	                    distanceArray[m] = tempValue;
	                    int tempIndex=indexSort[n];
	                    indexSort[n]=indexSort[m];
	                    indexSort[m]=tempIndex;
	                }
	            }
	        }
	        for(int k=0;k<this.numberOfClusters;k++) {
	        	medoids[k]=data.instance(indexSort[k]);
	        }
	        	
	        if (!medoids[i].classValue().toString().equals(oldMedoid.classValue().toString()))
	          changed = true; 
	      } 
	    } 
	    return changed;
	  }
	  public Dataset[] OurCluster(Dataset data,HashMap<String, Double> mapData) {
		    Instance[] medoids = new Instance[this.numberOfClusters];
		    DefaultDataset[] arrayOfDefaultDataset = new DefaultDataset[this.numberOfClusters];
		    for (int i = 0; i < this.numberOfClusters; i++) {
		      int random = this.rg.nextInt(data.size());
		      medoids[i] = data.instance(random);
		    } 
		    boolean changed = true;
		    int count = 0;
		    while (changed && count < this.maxIterations) {
		      changed = false;
		      count++;
		      int[] assignment = OurAssign(medoids, data,mapData);
		      changed = OurRecalculateMedoids(assignment, medoids, (Dataset[])arrayOfDefaultDataset, data,mapData);
		    } 
		    return (Dataset[])arrayOfDefaultDataset;
	  }
	  private int[] OurAssign(Instance[] medoids, Dataset data,HashMap<String, Double> mapData) {
		    int[] out = new int[data.size()];
		    for (int i = 0; i < data.size(); i++) {
		      double bestDistance = this.dm.OurMeasure(data.instance(i), medoids[0],mapData);
		      int bestIndex = 0;
		      for (int j = 1; j < medoids.length; j++) {
		        double tmpDistance = this.dm.OurMeasure(data.instance(i), medoids[j],mapData);
		        if (this.dm.compare(tmpDistance, bestDistance)) {
		          bestDistance = tmpDistance;
		          bestIndex = j;
		        } 
		      } 
		      out[i] = bestIndex;
		    } 
		    return out;
	  }
	  private boolean OurRecalculateMedoids(int[] assignment, Instance[] medoids, Dataset[] output, Dataset data,HashMap<String, Double> mapData) {
		    boolean changed = false;
		    for (int i = 0; i < this.numberOfClusters; i++) {
		      output[i] = (Dataset)new DefaultDataset();
		      for (int j = 0; j < assignment.length; j++) {
		        if (assignment[j] == i)
		          output[i].add(data.instance(j)); 
		      } 
		      if (output[i].size() == 0) {
		        medoids[i] = data.instance(this.rg.nextInt(data.size()));
		        changed = true;
		      } else {
		        Instance centroid = OurDatasetTools.OurAverage(output[i],this.dm,mapData);
		        Instance oldMedoid = medoids[i];
		        //medoids[i] = data.kNearest(1, centroid, this.dm).iterator().next();
		        //We make our KNearst
		        double[] distanceArray = new double[data.size()];
		        for (int j=0;j<data.size();j++) {
		        	distanceArray[j]=this.dm.OurMeasure(data.instance(j), centroid,mapData);
		        }
		        int[] indexSort=new int[data.size()];
		        for (int k=0;k<indexSort.length;k++)
		        	indexSort[k]=k;
		        for (int n = 0; n < distanceArray.length; n++) {
		            for (int m = 0; m < distanceArray.length; m++) {
		                if (distanceArray[n] < distanceArray[m]) {
		                    double tempValue = distanceArray[n];
		                    distanceArray[n] = distanceArray[m];
		                    distanceArray[m] = tempValue;
		                    int tempIndex=indexSort[n];
		                    indexSort[n]=indexSort[m];
		                    indexSort[m]=tempIndex;
		                }
		            }
		        }
		        medoids[i]=data.instance(indexSort[1]);
		        
		        	
		        if (!medoids[i].classValue().toString().equals(oldMedoid.classValue().toString()))
		          changed = true; 
		      } 
		    } 
		    return changed;
		  }
		  
		  
}
