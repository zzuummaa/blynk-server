package ru.zuma;

import cc.blynk.server.ProcedureManager;
import cc.blynk.server.core.model.auth.User;
import cc.blynk.server.core.model.enums.PinType;
import ru.zuma.interfaces.PinValueWriteHandler;
import ru.zuma.interfaces.Procedure;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ProcedureManagerImpl implements ProcedureManager {
    private final String classesDir;
    private ProcedureSourceManager sourceManager;
    private final ProcedureClassLoader classLoader;
    private ProcedureCompiler compiler;
    private List<Path> sources;
    private List<Procedure> procedures;

    public ProcedureManagerImpl(String dataFolder, String compilerName) {
        this.sourceManager = new ProcedureSourceManager(dataFolder);
        this.classLoader = new ProcedureClassLoader();
        this.sources = this.sourceManager.getSourcePaths();
        this.classesDir = this.sourceManager.getClassesDir().toString();
        this.compiler = new ProcedureCompiler(compilerName, this.sourceManager.getClassesDir().toString());
        procedures = this.sources.stream().map(path -> {
            if (this.compiler.compile(path.toString())) {
                String fileName = path.getFileName().toString().split("\\.")[0];
                return classLoader.instance(this.classesDir + File.separator + fileName);
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(toList());
    }

    @Override
    public void handleUpdatePinValue(User user, int deviceId, short pin, PinType pinType, String value) {
        for (Procedure procedure: procedures) {
            if (procedure.getClass().isAssignableFrom(PinValueWriteHandler.class)) {
                ((PinValueWriteHandler)procedure).onPinValueWrite(pin, pinType, value);
            }
        }
    }
}
