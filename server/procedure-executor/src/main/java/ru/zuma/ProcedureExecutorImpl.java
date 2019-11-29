package ru.zuma;

import cc.blynk.server.ProcedureExecutor;
import cc.blynk.server.core.model.auth.User;
import cc.blynk.server.core.model.enums.PinType;
import ru.zuma.interfaces.PinValueWriteHandler;
import ru.zuma.interfaces.Procedure;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ProcedureExecutorImpl implements ProcedureExecutor {
    private ProcedureSourceManager sourceManager;
    private final ProcedureClassLoader classLoader;
    private ProcedureCompiler compiler;
    private List<Path> sources;
    private List<Procedure> procedures;

    public ProcedureExecutorImpl(String dataFolder) {
        this.sourceManager = new ProcedureSourceManager(dataFolder);
        this.classLoader = new ProcedureClassLoader();
        this.sources = this.sourceManager.getSourcePaths();
        this.compiler = new ProcedureCompiler(
                "C:\\Program Files\\JetBrains\\IntelliJ IDEA Community Edition 2019.2.2\\jbr\\bin\\javac.exe",
                this.sourceManager.getClassesDir().toString());

        procedures = this.sources.stream().map(path -> {
            if (this.compiler.compile(path.toString())) {
                return classLoader.instance(path.getFileName());
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
