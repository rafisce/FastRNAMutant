import java.io.Serializable;
import java.util.HashMap;
import java.lang.Double;
import net.sf.javaml.core.Instance;

public interface OurDistanceMeasure extends Serializable {
  double measure(Instance paramInstance1, Instance paramInstance2);
  
  double OurMeasure(Instance paramInstance1, Instance paramInstance2, HashMap<String, Double> dataMap);
  
  boolean compare(double paramDouble1, double paramDouble2);
  
  double getMinValue();
  
  double getMaxValue();
}