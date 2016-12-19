
package cz.cuni.mff.d3s.trupple.language.parser;

import cz.cuni.mff.d3s.trupple.language.nodes.*;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;

import com.oracle.truffle.api.source.Source;

import java.util.ArrayList;
import java.util.List;

public class Parser{
	public static final int _EOF = 0;
	public static final int _identifier = 1;
	public static final int _stringLiteral = 2;
	public static final int _integerLiteral = 3;
	public static final int _doubleLiteral = 4;
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
			factory.startPascal(); 
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
		if (la.kind == 21) {
			VariableDefinitions();
		} else if (la.kind == 20) {
			ConstantDefinition();
		} else if (la.kind == 8) {
			TypeDefinition();
		} else if (la.kind == 23 || la.kind == 25) {
			Subroutine();
		} else SynErr(64);
	}

	void MainFunction() {
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
		Expect(21);
		VariableLineDefinition();
		Expect(7);
		while (la.kind == 1) {
			VariableLineDefinition();
			Expect(7);
		}
	}

	void ConstantDefinition() {
		Expect(20);
		Expect(1);
		Token identifier = t; 
		Expect(9);
		if (StartOf(3)) {
			NumericConstant(identifier);
		} else if (la.kind == 2) {
			String value = StringLiteral();
			factory.registerStringOrCharConstant(identifier, value); 
		} else if (la.kind == 56 || la.kind == 57) {
			boolean value = LogicLiteral();
			factory.registerBooleanConstant(identifier, value); 
		} else if (la.kind == 1 || la.kind == 18 || la.kind == 19) {
			IdentifierConstant(identifier);
		} else SynErr(65);
		Expect(7);
	}

	void TypeDefinition() {
		Expect(8);
		Expect(1);
		Token identifier = t; 
		Expect(9);
		TypeDescriptor typeDescriptor = Type();
		factory.registerNewType(identifier, typeDescriptor); 
		Expect(7);
	}

	void Subroutine() {
		if (la.kind == 23) {
			Procedure();
			Expect(7);
		} else if (la.kind == 25) {
			Function();
			Expect(7);
		} else SynErr(66);
	}

	TypeDescriptor  Type() {
		TypeDescriptor  typeDescriptor;
		typeDescriptor = null; 
		if (la.kind == 1) {
			Get();
			typeDescriptor = factory.getTypeDescriptor(t); 
		} else if (la.kind == 15) {
			typeDescriptor = EnumDefinition();
		} else if (la.kind == 11 || la.kind == 12) {
			List<OrdinalDescriptor> ordinalDimensions  = ArrayDefinition();
			while (continuesArray()) {
				Expect(10);
				List<OrdinalDescriptor> additionalDimensions  = ArrayDefinition();
				ordinalDimensions.addAll(additionalDimensions); 
			}
			Expect(10);
			Expect(1);
			typeDescriptor = factory.createArray(ordinalDimensions, t); 
		} else SynErr(67);
		return typeDescriptor;
	}

	TypeDescriptor  EnumDefinition() {
		TypeDescriptor  typeDescriptor;
		List<String> enumIdentifiers = new ArrayList<>(); 
		Expect(15);
		Expect(1);
		enumIdentifiers.add(t.val.toLowerCase()); 
		while (la.kind == 6) {
			Get();
			Expect(1);
			enumIdentifiers.add(t.val.toLowerCase()); 
		}
		Expect(16);
		typeDescriptor = factory.registerEnum(enumIdentifiers); 
		return typeDescriptor;
	}

	List<OrdinalDescriptor>  ArrayDefinition() {
		List<OrdinalDescriptor>  ordinalDimensions;
		if (la.kind == 11) {
			Get();
		}
		Expect(12);
		ordinalDimensions = new ArrayList<>(); 
		Expect(13);
		OrdinalDescriptor ordinalDescriptor = null; 
		ordinalDescriptor = Ordinal();
		ordinalDimensions.add(ordinalDescriptor); 
		while (la.kind == 6) {
			Get();
			ordinalDescriptor = Ordinal();
			ordinalDimensions.add(ordinalDescriptor); 
		}
		Expect(14);
		return ordinalDimensions;
	}

	OrdinalDescriptor  Ordinal() {
		OrdinalDescriptor  ordinal;
		ordinal = null; 
		if (la.kind == 3 || la.kind == 18 || la.kind == 19) {
			int lowerBound, upperBound; 
			lowerBound = SignedIntegerLiteral();
			Expect(17);
			upperBound = SignedIntegerLiteral();
			ordinal = factory.createSimpleOrdinalDescriptor(lowerBound, upperBound); 
		} else if (StartOf(4)) {
			TypeDescriptor typeDescriptor = Type();
			ordinal = factory.castTypeToOrdinalType(typeDescriptor); 
		} else SynErr(68);
		return ordinal;
	}

	int  SignedIntegerLiteral() {
		int  value;
		value = 0; 
		if (la.kind == 18) {
			Get();
			value = SignedIntegerLiteral();
		} else if (la.kind == 19) {
			Get();
			value = SignedIntegerLiteral();
			value = -value; 
		} else if (la.kind == 3) {
			Get();
			value = Integer.parseInt(t.val); 
		} else SynErr(69);
		return value;
	}

	void NumericConstant(Token identifier) {
		if (la.kind == 18 || la.kind == 19) {
			if (la.kind == 18) {
				Get();
			} else {
				Get();
			}
			Token sign = t; 
			if (la.kind == 3) {
				Get();
				factory.registerSignedIntegerConstant(identifier, sign, t); 
			} else if (la.kind == 4) {
				Get();
				factory.registerSignedRealConstant(identifier, sign, t); 
			} else SynErr(70);
		} else if (la.kind == 3 || la.kind == 4) {
			if (la.kind == 3) {
				Get();
				factory.registerIntegerConstant(identifier, t); 
			} else {
				Get();
				factory.registerRealConstant(identifier, t); 
			}
		} else SynErr(71);
	}

	String  StringLiteral() {
		String  value;
		Expect(2);
		value = factory.createStringFromToken(t); 
		return value;
	}

	boolean  LogicLiteral() {
		boolean  result;
		result = false; 
		if (la.kind == 56) {
			Get();
			result = true; 
		} else if (la.kind == 57) {
			Get();
			result = false; 
		} else SynErr(72);
		return result;
	}

	void IdentifierConstant(Token identifier) {
		if (la.kind == 18 || la.kind == 19) {
			if (la.kind == 18) {
				Get();
			} else {
				Get();
			}
			Token sign = t; 
			Expect(1);
			factory.registerSignedConstantFromIdentifier(identifier, sign, t); 
		} else if (la.kind == 1) {
			Get();
			factory.registerConstantFromIdentifier(identifier, t); 
		} else SynErr(73);
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
		Expect(22);
		TypeDescriptor typeDescriptor = Type();
		factory.registerVariables(identifiers, typeDescriptor); 
	}

	void Procedure() {
		Expect(23);
		Expect(1);
		Token identifierToken = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 15) {
			formalParameters = FormalParameterList();
		}
		Expect(7);
		if (la.kind == 24) {
			Get();
			factory.forwardProcedure(identifierToken, formalParameters); 
		} else if (StartOf(5)) {
			factory.startProcedure(identifierToken, formalParameters); 
			while (StartOf(2)) {
				Declaration();
			}
			StatementNode bodyNode = Block();
			factory.finishProcedure(bodyNode); 
		} else SynErr(74);
	}

	void Function() {
		Expect(25);
		Expect(1);
		Token identifierToken = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 15) {
			formalParameters = FormalParameterList();
		}
		Expect(22);
		Expect(1);
		Token returnTypeToken = t; 
		Expect(7);
		if (la.kind == 24) {
			Get();
			factory.forwardFunction(identifierToken, formalParameters, returnTypeToken); 
		} else if (StartOf(5)) {
			factory.startFunction(identifierToken, formalParameters, returnTypeToken); 
			while (StartOf(2)) {
				Declaration();
			}
			StatementNode bodyNode = Block();
			factory.finishFunction(bodyNode); 
		} else SynErr(75);
	}

	List<FormalParameter>  FormalParameterList() {
		List<FormalParameter>  formalParameters;
		Expect(15);
		formalParameters = new ArrayList<>(); 
		List<FormalParameter> newParameters = new ArrayList<>(); 
		newParameters = FormalParameter();
		factory.appendFormalParameter(newParameters, formalParameters); 
		while (la.kind == 7) {
			Get();
			newParameters = FormalParameter();
			factory.appendFormalParameter(newParameters, newParameters); 
		}
		Expect(16);
		return formalParameters;
	}

	StatementNode  Block() {
		StatementNode  blockNode;
		Expect(27);
		List<StatementNode> bodyNodes = new ArrayList<>(); 
		if (StartOf(6)) {
			StatementSequence(bodyNodes);
		}
		Expect(28);
		blockNode = factory.createBlockNode(bodyNodes); 
		return blockNode;
	}

	List<FormalParameter>  FormalParameter() {
		List<FormalParameter>  formalParameter;
		List<String> identifiers = new ArrayList<>(); 
		boolean isOutput = false; 
		if (la.kind == 21) {
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
		Expect(22);
		Expect(1);
		formalParameter = factory.createFormalParametersList(identifiers, t.val.toLowerCase(), isOutput); 
		return formalParameter;
	}

	void StatementSequence(List<StatementNode> body ) {
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
			statement = factory.createNopStatement(); 
			break;
		}
		case 1: case 2: case 3: case 4: case 15: case 18: case 19: case 46: case 56: case 57: case 58: {
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
		default: SynErr(76); break;
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
			expression = factory.createBinaryExpression(op, expression, right); 
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
		factory.startLoop(); 
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
		} else SynErr(77);
		ExpressionNode finalValue = Expression();
		Expect(38);
		StatementNode loopBody = Statement();
		statement = factory.createForLoop(ascending, variableToken, startValue, finalValue, loopBody); 
		factory.finishLoop(); 
		return statement;
	}

	StatementNode  WhileLoop() {
		StatementNode  statement;
		factory.startLoop(); 
		Expect(41);
		ExpressionNode condition = Expression();
		Expect(38);
		StatementNode loopBody = Statement();
		statement = factory.createWhileLoop(condition, loopBody); 
		factory.finishLoop(); 
		return statement;
	}

	StatementNode  RepeatLoop() {
		StatementNode  statement;
		factory.startLoop(); 
		Expect(39);
		List<StatementNode> bodyNodes = new ArrayList<>(); 
		StatementSequence(bodyNodes);
		Expect(40);
		ExpressionNode condition = Expression();
		statement = factory.createRepeatLoop(condition, factory.createBlockNode(bodyNodes)); 
		factory.finishLoop(); 
		return statement;
	}

	StatementNode  CaseStatement() {
		StatementNode  statement;
		Expect(32);
		ExpressionNode caseExpression = Expression();
		Expect(10);
		CaseStatementData caseData = CaseList();
		caseData.caseExpression = caseExpression; 
		Expect(28);
		statement = factory.createCaseStatement(caseData); 
		return statement;
	}

	StatementNode  ReadStatement() {
		StatementNode  statement;
		Expect(31);
		statement = null; 
		if (StartOf(7)) {
			statement = factory.createReadLine(); 
		} else if (la.kind == 15) {
			Get();
			List<String> identifiers = new ArrayList<>(); 
			if (la.kind == 16) {
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
				Expect(16);
				statement = factory.createReadLine(identifiers); 
			} else SynErr(78);
		} else SynErr(79);
		return statement;
	}

	CaseStatementData  CaseList() {
		CaseStatementData  data;
		data = new CaseStatementData(); 
		ExpressionNode caseConstant = Expression();
		data.indexNodes.add(caseConstant); 
		Expect(22);
		StatementNode caseStatement = Statement();
		data.statementNodes.add(caseStatement); 
		while (!caseEnds()) {
			Expect(7);
			caseConstant = Expression();
			data.indexNodes.add(caseConstant); 
			Expect(22);
			caseStatement = Statement();
			data.statementNodes.add(caseStatement); 
		}
		if (la.kind == 7) {
			Get();
		}
		if (la.kind == 33) {
			Get();
			data.elseNode = Statement();
		}
		if (la.kind == 7) {
			Get();
		}
		return data;
	}

	ExpressionNode  LogicTerm() {
		ExpressionNode  expression;
		expression = SignedLogicFactor();
		while (la.kind == 45) {
			Get();
			Token op = t; 
			ExpressionNode right = SignedLogicFactor();
			expression = factory.createBinaryExpression(op, expression, right); 
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
			expression = factory.createUnaryExpression(op, right); 
		} else if (StartOf(8)) {
			expression = LogicFactor();
		} else SynErr(80);
		return expression;
	}

	ExpressionNode  LogicFactor() {
		ExpressionNode  expression;
		expression = Arithmetic();
		if (StartOf(9)) {
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
			expression = factory.createBinaryExpression(op, expression, right); 
		}
		return expression;
	}

	ExpressionNode  Arithmetic() {
		ExpressionNode  expression;
		expression = Term();
		while (la.kind == 18 || la.kind == 19) {
			if (la.kind == 18) {
				Get();
			} else {
				Get();
			}
			Token op = t; 
			ExpressionNode right = Term();
			expression = factory.createBinaryExpression(op, expression, right); 
		}
		return expression;
	}

	ExpressionNode  Term() {
		ExpressionNode  expression;
		expression = SignedFactor();
		while (StartOf(10)) {
			if (la.kind == 52) {
				Get();
			} else if (la.kind == 53) {
				Get();
			} else if (la.kind == 54) {
				Get();
			} else {
				Get();
			}
			Token op = t; 
			ExpressionNode right = SignedFactor();
			expression = factory.createBinaryExpression(op, expression, right); 
		}
		return expression;
	}

	ExpressionNode  SignedFactor() {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 18 || la.kind == 19) {
			if (la.kind == 18) {
				Get();
			} else {
				Get();
			}
			Token unOp = t; 
			expression = SignedFactor();
			expression = factory.createUnaryExpression(unOp, expression); 
		} else if (StartOf(11)) {
			expression = Factor();
		} else SynErr(81);
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
			if (la.kind == 13 || la.kind == 15 || la.kind == 35) {
				expression = MemberExpression(t);
			} else if (StartOf(12)) {
				expression = factory.createExpressionFromSingleIdentifier(t); 
			} else SynErr(82);
			break;
		}
		case 15: {
			Get();
			expression = Expression();
			Expect(16);
			break;
		}
		case 2: {
			String value = ""; 
			value = StringLiteral();
			expression = factory.createCharOrStringLiteral(value); 
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
			break;
		}
		case 56: case 57: {
			boolean val; 
			val = LogicLiteral();
			expression = factory.createLogicLiteral(val); 
			break;
		}
		default: SynErr(83); break;
		}
		return expression;
	}

	ExpressionNode  Random() {
		ExpressionNode  expression;
		Expect(58);
		expression = null; 
		if (StartOf(12)) {
			expression = factory.createRandomNode(); 
		} else if (la.kind == 15) {
			Get();
			if (la.kind == 16) {
				Get();
				expression = factory.createRandomNode(); 
			} else if (la.kind == 3) {
				Get();
				expression = factory.createRandomNode(t); 
				Expect(16);
			} else SynErr(84);
		} else SynErr(85);
		return expression;
	}

	ExpressionNode  MemberExpression(Token identifierName) {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 15) {
			expression = SubroutineCall(identifierName);
		} else if (la.kind == 35) {
			Get();
			ExpressionNode value = Expression();
			expression = factory.createAssignment(identifierName, value); 
		} else if (la.kind == 13) {
			expression = ArrayAccessing(identifierName);
		} else SynErr(86);
		return expression;
	}

	ExpressionNode  SubroutineCall(Token identifierToken) {
		ExpressionNode  expression;
		Expect(15);
		ExpressionNode functionNode = factory.createFunctionLiteralNode(identifierToken); 
		List<ExpressionNode> parameters = new ArrayList<>(); 
		ExpressionNode parameter; 
		if (StartOf(13)) {
			parameter = Expression();
			parameters.add(parameter); 
			while (la.kind == 6) {
				Get();
				parameter = Expression();
				parameters.add(parameter); 
			}
		}
		Expect(16);
		expression = factory.createCall(functionNode, parameters); 
		return expression;
	}

	ExpressionNode  ArrayAccessing(Token identifierName) {
		ExpressionNode  expression;
		expression = null; 
		List<ExpressionNode> indexingNodes = new ArrayList<>(); 
		ExpressionNode indexingNode = null; 
		Expect(13);
		indexingNode = Expression();
		indexingNodes.add(indexingNode); 
		while (la.kind == 6) {
			Get();
			indexingNode = Expression();
			indexingNodes.add(indexingNode); 
		}
		Expect(14);
		if (StartOf(12)) {
			expression = factory.createReadArrayValue(identifierName, indexingNodes); 
		} else if (la.kind == 35) {
			Get();
			ExpressionNode value = Expression();
			expression = factory.createArrayIndexAssignment( 
			identifierName, indexingNodes, value); 
		} else SynErr(87);
		return expression;
	}

	void InterfaceSection() {
		while (StartOf(14)) {
			if (la.kind == 23) {
				ProcedureHeading();
			} else if (la.kind == 25) {
				FunctionHeading();
			} else if (la.kind == 21) {
				VariableDefinitions();
			} else {
				TypeDefinition();
			}
		}
	}

	void ImplementationSection() {
		while (StartOf(14)) {
			if (la.kind == 23 || la.kind == 25) {
				Subroutine();
			} else if (la.kind == 21) {
				VariableDefinitions();
			} else {
				TypeDefinition();
			}
		}
	}

	void ProcedureHeading() {
		Expect(23);
		Expect(1);
		Token name = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 15) {
			formalParameters = FormalParameterList();
		}
		factory.addProcedureInterface(name, formalParameters); 
		Expect(7);
	}

	void FunctionHeading() {
		Expect(25);
		Expect(1);
		Token name = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 15) {
			formalParameters = FormalParameterList();
		}
		Expect(22);
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
		{_x,_x,_x,_x, _x,_T,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_x,_T, _x,_T,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_x,_T, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_x,_T, _x,_T,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_T,_T, _x,_x,_x,_x, _x,_x,_x,_T, _T,_T,_T,_T, _T,_x,_T,_x, _x,_x,_x,_T, _x,_T,_T,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_T,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_T,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_T,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_T,_T,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_T,_T, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_T,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_T,_T, _x,_T,_T,_x, _x,_x,_T,_x, _T,_x,_T,_T, _x,_x,_T,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_T,_x,_x, _T,_T,_T,_x, _T,_x,_x,_T, _T,_T,_x,_T, _T,_T,_T,_T, _T,_T,_T,_T, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_T,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_T,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_T, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x}

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

class CaseStatementData {
    public ExpressionNode caseExpression;
    public final List<ExpressionNode> indexNodes = new ArrayList<ExpressionNode>();
    public final List<StatementNode> statementNodes = new ArrayList<StatementNode>();
    public StatementNode elseNode;
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
			case 3: s = "integerLiteral expected"; break;
			case 4: s = "doubleLiteral expected"; break;
			case 5: s = "\"uses\" expected"; break;
			case 6: s = "\",\" expected"; break;
			case 7: s = "\";\" expected"; break;
			case 8: s = "\"type\" expected"; break;
			case 9: s = "\"=\" expected"; break;
			case 10: s = "\"of\" expected"; break;
			case 11: s = "\"packed\" expected"; break;
			case 12: s = "\"array\" expected"; break;
			case 13: s = "\"[\" expected"; break;
			case 14: s = "\"]\" expected"; break;
			case 15: s = "\"(\" expected"; break;
			case 16: s = "\")\" expected"; break;
			case 17: s = "\"..\" expected"; break;
			case 18: s = "\"+\" expected"; break;
			case 19: s = "\"-\" expected"; break;
			case 20: s = "\"const\" expected"; break;
			case 21: s = "\"var\" expected"; break;
			case 22: s = "\":\" expected"; break;
			case 23: s = "\"procedure\" expected"; break;
			case 24: s = "\"forward\" expected"; break;
			case 25: s = "\"function\" expected"; break;
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
			case 52: s = "\"*\" expected"; break;
			case 53: s = "\"/\" expected"; break;
			case 54: s = "\"div\" expected"; break;
			case 55: s = "\"mod\" expected"; break;
			case 56: s = "\"true\" expected"; break;
			case 57: s = "\"false\" expected"; break;
			case 58: s = "\"random\" expected"; break;
			case 59: s = "\"unit\" expected"; break;
			case 60: s = "\"interface\" expected"; break;
			case 61: s = "\"implementation\" expected"; break;
			case 62: s = "??? expected"; break;
			case 63: s = "invalid Pascal"; break;
			case 64: s = "invalid Declaration"; break;
			case 65: s = "invalid ConstantDefinition"; break;
			case 66: s = "invalid Subroutine"; break;
			case 67: s = "invalid Type"; break;
			case 68: s = "invalid Ordinal"; break;
			case 69: s = "invalid SignedIntegerLiteral"; break;
			case 70: s = "invalid NumericConstant"; break;
			case 71: s = "invalid NumericConstant"; break;
			case 72: s = "invalid LogicLiteral"; break;
			case 73: s = "invalid IdentifierConstant"; break;
			case 74: s = "invalid Procedure"; break;
			case 75: s = "invalid Function"; break;
			case 76: s = "invalid Statement"; break;
			case 77: s = "invalid ForLoop"; break;
			case 78: s = "invalid ReadStatement"; break;
			case 79: s = "invalid ReadStatement"; break;
			case 80: s = "invalid SignedLogicFactor"; break;
			case 81: s = "invalid SignedFactor"; break;
			case 82: s = "invalid Factor"; break;
			case 83: s = "invalid Factor"; break;
			case 84: s = "invalid Random"; break;
			case 85: s = "invalid Random"; break;
			case 86: s = "invalid MemberExpression"; break;
			case 87: s = "invalid ArrayAccessing"; break;
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
