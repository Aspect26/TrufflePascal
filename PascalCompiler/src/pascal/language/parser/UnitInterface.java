package pascal.language.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pascal.language.runtime.PascalContext;
import pascal.language.runtime.PascalFunctionRegistry;

public class UnitInterface {
	public final String name;
	
	private final PascalContext context;
	private final Map<String, List<VariableDeclaration>> interfaceProcedures;
	private final Map<String, FunctionFormalParameters> interfaceFunctions;
	
	public UnitInterface(String name){
		this.name = name;
		this.context = new PascalContext();
		this.interfaceProcedures = new HashMap<>();
		this.interfaceFunctions = new HashMap<>();
	}
	
	public boolean addProcedureInterface(String name, List<VariableDeclaration> parameters){
		if(subroutineExists(name))
			return false;
		
		interfaceProcedures.put(name, parameters);
		return true;
	}
	
	public boolean addFunctionInterface(String name, List<VariableDeclaration> parameters, String returnType){
		if(subroutineExists(name))
			return false;
		
		interfaceFunctions.put(name, new FunctionFormalParameters(parameters, returnType));
		return true;
	}
	
	private boolean subroutineExists(String name){
		return interfaceProcedures.containsKey(name) || interfaceFunctions.containsKey(name);
	}
	
	public boolean subroutineImplementationExists(String name){
		return context.getFunctionRegistry().lookup(name) != null;
	}
	
	public boolean checkProcedureMatch(String name, List<VariableDeclaration> params){
		if(!interfaceProcedures.containsKey(name))
			return false;
		
		return compareFormalParametersList(interfaceProcedures.get(name), params);
	}
	
	public boolean checkFunctionMatch(String name, List<VariableDeclaration> params, String returnType){
		if(!interfaceFunctions.containsKey(name))
			return false;
		
		if(!compareFormalParametersList(interfaceFunctions.get(name).formalParameters, params))
			return false;
		
		return interfaceFunctions.get(name).typeName.equals(returnType);
	}
	
	private boolean compareFormalParametersList(List<VariableDeclaration> left, List<VariableDeclaration> right){
		if(left.size() != right.size())
			return false;
		
		for(int i=0; i<left.size(); i++){
			if(!left.get(i).identifier.equals(right.get(i).identifier))
				return false;
			
			if(!left.get(i).type.equals(right.get(i).type))
				return false;
		}
		
		return true;
	}
	
	public PascalFunctionRegistry getFunctionRegistry(){
		return context.getFunctionRegistry();
	}
	
	public PascalContext getCotnext(){
		return context;
	}
}

class FunctionFormalParameters{
	public FunctionFormalParameters(List<VariableDeclaration> formalParameters, String typeName){
		this.formalParameters = formalParameters;
		this.typeName = typeName;
	}
	
	public List<VariableDeclaration> formalParameters;
	public String typeName;
}
