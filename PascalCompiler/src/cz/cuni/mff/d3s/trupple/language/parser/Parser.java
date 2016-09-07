
package cz.cuni.mff.d3s.trupple.language.parser;

import cz.cuni.mff.d3s.trupple.language.nodes.*;
import cz.cuni.mff.d3s.trupple.language.customtypes.IOrdinalType;

import com.oracle.truffle.api.source.Source;

import java.util.ArrayList;
import java.util.List;

public class Parser{
	public static final int _EOF = 0;
	public static final int _identifier = 1;
	public static final int _stringLiteral = 2;
	public static final int _numericLiteral = 3;
	public static final int _floatLiteral = 4;
	public static final int maxT = 62;

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

	

	public Parser() {
		this.factory = new NodeFactory(this);
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
			while (StartOf(2)) {
				Declaration();
			}
			MainFunction();
		} else if (la.kind == 59) {
			Unit();
		} else SynErr(63);
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

	void Declaration() {
		if (la.kind == 15) {
			VariableDefinitions();
		} else if (la.kind == 12) {
			ConstantDefinition();
		} else if (la.kind == 8) {
			TypeDefinition();
		} else if (la.kind == 23 || la.kind == 24) {
			Subroutine();
		} else SynErr(64);
	}

	void MainFunction() {
		factory.startMainFunction(); 
		StatementNode blockNode = Block();
		mainNode = factory.finishMainFunction(blockNode); 
		Expect(26);
	}

	void Unit() {
		Expect(59);
		Expect(1);
		factory.startUnit(t); 
		Expect(7);
		Expect(60);
		InterfaceSection();
		Expect(61);
		factory.leaveUnitInterfaceSection(); 
		ImplementationSection();
		Expect(28);
		factory.endUnit(); 
		Expect(26);
	}

	void VariableDefinitions() {
		Expect(15);
		VariableLineDefinition();
		Expect(7);
		while (la.kind == 1) {
			VariableLineDefinition();
			Expect(7);
		}
	}

	void ConstantDefinition() {
		Expect(12);
		Expect(1);
		Token identifier = t; 
		Expect(9);
		if (la.kind == 4) {
			Get();
			factory.createFloatConstant(identifier, t); 
		} else if (la.kind == 3) {
			Get();
			factory.createIntegerConstant(identifier, t); 
		} else if (la.kind == 2) {
			Get();
			factory.createStringOrCharConstant(identifier, t); 
		} else if (la.kind == 13 || la.kind == 14) {
			boolean l; 
			l = LogicLiteral();
			factory.createBooleanConstant(identifier, l); 
		} else SynErr(65);
		Expect(7);
	}

	void TypeDefinition() {
		Expect(8);
		Expect(1);
		Token identifier = t; 
		Expect(9);
		Enum(identifier);
		Expect(7);
	}

	void Subroutine() {
		boolean isFunction = false; 
		if (la.kind == 23) {
			Get();
			isFunction = true; 
		} else if (la.kind == 24) {
			Get();
		} else SynErr(66);
		Expect(1);
		if(isFunction) factory.startFunction(t); else factory.startProcedure(t); 
		Token name = t; 
		Token returnType = null; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 10) {
			formalParameters = FormalParameterList();
		}
		factory.addFormalParameters(formalParameters); 
		if (isFunction) {
			Expect(16);
			Expect(1);
			factory.setFunctionReturnValue(t); 
			returnType = t; 
		}
		if(isFunction)  factory.finishFormalParameterListFunction(name, formalParameters,t.val.toLowerCase()); 
		else factory.finishFormalParameterListProcedure(name, formalParameters); 
		Expect(7);
		if (StartOf(3)) {
			while (StartOf(2)) {
				Declaration();
			}
			StatementNode bodyNode = Block();
			if(isFunction) factory.finishFunction(bodyNode); 
			else factory.finishProcedure(bodyNode); 
			Expect(7);
		} else if (la.kind == 25) {
			Get();
			if(isFunction) factory.addFunctionInterface(name, formalParameters, returnType.val.toLowerCase()); 
			else factory.addProcedureInterface(name, formalParameters); 
		} else SynErr(67);
	}

	void Enum(Token identifier) {
		Expect(10);
		List<String> identifiers = new ArrayList<String>(); 
		Expect(1);
		identifiers.add(t.val.toLowerCase()); 
		while (la.kind == 6) {
			Get();
			Expect(1);
			identifiers.add(t.val.toLowerCase()); 
		}
		Expect(11);
		factory.registerEnumType(identifier.val.toLowerCase(), identifiers); 
	}

	boolean  LogicLiteral() {
		boolean  result;
		result = false; 
		if (la.kind == 13) {
			Get();
			result = true; 
		} else if (la.kind == 14) {
			Get();
			result = false; 
		} else SynErr(68);
		return result;
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
		Expect(16);
		if (la.kind == 1) {
			Get();
			factory.finishVariableLineDefinition(identifiers, t); 
		} else if (la.kind == 18 || la.kind == 19) {
			List ordinalDimensions = ArrayDefinition();
			while (continuesArray()) {
				Expect(17);
				List additionalDimensions = ArrayDefinition();
				ordinalDimensions.addAll(additionalDimensions); 
			}
			Expect(17);
			Expect(1);
			factory.finishArrayDefinition(identifiers, ordinalDimensions, t); 
		} else SynErr(69);
	}

	List  ArrayDefinition() {
		List  ordinalDimensions;
		if (la.kind == 18) {
			Get();
		}
		Expect(19);
		ordinalDimensions = new ArrayList<>(); 
		Expect(20);
		IOrdinalType ordinal = null; 
		ordinal = Ordinal();
		ordinalDimensions.add(ordinal); 
		while (la.kind == 6) {
			Get();
			ordinal = Ordinal();
			ordinalDimensions.add(ordinal); 
		}
		Expect(21);
		return ordinalDimensions;
	}

	IOrdinalType  Ordinal() {
		IOrdinalType  ordinal;
		ordinal = null; 
		if (la.kind == 3) {
			Get();
			Token lowerBound = t; 
			Expect(22);
			Expect(3);
			Token upperBound = t; 
			ordinal = factory.createSimpleOrdinal(lowerBound, upperBound); 
		} else if (la.kind == 1) {
			Get();
			Token identifier = t; 
			ordinal = factory.createSimpleOrdinalFromTypename(identifier); 
		} else SynErr(70);
		return ordinal;
	}

	List  FormalParameterList() {
		List  formalParameters;
		Expect(10);
		formalParameters = new ArrayList<>(); 
		List<FormalParameter> parameter = new ArrayList<>(); 
		parameter = FormalParameter();
		factory.appendFormalParameter(parameter, formalParameters); 
		while (la.kind == 7) {
			Get();
			parameter = FormalParameter();
			factory.appendFormalParameter(parameter, formalParameters); 
		}
		Expect(11);
		return formalParameters;
	}

	StatementNode  Block() {
		StatementNode  blockNode;
		Expect(27);
		List<StatementNode> body = new ArrayList<>(); 
		if (StartOf(4)) {
			StatementSequence(body);
		}
		Expect(28);
		blockNode = factory.finishBlock(body); 
		return blockNode;
	}

	List  FormalParameter() {
		List  formalParameter;
		List<String> identifiers = new ArrayList<>(); 
		boolean isOutput = false; 
		if (la.kind == 15) {
			Get();
			isOutput = true; 
		}
		Expect(1);
		identifiers.add(t.val.toLowerCase()); 
		while (la.kind == 6) {
			Get();
			Expect(1);
			identifiers.add(t.val.toLowerCase()); 
		}
		Expect(16);
		Expect(1);
		formalParameter = factory.createFormalParametersList(identifiers, t.val.toLowerCase(), isOutput); 
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
		case 7: case 28: case 33: case 40: {
			statement = factory.createEmptyStatement(); 
			break;
		}
		case 1: case 2: case 3: case 4: case 10: case 13: case 14: case 46: case 52: case 53: case 58: {
			statement = Expression();
			break;
		}
		case 42: {
			statement = IfStatement();
			break;
		}
		case 34: {
			statement = ForLoop();
			break;
		}
		case 41: {
			statement = WhileLoop();
			break;
		}
		case 39: {
			statement = RepeatLoop();
			break;
		}
		case 32: {
			statement = CaseStatement();
			break;
		}
		case 29: {
			Get();
			if(loopDepth == 0) 
			SemErr("Break statement outside of a loop."); 
			statement = factory.createBreak(); 
			break;
		}
		case 27: {
			statement = Block();
			break;
		}
		case 31: {
			statement = ReadStatement();
			break;
		}
		case 30: {
			Get();
			statement = factory.createRandomizeNode(); 
			break;
		}
		default: SynErr(71); break;
		}
		return statement;
	}

	ExpressionNode  Expression() {
		ExpressionNode  expression;
		expression = LogicTerm();
		while (la.kind == 44) {
			Get();
			Token op = t; 
			ExpressionNode right = LogicTerm();
			expression = factory.createBinary(op, expression, right); 
		}
		return expression;
	}

	StatementNode  IfStatement() {
		StatementNode  statement;
		Expect(42);
		ExpressionNode condition = Expression();
		Expect(43);
		StatementNode thenStatement = Statement();
		StatementNode elseStatement = null; 
		if (la.kind == 33) {
			Get();
			elseStatement = Statement();
		}
		statement = factory.createIfStatement(condition, thenStatement, elseStatement); 
		return statement;
	}

	StatementNode  ForLoop() {
		StatementNode  statement;
		loopDepth++; 
		Expect(34);
		boolean ascending = true; 
		Expect(1);
		Token variableToken = t; 
		Expect(35);
		ExpressionNode startValue = Expression();
		if (la.kind == 36) {
			Get();
			ascending = true; 
		} else if (la.kind == 37) {
			Get();
			ascending = false; 
		} else SynErr(72);
		ExpressionNode finalValue = Expression();
		Expect(38);
		StatementNode loopBody = Statement();
		statement = factory.createForLoop(ascending, variableToken, startValue, finalValue, loopBody); 
		loopDepth--; 
		return statement;
	}

	StatementNode  WhileLoop() {
		StatementNode  statement;
		loopDepth++; 
		Expect(41);
		ExpressionNode condition = Expression();
		Expect(38);
		StatementNode loopBody = Statement();
		statement = factory.createWhileLoop(condition, loopBody); 
		loopDepth--; 
		return statement;
	}

	StatementNode  RepeatLoop() {
		StatementNode  statement;
		loopDepth++; 
		Expect(39);
		List<StatementNode> bodyNodes = new ArrayList<>(); 
		StatementSequence(bodyNodes);
		Expect(40);
		ExpressionNode condition = Expression();
		statement = factory.createRepeatLoop(condition, factory.finishBlock(bodyNodes)); 
		loopDepth--; 
		return statement;
	}

	StatementNode  CaseStatement() {
		StatementNode  statement;
		Expect(32);
		ExpressionNode caseIndex = Expression();
		Expect(17);
		factory.startCaseList();	
		CaseList();
		Expect(28);
		statement = factory.finishCaseStatement(caseIndex); 
		return statement;
	}

	StatementNode  ReadStatement() {
		StatementNode  statement;
		Expect(31);
		statement = null; 
		if (StartOf(5)) {
			statement = factory.createReadLine(); 
		} else if (la.kind == 10) {
			Get();
			List<String> identifiers = new ArrayList<>(); 
			if (la.kind == 11) {
				Get();
				statement = factory.createReadLine(); 
			} else if (la.kind == 1) {
				Get();
				identifiers.add(t.val.toLowerCase()); 
				while (la.kind == 6) {
					Get();
					Expect(1);
					identifiers.add(t.val.toLowerCase()); 
				}
				Expect(11);
				statement = factory.createReadLine(identifiers); 
			} else SynErr(73);
		} else SynErr(74);
		return statement;
	}

	void CaseList() {
		ExpressionNode caseConstant = Expression();
		Expect(16);
		StatementNode caseStatement = Statement();
		factory.addCaseOption(caseConstant, caseStatement); 
		while (!caseEnds()) {
			Expect(7);
			caseConstant = Expression();
			Expect(16);
			caseStatement = Statement();
			factory.addCaseOption(caseConstant, caseStatement); 
		}
		if (la.kind == 7) {
			Get();
		}
		StatementNode elseStatement = null; 
		if (la.kind == 33) {
			Get();
			elseStatement = Statement();
			factory.setCaseElse(elseStatement); 
		}
		if (la.kind == 7) {
			Get();
		}
	}

	ExpressionNode  LogicTerm() {
		ExpressionNode  expression;
		expression = SignedLogicFactor();
		while (la.kind == 45) {
			Get();
			Token op = t; 
			ExpressionNode right = SignedLogicFactor();
			expression = factory.createBinary(op, expression, right); 
		}
		return expression;
	}

	ExpressionNode  SignedLogicFactor() {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 46) {
			Get();
			Token op = t; 
			ExpressionNode right = SignedLogicFactor();
			expression = factory.createUnary(op, right); 
		} else if (StartOf(6)) {
			expression = LogicFactor();
		} else SynErr(75);
		return expression;
	}

	ExpressionNode  LogicFactor() {
		ExpressionNode  expression;
		expression = Arithmetic();
		if (StartOf(7)) {
			switch (la.kind) {
			case 47: {
				Get();
				break;
			}
			case 48: {
				Get();
				break;
			}
			case 49: {
				Get();
				break;
			}
			case 50: {
				Get();
				break;
			}
			case 9: {
				Get();
				break;
			}
			case 51: {
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
		while (la.kind == 52 || la.kind == 53) {
			if (la.kind == 52) {
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
		while (StartOf(8)) {
			if (la.kind == 54) {
				Get();
			} else if (la.kind == 55) {
				Get();
			} else if (la.kind == 56) {
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
		if (la.kind == 52 || la.kind == 53) {
			if (la.kind == 52) {
				Get();
			} else {
				Get();
			}
			Token unOp = t; 
			expression = SignedFactor();
			expression = factory.createUnary(unOp, expression); 
		} else if (StartOf(9)) {
			expression = Factor();
		} else SynErr(76);
		return expression;
	}

	ExpressionNode  Factor() {
		ExpressionNode  expression;
		expression = null; 
		switch (la.kind) {
		case 58: {
			expression = Random();
			break;
		}
		case 1: {
			Get();
			if (la.kind == 10 || la.kind == 20 || la.kind == 35) {
				expression = MemberExpression(t);
			} else if (StartOf(10)) {
				expression = factory.readSingleIdentifier(t); 
				if(expression == null) 
				SemErr("Undefined identifier " + t.val + "."); 
			} else SynErr(77);
			break;
		}
		case 10: {
			Get();
			expression = Expression();
			Expect(11);
			break;
		}
		case 2: {
			Get();
			expression = factory.createCharOrStringLiteral(t); 
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
		case 13: case 14: {
			boolean val; 
			val = LogicLiteral();
			expression = factory.createLogicLiteral(val); 
			break;
		}
		default: SynErr(78); break;
		}
		return expression;
	}

	ExpressionNode  Random() {
		ExpressionNode  expression;
		Expect(58);
		expression = null; 
		if (StartOf(10)) {
			expression = factory.createRandomNode(); 
		} else if (la.kind == 10) {
			Get();
			if (la.kind == 11) {
				Get();
				expression = factory.createRandomNode(); 
			} else if (la.kind == 3) {
				Get();
				expression = factory.createRandomNode(t); 
				Expect(11);
			} else SynErr(79);
		} else SynErr(80);
		return expression;
	}

	ExpressionNode  MemberExpression(Token identifierName) {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 10) {
			Get();
			ExpressionNode functionNode = factory.createFunctionNode(identifierName); 
			List<ExpressionNode> parameters = new ArrayList<>(); 
			ExpressionNode parameter; 
			if (StartOf(11)) {
				parameter = Expression();
				parameters.add(parameter); 
				while (la.kind == 6) {
					Get();
					parameter = Expression();
					parameters.add(parameter); 
				}
			}
			Expect(11);
			expression = factory.createCall(functionNode, parameters); 
		} else if (la.kind == 35) {
			Get();
			ExpressionNode value = Expression();
			if(identifierName == null) { 
			SemErr("Invalid assignment target!"); 
			} else { 
			expression = factory.createAssignment(identifierName, value); 
			if(expression == null) 
			SemErr("Undefined variable " + identifierName.val.toLowerCase() + "."); 
			} 
		} else if (la.kind == 20) {
			expression = ArrayAccessing(identifierName);
		} else SynErr(81);
		return expression;
	}

	ExpressionNode  ArrayAccessing(Token identifierName) {
		ExpressionNode  expression;
		expression = null; 
		List<ExpressionNode> indexingNodes = new ArrayList<>(); 
		ExpressionNode indexingNode = null; 
		Expect(20);
		indexingNode = ArrayIndex();
		indexingNodes.add(indexingNode); 
		while (la.kind == 6) {
			Get();
			indexingNode = ArrayIndex();
			indexingNodes.add(indexingNode); 
		}
		Expect(21);
		if (StartOf(10)) {
			expression = factory.createReadArrayValue(identifierName, indexingNodes); 
		} else if (la.kind == 35) {
			Get();
			ExpressionNode value = Expression();
			expression = factory.createArrayIndexAssignment( 
			identifierName, indexingNodes, value); 
		} else SynErr(82);
		return expression;
	}

	ExpressionNode  ArrayIndex() {
		ExpressionNode  indexingNode;
		indexingNode = null; 
		if (!isSingleIdentifier()) {
			ExpressionNode indexNode = Expression();
			indexingNode = indexNode; 
		} else if (la.kind == 1) {
			Get();
			indexingNode = factory.createIndexingNode(t); 
		} else SynErr(83);
		return indexingNode;
	}

	void InterfaceSection() {
		while (StartOf(12)) {
			if (la.kind == 24) {
				ProcedureHeading();
			} else if (la.kind == 23) {
				FunctionHeading();
			} else if (la.kind == 15) {
				VariableDefinitions();
			} else {
				TypeDefinition();
			}
		}
	}

	void ImplementationSection() {
		while (StartOf(12)) {
			if (la.kind == 23 || la.kind == 24) {
				Subroutine();
			} else if (la.kind == 15) {
				VariableDefinitions();
			} else {
				TypeDefinition();
			}
		}
	}

	void ProcedureHeading() {
		Expect(24);
		Expect(1);
		Token name = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 10) {
			formalParameters = FormalParameterList();
		}
		factory.addProcedureInterface(name, formalParameters); 
		Expect(7);
	}

	void FunctionHeading() {
		Expect(23);
		Expect(1);
		Token name = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 10) {
			formalParameters = FormalParameterList();
		}
		Expect(16);
		Expect(1);
		String returnValue = t.val; 
		factory.addFunctionInterface(name, formalParameters, returnValue); 
		Expect(7);
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
		{_T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_T,_x,_x, _T,_x,_x,_x, _T,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _T,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _T,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_T, _x,_x,_T,_x, _x,_T,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_T,_T,_T, _T,_x,_T,_x, _x,_x,_x,_T, _x,_T,_T,_x, _x,_x,_T,_x, _x,_x,_x,_x, _T,_T,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_x, _x,_x,_T,_x, _x,_T,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_T,_T,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_T, _T,_T,_x,_x, _x,_x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_x, _x,_x,_T,_x, _x,_T,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_T,_T, _x,_T,_x,_T, _x,_x,_x,_x, _T,_T,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_T,_x,_x, _T,_T,_T,_x, _T,_x,_x,_T, _T,_T,_x,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x,_x, _x,_x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_x, _x,_x,_T,_x, _x,_T,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _T,_T,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x}

	};
	
    public boolean noErrors(){
    	return errors.count == 0;
    }
    
    public boolean caseEnds(){
    	if(la.val.toLowerCase().equals("end") && !t.val.toLowerCase().equals(":"))
    		return true;
    		
    	if(la.val.toLowerCase().equals("else"))
    		return true;
    		
    	else if(la.val.toLowerCase().equals(";")){
    		Token next = scanner.Peek();
    		return next.val.toLowerCase().equals("end") || next.val.toLowerCase().equals("else");
    	}
    	
    	return false;
    }
    
    public boolean isSingleIdentifier(){
    	Token next = scanner.Peek();
    	if(next.val.equals("]") && factory.containsIdentifier(la.val.toLowerCase()))
    		return true;

    	return false;
    }
    
    public boolean continuesArray(){
    	Token next = scanner.Peek();
    	if(next.val.toLowerCase().equals("array") || (next.val.toLowerCase().equals("packed")))
    		return true;

    	return false;
    }
} // end Parser

class FormalParameter{
	public FormalParameter(String id, String type, boolean isOutput){
		this.type = type;
		this.identifier = id;
		this.isOutput = isOutput;
	}
	
	public String type;
	public String identifier;
	public boolean isOutput;
}

class Errors {
	public int count = 0;                                    // number of errors detected
	public java.io.PrintStream errorStream = System.err;     // error messages go to this stream
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
			case 8: s = "\"type\" expected"; break;
			case 9: s = "\"=\" expected"; break;
			case 10: s = "\"(\" expected"; break;
			case 11: s = "\")\" expected"; break;
			case 12: s = "\"const\" expected"; break;
			case 13: s = "\"true\" expected"; break;
			case 14: s = "\"false\" expected"; break;
			case 15: s = "\"var\" expected"; break;
			case 16: s = "\":\" expected"; break;
			case 17: s = "\"of\" expected"; break;
			case 18: s = "\"packed\" expected"; break;
			case 19: s = "\"array\" expected"; break;
			case 20: s = "\"[\" expected"; break;
			case 21: s = "\"]\" expected"; break;
			case 22: s = "\"..\" expected"; break;
			case 23: s = "\"function\" expected"; break;
			case 24: s = "\"procedure\" expected"; break;
			case 25: s = "\"forward;\" expected"; break;
			case 26: s = "\".\" expected"; break;
			case 27: s = "\"begin\" expected"; break;
			case 28: s = "\"end\" expected"; break;
			case 29: s = "\"break\" expected"; break;
			case 30: s = "\"randomize\" expected"; break;
			case 31: s = "\"readln\" expected"; break;
			case 32: s = "\"case\" expected"; break;
			case 33: s = "\"else\" expected"; break;
			case 34: s = "\"for\" expected"; break;
			case 35: s = "\":=\" expected"; break;
			case 36: s = "\"to\" expected"; break;
			case 37: s = "\"downto\" expected"; break;
			case 38: s = "\"do\" expected"; break;
			case 39: s = "\"repeat\" expected"; break;
			case 40: s = "\"until\" expected"; break;
			case 41: s = "\"while\" expected"; break;
			case 42: s = "\"if\" expected"; break;
			case 43: s = "\"then\" expected"; break;
			case 44: s = "\"or\" expected"; break;
			case 45: s = "\"and\" expected"; break;
			case 46: s = "\"not\" expected"; break;
			case 47: s = "\">\" expected"; break;
			case 48: s = "\">=\" expected"; break;
			case 49: s = "\"<\" expected"; break;
			case 50: s = "\"<=\" expected"; break;
			case 51: s = "\"<>\" expected"; break;
			case 52: s = "\"+\" expected"; break;
			case 53: s = "\"-\" expected"; break;
			case 54: s = "\"*\" expected"; break;
			case 55: s = "\"/\" expected"; break;
			case 56: s = "\"div\" expected"; break;
			case 57: s = "\"mod\" expected"; break;
			case 58: s = "\"random\" expected"; break;
			case 59: s = "\"unit\" expected"; break;
			case 60: s = "\"interface\" expected"; break;
			case 61: s = "\"implementation\" expected"; break;
			case 62: s = "??? expected"; break;
			case 63: s = "invalid Pascal"; break;
			case 64: s = "invalid Declaration"; break;
			case 65: s = "invalid ConstantDefinition"; break;
			case 66: s = "invalid Subroutine"; break;
			case 67: s = "invalid Subroutine"; break;
			case 68: s = "invalid LogicLiteral"; break;
			case 69: s = "invalid VariableLineDefinition"; break;
			case 70: s = "invalid Ordinal"; break;
			case 71: s = "invalid Statement"; break;
			case 72: s = "invalid ForLoop"; break;
			case 73: s = "invalid ReadStatement"; break;
			case 74: s = "invalid ReadStatement"; break;
			case 75: s = "invalid SignedLogicFactor"; break;
			case 76: s = "invalid SignedFactor"; break;
			case 77: s = "invalid Factor"; break;
			case 78: s = "invalid Factor"; break;
			case 79: s = "invalid Random"; break;
			case 80: s = "invalid Random"; break;
			case 81: s = "invalid MemberExpression"; break;
			case 82: s = "invalid ArrayAccessing"; break;
			case 83: s = "invalid ArrayIndex"; break;
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
