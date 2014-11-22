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
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Attribute;

public interface UncertainTrueEvaluator {
	public UncertainTrue evaluateUncertainEq(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException, NotInTheDomainException;
	public UncertainTrue evaluateUncertainNeq(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException, NotInTheDomainException;
	public UncertainTrue evaluateUncertainIn(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException, NotInTheDomainException;
	public UncertainTrue evaluateUncertainNotin(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException, NotInTheDomainException;
	public UncertainTrue evaluateUncertainSubset(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException, NotInTheDomainException;
	public UncertainTrue evaluateUncertainSupset(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException, NotInTheDomainException;
	public UncertainTrue evaluateUncertainSim(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException, NotInTheDomainException;
	public UncertainTrue evaluateUncertainNotsim(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException, NotInTheDomainException;
	public UncertainTrue evaluateUncertainLt(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException, NotInTheDomainException;
	public UncertainTrue evaluateUncertainLte(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException, NotInTheDomainException;
	public UncertainTrue evaluateUncertainGt(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException, NotInTheDomainException;
	public UncertainTrue evaluateUncertainGte(Attribute at, Value v, WorkingMemory wm) 
			throws UnsupportedOperationException, NotInTheDomainException;
	
	public float getMinCertainty();
	public float getMaxCertainty();

}
