package heart.xttgenerator;


import heart.xtt.Attribute;
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
				+ "\"valuesLengthParam\":[3,5]"
				+ "}"
			+ "}";
	@Test
	public void test() throws Exception {
		AttributeConfigurator attributeConfigurator = new AttributeConfigurator();
		attributeConfigurator.setCommParam(new Double[]{0.1, 0.2, 0.3, 0.4, 0.5});
		attributeConfigurator.setXTTClassParam(new Double[]{0.1, 0.4, 0.3, 0.2, 0.9});
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
		
		TypeConfigurator typeConfigurator = mapper.readValue(CONFIGURATION, TypeConfigurator.class);
		Random random = new Random();

		LinkedList<Type> types = new LinkedList<Type>() ;
		for (int i=0; i<5; i++){
			Type type1 = typeConfigurator.generateType(random);
			types.add(type1);
		}
		Attribute attr = attributeConfigurator.generateAttribute(new Random(), types);
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
