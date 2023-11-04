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
package jmab2.strategies;

import jmab2.agents.GoodDemander;
import jmab2.population.MacroPopulation;
import net.sourceforge.jabm.EventScheduler;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.strategy.AbstractStrategy;
import cern.jet.random.AbstractContinousDistribution;

/**
 * @author Alessandro Caiani and Antoine Godin
 *
 */
@SuppressWarnings("serial")
public class RandomPrice extends AbstractStrategy implements PricingStrategy {
	
	protected AbstractContinousDistribution priceDistribution;

	/**
	 * 
	 */
	public RandomPrice() {}


	/**
	 * @param agent
	 */
	public RandomPrice(Agent agent) {
		super(agent);
	}

	/**
	 * @param scheduler
	 * @param agent
	 */
	public RandomPrice(EventScheduler scheduler, Agent agent) {
		super(scheduler, agent);
	}

	/**
	 * @return the priceDistribution
	 */
	public AbstractContinousDistribution getPriceDistribution() {
		return priceDistribution;
	}

	/**
	 * @param priceDistribution the priceDistribution to set
	 */
	public void setPriceDistribution(AbstractContinousDistribution priceDistribution) {
		this.priceDistribution = priceDistribution;
	}

	/* (non-Javadoc)
	 * @see jmab.strategy.PricingStrategy#setPrice(jmab2.agents.Seller, jmab2.agents.Buyer, double, boolean)
	 */
	@Override
	public double computePrice() {
		return priceDistribution.nextDouble();
	}


	/* (non-Javadoc)
	 * @see jmab2.strategies.PricingStrategy#computePriceForSpecificBuyer(jmab2.agents.GoodDemander, double, boolean)
	 */
	@Override
	public double computePriceForSpecificBuyer(GoodDemander buyer,
			double demand, boolean real) {
		return priceDistribution.nextDouble();
	}


	/* (non-Javadoc)
	 * @see jmab2.strategies.SingleStrategy#getBytes()
	 */
	@Override
	public byte[] getBytes() {
		return new byte[1];//TODO
	}


	/* (non-Javadoc)
	 * @see jmab2.strategies.SingleStrategy#populateFromBytes(byte[], jmab2.population.MacroPopulation)
	 */
	@Override
	public void populateFromBytes(byte[] content, MacroPopulation pop) {}

}
