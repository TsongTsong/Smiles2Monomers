package io.html;

import io.imgs.PictureCoverageGenerator.ColorsMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import algorithms.utils.Coverage;
import db.FamilyDB;
import db.MonomersDB;

public class HTMLPeptidesCoverageVue extends HTMLAbstractVue {
	
	public HTMLPeptidesCoverageVue(List<Coverage> coverages, Map<Coverage, ColorsMap> allColors, MonomersDB monoDB, File coverageDir, FamilyDB families) {
		super();
		
		for (Coverage cov : coverages) {
			cov.setFamilies(families);
		}
		Collections.sort(coverages);
		
		List<ArrayList<Coverage>> lac = splitCoveragesList(coverages);
		
		for(ArrayList<Coverage> ac : lac){
			
			//for (Coverage cov : ac) {
				
				//ColorsMap cm = allColors.get(cov);
				//HTMLColoredCoverageVue ccv = new HTMLColoredCoverageVue(cov, cm, monoDB, coverageDir, families);
				HTMLColoredCoverageVue ccv = new HTMLColoredCoverageVue(ac, allColors, monoDB, coverageDir, families);
				// HTML
				this.html += "<div class='coverage'>";
				this.html += ccv.getHTML();
				this.html += "</div>";
				
				// CSS
				this.css.putAll(ccv.css);
				this.addToCSS(".coverage", "border", "black solid 1px");
				this.addToCSS(".coverage", "padding", "10px");
				this.addToCSS(".coverage", "margin-bottom", "20px");
			//}
		}
		
	}

	// Split List<Coverage> to List<ArrayList<Coverage>>
	private List<ArrayList<Coverage>> splitCoveragesList(List<Coverage> coverages){
		List<ArrayList<Coverage>> covsLargeGroup = new ArrayList<ArrayList<Coverage>>();
		
		for(int i=0; i<coverages.size()-1; i++){
			ArrayList<Coverage> covsSmallGroup = new ArrayList<>();
			covsSmallGroup.add(coverages.get(i));
			
			for(int j=i+1; j<coverages.size(); j++){
				if(!(coverages.get(i).getChemicalObject().getName()
						.equals(coverages.get(j).getChemicalObject().getName()))){
					i=j-1;
					break;
				}
				
				covsSmallGroup.add(coverages.get(j));
			}
			covsLargeGroup.add(covsSmallGroup);
		}
		
		return covsLargeGroup;
	}
	
	@Override
	public void updateVue() {}

}
