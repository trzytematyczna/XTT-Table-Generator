package heart.xttgenerator;

import static org.junit.Assert.*;
import jdk.nashorn.internal.parser.JSONParser;

import org.json.*;
import org.junit.Test;

public class TypeConfiguratorTest {

	@Test
	public void test() {
		TypeConfigurator typeConfigurator = new TypeConfigurator();
		typeConfigurator.setBaseParam(new Double[]{0.3, 0.6, 0.9});
		typeConfigurator.setDescription("description1");
		typeConfigurator.setLengthParam(new Integer[]{3, 5});
		JSONObject jsonObject = new JSONObject();
//		jsonObject.
	}

}
