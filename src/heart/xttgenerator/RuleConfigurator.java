package heart.xttgenerator;

import heart.Action;
import heart.alsvfd.Formulae;
import heart.xtt.Decision;
import heart.xtt.Rule;
import heart.xtt.Table;

import java.util.LinkedList;

public class RuleConfigurator {

	protected String id;

	protected String schemeName;


	protected Integer orderNumber;
	

	protected float certaintyFactor;
	

	protected LinkedList<Rule> ruleLinks;
	

	protected LinkedList<Table> tabLinks;


	protected LinkedList<Formulae> conditions;
	

	protected LinkedList<Decision> decisions;
	

	protected LinkedList<String> actions;
	
}
