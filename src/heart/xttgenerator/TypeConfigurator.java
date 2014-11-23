package heart.xttgenerator;

import java.util.Random;

import heart.alsvfd.SetValue;
import heart.xtt.Type;

public class TypeConfigurator {
/*
 * Type = "xtype" "[" "name" ":" String ","
                   "base" ":" ("numeric" / "symbolic") ","
		           "domain" ":" List ","
		           "desc" ":" String ","
                   "ordered" ":" Boolean  
                   "step" ":" Integer "]" "."
 */
	private static int TYPE_COUNTER = 0;
	
	private final static String GEN_ID = "id";
	private final static String GEN_NAME = "name";
	private final static String GEN_DESC = "desc";
	
	private String id;
	private String name;
	private Integer length;
	private Integer[] lengthParam;
	private String base;
	private Double[] baseParam;
	private String ordered;
	private Double[] orderedParam;
	private String description;
	private Integer precision;
	private Integer precisionParam;
	private SetValue domain;
	private Double[] domainParam;
	private Integer domainCount;
	
	public TypeConfigurator() {
		super();
		TypeConfigurator.TYPE_COUNTER++;
	}
	
	public boolean validateConfiguration(){
		Boolean isValid = true;
		return isValid;
	}
	
	public Type generateType(Random random) throws Exception{
		Type type = null;
		if (this.validateConfiguration()){
			Type.Builder builder = new Type.Builder();
			if (this.id != null) builder.setId(this.description);
			else builder.setId(new String(GEN_ID + TYPE_COUNTER));
			if (this.name != null) builder.setName(this.name);
			else builder.setName(new String(GEN_NAME + TYPE_COUNTER));
			if (this.length != null) builder.setLength(this.length);
			else {
				Integer pickedLength = random.nextInt(this.lengthParam[this.lengthParam[1] - this.lengthParam[0]]) + this.lengthParam[0];
				builder.setLength(pickedLength);
			}
			String pickedBase = this.base;
			if (this.base != null) builder.setBase(this.base);
			else {
				Double shot = random.nextDouble();
				if (this.baseParam[0] > shot) {
					builder.setBase(Type.BASE_NUMERIC);
					pickedBase = Type.BASE_NUMERIC;
				}
				else if (this.baseParam[0] + this.baseParam[1] > shot) {
					builder.setBase(Type.BASE_SYMBOLIC);
					pickedBase = Type.BASE_SYMBOLIC;
				}
				else {
					builder.setBase(Type.BASE_UNKNOWN);
					pickedBase = Type.BASE_UNKNOWN;
				}
			}
			if (pickedBase.equals(Type.BASE_NUMERIC)) builder.setOrdered(Type.ORDERED_YES);
			else{
				if (this.ordered != null) builder.setOrdered(this.ordered);
				else {
					Double shot = random.nextDouble();
					if (this.orderedParam[0] > shot) builder.setBase(Type.ORDERED_YES);
					else if (this.orderedParam[0] + this.orderedParam[1] > shot) builder.setBase(Type.ORDERED_NO);
					else builder.setBase(Type.ORDERED_UNKNOWN);
				}
			}
			if (this.description != null) builder.setDescription(this.description);
			else builder.setDescription(new String(GEN_DESC + TYPE_COUNTER));
			if (this.precision != null) builder.setPrecision(this.precision);
			else {
				Integer pickedPrecision = random.nextInt(this.precisionParam);
				builder.setPrecision(pickedPrecision);
			}
//			builder.setDomain()
			type = builder.build();
		}
		else{
			throw new Exception(); //TODO exception
		}
		return type;
	}
	
}
