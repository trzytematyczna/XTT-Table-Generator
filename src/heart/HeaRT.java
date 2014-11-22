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


import java.util.LinkedList;
import java.util.AbstractMap.SimpleEntry;

import heart.Debug.Level;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.NotInTheDomainException;
import heart.uncertainty.ConflictSet;
import heart.uncertainty.UncertainTrue;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.XTTModel;

public class HeaRT {
	
	private static final WorkingMemory wm = new WorkingMemory();
	
	public static void fixedOrderInference(XTTModel model, String[] tablesNames) throws UnsupportedOperationException, NotInTheDomainException, AttributeNotRegisteredException{
		fixedOrderInference(model, tablesNames, new Configuration.Builder().getDefaultConfiguration());
	}
	
	public static void fixedOrderInference(XTTModel model, String[] tablesNames, Configuration cs) throws UnsupportedOperationException, NotInTheDomainException, AttributeNotRegisteredException{
		try{
			wm.setCurrentState(cs.getInitialState(), model, true);
			for(String tn : tablesNames){
				for(Table table : model.getTables()){
					if(tn.equals(table.getName())){
						Debug.debug(Debug.heartTag, Level.TABLES,"Processing table "+table.getName()+" (ID: "+table.getId()+")");
						runRules(table,cs);
						Debug.debug(Debug.heartTag, Level.TABLES,"Processing table "+table.getName()+" (ID: "+table.getId()+") finished.");
					}
				}
			}
		}catch(NotInTheDomainException e){
			//TODO: add more info and rethrow
			throw e;
		} catch (AttributeNotRegisteredException e) {
			// TODO Auto-generated catch block
			throw e;
		}
		
	}
	
	public static void dataDrivenInference(XTTModel model, String[] tablesNames){
		dataDrivenInference(model, tablesNames, new Configuration.Builder().getDefaultConfiguration());
	}
	
	public static void dataDrivenInference(XTTModel model, String[] tablesNames, Configuration cs){
		
	}
	
	public static void goalDrivenInference(XTTModel model, String[] tablesNames){
		goalDrivenInference(model, tablesNames,  new Configuration.Builder().getDefaultConfiguration());
	}
	
	public static void goalDrivenInference(XTTModel model, String[] tablesNames,  Configuration cs){
		
	}
	
	public static void tokeDrivenInference(XTTModel model, String[] tablesNames){
		tokeDrivenInference(model, tablesNames ,new Configuration.Builder().getDefaultConfiguration());
	}
	
	public static void tokeDrivenInference(XTTModel model, String[] tablesNames,  Configuration  cs){
		
	}
	

	private static void runRules(Table table, Configuration cs) throws UnsupportedOperationException, NotInTheDomainException{
		ConflictSet conflictSet = new ConflictSet(); 
		try{
			UncertainTrue finalResult = new UncertainTrue(cs.getUncertainTrueEvaluator().getMinCertainty());
			Rule ruleToFire = null;
			for(Rule rule : table.getRules()){
				Debug.debug(Debug.heartTag, Level.RULES, "Processing rule "+rule.getName()+" (ID: "+rule.getId()+")");
				UncertainTrue partialResult = rule.evaluate(wm,cs.getUncertainTrueEvaluator());
				partialResult.setCertinatyFactor(partialResult.getCertinatyFactor()*rule.getCertaintyFactor());
				//TODO: add satisability threshold, or think how to do this
				if(finalResult.getCertinatyFactor() < partialResult.getCertinatyFactor()){
					finalResult = partialResult;
					ruleToFire = rule;
					conflictSet.clear();
				}else if(finalResult.getCertinatyFactor() == partialResult.getCertinatyFactor() &&
						//TODO: temporarly fix, in the future add satisability threshold, or think how to do this
						finalResult.getCertinatyFactor() > cs.getUncertainTrueEvaluator().getMinCertainty()){
					conflictSet.add(ruleToFire, finalResult);
					conflictSet.add(rule, partialResult);
				}
				Debug.debug(Debug.heartTag, Level.RULES, "Finished evaluating rule "+rule.getName()+" (ID: "+rule.getId()+"). "+
				"SATISFIED with ("+partialResult.getCertinatyFactor()+") certainty.");
				
				
			}
			
			//If the conflict set is empty, then fire the ruleToFire
			//Otherwise, launch conflict resolution mechanism
			if(conflictSet.isEmpty()){
				if(ruleToFire != null){
					if(ruleToFire.execute(wm,finalResult))
						Debug.debug(Debug.heartTag, Level.RULES, 
								"Rule "+ruleToFire.getName()+" (ID: "+ruleToFire.getId()+") fired.");
					else
						Debug.debug(Debug.heartTag, Level.RULES, 
								"Rule "+ruleToFire.getName()+" (ID: "+ruleToFire.getId()+") execution failed.");
				}else{
					Debug.debug(Debug.heartTag, Level.RULES, 
							"No rule to fire in table "+table.getName()+" (ID: "+table.getId()+").");
				}
				
			}else{
				Debug.debug(Debug.heartTag, Level.RULES, 
						"Conflict set of table "+table.getName()+" (ID: "+table.getId()+
						") is not empty (contains "+conflictSet.size()+" rules).");
				
				LinkedList<SimpleEntry<Rule, UncertainTrue>> toExecute = cs.getConflictSetResolution().resolveConflictSet(conflictSet);
				for(SimpleEntry<Rule, UncertainTrue> se: toExecute){
					Rule r = se.getKey();
					r.execute(wm, se.getValue());
				}
				
			}
			
		}catch(UnsupportedOperationException e){
			//TODO rethrow with modified message
			throw e;
		} catch (NotInTheDomainException e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}

	public static WorkingMemory getWm() {
		return wm;
	}
	
	

	
}
