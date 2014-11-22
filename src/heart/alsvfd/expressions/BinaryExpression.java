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


package heart.alsvfd.expressions;

import heart.WorkingMemory;
import heart.alsvfd.Value;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.StaticEvaluationException;
import heart.xtt.Attribute;
import java.util.Map;

public class BinaryExpression implements ExpressionInterface {

    public static enum BinaryOperator {
        UNION("union"), INTERSECTION("intersec"), EXCEPT("except"), COMPLEMENT("complement"),
        DIV("/"), SUM("+"), MUL("*"), SUB("-"), POW("**"), MOD("mod");
        
        private final String text;

        BinaryOperator(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static BinaryOperator fromString(String text) {
          if (text != null) {
            for (BinaryOperator b : BinaryOperator.values()) {
              if (text.equalsIgnoreCase(b.text)) {
                return b;
              }
            }
          }
          return null;
        }
    }
    
    private final ExpressionInterface lhs;
    private final ExpressionInterface rhs;
    private final BinaryOperator op;

    public BinaryExpression(ExpressionInterface lhs, ExpressionInterface rhs, BinaryOperator op) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    private Value staticEvaluate(Value left, Value right) throws UnsupportedOperationException, NotInTheDomainException {
        Value result = null;

        switch (op) {
            case SUM:
                result = left.add(right, null);
                break;
            case SUB:
                result = left.sub(right, null);
                break;
            case MUL:
                result = left.mul(right, null);
                break;
            case DIV:
                result = left.div(right, null);
                break;
            case MOD:
            case POW:
                throw new UnsupportedOperationException("Operation "+ op.getText() +" is not supported (yet).");
            case UNION:
                result = left.union(right, null);
                break;
            case EXCEPT:
                result = left.except(right, null);
                break;
            case INTERSECTION:
                result = left.intersect(right, null);
                break;
            case COMPLEMENT:
                throw new UnsupportedOperationException("Operation "+ op.getText() +" is not supported (yet).");
        }
        return result;
    }

    @Override
    public Value evaluate(WorkingMemory wm) throws UnsupportedOperationException, NotInTheDomainException {
        Value left = lhs.evaluate(wm);
        Value right =  rhs.evaluate(wm);
        return staticEvaluate(left, right);
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", this.lhs, this.op.getText(), this.rhs);
    }
    
    public static class Builder implements ExpressionBuilderInterface {
        private ExpressionBuilderInterface lhsBuilder;
        private ExpressionBuilderInterface rhsBuilder;
        private BinaryOperator op;
        private String debugInfo;

        @Override
        public Value staticEvaluate(Map<String, Attribute> atts) throws StaticEvaluationException, NotInTheDomainException {
            if (lhsBuilder == null || rhsBuilder == null || op == null){
                throw new StaticEvaluationException(String.format("Error while evaluating BinaryExpression. The left side, right side or operator has not been set.\n%s", debugInfo));
            }
            Value lhs = lhsBuilder.staticEvaluate(atts);
            Value rhs = rhsBuilder.staticEvaluate(atts);

            return new BinaryExpression(lhs, rhs, op).staticEvaluate(lhs, rhs);
        }

        @Override
        public ExpressionInterface build(Map<String, Attribute> atts) throws BuilderException {

            if (lhsBuilder == null || rhsBuilder == null || op == null){
		        throw new BuilderException(String.format("Error while building BinaryExpression. The left side, right side or operator has not been set.\n%s", debugInfo));
            } 
            
            ExpressionInterface lhs = lhsBuilder.build(atts);
            ExpressionInterface rhs = rhsBuilder.build(atts);
            return new BinaryExpression(lhs, rhs, op);
        }
        
        public Builder setLeftSideBuilder(ExpressionBuilderInterface lhsBuilder) {
            this.lhsBuilder = lhsBuilder;
            return this;
        }
        
        public Builder setRightSideBuilder(ExpressionBuilderInterface rhsBuilder) {
            this.rhsBuilder = rhsBuilder;
            return this;
        }
        
        public Builder setOperator(BinaryOperator op) {
            this.op = op;
            return this;
        }
        
        public Builder setDebugInfo(String debugInfo) {
            this.debugInfo = debugInfo;
            return this;
        }
        
        @Override
        public String toString() {
            return String.format("(%s %s %s)", this.lhsBuilder, this.op.getText(), this.rhsBuilder);
        }
    }
}
