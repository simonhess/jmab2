/*
 * JMAB - Java Macroeconomic Agent Based Modeling Toolkit
 * Copyright (C) 2013 Alessandro Caiani and Antoine Godin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package jmab2.report;

import java.util.Map;

import jmab2.events.MicroSimEvent;
import jmab2.simulations.MacroSimulation;
import net.sourceforge.jabm.Simulation;
import net.sourceforge.jabm.event.SimulationStartingEvent;
import net.sourceforge.jabm.report.CSVReportVariables;
import net.sourceforge.jabm.report.CSVWriter;
import net.sourceforge.jabm.util.MutableStringWrapper;

/**
 * @author Simon Hess
 * 
 * This reports creates a csv report for a specific micro variable. In comparison with regular reports this report is not included
 *  in the simulation's registered reports and works independently of events (Except for the dummy event used in the compute method). 
 *
 */
public class MicroSimpleCSVReport {

	private MicroMultipleVariablesComputer computer;
	
	private CSVReportVariables CSVreportVariable;
	
	public MicroSimpleCSVReport() {
		
	}
	
	public void compute(MacroSimulation sim) {
		
		Map<Long,Double> values = this.getValues(sim);
		MicroSimEvent eventToSend = new MicroSimEvent(sim.getRound(), values,0);
		CSVreportVariable.compute(eventToSend);
		
	}

	public Map<Long,Double> getValues(MacroSimulation sim){
		return computer.computeVariables(sim);
	}
	
	public MicroMultipleVariablesComputer getComputer() {
		return computer;
	}

	public void setComputer(MicroMultipleVariablesComputer computer) {
		this.computer = computer;
	}


	public CSVReportVariables getCSVreportVariable() {
		return CSVreportVariable;
	}


	public void setCSVreportVariable(CSVReportVariables cSVreportVariable) {
		CSVreportVariable = cSVreportVariable;
	}
	
}
