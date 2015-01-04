package heart.xttgenerator;

import static org.junit.Assert.*;
import heart.xtt.Attribute;
import heart.xtt.Table;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class TableConfiguratorTest {
	
	private static final String CONFIGURATION = 
		"{"
		+ "\"id\":null,"
		+ "\"name\":null,"
		+ "\"description\":null,"
		+ "\"precConcParam\":[0.9,0.1],"
		+ "\"attributesNumberParam\":[1,3]"
		+ "}";
	
	public void printConfiguration() throws JsonGenerationException, JsonMappingException, IOException {
		TableConfigurator tableConfigurator =  new TableConfigurator();
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(tableConfigurator));
	}
	
	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		TableConfigurator tableConfigurator = mapper.readValue(CONFIGURATION, TableConfigurator.class);
		Random random = new Random();
		LinkedList<Attribute> attributes = new LinkedList<Attribute>();
		attributes.add(null); //TODO gen attributes
		attributes.add(null);
		attributes.add(null);
		Table table1 = tableConfigurator.generateTable(random, attributes);
	}

}
