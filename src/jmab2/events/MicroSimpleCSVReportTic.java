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
package jmab2.events;

import java.util.List;
import java.util.Map;

import jmab2.report.MicroSimpleCSVReport;
import jmab2.simulations.MacroSimulation;
import net.sourceforge.jabm.Simulation;
import net.sourceforge.jabm.event.SimulationFinishedEvent;
import net.sourceforge.jabm.event.SimulationStartingEvent;

/**
 * @author Simon Hess
 *
 */
public class MicroSimpleCSVReportTic extends MacroTicEvent{
	
	List<MicroSimpleCSVReport> reports;
	
	public MicroSimpleCSVReportTic(){

	}

	public void report() {
		for(MicroSimpleCSVReport r: reports) {
			r.compute((MacroSimulation)this.getSimulationController().getSimulation());
		}
	}
	
	public void initialise(){
		Simulation sim = this.getSimulationController().getSimulation();
		for(MicroSimpleCSVReport r: reports) {
			r.getCSVreportVariable().initialise(new SimulationStartingEvent(sim));;
		}
	}
	
	public void dispose(){
		Simulation sim = this.getSimulationController().getSimulation();
		for(MicroSimpleCSVReport r: reports) {
			r.getCSVreportVariable().dispose(new SimulationFinishedEvent(sim));
		}
	}

	public List<MicroSimpleCSVReport> getReports() {
		return reports;
	}

	public void setReports(List<MicroSimpleCSVReport> reports) {
		this.reports = reports;
	}
	
	
}
