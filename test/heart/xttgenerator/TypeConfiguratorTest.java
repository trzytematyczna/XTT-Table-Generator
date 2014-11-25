package heart.xttgenerator;

import static org.junit.Assert.*;
import heart.alsvfd.SetValue;
import heart.xtt.Type;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.JsonGenerationException;

public class TypeConfiguratorTest {

	abstract class MixIn {
		@JsonIgnore abstract boolean isEmpty();
	}
	
	@Test
	public void test() throws Exception {
		TypeConfigurator typeConfigurator = new TypeConfigurator();
		typeConfigurator.setBaseParam(new Double[]{0.3, 0.6, 0.9});
		typeConfigurator.setDescription("description1");
		typeConfigurator.setLengthParam(new Integer[]{3, 5});
		typeConfigurator.setOrdered(Type.ORDERED_NO);
		typeConfigurator.setPrecision(4);
		typeConfigurator.setDomain(new SetValue());
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().addMixInAnnotations(SetValue.class, MixIn.class);
		String typeConfiguratorString = mapper.writeValueAsString(typeConfigurator);
		System.out.println(typeConfiguratorString);
		TypeConfigurator typeConfiguratorParsed = mapper.readValue(typeConfiguratorString, TypeConfigurator.class);
		Type type = typeConfigurator.generateType(new Random());
	}

}
