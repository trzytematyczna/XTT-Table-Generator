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
import heart.alsvfd.Null;
import heart.alsvfd.Value;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Attribute;

public class ProbabilityEvaluator implements UncertainTrueEvaluator{
	public static final float MAX_CERTAINTY = 1;
	public static final float MIN_CERTAINTY = 0;
	
	/**
	 * The method calculated the certainty of the formula being true, by incorporating standard
	 * probability theory.
	 * 
	 * @param at The attribute that is a LHS of the formula
	 * @param v The value that is RHS of the formula
	 * @param wm a working memory object that contains information about attributes values
	 * @param op The operation
	 * @param logicalValue The logical value that was evaluated without taking into consideration the certainty of the values
	 * 
	 * @return The uncertainTrue value that represents the certainty of formula being true. 
	 * @throws NotInTheDomainException 
	 * @throws UnsupportedOperationException 
	 * @throws AttributeNotRegisteredException 
	 */
	private UncertainTrue evaluateUncertainTrueValue(Attribute at, Value v, WorkingMemory wm,
			String op, boolean logicalValue)
			throws UnsupportedOperationException, NotInTheDomainException {
		
		/*
		 * This should treat all the set elements probabilities as a probability of presence 
		 * in the set. If the value is not present in the set it is equal to the situation
		 * when the value is present with probability 0.0.
		 * 
		 * To allow more robust reasoning it is worth introducing noise parameter 
		 * similar to noisy-or gateway, which will always add some probability
		 * to not present elements (as if they were very unlikely present in the set, 
		 * yet not completely impossible).
		 */
		
		if(wm.getAttributeValue(at.getName()) instanceof Null){
			// Here use the attribute sistribution field, 
			// TODO: where should be callbacks triggered?
			// TODO: where should be mediation triggered?
		}
		
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainEq(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainNeq(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainIn(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainNotin(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainSubset(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainSupset(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainSim(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainNotsim(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainLt(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainLte(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainGt(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UncertainTrue evaluateUncertainGte(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		// TODO Auto-generated method stub
		return null;
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
