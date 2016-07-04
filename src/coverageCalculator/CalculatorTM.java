package coverageCalculator;

import java.util.ArrayList;
import java.util.List;

import algorithms.isomorphism.indepth.Modulation_2;
import algorithms.utils.Coverage;
import model.Polymer;

/*
 * TM: Tiling & Modulation
 */
public class CalculatorTM implements Calculator{
	
	private static boolean verbose = false;
	private Modulation_2 modulation;
	
	public CalculatorTM(int modulationDepth){
		this.modulation = new Modulation_2(modulationDepth);
	}

	@Override
	public List<Coverage> process(Polymer pol, Coverage cov) {
		List<Coverage> covs = new ArrayList<>();
		
		cov.calculateGreedyCoverage();
		if (cov.getCoverageRatio() < 1.0) {
			if (verbose)
				System.out.println("++Modulation");
			covs = this.modulation.modulate(cov);
		}
		else{
			covs.add(cov);
		}
		
		return covs;
	}

}
