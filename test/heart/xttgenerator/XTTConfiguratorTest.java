package heart.xttgenerator;

import static org.junit.Assert.*;

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
			+ "\"tablesParam\":null,"
			+ "\"tableConfigurator\":"
				+ "{"
				+ "\"id\":null,"
				+ "\"name\":null,"
				+ "\"description\":null,"
				+ "\"precConcParam\":[0.9,0.1],"
				+ "\"attributesNumberParam\":[1,3]"
				+ "}"
			+ "\"types\":null,"
			+ "\"typesParam\":"
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
				+ "}"
			+ "\"typeConfigurator\":null,"
			+ "\"attributes\":null,"
			+ "\"attributesParam\":null,"
			+ "\"attributeConfigurator\":null" //TODO Monika
			+ "}";
	
	public void printConfiguration() throws JsonGenerationException, JsonMappingException, IOException {
		XTTConfigurator xttConfigurator =  new XTTConfigurator();
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(xttConfigurator));
	}
	
	@Test
	public void generateXTTModelTest() throws JsonGenerationException, JsonMappingException, IOException {
		printConfiguration();
	}

}
