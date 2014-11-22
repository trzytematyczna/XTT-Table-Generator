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


import heart.alsvfd.Formulae;
import heart.exceptions.BuilderException;
import heart.exceptions.ModelBuildingException;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Rule.Builder.IncompleteRuleId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XTTModel {
	public static final int SOURCE_HML = 0;
	public static final int SOURCE_SQL = 1;
	public static final int SOURCE_HMR = 3;
	
	private String version;
	private int source;
	
	private LinkedList<Table> tables;
	private LinkedList<Type> types;
	private LinkedList<Attribute> attributes;
	
	
	public XTTModel(int source){
		this.source = source;
		tables = new LinkedList<Table>();
		types = new LinkedList<Type>();
		attributes = new LinkedList<Attribute>();
	}
	
	public String getVersion() {
		return version;
	}
	
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public LinkedList<Table> getTables() {
		return tables;
	}
	
	
	public void setTables(LinkedList<Table> tables) {
		this.tables = tables;
	}
	
	public LinkedList<Type> getTypes() {
		return types;
	}
	
	
	public void setTypes(LinkedList<Type> types) {
		this.types = types;
	}
	
	public LinkedList<Attribute> getAttributes() {
		return attributes;
	}
	
	
	public void setAttributes(LinkedList<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	public int getSource() {
		return source;
	}
	
	public void setSource(int source) {
		this.source = source;
	}
	
	public Attribute getAttributeById(String id){
		for(Attribute a: attributes){
			if(a.getId().equals(id)) return a;
		}
		
		return null;
	}
	
	public Attribute getAttributeByName(String name){
		for(Attribute a: attributes){
			if(a.getName().equals(name)) return a;
		}
		
		return null;
	}
	
	public static class Builder {
               
            private int source;
            private final Map<String, Type> types;
            private final Map<String, Attribute> attributes;
            private final Map<String, Table> tables;
            private final Map<String, Rule> rules;
            
            private final Map<String, Attribute.Builder> incAttributes;
            private final Map<String, Table.Builder> incTables;
            private final Map<String, Rule.Builder> incRules;

            
            public Builder() {
                this.source = SOURCE_HML;
                this.types = new HashMap<>();
                this.attributes = new HashMap<>();
                this.tables = new HashMap<>();
                this.rules = new HashMap<>();
                this.incAttributes = new HashMap<>();
                this.incTables = new HashMap<>();
                this.incRules = new HashMap<>();
            }
                      
            public XTTModel build() throws ModelBuildingException {
                try {
                    buildAttributes();
                    buildTables();
                    buildRules();
                } catch (ModelBuildingException | BuilderException | NotInTheDomainException ex) {
                    throw new ModelBuildingException(ex.getMessage());
                }
                XTTModel model = new XTTModel(this.source);
                model.setTypes(new LinkedList<>(this.types.values()));
//                TODO: implement more efficient way to remove duplicates
                model.setAttributes(new LinkedList<>(new HashSet<>(this.attributes.values())));
                model.setTables(new LinkedList<>(this.tables.values()));
                return model;
            }
            
            private void buildAttributes() throws ModelBuildingException, BuilderException, NotInTheDomainException {                
                for (Attribute.Builder incAttr : this.incAttributes.values()) {
                    String typeName = incAttr.getTypeName();

                    if (!this.types.containsKey(typeName)) {
                        throw new ModelBuildingException(String.format("Attribute %s uses undefined Type %s.\n%s", incAttr.getName(), typeName, incAttr.getDebugInfo()));
                    }

                    incAttr.setType(this.types.get(typeName));
                    Attribute attr = incAttr.build();
                    this.attributes.put(attr.getName(), attr);
                    this.attributes.put(attr.getAbbreviation(), attr);
                }
            }
            
            private void buildTables() throws ModelBuildingException {                
                for (Table.Builder incTable : this.incTables.values()) {
                    LinkedList<Attribute> condAtts = new LinkedList<>();
                    LinkedList<Attribute> decAtts = new LinkedList<>();

                    for (String attrName : incTable.getConditionalAttributesNames()) {
                        if (!this.attributes.containsKey(attrName)) {
                            throw new ModelBuildingException(String.format("Table %s uses in preconditions an undefined Attribute %s.\n%s", incTable.getName(), attrName, incTable.getDebugInfo()));
                        }    
                        condAtts.add(this.attributes.get(attrName));
                    }
                    
                    for (String attrName : incTable.getDecisiveAttributesNames()) {
                        if (!this.attributes.containsKey(attrName)) {
                            throw new ModelBuildingException(String.format("Table %s uses decisions with an undefined Attribute %s.\n%s", incTable.getName(), attrName, incTable.getDebugInfo()));
                        }    
                        decAtts.add(this.attributes.get(attrName));
                    }

                    incTable.setConditionalAttributes(condAtts); 
                    incTable.setDecisiveAttributes(decAtts);
                    this.tables.put(incTable.getName(), incTable.build());
                }
            }
            
            private void buildRules() throws ModelBuildingException {  
                
                Map<Rule, LinkedList<String>> ruleToSchemes = new HashMap<>();
                Map<Rule, LinkedList<String>> ruleToRules = new HashMap<>();
                
                for (Rule.Builder incRule : this.incRules.values()) {
                    IncompleteRuleId ruleId = incRule.getRuleId();
                    String schemeName = ruleId.schemeName;
                    String ruleName = ruleId.getName();
                    if (!this.tables.containsKey(schemeName)) {
                        throw new ModelBuildingException(String.format("Rule %s uses belongs to undefined scheme %s.\n%s", ruleName, schemeName, incRule.getDebugInfo()));
                    }
                    
                    if (this.rules.containsKey(ruleName)) {
                        throw new ModelBuildingException(String.format("Rule named %s is already defined.\n%s", ruleName, incRule.getDebugInfo()));
                    }
                
                    incRule.buildConditions(attributes);
                    incRule.buildDecisions(attributes);
                    
                    LinkedList<IncompleteRuleId> links = incRule.getLinks();
                    
                    LinkedList<String> schemeLinks = new LinkedList<>();
                    LinkedList<String> ruleLinks = new LinkedList<>();
                    
                    for (IncompleteRuleId rId : incRule.getLinks()) {
                        if (rId.orderNumber != null) {
                            ruleLinks.add(rId.getName());
                        }
                        else {
                            schemeLinks.add(rId.schemeName);
                        }
                    }

                    Table table = this.tables.get(schemeName);
                    
                    Rule rule = incRule.build();
                   
                    for (Decision d : rule.getDecisions()) {
                        if (!table.getConclusion().contains(d.attr)) {
                            throw new ModelBuildingException(String.format("Rule %s uses in decisive part an Attribute %s not present in its Scheme definition.\n%s", ruleName, d.attr.getName(), incRule.getDebugInfo()));
                        }
                    }
                    
                    for (Formulae f : rule.getConditions()) {
                        if (!table.getPrecondition().contains(f.getAttribute())) {
                            throw new ModelBuildingException(String.format("Rule %s uses in conditional part an Attribute %s not present in its Scheme definition.\n%s", ruleName, f.getAttribute().getName(), incRule.getDebugInfo()));
                        }
                    }
                    
                    table.addRule(rule);
                    ruleToSchemes.put(rule, schemeLinks);
                    ruleToRules.put(rule, ruleLinks);
                    rules.put(rule.getName(), rule);
                }
                
                for (Map.Entry<Rule,LinkedList<String>> entry : ruleToSchemes.entrySet()) {
                    Rule rule = entry.getKey();
                    for (String schemeName : entry.getValue()) {
                        rule.addTabLink(tables.get(schemeName));
                    }
                }
                
                for (Map.Entry<Rule,LinkedList<String>> entry : ruleToRules.entrySet()) {
                    Rule rule = entry.getKey();
                    for (String ruleName : entry.getValue()) {
                        rule.addRuleLink(rules.get(ruleName));
                    }
                }           
            }
            
            public Builder setSource(int src) {
                this.source = src;
                return this;
            }
            
            public Builder addType(Type type) throws ModelBuildingException {
                String key = type.getName();
                if (this.types.containsKey(key)) {
                    throw new ModelBuildingException(String.format("Type %s is already defined.\n", key));
                }
                this.types.put(key, type);
                return this;
            }
            
            public Builder addIncompleteAttribute(Attribute.Builder incAttr) throws ModelBuildingException {
                String keyName = incAttr.getName();
                String keyAbbrev = incAttr.getAbbreviation();
                if (this.incAttributes.containsKey(keyName) || this.incAttributes.containsKey(keyAbbrev)) {
                    throw new ModelBuildingException(String.format("Attribute %s (%s) is already defined.\n%s", keyName, keyAbbrev, incAttr.getDebugInfo()));
                }
                this.incAttributes.put(keyName, incAttr);
                this.incAttributes.put(keyAbbrev, incAttr);
                return this;
            }
            
            public Builder addIncompleteTable(Table.Builder incTable) throws ModelBuildingException {
                String key = incTable.getName();
                if (this.incTables.containsKey(key)) {
                    throw new ModelBuildingException(String.format("Table %s is already defined\n%s", key, incTable.getDebugInfo()));
                }
                this.incTables.put(key, incTable);
                return this;
            }
            
            public Builder addIncompleteRule(Rule.Builder incRule) throws ModelBuildingException {
                String key = incRule.getRuleId().getName();
                if (this.incRules.containsKey(key)) {
                    throw new ModelBuildingException(String.format("Rule %s is already defined\n%s", key, incRule.getDebugInfo()));
                }
                this.incRules.put(key, incRule);
                return this;
            }
        }
	
}
