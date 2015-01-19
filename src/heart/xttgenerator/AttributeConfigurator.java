package heart.xttgenerator;

import heart.xtt.Attribute;
import heart.xtt.Type;

import java.util.LinkedList;
import java.util.Random;

public class AttributeConfigurator {

	private static int ATTR_COUNTER = 0;
	private static LinkedList<Attribute> attributes = new LinkedList<Attribute>();

	private final static String GEN_ID = "id";
	private final static String GEN_NAME = "att";
	private final static String GEN_DESC = "desc";

	public static final String CLASS_SIMPLE = "simple";
	public static final String CLASS_GENERAL ="general";
	public static final String CLASS_UNKNOWN ="unknown";
	
	public static final String COMM_IN ="in";
	public static final String COMM_OUT = "out";
	public static final String COMM_INTER = "inter";
	public static final String COMM_COMM = "comm";
	public static final String COMM_UNKNOWN ="unknown";

	private String id;
	private String name;
	/**
	 * A field that describes attribute's relation to the world. 
	 * If the attribute is in it means that a value of it is supposed to be given by the user; 
	 * if the attribute is out it means that the value of it should be presented to the user; 
	 * if attribute is inter it means that its value is set by the system as a result of inference process, 
	 * but it's not relevant to the user; if the attribute is comm, it means that it's both in and out.
	 */
	private String comm;
	private Double[] commParam;
	/**
	 * A field that denotes weather the attribute is simple (represents simple values) 
	 * or general (represents set values).
	 */
	private String xttClass;
	private Double[] xttClassParam;
	
	private String description;

	/**
	 * A field that contains a reference to a type defined as xtype in HMR. 
	 * This field denotes the attribute's type.
	 */
	private Type type;
	
	private Double[] typeParam;

//	UNUSED:
//	private String abbreviation; 
//	private String callback;
//	private Long expirationTime;
	
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
	public String getComm() {
		return comm;
	}
	public void setComm(String comm) {
		this.comm = comm;
	}
	public Double[] getCommParam() {
		return commParam;
	}
	public void setCommParam(Double[] commParam) {
		this.commParam = commParam;
	}
	public String getXttClass() {
		return xttClass;
	}
	public void setXttClass(String xTTClass) {
		xttClass = xTTClass;
	}
	public Double[] getXttClassParam() {
		return xttClassParam;
	}
	public void setXttClassParam(Double[] xTTClassParam) {
		xttClassParam = xTTClassParam;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Double[] getTypeParam() {
		return typeParam;
	}
	public void setTypeParam(Double[] typeParam) {
		this.typeParam = typeParam;
	}

	
	public boolean validateConfiguration(){
		if(this.comm == null && this.commParam == null) return false;
		if(this.commParam.length!=5) return false;
		if(this.comm != null && !(this.comm.equals(this.COMM_IN)|| this.comm.equals(this.COMM_OUT)|| this.comm.equals(this.COMM_INTER)||this.comm.equals(this.COMM_COMM) || this.comm.equals(this.COMM_UNKNOWN))) return false;
		if(this.xttClass == null && this.xttClassParam == null) return false;
		if(this.xttClass != null && !(this.xttClass.equals(this.CLASS_SIMPLE)||this.xttClass.equals(this.CLASS_GENERAL)||this.xttClass.equals(this.CLASS_UNKNOWN))) return false;
//		if(this.expirationTime != null ) return false;
		if(this.type == null && this.typeParam == null) return false;
		return true;
	}
	
	
	/*
	private String id; done 
	private String name; done
	private String comm; done ?
	private String[] commParam; done ?
	private String xttClass; done
	private String[] xttClassParam; done
	private String description; done
	private Type type; done ?
	 */
	public Attribute generateAttribute(Random random, LinkedList<Type> types) throws Exception{
		Attribute attribute = null;
		if(this.validateConfiguration()){
			//dlugosc typeParam zgadza sie z liczba typow w types.
			if(this.typeParam.length == types.size()){
				Attribute.Builder builder = new Attribute.Builder();
				if(this.id != null) builder.setId(this.id);
				else builder.setId(new String(GEN_ID + ATTR_COUNTER));
				if(this.name != null) builder.setName(this.name);
				else builder.setName(new String(GEN_NAME + ATTR_COUNTER));
				if (this.description != null) builder.setDescription(this.description);
				else builder.setDescription(new String(GEN_DESC + ATTR_COUNTER));
				if (this.type != null) builder.setType(this.type);
				else {
					Integer pickedType = random.nextInt(types.size()-1);
					builder.setType(types.get(pickedType));
				}
				if(this.comm != null) builder.setComm(this.comm);
				else{
					Double shot = random.nextDouble();
					if (this.commParam[0] > shot) {
						builder.setComm(COMM_IN);
						this.comm = COMM_IN;
					}
					else if (this.commParam[0] + this.commParam[1] > shot) {
						builder.setComm(COMM_OUT);
						this.comm = COMM_OUT;
					}
					else if (this.commParam[0] + this.commParam[1] + this.commParam[2]> shot) {
						builder.setComm(COMM_INTER);
						this.comm = COMM_INTER;
					}
					else if (this.commParam[0] + this.commParam[1] + this.commParam[2] + this.commParam[3]> shot) {
						builder.setComm(COMM_COMM);
						this.comm = COMM_COMM;
					}
					else {
						builder.setComm(COMM_UNKNOWN);
						this.comm = COMM_UNKNOWN;
					}
				}
				if(this.xttClass != null) builder.setXTTClass(this.xttClass);
				else{
					Double shot = random.nextDouble();
					if (this.xttClassParam[0] > shot) {
						builder.setXTTClass(CLASS_SIMPLE);
						this.xttClass = CLASS_SIMPLE;
					}
					else if (this.xttClassParam[0] + this.xttClassParam[1] > shot) {
						builder.setXTTClass(CLASS_GENERAL);
						this.xttClass = CLASS_GENERAL;
					}
					else {
						builder.setXTTClass(CLASS_UNKNOWN);
						this.xttClass = CLASS_UNKNOWN;
					}
				}
				attribute = builder.build();
			}
//			if(this.expirationTime != null) builder.setExpirationTime(this.expirationTime);
//			else{
//				Long pickedExpTime = random.nextLong();
//				builder.setExpirationTime(pickedExpTime);
//			}
		}
		else{
			
		}
		AttributeConfigurator.ATTR_COUNTER++;
		this.attributes.add(attribute);
		return attribute;
	}
	
}
