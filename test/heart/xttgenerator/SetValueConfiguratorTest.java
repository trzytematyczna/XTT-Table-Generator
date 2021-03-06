package heart.xttgenerator;

import static org.junit.Assert.*;
import heart.alsvfd.SetValue;
import heart.xtt.Type;

import java.io.IOException;
import java.util.Random;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class SetValueConfiguratorTest {

	private static final String CONFIGURATION = 
			"{"
			+ "\"values\":null,"
			+ "\"valuesParam\":[10,20],"
			+ "\"valuesIsRangeParam\":0.5,"
			+ "\"anyNullParam\":[0.1,0.1],"
			+ "\"valuesLengthParam\":[3,5]"
			+ "}";
	
	public void printConfiguration() throws JsonGenerationException, JsonMappingException, IOException {
		SetValueConfigurator setValueConfigurator =  new SetValueConfigurator();
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(setValueConfigurator));
	}
	
	@Test
	public void generateSetValueTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		SetValueConfigurator setValueConfigurator = mapper.readValue(CONFIGURATION, SetValueConfigurator.class);
		Random random = new Random();
		SetValue setValue1 = setValueConfigurator.generateSetValue(random, Type.BASE_NUMERIC);
		SetValue setValue2 = setValueConfigurator.generateSetValue(random, Type.BASE_SYMBOLIC);
	}

}
