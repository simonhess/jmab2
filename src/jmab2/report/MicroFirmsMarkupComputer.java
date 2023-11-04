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
import java.util.TreeMap;

import jmab2.agents.SimpleAbstractAgent;
import jmab2.population.MacroPopulation;
import jmab2.simulations.MacroSimulation;
import jmab2.strategies.MarkupPricingStrategy;
import net.sourceforge.jabm.Population;
import net.sourceforge.jabm.agent.Agent;

/**
 * @author Alessandro Caiani and Antoine Godin
 *
 */
public class MicroFirmsMarkupComputer extends AbstractMicroComputer implements MicroMultipleVariablesComputer {
	
	private int populationId;
	private int strategyId;

	/* (non-Javadoc)
	 * @see jmab2.report.VariableComputer#computeVariable(jmab2.simulations.MacroSimulation)
	 */
	@Override
	public Map<Long,Double> computeVariables(MacroSimulation sim) {
		MacroPopulation macroPop = (MacroPopulation) sim.getPopulation();
		Population pop = macroPop.getPopulation(populationId);
		TreeMap<Long,Double> result=new TreeMap<Long,Double>();
		for (Agent i:pop.getAgents()){
			SimpleAbstractAgent agent= (SimpleAbstractAgent) i;
			if (!agent.isDead()){
				MarkupPricingStrategy strategy = (MarkupPricingStrategy)agent.getStrategy(strategyId);
				result.put(agent.getAgentId(), strategy.getMarkUp());
			}
			else{
				result.put(agent.getAgentId(), Double.NaN);
			}
			
		}
		return result;
	}

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
	 * @return the strategyId
	 */
	public int getStrategyId() {
		return strategyId;
	}

	/**
	 * @param strategyId the strategyId to set
	 */
	public void setStrategyId(int strategyId) {
		this.strategyId = strategyId;
	}
	
}
