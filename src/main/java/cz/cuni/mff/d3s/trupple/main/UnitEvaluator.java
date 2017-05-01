package cz.cuni.mff.d3s.trupple.main;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;
import cz.cuni.mff.d3s.trupple.language.PascalLanguage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UnitEvaluator {

    public static void evalUnits(PolyglotEngine engine, List<String> unitDirectories) throws Exception {
        for (String directory : unitDirectories) {
            evalIncludeDirectory(engine, directory);
        }
    }

    private static void evalIncludeDirectory(PolyglotEngine engine, String directory) throws Exception {
        File[] filesInDirectory = new File(directory).listFiles();
        if (filesInDirectory == null) {
            return;
        }

        Arrays.sort(filesInDirectory); // TODO: this does not need to be here -> only for testing purposes
        for (File unit : filesInDirectory) {
            if (unit.isFile() && unit.getAbsolutePath().endsWith(".pas")) {
                evalSource(engine, createSource(unit));
            }
        }
    }

    private static void evalSource(PolyglotEngine engine, Source source) throws Exception {
        try {
            engine.eval(source);
        } catch (Throwable t) {
            if (!t.getMessage().contains("Parsing has not produced a CallTarget")) {
                throw new Exception("Errors while parsing source " + source.getName() + ".", t);
            }
        }
    }

    private static Source createSource(File file) throws IOException {
        return Source.newBuilder(file)
                .mimeType(PascalLanguage.MIME_TYPE)
                .build();
    }

}
