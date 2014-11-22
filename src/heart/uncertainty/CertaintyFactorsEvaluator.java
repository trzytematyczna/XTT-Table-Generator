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
import heart.alsvfd.Any;
import heart.alsvfd.Formulae;
import heart.alsvfd.Null;
import heart.alsvfd.SetValue;
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import static heart.alsvfd.Formulae.ConditionalOperator;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Attribute;

import java.util.List;

public class CertaintyFactorsEvaluator implements UncertainTrueEvaluator{
	
	public static final float MAX_CERTAINTY = 1;
	public static final float MIN_CERTAINTY = -1;

	@Override
	public UncertainTrue evaluateUncertainEq(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		
		Value attributeValue = wm.getAttributeValue(at);
		
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(v instanceof Null)){
			return new UncertainTrue(0.0f);
		}
		
		boolean logicalValue = attributeValue.eq(v, at.getType());
		
		return evaluateUncertainTrueValue(at, v, wm, ConditionalOperator.EQ, logicalValue);

	}


	@Override
	public UncertainTrue evaluateUncertainNeq(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(v instanceof Null)){
			return new UncertainTrue(0.0f);
		}
		
		boolean logicalValue = attributeValue.neq(v, at.getType());
		return evaluateUncertainTrueValue(at, v,wm,  ConditionalOperator.NEQ, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainIn(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(v instanceof Null)){
			return new UncertainTrue(0.0f);
		}
		
		boolean logicalValue = attributeValue.in(v, at.getType());
		return evaluateUncertainTrueValue(at, v,wm, ConditionalOperator.IN, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainNotin(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(v instanceof Null)){
			return new UncertainTrue(0.0f);
		}
		
		boolean logicalValue = attributeValue.notin(v, at.getType());
		return evaluateUncertainTrueValue(at, v,wm, ConditionalOperator.NOTIN, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainSubset(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(v instanceof Null)){
			return new UncertainTrue(0.0f);
		}
		
		boolean logicalValue = attributeValue.subset(v, at.getType());
		return evaluateUncertainTrueValue(at, v,wm, ConditionalOperator.SUBSET, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainSupset(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException,	NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(v instanceof Null)){
			return new UncertainTrue(0.0f);
		}
		
		boolean logicalValue = attributeValue.supset(v, at.getType());
		return evaluateUncertainTrueValue(at, v,wm, ConditionalOperator.SUPSET, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainSim(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(v instanceof Null)){
			return new UncertainTrue(0.0f);
		}
		
		boolean logicalValue = attributeValue.sim(v, at.getType());
		return evaluateUncertainTrueValue(at, v,wm, ConditionalOperator.SIM, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainNotsim(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException,NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(v instanceof Null)){
			return new UncertainTrue(0.0f);
		}
		
		boolean logicalValue = attributeValue.notsim(v, at.getType());
		return evaluateUncertainTrueValue(at, v,wm, ConditionalOperator.NOTSIM, logicalValue);
	}
	
	@Override
	public UncertainTrue evaluateUncertainLt(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(v instanceof Null)){
			return new UncertainTrue(0.0f);
		}
		
		boolean logicalValue = attributeValue.lt(v, at.getType());
		return evaluateUncertainTrueValue(at, v,wm, ConditionalOperator.LT, logicalValue);
	}

	

	@Override
	public UncertainTrue evaluateUncertainLte(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(v instanceof Null)){
			return new UncertainTrue(0.0f);
		}
		
		boolean logicalValue = attributeValue.lte(v, at.getType());
		return evaluateUncertainTrueValue(at, v,wm, ConditionalOperator.LTE, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainGt(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(v instanceof Null)){
			return new UncertainTrue(0.0f);
		}
		
		boolean logicalValue = attributeValue.gt(v, at.getType());
		return evaluateUncertainTrueValue(at, v,wm, ConditionalOperator.GT, logicalValue);
	}


	@Override
	public UncertainTrue evaluateUncertainGte(Attribute at, Value v, WorkingMemory wm)
			throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		// It means that once we consider evaluation in terms of uncertainty, 
		// If the value is completely unknown everything is possible.
		if(attributeValue instanceof Null && !(v instanceof Null)){
			return new UncertainTrue(0.0f);
		}
		
		boolean logicalValue = attributeValue.gte(v, at.getType());
		return evaluateUncertainTrueValue(at, v,wm, ConditionalOperator.GTE, logicalValue);
	}

	/**
	 * The method calculates the certainty of the formula being true. 
	 * It uses the certainty factors algebra, but slightly modified 
	 * to allow evaluating expressions like [a(0.2),b(0.3),c(0.9)] neq [c,d].
	 * In standard certainty factors algebra this could not be evaluated and it sometimes lead to 
	 * Now, the cumulative rules equations are used to measure the similarities between sets.
	 * The certainty that the above formula is true is now calculated as 
	 * cf(sim([a(0.2),b(0.3),c(0.9)] AND [a(0.2),b(0.3),c(0.9)]\[c,d])))
	 * It can be translated as cumulative certainty that the LHS set 
	 * is similar the intersection of the LHS set and the set complement to the RHS set
	 * 
	 * @param at The attribute that is a LHS of the formula
	 * @param v The value that is RHS of the formula
	 * @param op The operation
	 * @param logicalValue The logical value that was evaluated without taking into consideration the certaintyFactors
	 * 
	 * @return The uncertainTrue value that represents the certainty of formula being true. 
	 * In case the formula is false it is represented by the UncertainTrue with negative certainty.
	 * @throws NotInTheDomainException 
	 * @throws UnsupportedOperationException 
	 */
	private UncertainTrue evaluateUncertainTrueValue(Attribute at, Value v, WorkingMemory wm,
			ConditionalOperator op, boolean logicalValue) throws UnsupportedOperationException, NotInTheDomainException {
		
		if(v instanceof Null || v instanceof Any){
			if(logicalValue == false)
				return new UncertainTrue(getMinCertainty());
			else
				return new UncertainTrue(getMaxCertainty());
		}else{
			float formulaCertainty = 0;
			Value attributeValue = wm.getAttributeValue(at);
			//For simple attributes just return the certainty value of an attribute
			if(attributeValue instanceof SimpleSymbolic || attributeValue instanceof SimpleNumeric){
				formulaCertainty = attributeValue.getCertaintyFactor();
				formulaCertainty = (logicalValue==true ? formulaCertainty : -formulaCertainty );
			}else{
				if (op.equals(ConditionalOperator.SIM)) {
					if( logicalValue == true){
						formulaCertainty = sim(at, v, wm);
					}else{
						formulaCertainty = -sim(at,getComplement(at, v, wm),wm);
					}

				}else if(op.equals(ConditionalOperator.NOTSIM)){
					if(logicalValue == true){
						sim(at,getComplement(at, v, wm),wm);
					}else{
						formulaCertainty = -sim(at, v,wm);
					}
					
				}else if(op.equals(ConditionalOperator.EQ)){
					if(logicalValue == true){
						formulaCertainty = getMinCF(((SetValue)attributeValue).getValues());
					}
					else{
						SetValue diff = getDifference(at, v, wm);
						formulaCertainty = -sim(at,diff, wm); 
					}
				
				}else if(op.equals(ConditionalOperator.NEQ)){
					if(logicalValue == true){
						SetValue diff = getDifference(at, v, wm);
						formulaCertainty = sim(at,diff, wm); 
					}
					else{
						formulaCertainty = -getMinCF(((SetValue)attributeValue).getValues());
					}
				}else if(op.equals(ConditionalOperator.SUPSET)){
					if(logicalValue == true){
						//return the weakest chain element that makes it superset
						SetValue intersection = (SetValue) attributeValue.intersect( v,at.getType());
						formulaCertainty = getMinCF(intersection.getValues());
					}else{
						SetValue comp = getComplement(at, v, wm);
						SetValue intersect = (SetValue) attributeValue.intersect( comp,at.getType());
						// return  how the {Domain\value} is similar to the attribute value
						formulaCertainty = -sim(at,intersect, wm); 
					}
				}else if(op.equals(ConditionalOperator.SUBSET)){
					if(logicalValue == true){
						formulaCertainty = getMinCF(((SetValue)attributeValue).getValues());
					}else{
						SetValue comp = getComplement(at, v, wm);
						SetValue intersect = (SetValue) attributeValue.intersect( comp,at.getType());
						// return  how the {Domain\value} is similar to the attribute value
						formulaCertainty = -sim(at,intersect, wm); 
					}
				
				}
				
			}
			
		    return new UncertainTrue(formulaCertainty);
			
		}
	}
	
	private static float sim(Attribute at, Value v, WorkingMemory wm) throws UnsupportedOperationException, NotInTheDomainException {
		Value attributeValue = wm.getAttributeValue(at);
		float formulaCertainty = 0;

			SetValue intersection = (SetValue) attributeValue.intersect(v, at.getType());
			if(!intersection.getValues().isEmpty()){
				List<Value> vals = intersection.getValues();
				float ci=intersection.getValues().get(0).getCertaintyFactor();
				for(int j=1; j < vals.size();j++){
					float cj = vals.get(j).getCertaintyFactor();
					if(ci >= 0 && cj >= 0){
						ci = ci+cj-ci*cj;
					}else if(ci < 0 && cj < 0){
						ci = ci+cj + ci*cj;
					}else if(ci*cj != 0 && ci*cj != -1){
						ci = (ci+cj)/(1-(ci<cj?ci:cj));
					}
				}
				formulaCertainty = ci;
			}
	
		return formulaCertainty;
	}
	
	private static float getMinCF(List<Value> values){
		float cf = MAX_CERTAINTY;
		for(Value v: values){
			if(v.getCertaintyFactor() < cf){
				cf = v.getCertaintyFactor();
			}
		}
		return cf;
	}
	
	private static SetValue getComplement(Attribute at, Value v, WorkingMemory wm) throws UnsupportedOperationException, NotInTheDomainException{
		SetValue complement = (SetValue)at.getType().getDomain().except(v, at.getType());
		Value attributeValue = wm.getAttributeValue(at);
		for(Value atValue: ((SetValue)attributeValue).getValues()){
			for(Value compValue: complement.getValues()){
				if(atValue.eq(compValue, at.getType())){
					compValue.setCertaintyFactor(atValue.getCertaintyFactor());
				}
			}
		}
		return complement;
	}
	
	private static SetValue getDifference(Attribute at, Value v, WorkingMemory wm) throws UnsupportedOperationException, NotInTheDomainException{
		Value attributeValue = wm.getAttributeValue(at);
		SetValue diff = (SetValue)attributeValue.except(v, at.getType());
		
		for(Value atValue: ((SetValue)attributeValue).getValues()){
			for(Value compValue: diff.getValues()){
				if(atValue.eq(compValue, at.getType())){
					compValue.setCertaintyFactor(atValue.getCertaintyFactor());
				}
			}
		}
		return diff;
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
