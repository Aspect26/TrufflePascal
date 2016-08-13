package cz.cuni.mff.d3s.trupple.language.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ICustomType {
	
	String getIdentifier();
	
	
	public abstract class CustomType implements ICustomType{
		
		private static int id = 0;
		private String identifier;
		
		public CustomType(String identifier){
			this.identifier = identifier;
		}
		
		public static int getNextId(){
			return id++;
		}
		
		public String getIdentifier(){
			return identifier;
		}
	}
	
	public class EnumType extends CustomType {
		
		private Map<String, Integer> values;
		
		public EnumType(String identifier, List<String> identifiers){
			super(identifier);
			
			this.values = new HashMap<>();
			for(String value : identifiers){
				values.put(value, CustomType.getNextId());
			}
		}
	}
}
