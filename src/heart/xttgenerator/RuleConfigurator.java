package heart.xttgenerator;

import heart.Action;
import heart.alsvfd.Formulae;
import heart.alsvfd.Formulae.Builder;
import heart.xtt.Attribute;
import heart.xtt.Decision;
import heart.xtt.Rule;
import heart.xtt.Rule.Builder.IncompleteRuleId;
import heart.xtt.Table;

import java.util.LinkedList;
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
		if(this.conditions.isEmpty()) return false;
		if(this.decisions.isEmpty()) return false;
		if(this.formulaeParam == null || this.formulaeParam.length != 2 || this.formulaeParam[0]<=0 || this.formulaeParam[1] <= 0) return false;
		if(this.decisionParam == null || this.decisionParam.length != 2 || this.decisionParam[0]<=0 || this.decisionParam[1] <= 0) return false;
		
		return true;
	}
	
	public Rule generateRule(Random random, LinkedList<Attribute> precondition, LinkedList<Attribute> conclusion) {
		Rule rule = new Rule();
		if(this.validateConfiguration()){
			Rule.Builder builder = new Rule.Builder();
//			Formulae f = new Formulae();
//			f.setAttribute(at);
//			f.setOp(op);
//			f.setValue(v);
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
//			if (this.id != null) builder.setRuleId();
//			else rule.setId(RuleConfigurator.GEN_ID + RuleConfigurator.RULE_COUNTER);
			builder.setRuleId(ri);
			Integer form = random.nextInt(this.formulaeParam[1] - this.formulaeParam[0]) + this.formulaeParam[0];
			for(int i=0; i<form; i++){
//				conditions.add()
//				conditions.add(this.formulaeConf.generateFormulae(random, precondition));
			}
			builder.setConditions(this.conditions);
			Integer dec = random.nextInt(this.decisionParam[1] - this.decisionParam[0]) + this.decisionParam[0];
			for(int i=0; i<dec; i++){
//				decisions.add(this.decisionConf.generateDecision(random, conclusion));				
			}

			rule.setOrderNumber(RuleConfigurator.RULE_COUNTER);
			//TODO
			RuleConfigurator.RULE_COUNTER++;
			RuleConfigurator.rules.add(rule);
		}
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
	
	
	
}
