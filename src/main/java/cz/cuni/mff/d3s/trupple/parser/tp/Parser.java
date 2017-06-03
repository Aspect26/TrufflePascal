
package cz.cuni.mff.d3s.trupple.parser.tp;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.nodes.RootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.root.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
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
	public static final int maxT = 69;

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
    private boolean extendedGotoSupport;

	

	public Parser() {
		this.factory = new NodeFactory(this, true);
		this.extendedGotoSupport = false;
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
				Program();
			}
			if (la.kind == 9) {
				ImportsSection();
			}
			Declarations();
			MainFunction();
		} else if (la.kind == 66) {
			Unit();
		} else SynErr(70);
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
		while (StartOf(2)) {
			Declaration();
		}
	}

	void MainFunction() {
		StatementNode blockNode = Block();
		mainNode = factory.finishMainFunction(blockNode); 
		Expect(34);
	}

	void Unit() {
		UnitHeader();
		if (la.kind == 9) {
			ImportsSection();
		}
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
		if (la.kind == 11) {
			LabelDefinitions();
		} else if (la.kind == 27) {
			ConstantDefinitions();
		} else if (la.kind == 12) {
			TypeDefinitions();
		} else if (la.kind == 30) {
			VariableDeclarations();
		} else if (la.kind == 32 || la.kind == 33) {
			Subroutine();
		} else SynErr(71);
	}

	void LabelDefinitions() {
		Expect(11);
		Token labelToken = Label();
		factory.registerLabel(labelToken); 
		while (la.kind == 10) {
			Get();
			labelToken = Label();
			factory.registerLabel(labelToken); 
		}
		Expect(8);
	}

	void ConstantDefinitions() {
		Expect(27);
		ConstantDefinition();
		Expect(8);
		while (la.kind == 1) {
			ConstantDefinition();
			Expect(8);
		}
	}

	void TypeDefinitions() {
		Expect(12);
		TypeDefinition();
		Expect(8);
		while (la.kind == 1) {
			TypeDefinition();
			Expect(8);
		}
		factory.initializeAllUninitializedPointerDescriptors(); 
	}

	void VariableDeclarations() {
		Expect(30);
		VariableLineDeclaration();
		Expect(8);
		while (la.kind == 1) {
			VariableLineDeclaration();
			Expect(8);
		}
	}

	void Subroutine() {
		if (la.kind == 33) {
			Procedure();
			Expect(8);
		} else if (la.kind == 32) {
			Function();
			Expect(8);
		} else SynErr(72);
	}

	Token  Label() {
		Token  labelToken;
		Expect(3);
		labelToken = t; 
		return labelToken;
	}

	void TypeDefinition() {
		Expect(1);
		Token identifier = t; 
		Expect(13);
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
		} else if (la.kind == 16 || la.kind == 17) {
			List<OrdinalDescriptor> ordinalDimensions  = ArrayDefinition();
			while (continuesArray()) {
				Expect(14);
				List<OrdinalDescriptor> additionalDimensions  = ArrayDefinition();
				ordinalDimensions.addAll(additionalDimensions); 
			}
			Expect(14);
			Expect(1);
			typeDescriptor = factory.createArray(ordinalDimensions, t); 
		} else if (la.kind == 15) {
			Get();
			Expect(14);
			OrdinalDescriptor ordinal = Ordinal();
			typeDescriptor = factory.createSetType(ordinal); 
		} else if (la.kind == 20) {
			typeDescriptor = FileType();
		} else if (la.kind == 21) {
			typeDescriptor = RecordType();
		} else if (la.kind == 23) {
			typeDescriptor = PointerType();
		} else SynErr(73);
		return typeDescriptor;
	}

	OrdinalDescriptor  SubrangeType() {
		OrdinalDescriptor  ordinal;
		ConstantDescriptor lowerBound = Constant();
		Expect(26);
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
		if (la.kind == 16) {
			Get();
		}
		Expect(17);
		ordinalDimensions = new ArrayList<>(); 
		Expect(18);
		OrdinalDescriptor ordinalDescriptor = null; 
		ordinalDescriptor = Ordinal();
		ordinalDimensions.add(ordinalDescriptor); 
		while (la.kind == 10) {
			Get();
			ordinalDescriptor = Ordinal();
			ordinalDimensions.add(ordinalDescriptor); 
		}
		Expect(19);
		return ordinalDimensions;
	}

	OrdinalDescriptor  Ordinal() {
		OrdinalDescriptor  ordinal;
		ordinal = null; 
		if (isSubrange()) {
			ordinal = SubrangeType();
		} else if (StartOf(3)) {
			TypeDescriptor typeDescriptor = Type();
			ordinal = factory.castTypeToOrdinalType(typeDescriptor); 
		} else SynErr(74);
		return ordinal;
	}

	TypeDescriptor  FileType() {
		TypeDescriptor  fileDescriptor;
		Expect(20);
		Expect(14);
		TypeDescriptor contentType = Type();
		fileDescriptor = factory.createFileType(contentType); 
		return fileDescriptor;
	}

	TypeDescriptor  RecordType() {
		TypeDescriptor  typeDescriptor;
		Expect(21);
		factory.startRecord(); 
		typeDescriptor = RecordFieldList();
		Expect(22);
		factory.finishRecord(); 
		return typeDescriptor;
	}

	TypeDescriptor  PointerType() {
		TypeDescriptor  typeDescriptor;
		Expect(23);
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
		} else if (la.kind == 24) {
			RecordVariantPart();
		} else if (la.kind == 7 || la.kind == 8 || la.kind == 22) {
		} else SynErr(75);
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
		Expect(24);
		OrdinalDescriptor selectorType = RecordVariantSelector();
		Expect(14);
		List<ConstantDescriptor> caseConstants  = RecordVariants();
		factory.assertLegalsCaseValues(selectorType, caseConstants); 
	}

	void RecordFixedSection() {
		VariableLineDeclaration();
	}

	void VariableLineDeclaration() {
		List<String> identifiers  = IdentifiersList();
		Expect(25);
		TypeDescriptor typeDescriptor = Type();
		factory.registerVariables(identifiers, typeDescriptor); 
	}

	OrdinalDescriptor  RecordVariantSelector() {
		OrdinalDescriptor  selectorDescriptor;
		Token tagToken = null; 
		if (isVariantSelectorTag()) {
			Expect(1);
			tagToken = t; 
			Expect(25);
		}
		Expect(1);
		if (tagToken != null) factory.registerRecordVariantTagVariable(tagToken, t); 
		selectorDescriptor = factory.castTypeToOrdinalType(factory.getTypeDescriptor(t)); 
		return selectorDescriptor;
	}

	List<ConstantDescriptor>  RecordVariants() {
		List<ConstantDescriptor>  caseConstants;
		caseConstants = new ArrayList<>(); 
		List<ConstantDescriptor> newCaseConstants  = RecordVariant();
		caseConstants.addAll(newCaseConstants); 
		while (!recordVariantsEnd()) {
			Expect(8);
			newCaseConstants = RecordVariant();
			caseConstants.addAll(newCaseConstants); 
		}
		return caseConstants;
	}

	List<ConstantDescriptor>  RecordVariant() {
		List<ConstantDescriptor>  caseConstants;
		caseConstants = CaseConstantList();
		Expect(25);
		Expect(6);
		TypeDescriptor type = RecordFieldList();
		Expect(7);
		return caseConstants;
	}

	List<ConstantDescriptor>  CaseConstantList() {
		List<ConstantDescriptor>  caseConstants;
		caseConstants = new ArrayList<>(); 
		ConstantDescriptor constant = Constant();
		caseConstants.add(constant); 
		while (la.kind == 10) {
			Get();
			constant = Constant();
			caseConstants.add(constant); 
		}
		return caseConstants;
	}

	ConstantDescriptor  Constant() {
		ConstantDescriptor  constant;
		constant = null; String sign = ""; 
		if (la.kind == 28 || la.kind == 29) {
			if (la.kind == 28) {
				Get();
			} else {
				Get();
				sign = t.val; 
			}
		}
		if (la.kind == 3) {
			long value = UnsignedIntegerLiteral();
			constant = factory.createNumericConstant(sign, value);
		} else if (la.kind == 4) {
			double value = UnsignedDoubleLiteral();
			constant = factory.createDoubleConstant(sign, value); 
		} else if (la.kind == 2) {
			String value = StringLiteral();
			constant = factory.createCharOrStringConstant(sign, value); 
		} else if (la.kind == 64 || la.kind == 65) {
			boolean value = LogicLiteral();
			constant = factory.createBooleanConstant(sign, value); 
		} else if (la.kind == 1) {
			Token identifier = IdentifierConstant();
			constant = factory.createConstantFromIdentifier(sign, identifier); 
		} else SynErr(76);
		return constant;
	}

	void ConstantDefinition() {
		Expect(1);
		Token identifier = t; 
		Expect(13);
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
		if (la.kind == 64) {
			Get();
			result = true; 
		} else if (la.kind == 65) {
			Get();
			result = false; 
		} else SynErr(77);
		return result;
	}

	Token  IdentifierConstant() {
		Token  identifierToken;
		Expect(1);
		identifierToken = t; 
		return identifierToken;
	}

	void Procedure() {
		ProcedureHeading procedureHeading = ProcedureHeading();
		if (la.kind == 31) {
			Get();
			factory.forwardProcedure(procedureHeading); 
		} else if (StartOf(4)) {
			factory.startProcedureImplementation(procedureHeading); 
			Declarations();
			StatementNode bodyNode = Block();
			factory.finishProcedureImplementation(bodyNode); 
		} else SynErr(78);
	}

	void Function() {
		FunctionHeading functionHeading = FunctionHeading();
		if (la.kind == 31) {
			Get();
			factory.forwardFunction(functionHeading); 
		} else if (StartOf(4)) {
			factory.startFunctionImplementation(functionHeading); 
			Declarations();
			StatementNode bodyNode = Block();
			factory.finishFunctionImplementation(bodyNode); 
		} else SynErr(79);
	}

	ProcedureHeading  ProcedureHeading() {
		ProcedureHeading  procedureHeading;
		Expect(33);
		Expect(1);
		Token identifierToken = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 6) {
			formalParameters = FormalParameterList();
		}
		Expect(8);
		procedureHeading = new ProcedureHeading(identifierToken, formalParameters); 
		return procedureHeading;
	}

	StatementNode  Block() {
		StatementNode  blockNode;
		Expect(35);
		List<StatementNode> bodyNodes = new ArrayList<>(); 
		if (StartOf(5)) {
			StatementSequence(bodyNodes);
		}
		Expect(22);
		blockNode = factory.createBlockNode(bodyNodes, this.extendedGotoSupport); 
		return blockNode;
	}

	FunctionHeading  FunctionHeading() {
		FunctionHeading  functionHeading;
		Expect(32);
		Expect(1);
		Token identifierToken = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 6) {
			formalParameters = FormalParameterList();
		}
		Expect(25);
		Expect(1);
		Token returnTypeToken = t; 
		Expect(8);
		functionHeading = new FunctionHeading(identifierToken, formalParameters, factory.getTypeDescriptor(returnTypeToken)); 
		return functionHeading;
	}

	List<FormalParameter>  FormalParameterList() {
		List<FormalParameter>  formalParameters;
		Expect(6);
		formalParameters = new ArrayList<>(); 
		List<FormalParameter> newParameters = new ArrayList<>(); 
		newParameters = FormalParameter();
		formalParameters.addAll(newParameters); 
		while (la.kind == 8) {
			Get();
			newParameters = FormalParameter();
			formalParameters.addAll(newParameters); 
		}
		Expect(7);
		return formalParameters;
	}

	List<FormalParameter>  FormalParameter() {
		List<FormalParameter>  formalParameter;
		List<String> identifiers = new ArrayList<>(); 
		boolean isReference = false; 
		if (la.kind == 30) {
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
		Expect(25);
		Expect(1);
		formalParameter = factory.createFormalParametersList(identifiers, t, isReference); 
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
		statement = null; Token label = null;
		if (la.kind == 3) {
			label = Label();
			Expect(25);
		}
		switch (la.kind) {
		case 8: case 22: case 38: case 45: {
			statement = factory.createNopStatement(); 
			break;
		}
		case 47: {
			statement = IfStatement();
			break;
		}
		case 41: {
			statement = ForLoop();
			break;
		}
		case 46: {
			statement = WhileLoop();
			break;
		}
		case 44: {
			statement = RepeatLoop();
			break;
		}
		case 24: {
			statement = CaseStatement();
			break;
		}
		case 36: {
			Get();
			statement = factory.createBreak(); 
			break;
		}
		case 39: {
			statement = WithStatement();
			break;
		}
		case 49: {
			statement = GotoStatement();
			break;
		}
		case 35: {
			statement = Block();
			break;
		}
		case 1: {
			statement = IdentifierBeginningStatement();
			break;
		}
		default: SynErr(80); break;
		}
		if (label != null) statement = factory.createLabeledStatement(statement, label); 
		return statement;
	}

	StatementNode  IfStatement() {
		StatementNode  statement;
		Expect(47);
		ExpressionNode condition = Expression();
		Expect(48);
		StatementNode thenStatement = Statement();
		StatementNode elseStatement = null; 
		if (la.kind == 38) {
			Get();
			elseStatement = Statement();
		}
		statement = factory.createIfStatement(condition, thenStatement, elseStatement); 
		return statement;
	}

	StatementNode  ForLoop() {
		StatementNode  statement;
		factory.startLoop(); 
		Expect(41);
		boolean ascending = true; 
		Expect(1);
		Token variableToken = t; 
		Expect(37);
		ExpressionNode startValue = Expression();
		if (la.kind == 42) {
			Get();
			ascending = true; 
		} else if (la.kind == 43) {
			Get();
			ascending = false; 
		} else SynErr(81);
		ExpressionNode finalValue = Expression();
		Expect(40);
		StatementNode loopBody = Statement();
		statement = factory.createForLoop(ascending, variableToken, startValue, finalValue, loopBody); 
		factory.finishLoop(); 
		return statement;
	}

	StatementNode  WhileLoop() {
		StatementNode  statement;
		factory.startLoop(); 
		Expect(46);
		ExpressionNode condition = Expression();
		Expect(40);
		StatementNode loopBody = Statement();
		statement = factory.createWhileLoop(condition, loopBody); 
		factory.finishLoop(); 
		return statement;
	}

	StatementNode  RepeatLoop() {
		StatementNode  statement;
		factory.startLoop(); 
		Expect(44);
		List<StatementNode> bodyNodes = new ArrayList<>(); 
		StatementSequence(bodyNodes);
		Expect(45);
		ExpressionNode condition = Expression();
		statement = factory.createRepeatLoop(condition, factory.createBlockNode(bodyNodes, this.extendedGotoSupport)); 
		factory.finishLoop(); 
		return statement;
	}

	StatementNode  CaseStatement() {
		StatementNode  statement;
		Expect(24);
		ExpressionNode caseExpression = Expression();
		Expect(14);
		CaseStatementData caseData = CaseList();
		caseData.caseExpression = caseExpression; 
		Expect(22);
		statement = factory.createCaseStatement(caseData); 
		return statement;
	}

	StatementNode  WithStatement() {
		StatementNode  statement;
		Expect(39);
		List<String> recordIdentifiers  = IdentifiersList();
		LexicalScope initialScope = factory.getScope(); 
		List<FrameSlot> recordSlots = factory.stepIntoRecordsScope(recordIdentifiers); 
		Expect(40);
		StatementNode innerStatement = Statement();
		factory.setScope(initialScope); 
		statement = factory.createWithStatement(recordSlots, innerStatement); 
		return statement;
	}

	StatementNode  GotoStatement() {
		StatementNode  statement;
		Expect(49);
		Expect(3);
		statement = factory.createGotoStatement(t); 
		return statement;
	}

	StatementNode  IdentifierBeginningStatement() {
		StatementNode  statement;
		statement = null; 
		Expect(1);
		Token identifierToken = t; 
		if (StartOf(6)) {
			statement = factory.createSubroutineCall(identifierToken, new ArrayList<>()); 
		} else if (la.kind == 6) {
			statement = SubroutineCall(identifierToken);
		} else if (StartOf(7)) {
			statement = Assignment(identifierToken);
		} else SynErr(82);
		return statement;
	}

	ExpressionNode  SubroutineCall(Token identifierToken) {
		ExpressionNode  expression;
		Expect(6);
		List<ExpressionNode> parameters = new ArrayList<>(); 
		if (StartOf(8)) {
			parameters = ActualParameters(identifierToken);
		}
		Expect(7);
		expression = factory.createSubroutineCall(identifierToken, parameters); 
		return expression;
	}

	StatementNode  Assignment(Token identifierToken) {
		StatementNode  assignment;
		AssignmentData assignmentData = AssignmentRoute(identifierToken);
		Expect(37);
		ExpressionNode value = Expression();
		assignment = factory.finishAssignmentNode(assignmentData, value); 
		return assignment;
	}

	AssignmentData  AssignmentRoute(Token identifierToken) {
		AssignmentData  data;
		data = new AssignmentData(); 
		data.setSimple(factory, identifierToken); 
		while (la.kind == 18 || la.kind == 23 || la.kind == 34) {
			if (la.kind == 18) {
				List<ExpressionNode> indexNodes  = ArrayIndex();
				data.setArray(factory, indexNodes); 
			} else if (la.kind == 23) {
				Get();
				data.setDereference(factory); 
			} else {
				Get();
				Expect(1);
				data.setRecord(factory, t); 
			}
		}
		return data;
	}

	ExpressionNode  Expression() {
		ExpressionNode  expression;
		expression = LogicTerm();
		while (la.kind == 50) {
			Get();
			Token op = t; 
			ExpressionNode right = LogicTerm();
			expression = factory.createBinaryExpression(op, expression, right); 
		}
		return expression;
	}

	List<ExpressionNode>  ArrayIndex() {
		List<ExpressionNode>  indexNodes;
		indexNodes = new ArrayList<>(); 
		ExpressionNode indexingNode = null; 
		Expect(18);
		indexingNode = Expression();
		indexNodes.add(indexingNode); 
		while (la.kind == 10) {
			Get();
			indexingNode = Expression();
			indexNodes.add(indexingNode); 
		}
		Expect(19);
		return indexNodes;
	}

	CaseStatementData  CaseList() {
		CaseStatementData  data;
		data = new CaseStatementData(); 
		ExpressionNode caseConstant = Expression();
		data.indexNodes.add(caseConstant); 
		Expect(25);
		StatementNode caseStatement = Statement();
		data.statementNodes.add(caseStatement); 
		while (!caseEnds()) {
			Expect(8);
			caseConstant = Expression();
			data.indexNodes.add(caseConstant); 
			Expect(25);
			caseStatement = Statement();
			data.statementNodes.add(caseStatement); 
		}
		if (la.kind == 8) {
			Get();
		}
		if (la.kind == 38) {
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
		while (la.kind == 51) {
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
		if (la.kind == 52) {
			Get();
			Token op = t; 
			ExpressionNode right = SignedLogicFactor();
			expression = factory.createUnaryExpression(op, right); 
		} else if (StartOf(9)) {
			expression = LogicFactor();
		} else SynErr(83);
		return expression;
	}

	ExpressionNode  LogicFactor() {
		ExpressionNode  expression;
		expression = Arithmetic();
		if (StartOf(10)) {
			switch (la.kind) {
			case 53: {
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
			case 13: {
				Get();
				break;
			}
			case 57: {
				Get();
				break;
			}
			case 58: {
				Get();
				break;
			}
			case 59: {
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
		while (la.kind == 28 || la.kind == 29) {
			if (la.kind == 28) {
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
		while (StartOf(11)) {
			if (la.kind == 60) {
				Get();
			} else if (la.kind == 61) {
				Get();
			} else if (la.kind == 62) {
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
		if (la.kind == 28 || la.kind == 29) {
			if (la.kind == 28) {
				Get();
			} else {
				Get();
			}
			Token unOp = t; 
			expression = SignedFactor();
			expression = factory.createUnaryExpression(unOp, expression); 
		} else if (StartOf(12)) {
			expression = Factor();
		} else SynErr(84);
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
		case 64: case 65: {
			boolean val; 
			val = LogicLiteral();
			expression = factory.createLogicLiteralNode(val); 
			break;
		}
		case 18: {
			expression = SetConstructor();
			break;
		}
		default: SynErr(85); break;
		}
		return expression;
	}

	ExpressionNode  IdentifierAccess() {
		ExpressionNode  expression;
		Expect(1);
		Token identifierToken = t; expression = null; 
		if (StartOf(13)) {
			expression = factory.createExpressionFromSingleIdentifier(identifierToken); 
		} else if (StartOf(14)) {
			expression = InnerIdentifierAccess(identifierToken);
		} else SynErr(86);
		return expression;
	}

	ExpressionNode  SetConstructor() {
		ExpressionNode  expression;
		expression = null; 
		Expect(18);
		if (la.kind == 19) {
			expression = factory.createSetConstructorNode(new ArrayList<>()); 
		} else if (StartOf(8)) {
			List<ExpressionNode> valueNodes = new ArrayList<ExpressionNode>(); 
			ExpressionNode valueNode = Expression();
			valueNodes.add(valueNode); 
			while (la.kind == 10) {
				Get();
				valueNode = Expression();
				valueNodes.add(valueNode); 
			}
			expression = factory.createSetConstructorNode(valueNodes); 
		} else SynErr(87);
		Expect(19);
		return expression;
	}

	ExpressionNode  InnerIdentifierAccess(Token identifierToken) {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 6) {
			expression = SubroutineCall(identifierToken);
		} else if (la.kind == 18 || la.kind == 23 || la.kind == 34) {
			expression = InnerReadRouteNonEmpty(identifierToken);
		} else SynErr(88);
		return expression;
	}

	ExpressionNode  InnerReadRouteNonEmpty(Token identifierToken) {
		ExpressionNode  expression;
		ExpressionNode readIdentifier = factory.createExpressionFromSingleIdentifier(identifierToken); 
		expression = ReadRouteElement(readIdentifier);
		while (la.kind == 18 || la.kind == 23 || la.kind == 34) {
			expression = ReadRouteElement(expression);
		}
		return expression;
	}

	ExpressionNode  ReadRouteElement(ExpressionNode expression) {
		ExpressionNode  resultExpression;
		resultExpression = null; 
		if (la.kind == 18) {
			List<ExpressionNode> indexNodes  = ArrayIndex();
			resultExpression = factory.createReadFromArrayNode(expression, indexNodes); 
		} else if (la.kind == 34) {
			Get();
			Expect(1);
			resultExpression = factory.createReadFromRecordNode(expression, t); 
		} else if (la.kind == 23) {
			Get();
			resultExpression = factory.createReadDereferenceNode(expression); 
		} else SynErr(89);
		return resultExpression;
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
			parameter = factory.createReferencePassNode(t); 
		} else if (factory.shouldBeSubroutine(subroutineToken, currentParameterIndex)) {
			Expect(1);
			parameter = factory.createSubroutineParameterPassNode(t); 
		} else if (StartOf(8)) {
			parameter = Expression();
		} else SynErr(90);
		return parameter;
	}

	void UnitHeader() {
		Expect(66);
		Expect(1);
		factory.startUnit(t); 
		Expect(8);
	}

	void InterfaceSection() {
		Expect(67);
		while (StartOf(15)) {
			if (la.kind == 33) {
				UnitProcedureHeading();
			} else if (la.kind == 32) {
				UnitFunctionHeading();
			} else if (la.kind == 12) {
				TypeDefinitions();
			} else {
				VariableDeclarations();
			}
		}
	}

	void ImplementationSection() {
		Expect(68);
		while (StartOf(15)) {
			if (la.kind == 32 || la.kind == 33) {
				UnitSubroutineImplementation();
			} else if (la.kind == 30) {
				VariableDeclarations();
			} else {
				TypeDefinitions();
			}
		}
	}

	void UnitFooter() {
		Expect(22);
		factory.finishUnit(); 
		Expect(34);
	}

	void UnitProcedureHeading() {
		Expect(33);
		Expect(1);
		Token name = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 6) {
			formalParameters = FormalParameterList();
		}
		factory.addUnitProcedureInterface(name, formalParameters); 
		Expect(8);
	}

	void UnitFunctionHeading() {
		Expect(32);
		Expect(1);
		Token name = t; 
		List<FormalParameter> formalParameters = new ArrayList<>(); 
		if (la.kind == 6) {
			formalParameters = FormalParameterList();
		}
		Expect(25);
		Expect(1);
		factory.addUnitFunctionInterface(name, formalParameters, t); 
		Expect(8);
	}

	void UnitSubroutineImplementation() {
		if (la.kind == 33) {
			UnitProcedureImplementation();
			Expect(8);
		} else if (la.kind == 32) {
			UnitFunctionImplementation();
			Expect(8);
		} else SynErr(91);
	}

	void UnitProcedureImplementation() {
		ProcedureHeading heading = ProcedureHeading();
		if (la.kind == 31) {
			Get();
			factory.forwardProcedure(heading); 
		} else if (StartOf(4)) {
			factory.startUnitProcedureImplementation(heading); 
			Declarations();
			StatementNode bodyNode = Block();
			factory.finishProcedureImplementation(bodyNode); 
		} else SynErr(92);
	}

	void UnitFunctionImplementation() {
		FunctionHeading heading = FunctionHeading();
		if (la.kind == 31) {
			Get();
			factory.forwardFunction(heading); 
		} else if (StartOf(4)) {
			factory.startUnitFunctionImplementation(heading); 
			Declarations();
			StatementNode bodyNode = Block();
			factory.finishFunctionImplementation(bodyNode); 
		} else SynErr(93);
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
		{_T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_T,_x,_x, _x,_T,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_T,_x, _T,_T,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_T,_x, _T,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_T,_x,_x, _T,_T,_x,_T, _x,_x,_x,_x, _T,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_T,_x, _T,_T,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_T,_x,_T, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x,_T, _x,_T,_x,_x, _T,_x,_T,_T, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_x,_x, _x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_T,_T, _T,_T,_T,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_T,_T, _x,_x,_x,_x, _x,_x,_x},
		{_x,_T,_T,_T, _T,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_T,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_T,_x, _x,_T,_T,_x, _x,_x,_x,_T, _x,_x,_T,_x, _x,_T,_x,_x, _T,_T,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _T,_x,_T,_T, _x,_T,_x,_x, _T,_x,_T,_T, _x,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _x,_x,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_x, _T,_T,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x}

	};

    public void reset() {
        this.factory.reset();
        this.errors = new Errors();
        this.mainNode = null;
    }

	public boolean isUsingTPExtension() {
	    return true;
	}

	public void setExtendedGoto(boolean extendedGoto) {
        this.extendedGotoSupport = extendedGoto;
    }
	
    public boolean hadErrors(){
    	return errors.count > 0;
    }

    public RootNode getRootNode() {
        return (this.mainNode == null)? factory.createUnitRootNode() : this.mainNode;
    }
    
    boolean caseEnds(){
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

    boolean continuesArray() {
        Token next = scanner.Peek();
        scanner.ResetPeek();
        if(next.val.toLowerCase().equals("array") || (next.val.toLowerCase().equals("packed")))
            return true;

        return false;
    }

    boolean isSubrange() {
        Token next = scanner.Peek();
        scanner.ResetPeek();
        return next.val.toLowerCase().equals("..");
    }

     boolean recordFixedPartContinues() {
        String actual = la.val.toLowerCase();
        String next = scanner.Peek().val.toLowerCase();
        scanner.ResetPeek();

        boolean variantPartStarts = actual.equals(";") && next.equals("case");
        boolean innerFieldListEnds = actual.equals(")") || (actual.equals(";") && next.equals(")") );
        boolean recordsEnds = actual.equals("end") || (actual.equals(";") && next.equals("end") );

        return !variantPartStarts && !recordsEnds && !innerFieldListEnds;
    }

    boolean isVariantSelectorTag() {
        Token next = scanner.Peek();
        scanner.ResetPeek();

        return next.val.equals(":");
    }

    boolean recordVariantPartStarts() {
        Token next = scanner.Peek();
                scanner.ResetPeek();

        return next.val.equals("case");
    }

    boolean recordVariantsEnd() {
        Token nextToken = scanner.Peek();
        scanner.ResetPeek();

        String lookAhead = la.val.toLowerCase();
        String next = nextToken.val.toLowerCase();

        return lookAhead.equals("end") || (lookAhead.equals(";") && next.equals("end"));
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
			case 11: s = "\"label\" expected"; break;
			case 12: s = "\"type\" expected"; break;
			case 13: s = "\"=\" expected"; break;
			case 14: s = "\"of\" expected"; break;
			case 15: s = "\"set\" expected"; break;
			case 16: s = "\"packed\" expected"; break;
			case 17: s = "\"array\" expected"; break;
			case 18: s = "\"[\" expected"; break;
			case 19: s = "\"]\" expected"; break;
			case 20: s = "\"file\" expected"; break;
			case 21: s = "\"record\" expected"; break;
			case 22: s = "\"end\" expected"; break;
			case 23: s = "\"^\" expected"; break;
			case 24: s = "\"case\" expected"; break;
			case 25: s = "\":\" expected"; break;
			case 26: s = "\"..\" expected"; break;
			case 27: s = "\"const\" expected"; break;
			case 28: s = "\"+\" expected"; break;
			case 29: s = "\"-\" expected"; break;
			case 30: s = "\"var\" expected"; break;
			case 31: s = "\"forward\" expected"; break;
			case 32: s = "\"function\" expected"; break;
			case 33: s = "\"procedure\" expected"; break;
			case 34: s = "\".\" expected"; break;
			case 35: s = "\"begin\" expected"; break;
			case 36: s = "\"break\" expected"; break;
			case 37: s = "\":=\" expected"; break;
			case 38: s = "\"else\" expected"; break;
			case 39: s = "\"with\" expected"; break;
			case 40: s = "\"do\" expected"; break;
			case 41: s = "\"for\" expected"; break;
			case 42: s = "\"to\" expected"; break;
			case 43: s = "\"downto\" expected"; break;
			case 44: s = "\"repeat\" expected"; break;
			case 45: s = "\"until\" expected"; break;
			case 46: s = "\"while\" expected"; break;
			case 47: s = "\"if\" expected"; break;
			case 48: s = "\"then\" expected"; break;
			case 49: s = "\"goto\" expected"; break;
			case 50: s = "\"or\" expected"; break;
			case 51: s = "\"and\" expected"; break;
			case 52: s = "\"not\" expected"; break;
			case 53: s = "\">\" expected"; break;
			case 54: s = "\">=\" expected"; break;
			case 55: s = "\"<\" expected"; break;
			case 56: s = "\"<=\" expected"; break;
			case 57: s = "\"<>\" expected"; break;
			case 58: s = "\"in\" expected"; break;
			case 59: s = "\"><\" expected"; break;
			case 60: s = "\"*\" expected"; break;
			case 61: s = "\"/\" expected"; break;
			case 62: s = "\"div\" expected"; break;
			case 63: s = "\"mod\" expected"; break;
			case 64: s = "\"true\" expected"; break;
			case 65: s = "\"false\" expected"; break;
			case 66: s = "\"unit\" expected"; break;
			case 67: s = "\"interface\" expected"; break;
			case 68: s = "\"implementation\" expected"; break;
			case 69: s = "??? expected"; break;
			case 70: s = "invalid Pascal"; break;
			case 71: s = "invalid Declaration"; break;
			case 72: s = "invalid Subroutine"; break;
			case 73: s = "invalid Type"; break;
			case 74: s = "invalid Ordinal"; break;
			case 75: s = "invalid RecordFieldList"; break;
			case 76: s = "invalid Constant"; break;
			case 77: s = "invalid LogicLiteral"; break;
			case 78: s = "invalid Procedure"; break;
			case 79: s = "invalid Function"; break;
			case 80: s = "invalid Statement"; break;
			case 81: s = "invalid ForLoop"; break;
			case 82: s = "invalid IdentifierBeginningStatement"; break;
			case 83: s = "invalid SignedLogicFactor"; break;
			case 84: s = "invalid SignedFactor"; break;
			case 85: s = "invalid Factor"; break;
			case 86: s = "invalid IdentifierAccess"; break;
			case 87: s = "invalid SetConstructor"; break;
			case 88: s = "invalid InnerIdentifierAccess"; break;
			case 89: s = "invalid ReadRouteElement"; break;
			case 90: s = "invalid ActualParameter"; break;
			case 91: s = "invalid UnitSubroutineImplementation"; break;
			case 92: s = "invalid UnitProcedureImplementation"; break;
			case 93: s = "invalid UnitFunctionImplementation"; break;
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
