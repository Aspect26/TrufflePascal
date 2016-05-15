package pascal.language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.Source;

import pascal.language.parser.Parser;
import pascal.language.runtime.PascalContext;

@TruffleLanguage.Registration(name = "Pascal", version = "0.01", mimeType = "text/x-pascal")
public final class PascalLanguage extends TruffleLanguage<PascalContext>{

	public static final PascalLanguage INSTANCE = new PascalLanguage();
	public static final String builtinKind = "Pascal builtin";
	
	@Override
	protected PascalContext createContext(com.oracle.truffle.api.TruffleLanguage.Env env) {
		/*return new PascalContext(env, new BufferedReader(new InputStreamReader(env.in())),
				new PrintWriter(env.out(), true));*/
		
		return null;
	}

	@Override
	protected CallTarget parse(Source code, Node context, String... argumentNames) throws IOException {
		final PascalContext _context = new PascalContext();
		_context.evalSource(code);
		
		RootNode rootNode = new RootNode(PascalLanguage.class, null, null){

			@Override
			public Object execute(VirtualFrame frame) {
				CompilerDirectives.transferToInterpreter();

				Object[] arguments = frame.getArguments();
				Node callNode = Message.createExecute(arguments.length).createNode();
				try {
                    return ForeignAccess.sendExecute(callNode, frame, null, arguments);
                } catch (UnsupportedTypeException | ArityException | UnsupportedMessageException e) {
                    return null;
                }
			}
		};
		
		return Truffle.getRuntime().createCallTarget(rootNode);
	}

	@Override
	protected Object findExportedSymbol(PascalContext context, String globalName, boolean onlyExplicit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object getLanguageGlobal(PascalContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isObjectOfLanguage(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Object evalInContext(Source source, Node node, MaterializedFrame mFrame) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		/*if(args.length != 1){
			System.out.println("The compiler takes exactly 1 parameters, which is path to the source code");
			return;
		}
		
		Source source;
		source = Source.fromFileName("test.pas");
		
		PolyglotEngine vm = PolyglotEngine.newBuilder().build();
		vm.eval(source);
		Value main = vm.findGlobalSymbol("BEGIN");
		
		if(main == null){
			throw new PascalException("No BEGIN defined...");
		}
		
		main.execute();*/
		Parser parser = new Parser(new PascalContext(), Source.fromFileName("test.pas"));
		parser.Parse();
		if(!parser.noErrors()){
			System.out.println("Errors while parsing, the code cannot be interpreted...");
			return;
		}
			
		Truffle.getRuntime().createCallTarget(parser.mainNode).call();
	}
	
	public Node createFindContextNode1() {
        return createFindContextNode();
    }

    public PascalContext findContext1(Node contextNode) {
        return findContext(contextNode);
    }
}
