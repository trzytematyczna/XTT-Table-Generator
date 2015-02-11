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
	private TableConfigurator tableConfigurator;
	
	private LinkedList<Type> types;
	private int[] typesParam;
	private TypeConfigurator typeConfigurator;
	
	private LinkedList<Attribute> attributes;
	private int[] attributesParam;
	private AttributeConfigurator attributeConfigurator;
		
	public boolean validateConfiguration(){
		if (this.tables == null && (this.tablesParam == null || this.tableConfigurator == null)) return false;
		if (this.tables == null && ((this.tablesParam[0] > this.tablesParam[1] || this.tablesParam[1] < 0))) return false; //TODO tableConfigurator
		if (this.types == null && (this.typesParam == null || this.typeConfigurator == null)) return false;
		if (this.types == null && ((this.typesParam[0] > this.typesParam[1] || this.typesParam[1] < 0) || this.typeConfigurator == null)) return false;
		if (this.attributes == null && (this.attributesParam == null || this.attributeConfigurator == null)) return false;
		if (this.attributes == null && ((this.attributesParam[0] > this.attributesParam[1] || this.attributesParam[1] < 0) || this.attributeConfigurator == null)) return false;
		return true;
	}
	
	public XTTModel generateXTTModel(Random random) throws Exception {
		if (this.validateConfiguration() == true) {
			XTTModel xttModel = new XTTModel(4); //TODO add new source to XTTModel, 4 in meantime
			if (this.version != null) xttModel.setVersion(version);
			int number = random.nextInt(this.typesParam[1] - this.typesParam[0]) + this.typesParam[0];
			LinkedList<Type> types = new LinkedList<Type>();
			for (int i = 0; i < number; i++) {
				types.add(this.typeConfigurator.generateType(random));
			}
			number = random.nextInt(this.attributesParam[1] - this.attributesParam[0]) + this.attributesParam[0];
			LinkedList<Attribute> attributes = new LinkedList<Attribute>();
			for (int i = 0; i < number; i++) {
				attributes.add(this.attributeConfigurator.generateAttribute(random, types));
			}
			number = random.nextInt(this.tablesParam[1] - this.tablesParam[0]) + this.tablesParam[0];
			LinkedList<Table> tables = new LinkedList<Table>();
			for (int i = 0; i < number; i++) {
				tables.add(this.tableConfigurator.generateTable(random, attributes));
			}
			xttModel.setTables(tables);
			xttModel.setAttributes(attributes);
			xttModel.setTypes(types);
			return xttModel;
		}
		else {
			throw new Exception();
		}
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

	public int[] getTablesParam() {
		return tablesParam;
	}

	public void setTablesParam(int[] tablesParam) {
		this.tablesParam = tablesParam;
	}

	public TableConfigurator getTableConfigurator() {
		return tableConfigurator;
	}

	public void setTableConfigurator(TableConfigurator tableConfigurator) {
		this.tableConfigurator = tableConfigurator;
	}

	public LinkedList<Type> getTypes() {
		return types;
	}

	public void setTypes(LinkedList<Type> types) {
		this.types = types;
	}

	public int[] getTypesParam() {
		return typesParam;
	}

	public void setTypesParam(int[] typesParam) {
		this.typesParam = typesParam;
	}

	public TypeConfigurator getTypeConfigurator() {
		return typeConfigurator;
	}

	public void setTypeConfigurator(TypeConfigurator typeConfigurator) {
		this.typeConfigurator = typeConfigurator;
	}

	public LinkedList<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(LinkedList<Attribute> attributes) {
		this.attributes = attributes;
	}

	public int[] getAttributesParam() {
		return attributesParam;
	}

	public void setAttributesParam(int[] attributesParam) {
		this.attributesParam = attributesParam;
	}

	public AttributeConfigurator getAttributeConfigurator() {
		return attributeConfigurator;
	}

	public void setAttributeConfigurator(AttributeConfigurator attributeConfigurator) {
		this.attributeConfigurator = attributeConfigurator;
	}
	
}
