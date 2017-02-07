package cz.cuni.mff.d3s.trupple.language.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.api.frame.FrameSlot;

import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

public class Unit {
	public final String name;
	
	private boolean inInterfaceSection;
	private LexicalScope lexicalScope;
	private final Map<String, List<FormalParameter>> interfaceProcedures;
	private final Map<String, FunctionFormalParameters> interfaceFunctions;

	public Unit(String name) {
		this.name = name;
		this.inInterfaceSection = true;
		this.interfaceProcedures = new HashMap<>();
		this.interfaceFunctions = new HashMap<>();
		this.lexicalScope = new LexicalScope(null, name);
	}
	
	public PascalContext getContext(){
		return lexicalScope.getContext();
	}
	
	public boolean isInInterfaceSection(){
		return inInterfaceSection;
	}
	
	public void leaveInterfaceSection(){
		inInterfaceSection = false;
	}

	public boolean addProcedureInterface(String name, List<FormalParameter> parameters) {
		if (subroutineExists(name))
			return false;

		interfaceProcedures.put(name, parameters);
		return true;
	}

	public boolean addFunctionInterface(String name, List<FormalParameter> parameters, String returnType) {
		if (subroutineExists(name))
			return false;

		interfaceFunctions.put(name, new FunctionFormalParameters(parameters, returnType));
		return true;
	}

	private boolean subroutineExists(String name) {
		return interfaceProcedures.containsKey(name) || interfaceFunctions.containsKey(name);
	}

	public LexicalScope startSubroutineImplementation(String identifier) {
		if(this.interfaceFunctions.containsKey(identifier) || this.interfaceProcedures.containsKey(identifier))
			lexicalScope.getContext().getGlobalFunctionRegistry().registerSubroutineName(identifier);
		else
			lexicalScope.getContext().getPrivateFunctionRegistry().registerSubroutineName(identifier);
			
		lexicalScope = new LexicalScope(lexicalScope, identifier);
		return lexicalScope;
	}

	public boolean checkProcedureMatchInterface(String name, List<FormalParameter> params) {
		if (!interfaceProcedures.containsKey(name))
			return true;

		return compareFormalParametersList(interfaceProcedures.get(name), params);
	}

	public boolean checkFunctionMatchInterface(String name, List<FormalParameter> params, String returnType) {
		if (!interfaceFunctions.containsKey(name))
			return true;

		if (!compareFormalParametersList(interfaceFunctions.get(name).formalParameters, params))
			return false;

		return interfaceFunctions.get(name).typeName.equals(returnType);
	}

	private boolean compareFormalParametersList(List<FormalParameter> left, List<FormalParameter> right) {
		if (left.size() != right.size())
			return false;

		for (int i = 0; i < left.size(); i++) {
			if (!left.get(i).identifier.equals(right.get(i).identifier))
				return false;

			if (!left.get(i).type.equals(right.get(i).type))
				return false;
		}

		return true;
	}
	
	public void implementSubroutine(PascalRootNode rootNode){
		String identifier = lexicalScope.getName();
		leaveLexicalScope();
		
		if(interfaceFunctions.containsKey(identifier))
			lexicalScope.getContext().getGlobalFunctionRegistry().setFunctionRootNode(identifier, rootNode);
		else
			lexicalScope.getContext().getPrivateFunctionRegistry().setFunctionRootNode(identifier, rootNode);
	}
	
	private void leaveLexicalScope() {
		this.lexicalScope = lexicalScope.getOuterScope();
	}

	public FrameSlot getSlot(String identifier) {
		return lexicalScope.getLocalSlot(identifier);
	}

	public LexicalScope getLexicalScope() {
		return lexicalScope;
	}
}

class FunctionFormalParameters {
	public FunctionFormalParameters(List<FormalParameter> formalParameters, String typeName) {
		this.formalParameters = formalParameters;
		this.typeName = typeName;
	}

	public List<FormalParameter> formalParameters;
	public String typeName;
}
