package heart.xttgenerator;

import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.xtt.Attribute;
import heart.xtt.Table;
import heart.xtt.Type;
import heart.xtt.XTTModel;

import java.util.LinkedList;
import java.util.Random;

public class XTTConfigurator {

	private String version;
	private LinkedList<Table> tables;
	private int[] tablesParam;
	private LinkedList<Type> types;
	private int[] typesParam;
	private LinkedList<Attribute> attributes;
	private int[] attributesParam;
	
	private AttributeConfigurator attributeConfigurator;
	//private SetValueConfigurator setValueConfigurator;
	private TypeConfigurator typeConfigurator;
	private ValueConfigurator valueConfigurator;
	//TODO adding next generators to overall configuration object and json file
	
	public boolean validateConfiguration(){
		if (this.tables == null && ((this.tablesParam[0] > this.tablesParam[1] || this.tablesParam[1] < 0) || true)) return false; //TODO tableConfigurator
		if (this.types == null && ((this.typesParam[0] > this.typesParam[1] || this.typesParam[1] < 0) || this.typeConfigurator == null)) return false;
		if (this.attributes == null && ((this.attributesParam[0] > this.attributesParam[1] || this.attributesParam[1] < 0) || this.attributeConfigurator == null)) return false;
		return true;
	}
	
	public XTTModel generateXTTModel(Random random) throws Exception {
		XTTModel xttModel = new XTTModel(4); //TODO add new source to XTTModel, 4 in meantime
		int number = random.nextInt(this.tablesParam[1] - this.tablesParam[0]) + this.tablesParam[0];
		LinkedList<Table> tables = new LinkedList<Table>();
		for (int i = 0; i < number; i++) {
			//tables.add(this.tableConfigurator.generateTable(random)); TODO
		}
		xttModel.setTables(tables);
		number = random.nextInt(this.typesParam[1] - this.typesParam[0]) + this.typesParam[0];
		LinkedList<Type> types = new LinkedList<Type>();
		for (int i = 0; i < number; i++) {
			types.add(this.typeConfigurator.generateType(random));
		}
		number = random.nextInt(this.attributesParam[1] - this.attributesParam[0]) + this.attributesParam[0];
		LinkedList<Attribute> attributes = new LinkedList<Attribute>();
		for (int i = 0; i < number; i++) {
			attributes.add(this.attributeConfigurator.generateAttribute(random, types));
		}
		xttModel.setAttributes(attributes);
		return xttModel;
	}
	
}
