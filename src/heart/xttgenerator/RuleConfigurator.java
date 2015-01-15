package heart.xttgenerator;

import heart.Action;
import heart.alsvfd.Formulae;
import heart.xtt.Decision;
import heart.xtt.Rule;
import heart.xtt.Table;

import java.util.LinkedList;

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
	private LinkedList<Formulae> conditions;
	private LinkedList<Decision> decisions;
	private LinkedList<String> actions;

	public boolean validate() {
		
		return true;
	}
	
	public Rule generateRule() {
		Rule rule = new Rule();
		if (this.id != null) rule.setId(this.id);
		else rule.setId(RuleConfigurator.GEN_ID + RuleConfigurator.RULE_COUNTER);
		if (this.schemeName != null) rule.setSchemeName(this.schemeName);
		else rule.setSchemeName(RuleConfigurator.GEN_SCHEMENAME);
		rule.setOrderNumber(RuleConfigurator.RULE_COUNTER);
		//TODO
		RuleConfigurator.RULE_COUNTER++;
		RuleConfigurator.rules.add(rule);
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

	public LinkedList<Formulae> getConditions() {
		return conditions;
	}

	public void setConditions(LinkedList<Formulae> conditions) {
		this.conditions = conditions;
	}

	public LinkedList<Decision> getDecisions() {
		return decisions;
	}

	public void setDecisions(LinkedList<Decision> decisions) {
		this.decisions = decisions;
	}

	public LinkedList<String> getActions() {
		return actions;
	}

	public void setActions(LinkedList<String> actions) {
		this.actions = actions;
	}
	
	
	
}
