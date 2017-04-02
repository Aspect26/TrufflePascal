
package cz.cuni.mff.d3s.trupple.parser.tp;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.nodes.RootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute.*;
import cz.cuni.mff.d3s.trupple.language.nodes.*;
import com.oracle.truffle.api.source.Source;
import cz.cuni.mff.d3s.trupple.parser.*;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.ConstantDescriptor;
import java.util.ArrayList;
import java.util.List;

public class Parser implements IParser {
	public static final int _EOF = 0;
	public static final int _identifier = 1;
	public static final int _stringLiteral = 2;
	public static final int _integerLiteral = 3;
	public static final int _doubleLiteral = 4;
	public static final int maxT = 67;

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
			if (la.kind == 9) {
				ImportsSection();
			}
			Declarations();
			MainFunction();
		} else if (la.kind == 64) {
			Unit();
		} else SynErr(68);
	}

	void Program() {
		Expect(5);
		Expect(1);
		factory.startPascal(t); 
		List<String> programArgumentsIdentifiers = new ArrayList<>(); 
		if (la.kind == 6) {
			Get();
			programArgumentsIdentifiers = IdentifiersList();
			Expect(7);
		}
		factory.setMainProgramArguments(programArgumentsIdentifiers); 
		Expect(8);
	}

	void ImportsSection() {
		Expect(9);
		Expect(1);
		factory.registerUnit(t); 
		while (la.kind == 10) {
			Get();
			Expect(1);
			factory.registerUnit(t); 
		}
		Expect(8);
	}

	void Declarations() {
		while (StartOf(1)) {
			Declaration();
		}
	}

	void MainFunction() {
		BlockNode blockNode = Block();
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

	List<String>  IdentifiersList() {
		List<String>  identifiers;
		identifiers = new ArrayList<>(); 
		Expect(1);
		identifiers.add(factory.getIdentifierFromToken(t)); 
		while (la.kind == 10) {
			Get();
			Expect(1);
			identifiers.add(factory.getIdentifierFromToken(t)); 
		}
		return identifiers;
	}

	void Declaration() {
		if (la.kind == 26) {
			ConstantDefinitions();
		} else if (la.kind == 11) {
			TypeDefinitions();
		} else if (la.kind == 29) {
			VariableDeclarations();
		} else if (la.kind == 30 || la.kind == 32) {
			Subroutine();
		} else SynErr(69);
	}

	void ConstantDefinitions() {
		Expect(26);
		ConstantDefinition();
		Expect(8);
		while (la.kind == 1) {
			ConstantDefinition();
			Expect(8);
		}
	}

	void TypeDefinitions() {
		Expect(11);
		TypeDefinition();
		Expect(8);
		while (la.kind == 1) {
			TypeDefinition();
			Expect(8);
		}
		factory.initializeAllUninitializedPointerDescriptors(); 
	}

	void VariableDeclarations() {
		Expect(29);
		VariableLineDeclaration();
		Expect(8);
		while (la.kind == 1) {
			VariableLineDeclaration();
			Expect(8);
		}
	}

	void Subroutine() {
		if (la.kind == 30) {
			Procedure();
			Expect(8);
		} else if (la.kind == 32) {
			Function();
			Expect(8);
		} else SynErr(70);
	}

	void TypeDefinition() {
		Expect(1);
		Token identifier = t; 
		Expect(12);
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
		} else if (la.kind == 6) {
			typeDescriptor = EnumDefinition();
		} else if (la.kind == 15 || la.kind == 16) {
			List<OrdinalDescriptor> ordinalDimensions  = ArrayDefinition();
			while (continuesArray()) {
				Expect(13);
				List<OrdinalDescriptor> additionalDimensions  = ArrayDefinition();
				ordinalDimensions.addAll(additionalDimensions); 
			}
			Expect(13);
			Expect(1);
			typeDescriptor = factory.createArray(ordinalDimensions, t); 
		} else if (la.kind == 14) {
			Get();
			Expect(13);
			OrdinalDescriptor ordinal = Ordinal();
			typeDescriptor = factory.createSetType(ordinal); 
		} else if (la.kind == 19) {
			typeDescriptor = FileType();
		} else if (la.kind == 20) {
			typeDescriptor = RecordType();
		} else if (la.kind == 22) {
			typeDescriptor = PointerType();
		} else SynErr(71);
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
		Expect(6);
		Expect(1);
		enumIdentifiers.add(factory.getIdentifierFromToken(t)); 
		while (la.kind == 10) {
			Get();
			Expect(1);
			enumIdentifiers.add(factory.getIdentifierFromToken(t)); 
		}
		Expect(7);
		typeDescriptor = factory.registerEnum(enumIdentifiers); 
		return typeDescriptor;
	}

	List<OrdinalDescriptor>  ArrayDefinition() {
		List<OrdinalDescriptor>  ordinalDimensions;
		if (la.kind == 15) {
			Get();
		}
		Expect(16);
		ordinalDimensions = new ArrayList<>(); 
		Expect(17);
		OrdinalDescriptor ordinalDescriptor = null; 
		ordinalDescriptor = Ordinal();
		ordinalDimensions.add(ordinalDescriptor); 
		while (la.kind == 10) {
			Get();
			ordinalDescriptor = Ordinal();
			ordinalDimensions.add(ordinalDescriptor); 
		}
		Expect(18);
		return ordinalDimensions;
	}

	OrdinalDescriptor  Ordinal() {
		OrdinalDescriptor  ordinal;
		ordinal = null; 
		if (isSubrange()) {
			ordinal = SubrangeType();
		} else if (StartOf(2)) {
			TypeDescriptor typeDescriptor = Type();
			ordinal = factory.castTypeToOrdinalType(typeDescriptor); 
		} else SynErr(72);
		return ordinal;
	}

	TypeDescriptor  FileType() {
		TypeDescriptor  fileDescriptor;
		Expect(19);
		Expect(13);
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
		Expect(1);
		typeDescriptor = factory.createPointerType(t); 
		return typeDescriptor;
	}

	TypeDescriptor  RecordFieldList() {
		TypeDescriptor  typeDescriptor;
		if (la.kind == 1) {
			RecordFixedPart();
			if (recordVariantPartStarts()) {
				Expect(8);
				RecordVariantPart();
			}
		} else if (la.kind == 23) {
			RecordVariantPart();
		} else SynErr(73);
		if (la.kind == 8) {
			Get();
		}
		typeDescriptor = factory.createRecordType(); 
		return typeDescriptor;
	}

	void RecordFixedPart() {
		RecordFixedSection();
		while (recordFixedPartContinues()) {
			Expect(8);
			RecordFixedSection();
		}
	}

	void RecordVariantPart() {
		Expect(23);
		RecordVariantSelector();
		Expect(13);
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
		Expect(6);
		TypeDescriptor type = RecordFieldList();
		Expect(7);
	}

	void CaseConstantList() {
		ConstantDescriptor constant = Constant();
		while (la.kind == 10) {
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
		} else if (la.kind == 62 || la.kind == 63) {
			boolean value = LogicLiteral();
			constant = factory.createBooleanConstant(sign, value); 
		} else if (la.kind == 1) {
			Token identifier = IdentifierConstant();
			constant = factory.createConstantFromIdentifier(sign, identifier); 
		} else SynErr(74);
		return constant;
	}

	void ConstantDefinition() {
		Expect(1);
		Token identifier = t; 
		Expect(12);
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
		if (la.kind == 62) {
			Get();
			result = true; 
		} else if (la.kind == 63) {
			Get();
			result = false; 
		} else SynErr(75);
		return result;
	}

	Token  IdentifierConstant() {
		Token  identifierToken;
		Expect(1);
		identifierToken = t; 
		return identifierToken;
	}

	void Procedure() {
		Expect(30);
		Expect(1);
		Token identifierToken = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 6) {
			formalParameters = FormalParameterList();
		}
		Expect(8);
		if (la.kind == 31) {
			Get();
			factory.forwardProcedure(identifierToken, formalParameters); 
		} else if (StartOf(3)) {
			factory.startProcedureImplementation(identifierToken, formalParameters); 
			Declarations();
			StatementNode bodyNode = Block();
			factory.finishProcedureImplementation(bodyNode); 
		} else SynErr(76);
	}

	void Function() {
		Expect(32);
		Expect(1);
		Token identifierToken = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 6) {
			formalParameters = FormalParameterList();
		}
		Expect(24);
		Expect(1);
		Token returnTypeToken = t; 
		Expect(8);
		if (la.kind == 31) {
			Get();
			factory.forwardFunction(identifierToken, formalParameters, returnTypeToken); 
		} else if (StartOf(3)) {
			factory.startFunctionImplementation(identifierToken, formalParameters, returnTypeToken); 
			Declarations();
			StatementNode bodyNode = Block();
			factory.finishFunctionImplementation(bodyNode); 
		} else SynErr(77);
	}

	List<FormalParameter>  FormalParameterList() {
		List<FormalParameter>  formalParameters;
		Expect(6);
		formalParameters = new ArrayList<>(); 
		List<FormalParameter> newParameters = new ArrayList<>(); 
		newParameters = FormalParameter();
		factory.appendFormalParameter(newParameters, formalParameters); 
		while (la.kind == 8) {
			Get();
			newParameters = FormalParameter();
			factory.appendFormalParameter(newParameters, formalParameters); 
		}
		Expect(7);
		return formalParameters;
	}

	BlockNode  Block() {
		BlockNode  blockNode;
		Expect(34);
		List<StatementNode> bodyNodes = new ArrayList<>(); 
		if (StartOf(4)) {
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
		while (la.kind == 10) {
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
		while (la.kind == 8) {
			Get();
			statement = Statement();
			body.add(statement); 
		}
	}

	StatementNode  Statement() {
		StatementNode  statement;
		statement = null; 
		switch (la.kind) {
		case 8: case 21: case 37: case 44: {
			statement = factory.createNopStatement(); 
			break;
		}
		case 46: {
			statement = IfStatement();
			break;
		}
		case 40: {
			statement = ForLoop();
			break;
		}
		case 45: {
			statement = WhileLoop();
			break;
		}
		case 43: {
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
		case 38: {
			statement = WithStatement();
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
		default: SynErr(78); break;
		}
		return statement;
	}

	StatementNode  IfStatement() {
		StatementNode  statement;
		Expect(46);
		ExpressionNode condition = Expression();
		Expect(47);
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
		Expect(40);
		boolean ascending = true; 
		Expect(1);
		Token variableToken = t; 
		Expect(36);
		ExpressionNode startValue = Expression();
		if (la.kind == 41) {
			Get();
			ascending = true; 
		} else if (la.kind == 42) {
			Get();
			ascending = false; 
		} else SynErr(79);
		ExpressionNode finalValue = Expression();
		Expect(39);
		StatementNode loopBody = Statement();
		statement = factory.createForLoop(ascending, variableToken, startValue, finalValue, loopBody); 
		factory.finishLoop(); 
		return statement;
	}

	StatementNode  WhileLoop() {
		StatementNode  statement;
		factory.startLoop(); 
		Expect(45);
		ExpressionNode condition = Expression();
		Expect(39);
		StatementNode loopBody = Statement();
		statement = factory.createWhileLoop(condition, loopBody); 
		factory.finishLoop(); 
		return statement;
	}

	StatementNode  RepeatLoop() {
		StatementNode  statement;
		factory.startLoop(); 
		Expect(43);
		List<StatementNode> bodyNodes = new ArrayList<>(); 
		StatementSequence(bodyNodes);
		Expect(44);
		ExpressionNode condition = Expression();
		statement = factory.createRepeatLoop(condition, factory.createBlockNode(bodyNodes)); 
		factory.finishLoop(); 
		return statement;
	}

	StatementNode  CaseStatement() {
		StatementNode  statement;
		Expect(23);
		ExpressionNode caseExpression = Expression();
		Expect(13);
		CaseStatementData caseData = CaseList();
		caseData.caseExpression = caseExpression; 
		Expect(21);
		statement = factory.createCaseStatement(caseData); 
		return statement;
	}

	StatementNode  WithStatement() {
		StatementNode  statement;
		Expect(38);
		List<String> recordIdentifiers  = IdentifiersList();
		LexicalScope initialScope = factory.getScope(); 
		List<FrameSlot> recordSlots = factory.stepIntoRecordsScope(recordIdentifiers); 
		Expect(39);
		StatementNode innerStatement = Statement();
		factory.setScope(initialScope); 
		statement = factory.createWithStatement(recordSlots, innerStatement); 
		return statement;
	}

	StatementNode  IdentifierBeginningStatement() {
		StatementNode  statement;
		statement = null; 
		Expect(1);
		Token identifierToken = t; 
		if (StartOf(5)) {
			statement = factory.createSubroutineCall(identifierToken, new ArrayList<>()); 
		} else if (la.kind == 6) {
			statement = SubroutineCall(identifierToken);
		} else if (StartOf(6)) {
			AccessNode accessNode = InnerAccessRoute(identifierToken);
			Expect(36);
			ExpressionNode value = Expression();
			statement = factory.createAssignmentWithRoute(identifierToken, accessNode, value); 
		} else SynErr(80);
		return statement;
	}

	ExpressionNode  SubroutineCall(Token identifierToken) {
		ExpressionNode  expression;
		Expect(6);
		List<ExpressionNode> parameters = new ArrayList<>(); 
		if (StartOf(7)) {
			parameters = ActualParameters(identifierToken);
		}
		Expect(7);
		expression = factory.createSubroutineCall(identifierToken, parameters); 
		return expression;
	}

	AccessNode  InnerAccessRoute(Token identifierToken) {
		AccessNode  accessRoute;
		accessRoute = factory.createSimpleAccessNode(identifierToken); 
		while (la.kind == 17 || la.kind == 22 || la.kind == 33) {
			accessRoute = InnerAccessRouteElement(accessRoute);
		}
		return accessRoute;
	}

	ExpressionNode  Expression() {
		ExpressionNode  expression;
		expression = LogicTerm();
		while (la.kind == 48) {
			Get();
			Token op = t; 
			ExpressionNode right = LogicTerm();
			expression = factory.createBinaryExpression(op, expression, right); 
		}
		return expression;
	}

	AccessNode  InnerAccessRouteElement(AccessNode previousAccessNode) {
		AccessNode  accessNode;
		accessNode = null; 
		if (la.kind == 17) {
			List<ExpressionNode> indexNodes  = ArrayIndex();
			accessNode = new ArrayAccessNode(previousAccessNode, indexNodes); 
		} else if (la.kind == 33) {
			Get();
			Expect(1);
			String variableIdentifier = factory.getIdentifierFromToken(t); 
			accessNode = new RecordAccessNode(previousAccessNode, variableIdentifier); 
		} else if (la.kind == 22) {
			Get();
			accessNode = new PointerDereference(previousAccessNode); 
		} else SynErr(81);
		return accessNode;
	}

	AccessNode  InnerAccessRouteNonEmpty(Token identifierToken) {
		AccessNode  accessRoute;
		accessRoute = factory.createSimpleAccessNode(identifierToken); 
		accessRoute = InnerAccessRouteElement(accessRoute);
		while (la.kind == 17 || la.kind == 22 || la.kind == 33) {
			accessRoute = InnerAccessRouteElement(accessRoute);
		}
		return accessRoute;
	}

	List<ExpressionNode>  ArrayIndex() {
		List<ExpressionNode>  indexNodes;
		indexNodes = new ArrayList<>(); 
		ExpressionNode indexingNode = null; 
		Expect(17);
		indexingNode = Expression();
		indexNodes.add(indexingNode); 
		while (la.kind == 10) {
			Get();
			indexingNode = Expression();
			indexNodes.add(indexingNode); 
		}
		Expect(18);
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
			Expect(8);
			caseConstant = Expression();
			data.indexNodes.add(caseConstant); 
			Expect(24);
			caseStatement = Statement();
			data.statementNodes.add(caseStatement); 
		}
		if (la.kind == 8) {
			Get();
		}
		if (la.kind == 37) {
			Get();
			data.elseNode = Statement();
		}
		if (la.kind == 8) {
			Get();
		}
		return data;
	}

	ExpressionNode  LogicTerm() {
		ExpressionNode  expression;
		expression = SignedLogicFactor();
		while (la.kind == 49) {
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
		if (la.kind == 50) {
			Get();
			Token op = t; 
			ExpressionNode right = SignedLogicFactor();
			expression = factory.createUnaryExpression(op, right); 
		} else if (StartOf(8)) {
			expression = LogicFactor();
		} else SynErr(82);
		return expression;
	}

	ExpressionNode  LogicFactor() {
		ExpressionNode  expression;
		expression = Arithmetic();
		if (StartOf(9)) {
			switch (la.kind) {
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
			case 54: {
				Get();
				break;
			}
			case 12: {
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
			case 57: {
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
		while (StartOf(10)) {
			if (la.kind == 58) {
				Get();
			} else if (la.kind == 59) {
				Get();
			} else if (la.kind == 60) {
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
		} else if (StartOf(11)) {
			expression = Factor();
		} else SynErr(83);
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
		case 6: {
			Get();
			expression = Expression();
			Expect(7);
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
		case 62: case 63: {
			boolean val; 
			val = LogicLiteral();
			expression = factory.createLogicLiteralNode(val); 
			break;
		}
		case 17: {
			expression = SetConstructor();
			break;
		}
		default: SynErr(84); break;
		}
		return expression;
	}

	ExpressionNode  IdentifierAccess() {
		ExpressionNode  expression;
		Expect(1);
		Token identifierToken = t; expression = null; 
		if (StartOf(12)) {
			expression = factory.createExpressionFromSingleIdentifier(identifierToken); 
		} else if (StartOf(13)) {
			expression = InnerIdentifierAccess(identifierToken);
		} else SynErr(85);
		return expression;
	}

	ExpressionNode  SetConstructor() {
		ExpressionNode  expression;
		expression = null; 
		Expect(17);
		if (la.kind == 18) {
			expression = factory.createSetConstructorNode(new ArrayList<>()); 
		} else if (StartOf(7)) {
			List<ExpressionNode> valueNodes = new ArrayList<ExpressionNode>(); 
			ExpressionNode valueNode = Expression();
			valueNodes.add(valueNode); 
			while (la.kind == 10) {
				Get();
				valueNode = Expression();
				valueNodes.add(valueNode); 
			}
			expression = factory.createSetConstructorNode(valueNodes); 
		} else SynErr(86);
		Expect(18);
		return expression;
	}

	ExpressionNode  InnerIdentifierAccess(Token identifierToken) {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 6) {
			expression = SubroutineCall(identifierToken);
		} else if (la.kind == 17 || la.kind == 22 || la.kind == 33) {
			AccessNode accessRoute = InnerAccessRouteNonEmpty(identifierToken);
			expression = factory.createExpressionFromIdentifierWithRoute(accessRoute); 
		} else SynErr(87);
		return expression;
	}

	List<ExpressionNode>  ActualParameters(Token subroutineToken ) {
		List<ExpressionNode>  parameters;
		parameters = new ArrayList<>(); 
		int currentParameter = 0; 
		ExpressionNode parameter = ActualParameter(currentParameter, subroutineToken);
		parameters.add(parameter); 
		while (la.kind == 10) {
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
		} else if (StartOf(7)) {
			parameter = Expression();
		} else SynErr(88);
		return parameter;
	}

	void UnitHeader() {
		Expect(64);
		Expect(1);
		factory.startUnit(t); 
		Expect(8);
	}

	void InterfaceSection() {
		Expect(65);
		while (StartOf(14)) {
			if (la.kind == 30) {
				ProcedureHeading();
			} else if (la.kind == 32) {
				FunctionHeading();
			} else if (la.kind == 11) {
				TypeDefinitions();
			} else {
				VariableDeclarations();
			}
		}
	}

	void ImplementationSection() {
		Expect(66);
		while (StartOf(14)) {
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
		if (la.kind == 6) {
			formalParameters = FormalParameterList();
		}
		factory.addUnitProcedureInterface(name, formalParameters); 
		Expect(8);
	}

	void FunctionHeading() {
		Expect(32);
		Expect(1);
		Token name = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 6) {
			formalParameters = FormalParameterList();
		}
		Expect(24);
		Expect(1);
		String returnTypeName = factory.getTypeNameFromToken(t); 
		factory.addUnitFunctionInterface(name, formalParameters, returnTypeName); 
		Expect(8);
	}

	void UnitSubroutineImplementation() {
		if (la.kind == 30) {
			UnitProcedureImplementation();
			Expect(8);
		} else if (la.kind == 32) {
			UnitFunctionImplementation();
			Expect(8);
		} else SynErr(89);
	}

	void UnitProcedureImplementation() {
		Expect(30);
		Expect(1);
		Token identifierToken = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 6) {
			formalParameters = FormalParameterList();
		}
		Expect(8);
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
		if (la.kind == 6) {
			formalParameters = FormalParameterList();
		}
		Expect(24);
		Expect(1);
		Token returnTypeToken = t; 
		Expect(8);
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
		{_T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_T,_T,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x},
		{_x,_T,_T,_T, _T,_x,_T,_x, _x,_x,_x,_x, _x,_x,_T,_T, _T,_x,_x,_T, _T,_x,_T,_x, _x,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_T, _x,_x,_x,_x, _x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_T,_T,_x, _T,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x},
		{_x,_T,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_T, _x,_x,_T,_x, _T,_x,_x,_T, _x,_T,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x},
		{_x,_T,_T,_T, _T,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_T, _x,_x,_x,_x, _x},
		{_x,_T,_T,_T, _T,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_T, _x,_x,_x,_x, _x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_T,_T,_T, _T,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_T, _T,_T,_x,_x, _x,_x,_x,_x, _x},
		{_x,_T,_T,_T, _T,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_T, _x,_x,_x,_x, _x},
		{_x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_T,_x, _T,_T,_x,_x, _x,_x,_T,_x, _x,_T,_x,_x, _T,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_T, _x,_T,_T,_x, _T,_x,_x,_T, _T,_T,_x,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x,_x, _x,_x,_x,_x, _x},
		{_x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_T,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x}

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
			case 6: s = "\"(\" expected"; break;
			case 7: s = "\")\" expected"; break;
			case 8: s = "\";\" expected"; break;
			case 9: s = "\"uses\" expected"; break;
			case 10: s = "\",\" expected"; break;
			case 11: s = "\"type\" expected"; break;
			case 12: s = "\"=\" expected"; break;
			case 13: s = "\"of\" expected"; break;
			case 14: s = "\"set\" expected"; break;
			case 15: s = "\"packed\" expected"; break;
			case 16: s = "\"array\" expected"; break;
			case 17: s = "\"[\" expected"; break;
			case 18: s = "\"]\" expected"; break;
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
			case 38: s = "\"with\" expected"; break;
			case 39: s = "\"do\" expected"; break;
			case 40: s = "\"for\" expected"; break;
			case 41: s = "\"to\" expected"; break;
			case 42: s = "\"downto\" expected"; break;
			case 43: s = "\"repeat\" expected"; break;
			case 44: s = "\"until\" expected"; break;
			case 45: s = "\"while\" expected"; break;
			case 46: s = "\"if\" expected"; break;
			case 47: s = "\"then\" expected"; break;
			case 48: s = "\"or\" expected"; break;
			case 49: s = "\"and\" expected"; break;
			case 50: s = "\"not\" expected"; break;
			case 51: s = "\">\" expected"; break;
			case 52: s = "\">=\" expected"; break;
			case 53: s = "\"<\" expected"; break;
			case 54: s = "\"<=\" expected"; break;
			case 55: s = "\"<>\" expected"; break;
			case 56: s = "\"in\" expected"; break;
			case 57: s = "\"><\" expected"; break;
			case 58: s = "\"*\" expected"; break;
			case 59: s = "\"/\" expected"; break;
			case 60: s = "\"div\" expected"; break;
			case 61: s = "\"mod\" expected"; break;
			case 62: s = "\"true\" expected"; break;
			case 63: s = "\"false\" expected"; break;
			case 64: s = "\"unit\" expected"; break;
			case 65: s = "\"interface\" expected"; break;
			case 66: s = "\"implementation\" expected"; break;
			case 67: s = "??? expected"; break;
			case 68: s = "invalid Pascal"; break;
			case 69: s = "invalid Declaration"; break;
			case 70: s = "invalid Subroutine"; break;
			case 71: s = "invalid Type"; break;
			case 72: s = "invalid Ordinal"; break;
			case 73: s = "invalid RecordFieldList"; break;
			case 74: s = "invalid Constant"; break;
			case 75: s = "invalid LogicLiteral"; break;
			case 76: s = "invalid Procedure"; break;
			case 77: s = "invalid Function"; break;
			case 78: s = "invalid Statement"; break;
			case 79: s = "invalid ForLoop"; break;
			case 80: s = "invalid IdentifierBeginningStatement"; break;
			case 81: s = "invalid InnerAccessRouteElement"; break;
			case 82: s = "invalid SignedLogicFactor"; break;
			case 83: s = "invalid SignedFactor"; break;
			case 84: s = "invalid Factor"; break;
			case 85: s = "invalid IdentifierAccess"; break;
			case 86: s = "invalid SetConstructor"; break;
			case 87: s = "invalid InnerIdentifierAccess"; break;
			case 88: s = "invalid ActualParameter"; break;
			case 89: s = "invalid UnitSubroutineImplementation"; break;
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
