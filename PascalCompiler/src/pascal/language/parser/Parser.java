
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
	public static final int maxT = 18;

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
		MainFunction();
	}

	void VariableDefinitions() {
		Expect(4);
		VariableLineDefinition();
		while (la.kind == 1) {
			VariableLineDefinition();
		}
	}

	void MainFunction() {
		factory.startMainFunction(); 
		StatementNode blockNode = MainBlock();
		mainNode = factory.finishMainFunction(blockNode); 
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

	StatementNode  MainBlock() {
		StatementNode  blockNode;
		factory.startMainBlock(); 
		Expect(8);
		List<StatementNode> body = new ArrayList<>(); 
		while (StartOf(1)) {
			StatementNode statement = Statement();
			body.add(statement); 
		}
		Expect(9);
		blockNode = factory.finishMainBlock(body); 
		return blockNode;
	}

	StatementNode  Statement() {
		StatementNode  statement;
		statement = Expression();
		Expect(7);
		return statement;
	}

	ExpressionNode  Expression() {
		ExpressionNode  expression;
		expression = Term();
		while (la.kind == 10 || la.kind == 11) {
			if (la.kind == 10) {
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
		while (la.kind == 12 || la.kind == 13 || la.kind == 14) {
			if (la.kind == 12) {
				Get();
			} else if (la.kind == 13) {
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
		if (la.kind == 10 || la.kind == 11) {
			if (la.kind == 10) {
				Get();
			} else {
				Get();
			}
			Token unOp = t; 
			expression = SignedFactor();
			expression = factory.createUnary(unOp, expression); 
		} else if (StartOf(2)) {
			expression = Factor();
		} else SynErr(19);
		return expression;
	}

	ExpressionNode  Factor() {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 1) {
			Get();
			if (la.kind == 15 || la.kind == 17) {
				expression = MemberExpression(null, t);
			} else if (StartOf(3)) {
				expression = factory.readVariable(t); 
				if(expression == null) 
				SemErr("Undefined variable!"); 
			} else SynErr(20);
		} else if (la.kind == 15) {
			Get();
			expression = Expression();
			Expect(16);
		} else if (la.kind == 2) {
			Get();
			expression = factory.createStringLiteral(t); 
		} else if (la.kind == 3) {
			Get();
			expression = factory.createNumericLiteral(t); 
			if(expression == null) 
			SemErr("Constant out of range!"); 
		} else SynErr(21);
		return expression;
	}

	ExpressionNode  MemberExpression(ExpressionNode r, Token assignmentName) {
		ExpressionNode  expression;
		expression = null; 
		if (la.kind == 15) {
			ExpressionNode receiver = r; 
			Get();
			if(receiver == null); 
			                    receiver = factory.createFunctionNode(assignmentName); 
			List<ExpressionNode> parameters = new ArrayList<>(); 
			ExpressionNode parameter; 
			if (StartOf(1)) {
				parameter = Expression();
				parameters.add(parameter); 
				while (la.kind == 5) {
					Get();
					parameter = Expression();
					parameters.add(parameter); 
				}
			}
			Expect(16);
			expression = factory.createCall(receiver, parameters); 
		} else if (la.kind == 17) {
			Get();
			ExpressionNode value = Expression();
			if(assignmentName == null) { 
			SemErr("Invalid assignment target!"); 
			} else { 
			expression = factory.createAssignment(assignmentName, value); 
			if(expression == null) 
			SemErr("Undefined variable!"); 
			} 
		} else SynErr(22);
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
		{_T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x},
		{_x,_T,_T,_T, _x,_x,_x,_x, _x,_x,_T,_T, _x,_x,_x,_T, _x,_x,_x,_x},
		{_x,_T,_T,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x,_x},
		{_x,_x,_x,_x, _x,_T,_x,_T, _x,_x,_T,_T, _T,_T,_T,_x, _T,_x,_x,_x}

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
			case 8: s = "\"begin\" expected"; break;
			case 9: s = "\"end.\" expected"; break;
			case 10: s = "\"+\" expected"; break;
			case 11: s = "\"-\" expected"; break;
			case 12: s = "\"*\" expected"; break;
			case 13: s = "\"div\" expected"; break;
			case 14: s = "\"mod\" expected"; break;
			case 15: s = "\"(\" expected"; break;
			case 16: s = "\")\" expected"; break;
			case 17: s = "\":=\" expected"; break;
			case 18: s = "??? expected"; break;
			case 19: s = "invalid SignedFactor"; break;
			case 20: s = "invalid Factor"; break;
			case 21: s = "invalid Factor"; break;
			case 22: s = "invalid MemberExpression"; break;
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
