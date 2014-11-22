
import heart.Configuration;
import heart.HeaRT;
import heart.State;
import heart.StateElement;
import heart.alsvfd.Formulae;
import heart.alsvfd.SimpleNumeric;
import heart.alsvfd.SimpleSymbolic;
import heart.alsvfd.Value;
import heart.alsvfd.expressions.ExpressionInterface;
import heart.exceptions.AttributeNotRegisteredException;
import heart.exceptions.BuilderException;
import heart.exceptions.ModelBuildingException;
import heart.exceptions.NotInTheDomainException;
import heart.exceptions.ParsingSyntaxException;
import heart.exceptions.RangeFormatException;
import heart.parser.hmr.HMRParser;
import heart.parser.hmr.runtime.SourceFile;
import heart.xtt.Attribute;
import heart.xtt.Decision;
import heart.xtt.Rule;
import heart.xtt.Table;
import heart.xtt.Type;
import heart.xtt.XTTModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

public class HMRParsingDemo {
    public static void main(String [] args){
        try{
            SourceFile hmr_threat_monitor = new SourceFile("./assets/threat-monitor.pl");
            HMRParser parser = new HMRParser();
            parser.parse(hmr_threat_monitor);
            XTTModel model = parser.getModel();
            LinkedList<Type> types = model.getTypes();
            for(Type t : types){
                System.out.println("Type id: "+t.getId());
                System.out.println("Type name: "+t.getName());
                System.out.println("Type base: "+t.getBase());
                System.out.println("Type length: "+t.getLength());
                System.out.println("desc: "+t.getDescription());
                
                for(Value v: t.getDomain().getValues()){
                    System.out.println("Value: "+v);
                }
                System.out.println("==========================");
            }
            
            LinkedList<Attribute> atts = model.getAttributes();
            for(Attribute att: atts){
                System.out.println("Att Id: "+att.getId());
                System.out.println("Att name: "+att.getName());
                System.out.println("Att typeName: "+att.getType().getName());
                System.out.println("Att abbrev: "+att.getAbbreviation());
                System.out.println("Att comm: "+att.getComm());
                System.out.println("Att desc: "+att.getDescription());
                System.out.println("Att class: "+att.getXTTClass());
                System.out.println("==========================");
            }
            
            LinkedList<Table> tables = model.getTables();
            for(Table t : tables){
                System.out.println("Table id:"+t.getId());
                System.out.println("Table name:"+t.getName());
                LinkedList<heart.xtt.Attribute> cond = t.getPrecondition();
                for(heart.xtt.Attribute a : cond){
                    System.out.println("schm Cond: "+a.getName());
                }
                LinkedList<heart.xtt.Attribute> concl = t.getConclusion();
                for(heart.xtt.Attribute a : concl){
                    System.out.println("schm Conclusion: "+a.getName());
                }
                System.out.println("RULES FOR TABLE "+t.getName());
                
                for(Rule r : t.getRules()){
                    System.out.print("Rule id: "+r.getId()+ ":\n\tIF ");
                    for(Formulae f : r.getConditions()){
                        System.out.print(f.getAttribute().getName()+" "+f.getOp()+" "+f.getValue()+", ");
                    }
                    
                    System.out.println("THEN ");
                    
                    for(Decision d: r.getDecisions()){
                        System.out.print("\t"+d.getAttribute().getName()+"is set to ");

                        ExpressionInterface e = d.getDecision();
                        System.out.print(e);
                    }
                    System.out.println();
                    
                }
                System.out.println();
                System.out.println("=============================");
            }
            
            StateElement hourE = new StateElement();
            StateElement dayE = new StateElement();
            StateElement locationE = new StateElement();
            StateElement activityE = new StateElement();

            hourE.setAttributeName("hour");
            hourE.setValue(new SimpleNumeric(16d));

            dayE.setAttributeName("day");
            dayE.setValue(new SimpleSymbolic("mon",1));

            locationE.setAttributeName("location");
            locationE.setValue(new SimpleSymbolic("work"));

            activityE.setAttributeName("activity");
            activityE.setValue(new SimpleSymbolic("walking"));


            State XTTstate = new State();
            XTTstate.addStateElement(hourE);
            XTTstate.addStateElement(dayE);
            XTTstate.addStateElement(locationE);
            XTTstate.addStateElement(activityE);


            System.out.println("Printing current state");
            State current = HeaRT.getWm().getCurrentState(model);
            for(StateElement se : current){
                System.out.println("Attribute "+se.getAttributeName()+" = "+se.getValue());
            }

            try{
                HeaRT.fixedOrderInference(model, new String[]{"DayTime","Today","Actions","Threats"},
                        new Configuration.Builder()
                                .setInitialState(XTTstate)
                                .build());

            }catch(UnsupportedOperationException e){
                e.printStackTrace();
            } catch (AttributeNotRegisteredException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NotInTheDomainException ex) {
                Logger.getLogger(HMRParsingDemo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BuilderException ex) {
                Logger.getLogger(HMRParsingDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            System.out.println("Printing current state (after inference");
            current = HeaRT.getWm().getCurrentState(model);
            for(StateElement se : current){
                System.out.println("Attribute "+se.getAttributeName()+" = "+se.getValue());
            }
        }catch(ModelBuildingException | ParsingSyntaxException ex){
            Logger.getLogger(HMRParsingDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }	
}
