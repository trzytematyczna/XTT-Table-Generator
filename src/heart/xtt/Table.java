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

import java.util.LinkedList;


public class Table {
	/**
	 * This field is used by the SQL module while storing/restoring model into/from database.
	 * If not provided, this should be null; 
	 */
	protected String id;
	
	/**
	 * A  field containing obligatory schema (table) name
	 */
	protected String name;
	
	/**
	 * A field containing an optional schema description.
	 */
	protected String description;
	
	
	//XTT2 Schema
	/**
	 * It is a list that denotes attributes that are required to be 
	 * known by the rules that falls in this schema
	 */
	protected LinkedList<heart.xtt.Attribute> precondition;
	
	/**
	 * It is a list denoted attributes that values are calculate by the rules in given schema.
	 */
	protected LinkedList<heart.xtt.Attribute> conclusion;
	
	/**
	 * This is a list of rules that belongs to the table.
	 */
	protected LinkedList<Rule> rules;
	
	public Table() {
		rules = new LinkedList<Rule>();
		precondition = new LinkedList<heart.xtt.Attribute>();
		conclusion = new LinkedList<heart.xtt.Attribute>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<heart.xtt.Attribute> getPrecondition() {
		return precondition;
	}

	public void setPrecondition(LinkedList<heart.xtt.Attribute> precondition) {
		this.precondition = precondition;
	}

	public LinkedList<heart.xtt.Attribute> getConclusion() {
		return conclusion;
	}

	public void setConclusion(LinkedList<heart.xtt.Attribute> conclusion) {
		this.conclusion = conclusion;
	}

	public LinkedList<Rule> getRules() {
		return rules;
	}

	public void setRules(LinkedList<Rule> rules) {
		this.rules = rules;
	}
        
    public void addRule(Rule rule) {
        if (rules == null) {
            rules = new LinkedList<>();
        }

        int i = 0;
        for (i = 0; i < rules.size(); i++) {
            Rule exr = rules.get(i);
            if (exr.orderNumber > rule.orderNumber) {
                break;
            }
        }
        rules.add(i, rule);
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
        
        public static class Builder {
            private String name;
            private String desc;
            private LinkedList<String> conditionalAttributesNames;
            private LinkedList<String> decisiveAttributesNames;
            private LinkedList<Attribute> conditionalAttributes;
            private LinkedList<Attribute> decisiveAttributes;
            private String debugInfo;
            
            public Table build() {
                Table t = new Table();
                t.setConclusion(this.decisiveAttributes);
                t.setPrecondition(this.conditionalAttributes);
                t.setName(this.name);
                t.setDescription(this.desc);
                return t;
            }
            
            public Builder setName(String name) {
                this.name = name;
                return this;
            }
            
            public String getName() {
                return this.name;
            }
            
            public Builder setDescription(String desc) {
                this.desc = desc;
                return this;
            }
            
            public Builder setConditionalAttributesNames(LinkedList<String> condAtts) {
                this.conditionalAttributesNames = condAtts;
                return this;
            }
            
            public LinkedList<String> getConditionalAttributesNames() {
                return this.conditionalAttributesNames;
            }            
            
            public Builder setDecisiveAttributesNames(LinkedList<String> decAtts) {
                this.decisiveAttributesNames = decAtts;
                return this;
            }
            
            public LinkedList<String> getDecisiveAttributesNames() {
                return this.decisiveAttributesNames;
            }  
            
            public Builder setConditionalAttributes(LinkedList<Attribute> condAtts) {
                this.conditionalAttributes = condAtts;
                return this;
            }
            
            public Builder setDecisiveAttributes(LinkedList<Attribute> decAtts) {
                this.decisiveAttributes = decAtts;
                return this;
            }
            
            public Builder setDebugInfo(String debugInfo) {
                this.debugInfo = debugInfo;
                return this;
            }
                
            public String getDebugInfo() {
                return this.debugInfo;
            }
        }
}
