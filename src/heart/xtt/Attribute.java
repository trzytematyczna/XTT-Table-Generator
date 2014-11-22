/**
*
*     Copyright 2013-15 by Szymon Bobek, Grzegorz J. Nalepa, Mateusz Ślażyński
*
*
*     This file is part of HeaRTDroid.
*     HeaRTDroid is a rule engine that is based on HeaRT inference engine,
*     XTT2 representation and other concepts developed within the HeKatE project .
*
*     HeaRTDroid is free software: you can redistribute it and/or modify
*     it under the terms of the GNU General Public License as published by
*     the Free Software Foundation, either version 3 of the License, or
*     (at your option) any later version.
*
*     HeaRTDroid is distributed in the hope that it will be useful,
*     but WITHOUT ANY WARRANTY; without even the implied warranty of
*     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*     GNU General Public License for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with HeaRTDroid.  If not, see <http://www.gnu.org/licenses/>.
*
**/


package heart.xtt;


import heart.WorkingMemory;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.alsvfd.Null;
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.exceptions.BuilderException;
import heart.exceptions.NotInTheDomainException;

/**
 * 
 * The XTT2 representation is based on the ALSV(FD) logic
 * which identifies two types of attributes: 
 * <ul>
 *    <li> Simple that can take only one value at any time.</li>
 *    <li> Generalized that can take multiple values at any time.</li>
 * </ul>
 * An attribute can be of any type defined as {@link Type}
 * For detailed information visit <a href=http://ai.ia.agh.edu.pl/wiki/hekate:xtt2>HeKatE documentation</a>.
 */
public class Attribute implements ExpressionInterface {
	/**
	 * This field is used by the SQL module while storing/restoring model into/from database.
	 * If not provided, this should be null; 
	 */
	private String id;
	
	/**
	 * This field contains attribute name. This should be the default identifier that
	 * attributes are referred.
	 */
	private String name;
	
	/**
	 * A field that describes attribute's relation to the world. 
	 * If the attribute is in it means that a value of it is supposed to be given by the user; 
	 * if the attribute is out it means that the value of it should be presented to the user; 
	 * if attribute is inter it means that its value is set by the system as a result of inference process, 
	 * but it's not relevant to the user; if the attribute is comm, it means that it's both in and out.
	 */
	private String comm;
	
	/**
	 * A field that denotes weather the attribute is simple (represents simple values) 
	 * or general (represents set values).
	 */
	private String XTTClass;
	
	/**
	 * A field that contains longer description of the attribute.
	 */
	private String description;
	
	/**
	 * A field that contains short name of the attribute. 
	 */
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
	private long expirationTime;

	
	/**
	 * A field that contains a reference to a type defined as xtype in HMR. 
	 * This field denotes the attribute's type.
	 */
	private Type type;
	
	
	public static final String CLASS_SIMPLE = "simple";
	public static final String CLASS_GENERAL ="general";
	public static final String CLASS_UNKNOWN ="unknown";
	

	public static final String COMM_IN ="in";
	public static final String COMM_OUT = "out";
	public static final String COMM_INTER = "inter";
	public static final String COMM_COMM = "comm";
	public static final String COMM_UNKNOWN ="unknown";
	
	/**
	 * A constructor that is used for SQL Mapping with ORM.
	 */
	Attribute(){}

	
	private Attribute(Builder builder) throws BuilderException, NotInTheDomainException{		
		this.setAbbreviation(builder.abbreviation);
		this.setCallback(builder.callback);
		this.setComm(builder.comm);
		this.setDescription(builder.description);
		this.setId(builder.id);
		this.setName(builder.name);
		this.setType(builder.type);
		this.setXTTClass(builder.XTTClass);			
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getComm() {
		return comm;
	}

	public String getTypeId() {
		return type.getId();
	}

	public String getDescription() {
		return description;
	}

	public String getAbbreviation() {
		return abbreviation;
	}
	
	public String getXTTClass(){
		return XTTClass;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setComm(String comm) {
		this.comm = comm;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	public void setXTTClass(String XTTClass){
		this.XTTClass = XTTClass;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}


	/**
	 * @return the expirationTime
	 */
	public long getExpirationTime() {
		return expirationTime;
	}

	/**
	 * @param expirationTime the expirationTime to set
	 */
	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	@Override
	public Value evaluate(WorkingMemory wm) throws UnsupportedOperationException, NotInTheDomainException {
		return wm.getAttributeValue(this); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static class Builder{
		private String id;
		private String name;
		private String comm = Attribute.COMM_UNKNOWN;
		private String XTTClass;
		private String description;
		private String abbreviation; 
		private String callback;

		private Type type;
                
//              For parsing purposes
                private String typeName;
                private String debugInfo;
                
		
		public Attribute build() throws BuilderException, NotInTheDomainException{
			if(name == null || XTTClass == null || type == null){
				throw new BuilderException("Error while building Attribute ("+name+"). Name, class, or type was not defined.");
			}else{
				return new Attribute(this);
			}
		}
		
		public Builder setId(String id) {
			this.id = id;
			return this;
		}
		
		public Builder setAbbreviation(String abbreviation) {
			this.abbreviation = abbreviation;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}
                
                public String getName() {
                        return this.name; //To change body of generated methods, choose Tools | Templates.
                }
                
                public String getAbbreviation() {
                    return this.abbreviation;
                }

		public Builder setComm(String comm) {
			this.comm = comm;
			return this;
		}
		
		

		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}
		
		public Builder setXTTClass(String XTTClass){
			this.XTTClass = XTTClass;
			return this;
		}


		public Builder setType(Type type) {
			this.type = type;
			return this;
		}
                
                public Builder setTypeName(String typeName) {
			this.typeName = typeName;
			return this;
		}
                
                public String getTypeName() {
                    return this.typeName;
                }
                
                public Builder setDebugInfo(String debugInfo) {
			this.debugInfo = debugInfo;
			return this;
		}
                
                public String getDebugInfo() {
                    return this.debugInfo;
                }

		public Builder setCallback(String callback) {
			this.callback = callback;
			return this;
		}

	}
	
	
	

	
}
