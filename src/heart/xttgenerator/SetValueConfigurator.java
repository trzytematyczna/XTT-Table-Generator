package heart.xttgenerator;

import java.util.LinkedList;
import java.util.Random;

import heart.alsvfd.SetValue;
import heart.alsvfd.Value;

public class SetValueConfigurator {

	private static int SETVALUE_COUNTER = 0;
	private static LinkedList<SetValue> setValues = new LinkedList<SetValue>();
	
	private LinkedList<Value> values;
	private ValueConfigurator valuesParam;
	private String baseParam;
	
	public boolean validateConfiguration() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public SetValue generateSetValue(Random random, String base){
		return new SetValue();
	}
	
	public SetValue generateSetValue(Random random){
		return new SetValue();
	}
	
}
