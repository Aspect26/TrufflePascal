
package pascal.language.parser;

import pascal.language.nodes.*;
import pascal.language.runtime.PascalContext;

import com.oracle.truffle.api.source.Source;

import java.util.ArrayList;
import java.util.List;

public class Parser{
	public static final int _EOF = 0;
	public static final int _identifier = 1;
	public static final int _stringLiteral = 2;
	public static final int _numericLiteral = 3;
	public static final int _floatLiteral = 4;
	public static final int maxT = 49;

	static final boolean _T = true;
	static final boolean _x = false;
	static final int minErrDist = 2;

	public Token t;    // last recognized token
	public Token la;   // lookahead token
	int errDist = minErrDist;
	
	public Scanner scanner;
	public Errors errors;
	private final NodeFactory factory;
    public PascalRootNode mainNode;
	private int loopDepth = 0;

	

	public Parser(PascalContext context) {
		this.factory = new NodeFactory(this, context);
		errors = new Errors();
	}

	void SynErr (int n) {
		if (errDist >= minErrDist) errors.SynErr(la.line, la.col, n);
		errDist = 0;
	}

	public void SemErr (String msg) {
		if (errDist >= minErrDist) errors.SemErr(t.line, t.col, msg);
		errDist = 0;
	}
	
	void Get () {
		for (;;) {
			t = la;
			la = scanner.Scan();
			if (la.kind <= maxT) {
				++errDist;
				break;
			}

			la = t;
		}
	}
	
	void Expect (int n) {
		if (la.kind==n) Get(); else { SynErr(n); }
	}
	
	boolean StartOf (int s) {
		return set[s][la.kind];
	}
	
	void ExpectWeak (int n, int follow) {
		if (la.kind == n) Get();
		else {
			SynErr(n);
			while (!StartOf(follow)) Get();
		}
	}
	
	boolean WeakSeparator (int n, int syFol, int repFol) {
		int kind = la.kind;
		if (kind == n) { Get(); return true; }
		else if (StartOf(repFol)) return false;
		else {
			SynErr(n);
			while (!(set[syFol][kind] || set[repFol][kind] || set[0][kind])) {
				Get();
				kind = la.kind;
			}
			return StartOf(syFol);
		}
	}
	
	void Pascal() {
		if (StartOf(1)) {
			if (la.kind == 5) {
				ImportsSection();
			}
			while (la.kind == 8 || la.kind == 10 || la.kind == 11) {
				PreDeclaration();
			}
			MainFunction();
		} else if (la.kind == 46) {
			Unit();
		} else SynErr(50);
	}

	void ImportsSection() {
		Expect(5);
		Expect(1);
		factory.importUnit(t); 
		while (la.kind == 6) {
			Get();
			Expect(1);
			factory.importUnit(t); 
		}
		Expect(7);
	}

	void PreDeclaration() {
		if (la.kind == 8) {
			VariableDefinitions();
		} else if (la.kind == 10 || la.kind == 11) {
			Subroutine();
		} else SynErr(51);
	}

	void MainFunction() {
		factory.startMainFunction(); 
		StatementNode blockNode = Block();
		mainNode = factory.finishMainFunction(blockNode); 
		Expect(14);
	}

	void Unit() {
		Expect(46);
		Expect(1);
		factory.startUnit(t); 
		Expect(7);
		Expect(47);
		InterfaceSection();
		Expect(48);
		ImplementationSection();
		Expect(16);
		factory.endUnit(); 
		Expect(14);
	}

	void VariableDefinitions() {
		Expect(8);
		VariableLineDefinition();
		Expect(7);
		while (la.kind == 1) {
			VariableLineDefinition();
			Expect(7);
		}
	}

	void Subroutine() {
		if (la.kind == 11) {
			Function();
		} else if (la.kind == 10) {
			Procedure();
		} else SynErr(52);
	}

	void VariableLineDefinition() {
		List<String> identifiers = new ArrayList<>(); 
		Expect(1);
		identifiers.add(t.val.toLowerCase()); 
		while (la.kind == 6) {
			Get();
			Expect(1);
			identifiers.add(t.val.toLowerCase()); 
		}
		Expect(9);
		Expect(1);
		factory.finishVariableLineDefinition(identifiers, t); 
	}

	void Function() {
		Expect(11);
		Expect(1);
		factory.startFunction(t); 
		Token name = t; 
		List<VariableDeclaration> formalParameters= new ArrayList<>(); 
		if (la.kind == 12) {
			formalParameters = FormalParameterList();
		}
		factory.addFormalParameters(formalParameters); 
		Expect(9);
		Expect(1);
		factory.setFunctionReturnValue(t); 
		factory.checkUnitInterfaceMatchFunction(name, formalParameters,t.val.toLowerCase()); 
		Expect(7);
		if (la.kind == 8) {
			VariableDefinitions();
		}
		StatementNode bodyNode = Block();
		factory.finishFunction(bodyNode); 
		Expect(7);
	}

	void Procedure() {
		Expect(10);
		Expect(1);
		factory.startProcedure(t); 
		Token name = t; 
		List<VariableDeclaration> formalParameters = new ArrayList<>(); 
		if (la.kind == 12) {
			formalParameters = FormalParameterList();
		}
		factory.addFormalParameters(formalParameters); 
		Expect(7);
		factory.checkUnitInterfaceMatchProcedure(name, formalParameters); 
		if (la.kind == 8) {
			VariableDefinitions();
		}
		StatementNode bodyNode = Block();
		factory.finishProcedure(bodyNode); 
		Expect(7);
	}

	List  FormalParameterList() {
		List  formalParameters;
		Expect(12);
		List<VariableDeclaration> parameters = new ArrayList<>(); 
		List<VariableDeclaration> parameter = new ArrayList<>(); 
		parameter = FormalParameter();
		factory.appendFormalParameter(parameter, parameters); 
		while (la.kind == 7) {
			Get();
			parameter = FormalParameter();
			factory.appendFormalParameter(parameter, parameters); 
		}
		Expect(13);
		formalParameters = parameters; 
		return formalParameters;
	}

	StatementNode  Block() {
		StatementNode  blockNode;
		Expect(15);
		List<StatementNode> body = new ArrayList<>(); 
		if (StartOf(2)) {
			StatementSequence(body);
		}
		Expect(16);
		blockNode = factory.finishBlock(body); 
		return blockNode;
	}

	List  FormalParameter() {
		List  formalParameter;
		formalParameter = new ArrayList<>(); 
		if (la.kind == 1) {
			formalParameter = ValueParameter();
		} else if (la.kind == 1) {
			formalParameter = VariableParameter();
		} else SynErr(53);
		return formalParameter;
	}

	List  ValueParameter() {
		List  formalParameter;
		List<String> identifiers = new ArrayList<>(); 
		Expect(1);
		identifiers.add(t.val.toLowerCase()); 
		while (la.kind == 6) {
			Get();
			Expect(1);
			identifiers.add(t.val.toLowerCase()); 
		}
		Expect(9);
		Expect(1);
		formalParameter = factory.createFormalParametersList(identifiers, t.val.toLowerCase()); 
		return formalParameter;
	}

	List  VariableParameter() {
		List  formalParameter;
		List<String> identifiers = new ArrayList<>(); 
		Expect(1);
		identifiers.add(t.val.toLowerCase()); 
		while (la.kind == 6) {
			Get();
			Expect(1);
			identifiers.add(t.val.toLowerCase()); 
		}
		Expect(9);
		Expect(1);
		formalParameter = factory.createFormalParametersList(identifiers, t.val.toLowerCase()); 
		return formalParameter;
	}

	void StatementSequence(List body) {
		StatementNode statement = Statement();
		body.add(statement); 
		while (la.kind == 7) {
			Get();
			statement = Statement();
			body.add(statement); 
		}
	}

	StatementNode  Statement() {
		StatementNode  statement;
		statement = null; 
		switch (la.kind) {
		case 7: case 16: case 26: case 30: {
			statement = factory.createEmptyStatement(); 
			break;
		}
		case 1: case 2: case 3: case 4: case 12: case 39: case 40: case 44: case 45: {
			statement = Expression();
			break;
		}
		case 28: {
			statement = IfStatement();
			break;
		}
		case 20: {
			statement = ForLoop();
			break;
		}
		case 27: {
			statement = WhileLoop();
			break;
		}
		case 25: {
			statement = RepeatLoop();
			break;
		}
		case 18: {
			statement = CaseStatement();
			break;
		}
		case 17: {
			Get();
			if(loopDepth == 0) 
			SemErr("Break statement outside of a loop."); 
			statement = factory.createBreak(); 
			break;
		}
		case 15: {
			statement = Block();
			break;
		}
		default: SynErr(54); break;
		}
		return statement;
	}

	ExpressionNode  Expression() {
		ExpressionNode  expression;
		expression = LogicTerm();
		while (la.kind == 31) {
			Get();
			Token op = t; 
			ExpressionNode right = LogicTerm();
			expression = factory.createBinary(op, expression, right); 
		}
		return expression;
	}

	StatementNode  IfStatement() {
		StatementNode  statement;
		Expect(28);
		ExpressionNode condition = Expression();
		Expect(29);
		StatementNode thenStatement = Statement();
		StatementNode elseStatement = null; 
		if (la.kind == 30) {
			Get();
			elseStatement = Statement();
		}
		statement = factory.createIfStatement(condition, thenStatement, elseStatement); 
		return statement;
	}

	StatementNode  ForLoop() {
		StatementNode  statement;
		loopDepth++; 
		Expect(20);
		boolean ascending = true; 
		Expect(1);
		Token variableToken = t; 
		Expect(21);
		ExpressionNode startValue = Expression();
		if (la.kind == 22) {
			Get();
			ascending = true; 
		} else if (la.kind == 23) {
			Get();
			ascending = false; 
		} else SynErr(55);
		ExpressionNode finalValue = Expression();
		Expect(24);
		StatementNode loopBody = Statement();
		statement = factory.createForLoop(ascending, variableToken, startValue, finalValue, loopBody); 
		loopDepth--; 
		return statement;
	}

	StatementNode  WhileLoop() {
		StatementNode  statement;
		loopDepth++; 
		Expect(27);
		ExpressionNode condition = Expression();
		Expect(24);
		StatementNode loopBody = Statement();
		statement = factory.createWhileLoop(condition, loopBody); 
		loopDepth--; 
		return statement;
	}

	StatementNode  RepeatLoop() {
		StatementNode  statement;
		loopDepth++; 
		Expect(25);
		List<StatementNode> bodyNodes = new ArrayList<>(); 
		StatementSequence(bodyNodes);
		Expect(26);
		ExpressionNode condition = Expression();
		statement = factory.createRepeatLoop(condition, factory.finishBlock(bodyNodes)); 
		loopDepth--; 
		return statement;
	}

	StatementNode  CaseStatement() {
		StatementNode  statement;
		Expect(18);
		ExpressionNode caseIndex = Expression();
		Expect(19);
		factory.startCaseList();	
		CaseList();
		Expect(16);
		statement = factory.finishCaseStatement(caseIndex); 
		return statement;
	}

	void CaseList() {
		ExpressionNode caseConstant = Expression();
		Expect(9);
		StatementNode caseStatement = Statement();
		factory.addCaseOption(caseConstant, caseStatement); 
		while (!caseEnds()) {
			Expect(7);
			caseConstant = Expression();
			Expect(9);
			caseStatement = Statement();
			factory.addCaseOption(caseConstant, caseStatement); 
		}
		if (la.kind == 7) {
			Get();
		}
	}

	ExpressionNode  LogicTerm() {
		ExpressionNode  expression;
		expression = LogicFactor();
		while (la.kind == 32) {
			Get();
			Token op = t; 
			ExpressionNode right = LogicFactor();
			expression = factory.createBinary(op, expression, right); 
		}
		return expression;
	}

	ExpressionNode  LogicFactor() {
		ExpressionNode  expression;
		expression = Arithmetic();
		if (StartOf(3)) {
			switch (la.kind) {
			case 33: {
				Get();
				break;
			}
			case 34: {
				Get();
				break;
			}
			case 35: {
				Get();
				break;
			}
			case 36: {
				Get();
				break;
			}
			case 37: {
				Get();
				break;
			}
			case 38: {
				Get();
				break;
			}
			}
			Token op = t; 
			ExpressionNode right = Arithmetic();
			expression = factory.createBinary(op, expression, right); 
		}
		return expression;
	}

	ExpressionNode  Arithmetic() {
		ExpressionNode  expression;
		expression = Term();
		while (la.kind == 39 || la.kind == 40) {
			if (la.kind == 39) {
				Get();
			} else {
				Get();
			}
			Token op = t; 
			ExpressionNode right = Term();
			expression = factory.createBinary(op, expression, right); 
		}
		return expression;
	}

	ExpressionNode  Term() {
		ExpressionNode  expression;
		expression = SignedFactor();
		while (la.kind == 41 || la.kind == 42 || la.kind == 43) {
			if (la.kind == 41) {
				Get();
			} else if (la.kind == 42) {
				Get();
			} else {
				Get();
			}
			Token op = t; 
			ExpressionNode right = SignedFactor();
			expression = factory.createBinary(op, expression, right); 
		}
		return expression;
	}

	ExpressionNode  SignedFactor() {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 39 || la.kind == 40) {
			if (la.kind == 39) {
				Get();
			} else {
				Get();
			}
			Token unOp = t; 
			expression = SignedFactor();
			expression = factory.createUnary(unOp, expression); 
		} else if (StartOf(4)) {
			expression = Factor();
		} else SynErr(56);
		return expression;
	}

	ExpressionNode  Factor() {
		ExpressionNode  expression;
		expression = null; 
		switch (la.kind) {
		case 1: {
			Get();
			if (la.kind == 12 || la.kind == 21) {
				expression = MemberExpression(t);
			} else if (StartOf(5)) {
				expression = factory.readVariable(t); 
				if(expression == null) 
				SemErr("Undefined variable " + t.val + "."); 
			} else SynErr(57);
			break;
		}
		case 12: {
			Get();
			expression = Expression();
			Expect(13);
			break;
		}
		case 2: {
			Get();
			expression = factory.createCharLiteral(t); 
			break;
		}
		case 4: {
			Get();
			expression = factory.createFloatLiteral(t); 
			break;
		}
		case 3: {
			Get();
			expression = factory.createNumericLiteral(t); 
			if(expression == null) 
			SemErr("Constant out of range!"); 
			break;
		}
		case 44: case 45: {
			expression = LogicLiteral();
			break;
		}
		default: SynErr(58); break;
		}
		return expression;
	}

	ExpressionNode  MemberExpression(Token identifierName) {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 12) {
			Get();
			ExpressionNode functionNode = factory.createFunctionNode(identifierName); 
			List<ExpressionNode> parameters = new ArrayList<>(); 
			ExpressionNode parameter; 
			if (StartOf(6)) {
				parameter = Expression();
				parameters.add(parameter); 
				while (la.kind == 6) {
					Get();
					parameter = Expression();
					parameters.add(parameter); 
				}
			}
			Expect(13);
			expression = factory.createCall(functionNode, parameters); 
		} else if (la.kind == 21) {
			Get();
			ExpressionNode value = Expression();
			if(identifierName == null) { 
			SemErr("Invalid assignment target!"); 
			} else { 
			expression = factory.createAssignment(identifierName, value); 
			if(expression == null) 
			SemErr("Undefined variable " + identifierName.val.toLowerCase() + "."); 
			} 
		} else SynErr(59);
		return expression;
	}

	ExpressionNode  LogicLiteral() {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 44) {
			Get();
			expression = factory.createLogicLiteral(true); 
		} else if (la.kind == 45) {
			Get();
			expression = factory.createLogicLiteral(false); 
		} else SynErr(60);
		return expression;
	}

	void InterfaceSection() {
		while (la.kind == 8 || la.kind == 10 || la.kind == 11) {
			if (la.kind == 10) {
				ProcedureHeading();
			} else if (la.kind == 11) {
				FunctionHeading();
			} else {
				VariableDefinitions();
			}
		}
	}

	void ImplementationSection() {
		while (la.kind == 8 || la.kind == 10 || la.kind == 11) {
			if (la.kind == 10 || la.kind == 11) {
				Subroutine();
			} else {
				VariableDefinitions();
			}
		}
	}

	void ProcedureHeading() {
		Expect(10);
		Expect(1);
		Token name = t; 
		List<VariableDeclaration> formalParameters = new ArrayList<>(); 
		if (la.kind == 12) {
			formalParameters = IFormalParameterList();
		}
		factory.addProcedureInterface(name, formalParameters); 
		Expect(7);
	}

	void FunctionHeading() {
		Expect(11);
		Expect(1);
		Token name = t; 
		List<VariableDeclaration> formalParameters = new ArrayList<>(); 
		if (la.kind == 12) {
			formalParameters = IFormalParameterList();
		}
		Expect(9);
		Expect(1);
		String returnValue = t.val; 
		factory.addFunctionInterface(name, formalParameters, returnValue); 
		Expect(7);
	}

	List  IFormalParameterList() {
		List  formalParameters;
		formalParameters = new ArrayList<VariableDeclaration>(); 
		Expect(12);
		List<VariableDeclaration> formalParameter = new ArrayList<>(); 
		formalParameter = IFormalParameter();
		factory.appendFormalParameter(formalParameter, formalParameters); 
		while (la.kind == 7) {
			Get();
			formalParameter = IFormalParameter();
			factory.appendFormalParameter(formalParameter, formalParameters); 
		}
		Expect(13);
		return formalParameters;
	}

	List  IFormalParameter() {
		List  formalParameter;
		formalParameter = new ArrayList<VariableDeclaration>(); 
		if (la.kind == 1) {
			formalParameter = IValueParameter();
		} else if (la.kind == 8) {
			formalParameter = IVariableParameter();
		} else SynErr(61);
		return formalParameter;
	}

	List  IValueParameter() {
		List  formalParameter;
		List<String> identifiers = new ArrayList<>(); 
		Expect(1);
		identifiers.add(t.val); 
		while (la.kind == 6) {
			Get();
			Expect(1);
			identifiers.add(t.val); 
		}
		Expect(9);
		Expect(1);
		String typeName = t.val; 
		formalParameter = factory.createFormalParametersList(identifiers, typeName); 
		return formalParameter;
	}

	List  IVariableParameter() {
		List  formalParameter;
		List<String> identifiers = new ArrayList<>(); 
		Expect(8);
		Expect(1);
		identifiers.add(t.val); 
		while (la.kind == 6) {
			Get();
			Expect(1);
			identifiers.add(t.val); 
		}
		Expect(9);
		Expect(1);
		String typeName = t.val; 
		formalParameter = factory.createFormalParametersList(identifiers, typeName); 
		return formalParameter;
	}



	public void Parse(Source source) {
		this.scanner = new Scanner(source.getInputStream());
		la = new Token();
		la.val = "";		
		Get();
		Pascal();
		Expect(0);

	}

	private static final boolean[][] set = {
		{_T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_T,_x,_x, _T,_x,_T,_T, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_T, _x,_x,_x,_x, _T,_x,_x,_T, _T,_T,_T,_x, _T,_x,_x,_x, _x,_T,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_x, _T,_T,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_T,_T, _T,_T,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_T,_T, _x,_T,_x,_x, _x,_T,_x,_x, _T,_x,_x,_T, _x,_x,_T,_T, _T,_x,_T,_x, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _x,_x,_x,_x, _x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_x, _T,_T,_x,_x, _x,_x,_x}

	};
	
    public boolean noErrors(){
    	return errors.count == 0;
    }
    
    public boolean caseEnds(){
    	if(la.val.toLowerCase().equals("end") && !t.val.toLowerCase().equals(":"))
    		return true;
    		
    	else if(la.val.toLowerCase().equals(";")){
    		Token next = scanner.Peek();
    		return next.val.toLowerCase().equals("end");
    	}
    	
    	return false;
    }
} // end Parser

class VariableDeclaration{
	public VariableDeclaration(String id, String type){
		this.type = type;
		this.identifier = id;
	}
	
	public String type;
	public String identifier;
}

class Errors {
	public int count = 0;                                    // number of errors detected
	public java.io.PrintStream errorStream = System.out;     // error messages go to this stream
	public String errMsgFormat = "-- line {0} col {1}: {2}"; // 0=line, 1=column, 2=text
	
	protected void printMsg(int line, int column, String msg) {
		StringBuffer b = new StringBuffer(errMsgFormat);
		int pos = b.indexOf("{0}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, line); }
		pos = b.indexOf("{1}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, column); }
		pos = b.indexOf("{2}");
		if (pos >= 0) b.replace(pos, pos+3, msg);
		errorStream.println(b.toString());
	}
	
	public void SynErr (int line, int col, int n) {
		String s;
		switch (n) {
			case 0: s = "EOF expected"; break;
			case 1: s = "identifier expected"; break;
			case 2: s = "stringLiteral expected"; break;
			case 3: s = "numericLiteral expected"; break;
			case 4: s = "floatLiteral expected"; break;
			case 5: s = "\"uses\" expected"; break;
			case 6: s = "\",\" expected"; break;
			case 7: s = "\";\" expected"; break;
			case 8: s = "\"var\" expected"; break;
			case 9: s = "\":\" expected"; break;
			case 10: s = "\"procedure\" expected"; break;
			case 11: s = "\"function\" expected"; break;
			case 12: s = "\"(\" expected"; break;
			case 13: s = "\")\" expected"; break;
			case 14: s = "\".\" expected"; break;
			case 15: s = "\"begin\" expected"; break;
			case 16: s = "\"end\" expected"; break;
			case 17: s = "\"break\" expected"; break;
			case 18: s = "\"case\" expected"; break;
			case 19: s = "\"of\" expected"; break;
			case 20: s = "\"for\" expected"; break;
			case 21: s = "\":=\" expected"; break;
			case 22: s = "\"to\" expected"; break;
			case 23: s = "\"downto\" expected"; break;
			case 24: s = "\"do\" expected"; break;
			case 25: s = "\"repeat\" expected"; break;
			case 26: s = "\"until\" expected"; break;
			case 27: s = "\"while\" expected"; break;
			case 28: s = "\"if\" expected"; break;
			case 29: s = "\"then\" expected"; break;
			case 30: s = "\"else\" expected"; break;
			case 31: s = "\"or\" expected"; break;
			case 32: s = "\"and\" expected"; break;
			case 33: s = "\">\" expected"; break;
			case 34: s = "\">=\" expected"; break;
			case 35: s = "\"<\" expected"; break;
			case 36: s = "\"<=\" expected"; break;
			case 37: s = "\"=\" expected"; break;
			case 38: s = "\"<>\" expected"; break;
			case 39: s = "\"+\" expected"; break;
			case 40: s = "\"-\" expected"; break;
			case 41: s = "\"*\" expected"; break;
			case 42: s = "\"div\" expected"; break;
			case 43: s = "\"mod\" expected"; break;
			case 44: s = "\"true\" expected"; break;
			case 45: s = "\"false\" expected"; break;
			case 46: s = "\"unit\" expected"; break;
			case 47: s = "\"interface\" expected"; break;
			case 48: s = "\"implementation\" expected"; break;
			case 49: s = "??? expected"; break;
			case 50: s = "invalid Pascal"; break;
			case 51: s = "invalid PreDeclaration"; break;
			case 52: s = "invalid Subroutine"; break;
			case 53: s = "invalid FormalParameter"; break;
			case 54: s = "invalid Statement"; break;
			case 55: s = "invalid ForLoop"; break;
			case 56: s = "invalid SignedFactor"; break;
			case 57: s = "invalid Factor"; break;
			case 58: s = "invalid Factor"; break;
			case 59: s = "invalid MemberExpression"; break;
			case 60: s = "invalid LogicLiteral"; break;
			case 61: s = "invalid IFormalParameter"; break;
			default: s = "error " + n; break;
		}
		printMsg(line, col, s);
		count++;
	}

	public void SemErr (int line, int col, String s) {	
		printMsg(line, col, s);
		count++;
	}
	
	public void SemErr (String s) {
		errorStream.println(s);
		count++;
	}
	
	public void Warning (int line, int col, String s) {	
		printMsg(line, col, s);
	}
	
	public void Warning (String s) {
		errorStream.println(s);
	}
} // Errors


class FatalError extends RuntimeException {
	public static final long serialVersionUID = 1L;
	public FatalError(String s) { super(s); }
}
