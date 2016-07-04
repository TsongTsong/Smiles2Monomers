package coverageCalculator;

import java.util.ArrayList;
import java.util.List;

import algorithms.utils.Coverage;
import model.Polymer;

public class CalculatorMIP_TM implements Calculator{
	
	private int modulationDepth;

	public CalculatorMIP_TM(int modulationDepth){
		this.modulationDepth = modulationDepth;
	}

	@Override
	public List<Coverage> process(Polymer pol, Coverage cov) {

		List<Coverage> covsList = new ArrayList<>();
		Calculator cal_mip = new CalculatorMIP();
		Calculator cal_tm = new CalculatorTM(this.modulationDepth);
		
		List<Coverage> covsList_mip = new ArrayList<>();
		covsList_mip = cal_mip.process(pol, cov);
		covsList.addAll(covsList_mip);
		
		List<Coverage> covsList_tm = new ArrayList<>();
		covsList_tm = cal_tm.process(pol, cov);
		covsList.addAll(covsList_tm);
		
		return covsList;
	}

}
