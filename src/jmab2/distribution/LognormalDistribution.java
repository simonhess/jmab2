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
package jmab2.distribution;

import org.springframework.beans.factory.annotation.Required;

import cern.jet.random.Normal;
import net.sourceforge.jabm.distribution.AbstractDelegatedDistribution;

/**
 * @author Alessandro Caiani and Antoine Godin
 *
 */
@SuppressWarnings("serial")
public class LognormalDistribution extends AbstractDelegatedDistribution {

	protected double lognormalMean;
	
	protected double lognormalStDev;
	
	protected double mean;
	
	protected double stdev;

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
		reinitialise();
	}

	public double getStdev() {
		return stdev;
	}

	public void setStdev(double stdev) {
		this.stdev = stdev;
		reinitialise();
	}
	
	@Override
	public double nextDouble() {
		return Math.exp(delegate.nextDouble());
	}
	
	public void initialise() {
		double lognormalVariance = Math.pow(lognormalStDev, 2);
		this.mean=Math.log(Math.pow(lognormalMean, 2)/Math.sqrt(Math.pow(lognormalMean, 2)+lognormalVariance));
		double variance =Math.log(1+(lognormalVariance/Math.pow(lognormalMean, 2)));
		this.stdev=Math.sqrt(variance);
		delegate = new Normal(mean, stdev, prng);
	}
	
	// Returns the cumulative distribution function of the lognormal distribution with value x
	public double cdfLognormal(double x) {
		Normal norm = (Normal) delegate;
		return norm.cdf(Math.log(x));
	}
	
	public double getLognormalMean() {
		return lognormalMean;
	}

	public void setLognormalMean(double lognormalMean) {
		this.lognormalMean = lognormalMean;
	}

	public double getLognormalStDev() {
		return lognormalStDev;
	}

	public void setLognormalStDev(double lognormalStDev) {
		this.lognormalStDev = lognormalStDev;
	}
}
