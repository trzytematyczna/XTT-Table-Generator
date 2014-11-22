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


package heart.xtt;

import heart.WorkingMemory;
import heart.alsvfd.Value;
import heart.alsvfd.expressions.ExpressionBuilderInterface;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.uncertainty.CertaintyFactorsEvaluator;
import heart.uncertainty.UncertainTrue;

import java.util.Map;

public class Decision {
	protected Attribute attr;
	protected ExpressionInterface decision;
	protected ExpressionInterface tail;
	
	/**
	 * A variable that contains a name of a Action class
	 * that should be triggered with a reflection mechanisms
	 * while executing decision.
	 */
	protected String action;
	
	
	public Attribute getAttribute() {
		return attr;
	}
	
	public void setAttribute(Attribute attr) {
		this.attr = attr;
	}
	
	public ExpressionInterface getDecision() {
		return decision;
	}
	
	public void setDecision(ExpressionInterface decision) {
		this.decision = this.tail =  decision;
	}
	
	
	public boolean execute(WorkingMemory wm, UncertainTrue certainty)  throws UnsupportedOperationException, NotInTheDomainException  {
		try{
			Value newValue = decision.evaluate(wm);
			newValue.setCertaintyFactor(certainty.getCertinatyFactor());
			wm.setAttributeValue(attr, newValue, true);
	
			return true;
		}catch(UnsupportedOperationException e){
			//TODO rethrow with modified message
			throw e;
		}catch(NotInTheDomainException e){
			//TODO: change message
			throw e;
		}catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}catch (AttributeNotRegisteredException e) {
			// This will never happen, as the aturoregister option is on
			return false;
		}
	}
	
	@Override
	public String toString() {
		return attr.getName()+" set "+decision;
	}
	
	public static class Builder {
            private String attName;
            private Attribute att;
            private ExpressionBuilderInterface incDec;
            private ExpressionInterface dec;
            private String debugInfo;
            
            public Decision build() {
                Decision decision = new Decision();
                decision.setAttribute(att);
                decision.setDecision(dec);
                return decision;
            };
            
            public Decision build(Map<String, Attribute> attributes) throws BuilderException {
                if (incDec == null){
                    throw new BuilderException(String.format("Error while building Decision. The argument has not been set.\n%s", this.debugInfo));
                }      
                setDecision(this.incDec.build(attributes));
                return build();
            }
            
            public Builder setAttributeName(String attName) {
                this.attName = attName;
                return this;
            }
            
            public String getAttributeName() {
                return this.attName;
            }
            
            public Builder setAttribute(Attribute att) {
                this.att = att;
                return this;
            }
            
            public Builder setIncompleteDecision(ExpressionBuilderInterface incDec) {
                this.incDec = incDec;
                return this;
            }
            
            public Builder setDecision(ExpressionInterface dec) {
                this.dec = dec;
                return this;
            }
            
            public Builder setDebugInfo(String debugInfo) {
                this.debugInfo = debugInfo;
                return this;
            }
        }
}
