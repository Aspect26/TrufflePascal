package cz.cuni.mff.d3s.trupple.main;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;
import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import cz.cuni.mff.d3s.trupple.language.PascalParseException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class UnitEvaluator {

    static void evalUnits(PolyglotEngine engine, List<String> unitDirectories) throws IOException {
        for (String directory : unitDirectories) {
            evalIncludeDirectory(engine, directory);
        }
    }

    private static void evalIncludeDirectory(PolyglotEngine engine, String directory) throws IOException {
        File[] filesInDirectory = new File(directory).listFiles();
        if (filesInDirectory == null) {
            return;
        }

        Arrays.sort(filesInDirectory); // TODO: this does not need to be here -> only for testing purposes
        for (File unit : filesInDirectory) {
            if (unit.isFile() && unit.getAbsolutePath().endsWith(".pas")) {
                engine.eval(createSource(unit));
            }
        }
    }

    private static Source createSource(File file) throws IOException {
        return Source.newBuilder(file)
                .mimeType(PascalLanguage.MIME_TYPE)
                .build();
    }

}
