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

import jmab2.agents.GoodSupplier;
import jmab2.population.MacroPopulation;
import jmab2.simulations.MacroSimulation;
import net.sourceforge.jabm.Population;
import net.sourceforge.jabm.agent.Agent;

/**
 * @author Alessandro Caiani and Antoine Godin
 *
 */
public class FirmsProfitComputer implements MacroVariableComputer {
	
	int populationId;
	int pastProfitId;

	/**
	 * @return the populationId
	 */
	public int getPopulationId() {
		return populationId;
	}

	/**
	 * @param populationId the populationId to set
	 */
	public void setPopulationId(int populationId) {
		this.populationId = populationId;
	}

	/**
	 * @return the pastProfitId
	 */
	public int getPastProfitId() {
		return pastProfitId;
	}

	/**
	 * @param pastProfitId the pastProfitId to set
	 */
	public void setPastProfitId(int pastProfitId) {
		this.pastProfitId = pastProfitId;
	}

	/* (non-Javadoc)
	 * @see jmab2.report.VariableComputer#computeVariable(jmab2.simulations.MacroSimulation)
	 */
	@Override
	public double computeVariable(MacroSimulation sim) {
		MacroPopulation macroPop = (MacroPopulation) sim.getPopulation();
		Population pop = macroPop.getPopulation(populationId);
		double profits=0;
		for (Agent i:pop.getAgents()){
			GoodSupplier agent= (GoodSupplier) i;
			if (!agent.isDead()){
			profits+=agent.getPassedValue(pastProfitId, 0);
			}
		}
		return profits;
	}

}
