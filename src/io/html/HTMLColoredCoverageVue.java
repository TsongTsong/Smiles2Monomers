package io.html;

import java.awt.Color;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import algorithms.utils.Coverage;
import db.FamilyDB;
import db.MonomersDB;
import io.imgs.PictureCoverageGenerator.ColorsMap;
import model.Monomer;
import model.Polymer;
import model.Residue;

public class HTMLColoredCoverageVue extends HTMLAbstractVue {

	private Coverage coverage;
	private ArrayList<Coverage> coverList;
	
	private MonomersDB monosDB;
	private File coverageDir;
	private FamilyDB families;
	
	private ColorsMap cm;
	private Map<Coverage, ColorsMap> allColors;

	public HTMLColoredCoverageVue(ArrayList<Coverage> ac, Map<Coverage, ColorsMap> allColors, MonomersDB monoDB, File coverageDir, FamilyDB families) {
		super();
		this.coverList=ac;
		
		this.monosDB = monoDB;
		this.coverageDir = coverageDir;
		this.families = families;
		
		this.allColors=allColors;
		
		DecimalFormat df = new DecimalFormat("0.000");
		
		if(ac.size()==0 || ac.size()==1){
			this.html 
	        = "<div>" 
	        + "<p><b>" 
			+ this.coverList.get(0).getChemicalObject().getName() 
			+"</b><br/><br/>"
			+ac.size()+" coverages found"
			+  "</p>"
	        +  "</div>";
			
			this.html += this.createPeptideInfos();
			this.html += this.createMonomerLists();
		}
		else{
			this.html 
	        = "<div>" 
	        + "<p><b>" 
			+ this.coverList.get(0).getChemicalObject().getName()
			+"</b><br/><br/>"
			+ac.size()+" coverages found."
			+"&nbsp;&nbsp;&nbsp;&nbsp;"
			+"To show the best "
			+"<select id='countSelector_"+ this.coverList.get(0).getChemicalObject().getName() +"' "
					+ " onchange='changeCoveragesCount(parseInt(this.value), \""+this.coverList.get(0).getChemicalObject().getName()+"\");'>";
			
			for(int i=0; i<ac.size(); i++){
				if(i==ac.size()-1){
					String option = "<option selected='selected' value='" +(i+1) +"'>"+ (i+1)+"</option>";
					this.html = this.html+option;
				}
				else{
					this.html += "<option value='" +(i+1) +"'>"+ (i+1) +"</option>";
				}		
			}
			
			
			this.html 
			+= "</select>"
			+" coverages,"
			+"&nbsp;&nbsp;"
			+ "<select id='covsToShowList_"+this.coverList.get(0).getChemicalObject().getName()
					+ "' onchange='changeCoverage(parseInt(this.value), \""+this.coverList.get(0).getChemicalObject().getName()+"\");'>";
	
			for(int i=0; i<ac.size(); i++){
				if(i==0){
					String option0 = "<option selected='selected' value='" +(i+1) +"'>Coverage "+ (i+1)
													+"  Ratio: "+df.format(ac.get(i).getCoverageRatio()) +"</option>";
					this.html = this.html+option0;
				}
				else{
					this.html += "<option value='" +(i+1) +"'>Coverage "+ (i+1)
													+"  Ratio: "+df.format(ac.get(i).getCoverageRatio()) +"</option>";
				}		
			}

			this.html
					+= "</select>"
					+  "</p>"
					+  "</div>"
					+  "<div class = 'covsContainer_"+this.coverList.get(0).getChemicalObject().getName()+"' style='position:relative;'>";
			
			this.html += "<div id='leftArrow_"+this.coverList.get(0).getChemicalObject().getName()+
					"' style='width:50px; height:50px; position:absolute; top:80px; border:1px solid blue; cursor:pointer; "
					+ "background:url(left.png); background-size:50px 50px; background-repeat:no-repeat; left:0px; right:20px;' "
					+ " onclick='getPreCoverage(\""+this.coverList.get(0).getChemicalObject().getName()+"\");'>"+"</div>";
			
			this.html += this.createPeptideInfos();
			this.html += this.createMonomerLists();
			
			this.html += "<div id='rightArrow_"+this.coverList.get(0).getChemicalObject().getName()+
					"' style='width:50px; height:50px; position:absolute; top:80px; border:1px solid blue; cursor:pointer; "
					+ "background:url(right.png); background-size:50px 50px; background-repeat:no-repeat;right:0px;' "
					+ " onclick='getNextCoverage(\""+this.coverList.get(0).getChemicalObject().getName()+"\");'>"+"</div>";
			this.html += "</div>";
		}		
		
	}
	
	private String createPeptideInfos() {
		String pepHTML = "";
		
		// Create coverage image
		String path = this.coverageDir.getPath();
		int splitIdx = 0; int slash = 0;
		for (int i=path.length()-1 ; i>=0 ; i--) {
			splitIdx = i;
			if (path.charAt(i) == '/') {
				slash++;
				if (slash == 2)
					break;
			}
		}
		path = this.coverageDir.getPath().substring(splitIdx+1);
		
		// Create HTML
		for(int i=0; i<this.coverList.size();i++){
			this.coverage = this.coverList.get(i);
			
			if(i==0){
				pepHTML += "<div  style='display:block' class='peptide' id='"+ this.coverage.getChemicalObject().getName()+(i+1) +"'>\n";
			}
			else{
				pepHTML += "<div style='display:none' class='peptide' id='"+ this.coverage.getChemicalObject().getName()+(i+1) +"'>\n";
			}
						
			String name = this.coverage.getChemicalObject().getName().replaceAll("\\s", "_");
			pepHTML += "<img src='" + path + "/" + this.coverage.getChemicalObject().getId()+"_"+ name + i + ".png'"
					+ " class='covImage' />\n";
			double coverageRatio = Math.floor(this.coverage.getCoverageRatio()*1000.0)/1000.0;
			double correctness = Math.floor(this.coverage.getCorrectness(this.families)*1000.0)/1000.0;
			pepHTML += "<div>"
					+ "	<p>Atomic coverage : " + coverageRatio + "</p>";
			if (((Polymer)this.coverage.getChemicalObject()).getMonomeres().length != 0)
				pepHTML += "	<p>Correctness : " + correctness + "</p>";
			pepHTML += "</div></div>\n";
			
			// Create CSS
			this.addToCSS(".covImage", "width", "500px");
			this.addToCSS(".covImage", "height", "500px");
			this.addToCSS(".peptide>*", "display", "inline-block");
			this.addToCSS(".peptide>*", "vertical-align", "middle");
		}
				
		return pepHTML;
	}

	private String createMonomerLists() {
		String monosHTML = "";
		
		for(int i=0; i<this.coverList.size(); i++){
			this.coverage = this.coverList.get(i);
			
			this.cm = allColors.get(this.coverage);
			
			if(i==0){
				monosHTML += "<div style='display:block' class='monomers' id='"+ this.coverage.getChemicalObject().getName()+(i+1+this.coverList.size()) +"'>\n";
			}else{
				monosHTML += "<div style='display:none' class='monomers' id='"+ this.coverage.getChemicalObject().getName()+(i+1+this.coverList.size()) +"'>\n";
			}

			if (((Polymer)this.coverage.getChemicalObject()).getMonomeres().length > 0) {
				Map<String, Integer> correctMonomers = this.coverage.getCorrectMonomers (this.families);
				Map<String, List<Color>> correctColors = this.calculateColorsof(correctMonomers.keySet());
				Map<String, Integer> incorrectMonomers = this.coverage.getIncorrectMonomers(this.families);
				Map<String, List<Color>> incorrectColors = this.calculateColorsof(incorrectMonomers.keySet());
				Map<String, Integer> notfoundMonomers = this.coverage.getNotFoundMonomers(this.families);
				
				// Correct
				monosHTML += "<p>Correct monomers</p>";
				monosHTML += this.createColoredList("correct", correctMonomers, correctColors);
				
				// Incorrect
				monosHTML += "<p>Incorrect monomers</p>";
				monosHTML += this.createColoredList("incorrect", incorrectMonomers, incorrectColors);
				
				// Not found
				monosHTML += "<p>Not found monomers</p>";
				monosHTML += this.createColoredList("notFound", notfoundMonomers);
			} else {
				monosHTML += "<p>Monomers</p>";
				Map<String, Integer> monos = this.coverage.getIncorrectMonomers(this.families);				
				Map<String, List<Color>> colors = this.calculateColorsof(monos.keySet());
				monosHTML += this.createColoredList("correct", monos, colors);
				
			}
			monosHTML +="</div>\n";
			// CSS
			this.addToCSS(".list>div", "display", "inline-block");
			this.addToCSS(".list", "white-space", "nowrap");
			this.addToCSS(".list", "overflow", "auto");
		}
		
		return monosHTML;
	}

	private Map<String, List<Color>> calculateColorsof(Set<String> set) {
		Map<String, List<Color>> colors = new HashMap<>();
		for (String name : set) {
			List<Color> c = colors.containsKey(name) ? colors.get(name) : new ArrayList<Color>();
			for (Residue res : this.cm.keySet()) {
				if (this.families.areInSameFamily(name, res.getMonoName())) {
					c.addAll(this.cm.get(res));
				}
			}
			colors.put(name, c);
		}
		
		return colors;
	}

	private String createColoredList(String string, Map<String, Integer> monomers) {
		return this.createColoredList(string, monomers, new HashMap<String, List<Color>>());
	}

	private String createColoredList (String listName, Map<String, Integer> monomers, Map<String, List<Color>> colors) {
		String monosHTML = "";
		
		monosHTML += "<div class='list' id='" + listName + "'>\n";
		for (String name : monomers.keySet()) {
			// HTML for each Residue matched.
			Monomer monomer = null;
			try {
				monomer = this.monosDB.getObject(name);
			} catch (NullPointerException e) {
				System.err.println(e.getMessage());
				for (Monomer candidate : monosDB.getObjects()) {
					if (candidate.getName().toLowerCase().equals(name.toLowerCase())) {
						monomer = candidate;
						break;
					}
				}
			}
			List<Color> col = colors.containsKey(name) ? colors.get(name) : new ArrayList<Color>();
			String code = monomer == null ? name + "_unloaded" : monomer.getCode();
			HTMLMonomerShortVue hmsv = new HTMLMonomerShortVue(code, monomers.get(name), col);
			monosHTML += hmsv.getHTML();
			
			// CSS for each Residue matched.
			Map<String, Map<String, String>> properties = hmsv.getCSSProperties();
			for (String property : properties.keySet()) {
				Map<String, String> localProperty = this.css.containsKey(property) ? this.css.get(property) : new HashMap<String, String>();
				for (Entry<String, String> value : properties.get(property).entrySet())
					localProperty.put(value.getKey(), value.getValue());
				this.css.put(property, localProperty);
			}
		}
		monosHTML += "</div>\n";
		
		return monosHTML;
	}

	@Override
	public void updateVue() {}
	
}
