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


public class UnaryExpression implements ExpressionInterface {

    public static enum UnaryOperator {
        ABS("abs"), COSINUS("cos"), SINUS("sin"), TANGENS("tan"),
        FACTORIAL("fac"), LOGARITHM("log"), POWERSET("powerset");

        private final String text;
      
        UnaryOperator(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static UnaryOperator fromString(String text) {
          if (text != null) {
            for (UnaryOperator b : UnaryOperator.values()) {
              if (text.equalsIgnoreCase(b.text)) {
                return b;
              }
            }
          }
          return null;
        }
    }
    
    private final ExpressionInterface arg;
    private final UnaryOperator op; 
            
    public UnaryExpression(ExpressionInterface arg, UnaryOperator op) {
        this.arg = arg;
        this.op = op;
    }

    private Value staticEvaluate(Value argument) throws UnsupportedOperationException, NotInTheDomainException {
        Value result = null;

        switch (op) {
            case ABS:
            case COSINUS:
            case SINUS:
            case TANGENS:
            case FACTORIAL:
            case LOGARITHM:
            case POWERSET:
                throw new UnsupportedOperationException("Operation "+ op.getText() +" is not supported (yet).");
        }
        return result;
    }

    @Override
    public Value evaluate(WorkingMemory wm) throws UnsupportedOperationException, NotInTheDomainException {
        Value argument = arg.evaluate(wm);
        return staticEvaluate(argument);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", this.op.getText(), this.arg);
    }
    
    public static class Builder implements ExpressionBuilderInterface {
        private ExpressionBuilderInterface argBuilder;
        private UnaryOperator op;
        private String debugInfo;

        @Override
        public Value staticEvaluate(Map<String, Attribute> atts) throws StaticEvaluationException, NotInTheDomainException {
            if (argBuilder == null || op == null) {
                throw new StaticEvaluationException(String.format("Error while evaluating UnaryExpression. The argument or operator has not been set.\n%s", debugInfo));
            }
            Value arg = argBuilder.staticEvaluate(atts);
            return new UnaryExpression(arg, op).staticEvaluate(arg);
        }

        @Override
        public ExpressionInterface build(Map<String, Attribute> atts) throws BuilderException {
            if (argBuilder == null || op == null){
		throw new BuilderException(String.format("Error while building UnaryExpression. The argument or operator has not been set.\n%s", debugInfo));
            }
            ExpressionInterface arg = argBuilder.build(atts);
            return new UnaryExpression(arg, op);
        }
        
        public Builder setArgumentBuilder(ExpressionBuilderInterface argBuilder) {
            this.argBuilder = argBuilder;
            return this;
        }

        public Builder setOperator(UnaryOperator op) {
            this.op = op;
            return this;
        }

        public ExpressionBuilderInterface setDebugInfo(String debugInfo) {
            this.debugInfo = debugInfo;
            return this;
        }
        
        @Override
        public String toString() {
            return String.format("%s(%s)", this.op.getText(), this.argBuilder);
        }
    }
}
