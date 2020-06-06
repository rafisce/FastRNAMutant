import java.util.HashMap;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.distance.DistanceMeasure;

public final class OurDatasetTools {
	  
	  public static Instance average(Dataset data,DistanceMeasure dm) {
		  double[] tmpOut = new double[data.size()];
		  for (int i = 0; i < data.size(); i++) {
			  double sum =0;
			  for (int j = 0; j < data.size(); j++)
				  sum += dm.measure(data.instance(i), data.instance(j)); 
			  tmpOut[i] = sum / data.size();
		  }
		  int index=0;
		  double minValue=tmpOut[0];
		  for (int i = 0; i < tmpOut.length; i++) {
			  if (tmpOut[i]<minValue) {
				  minValue=tmpOut[i];
				  index=i;
			  }
		  }	  
		  Object obj=data.classValue(index);
		  SparseInstance sparseinstance = new SparseInstance(0, obj);
		  Instance instance=sparseinstance;
		  return instance;
	  }
	  public static Instance extremum(Dataset data,Dataset centroid,int group,DistanceMeasure dm) {
		  double[] tmpOut = new double[data.size()];
		  for (int i = 0; i < data.size(); i++) {
			  double sum =0;
			  for (int j = 0; j < centroid.size(); j++)
				  if (j!=group)
					  sum += dm.measure(data.instance(i), centroid.instance(j)); 
			  tmpOut[i] = sum;
		  }
		  int index=0;
		  double minValue=tmpOut[0];
		  for (int i = 0; i < tmpOut.length; i++) {
			  if (tmpOut[i]<minValue) {
				  minValue=tmpOut[i];
				  index=i;
			  }
		  }	  
		  Object obj=data.classValue(index);
		  SparseInstance sparseinstance = new SparseInstance(0, obj);
		  Instance instance=sparseinstance;
		  return instance;
	  }
	  public static Instance OurAverage(Dataset data,OurDistanceMeasure dm,HashMap<String, Double> mapData) {
		  double[] tmpOut = new double[data.size()];
		  if(tmpOut.length==0) {
			  return null;
		  }
			  
		  for (int i = 0; i < data.size(); i++) {
			  double sum =0;
			  for (int j = 0; j < data.size(); j++)
				  sum += dm.OurMeasure(data.instance(i), data.instance(j),mapData); 
			  tmpOut[i] = sum / data.size();
		  }
		  int index=0;
		  double minValue=tmpOut[0];
		  for (int i = 0; i < tmpOut.length; i++) {
			  if (tmpOut[i]<minValue) {
				  minValue=tmpOut[i];
				  index=i;
			  }
		  }	  
		  Object obj=data.classValue(index);
		  SparseInstance sparseinstance = new SparseInstance(0, obj);
		  Instance instance=sparseinstance;
		  return instance;
	  }
	  public static Instance OurExtremum(Dataset data,Dataset centroid,int group,OurDistanceMeasure dm,HashMap<String, Double> mapData) {
		  double[] tmpOut = new double[data.size()];
		  for (int i = 0; i < data.size(); i++) {
			  double sum =0;
			  for (int j = 0; j < centroid.size(); j++)
				  if (j!=group)
				  	sum += dm.OurMeasure(data.instance(i), centroid.instance(j),mapData); 
			  tmpOut[i] = sum;
		  }
		  int index=0;
		  double maxValue=tmpOut[0];
		  for (int i = 0; i < tmpOut.length; i++) {
			  if (tmpOut[i]>maxValue) {
				  maxValue=tmpOut[i];
				  index=i;
			  }
		  }	  
		  Object obj=data.classValue(index);
		  SparseInstance sparseinstance = new SparseInstance(0, obj);
		  Instance instance=sparseinstance;
		  return instance;
	  }
}
