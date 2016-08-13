package cz.cuni.mff.d3s.trupple.language.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ICustomType {
	
	String getIdentifier();
	boolean containsCustomValue(String identifier);
	boolean isGlobal();
	long getCustomValue(String identifier);
	
	
	public abstract class CustomType implements ICustomType{
		
		private static int id = 0;
		private String identifier;
		private boolean global;
		
		public CustomType(String identifier, boolean global){
			this.identifier = identifier;
			this.global = global;
		}
		
		public static int getNextId(){
			return id++;
		}
		
		@Override
		public String getIdentifier(){
			return identifier;
		}
		
		@Override
		public boolean isGlobal(){
			return global;
		}
	}
	
	public class EnumType extends CustomType {
		
		private Map<String, Integer> values;
		
		public EnumType(String identifier, List<String> identifiers, boolean global){
			super(identifier, global);
			
			this.values = new HashMap<>();
			for(String value : identifiers){
				values.put(value, CustomType.getNextId());
			}
		}
		
		@Override
		public boolean containsCustomValue(String identifier){
			return values.containsKey(identifier);
		}
		
		@Override
		public long getCustomValue(String identifier){
			return values.get(identifier);
		}
	}
}
