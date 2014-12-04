package heart.xttgenerator;

import java.util.LinkedList;
import java.util.Random;

import heart.alsvfd.SetValue;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.xtt.Type;

public class SetValueConfigurator {

	private final static String GEN_SYMBOLIC_VALUE = "value";
	
	private static int SETVALUE_COUNTER = 0;
	private static LinkedList<SetValue> setValues = new LinkedList<SetValue>();
	
	private LinkedList<Value> values;
	private double[] valuesParam;
	private int[] valuesLengthParam;
	private String baseParam;
	
	public boolean validateConfiguration() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean validateConfiguration(String base) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public SetValue generateSetValue(Random random, String base) throws Exception {
		SetValue setValue = null;
		if (this.validateConfiguration(base)) {
			if (this.values != null){
				return new SetValue(values);
			}
			if (base.equals(Type.BASE_NUMERIC)) {
				
			}
			else if (base.equals(Type.BASE_SYMBOLIC)) {
				int number = random.nextInt(this.valuesLengthParam[1] - this.valuesLengthParam[0]) + this.valuesLengthParam[0];
				LinkedList<Value> values = new LinkedList<Value>();
				for (int i = 0; i < number; i++) {
					values.add(new SimpleSymbolic(SetValueConfigurator.GEN_SYMBOLIC_VALUE + i, i)); //TODO make sure what order does
				}
				return new SetValue(values);
			}
			else {
				
			}
		}
		else {
			throw new Exception(); //TODO exception
		}
		SetValueConfigurator.SETVALUE_COUNTER++;
		SetValueConfigurator.setValues.add(setValue);
		return setValue;
	}
	
	public SetValue generateSetValue(Random random) {
		return new SetValue();
	}
	
}
