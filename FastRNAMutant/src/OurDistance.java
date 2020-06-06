import java.util.HashMap;

import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.AbstractDistance;
import java.lang.Double;

public class OurDistance extends AbstractDistance implements OurDistanceMeasure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public double measure(Instance x, Instance y) {
		ViennaRNA viennaRNA= new ViennaRNA();
		return Double.parseDouble(viennaRNA.RNAdistance(x.classValue().toString(), y.classValue().toString()));
	}
	
	public double OurMeasure(Instance x, Instance y,HashMap<String, Double> dataMap) {
		return dataMap.get(x.classValue().toString()+y.classValue().toString());
	}
}