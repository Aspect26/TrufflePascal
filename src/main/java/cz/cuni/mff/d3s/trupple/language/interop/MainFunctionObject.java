package cz.cuni.mff.d3s.trupple.language.interop;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.RootNode;

public class MainFunctionObject implements TruffleObject {

    private final RootNode rootNode;

    public MainFunctionObject(RootNode rootNode) {
        this.rootNode = rootNode;
    }

    @Override
    public ForeignAccess getForeignAccess() {
        ForeignAccess.Factory factory = new ForeignAccess.Factory() {

            @Override
            public boolean canHandle(TruffleObject truffleObject) {
                return false;
            }

            @Override
            public CallTarget accessMessage(Message message) {
                return Truffle.getRuntime().createCallTarget(rootNode);
            }

        };
        return ForeignAccess.create(factory);
    }

}
