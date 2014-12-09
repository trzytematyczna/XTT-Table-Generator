package heart.xttgenerator;

import java.util.LinkedList;
import java.util.Random;

import heart.alsvfd.SetValue;
import heart.xtt.Type;

public class TypeConfigurator {

	private static int TYPE_COUNTER = 0;
	private static LinkedList<Type> types = new LinkedList<Type>();
	
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
	private SetValueConfigurator domainParam;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer[] getLengthParam() {
		return lengthParam;
	}

	public void setLengthParam(Integer[] lengthParam) {
		this.lengthParam = lengthParam;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public Double[] getBaseParam() {
		return baseParam;
	}

	public void setBaseParam(Double[] baseParam) {
		this.baseParam = baseParam;
	}

	public String getOrdered() {
		return ordered;
	}

	public void setOrdered(String ordered) {
		this.ordered = ordered;
	}

	public Double[] getOrderedParam() {
		return orderedParam;
	}

	public void setOrderedParam(Double[] orderedParam) {
		this.orderedParam = orderedParam;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Integer getPrecisionParam() {
		return precisionParam;
	}

	public void setPrecisionParam(Integer precisionParam) {
		this.precisionParam = precisionParam;
	}

	public SetValue getDomain() {
		return domain;
	}

	public void setDomain(SetValue domain) {
		this.domain = domain;
	}

	public SetValueConfigurator getDomainParam() {
		return domainParam;
	}

	public void setDomainParam(SetValueConfigurator domainParam) {
		this.domainParam = domainParam;
	}
	
	public boolean validateConfiguration(){
		if (this.length == null && this.lengthParam == null) return false;
		if (this.length == null && this.lengthParam != null && (this.lengthParam.length != 2 || (this.lengthParam[0] >= this.lengthParam[1]) || this.lengthParam[0] < 0 || this.lengthParam[1] < 0)) return false;
		if (this.base == null && this.baseParam == null) return false;
		if (this.base != null && !(this.base.equals(Type.BASE_NUMERIC) || this.base.equals(Type.BASE_SYMBOLIC) || this.base.equals(Type.BASE_UNKNOWN))) return false;
		if (this.ordered == null && this.orderedParam == null) return false;
		if (this.ordered != null && !(this.ordered.equals(Type.ORDERED_YES) || this.ordered.equals(Type.ORDERED_NO) || this.ordered.equals(Type.ORDERED_UNKNOWN))) return false;
		if (this.base != null && this.base.equals(Type.BASE_NUMERIC) && this.ordered != null && (this.ordered.equals(Type.ORDERED_NO) || this.ordered.equals(Type.ORDERED_UNKNOWN))) return false;
		if (this.precision == null && this.precisionParam == null) return false;
		if (this.precision == null && this.precisionParam != null && this.precisionParam < 0) return false;
		if (this.domain == null && this.domainParam == null) return false;
		if (this.domain == null && this.domainParam.validateConfiguration() == false) return false;
		return true;
	}
	
	public Type generateType(Random random) throws Exception{
		Type type = null;
		if (this.validateConfiguration()){
			Type.Builder builder = new Type.Builder();
			if (this.id != null) builder.setId(this.id);
			else builder.setId(new String(GEN_ID + TYPE_COUNTER));
			if (this.name != null) builder.setName(this.name);
			else builder.setName(new String(GEN_NAME + TYPE_COUNTER));
			if (this.length != null) builder.setLength(this.length);
			else {
				Integer pickedLength = random.nextInt(this.lengthParam[1] - this.lengthParam[0]) + this.lengthParam[0];
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
			if (this.domain != null) builder.setDomain(this.domain);
			else{
				builder.setDomain(this.domainParam.generateSetValue(random, pickedBase));
			}
			type = builder.build();
		}
		else{
			throw new Exception(); //TODO exception
		}
		TypeConfigurator.TYPE_COUNTER++;
		TypeConfigurator.types.add(type);
		return type;
	}
	
}
