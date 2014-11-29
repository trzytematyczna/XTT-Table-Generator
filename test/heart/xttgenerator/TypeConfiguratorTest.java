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
		typeConfigurator.setDomainParam(new SetValueConfigurator());
		
		ObjectMapper mapper = new ObjectMapper();
//		mapper.getSerializationConfig().addMixInAnnotations(SetValue.class, MixIn.class);
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		String typeConfiguratorString = mapper.writeValueAsString(typeConfigurator);
		

		File file = new File("TypeConfiguration.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(typeConfiguratorString);
		bw.close();
		
		Type type = typeConfigurator.generateType(new Random());
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
