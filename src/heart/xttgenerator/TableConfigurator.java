package heart.xttgenerator;

import heart.xtt.Attribute;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.Type;

import java.util.LinkedList;
import java.util.Random;

public class TableConfigurator {

	private static int TABLE_COUNTER = 0;
	private static LinkedList<Table> tables = new LinkedList<Table>();
	
	private final static String GEN_ID = "id";
	private final static String GEN_NAME = "name";
	private final static String GEN_DESC = "desc";
	
	private String id;
	private String name;
	private String description;
	
	private double[] precConcParam;
	private int[] attributesNumberParam;
	
	private LinkedList<Rule> rules;
	private RuleConfigurator rulesParam;
	private int[] rulesNumberParam;
	
	public TableConfigurator() {
		super();
	}
	
	public boolean validateConfiguration(LinkedList<Attribute> attributes){
		if (attributes.size() < 2) return false;
		if (this.precConcParam.length != 2) return false;
		if (this.precConcParam[0] + this.precConcParam[1] != 1) return false;
		if (this.rules == null && this.rulesParam == null) return false;
		if (this.rules == null && this.rulesNumberParam == null) return false;
		if (this.rulesNumberParam[0] < 0 || this.rulesNumberParam[0] > this.rulesNumberParam[1]) return false;
		return true;
	}
	

	public Table generateTable(Random random, LinkedList<Attribute> attributes) throws Exception {
		if (validateConfiguration(attributes) == true) {
			Table table = new Table();
			if (this.id == null) table.setId(GEN_ID + TableConfigurator.TABLE_COUNTER);
			else table.setId(this.id);
			if (this.name == null) table.setName(GEN_NAME + TableConfigurator.TABLE_COUNTER);
			else table.setName(this.name);
			if (this.description == null) table.setDescription(GEN_DESC + TableConfigurator.TABLE_COUNTER);
			else table.setDescription(this.description);
			LinkedList<Attribute> precondition = new LinkedList<Attribute>();
			precondition.add(attributes.get(random.nextInt(attributes.size())));
			LinkedList<Attribute> conclusion = new LinkedList<Attribute>();
			conclusion.add(attributes.get(random.nextInt(attributes.size())));
			int number = random.nextInt(this.attributesNumberParam[1] - this.attributesNumberParam[0]) + this.attributesNumberParam[0];
			for (int i=0; i < number; i++) {
				double shot = random.nextDouble();
				if (shot < this.precConcParam[0]) precondition.add(attributes.get(random.nextInt(attributes.size())));
				else conclusion.add(attributes.get(random.nextInt(attributes.size())));
			}
			table.setConclusion(precondition);
			table.setPrecondition(conclusion);
			if (this.rules != null) table.setRules(this.rules);
			else {
				LinkedList<Rule> generatedRules = new LinkedList<Rule>();
				number = random.nextInt(this.rulesNumberParam[1] - this.rulesNumberParam[0]) + this.rulesNumberParam[0];
				for (int i = 0; i < number; i++) {
					generatedRules.add(this.rulesParam.generateRule());
				}
				table.setRules(generatedRules);
				//TODO adding Rules to ruleLinks in Rule
			}
			TableConfigurator.TABLE_COUNTER++;
			TableConfigurator.tables.add(table);
			return table;
		}
		else {
			throw new Exception();
		}
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double[] getPrecConcParam() {
		return precConcParam;
	}

	public void setPrecConcParam(double[] precConcParam) {
		this.precConcParam = precConcParam;
	}

	public int[] getAttributesNumberParam() {
		return attributesNumberParam;
	}

	public void setAttributesNumberParam(int[] attributesNumberParam) {
		this.attributesNumberParam = attributesNumberParam;
	}

	public LinkedList<Rule> getRules() {
		return rules;
	}

	public void setRules(LinkedList<Rule> rules) {
		this.rules = rules;
	}

	public RuleConfigurator getRulesParam() {
		return rulesParam;
	}

	public void setRulesParam(RuleConfigurator rulesParam) {
		this.rulesParam = rulesParam;
	}
	
	
	
}
