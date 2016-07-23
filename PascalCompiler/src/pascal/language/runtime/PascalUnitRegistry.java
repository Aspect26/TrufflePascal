package pascal.language.runtime;

import java.util.ArrayList;
import java.util.List;

public class PascalUnitRegistry {
	List<String> registeredUnits = new ArrayList<>();
	
	public void addUnit(String unitName){
		this.registeredUnits.add(unitName);
	}
	
	public boolean hasUnit(String unitName){
		return this.registeredUnits.contains(unitName);
	}
}
