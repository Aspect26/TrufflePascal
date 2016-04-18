
package pascal.language.parser;

import com.oracle.truffle.api.source.Source;
import pascal.language.runtime.PascalContext;

public class Parser {
	public static final int _EOF = 0;
	public static final int _identifier = 1;
	public static final int _stringLiteral = 2;
	public static final int _numericLiteral = 3;
	public static final int _charLiteral = 4;
	public static final int maxT = 47;

	static final boolean _T = true;
	static final boolean _x = false;
	static final int minErrDist = 2;

	public Token t;    // last recognized token
	public Token la;   // lookahead token
	int errDist = minErrDist;
	
	public Scanner scanner;
	public Errors errors;

	

	public Parser(PascalContext context, Source source) {
		this.scanner = new Scanner(source.getInputStream());
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
			UsesDeclaration();
		}
		if (la.kind == 6) {
			VariableDeclaration();
		}
		if (la.kind == 11) {
			TypesDeclaration();
		}
		if (la.kind == 12 || la.kind == 15 || la.kind == 16) {
			while (la.kind == 12 || la.kind == 15) {
				if (la.kind == 12) {
					Function();
				} else {
					Procedure();
				}
			}
		}
		MainStatementsBlock();
	}

	void UsesDeclaration() {
		Expect(5);
	}

	void VariableDeclaration() {
		Expect(6);
		VariableTypeDeclaration();
		Expect(7);
		while (la.kind == 1 || la.kind == 7 || la.kind == 10) {
			VariableTypeDeclaration();
			Expect(7);
		}
	}

	void TypesDeclaration() {
		Expect(11);
	}

	void Function() {
		Expect(12);
		Expect(1);
		Expect(13);
		if (la.kind == 1 || la.kind == 14) {
			DefineArgumentsList();
		}
		Expect(14);
		Expect(9);
		Expect(1);
		Expect(7);
		VariableDeclaration();
		StatementsBlock();
	}

	void Procedure() {
		Expect(15);
		Expect(1);
		Expect(13);
		if (la.kind == 1 || la.kind == 14) {
			DefineArgumentsList();
		}
		Expect(14);
		Expect(7);
		VariableDeclaration();
		StatementsBlock();
	}

	void MainStatementsBlock() {
		Expect(16);
		Statement();
		while (StartOf(1)) {
			Statement();
		}
		Expect(17);
	}

	void VariableTypeDeclaration() {
		while (la.kind == 1 || la.kind == 10) {
			if (la.kind == 1) {
				SimpleVariableTypeDeclaration();
			} else {
				ArrayVariableTypeDeclaration();
			}
		}
	}

	void SimpleVariableTypeDeclaration() {
		Expect(1);
		while (la.kind == 8) {
			Get();
			Expect(1);
		}
		Expect(9);
		Expect(1);
	}

	void ArrayVariableTypeDeclaration() {
		Expect(10);
	}

	void DefineArgumentsList() {
		while (la.kind == 1) {
			Get();
			Expect(9);
			Expect(1);
			if (la.kind == 1 || la.kind == 8) {
				while (la.kind == 8) {
					Get();
					Expect(1);
					Expect(9);
					Expect(1);
				}
			}
		}
	}

	void StatementsBlock() {
		if (StartOf(1)) {
			Statement();
		} else if (la.kind == 16) {
			Get();
			Statement();
			while (StartOf(1)) {
				Statement();
			}
			Expect(18);
		} else SynErr(48);
	}

	void Statement() {
		switch (la.kind) {
		case 1: case 3: case 13: {
			Expression();
			Expect(7);
			break;
		}
		case 30: {
			IfStatement();
			break;
		}
		case 19: {
			CaseStatement();
			break;
		}
		case 22: {
			WhileStatement();
			break;
		}
		case 24: {
			RepeatStatement();
			break;
		}
		case 26: {
			ForStatement();
			break;
		}
		default: SynErr(49); break;
		}
	}

	void Expression() {
		LogicConjunction();
		if (la.kind == 33) {
			Get();
			LogicConjunction();
		}
	}

	void IfStatement() {
		Expect(30);
		Expression();
		Expect(31);
		StatementsBlock();
		if (la.kind == 32) {
			Get();
			StatementsBlock();
		}
	}

	void CaseStatement() {
		Expect(19);
		Expect(1);
		Expect(20);
		Expect(21);
		Expect(18);
	}

	void WhileStatement() {
		Expect(22);
		Expression();
		Expect(23);
		StatementsBlock();
	}

	void RepeatStatement() {
		Expect(24);
		StatementsBlock();
		Expect(25);
		Expression();
	}

	void ForStatement() {
		Expect(26);
		Expect(27);
		Assignment();
		if (la.kind == 28) {
			Get();
		} else if (la.kind == 29) {
			Get();
		} else SynErr(50);
		Expect(3);
		StatementsBlock();
	}

	void Assignment() {
		Expect(1);
		Expect(46);
		if (la.kind == 3) {
			Get();
		} else if (la.kind == 2) {
			Get();
		} else if (la.kind == 4) {
			Get();
		} else SynErr(51);
	}

	void LogicConjunction() {
		LogicFactor();
		if (la.kind == 34) {
			Get();
			LogicFactor();
		}
	}

	void LogicFactor() {
		Arithmetic();
		switch (la.kind) {
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
		case 39: {
			Get();
			break;
		}
		case 40: {
			Get();
			break;
		}
		default: SynErr(52); break;
		}
		Arithmetic();
	}

	void Arithmetic() {
		Term();
		while (la.kind == 41 || la.kind == 42) {
			if (la.kind == 41) {
				Get();
			} else {
				Get();
			}
			Term();
		}
	}

	void Term() {
		Factor();
		while (la.kind == 43 || la.kind == 44 || la.kind == 45) {
			if (la.kind == 43) {
				Get();
			} else if (la.kind == 44) {
				Get();
			} else {
				Get();
			}
			Factor();
		}
	}

	void Factor() {
		if (la.kind == 3) {
			Get();
		} else if (la.kind == 13) {
			Get();
			Expression();
			Expect(14);
		} else if (la.kind == 1) {
			Get();
			Expect(13);
			ArgumentList();
			Expect(14);
		} else if (la.kind == 1) {
			Assignment();
		} else SynErr(53);
	}

	void ArgumentList() {
		if (la.kind == 1 || la.kind == 3 || la.kind == 13) {
			Expression();
			while (la.kind == 8) {
				Get();
				Expression();
			}
		}
	}



	public void Parse() {
		la = new Token();
		la.val = "";		
		Get();
		Pascal();
		Expect(0);

	}

	private static final boolean[][] set = {
		{_T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x},
		{_x,_T,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_x,_x, _x,_x,_x,_T, _x,_x,_T,_x, _T,_x,_T,_x, _x,_x,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x}

	};
	
	public static void parsePascal(PascalContext context, Source source) {
        Parser parser = new Parser(context, source);
        parser.Parse();
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
			case 4: s = "charLiteral expected"; break;
			case 5: s = "\"unfinished_uses\" expected"; break;
			case 6: s = "\"var\" expected"; break;
			case 7: s = "\";\" expected"; break;
			case 8: s = "\",\" expected"; break;
			case 9: s = "\":\" expected"; break;
			case 10: s = "\"unfinished_a\" expected"; break;
			case 11: s = "\"unfinished_types\" expected"; break;
			case 12: s = "\"function\" expected"; break;
			case 13: s = "\"(\" expected"; break;
			case 14: s = "\")\" expected"; break;
			case 15: s = "\"procedure\" expected"; break;
			case 16: s = "\"begin\" expected"; break;
			case 17: s = "\"end.\" expected"; break;
			case 18: s = "\"end\" expected"; break;
			case 19: s = "\"case\" expected"; break;
			case 20: s = "\"of\" expected"; break;
			case 21: s = "\"unfinished_case\" expected"; break;
			case 22: s = "\"while\" expected"; break;
			case 23: s = "\"do\" expected"; break;
			case 24: s = "\"repeat\" expected"; break;
			case 25: s = "\"until\" expected"; break;
			case 26: s = "\"unfinished_for\" expected"; break;
			case 27: s = "\"for\" expected"; break;
			case 28: s = "\"to\" expected"; break;
			case 29: s = "\"downto\" expected"; break;
			case 30: s = "\"if\" expected"; break;
			case 31: s = "\"then\" expected"; break;
			case 32: s = "\"else\" expected"; break;
			case 33: s = "\"or\" expected"; break;
			case 34: s = "\"and\" expected"; break;
			case 35: s = "\"<\" expected"; break;
			case 36: s = "\"<=\" expected"; break;
			case 37: s = "\">\" expected"; break;
			case 38: s = "\">=\" expected"; break;
			case 39: s = "\"=\" expected"; break;
			case 40: s = "\"<>\" expected"; break;
			case 41: s = "\"+\" expected"; break;
			case 42: s = "\"-\" expected"; break;
			case 43: s = "\"*\" expected"; break;
			case 44: s = "\"div\" expected"; break;
			case 45: s = "\"mod\" expected"; break;
			case 46: s = "\":=\" expected"; break;
			case 47: s = "??? expected"; break;
			case 48: s = "invalid StatementsBlock"; break;
			case 49: s = "invalid Statement"; break;
			case 50: s = "invalid ForStatement"; break;
			case 51: s = "invalid Assignment"; break;
			case 52: s = "invalid LogicFactor"; break;
			case 53: s = "invalid Factor"; break;
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
