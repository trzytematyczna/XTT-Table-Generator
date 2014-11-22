/**
*
*     Copyright 2013-15 by Szymon Bobek, Grzegorz J. Nalepa, Mateusz Ślażyński
*
*
*     This file is part of HeaRTDroid.
*     HeaRTDroid is a rule engine that is based on HeaRT inference engine,
*     XTT2 representation and other concepts developed within the HeKatE project .
*
*     HeaRTDroid is free software: you can redistribute it and/or modify
*     it under the terms of the GNU General Public License as published by
*     the Free Software Foundation, either version 3 of the License, or
*     (at your option) any later version.
*
*     HeaRTDroid is distributed in the hope that it will be useful,
*     but WITHOUT ANY WARRANTY; without even the implied warranty of
*     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*     GNU General Public License for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with HeaRTDroid.  If not, see <http://www.gnu.org/licenses/>.
*
**/

package heart.uncertainty;

import heart.WorkingMemory;
import heart.alsvfd.Value;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Attribute;

public class ALSVEvaluator implements UncertainTrueEvaluator{
	
	public static final float MIN_CERTAINTY = 0;
	public static final float MAX_CERTAINTY = 1;

	@Override
	public UncertainTrue evaluateUncertainEq(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		boolean logicalValue = attributeValue.eq(v, at.getType());
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainNeq(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		boolean logicalValue = attributeValue.neq(v, at.getType());
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainIn(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		boolean logicalValue = attributeValue.in(v, at.getType());
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainNotin(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		boolean logicalValue = attributeValue.notin(v, at.getType());
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainSubset(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		boolean logicalValue = attributeValue.subset(v, at.getType());
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainSupset(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		boolean logicalValue = attributeValue.supset(v, at.getType());
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainSim(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		boolean logicalValue = attributeValue.sim(v, at.getType());
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainNotsim(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		boolean logicalValue = attributeValue.notsim(v, at.getType());
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainLt(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		boolean logicalValue = attributeValue.lt(v, at.getType());
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainLte(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		boolean logicalValue = attributeValue.lte(v, at.getType());
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainGt(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		boolean logicalValue = attributeValue.gt(v, at.getType());
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public UncertainTrue evaluateUncertainGte(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		boolean logicalValue = attributeValue.gte(v, at.getType());
		
		return (logicalValue ? new UncertainTrue(getMaxCertainty()): new UncertainTrue(getMinCertainty()));
	}

	@Override
	public float getMinCertainty() {
		return MIN_CERTAINTY;
	}

	@Override
	public float getMaxCertainty() {
		return MAX_CERTAINTY;
	}

}
