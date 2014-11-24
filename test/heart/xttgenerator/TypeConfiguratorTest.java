package heart.xttgenerator;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.JsonGenerationException;

public class TypeConfiguratorTest {

	@Test
	public void test() throws JsonGenerationException, JsonMappingException, IOException {
		TypeConfigurator typeConfigurator = new TypeConfigurator();
		typeConfigurator.setBaseParam(new Double[]{0.3, 0.6, 0.9});
		typeConfigurator.setDescription("description1");
		typeConfigurator.setLengthParam(new Integer[]{3, 5});
		ObjectMapper mapper = new ObjectMapper();
		String typeConfiguratorString = mapper.writeValueAsString(typeConfigurator);
		TypeConfigurator typeConfiguratorParsed = mapper.readValue(typeConfiguratorString, TypeConfigurator.class);
	}

}
