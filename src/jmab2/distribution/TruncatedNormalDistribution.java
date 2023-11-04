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

import cern.jet.random.Normal;
import net.sourceforge.jabm.distribution.NormalDistribution;

/**
 * @author Alessandro Caiani and Antoine Godin
 *
 */
@SuppressWarnings("serial")
public class TruncatedNormalDistribution extends NormalDistribution {

	protected double lowerBound;
	
	protected double upperBound;
	
	@Override
	public double nextDouble() {
		double nextDouble = delegate.nextDouble();
		while(nextDouble<lowerBound||nextDouble>upperBound) {
			nextDouble = delegate.nextDouble();
		}
		return nextDouble;
	}
	
	public int nextInt() {
		int nextInt = delegate.nextInt();
		while(nextInt<lowerBound||nextInt>upperBound) {
			nextInt = delegate.nextInt();
		}
		return nextInt;
	}
	
	public void initialise() {
		delegate = new Normal(mean, stdev, prng);
	}
	
	public double getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}
	
}
