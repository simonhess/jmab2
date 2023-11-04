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
package jmab.mechanisms;

import java.math.BigDecimal;
import java.util.List;

import jmab.agents.LiabilitySupplier;
import jmab.simulations.MarketSimulation;
import jmab.stockmatrix.Item;

/**
 * @author Alessandro Caiani and Antoine Godin
 *
 */
public abstract class AbstractMechanism implements Mechanism {

	protected MarketSimulation market;
	
	/**
	 * 
	 */
	public AbstractMechanism() {
		super();
	}

	/**
	 * @param scheduler
	 * @param market
	 */
	public AbstractMechanism(MarketSimulation market) {
		super();
		this.market = market;
	}

	/**
	 * @param market the market to set
	 */
	public void setMarket(MarketSimulation market) {
		this.market = market;
	}

	/* (non-Javadoc)
	 * @see jmab.mechanisms.Mechanism#setMarketSimulation(jmab.simulations.MarketSimulation)
	 */
	@Override
	public void setMarketSimulation(MarketSimulation market) {
		this.market=market;

	}

	/* (non-Javadoc)
	 * @see jmab.mechanisms.Mechanism#getMarketSimulation()
	 */
	@Override
	public MarketSimulation getMarketSimulation() {
		return market;
	}
	
	/**
	 * Re-allocates liquidity among different items of a stock matrix. Different case might arise
	 * a. Withdraw or deposit cash on deposit account
	 * b. Transfer from a long term deposit to a short term deposit
	 * c. Transfer from a deposit account to an other deposit account
	 * @param amount the amount to be raised from the different payingStocks
	 * @param payingStocks the list of items to be used to raise the amount
	 * @param targetStock the stock target for the amount to be raised
	 */
	public void reallocateLiquidity(double amount, List<Item> payingStocks, Item targetStock){
		BigDecimal amountBD = BigDecimal.valueOf(amount);
		//The amount raised is equal to what is already on the target stock
		BigDecimal amountRaised=BigDecimal.valueOf(targetStock.getValue());
		for(int i=0;i<payingStocks.size()&&amountRaised.compareTo(amountBD) ==-1;i++){
			//For each item in the list
			Item payingStock = payingStocks.get(i);
			//If the payingStock is not the target stock (otherwise, there's nothing to do).
			if(payingStock!=targetStock){
				//compute different amounts
				BigDecimal thisAmount=BigDecimal.valueOf(payingStock.getValue());
				BigDecimal remAmount=thisAmount.add(amountRaised).subtract(amountBD).max(new BigDecimal("0"));
				BigDecimal transferAmount=thisAmount.subtract(remAmount);
				amountRaised = amountRaised.add(transferAmount);
				//who is the supplier of the paying stock?
				LiabilitySupplier payingSupplier = (LiabilitySupplier) payingStock.getLiabilityHolder();
				// Do the transfer
				payingSupplier.transfer(payingStock, targetStock, transferAmount.doubleValue());
			}
		}
	}
}


/* OLD CODE
 * //Else, it becomes more complicated, first update all amounts
					payingStock.setValue(payingStock.getValue()-transferAmount);
					targetStock.setValue(targetStock.getValue()+transferAmount);
					//1. Get the balancing stock from the liquidity supplier
					Item balanceStock = payingSupplier.getCounterpartItem(payingStock, targetStock);
					//2. If the balancing stock is equal to the paying stock
					if(balanceStock.getSMId()==payingStock.getSMId()){
						//3. If the balance stock and paying stock have the same liability holder
						if(balanceStock.getLiabilityHolder()==payingStock.getLiabilityHolder())
							//Just update the balancing stock
							balanceStock.setValue(balanceStock.getValue()+transferAmount);
						else{
							//Otherwise, need to update two balancing stocks
							balanceStock.setValue(balanceStock.getValue()+transferAmount);
							LiabilitySupplier targetSupplier = (LiabilitySupplier) targetStock.getLiabilityHolder();
							Item otherBalanceStock = targetSupplier.getCounterpartItem(payingStock, targetStock);
							otherBalanceStock.setValue(otherBalanceStock.getValue()-transferAmount);
						}
					//2. If the balancing stock is of another type
					}else{
						//There are for sure 2 other balancing stocks, but need to determine whether they are assets or liabilities
						if(payingSupplier==balanceStock.getAssetHolder()){
							//In the case of assets, the balancing stock is decreased and the other balancing stock is increased
							LiabilitySupplier otherBank=(LiabilitySupplier)targetStock.getLiabilityHolder();
							Item otherCounterPart = otherBank.getItemStockMatrix(true, balanceStock.getSMId(), balanceStock.getLiabilityHolder()); 
							balanceStock.setValue(balanceStock.getValue()-transferAmount);
							otherCounterPart.setValue(otherCounterPart.getValue()+transferAmount);
						}else{
							//In the case of liabilities, the balancing stock is increased and the other balancing stock is decrased
							LiabilitySupplier otherBank=(LiabilitySupplier)targetStock.getAssetHolder();
							Item otherCounterPart = otherBank.getItemStockMatrix(false, balanceStock.getSMId(), balanceStock.getAssetHolder()); 
							balanceStock.setValue(balanceStock.getValue()+transferAmount);
							otherCounterPart.setValue(otherCounterPart.getValue()-transferAmount);
						}
					}
				}
 */