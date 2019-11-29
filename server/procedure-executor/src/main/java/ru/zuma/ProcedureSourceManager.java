package ru.zuma;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.createDirectories;
import static java.util.stream.Collectors.toList;

public class ProcedureSourceManager {
    private static final String PROCEDURE_DIR_NAME = "procedure";
    private static final String SOURCES_DIR_NAME = "sources";
    private static final String CLASSES_DIR_NAME = "classes";

    private List<Path> sources;

    private Path procedureDir;
    private Path sourcesDir;
    private Path classesDir;

    public ProcedureSourceManager(String dataFolder) {
        this.sources = new ArrayList<>();

        try {
            this.procedureDir = createDirectories(Paths.get(dataFolder, PROCEDURE_DIR_NAME));
            this.sourcesDir = createDirectories(Paths.get(dataFolder, PROCEDURE_DIR_NAME, SOURCES_DIR_NAME));
            this.classesDir = createDirectories(Paths.get(dataFolder, PROCEDURE_DIR_NAME, CLASSES_DIR_NAME));

            this.sources = Files.walk(this.sourcesDir).filter(Files::isRegularFile).collect(toList());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public List<Path> getSourcePaths() {
        return sources;
    }

    public Path getSourcesDir() {
        return sourcesDir;
    }

    public Path getClassesDir() {
        return classesDir;
    }
}
