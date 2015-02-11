package heart.xttgenerator;

import static org.junit.Assert.*;
import heart.alsvfd.SetValue;
import heart.xtt.Type;
import heart.xtt.XTTModel;

import java.io.IOException;
import java.util.Random;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class XTTConfiguratorTest {

	private static final String CONFIGURATION = 
			"{"
			+ "\"version\":1.0,"
			+ "\"tables\":null,"
			+ "\"tablesParam\":[3,5],"
			+ "\"tableConfigurator\":"
				+ "{"
				+ "\"id\":null,"
				+ "\"name\":null,"
				+ "\"description\":null,"
				+ "\"precConcParam\":[0.9,0.1],"
				+ "\"attributesNumberParam\":[1,3],"
				+ "\"rules\":null,"
				+ "\"rulesParam\":null,"
				+ "\"rulesNumberParam\":null"
				+ "},"
			+ "\"types\":null,"
			+ "\"typesParam\":[5,10],"
			+ "\"typeConfigurator\":"
				+ "{"
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
				+ "},"
			+ "\"attributes\":null,"
			+ "\"attributesParam\":[5,10],"
			+ "\"attributeConfigurator\":"
				+ "{"
				+ "\"id\":null,"
				+ "\"name\":null,"
				+ "\"comm\":null,"
				+ "\"commParam\":[0.1,0.2,0.3,0.4,0.5],"
				+ "\"xttClass\":null,"
				+ "\"xttClassParam\":[0.1,0.4,0.3,0.2,0.9],"
				+ "\"description\":\"description1\","
				+ "\"type\":null,"
				+ "\"typeParam\":[0.2,0.4,0.6,0.8]"
				+ "}"
			+ "}";
	
	public void printConfiguration() throws JsonGenerationException, JsonMappingException, IOException {
		XTTConfigurator xttConfigurator =  new XTTConfigurator();
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(xttConfigurator));
	}
	
	@Test
	public void generateXTTModelTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		XTTConfigurator xttConfigurator = mapper.readValue(CONFIGURATION, XTTConfigurator.class);
		Random random = new Random();
 		XTTModel xttModel = xttConfigurator.generateXTTModel(random);
		int i=0;
	}

}
