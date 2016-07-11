
package pascal.language.parser;

import pascal.language.nodes.*;
import pascal.language.runtime.PascalContext;

import com.oracle.truffle.api.source.Source;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	public static final int _EOF = 0;
	public static final int _identifier = 1;
	public static final int _stringLiteral = 2;
	public static final int _numericLiteral = 3;
	public static final int maxT = 45;

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

	

	public Parser(PascalContext context, Source source) {
		this.scanner = new Scanner(source.getInputStream());
		this.factory = new NodeFactory(this, context, source);
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
		if (la.kind == 4) {
			VariableDefinitions();
		}
		while (la.kind == 8 || la.kind == 9) {
			Subroutine();
		}
		MainFunction();
	}

	void VariableDefinitions() {
		Expect(4);
		VariableLineDefinition();
		while (la.kind == 1) {
			VariableLineDefinition();
		}
	}

	void Subroutine() {
		if (la.kind == 9) {
			Function();
		} else if (la.kind == 8) {
			Procedure();
		} else SynErr(46);
	}

	void MainFunction() {
		factory.startMainFunction(); 
		StatementNode blockNode = Block();
		mainNode = factory.finishMainFunction(blockNode); 
		Expect(12);
	}

	void VariableLineDefinition() {
		factory.startVariableLineDefinition(); 
		Expect(1);
		factory.addNewUnknownVariable(t); 
		while (la.kind == 5) {
			Get();
			Expect(1);
			factory.addNewUnknownVariable(t); 
		}
		Expect(6);
		Expect(1);
		factory.finishVariableLineDefinition(t); 
		Expect(7);
	}

	void Function() {
		Expect(9);
		Expect(1);
		factory.startFunction(t); 
		if (la.kind == 10) {
			FormalParameterList();
		}
		Expect(6);
		Expect(1);
		Expect(7);
		factory.setFunctionReturnValue(t); 
		if (la.kind == 4) {
			VariableDefinitions();
		}
		StatementNode bodyNode = Block();
		factory.finishFunction(bodyNode); 
		Expect(7);
	}

	void Procedure() {
		Expect(8);
		Expect(1);
		factory.startProcedure(t); 
		if (la.kind == 10) {
			FormalParameterList();
		}
		Expect(7);
		if (la.kind == 4) {
			VariableDefinitions();
		}
		StatementNode bodyNode = Block();
		factory.finishProcedure(bodyNode); 
		Expect(7);
	}

	void FormalParameterList() {
		Expect(10);
		FormalParameter();
		while (la.kind == 7) {
			Get();
			FormalParameter();
		}
		Expect(11);
	}

	StatementNode  Block() {
		StatementNode  blockNode;
		Expect(13);
		List<StatementNode> body = new ArrayList<>(); 
		if (StartOf(1)) {
			StatementSequence(body);
		}
		Expect(14);
		blockNode = factory.finishBlock(body); 
		return blockNode;
	}

	void FormalParameter() {
		if (la.kind == 1) {
			ValueParameter();
		} else if (la.kind == 4) {
			VariableParameter();
		} else SynErr(47);
	}

	void ValueParameter() {
		Expect(1);
		while (la.kind == 5) {
			Get();
			Expect(1);
		}
		Expect(6);
		Expect(1);
	}

	void VariableParameter() {
		Expect(4);
		VariableLineDefinition();
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
		case 7: case 14: case 24: case 28: {
			statement = factory.createEmptyStatement(); 
			break;
		}
		case 1: case 2: case 3: case 10: case 37: case 38: case 43: case 44: {
			statement = Expression();
			break;
		}
		case 26: {
			statement = IfStatement();
			break;
		}
		case 18: {
			statement = ForLoop();
			break;
		}
		case 25: {
			statement = WhileLoop();
			break;
		}
		case 23: {
			statement = RepeatLoop();
			break;
		}
		case 16: {
			statement = CaseStatement();
			break;
		}
		case 15: {
			Get();
			statement = factory.createBreak(); 
			break;
		}
		case 13: {
			statement = Block();
			break;
		}
		default: SynErr(48); break;
		}
		return statement;
	}

	ExpressionNode  Expression() {
		ExpressionNode  expression;
		expression = LogicTerm();
		while (la.kind == 29) {
			Get();
			Token op = t; 
			ExpressionNode right = LogicTerm();
			expression = factory.createBinary(op, expression, right); 
		}
		return expression;
	}

	StatementNode  IfStatement() {
		StatementNode  statement;
		Expect(26);
		ExpressionNode condition = Expression();
		Expect(27);
		StatementNode thenStatement = Statement();
		StatementNode elseStatement = null; 
		if (la.kind == 28) {
			Get();
			elseStatement = Statement();
		}
		statement = factory.createIfStatement(condition, thenStatement, elseStatement); 
		return statement;
	}

	StatementNode  ForLoop() {
		StatementNode  statement;
		Expect(18);
		boolean ascending = true; 
		Expect(1);
		Token variableToken = t; 
		Expect(19);
		ExpressionNode startValue = Expression();
		if (la.kind == 20) {
			Get();
			ascending = true; 
		} else if (la.kind == 21) {
			Get();
			ascending = false; 
		} else SynErr(49);
		ExpressionNode finalValue = Expression();
		Expect(22);
		StatementNode loopBody = Statement();
		statement = factory.createForLoop(ascending, variableToken, startValue, finalValue, loopBody); 
		return statement;
	}

	StatementNode  WhileLoop() {
		StatementNode  statement;
		Expect(25);
		ExpressionNode condition = Expression();
		Expect(22);
		StatementNode loopBody = Statement();
		statement = factory.createWhileLoop(condition, loopBody); 
		return statement;
	}

	StatementNode  RepeatLoop() {
		StatementNode  statement;
		Expect(23);
		List<StatementNode> bodyNodes = new ArrayList<>(); 
		StatementSequence(bodyNodes);
		Expect(24);
		ExpressionNode condition = Expression();
		statement = factory.createRepeatLoop(condition, factory.finishBlock(bodyNodes)); 
		return statement;
	}

	StatementNode  CaseStatement() {
		StatementNode  statement;
		Expect(16);
		ExpressionNode caseIndex = Expression();
		Expect(17);
		factory.startCaseList();	
		CaseList();
		Expect(14);
		statement = factory.finishCaseStatement(caseIndex); 
		return statement;
	}

	void CaseList() {
		ExpressionNode caseConstant = Expression();
		Expect(6);
		StatementNode caseStatement = Statement();
		factory.addCaseOption(caseConstant, caseStatement); 
		while (la.kind == 7) {
			Get();
			caseConstant = Expression();
			Expect(6);
			caseStatement = Statement();
			factory.addCaseOption(caseConstant, caseStatement); 
		}
	}

	ExpressionNode  LogicTerm() {
		ExpressionNode  expression;
		expression = LogicFactor();
		while (la.kind == 30) {
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
		if (StartOf(2)) {
			switch (la.kind) {
			case 31: {
				Get();
				break;
			}
			case 32: {
				Get();
				break;
			}
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
		while (la.kind == 37 || la.kind == 38) {
			if (la.kind == 37) {
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
		while (la.kind == 39 || la.kind == 40 || la.kind == 41) {
			if (la.kind == 39) {
				Get();
			} else if (la.kind == 40) {
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
		if (la.kind == 37 || la.kind == 38) {
			if (la.kind == 37) {
				Get();
			} else {
				Get();
			}
			Token unOp = t; 
			expression = SignedFactor();
			expression = factory.createUnary(unOp, expression); 
		} else if (StartOf(3)) {
			expression = Factor();
		} else SynErr(50);
		return expression;
	}

	ExpressionNode  Factor() {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 1) {
			Get();
			if (la.kind == 10 || la.kind == 19) {
				expression = MemberExpression(t);
			} else if (StartOf(4)) {
				expression = factory.readVariable(t); 
				if(expression == null) 
				SemErr("Undefined variable!"); 
			} else SynErr(51);
		} else if (la.kind == 10) {
			Get();
			expression = Expression();
			Expect(11);
		} else if (la.kind == 2) {
			Get();
			expression = factory.createCharLiteral(t); 
		} else if (la.kind == 3) {
			Get();
			Token integerPartToken; 
			Token fractionalPartToken = null; 
			Token exponentOpToken = null; 
			Token exponentToken = null; 
			integerPartToken = t; 
			if (StartOf(4)) {
				expression = factory.createNumericLiteral(integerPartToken); 
				if(expression == null) 
				SemErr("Constant out of range!"); 
			} else if (la.kind == 12 || la.kind == 42) {
				if (la.kind == 12) {
					Get();
					Expect(3);
					fractionalPartToken = t; 
					if (la.kind == 42) {
						Get();
						if (la.kind == 37 || la.kind == 38) {
							if (la.kind == 37) {
								Get();
							} else {
								Get();
							}
						}
						exponentOpToken = t; 
						Expect(3);
						exponentToken = t; 
					}
				} else {
					Get();
					if (la.kind == 37 || la.kind == 38) {
						if (la.kind == 37) {
							Get();
						} else {
							Get();
						}
					}
					exponentOpToken = t; 
					Expect(3);
					exponentToken = t; 
				}
				expression = factory.createRealLiteral(integerPartToken, 
				fractionalPartToken, exponentOpToken, exponentToken); 
			} else SynErr(52);
		} else if (la.kind == 43 || la.kind == 44) {
			expression = LogicLiteral();
		} else SynErr(53);
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
			if (StartOf(5)) {
				parameter = Expression();
				parameters.add(parameter); 
				while (la.kind == 5) {
					Get();
					parameter = Expression();
					parameters.add(parameter); 
				}
			}
			Expect(11);
			expression = factory.createCall(functionNode, parameters); 
		} else if (la.kind == 19) {
			Get();
			ExpressionNode value = Expression();
			if(identifierName == null) { 
			SemErr("Invalid assignment target!"); 
			} else { 
			expression = factory.createAssignment(identifierName, value); 
			if(expression == null) 
			SemErr("Undefined variable!"); 
			} 
		} else SynErr(54);
		return expression;
	}

	ExpressionNode  LogicLiteral() {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 43) {
			Get();
			expression = factory.createLogicLiteral(true); 
		} else if (la.kind == 44) {
			Get();
			expression = factory.createLogicLiteral(false); 
		} else SynErr(55);
		return expression;
	}



	public void Parse() {
		la = new Token();
		la.val = "";		
		Get();
		Pascal();
		Expect(0);

	}

	private static final boolean[][] set = {
		{_T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_T,_T,_T, _x,_x,_x,_T, _x,_x,_T,_x, _x,_T,_T,_T, _T,_x,_T,_x, _x,_x,_x,_T, _x,_T,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_T,_x, _x,_x,_x,_T, _T,_x,_x},
		{_x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_T,_T,_T, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x},
		{_x,_T,_T,_T, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _T,_x,_x},
		{_x,_x,_x,_x, _x,_T,_T,_T, _x,_x,_x,_T, _x,_x,_T,_x, _x,_T,_x,_x, _T,_T,_T,_x, _T,_x,_x,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_T,_T, _T,_T,_x,_x, _x,_x,_x},
		{_x,_T,_T,_T, _x,_x,_x,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_T,_x, _x,_x,_x,_T, _T,_x,_x}

	};
	
	public static void parsePascal(PascalContext context, Source source) {
        Parser parser = new Parser(context, source);
        parser.Parse();
    }
    
    public boolean noErrors(){
    	return errors.count == 0;
    }
} // end Parser


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
			case 4: s = "\"var\" expected"; break;
			case 5: s = "\",\" expected"; break;
			case 6: s = "\":\" expected"; break;
			case 7: s = "\";\" expected"; break;
			case 8: s = "\"procedure\" expected"; break;
			case 9: s = "\"function\" expected"; break;
			case 10: s = "\"(\" expected"; break;
			case 11: s = "\")\" expected"; break;
			case 12: s = "\".\" expected"; break;
			case 13: s = "\"begin\" expected"; break;
			case 14: s = "\"end\" expected"; break;
			case 15: s = "\"break\" expected"; break;
			case 16: s = "\"case\" expected"; break;
			case 17: s = "\"of\" expected"; break;
			case 18: s = "\"for\" expected"; break;
			case 19: s = "\":=\" expected"; break;
			case 20: s = "\"to\" expected"; break;
			case 21: s = "\"downto\" expected"; break;
			case 22: s = "\"do\" expected"; break;
			case 23: s = "\"repeat\" expected"; break;
			case 24: s = "\"until\" expected"; break;
			case 25: s = "\"while\" expected"; break;
			case 26: s = "\"if\" expected"; break;
			case 27: s = "\"then\" expected"; break;
			case 28: s = "\"else\" expected"; break;
			case 29: s = "\"or\" expected"; break;
			case 30: s = "\"and\" expected"; break;
			case 31: s = "\">\" expected"; break;
			case 32: s = "\">=\" expected"; break;
			case 33: s = "\"<\" expected"; break;
			case 34: s = "\"<=\" expected"; break;
			case 35: s = "\"=\" expected"; break;
			case 36: s = "\"<>\" expected"; break;
			case 37: s = "\"+\" expected"; break;
			case 38: s = "\"-\" expected"; break;
			case 39: s = "\"*\" expected"; break;
			case 40: s = "\"div\" expected"; break;
			case 41: s = "\"mod\" expected"; break;
			case 42: s = "\"e\" expected"; break;
			case 43: s = "\"true\" expected"; break;
			case 44: s = "\"false\" expected"; break;
			case 45: s = "??? expected"; break;
			case 46: s = "invalid Subroutine"; break;
			case 47: s = "invalid FormalParameter"; break;
			case 48: s = "invalid Statement"; break;
			case 49: s = "invalid ForLoop"; break;
			case 50: s = "invalid SignedFactor"; break;
			case 51: s = "invalid Factor"; break;
			case 52: s = "invalid Factor"; break;
			case 53: s = "invalid Factor"; break;
			case 54: s = "invalid MemberExpression"; break;
			case 55: s = "invalid LogicLiteral"; break;
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
