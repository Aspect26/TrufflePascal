package cz.cuni.mff.d3s.trupple.main.exceptions;

public abstract class CompilerException extends Exception {

    protected enum ExitCodesEnum {

        EXIT_CODE_WRONG_OPTIONS (1),
        EXIT_CODE_SOURCE_DOESNT_EXIST (2),
        EXIT_CODE_WRONG_STANDARD (3);

        private final int code;

        ExitCodesEnum(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }

    private ExitCodesEnum exitCode;
    String message;

    CompilerException(String message, ExitCodesEnum exitCode) {
        this.exitCode = exitCode;
        this.message = message;
    }

    CompilerException(ExitCodesEnum exitCode) {
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return this.exitCode.getCode();
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
