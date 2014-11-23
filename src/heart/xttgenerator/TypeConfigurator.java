package heart.xttgenerator;

public class TypeConfigurator {
/*
 * Type = "xtype" "[" "name" ":" String ","
                   "base" ":" ("numeric" / "symbolic") ","
		           "domain" ":" List ","
		           "desc" ":" String ","
                   "ordered" ":" Boolean  
                   "step" ":" Integer "]" "."
 */
	private String base;
	private String domain;
	private String desc;
	private boolean ordered;
	private int step;
	
	public TypeConfigurator() {
			
		
	}
}
