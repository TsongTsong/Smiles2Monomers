package coverageCalculator;

import java.util.List;

import algorithms.utils.Coverage;
import model.Polymer;

public interface Calculator {
	
	List<Coverage> process(Polymer pol, Coverage cov);
}
