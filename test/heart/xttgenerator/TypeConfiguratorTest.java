package heart.xttgenerator;

import static org.junit.Assert.*;
import heart.alsvfd.SetValue;
import heart.xtt.Type;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.JsonGenerationException;

public class TypeConfiguratorTest {

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
	
	abstract class MixIn {
		@JsonIgnore abstract boolean isEmpty();
	}
	
	public void printConfiguration() throws JsonGenerationException, JsonMappingException, IOException {
		TypeConfigurator typeConfigurator = new TypeConfigurator();
		ObjectMapper mapper = new ObjectMapper();
//		mapper.getSerializationConfig().addMixInAnnotations(SetValue.class, MixIn.class);
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		System.out.println(mapper.writeValueAsString(typeConfigurator));
	}
	
	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
//		mapper.getSerializationConfig().addMixInAnnotations(SetValue.class, MixIn.class);
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		TypeConfigurator typeConfigurator = mapper.readValue(CONFIGURATION, TypeConfigurator.class);
		Random random = new Random();
		Type type1 = typeConfigurator.generateType(random);
	}

}
