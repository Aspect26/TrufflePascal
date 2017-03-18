
package cz.cuni.mff.d3s.trupple.language.parser.tp;

import com.oracle.truffle.api.nodes.RootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AccessRouteNode;
import cz.cuni.mff.d3s.trupple.language.nodes.*;
import cz.cuni.mff.d3s.trupple.language.parser.*;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.constant.ConstantDescriptor;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;
import com.oracle.truffle.api.source.Source;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Parser implements IParser {
	public static final int _EOF = 0;
	public static final int _identifier = 1;
	public static final int _stringLiteral = 2;
	public static final int _integerLiteral = 3;
	public static final int _doubleLiteral = 4;
	public static final int maxT = 66;

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
		this.factory = new NodeFactory(this, true);
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
		if (la.kind == 5) {
			Program();
			if (la.kind == 7) {
				ImportsSection();
			}
			Declarations();
			MainFunction();
		} else if (la.kind == 63) {
			Unit();
		} else SynErr(67);
	}

	void Program() {
		Expect(5);
		Expect(1);
		factory.startPascal(t); 
		Expect(6);
	}

	void ImportsSection() {
		Expect(7);
		Expect(1);
		factory.registerUnit(t); 
		while (la.kind == 8) {
			Get();
			Expect(1);
			factory.registerUnit(t); 
		}
		Expect(6);
	}

	void Declarations() {
		if (la.kind == 26) {
			ConstantDefinitions();
		}
		if (la.kind == 9) {
			TypeDefinitions();
		}
		if (la.kind == 29) {
			VariableDeclarations();
		}
		while (la.kind == 30 || la.kind == 32) {
			Subroutine();
		}
	}

	void MainFunction() {
		StatementNode blockNode = Block();
		mainNode = factory.finishMainFunction(blockNode); 
		Expect(33);
	}

	void Unit() {
		UnitHeader();
		InterfaceSection();
		factory.finishUnitInterfaceSection(); 
		ImplementationSection();
		UnitFooter();
	}

	void ConstantDefinitions() {
		Expect(26);
		ConstantDefinition();
		Expect(6);
		while (la.kind == 1) {
			ConstantDefinition();
			Expect(6);
		}
	}

	void TypeDefinitions() {
		Expect(9);
		TypeDefinition();
		Expect(6);
		while (la.kind == 1) {
			TypeDefinition();
			Expect(6);
		}
	}

	void VariableDeclarations() {
		Expect(29);
		VariableLineDeclaration();
		Expect(6);
		while (la.kind == 1) {
			VariableLineDeclaration();
			Expect(6);
		}
	}

	void Subroutine() {
		if (la.kind == 30) {
			Procedure();
			Expect(6);
		} else if (la.kind == 32) {
			Function();
			Expect(6);
		} else SynErr(68);
	}

	void TypeDefinition() {
		Expect(1);
		Token identifier = t; 
		Expect(10);
		TypeDescriptor typeDescriptor = Type();
		factory.registerNewType(identifier, typeDescriptor); 
	}

	TypeDescriptor  Type() {
		TypeDescriptor  typeDescriptor;
		typeDescriptor = null; 
		if (isSubrange()) {
			typeDescriptor = SubrangeType();
		} else if (la.kind == 1) {
			Get();
			typeDescriptor = factory.getTypeDescriptor(t); 
		} else if (la.kind == 17) {
			typeDescriptor = EnumDefinition();
		} else if (la.kind == 13 || la.kind == 14) {
			List<OrdinalDescriptor> ordinalDimensions  = ArrayDefinition();
			while (continuesArray()) {
				Expect(11);
				List<OrdinalDescriptor> additionalDimensions  = ArrayDefinition();
				ordinalDimensions.addAll(additionalDimensions); 
			}
			Expect(11);
			Expect(1);
			typeDescriptor = factory.createArray(ordinalDimensions, t); 
		} else if (la.kind == 12) {
			Get();
			Expect(11);
			OrdinalDescriptor ordinal = Ordinal();
			typeDescriptor = factory.createSetType(ordinal); 
		} else if (la.kind == 19) {
			typeDescriptor = FileType();
		} else if (la.kind == 20) {
			typeDescriptor = RecordType();
		} else if (la.kind == 22) {
			typeDescriptor = PointerType();
		} else SynErr(69);
		return typeDescriptor;
	}

	OrdinalDescriptor  SubrangeType() {
		OrdinalDescriptor  ordinal;
		ConstantDescriptor lowerBound = Constant();
		Expect(25);
		ConstantDescriptor upperBound = Constant();
		ordinal = factory.createSimpleOrdinalDescriptor(lowerBound, upperBound); 
		return ordinal;
	}

	TypeDescriptor  EnumDefinition() {
		TypeDescriptor  typeDescriptor;
		List<String> enumIdentifiers = new ArrayList<>(); 
		Expect(17);
		Expect(1);
		enumIdentifiers.add(factory.getIdentifierFromToken(t)); 
		while (la.kind == 8) {
			Get();
			Expect(1);
			enumIdentifiers.add(factory.getIdentifierFromToken(t)); 
		}
		Expect(18);
		typeDescriptor = factory.registerEnum(enumIdentifiers); 
		return typeDescriptor;
	}

	List<OrdinalDescriptor>  ArrayDefinition() {
		List<OrdinalDescriptor>  ordinalDimensions;
		if (la.kind == 13) {
			Get();
		}
		Expect(14);
		ordinalDimensions = new ArrayList<>(); 
		Expect(15);
		OrdinalDescriptor ordinalDescriptor = null; 
		ordinalDescriptor = Ordinal();
		ordinalDimensions.add(ordinalDescriptor); 
		while (la.kind == 8) {
			Get();
			ordinalDescriptor = Ordinal();
			ordinalDimensions.add(ordinalDescriptor); 
		}
		Expect(16);
		return ordinalDimensions;
	}

	OrdinalDescriptor  Ordinal() {
		OrdinalDescriptor  ordinal;
		ordinal = null; 
		if (isSubrange()) {
			ordinal = SubrangeType();
		} else if (StartOf(1)) {
			TypeDescriptor typeDescriptor = Type();
			ordinal = factory.castTypeToOrdinalType(typeDescriptor); 
		} else SynErr(70);
		return ordinal;
	}

	TypeDescriptor  FileType() {
		TypeDescriptor  fileDescriptor;
		Expect(19);
		Expect(11);
		TypeDescriptor contentType = Type();
		fileDescriptor = factory.createFileType(contentType); 
		return fileDescriptor;
	}

	TypeDescriptor  RecordType() {
		TypeDescriptor  typeDescriptor;
		Expect(20);
		factory.startRecord(); 
		typeDescriptor = RecordFieldList();
		Expect(21);
		factory.finishRecord(); 
		return typeDescriptor;
	}

	TypeDescriptor  PointerType() {
		TypeDescriptor  typeDescriptor;
		Expect(22);
		TypeDescriptor innerType = Type();
		typeDescriptor = factory.createPointerType(innerType); 
		return typeDescriptor;
	}

	TypeDescriptor  RecordFieldList() {
		TypeDescriptor  typeDescriptor;
		if (la.kind == 1) {
			RecordFixedPart();
			if (recordVariantPartStarts()) {
				Expect(6);
				RecordVariantPart();
			}
		} else if (la.kind == 23) {
			RecordVariantPart();
		} else SynErr(71);
		if (la.kind == 6) {
			Get();
		}
		typeDescriptor = factory.createRecordType(); 
		return typeDescriptor;
	}

	void RecordFixedPart() {
		RecordFixedSection();
		while (recordFixedPartContinues()) {
			Expect(6);
			RecordFixedSection();
		}
	}

	void RecordVariantPart() {
		Expect(23);
		RecordVariantSelector();
		Expect(11);
		RecordVariants();
	}

	void RecordFixedSection() {
		VariableLineDeclaration();
	}

	void VariableLineDeclaration() {
		List<String> identifiers  = IdentifiersList();
		Expect(24);
		TypeDescriptor typeDescriptor = Type();
		factory.registerVariables(identifiers, typeDescriptor); 
	}

	void RecordVariantSelector() {
		if (isVariantSelectorTag()) {
			Expect(1);
			Expect(24);
		}
		Expect(1);
		
	}

	void RecordVariants() {
		CaseConstantList();
		Expect(24);
		Expect(17);
		TypeDescriptor type = RecordFieldList();
		Expect(18);
	}

	void CaseConstantList() {
		ConstantDescriptor constant = Constant();
		while (la.kind == 8) {
			Get();
			constant = Constant();
		}
	}

	ConstantDescriptor  Constant() {
		ConstantDescriptor  constant;
		constant = null; String sign = ""; 
		if (la.kind == 27 || la.kind == 28) {
			if (la.kind == 27) {
				Get();
			} else {
				Get();
				sign = t.val; 
			}
		}
		if (la.kind == 3) {
			long value = UnsignedIntegerLiteral();
			constant = factory.createLongConstant(sign, value); 
		} else if (la.kind == 4) {
			double value = UnsignedDoubleLiteral();
			constant = factory.createDoubleConstant(sign, value); 
		} else if (la.kind == 2) {
			String value = StringLiteral();
			constant = factory.createCharOrStringConstant(sign, value); 
		} else if (la.kind == 61 || la.kind == 62) {
			boolean value = LogicLiteral();
			constant = factory.createBooleanConstant(sign, value); 
		} else if (la.kind == 1) {
			Token identifier = IdentifierConstant();
			constant = factory.createConstantFromIdentifier(sign, identifier); 
		} else SynErr(72);
		return constant;
	}

	void ConstantDefinition() {
		Expect(1);
		Token identifier = t; 
		Expect(10);
		ConstantDescriptor constant = Constant();
		factory.registerConstant(identifier, constant); 
	}

	long  UnsignedIntegerLiteral() {
		long  value;
		Expect(3);
		value = factory.getLongFromToken(t); 
		return value;
	}

	double  UnsignedDoubleLiteral() {
		double  value;
		Expect(4);
		value = factory.getDoubleFromToken(t); 
		return value;
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
		if (la.kind == 61) {
			Get();
			result = true; 
		} else if (la.kind == 62) {
			Get();
			result = false; 
		} else SynErr(73);
		return result;
	}

	Token  IdentifierConstant() {
		Token  identifierToken;
		Expect(1);
		identifierToken = t; 
		return identifierToken;
	}

	List<String>  IdentifiersList() {
		List<String>  identifiers;
		identifiers = new ArrayList<>(); 
		Expect(1);
		identifiers.add(factory.getIdentifierFromToken(t)); 
		while (la.kind == 8) {
			Get();
			Expect(1);
			identifiers.add(factory.getIdentifierFromToken(t)); 
		}
		return identifiers;
	}

	void Procedure() {
		Expect(30);
		Expect(1);
		Token identifierToken = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 17) {
			formalParameters = FormalParameterList();
		}
		Expect(6);
		if (la.kind == 31) {
			Get();
			factory.forwardProcedure(identifierToken, formalParameters); 
		} else if (StartOf(2)) {
			factory.startProcedureImplementation(identifierToken, formalParameters); 
			Declarations();
			StatementNode bodyNode = Block();
			factory.finishProcedureImplementation(bodyNode); 
		} else SynErr(74);
	}

	void Function() {
		Expect(32);
		Expect(1);
		Token identifierToken = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 17) {
			formalParameters = FormalParameterList();
		}
		Expect(24);
		Expect(1);
		Token returnTypeToken = t; 
		Expect(6);
		if (la.kind == 31) {
			Get();
			factory.forwardFunction(identifierToken, formalParameters, returnTypeToken); 
		} else if (StartOf(2)) {
			factory.startFunctionImplementation(identifierToken, formalParameters, returnTypeToken); 
			Declarations();
			StatementNode bodyNode = Block();
			factory.finishFunctionImplementation(bodyNode); 
		} else SynErr(75);
	}

	List<FormalParameter>  FormalParameterList() {
		List<FormalParameter>  formalParameters;
		Expect(17);
		formalParameters = new ArrayList<>(); 
		List<FormalParameter> newParameters = new ArrayList<>(); 
		newParameters = FormalParameter();
		factory.appendFormalParameter(newParameters, formalParameters); 
		while (la.kind == 6) {
			Get();
			newParameters = FormalParameter();
			factory.appendFormalParameter(newParameters, formalParameters); 
		}
		Expect(18);
		return formalParameters;
	}

	StatementNode  Block() {
		StatementNode  blockNode;
		Expect(34);
		List<StatementNode> bodyNodes = new ArrayList<>(); 
		if (StartOf(3)) {
			StatementSequence(bodyNodes);
		}
		Expect(21);
		blockNode = factory.createBlockNode(bodyNodes); 
		return blockNode;
	}

	List<FormalParameter>  FormalParameter() {
		List<FormalParameter>  formalParameter;
		List<String> identifiers = new ArrayList<>(); 
		boolean isReference = false; 
		if (la.kind == 29) {
			Get();
			isReference = true; 
		}
		Expect(1);
		identifiers.add(factory.getIdentifierFromToken(t)); 
		while (la.kind == 8) {
			Get();
			Expect(1);
			identifiers.add(factory.getIdentifierFromToken(t)); 
		}
		Expect(24);
		Expect(1);
		formalParameter = factory.createFormalParametersList(identifiers, factory.getTypeNameFromToken(t), isReference); 
		return formalParameter;
	}

	void StatementSequence(List<StatementNode> body ) {
		StatementNode statement = Statement();
		body.add(statement); 
		while (la.kind == 6) {
			Get();
			statement = Statement();
			body.add(statement); 
		}
	}

	StatementNode  Statement() {
		StatementNode  statement;
		statement = null; 
		switch (la.kind) {
		case 6: case 21: case 37: case 43: {
			statement = factory.createNopStatement(); 
			break;
		}
		case 45: {
			statement = IfStatement();
			break;
		}
		case 38: {
			statement = ForLoop();
			break;
		}
		case 44: {
			statement = WhileLoop();
			break;
		}
		case 42: {
			statement = RepeatLoop();
			break;
		}
		case 23: {
			statement = CaseStatement();
			break;
		}
		case 35: {
			Get();
			statement = factory.createBreak(); 
			break;
		}
		case 34: {
			statement = Block();
			break;
		}
		case 1: {
			statement = IdentifierBeginningStatement();
			break;
		}
		default: SynErr(76); break;
		}
		return statement;
	}

	StatementNode  IfStatement() {
		StatementNode  statement;
		Expect(45);
		ExpressionNode condition = Expression();
		Expect(46);
		StatementNode thenStatement = Statement();
		StatementNode elseStatement = null; 
		if (la.kind == 37) {
			Get();
			elseStatement = Statement();
		}
		statement = factory.createIfStatement(condition, thenStatement, elseStatement); 
		return statement;
	}

	StatementNode  ForLoop() {
		StatementNode  statement;
		factory.startLoop(); 
		Expect(38);
		boolean ascending = true; 
		Expect(1);
		Token variableToken = t; 
		Expect(36);
		ExpressionNode startValue = Expression();
		if (la.kind == 39) {
			Get();
			ascending = true; 
		} else if (la.kind == 40) {
			Get();
			ascending = false; 
		} else SynErr(77);
		ExpressionNode finalValue = Expression();
		Expect(41);
		StatementNode loopBody = Statement();
		statement = factory.createForLoop(ascending, variableToken, startValue, finalValue, loopBody); 
		factory.finishLoop(); 
		return statement;
	}

	StatementNode  WhileLoop() {
		StatementNode  statement;
		factory.startLoop(); 
		Expect(44);
		ExpressionNode condition = Expression();
		Expect(41);
		StatementNode loopBody = Statement();
		statement = factory.createWhileLoop(condition, loopBody); 
		factory.finishLoop(); 
		return statement;
	}

	StatementNode  RepeatLoop() {
		StatementNode  statement;
		factory.startLoop(); 
		Expect(42);
		List<StatementNode> bodyNodes = new ArrayList<>(); 
		StatementSequence(bodyNodes);
		Expect(43);
		ExpressionNode condition = Expression();
		statement = factory.createRepeatLoop(condition, factory.createBlockNode(bodyNodes)); 
		factory.finishLoop(); 
		return statement;
	}

	StatementNode  CaseStatement() {
		StatementNode  statement;
		Expect(23);
		ExpressionNode caseExpression = Expression();
		Expect(11);
		CaseStatementData caseData = CaseList();
		caseData.caseExpression = caseExpression; 
		Expect(21);
		statement = factory.createCaseStatement(caseData); 
		return statement;
	}

	StatementNode  IdentifierBeginningStatement() {
		StatementNode  statement;
		statement = null; 
		Expect(1);
		Token identifierToken = t; 
		if (StartOf(4)) {
			statement = factory.createSubroutineCall(identifierToken, new ArrayList<>()); 
		} else if (la.kind == 17) {
			statement = SubroutineCall(identifierToken);
		} else if (la.kind == 15 || la.kind == 33 || la.kind == 36) {
			List<AccessRouteNode> accessRoute  = InnerAccessRoute();
			Expect(36);
			ExpressionNode value = Expression();
			statement = factory.createAssignmentWithRoute(identifierToken, accessRoute, value); 
		} else SynErr(78);
		return statement;
	}

	ExpressionNode  SubroutineCall(Token identifierToken) {
		ExpressionNode  expression;
		Expect(17);
		List<ExpressionNode> parameters = new ArrayList<>(); 
		if (StartOf(5)) {
			parameters = ActualParameters(identifierToken);
		}
		Expect(18);
		expression = factory.createSubroutineCall(identifierToken, parameters); 
		return expression;
	}

	List<AccessRouteNode>  InnerAccessRoute() {
		List<AccessRouteNode>  accessRoute;
		accessRoute = new ArrayList<>(); 
		while (la.kind == 15 || la.kind == 33) {
			AccessRouteNode element = InnerAccessRouteElement();
			accessRoute.add(element); 
		}
		return accessRoute;
	}

	ExpressionNode  Expression() {
		ExpressionNode  expression;
		expression = LogicTerm();
		while (la.kind == 47) {
			Get();
			Token op = t; 
			ExpressionNode right = LogicTerm();
			expression = factory.createBinaryExpression(op, expression, right); 
		}
		return expression;
	}

	AccessRouteNode  InnerAccessRouteElement() {
		AccessRouteNode  element;
		element = null; 
		if (la.kind == 15) {
			List<ExpressionNode> indexNodes  = ArrayIndex();
			element = new AccessRouteNode.ArrayIndex(indexNodes); 
		} else if (la.kind == 33) {
			Get();
			Expect(1);
			String variableIdentifier = factory.getIdentifierFromToken(t); 
			element = new AccessRouteNode.EnterRecord(variableIdentifier); 
		} else SynErr(79);
		return element;
	}

	List<AccessRouteNode>  InnerAccessRouteNonEmpty() {
		List<AccessRouteNode>  accessRoute;
		accessRoute = new ArrayList<>(); 
		AccessRouteNode element = InnerAccessRouteElement();
		accessRoute.add(element); 
		while (la.kind == 15 || la.kind == 33) {
			element = InnerAccessRouteElement();
			accessRoute.add(element); 
		}
		return accessRoute;
	}

	List<ExpressionNode>  ArrayIndex() {
		List<ExpressionNode>  indexNodes;
		indexNodes = new ArrayList<>(); 
		ExpressionNode indexingNode = null; 
		Expect(15);
		indexingNode = Expression();
		indexNodes.add(indexingNode); 
		while (la.kind == 8) {
			Get();
			indexingNode = Expression();
			indexNodes.add(indexingNode); 
		}
		Expect(16);
		return indexNodes;
	}

	CaseStatementData  CaseList() {
		CaseStatementData  data;
		data = new CaseStatementData(); 
		ExpressionNode caseConstant = Expression();
		data.indexNodes.add(caseConstant); 
		Expect(24);
		StatementNode caseStatement = Statement();
		data.statementNodes.add(caseStatement); 
		while (!caseEnds()) {
			Expect(6);
			caseConstant = Expression();
			data.indexNodes.add(caseConstant); 
			Expect(24);
			caseStatement = Statement();
			data.statementNodes.add(caseStatement); 
		}
		if (la.kind == 6) {
			Get();
		}
		if (la.kind == 37) {
			Get();
			data.elseNode = Statement();
		}
		if (la.kind == 6) {
			Get();
		}
		return data;
	}

	ExpressionNode  LogicTerm() {
		ExpressionNode  expression;
		expression = SignedLogicFactor();
		while (la.kind == 48) {
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
		if (la.kind == 49) {
			Get();
			Token op = t; 
			ExpressionNode right = SignedLogicFactor();
			expression = factory.createUnaryExpression(op, right); 
		} else if (StartOf(6)) {
			expression = LogicFactor();
		} else SynErr(80);
		return expression;
	}

	ExpressionNode  LogicFactor() {
		ExpressionNode  expression;
		expression = Arithmetic();
		if (StartOf(7)) {
			switch (la.kind) {
			case 50: {
				Get();
				break;
			}
			case 51: {
				Get();
				break;
			}
			case 52: {
				Get();
				break;
			}
			case 53: {
				Get();
				break;
			}
			case 10: {
				Get();
				break;
			}
			case 54: {
				Get();
				break;
			}
			case 55: {
				Get();
				break;
			}
			case 56: {
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
		while (la.kind == 27 || la.kind == 28) {
			if (la.kind == 27) {
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
		while (StartOf(8)) {
			if (la.kind == 57) {
				Get();
			} else if (la.kind == 58) {
				Get();
			} else if (la.kind == 59) {
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
		if (la.kind == 27 || la.kind == 28) {
			if (la.kind == 27) {
				Get();
			} else {
				Get();
			}
			Token unOp = t; 
			expression = SignedFactor();
			expression = factory.createUnaryExpression(unOp, expression); 
		} else if (StartOf(9)) {
			expression = Factor();
		} else SynErr(81);
		return expression;
	}

	ExpressionNode  Factor() {
		ExpressionNode  expression;
		expression = null; 
		switch (la.kind) {
		case 1: {
			expression = IdentifierAccess();
			break;
		}
		case 17: {
			Get();
			expression = Expression();
			Expect(18);
			break;
		}
		case 2: {
			String value = ""; 
			value = StringLiteral();
			expression = factory.createCharOrStringLiteralNode(value); 
			break;
		}
		case 4: {
			Get();
			expression = factory.createFloatLiteralNode(t); 
			break;
		}
		case 3: {
			Get();
			expression = factory.createNumericLiteralNode(t); 
			break;
		}
		case 61: case 62: {
			boolean val; 
			val = LogicLiteral();
			expression = factory.createLogicLiteralNode(val); 
			break;
		}
		case 15: {
			expression = SetConstructor();
			break;
		}
		default: SynErr(82); break;
		}
		return expression;
	}

	ExpressionNode  IdentifierAccess() {
		ExpressionNode  expression;
		Expect(1);
		Token identifierToken = t; expression = null; 
		if (StartOf(10)) {
			expression = factory.createExpressionFromSingleIdentifier(identifierToken); 
		} else if (la.kind == 15 || la.kind == 17 || la.kind == 33) {
			expression = InnerIdentifierAccess(identifierToken);
		} else SynErr(83);
		return expression;
	}

	ExpressionNode  SetConstructor() {
		ExpressionNode  expression;
		expression = null; 
		Expect(15);
		if (la.kind == 16) {
			expression = factory.createSetConstructorNode(new ArrayList<>()); 
		} else if (StartOf(5)) {
			List<ExpressionNode> valueNodes = new ArrayList<ExpressionNode>(); 
			ExpressionNode valueNode = Expression();
			valueNodes.add(valueNode); 
			while (la.kind == 8) {
				Get();
				valueNode = Expression();
				valueNodes.add(valueNode); 
			}
			expression = factory.createSetConstructorNode(valueNodes); 
		} else SynErr(84);
		Expect(16);
		return expression;
	}

	ExpressionNode  InnerIdentifierAccess(Token identifierToken) {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 17) {
			expression = SubroutineCall(identifierToken);
		} else if (la.kind == 15 || la.kind == 33) {
			List<AccessRouteNode> accessRoute  = InnerAccessRouteNonEmpty();
			expression = factory.createExpressionFromIdentifierWithRoute(identifierToken, accessRoute); 
		} else SynErr(85);
		return expression;
	}

	List<ExpressionNode>  ActualParameters(Token subroutineToken ) {
		List<ExpressionNode>  parameters;
		parameters = new ArrayList<>(); 
		int currentParameter = 0; 
		ExpressionNode parameter = ActualParameter(currentParameter, subroutineToken);
		parameters.add(parameter); 
		while (la.kind == 8) {
			Get();
			parameter = ActualParameter(++currentParameter, subroutineToken);
			parameters.add(parameter); 
		}
		return parameters;
	}

	ExpressionNode  ActualParameter(int currentParameterIndex, Token subroutineToken) {
		ExpressionNode  parameter;
		parameter = null; 
		if (factory.shouldBeReference(subroutineToken, currentParameterIndex)) {
			Expect(1);
			parameter = factory.createReferenceNode(t); 
		} else if (StartOf(5)) {
			parameter = Expression();
		} else SynErr(86);
		return parameter;
	}

	void UnitHeader() {
		Expect(63);
		Expect(1);
		factory.startUnit(t); 
		Expect(6);
	}

	void InterfaceSection() {
		Expect(64);
		while (StartOf(11)) {
			if (la.kind == 30) {
				ProcedureHeading();
			} else if (la.kind == 32) {
				FunctionHeading();
			} else if (la.kind == 9) {
				TypeDefinitions();
			} else {
				VariableDeclarations();
			}
		}
	}

	void ImplementationSection() {
		Expect(65);
		while (StartOf(11)) {
			if (la.kind == 30 || la.kind == 32) {
				UnitSubroutineImplementation();
			} else if (la.kind == 29) {
				VariableDeclarations();
			} else {
				TypeDefinitions();
			}
		}
	}

	void UnitFooter() {
		Expect(21);
		factory.endUnit(); 
		Expect(33);
	}

	void ProcedureHeading() {
		Expect(30);
		Expect(1);
		Token name = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 17) {
			formalParameters = FormalParameterList();
		}
		factory.addUnitProcedureInterface(name, formalParameters); 
		Expect(6);
	}

	void FunctionHeading() {
		Expect(32);
		Expect(1);
		Token name = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 17) {
			formalParameters = FormalParameterList();
		}
		Expect(24);
		Expect(1);
		String returnTypeName = factory.getTypeNameFromToken(t); 
		factory.addUnitFunctionInterface(name, formalParameters, returnTypeName); 
		Expect(6);
	}

	void UnitSubroutineImplementation() {
		if (la.kind == 30) {
			UnitProcedureImplementation();
			Expect(6);
		} else if (la.kind == 32) {
			UnitFunctionImplementation();
			Expect(6);
		} else SynErr(87);
	}

	void UnitProcedureImplementation() {
		Expect(30);
		Expect(1);
		Token identifierToken = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 17) {
			formalParameters = FormalParameterList();
		}
		Expect(6);
		factory.startUnitProcedureImplementation(identifierToken, formalParameters); 
		Declarations();
		StatementNode bodyNode = Block();
		factory.finishProcedureImplementation(bodyNode); 
	}

	void UnitFunctionImplementation() {
		Expect(32);
		Expect(1);
		Token identifierToken = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 17) {
			formalParameters = FormalParameterList();
		}
		Expect(24);
		Expect(1);
		Token returnTypeToken = t; 
		Expect(6);
		factory.startUnitFunctionImplementation(identifierToken, formalParameters, returnTypeToken); 
		Declarations();
		StatementNode bodyNode = Block();
		factory.finishFunctionImplementation(bodyNode); 
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
		{_T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_x, _x,_x,_x,_x, _T,_T,_T,_x, _x,_T,_x,_T, _T,_x,_T,_x, _x,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_T,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_T,_T,_x, _T,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_T,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_T, _x,_x,_T,_x, _x,_x,_T,_x, _T,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_T,_x, _x,_x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_T,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_T, _T,_T,_T,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_T,_T, _T,_x,_x,_x, _x,_x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_T,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_T,_x, _T,_x,_T,_T, _x,_x,_x,_x, _T,_x,_T,_x, _x,_T,_x,_x, _T,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_T, _T,_T,_x,_T, _x,_x,_T,_T, _T,_x,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_x,_x,_x, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_T,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x}

	};

	public boolean isUsingTPExtension() {
	    return true;
	}
	
    public boolean hadErrors(){
    	return errors.count > 0;
    }

    public RootNode getRootNode() {
        return this.mainNode;
    }
    
    public boolean caseEnds(){
        if(la.val.toLowerCase().equals("end") && !t.val.toLowerCase().equals(":"))
            return true;

        if(la.val.toLowerCase().equals("else"))
            return true;

        else if(la.val.toLowerCase().equals(";")){
            Token next = scanner.Peek();
            scanner.ResetPeek();
            return next.val.toLowerCase().equals("end") || next.val.toLowerCase().equals("else");
        }

        return false;
    }

    public boolean continuesArray() {
        Token next = scanner.Peek();
        scanner.ResetPeek();
        if(next.val.toLowerCase().equals("array") || (next.val.toLowerCase().equals("packed")))
            return true;

        return false;
    }

    public boolean isSubrange() {
        Token next = scanner.Peek();
        scanner.ResetPeek();
        return next.val.toLowerCase().equals("..");
    }

    public  boolean recordFixedPartContinues() {
        String actual = la.val.toLowerCase();
        String next = scanner.Peek().val.toLowerCase();
        scanner.ResetPeek();

        boolean variantPartStarts = actual.equals(";") && next.equals("case");
        boolean innerFieldListEnds = actual.equals(")") || (actual.equals(";") && next.equals(")") );
        boolean recordsEnds = actual.equals("end") || (actual.equals(";") && next.equals("end") );

        return !variantPartStarts && !recordsEnds && !innerFieldListEnds;
    }

    public boolean isVariantSelectorTag() {
        Token next = scanner.Peek();
        scanner.ResetPeek();

        return next.val.equals(":");
    }

    public boolean recordVariantPartStarts() {
        Token next = scanner.Peek();
        scanner.ResetPeek();

        return next.val.equals("case");
    }

} // end Parser

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
			case 5: s = "\"program\" expected"; break;
			case 6: s = "\";\" expected"; break;
			case 7: s = "\"uses\" expected"; break;
			case 8: s = "\",\" expected"; break;
			case 9: s = "\"type\" expected"; break;
			case 10: s = "\"=\" expected"; break;
			case 11: s = "\"of\" expected"; break;
			case 12: s = "\"set\" expected"; break;
			case 13: s = "\"packed\" expected"; break;
			case 14: s = "\"array\" expected"; break;
			case 15: s = "\"[\" expected"; break;
			case 16: s = "\"]\" expected"; break;
			case 17: s = "\"(\" expected"; break;
			case 18: s = "\")\" expected"; break;
			case 19: s = "\"file\" expected"; break;
			case 20: s = "\"record\" expected"; break;
			case 21: s = "\"end\" expected"; break;
			case 22: s = "\"^\" expected"; break;
			case 23: s = "\"case\" expected"; break;
			case 24: s = "\":\" expected"; break;
			case 25: s = "\"..\" expected"; break;
			case 26: s = "\"const\" expected"; break;
			case 27: s = "\"+\" expected"; break;
			case 28: s = "\"-\" expected"; break;
			case 29: s = "\"var\" expected"; break;
			case 30: s = "\"procedure\" expected"; break;
			case 31: s = "\"forward\" expected"; break;
			case 32: s = "\"function\" expected"; break;
			case 33: s = "\".\" expected"; break;
			case 34: s = "\"begin\" expected"; break;
			case 35: s = "\"break\" expected"; break;
			case 36: s = "\":=\" expected"; break;
			case 37: s = "\"else\" expected"; break;
			case 38: s = "\"for\" expected"; break;
			case 39: s = "\"to\" expected"; break;
			case 40: s = "\"downto\" expected"; break;
			case 41: s = "\"do\" expected"; break;
			case 42: s = "\"repeat\" expected"; break;
			case 43: s = "\"until\" expected"; break;
			case 44: s = "\"while\" expected"; break;
			case 45: s = "\"if\" expected"; break;
			case 46: s = "\"then\" expected"; break;
			case 47: s = "\"or\" expected"; break;
			case 48: s = "\"and\" expected"; break;
			case 49: s = "\"not\" expected"; break;
			case 50: s = "\">\" expected"; break;
			case 51: s = "\">=\" expected"; break;
			case 52: s = "\"<\" expected"; break;
			case 53: s = "\"<=\" expected"; break;
			case 54: s = "\"<>\" expected"; break;
			case 55: s = "\"in\" expected"; break;
			case 56: s = "\"><\" expected"; break;
			case 57: s = "\"*\" expected"; break;
			case 58: s = "\"/\" expected"; break;
			case 59: s = "\"div\" expected"; break;
			case 60: s = "\"mod\" expected"; break;
			case 61: s = "\"true\" expected"; break;
			case 62: s = "\"false\" expected"; break;
			case 63: s = "\"unit\" expected"; break;
			case 64: s = "\"interface\" expected"; break;
			case 65: s = "\"implementation\" expected"; break;
			case 66: s = "??? expected"; break;
			case 67: s = "invalid Pascal"; break;
			case 68: s = "invalid Subroutine"; break;
			case 69: s = "invalid Type"; break;
			case 70: s = "invalid Ordinal"; break;
			case 71: s = "invalid RecordFieldList"; break;
			case 72: s = "invalid Constant"; break;
			case 73: s = "invalid LogicLiteral"; break;
			case 74: s = "invalid Procedure"; break;
			case 75: s = "invalid Function"; break;
			case 76: s = "invalid Statement"; break;
			case 77: s = "invalid ForLoop"; break;
			case 78: s = "invalid IdentifierBeginningStatement"; break;
			case 79: s = "invalid InnerAccessRouteElement"; break;
			case 80: s = "invalid SignedLogicFactor"; break;
			case 81: s = "invalid SignedFactor"; break;
			case 82: s = "invalid Factor"; break;
			case 83: s = "invalid IdentifierAccess"; break;
			case 84: s = "invalid SetConstructor"; break;
			case 85: s = "invalid InnerIdentifierAccess"; break;
			case 86: s = "invalid ActualParameter"; break;
			case 87: s = "invalid UnitSubroutineImplementation"; break;
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
