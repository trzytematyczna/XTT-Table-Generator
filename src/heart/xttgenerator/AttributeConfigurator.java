package heart.xttgenerator;

import heart.xtt.Attribute;
import heart.xtt.Type;

import java.util.LinkedList;
import java.util.Random;

import org.w3c.dom.Attr;


public class AttributeConfigurator {

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
	private String[] commParam;
	/**
	 * A field that denotes weather the attribute is simple (represents simple values) 
	 * or general (represents set values).
	 */
	private String XTTClass;
	private String[] XTTClassParam;
	
	private String description;
	private String abbreviation; 
	
	/**
	 * A variable that contains a name of a Callback class
	 * that should be triggered with a reflection mechanism
	 * depending on the {@link #comm} value.
	 */
	private String callback;
	
	/**
	 * Time in milliseconds indicating a period after which the value of the attribute 
	 * cannot be considered valid anymore.
	 */
	private Long expirationTime;
	private Long[] expirationTimeParam;
	/**
	 * A field that contains a reference to a type defined as xtype in HMR. 
	 * This field denotes the attribute's type.
	 */
	private Type type;
	private Type[] typeParam;	
	
	
	public boolean validateConfiguration(){
		if(this.comm == null && this.commParam == null) return false;
		if(this.comm != null && !(this.comm.equals(this.COMM_IN)|| this.comm.equals(this.COMM_OUT)|| this.comm.equals(this.COMM_INTER)||this.comm.equals(this.COMM_COMM) || this.comm.equals(this.COMM_UNKNOWN))) return false;
		if(this.XTTClass == null && this.XTTClassParam == null) return false;
		if(this.XTTClass != null && !(this.XTTClass.equals(this.CLASS_SIMPLE)||this.XTTClass.equals(this.CLASS_GENERAL)||this.XTTClass.equals(this.CLASS_UNKNOWN))) return false;
		if(this.expirationTime != null && this.expirationTimeParam == null) return false;
		if(this.type == null && this.typeParam == null) return false;
		return true;
	}
	
	public Attribute generateAttribute(Random random) throws Exception{
		Attribute attribute = null;
				
		return attribute;
	}
	
}
