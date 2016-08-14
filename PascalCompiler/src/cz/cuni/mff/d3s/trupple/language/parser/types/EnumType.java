package cz.cuni.mff.d3s.trupple.language.parser.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnumType extends CustomType {
	
	private Map<String, Integer> values;
	private final int size;
	
	public EnumType(String identifier, List<String> identifiers, boolean global){
		super(identifier, global);
		
		int index = 0;
		size = identifiers.size();
		this.values = new HashMap<>();
		for(String value : identifiers){
			values.put(value, index++);
		}
	}
	
	public int getFirstIndex() {
		return 0;
	}
	
	public int getSize() {
		return this.size;
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