package heart.xttgenerator;

import java.util.LinkedList;
import java.util.Random;

import heart.alsvfd.Any;
import heart.alsvfd.Range;
import heart.alsvfd.SetValue;
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.xtt.Type;

public class SetValueConfigurator {

	private final static String GEN_SYMBOLIC_VALUE = "value";
	
	private static int SETVALUE_COUNTER = 0;
	private static LinkedList<SetValue> setValues = new LinkedList<SetValue>();
	
	private LinkedList<Value> values;
	private double[] valuesParam;
	private double valuesIsRangeParam;
	private double[] rangeParam;
	private int[] valuesLengthParam;
	
	public boolean validateConfiguration(String base) {
		if (this.values == null) {
			if (base == Type.BASE_NUMERIC) {
				if (this.valuesParam == null || this.valuesIsRangeParam < 0 || this.valuesIsRangeParam > 1) return false;
				if (this.valuesIsRangeParam == 0 && this.valuesLengthParam == null) return false;
				if (this.valuesIsRangeParam == 1 && this.rangeParam == null) return false;
				if (this.valuesLengthParam == null || this.rangeParam == null) return false;
				if (this.rangeParam[0] > this.rangeParam[1]) return false;
				if (this.rangeParam[0] < 0) return false;
			}
			if (this.valuesLengthParam == null) return false;
			if (this.valuesLengthParam[0] > this.valuesLengthParam[1]) return false;
			if (this.valuesLengthParam[0] < 0) return false;
		}
		return true;
	}
	
	public SetValue generateSetValue(Random random, String base) throws Exception {
		SetValue setValue = null;
		if (this.validateConfiguration(base)) {
			if (this.values != null){
				setValue = new SetValue(values);
			}
			else if (base.equals(Type.BASE_NUMERIC)) {
				if (valuesLengthParam != null) {
					int number = random.nextInt(this.valuesLengthParam[1] - this.valuesLengthParam[0]);
					LinkedList<Value> values = new LinkedList<Value>();
					for (int i = 0; i < number; i++) {
						double shot = random.nextDouble();
						if (shot < this.valuesIsRangeParam) {
							values.add(new Range(new SimpleNumeric(this.valuesParam[0]), new SimpleNumeric(this.valuesParam[1])));
						}
						double value = random.nextDouble() * (this.valuesParam[1] - this.valuesParam[0]) + this.valuesParam[0];
						values.add(new SimpleNumeric(value, random.nextFloat()));
					}
					setValue = new SetValue(values);
				}
			}
			else if (base.equals(Type.BASE_SYMBOLIC)) {
				int number = random.nextInt(this.valuesLengthParam[1] - this.valuesLengthParam[0]);
				LinkedList<Value> values = new LinkedList<Value>();
				for (int i = 0; i < number; i++) {
					values.add(new SimpleSymbolic(SetValueConfigurator.GEN_SYMBOLIC_VALUE + i, i));
				}
				setValue = new SetValue(values);
			}// TODO is it all ??
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
