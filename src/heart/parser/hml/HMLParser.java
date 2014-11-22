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


package heart.parser.hml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import heart.State;
import heart.StateElement;
import heart.alsvfd.*;
import heart.alsvfd.Formulae.ConditionalOperator;
import heart.alsvfd.expressions.BinaryExpression;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.alsvfd.expressions.UnaryExpression;
import heart.alsvfd.expressions.BinaryExpression.BinaryOperator;
import heart.alsvfd.expressions.UnaryExpression.UnaryOperator;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.RangeFormatException;
import heart.xtt.*;


public class HMLParser {
	public static final String HML = "hml";
	public static final String VERSION  = "version";
	public static final String HML_TYPE = "type";
	public static final String HML_ATTRIBUTE = "attr";
	public static final String HML_STATE = "state";
	public static final String HML_TABLE = "table";
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String ATTREF = "attref";
	public static final String REF = "ref";
	public static final String SET= "set";
	
	public static final String TYPE_BASE = "base";
	public static final String TYPE_LENGTH = "length";
	public static final String TYPE_SCALE = "scale";
	public static final String TYPE_DOMAIN = "domain";
	public static final String TYPE_VALUE = "value";
	public static final String TYPE_ORDERED = "ordered";
	public static final String TYPE_DESC = "desc";
	
	public static final String VALUE_IS = "is";
	public static final String VALUE_FROM = "from";
	public static final String VALUE_TO = "to";
	public static final String VALUE_NUM = "num";
	public static final String VALUE_IN = "in";
	public static final String VALUE_NOTIN = "notin";
	public static final String VALUE_SIM = "sim";
	public static final String VALUE_NOTSIM = "sim";
	public static final String VALUE_SUPSET = "supset";
	public static final String VALUE_SUBSET = "subset";
	
	public static final String ATTR_TYPE = "type";
	public static final String ATTR_ABBREV = "abbrev";
	public static final String ATTR_CLASS = "class";
	public static final String ATTR_COMM = "comm";	
	public static final String ATTR_CLB = "clb";

	public static final String TABLE_SCHM = "schm";
	public static final String TABLE_RULE = "rule";
	public static final String TABLE_PRECOND = "precondition";
	public static final String TABLE_CONCLUSION = "conclusion";
	
	public static final String RULE_COND = "condition";
	public static final String RULE_REL = "relation";
	public static final String RULE_REL_NAME = "name";
	
	public static final String RULE_DEC = "decision";
	public static final String RULE_TRANS = "trans";
	
	public static final String RULE_LINK = "link";
	public static final String RULE_TABREF = "tabref";
	public static final String RULE_RULEREF = "ruleref";
	public static final String RULE_EXPR = "expr";
	public static final String RULE_EVAL= "eval";
	
	public static final HashMap<String,String> hml2hmr = new HashMap<String,String>(){
		{
			put("mul","*");
			put("sub","-");
			put("div","/");
			put("add","+");
			put("sin","sin");
			put("cos","cos");
			put("tan","tan");
			put("log","log");
			put("abs","abs");
			put("fac","fac");
			put("pow","**");
			put("root","sqrt"); 
			put("union","union");
			put("except","except");
			put("complement","complement");
			put("intersec","intersec");
		}
	};
	
	
	public static XTTModel parseHML(InputStream is) throws BuilderException, NotInTheDomainException, RangeFormatException{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		XTTModel model = new XTTModel(XTTModel.SOURCE_HML);
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document hml = dBuilder.parse(is);
			
			NodeList hmlNodes =  hml.getElementsByTagName(HML);
			
			for(int index = 0; index < hmlNodes.getLength(); index++){
				Node mainNode  = hmlNodes.item(index);
				
				//check if the node is element node. It should be HML Element.
				//If it is not, continue to the main node
				if (mainNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) mainNode;
					String version  = eElement.getAttribute(VERSION);
					model.setVersion(version);
					
					//read core elements of HML node
					LinkedList<Type> types = readTypes(eElement,model);
					model.setTypes(types);
					
					LinkedList<Attribute> attributes = readAttributes(eElement,model);
					model.setAttributes(attributes);
					
					LinkedList<Table>  tables = readTables(eElement,model);
					model.setTables(tables);
					
				}else{
					continue;
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return model;
	}

	private static LinkedList<Table> readTables(Element hmlElement, XTTModel model) throws RangeFormatException {
		NodeList tablesNodes =  hmlElement.getElementsByTagName(HML_TABLE);
		LinkedList<Table> tables = new LinkedList<Table>();
		LinkedList<BlindRule> blindRules = new LinkedList<BlindRule>();
		
		for(int index = 0; index < tablesNodes.getLength(); index++){
			Node mainNode  = tablesNodes.item(index);
		    Table table = new Table();
			//check if the node is element node. It should be table Element.
			//If it is not, continue to the main node
			if (mainNode.getNodeType() == Node.ELEMENT_NODE) {
				//Read basic information about the table, like id and name
				Element tableElement = (Element) mainNode;
				if(tableElement.hasAttribute(ID)){
					table.setId(tableElement.getAttribute(ID));
				}
				if(tableElement.hasAttribute(NAME)){
					table.setName(tableElement.getAttribute(NAME));
				}
				
				//Now, go deeper and check schema of the table
				NodeList tableChilds = tableElement.getChildNodes();
				for(int n = 0; n < tableChilds.getLength(); n++){		
					Node tableChildNode  = tableChilds.item(n);
		
					if (tableChildNode.getNodeType() == Node.ELEMENT_NODE) {
						Element  tableChildElement = (Element)tableChildNode;  
						if(tableChildElement.getNodeName().equals(TABLE_SCHM)){
							//Preconditions 
							NodeList preconditionNodeList = tableChildElement.getElementsByTagName(TABLE_PRECOND);
							for(int p = 0; p < preconditionNodeList.getLength(); p++){
								Node precondNode  = preconditionNodeList.item(p);
								if (precondNode.getNodeType() == Node.ELEMENT_NODE) {
									Element precondElement = (Element) precondNode;
									LinkedList<Attribute> precondition = readAttrefs(precondElement, model);
									table.setPrecondition(precondition);
									break;
								}
							}
							
							//Conclusions
							NodeList conclusionNodeList = tableChildElement.getElementsByTagName(TABLE_CONCLUSION);
							for(int c = 0; c < preconditionNodeList.getLength(); c++){
								Node conclNode  = conclusionNodeList.item(c);
								if (conclNode.getNodeType() == Node.ELEMENT_NODE) {
									Element conclElement = (Element) conclNode;
									LinkedList<Attribute> conclusions = readAttrefs(conclElement, model);
									table.setConclusion(conclusions);
								}
							}
						}else if(tableChildElement.getNodeName().equals(TABLE_RULE)){
							//Now, go further and read rules
							//Do not go deeper, as they are defined on the same level as schm
							
							//Create BlindRule and assign rule ID
							BlindRule br = new BlindRule();
							
							if(tableChildElement.hasAttribute(ID)){
								br.id = tableChildElement.getAttribute(ID);
							}
							//Read conditions
							LinkedList<Formulae> conditions = readConditions(tableChildElement,model);
							br.conditions = conditions;
							//ReadDecisions
							LinkedList<Decision> decisions = readDecisions(tableChildElement,model);
							br.decisions = decisions;
							//Read Links
							LinkedList<String> tabLinks = new LinkedList<String>();
							LinkedList<String> ruleLinks = new LinkedList<String>();
							
							NodeList tabLinksNodeList = tableChildElement.getElementsByTagName(RULE_TABREF);
							for(int tl = 0; tl < tabLinksNodeList.getLength(); tl++){		
								Node tabLinkNode  = tabLinksNodeList.item(tl);
								if (tabLinkNode.getNodeType() == Node.ELEMENT_NODE) {
									Element tablinkElement = (Element) tabLinkNode;
									if(tablinkElement.hasAttribute(REF)){
										tabLinks.add(tablinkElement.getAttribute(REF));
									}
								}
								
							}
							
							br.tabLinks = tabLinks;
							
							NodeList ruleLinksNodeList = tableChildElement.getElementsByTagName(RULE_RULEREF);
							for(int rl = 0; rl < ruleLinksNodeList.getLength(); rl++){		
								Node ruleLinkNode  = ruleLinksNodeList.item(rl);
								if (ruleLinkNode.getNodeType() == Node.ELEMENT_NODE) {
									Element rulelinkElement = (Element) ruleLinkNode;
									if(rulelinkElement.hasAttribute(REF)){
										ruleLinks.add(rulelinkElement.getAttribute(REF));
									}
								}
							}
							
							br.ruleLinks = ruleLinks;
							br.ownerTableId = table.getId();
							blindRules.add(br);
						}
					}
				}
				if(table != null) tables.add(table);
			}
		}
		return connectModel(blindRules, tables);
	}
	
	private static LinkedList<Decision> readDecisions(Element ruleElement, XTTModel model) throws RangeFormatException {
		LinkedList<Decision> decisions = new LinkedList<Decision>();
		NodeList transitionNodes =  ruleElement.getElementsByTagName(RULE_TRANS);
		for(int index = 0; index < transitionNodes.getLength(); index++){
			Node mainNode  = transitionNodes.item(index);
			//check if the node is element node. It should be transition Element.
			//If it is not, continue to the main node
			if (mainNode.getNodeType() == Node.ELEMENT_NODE) {
				Element transitionElement = (Element) mainNode;
				Attribute left = null;
				Decision d = new Decision();
				NodeList transitionChildNodes = transitionElement.getChildNodes();
				for(int n = 0; n < transitionChildNodes.getLength(); n++){
					Node transitionChildNode  = transitionChildNodes.item(n);
					if (transitionChildNode.getNodeType() == Node.ELEMENT_NODE) {
						Element transitionChildElement = (Element) transitionChildNode;
						//Now, get the left hand side of transition which has to be attref. 
						//However, this also can be a right hand side as we may allow att = att
						if(transitionChildElement.getNodeName().equals(ATTREF)){
							if(transitionChildElement.hasAttribute(REF)){
								String attrId = transitionChildElement.getAttribute(REF);
								//This is the case, when we read first attref which is left hand side.
								if(left == null){
									left = model.getAttributeById(attrId);
									d.setAttribute(left);
								}else{
									//This is the case whe we assign other 	
									Attribute right = model.getAttributeById(attrId);
									d.setDecision(right);
								}
							}
						}else if(transitionChildElement.getNodeName().equals(SET)){
							//This is static expression, so read values
							ArrayList<Value> values = parseValues(transitionChildElement, left.getType().getBase());
							if(values.size() > 1 || left.getXTTClass().equals(Attribute.CLASS_GENERAL) ){ 
								d.setDecision(new SetValue(values));
				        	}else {
				        		d.setDecision(values.get(0));
				        	}
							
						}else{
							ExpressionInterface e = readComplexExpression(transitionChildElement, d.getAttribute(), model);
							d.setDecision(e);
						}
					}
				}	
				decisions.add(d);
			}
		}
		return decisions;
	}

	private static ExpressionInterface readComplexExpression(Element transRootElement, Attribute lhs, XTTModel model) throws RangeFormatException{
		LinkedList<ExpressionInterface> operands = new LinkedList<>();
		String operation = null;
		if(transRootElement.hasAttribute(NAME)){
			operation = transRootElement.getAttribute(NAME);
		}
		NodeList transitionChildNodes = transRootElement.getChildNodes();
		for(int n = 0; n < transitionChildNodes.getLength(); n++){
			Node transitionChildNode  = transitionChildNodes.item(n);
			if (transitionChildNode.getNodeType() == Node.ELEMENT_NODE) {
				Element transitionChildElement = (Element) transitionChildNode;
				if(transitionChildElement.getNodeName().equals(ATTREF)){
					if(transitionChildElement.hasAttribute(REF)){
						String attrId = transitionChildElement.getAttribute(REF);
						Attribute right = model.getAttributeById(attrId);
						operands.add(right);
					}
				}else if(transitionChildElement.getNodeName().equals(SET)){
					//This is static expression, so read values
					ArrayList<Value> values = parseValues(transitionChildElement, lhs.getType().getBase());
					if(values.size() > 1 || lhs.getXTTClass().equals(Attribute.CLASS_GENERAL) ){ 
						operands.add(new SetValue(values));
		        	}else {
		        		operands.add(values.get(0));
		        	}
				}else if(transitionChildElement.getNodeName().equals(TYPE_VALUE)){
					ArrayList<Value> values = parseValues(transRootElement, lhs.getType().getBase());
					if(values.size() > 1 || lhs.getXTTClass().equals(Attribute.CLASS_GENERAL) ){ 
						operands.add(new SetValue(values));
		        	}else {
		        		operands.add(values.get(0));
		        	}
				}else if(transitionChildElement.getNodeName().equals(RULE_EXPR) ||
						transitionChildElement.getNodeName().equals(RULE_EVAL)){
					ExpressionInterface innerExpr = readComplexExpression(transitionChildElement, lhs, model);
					operands.add(innerExpr);
				}
			}
		}
		
		ExpressionInterface complexExpression = null;
		if(operands.size() == 2){
			return new BinaryExpression(operands.poll(),
					operands.poll(),
					BinaryOperator.fromString(hml2hmr.get(operation))); 
		}else if(operands.size() == 1){
			if(operation != null){
				return new UnaryExpression(operands.poll(),
						UnaryOperator.fromString(hml2hmr.get(operation)));
			}else{
				return operands.getFirst();
			}
		}else{
			//TODO something went wrong - throw  some more shit into fan
		}
			
		return complexExpression;
		
	}

	private static LinkedList<Formulae> readConditions(Element ruleElement, XTTModel model) throws RangeFormatException {
		LinkedList<Formulae> conditions = new LinkedList<Formulae>();
	
		NodeList relationNodes =  ruleElement.getElementsByTagName(RULE_REL);
		for(int index = 0; index < relationNodes.getLength(); index++){
			Node mainNode  = relationNodes.item(index);
			//check if the node is element node. It should be relation Element.
			//If it is not, continue to the main node
			if (mainNode.getNodeType() == Node.ELEMENT_NODE) {
				//check for relation name
			    Formulae f = new Formulae();
				Element relationElement = (Element) mainNode;
				if(relationElement.hasAttribute(RULE_REL_NAME)){
					String relationalOperator = relationElement.getAttribute(RULE_REL_NAME);
					
					f.setOp(ConditionalOperator.fromString(relationalOperator));
				
					NodeList relationChilds = mainNode.getChildNodes();
					for(int n = 0; n < relationChilds.getLength(); n++){
						Node relationChildNode  = relationChilds.item(n);
						if (relationChildNode.getNodeType() == Node.ELEMENT_NODE) {
							//check which noe are we investigating
							Element relationChildElement = (Element) relationChildNode;
							if(relationChildElement.getNodeName().equals(ATTREF)){
								//read the name of the attribute we are comparing to
								if(relationChildElement.hasAttribute(REF)){ 
									String attributeId = relationChildElement.getAttribute(REF);
									Attribute att = model.getAttributeById(attributeId);
									f.setAttribute(att);
								}
							}else if(relationChildElement.getNodeName().equals(SET)){
								//read values that we are comparing to
								//TODO: This in fact can be either value or attref, when we allow to compare 
								//		attributes against other attributes.
								ArrayList<Value> values = parseValues(relationChildElement, f.getAttribute().getType().getBase());
					            //Depending on the number of read attributes either create general or simple value
								if(values.size() > 1 || 
					        			f.getOp().toString().equalsIgnoreCase(VALUE_IN) ||
					        			f.getOp().toString().equalsIgnoreCase(VALUE_NOTIN) ||
					        			f.getOp().toString().equalsIgnoreCase(VALUE_SIM) || 
					        			f.getOp().toString().equalsIgnoreCase(VALUE_NOTSIM) || 
					        			f.getOp().toString().equalsIgnoreCase(VALUE_SUPSET) || 
					        			f.getOp().toString().equalsIgnoreCase(VALUE_SUBSET) || 
					        			f.getAttribute().getXTTClass().equalsIgnoreCase(Attribute.CLASS_GENERAL)){
					        		f.setValue(new SetValue(values));  
					        	}else{
					        	    f.setValue(values.get(0)); 
					        	}
							}
						}
					}
					
					conditions.add(f);
				}
				
			}
		}
		return conditions;
	}
	/**
	 * This method reads all attribute references that are on the level below the element given as an {@code eElement} parameter.
	 * It does not read any of the attributes references that are nested within the {@code eElement} parameter.
	 * @param eElement {@link Element} that should point exactly the level above the attribute references tags
	 * @param model {@link XTT2Model} that is currently build
	 * @return {@link LinkedList} of all {@link Attributes} that were referenced by the attref tags, and are present in {@code model} parameter.
	 */
	private static LinkedList<Attribute> readAttrefs(Element eElement, XTTModel model) {
		NodeList attrefNodes =  eElement.getElementsByTagName(ATTREF);
		LinkedList<Attribute> attributes = new LinkedList<Attribute>();
		for(int index = 0; index < attrefNodes.getLength(); index++){
			Node mainNode  = attrefNodes.item(index);
		    
			//check if the node is element node. It should be attref Element.
			//If it is not, continue to the main node
			if (mainNode.getNodeType() == Node.ELEMENT_NODE) {
				Element  attrefElement = (Element)mainNode;
				if(attrefElement.hasAttribute(REF) && attrefElement.getParentNode() == eElement){
					String attId = attrefElement.getAttribute(REF);
					Attribute a = model.getAttributeById(attId);
					attributes.add(a);
				}
			}
		}
		return attributes;
	}
	
	
	private static LinkedList<Table> connectModel(LinkedList<BlindRule> brules, LinkedList<Table> tables){
		ArrayList<Rule> rules = new ArrayList<Rule>(brules.size());
		
		//Fill the rules list with real rules, but without links
		for(BlindRule currentRule : brules){
			Rule rule = new Rule();
			rule.setId(currentRule.id);
			rule.setConditions(currentRule.conditions);
			rule.setDecisions(currentRule.decisions);
			
			rules.add(rule);
		}
		
		int currentRuleIndex = 0;
		for(BlindRule currentRule : brules){
			for(String toLinkRuleId : currentRule.ruleLinks){
				for(Rule other : rules){
					if(toLinkRuleId.equals(other.getId())){
						rules.get(currentRuleIndex).getRuleLinks().add(other);
						break;
					}
				}
			}
			
			for(String toLinkTableId : currentRule.tabLinks){
				for(Table table : tables){
					if(toLinkTableId.equals(table.getId())){
						rules.get(currentRuleIndex).getTabLinks().add(table);
						break;
					}
				}
			}
			
			for(Table table: tables){
				if(table.getId().equals(currentRule.ownerTableId)){
					table.getRules().add(rules.get(currentRuleIndex));
				}
			}
			
			currentRuleIndex++;
		}

		return tables;
	}
	

	private static LinkedList<State> readStates(Element hmlElement, XTTModel model) throws RangeFormatException {
		NodeList stateNodes =  hmlElement.getElementsByTagName(HML_STATE);
		LinkedList<State> states = new LinkedList<State>();
		for(int index = 0; index < stateNodes.getLength(); index++){
			Node mainNode  = stateNodes.item(index);
			
			State state = new State();
			Attribute attr = null;
			//check if the node is element node. It should be state Element.
			//If it is not, continue to the main node
			if (mainNode.getNodeType() == Node.ELEMENT_NODE) {
				Element stateElement = (Element) mainNode;
				if(stateElement.hasAttribute(ID)){
					state.setId(stateElement.getAttribute(ID));
					
				}
				if(stateElement.hasAttribute(NAME)){
					state.setName(stateElement.getAttribute(NAME));
				}
				
				//Get a list of state elements frm this element
				NodeList stateElements = stateElement.getChildNodes();
				StateElement stateElementObject = null;
				for(int se = 0; se < stateElements.getLength(); se++){
					Node finalStateNode  = stateElements.item(se);
					if (finalStateNode.getNodeType() == Node.ELEMENT_NODE) {
						Element finalStateElement = (Element) finalStateNode;
						if(finalStateElement.getNodeName().equals(ATTREF)){
							stateElementObject = new StateElement();
							String attrId = finalStateElement.getAttribute(REF);
							attr = model.getAttributeById(attrId);
							stateElementObject.setAttributeName(attr.getName());
						}else if(finalStateElement.getNodeName().equals(SET)){
							Type corresponginType = attr.getType();
							ArrayList<Value> values = parseValues(finalStateElement, corresponginType.getBase());
							if(values.size() > 1){
								stateElementObject.setValue(new SetValue(values));
							}else{
								stateElementObject.setValue(values.get(0));
							}
							state.addStateElement(stateElementObject);
						}
					}
				}
			}
			if(state != null) states.add(state);
		}
		
		return states;
		
	}

	private static LinkedList<Attribute> readAttributes(Element hmlElement, XTTModel model) throws BuilderException, NotInTheDomainException {
		NodeList attrNodes =  hmlElement.getElementsByTagName(HML_ATTRIBUTE);
		LinkedList<Attribute> attributes = new LinkedList<Attribute>();
		for(int index = 0; index < attrNodes.getLength(); index++){
			Node mainNode  = attrNodes.item(index);
			
			//check if the node is element node. It should be attribute Element.
			//If it is not, continue to the main node
			if (mainNode.getNodeType() == Node.ELEMENT_NODE) {
				Element attrElement = (Element) mainNode;
				
				String id = null;
				String name = null;
				String comm = null;
				String XTTClass = null;
				String description = null;
				String abbreviation = null;
				String callback = null;
				Type type = null;
				
				if(attrElement.hasAttribute(ID)){
					id = attrElement.getAttribute(ID);
				}
				if(attrElement.hasAttribute(ATTR_TYPE)){
					//Add type by searching in types that has already been added
					String typeId = attrElement.getAttribute(ATTR_TYPE);
					LinkedList<Type> types = model.getTypes();
					for(Type t : types){
						if(t.getId().equals(typeId)){
							type = t;
							break;
						}
					}
				}
				if(attrElement.hasAttribute(NAME)){
					name = attrElement.getAttribute(NAME);
				}
				if(attrElement.hasAttribute(ATTR_ABBREV)){
					 abbreviation = attrElement.getAttribute(ATTR_ABBREV);
				}
				if(attrElement.hasAttribute(ATTR_CLASS)){
					XTTClass = attrElement.getAttribute(ATTR_CLASS);
				}
				if(attrElement.hasAttribute(ATTR_COMM)){
					comm = attrElement.getAttribute(ATTR_COMM);
				}
				if(attrElement.hasAttribute(ATTR_CLB)){
					callback = attrElement.getAttribute(ATTR_CLB);
				}
				
				Attribute attr = new Attribute.Builder().setId(id)
						.setName(name)
						.setType(type)
						.setCallback(callback)
						.setComm(comm)
						.setAbbreviation(abbreviation)
						.setDescription(description) 
						.setXTTClass(XTTClass).build();
				
	
				attributes.add(attr);

			}
		}
		return attributes;
	}

	private static LinkedList<Type> readTypes(Element hmlElement, XTTModel model) throws BuilderException, RangeFormatException {
		NodeList typeNodes =  hmlElement.getElementsByTagName(HML_TYPE);
		LinkedList<Type> types = new LinkedList<Type>();
		
		for(int index = 0; index < typeNodes.getLength(); index++){
			Node mainNode  = typeNodes.item(index);
			
			//check if the node is element node. It should be type Element.
			//If it is not, continue to the main node
			if (mainNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) mainNode;
				String id  = eElement.getAttribute(ID);
				String name = eElement.getAttribute(NAME);
				String base = eElement.getAttribute(TYPE_BASE);
				String desc = null;
				String ordered = null;
				int length = 0;
				int precision = 0;
				
				ArrayList<Value> values = null;
				
				if(eElement.hasAttribute(TYPE_LENGTH)){
					length = Integer.parseInt(eElement.getAttribute(TYPE_LENGTH));
				}
				if(eElement.hasAttribute(TYPE_SCALE)){
					precision = Integer.parseInt(eElement.getAttribute(TYPE_SCALE));
				}
						
				//read description of a type
				NodeList description  =  eElement.getElementsByTagName(TYPE_DESC);

				for(int n = 0; n < description.getLength(); n++){
					Node descNode  = description.item(n);
					if (descNode.getNodeType() == Node.ELEMENT_NODE) {
						desc = descNode.getTextContent().trim();
					}
				}
				
				//read domain of a type
				NodeList domainList  =  eElement.getElementsByTagName(TYPE_DOMAIN);
				
				for(int n = 0; n < domainList.getLength(); n++){
					Node domainNode  = domainList.item(n);
					if (domainNode.getNodeType() == Node.ELEMENT_NODE) {
						Element domainElement = (Element) domainNode;
						
						//Check if the type is ordered 
						if(domainElement.hasAttribute(TYPE_ORDERED)){
							ordered = domainElement.getAttribute(TYPE_ORDERED);
						}
						
						//Read domain values
						values = parseValues(domainElement, base);
					}
				}
				
				//All information are collecetd, so create a type and add it to the list
				Type newType = new Type.Builder().setId(id)
						.setName(name)
						.setBase(base)
						.setDescription(desc)
						.setLength(length)
						.setOrdered(ordered)
						.setPrecision(precision)
						.setDomain(new SetValue(values)).build();
				
				types.add(newType);
			}else{
				continue;
			}
		}
		return types;
	}
	
	private static ArrayList<Value> parseValues(Element parentElement, String typeBase) throws RangeFormatException{
		ArrayList<Value> values = new ArrayList<Value>();
		NodeList valueNodes =  parentElement.getChildNodes();
		//Iterate through all values and add them to the list
		for(int n = 0; n < valueNodes.getLength(); n++){
			Node valueNode  = valueNodes.item(n);
			if (valueNode.getNodeType() == Node.ELEMENT_NODE && 
					valueNode.getNodeName().equals(TYPE_VALUE)) {
				Element valueElement = (Element) valueNode;
				String is = null;
				String num = null;
				String from = null;
				String to = null;
				Value value = null;
				
				
				if(valueElement.hasAttribute(VALUE_IS)){
					is = valueElement.getAttribute(VALUE_IS);
				    if(is.toLowerCase().equals(Value.ANY)){
					    value = new Any();
					    values.add(value); 
					    continue;
				    }else if(is.toLowerCase().equals(Value.NULL)){
					    value = new Null();
					    values.add(value); 
					    continue;
				    }else if(valueElement.hasAttribute(VALUE_NUM)){
						num = valueElement.getAttribute(VALUE_NUM);
					}
				}else if(valueElement.hasAttribute(VALUE_FROM)){
					from = valueElement.getAttribute(VALUE_FROM);
					to = valueElement.getAttribute(VALUE_TO);
				}
				
				//depending on the type base and class, create an exact value
				if(typeBase.equals(Type.BASE_NUMERIC)){
					// range numeric
	        		  if(from != null && to != null){
	        			  SimpleNumeric snfrom = new SimpleNumeric(Double.parseDouble(from));
	        			  SimpleNumeric snto = new SimpleNumeric(Double.parseDouble(to));
	        			  Range rnvalue = new Range(snfrom,snto);
	        			  
	        			  value = rnvalue;
	        		  }else{
	        			  SimpleNumeric snvalue = new SimpleNumeric();

	        			  snvalue.setValue(Double.parseDouble(is));

	        			  value = snvalue;
	        		  }
	        	  }else{
	        		  //range symbolic (this apply only when the type is ordered)
	        		  if(from != null && to != null){
	        			  SimpleSymbolic ssfrom = new SimpleSymbolic(from);
	        			  SimpleSymbolic ssto = new SimpleSymbolic(to);
	        			  
	        			  Range rsvalue = new Range(ssfrom,ssto);
	        			  
	        			  
	        			  value = rsvalue;
	        		  }else{
	        			  
	        			  SimpleSymbolic ssvalue = new SimpleSymbolic(is);
	        			  
	        			  if(num != null) ssvalue.setOrder(Integer.valueOf(num));
	        			  
	        			  value = ssvalue;
	        		  }
	        	  }
				
				//Check if the value is not null and add it to the list
				if(value != null){
	        		  values.add(value); 
	        	}
			}
		}
		return values;
	}
}

/**
 * This class represents a rule, but without references to other rules and tables.
 * This connections re defined by the ruleLinks and tabLinks variables, and in the 
 * model they should be references to real objects.
 * Because during parsing, there is no possibility to link rules and tables with objects that do not exists, 
 * first all links are read as String references, and later this BlindRule objects are transformed into real Rule objects.
 * @author sbk
 *
 */
class BlindRule {
	String id;
	String ownerTableId;
	
	LinkedList<String> ruleLinks;
	LinkedList<String> tabLinks;

	LinkedList<Formulae> conditions;
	LinkedList<Decision> decisions;
	
	public BlindRule() {
		ruleLinks = new LinkedList<String>();
		tabLinks =  new LinkedList<String>();
		conditions = new LinkedList<Formulae>();
		decisions = new LinkedList<Decision>();
	}
	
}
