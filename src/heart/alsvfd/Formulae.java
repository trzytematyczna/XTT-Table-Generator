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


package heart.alsvfd;

import heart.WorkingMemory;
import heart.alsvfd.expressions.ExpressionBuilderInterface;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.alsvfd.expressions.StringExpressionBuilder;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.StaticEvaluationException;
import heart.uncertainty.CertaintyFactorsEvaluator;
import heart.uncertainty.UncertainTrue;
import heart.uncertainty.UncertainTrueEvaluator;
import heart.xtt.Attribute;
import java.util.Map;

public class Formulae {
    
        public static enum ConditionalOperator {
            EQ("eq"), NEQ("neq"), IN("in"),
            NOTIN("notin"), SUBSET("subset"),
            SUPSET("supset"), SIM("sim"),
            NOTSIM("notsim"), LT("lt"),
            GT("gt"), LTE("lte"), GTE("gte");
        
            private final String text;

            ConditionalOperator(String text) {
                this.text = text;
            }

            public String getText() {
                return this.text;
            }

            public static ConditionalOperator fromString(String text) {
              if (text != null) {
                for (ConditionalOperator b : ConditionalOperator.values()) {
                  if (text.equalsIgnoreCase(b.text)) {
                    return b;
                  }
                }
              }
              return null;
            }
        }
	
	protected Attribute at;
	protected ConditionalOperator op;
	protected Value v;						    //TODO:  this suppose to be Expression in future cases
												//when we allow to compare attributes against more 
												//complicated expressions
	
	public UncertainTrue evaluate(WorkingMemory wm, UncertainTrueEvaluator ute)   
			throws UnsupportedOperationException, NotInTheDomainException {	
            
            Value val = v.evaluate(wm);
            switch (op) {
                case EQ:
                    return ute.evaluateUncertainEq(at,val,wm);
                case NEQ:
                    return  ute.evaluateUncertainNeq(at,val,wm);
                case IN:
                    return  ute.evaluateUncertainIn(at,val,wm);
                case NOTIN:
                    return  ute.evaluateUncertainNotin(at,val,wm);
                case SUBSET:
                    return  ute.evaluateUncertainSubset(at,val,wm);
                case SUPSET:
                    return  ute.evaluateUncertainSupset(at,val,wm);
                case SIM:
                    return  ute.evaluateUncertainSim(at,val,wm);
                case NOTSIM:
                    return  ute.evaluateUncertainNotsim(at,val,wm);
                case LT:
                    return  ute.evaluateUncertainLt(at,val,wm);
                case LTE:
                    return  ute.evaluateUncertainLte(at,val,wm);
                case GT:
                    return  ute.evaluateUncertainGt(at,val,wm);
                case GTE:
                    return  ute.evaluateUncertainGte(at,val,wm);
                default:
                    throw new UnsupportedOperationException("Operator "+op+" not defined in ALSV(FD).");
            }
	}
	
	/**
	 * 
	 * @return the operator of the formula
	 */
	public ConditionalOperator getOp() {
		return op;
	}

	/**
	 * Sets the operator of the formula
	 * 
	 * @param op The operator to be set
	 */
	public void setOp(ConditionalOperator op) {
		this.op = op;
	}

	public Attribute getAttribute() {
		return at;
	}

	public void setAttribute(Attribute at) {
		this.at = at;
	}

	public Value getValue() {
		return v;
	}

	public void setValue(Value v) {
		this.v = v;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return at.getName()+" "+op+" "+v;
	}
        
        public static class Builder {
            protected Attribute at;
            protected String atName;
            protected ConditionalOperator op;
            protected ExpressionBuilderInterface v;
            protected String debugInfo;

            public Builder() {
            }
            
            public Formulae build(Map<String,Attribute> attributes) throws BuilderException, StaticEvaluationException, NotInTheDomainException {
                Formulae f = new Formulae();
                if (at == null || op == null || v == null) {
                    throw new BuilderException(String.format("Can't build Formula, there is no attribute, operator, or value.\n%s", this.debugInfo));
                }
                f.setAttribute(this.at);
                f.setOp(op);
                f.setValue(v.staticEvaluate(attributes));
                return f;
            }
            
            public Builder setAttributeName(String atName) {
                this.atName = atName;
                return this;
            }
            
            public String getAttributeName() {
                return this.atName;
            }
            
            public Builder setAttribute(Attribute at) {
                this.at = at;
                return this;
            }
            
            public Builder setOp(ConditionalOperator op) {
                this.op = op;
                return this;
            }
            
            public Builder setValue(ExpressionBuilderInterface v) {
                this.v = v;
                return this;
            }
            
            public Builder setDebugInfo(String info) {
                this.debugInfo = info;
                return this;
            }
            
        }
	
}