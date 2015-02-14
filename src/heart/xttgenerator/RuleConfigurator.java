package heart.xttgenerator;

import heart.Action;
import heart.alsvfd.Formulae;
import heart.alsvfd.Formulae.Builder;
import heart.alsvfd.Formulae.ConditionalOperator;
import heart.exceptions.ModelBuildingException;
import heart.xtt.Attribute;
import heart.xtt.Decision;
import heart.xtt.Rule;
import heart.xtt.Rule.Builder.IncompleteRuleId;
import heart.xtt.Table;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class RuleConfigurator {

	private static int RULE_COUNTER = 0;
	private static LinkedList<Rule> rules = new LinkedList<Rule>();
	
	private final static String GEN_ID = "id";
	private final static String GEN_SCHEMENAME = "schemeName";
	
	private String id;
	private String schemeName;
//	private Integer orderNumber;
//	private float certaintyFactor;
//	private LinkedList<Rule> ruleLinks; TODO
//	private LinkedList<Table> tabLinks; TODO
	private LinkedList<Formulae.Builder> conditions;
	private LinkedList<Decision.Builder> decisions;
	private LinkedList<String> actions;
	private Integer[] formulaeParam;
	private Integer[] decisionParam;
//	private FormulaeConfigurator formulaeConf;
//	private DecisionConfigurator decisionConf;
	
	public boolean validateConfiguration() {
//		if(this.conditions.isEmpty()) return false;
//		if(this.decisions.isEmpty()) return false;
		if(this.formulaeParam == null || this.formulaeParam.length != 2 || this.formulaeParam[0]<=0 || this.formulaeParam[1] <= 0) return false;
		if(this.decisionParam == null || this.decisionParam.length != 2 || this.decisionParam[0]<=0 || this.decisionParam[1] <= 0) return false;
		
		return true;
	}
	
	public Rule generateRule(Random random, LinkedList<Attribute> precondition, LinkedList<Attribute> conclusion) throws ModelBuildingException {
		Rule rule = new Rule();
		if(this.validateConfiguration()){
			Rule.Builder builder = new Rule.Builder();
			this.conditions = new LinkedList<Formulae.Builder>();
			this.decisions = new LinkedList<Decision.Builder>();
			IncompleteRuleId ri = new IncompleteRuleId();
			if (this.schemeName != null) {
				rule.setSchemeName(this.schemeName);
				ri.schemeName=this.schemeName;
			}
			else {
				rule.setSchemeName(RuleConfigurator.GEN_SCHEMENAME);
				ri.schemeName=RuleConfigurator.GEN_SCHEMENAME;
			}
			ri.orderNumber = RuleConfigurator.RULE_COUNTER;
//			if (this.id != null) builder.setRuleId(ri);
//			else rule.setId(RuleConfigurator.GEN_ID + RuleConfigurator.RULE_COUNTER);
			
			builder.setRuleId(ri);
			rule.setId(RuleConfigurator.GEN_ID+RuleConfigurator.RULE_COUNTER);
			rule.setOrderNumber(RuleConfigurator.RULE_COUNTER);
			rule.setSchemeName(ri.schemeName);
			
//			Integer form = random.nextInt(this.formulaeParam[1] - this.formulaeParam[0]) + this.formulaeParam[0];
			HashMap<String , Attribute> map_formulae = new HashMap<String, Attribute>();
			for(int i=0; i<precondition.size(); i++){
//				Integer att = random.nextInt((precondition.size()-1));				
//				Attribute attrib = precondition.get(att);
				Attribute attrib = precondition.get(i);
				Formulae.ConditionalOperator co = null;
				Integer operator = random.nextInt(2-1)+1;
				switch(operator){
					case 1 : 
						co = ConditionalOperator.EQ;
						break;
					case 2 :
						co =  ConditionalOperator.GT;
				}
				Formulae formul = new Formulae();
				formul.setAttribute(attrib);
				formul.setOp(co);
				formul.setValue(attrib.getType().getDomain()); //TODO: wylosowanie wartosci value ? (teraz: caly przedzial)
				
				//dodatnie do mapy potrzebnej dla buildera w setConditions
				map_formulae.put(attrib.getName(), attrib);

				Formulae.Builder form_builder = new Formulae.Builder();
				form_builder.setAttribute(attrib);
				form_builder.setAttributeName(attrib.getName());
				form_builder.setOp(co);
				form_builder.setValue(attrib.getType().getDomain());
				//dodanei do incConditions
				conditions.add(form_builder);
			}

			builder.setConditions(this.conditions);
			builder.buildConditions(map_formulae);
			
//			Integer dec = random.nextInt(this.decisionParam[1] - this.decisionParam[0]) + this.decisionParam[0];
			HashMap<String , Attribute> map_decisions = new HashMap<String, Attribute>();
			for(int i=0; i<conclusion.size(); i++){
//				Integer att = random.nextInt((conclusion.size()-1));
//				Attribute attrib = conclusion.get(att);
			
				Attribute attrib = conclusion.get(i);
				
				Decision decis = new Decision();
				decis.setAttribute(attrib);
				decis.setDecision(attrib.getType().getDomain()); //TODO analogicznie
				
				map_decisions.put(attrib.getName(), attrib);

				Decision.Builder dec_builder = new Decision.Builder(); 
				dec_builder.setAttribute(attrib);
				dec_builder.setAttributeName(attrib.getName());
//				dec_builder.setDecision(decis.getDecision());
//				dec_builder.setIncompleteDecision(decis.getDecision());

				decisions.add(dec_builder);
			}
            builder.setDecisions(decisions);
            builder.buildDecisions(map_decisions);
            
			builder.build();
			RuleConfigurator.RULE_COUNTER++;
			RuleConfigurator.rules.add(rule);
		}
		System.out.println(rule.getActions());
		System.out.println(rule.getConditions());
		System.out.println(rule.getDecisions());
		System.out.println(rule.getName());
		System.out.println(rule.getId());
		
		return rule;
	}

	public static LinkedList<Rule> getRules() {
		return rules;
	}

	public static void setRules(LinkedList<Rule> rules) {
		RuleConfigurator.rules = rules;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public LinkedList<Formulae.Builder> getConditions() {
		return conditions;
	}

	public void setConditions(LinkedList<Formulae.Builder> conditions) {
		this.conditions = conditions;
	}

	public LinkedList<Decision.Builder> getDecisions() {
		return decisions;
	}

	public void setDecisions(LinkedList<Decision.Builder> decisions) {
		this.decisions = decisions;
	}

	public LinkedList<String> getActions() {
		return actions;
	}

	public void setActions(LinkedList<String> actions) {
		this.actions = actions;
	}

	public Integer[] getFormulaeParam() {
		return formulaeParam;
	}

	public void setFormulaeParam(Integer[] formulaeParam) {
		this.formulaeParam = formulaeParam;
	}

	public Integer[] getDecisionParam() {
		return decisionParam;
	}

	public void setDecisionParam(Integer[] decisionParam) {
		this.decisionParam = decisionParam;
	}
	
	
	
}
