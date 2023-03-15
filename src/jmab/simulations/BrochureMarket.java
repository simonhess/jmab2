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
package jmab.simulations;

import jmab.population.MacroPopulation;
import jmab.population.MarketPopulation;


/**
 * @author Alessandro Caiani and Antoine Godin
 *
 */
public class BrochureMarket extends AbstractTwoStepMarketSimulation
		implements TwoStepMarketSimulation {

	protected int nbRuns;
	/* (non-Javadoc)
	 * @see jmab.simulations.AbstractTwoStepMarketSimulation#secondSteo()
	 */
	@Override
	public void secondStep() {
		//while(!this.mixer.closed(population, simulation)){
	//		super.invokeSecondAgentInteractions();
		//}
			for(int i=0;i<this.nbRuns&&!this.mixer.closed(population, simulation);i++){
				super.invokeSecondAgentInteractions();
			}
	}

	/* (non-Javadoc)
	 * @see jmab.simulations.AbstractTwoStepMarketSimulation#firstStep()
	 */
	@Override
	public void firstStep() {
		super.invokeFirstAgentInteractions();
	}
	
	/**
	 * @return the nbRuns
	 */
	public int getNbRuns() {
		return nbRuns;
	}

	/**
	 * @param nbRuns the nbRuns to set
	 */
	public void setNbRuns(int nbRuns) {
		this.nbRuns = nbRuns;
	}

	/* (non-Javadoc)
	 * @see jmab.simulations.MarketSimulation#populateFromBytes(byte[], jmab.population.MacroPopulation)
	 */
	@Override
	public void populateFromBytes(byte[] content, MacroPopulation pop) {}

	/* (non-Javadoc)
	 * @see jmab.simulations.MarketSimulation#getBytes()
	 */
	@Override
	public byte[] getBytes() {
		return new byte[1];// TODO
	}

	/* (non-Javadoc)
	 * @see jmab.simulations.MarketSimulation#getPopulation()
	 */
	@Override
	public MarketPopulation getPopulation() {
		return population;
	}

}
