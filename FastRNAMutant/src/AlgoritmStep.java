

import java.util.HashMap;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;


public class AlgoritmStep {
	
	AlgoritmStep(){}
	
	public RNAInfo findOptimal(String RNAsequence)
	{
		ViennaRNA viennaRNA=new ViennaRNA();
		return viennaRNA.RNAfold(RNAsequence);
	}
	public RNAMultiInfo findSubOptimal(String RNAsequence,double e_range)
	{
		ViennaRNA viennaRNA=new ViennaRNA();
		return viennaRNA.RNAsubopt(RNAsequence,e_range);
	}

	public double[][] makeDistanceMatrix(RNAMultiInfo RNAsubOptimal)
	{
		ViennaRNA viennaRNA=new ViennaRNA();
		int sizeMatrix=RNAsubOptimal.getSize();
		double[][] matrixDistance=new double[sizeMatrix][sizeMatrix];
		for (int i=0;i<sizeMatrix;i++) {
			for (int j=0;j<sizeMatrix;j++) {
				if (i<j) {
					matrixDistance[i][j]=Double.parseDouble(viennaRNA.RNAdistance(RNAsubOptimal.getStructure(i), RNAsubOptimal.getStructure(j)));
					matrixDistance[j][i]=matrixDistance[i][j];
				}
			}
		}
		return matrixDistance;
	}
	
	public Dataset createDataSetForKMedoids(RNAMultiInfo RNAsubOptimal)
	{
		DefaultDataset dataSet=new DefaultDataset();
		int size=RNAsubOptimal.getSize();
		for (int i=0;i<size;i++) {
			Object obj=RNAsubOptimal.getStructure(i);
			SparseInstance sparseinstance = new SparseInstance(0, obj);
			Instance instance=sparseinstance;
			System.out.println(instance.classValue().toString()+"add to dataset");
			dataSet.add(instance);
		}
		return dataSet;
	}
	
	
	public Dataset[] findCluster(Dataset dataset, int k,HashMap<String, Double> mapData)
	{
		OurKMedoids kmedoids=new OurKMedoids(k,150, (OurDistanceMeasure)new OurDistance());
		return kmedoids.OurCluster(dataset, mapData);
	}
	
	public Dataset findCentroids(Dataset[] dataset,HashMap<String, Double> mapData)
	{
		
		DefaultDataset centroids=new DefaultDataset();
		for (int i=0;i<dataset.length;i++) {
			Instance centroid = OurDatasetTools.OurAverage(dataset[i],(OurDistanceMeasure)new OurDistance(),mapData);
			if (centroid==null)
				return null;
			centroids.add(centroid);
		}
		return centroids;
	}
	public Dataset findFar(Dataset[] dataset,Dataset centroids,HashMap<String, Double> mapData)
	{
		
		DefaultDataset fars=new DefaultDataset();
		for (int i=0;i<dataset.length;i++) {
			Instance far = OurDatasetTools.OurExtremum(dataset[i],centroids,i,(OurDistanceMeasure)new OurDistance(),mapData);
			fars.add(far);
		}
		return fars;
	}
	
	
}
