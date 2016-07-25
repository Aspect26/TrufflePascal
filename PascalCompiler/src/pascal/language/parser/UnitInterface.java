package pascal.language.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pascal.language.runtime.PascalContext;
import pascal.language.runtime.PascalFunctionRegistry;

public class UnitInterface {
	public final String name;
	
	private final PascalContext context;
	private final Map<String, List<FormalParameter>> interfaceProcedures;
	private final Map<String, FunctionFormalParameters> interfaceFunctions;
	
	public UnitInterface(String name){
		this.name = name;
		this.context = new PascalContext();
		this.interfaceProcedures = new HashMap<>();
		this.interfaceFunctions = new HashMap<>();
	}
	
	public boolean addProcedureInterface(String name, List<FormalParameter> parameters){
		if(subroutineExists(name))
			return false;
		
		interfaceProcedures.put(name, parameters);
		return true;
	}
	
	public boolean addFunctionInterface(String name, List<FormalParameter> parameters, String returnType){
		if(subroutineExists(name))
			return false;
		
		interfaceFunctions.put(name, new FunctionFormalParameters(parameters, returnType));
		return true;
	}
	
	private boolean subroutineExists(String name){
		return interfaceProcedures.containsKey(name) || interfaceFunctions.containsKey(name);
	}
	
	public PascalFunctionRegistry getFunctionRegistry(){
		return context.getFunctionRegistry();
	}
	
	public PascalContext getCotnext(){
		return context;
	}
}

class FunctionFormalParameters{
	public FunctionFormalParameters(List<FormalParameter> formalParameters, String typeName){
		this.formalParameters = formalParameters;
		this.typeName = typeName;
	}
	
	public List<FormalParameter> formalParameters;
	public String typeName;
}
