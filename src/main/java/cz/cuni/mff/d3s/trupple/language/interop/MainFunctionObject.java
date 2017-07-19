package cz.cuni.mff.d3s.trupple.language.interop;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.RootNode;

/**
 * Initial evaluation of a Pascal source returns an instance of this class. It implements {@link TruffleObject} so it
 * can be used inside other Truffle-based languages. Our implementation also allows execution of these instances which
 * results in executing the main block of the interpreted Pascal source.
 */
public class MainFunctionObject implements TruffleObject {

    /**
     * The root node of the main block of the interpreted Pascal source.
     */
    private final RootNode rootNode;

    public MainFunctionObject(RootNode rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * Returns a {@link ForeignAccess} object that is executable outside our language.
     */
    @Override
    public ForeignAccess getForeignAccess() {
        ForeignAccess.Factory26 factory = new ForeignAccess.Factory26() {
            @Override
            public CallTarget accessIsNull() {
                return null;
            }

            @Override
            public CallTarget accessIsExecutable() {
                return null;
            }

            @Override
            public CallTarget accessIsBoxed() {
                return null;
            }

            @Override
            public CallTarget accessHasSize() {
                return null;
            }

            @Override
            public CallTarget accessGetSize() {
                return null;
            }

            @Override
            public CallTarget accessUnbox() {
                return null;
            }

            @Override
            public CallTarget accessRead() {
                return null;
            }

            @Override
            public CallTarget accessWrite() {
                return null;
            }

            @Override
            public CallTarget accessExecute(int i) {
                return Truffle.getRuntime().createCallTarget(rootNode);
            }

            @Override
            public CallTarget accessInvoke(int i) {
                return null;
            }

            @Override
            public CallTarget accessNew(int i) {
                return null;
            }

            @Override
            public CallTarget accessKeys() {
                return null;
            }

            @Override
            public CallTarget accessKeyInfo() {
                return null;
            }

            @Override
            public CallTarget accessMessage(Message message) {
                return null;
            }
        };

        return ForeignAccess.create(MainFunctionObject.class, factory);
    }

}
