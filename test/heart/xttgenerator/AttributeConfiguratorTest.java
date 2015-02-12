package heart.xttgenerator;


import heart.xtt.Attribute;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.Type;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Random;

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class AttributeConfiguratorTest {
	private static final String CONFIGURATION = 
		"{"
		+ "\"id\":null,"
		+ "\"name\":null,"
		+ "\"comm\":null,"
		+ "\"commParam\":[0.1,0.2,0.3,0.4,0.5],"
		+ "\"xttClass\":null,"
		+ "\"xttClassParam\":[0.1,0.4,0.3,0.2,0.9],"
		+ "\"description\":\"description1\","
		+ "\"type\":null,"
		+ "\"typeParam\":[0.2,0.4,0.6,0.8]"
		+ "}";
	
	private static final String TYPE_CONFIGURATION = 
			"{"
			+ "\"id\":null,"
			+ "\"name\":null,"
			+ "\"length\":null,"
			+ "\"lengthParam\":[2,4],"
			+ "\"base\":null,"
			+ "\"baseParam\":[0.5, 0.4, 0.1],"
			+ "\"ordered\":null,"
			+ "\"orderedParam\":[0.5, 0.5],"
			+ "\"description\":null,"
			+ "\"precision\":null,"
			+ "\"precisionParam\":4,"
			+ "\"domain\":null,"
			+ "\"domainParam\":"
				+ "{"
				+ "\"values\":null,"
				+ "\"valuesParam\":[10,20],"
				+ "\"valuesIsRangeParam\":0.5,"
				+ "\"anyNullParam\":[0.1,0.1],"
				+ "\"valuesLengthParam\":[3,5]"
				+ "}"
			+ "}";

//	@Test
//	public void generateAttributeTest() throws Exception {
//		ObjectMapper mapper = new ObjectMapper();
//		AttributeConfigurator attributeConfigurator = mapper.readValue(CONFIGURATION, AttributeConfigurator.class);
//		Random random = new Random();
//		LinkedList<Type> types = new LinkedList<Type>();
//		for (int i = 0; i < 10; i++) {
//			types.add(null);
//		}
//		Attribute attribute1 = attributeConfigurator.generateAttribute(random, types);
//	}

	@Test
	public void test() throws Exception {
		AttributeConfigurator attributeConfigurator = new AttributeConfigurator();
		attributeConfigurator.setCommParam(new Double[]{0.1, 0.2, 0.3, 0.4, 0.5});
		attributeConfigurator.setXttClassParam(new Double[]{0.1, 0.4, 0.3, 0.2, 0.9});
		attributeConfigurator.setDescription("description1");
		attributeConfigurator.setTypeParam(new Double[]{0.2, 0.4, 0.6, 0.8});
		
		ObjectMapper mapper = new ObjectMapper();
//		mapper.getSerializationConfig().addMixInAnnotations(SetValue.class, MixIn.class);
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		String attributeConfiguratorString = mapper.writeValueAsString(attributeConfigurator);
		

		File file = new File("AttributeConfiguration.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(attributeConfiguratorString);
		bw.close();
		
		TypeConfigurator typeConfigurator = mapper.readValue(TYPE_CONFIGURATION, TypeConfigurator.class);
		Random random = new Random();

		LinkedList<Type> types = new LinkedList<Type>() ;
		for (int i=0; i<4; i++){
			Type type1 = typeConfigurator.generateType(random);
			types.add(type1);
		}
		Attribute attr = attributeConfigurator.generateAttribute(new Random(), types);
		
		
		RuleConfigurator ruleConfigurator = new RuleConfigurator();
		ruleConfigurator.setDecisionParam(new Integer[]{1,2});
		ruleConfigurator.setFormulaeParam(new Integer[]{1,2});
		
		LinkedList<Attribute> att = new LinkedList<Attribute>();
		att.add(attr);
		att.add(attr);
		Rule r = ruleConfigurator.generateRule(new Random(), att, att);
		int i=0;
//		String typeString = mapper.writeValueAsString(type);
//		
//		file = new File("GeneratedType.txt");
//		if (!file.exists()) {
//			file.createNewFile();
//		}
//		fw = new FileWriter(file.getAbsoluteFile());
//		bw = new BufferedWriter(fw);
//		bw.write(typeString);
//		bw.close();
	}

}
