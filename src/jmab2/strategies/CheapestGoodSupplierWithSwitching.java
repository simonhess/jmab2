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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import jmab2.agents.GoodDemander;
import jmab2.agents.GoodSupplier;
import jmab2.agents.MacroAgent;
import jmab2.population.MacroPopulation;
import jmab2.simulations.MacroSimulation;
import net.sourceforge.jabm.EventScheduler;
import net.sourceforge.jabm.SimulationController;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.strategy.AbstractStrategy;

/**
 * @author Alessandro Caiani and Antoine Godin
 *
 */
@SuppressWarnings("serial")
public class CheapestGoodSupplierWithSwitching extends AbstractStrategy implements
		SelectSellerStrategy {

	private GoodSupplier previousGoodSupplier;
	private SwitchingStrategy strategy;
	public boolean switched;
	
	/**
	 * 
	 */
	public CheapestGoodSupplierWithSwitching() {
		super();
		this.previousGoodSupplier=null;
	}

	/**
	 * @param agent
	 */
	public CheapestGoodSupplierWithSwitching(Agent agent) {
		super(agent);
		this.previousGoodSupplier=null;
	}

	/**
	 * @param scheduler
	 * @param agent
	 */
	public CheapestGoodSupplierWithSwitching(EventScheduler scheduler, Agent agent) {
		super(scheduler, agent);
		this.previousGoodSupplier=null;
	}
	
	/**
	 * @return the strategy
	 */
	public SwitchingStrategy getStrategy() {
		return strategy;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(SwitchingStrategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * @return the previousGoodSupplier
	 */
	public GoodSupplier getPreviousGoodSupplier() {
		return previousGoodSupplier;
	}

	/**
	 * @param previousGoodSupplier the previousGoodSupplier to set
	 */
	public void setPreviousGoodSupplier(GoodSupplier previousGoodSupplier) {
		this.previousGoodSupplier = previousGoodSupplier;
	}

	/* (non-Javadoc)
	 * @see jmab2.strategies.BuyingStrategy#selectGoodSupplier(java.util.ArrayList, double, boolean)
	 */
	@Override
	public MacroAgent selectGoodSupplier(ArrayList<Agent> goodSuppliers, double demand, boolean real) {
		this.switched=false;
		double minPrice=Double.POSITIVE_INFINITY;
		GoodSupplier minGoodSupplier=(GoodSupplier) goodSuppliers.get(0);
		GoodDemander goodDemander = (GoodDemander) getAgent();
		for(Agent agent : goodSuppliers){
			GoodSupplier goodSupplier=(GoodSupplier)agent;
			double tempPrice=goodSupplier.getPrice(goodDemander, demand);
			if(tempPrice<minPrice){
				minPrice=tempPrice;
				minGoodSupplier=goodSupplier;
			}
		}
		double previousPrice=0;
		if (!previousGoodSupplier.isDead()&&previousGoodSupplier.isActive(((MacroSimulation)((SimulationController)this.scheduler).getSimulation()).getActiveMarketId())){
			previousPrice=previousGoodSupplier.getPrice(goodDemander, demand);
		}
		else{
			previousPrice=Double.POSITIVE_INFINITY;
		}
		if(previousPrice>minPrice){
			if(previousPrice==Double.POSITIVE_INFINITY||strategy.switches(previousPrice,minPrice)){
				previousGoodSupplier=minGoodSupplier;
				this.switched=true;
			}
		}
		return previousGoodSupplier;
	}

	/* (non-Javadoc)
	 * @see jmab2.strategies.BuyingStrategyWithSwitching#setPreviousSeller(jmab2.agents.GoodSupplier)
	 */
	public void setPreviousSeller(GoodSupplier counterpart) {
		this.previousGoodSupplier=counterpart;
		
	}

	/* (non-Javadoc)
	 * @see jmab2.strategies.SelectSellerStrategy#selectMultipleGoodSupplier(java.util.ArrayList, double, boolean)
	 */
	@Override
	public List<MacroAgent> selectMultipleGoodSupplier(
			ArrayList<Agent> sellers, double demand, boolean real) {
		this.switched=false;
		TreeMap<Double,ArrayList<MacroAgent>> orederedSellers = new TreeMap<Double,ArrayList<MacroAgent>>();
		GoodDemander buyer=(GoodDemander)this.getAgent();
		for (Agent agent:sellers){
			GoodSupplier seller=(GoodSupplier)agent;
			double price=seller.getPrice(buyer, demand);
			if(orederedSellers.containsKey(price)){
				ArrayList<MacroAgent> list = orederedSellers.get(price);
				list.add(seller);
			}else{
				ArrayList<MacroAgent> list = new ArrayList<MacroAgent>();
				list.add(seller);
				orederedSellers.put(price, list);
			}
		}
		ArrayList<MacroAgent> result = new ArrayList<MacroAgent>();
		for (Double key:orederedSellers.descendingKeySet()){
			for(MacroAgent agent:orederedSellers.get(key)){
				double previousPrice=previousGoodSupplier.getPrice(buyer, demand);
				GoodSupplier seller=(GoodSupplier)agent;
				double price=seller.getPrice(buyer, demand);
				if(previousPrice>price){
					if(previousPrice==Double.POSITIVE_INFINITY||strategy.switches(previousPrice,price)){
						result.add(agent);
						this.switched=true;
					}else{
						result.add(previousGoodSupplier);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Generate the byte array structure of the strategy. The structure is as follow:
	 * [popId][previousGoodSupplierId]
	 * @return the byte array content
	 */
	@Override
	public byte[] getBytes() {
		ByteBuffer buf = ByteBuffer.allocate(21);
		buf.putInt(this.previousGoodSupplier.getPopulationId());
		buf.putLong(this.previousGoodSupplier.getAgentId());
		return buf.array();
	}
	
	/**
	 * Populates the strategy from the byte array content. The structure should be as follows:
	 * [popId][previousGoodSupplierId]
	 * @param content the byte array containing the structure of the strategy
	 * @param pop the Macro Population of agents
	 */
	@Override
	public void populateFromBytes(byte[] content, MacroPopulation pop) {
		ByteBuffer buf = ByteBuffer.wrap(content);
		Collection<Agent> aHolders = pop.getPopulation(buf.getInt()).getAgents();
		long goodSupplierId = buf.getLong(); 
		for(Agent a:aHolders){
			MacroAgent pot = (MacroAgent) a;
			if(pot.getAgentId()==goodSupplierId){
				this.previousGoodSupplier=(GoodSupplier) pot;
				break;
			}
		}
		
	}

	public boolean isSwitched() {
		return switched;
	}

	public void setSwitched(boolean switched) {
		this.switched = switched;
	}

}
