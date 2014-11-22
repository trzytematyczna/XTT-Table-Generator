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


package heart;

import heart.alsvfd.Null;
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.xtt.Attribute;
import heart.xtt.Type;
import heart.xtt.XTTModel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * 
 * @author sbk
 * 
 * This is the class that is responsible for state management of the system
 * Every attributes values changes has to be done with this class methods.
 * It also gives access to the current and past system states.
 *
 */
public class WorkingMemory {
	
	/**
	 * The list of attributes names and real attributes and their values that are matched against the names.
	 * In usual case this will be redundant, but allows faster search thanks to HashMap.
	 */
	private HashMap<String, AVEntry> registeredAttributes; 
	
	/**
	 * The list of states of the system. The resolution of how often the system state is logged is set
	 * by the flag.
	 */
	private LinkedList<State> historyLog;
	
	/**
	 * The list of snapshots of the system state. This is different from the {@link #historyLog} as it allows custom names,
	 * and can be invoked by the user on demand.
	 */
	private HashMap<String,State> snapshots;
	
	/**
	 * Default constructor for the working memory object.
	 */
	public WorkingMemory() {
		registeredAttributes = new HashMap<String, AVEntry>();
		historyLog = new LinkedList<State>();
		snapshots = new HashMap<String, State>();
	}
	
	/**
	 * The method sets values of the attributes defined by state parameter.
	 * Depending on the autoregister parameter, the system registers all the attributes
	 * from the model.
	 * 
	 * @param state a state to be set
	 * @param model the model from which the attributes values re to be set
	 * @param autoregister the parameter indicating if the method should register all the attributes from the model (if true) or none (if false).
	 * @throws NotInTheDomainException 
	 * @throws AttributeNotRegisteredException 
	 */
	public void setCurrentState(State state, XTTModel model, boolean autoregister) throws NotInTheDomainException, AttributeNotRegisteredException{
		for(StateElement se : state){
			Attribute attr = model.getAttributeByName(se.getAttributeName());
			setAttributeValue(attr, se.getValue(), autoregister);
		}
	}
	
	/**
	 * The method returns values of all registered attributes in a form of {@link State} object.
	 * 
	 * @return the state that contains values of all registered attributes.
	 * 
	 */
	public State getCurrentState(){
		State current = new State();
		for(Entry<String, AVEntry> se : registeredAttributes.entrySet()){
			current.addStateElement(new StateElement(se.getKey(),se.getValue().getValue()));
		}
		return current;
	}
	
	/**
	 * Returns the state of the system only for the registered attributes within the model
	 * 
	 * @param model a model which attributes should be returned as a state
	 * @return the current state of the attributes values from the model given as a parameter
	 */
	public State getCurrentState(XTTModel model){
		State current = new State();
		for(Attribute a: model.getAttributes()){
			Value  v = getAttributeValue(a);
			current.addStateElement(new StateElement(a.getName(),v));
		}
		
		return current;
	}
	
	/**
	 * It registers the attribute that later will be accessible and will be included in snapshots and current state.
	 * Registering and unregistering is based on attributes names, not object refferences.
	 * 
	 * @param attribute an attribute to be registered
	 */
	public void registerAttribute(Attribute attribute){
		try {
			registeredAttributes.put(attribute.getName(), new AVEntry(attribute, new Null()));
		} catch (NotInTheDomainException e) {
			// This will not happend, as the Null ca always be assigned as an attribute value
		}
	}
	
	/**
	 * It registers the attribute that later will be accessible and will be included in snapshots and current state.
	 * Registering and unregistering is based on attributes names, not object refferences.
	 * 
	 * @param name a name of the attribute to register
	 * @param model a model where the attribute is located
	 */
	public void registerAttribute(String name, XTTModel model){
		Attribute attr = model.getAttributeByName(name);
		registerAttribute(attr);
	}
	
	/**
	 * It registers all attributes from a given model
	 * Registering and unregistering is based on attributes names, not object refferences.
	 * 
	 * @param model a model from which the attributes have to be regisered
	 */
	public void registerAllAttributes(XTTModel model){
		for(Attribute a: model.getAttributes()){
			registerAttribute(a);
		}
	}
	
	/**
	 * It unregisters the attribute of a given name. 
	 * If such attribute is no present in the register, nothing happens. 
	 * Registering and unregistering is based on attributes names, not object refferences.
	 * 
	 * @param name a name of the attribute to unregister
	 */
	public void unregisterAttribute(String name){
		registeredAttributes.remove(name);
	}
	
	/**
	 * The method unregisters all the attribute from a model given as a parameter.
	 * Registering and unregistering is based on attributes names, not object refferences.
	 * 
	 * @param model a model with attributes to unregister
	 */
	public void unregisterAll(XTTModel model){
		for(Attribute a: model.getAttributes()){
			unregisterAttribute(a.getName());
		}
	}

	/**
	 * The method return a value of an attribute of a given name.
	 * If the attribute is not registered, {@link Null} is returned.
	 * 
	 * @param attributeName the attribute name which value is needed
	 * @return the value of the attribute or {@link Null} id the attribute is not registered
	 */
	public Value getAttributeValue(String attributeName){
		AVEntry ave = registeredAttributes.get(attributeName);
		if(ave != null){
			return ave.getValue();
		}else{
			return new Null();
		}
	}
	
	/**
	 * The method return a value of an attribute of a given name.
	 * If the attribute is not registered, {@link Null} is returned.
	 * 
	 * @param attr the attribute which value is needed
	 * @return the value of the attribute or Null if it is not registered
	 */
	public Value getAttributeValue(Attribute attr){
		AVEntry ave = registeredAttributes.get(attr.getName());
		if(ave != null){
			return ave.getValue();
		}else{
			return new Null();
		}
	}
	
	/**
	 * The method sets a given value to the attribute which name is passed as a parameter.
	 * It the attribute is not present in the WorkingMemmory registry, the {@link AttributeNotRegisteredException} is thrown.
	 * 
	 * @param attributeName name of the attribute which value has to be set
	 * @param value the value to set
	 * @throws AttributeNotRegisteredException
	 * @throws NotInTheDomainException 
	 */
	public void setAttributeValue(String attributeName, Value value) throws AttributeNotRegisteredException, NotInTheDomainException{
		AVEntry ave = registeredAttributes.get(attributeName);
		if(ave != null){
			ave.setAttributeValue(value);
		}else{
			throw new AttributeNotRegisteredException("Attribute "+attributeName+" was not registered.", attributeName);
		}
	}
	
	/**
	 * The method sets a given value to the attribute which name is passed as a parameter.
	 * It the attribute is not present in the WorkingMemmory registry, the {@link AttributeNotRegisteredException} is thrown.
	 * 
	 * @param attributeName name of the attribute which value has to be set
	 * @param value the value to set
	 * @throws AttributeNotRegisteredException
	 * @throws NotInTheDomainException 
	 */
	
	/**
	 * The method sets a given value to the attribute which name is passed as a parameter.
	 * It the attribute is not present in the WorkingMemmory registry, the {@link AttributeNotRegisteredException} is thrown.
	 * 
	 * @param attributeName a name of the attribute
	 * @param model a model in which the attribute is present
	 * @param value a value to be set
	 * @param autoregister true or false boolean indicating weather register the attribute in workingMemory or not.
	 * 
	 * @throws AttributeNotRegisteredException
	 * @throws NotInTheDomainException
	 */
	public void setAttributeValue(String attributeName, XTTModel model, Value value, boolean autoregister) throws AttributeNotRegisteredException, NotInTheDomainException{
		AVEntry ave = null;
		if(autoregister) registerAttribute(attributeName, model);
		
		ave = registeredAttributes.get(attributeName);
 
		if(ave != null){
			ave.setAttributeValue(value);
		}else{
			throw new AttributeNotRegisteredException("Attribute "+attributeName+" was not registered.", attributeName);
		}
	}
	
	/**
	 * The method sets a given value to the attribute which  is passed as a parameter.
	 * It the attribute is not present in the WorkingMemmory registry, the {@link AttributeNotRegisteredException} is thrown in case
	 * when autoregister is set to false. In case when autoregister is set to true, the Attribute will be automatically 
	 * registered in Working Memory registry.
	 * 
	 * @param attribute the attribute which value has to be set
	 * @param value the value to set
	 * @param autoregister the parameter indicating if the method should register the attribute (if true) or none (if false).
	 * @throws AttributeNotRegisteredException
	 * @throws NotInTheDomainException 
	 */
	public void setAttributeValue(Attribute attribute, Value value, boolean autoregister) throws AttributeNotRegisteredException, NotInTheDomainException{
		if(autoregister) registerAttribute(attribute);
		AVEntry ave = registeredAttributes.get(attribute.getName());
		ave.setAttributeValue(value);
	}
	
	/**
	 * The method makes a snapshot of a current system state, by saving all the values of the registered attributes.
	 * 
	 * @param snapshotName the name of the snapshot.
	 */
	public void makeSnapshot(String snapshotName){
		State snapshot = getCurrentState();
		snapshot.setName(snapshotName);
		//TODO: what if the snapshot of a given name exists?
		snapshots.put(snapshotName, snapshot);
	}
	
	/**
	 * It returns the state that represents the snapshot of a given name.
	 * 
	 * @param snapshotName a name of the snapshot to return
	 * @return a state representing the snapshot or null if the snapshot of a given name is not present
	 */
	public State getSnapshot(String snapshotName){
		return snapshots.get(snapshotName);
	}
	
	/**
	 * It saves the state of the system at the time of the method is being call
	 * to the history log. It differs from the {@link #makeSnapshot(String)} method
	 * as the logs are more anonymous, as they cannot have custom names, and what is more
	 * logs may be recorded automatically be the logging mechanism, whereas snapshots 
	 * can only be made by the programmer explicitly with the {@link #makeSnapshot(String)} call.
	 */
	public void recordLog(){
		State s = getCurrentState();
		s.setName("log_"+s.getTimestamp());
		historyLog.add(s);
	}

	/**
	 * It returns the entire history log with all the records made.
	 * 
	 * @return the list of all States made with the {@link #recordLog()} method.
	 */
	public LinkedList<State> getHistoryLog() {
		return historyLog;
	}


	/**
	 * The method returns list of all snapshots made with the {@link #makeSnapshot(String)}
	 * method.
	 * 
	 * @return the list of all the snapshots made.
	 */
	public LinkedList<State> getSnapshots() {
		return new LinkedList<State>(snapshots.values());
	}


	protected class AVEntry{
		/**
		 * The attribute which value is stored within the entry
		 */
		private Attribute attr;
		
		/**
		 * current value of the Attribute. This is where the current state is stored
		 */
		private Value value;
		
		public AVEntry(Attribute attr, Value value) throws NotInTheDomainException{
			this.attr = attr;
			setAttributeValue(value);
		}
		
		protected Attribute getAttribute(){
			return attr;
		}
		
		protected Value getValue(){
			return value;
		}
		
		/**
		 * A method that sets a value of the attribute for a value given as a parameter.
		 * The method checks if the value that is to be assigned is in a domain of the 
		 * attribute type. If not, the {@link NotInTheDomainException} is thrown.
		 * 
		 * Casting between {@link SimpleSymbolic} and {@link SimpleNumeric} is allowed if
		 * SimpleSimbolic type is ordered. In every other case, the {@link NotInTheDomainException}
		 * is thrown.
		 * 
		 * The only exception is {@link Null} value, that despite not being in any domain
		 * can be assigned to the attribute value.
		 * 
		 * @param value to set
		 * @throws NotInTheDomainException
		 */
		protected void setAttributeValue(Value value) throws NotInTheDomainException {
			if(value instanceof Null){
				this.value = value;
			}else if(value.isInTheDomain(attr.getType())){
				if(attr.getXTTClass().equals(Attribute.CLASS_SIMPLE)){
					if(value instanceof SimpleNumeric && attr.getType().getBase().endsWith(Type.BASE_SYMBOLIC)){ 
						// Add casting between symbolic and numeric if ordered
						this.value = SimpleSymbolic.findInTheDomain((SimpleNumeric) value, attr.getType());
						if(this.value == null){
							throw new NotInTheDomainException(attr.getType().getDomain(), value, 
									"Setting value of attribute '"+attr.getName()+
									"' for a value '"+value+"' that is not in the domain that is not 'Null'.");
						}
					}else if(value instanceof SimpleSymbolic && attr.getType().getBase().endsWith(Type.BASE_NUMERIC)){
						this.value = SimpleSymbolic.findInTheDomain((SimpleNumeric) value, attr.getType());
						if(this.value == null){
							throw new NotInTheDomainException(attr.getType().getDomain(), value, 
									"Setting value of attribute '"+attr.getName()+
									"' for a value '"+value+"' that is not in the domain that is not 'Null'.");
						}
					}else{
						this.value = value;
					}
				}else if(attr.getXTTClass().equals(Attribute.CLASS_GENERAL)){
					this.value = value;
				}
			}else{
				throw new NotInTheDomainException(attr.getType().getDomain(), value, 
						"Setting value of attribute '"+attr.getName()+
						"' for a value '"+value+"' that is not in the domain that is not 'Null'.");
			}
		}
	}
	
	
	
	
	
	
}
