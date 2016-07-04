package coverageCalculator;

import java.util.List;

import algorithms.utils.Coverage;
import model.Polymer;

public class ProcessCoverageGain {

	public List<Coverage> process(Polymer pol, Coverage cov, Calculator cal){
		
		return cal.process(pol, cov);
	}
}
