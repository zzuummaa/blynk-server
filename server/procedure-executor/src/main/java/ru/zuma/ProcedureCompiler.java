package ru.zuma;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProcedureCompiler {
    protected static final Logger log = LogManager.getLogger(ProcedureCompiler.class);

    private String compilerPath;
    private String targetDir;
    private String classPath;

    public ProcedureCompiler(String compilerFileName, String targetDir) {
        if (Files.isExecutable(Paths.get(compilerFileName))) {
            this.compilerPath = compilerFileName;
            this.targetDir = targetDir;
            try {
                this.classPath = ".;" + new File(ProcedureCompiler.class.getProtectionDomain().getCodeSource()
                        .getLocation().toURI()).getPath();
            } catch (URISyntaxException e) {
                log.error("", e);
            }
        } else {
            log.debug("ProcedureCompiler: file \"" + compilerFileName + "\" is not executable");
        }
    }

    public boolean compile(String filename) {
        if (compilerPath == null) return false;
        File file = new File(filename);
        String[] params = new String[]{"-d", targetDir, "-cp", classPath, file.getName()};
        try {
            Process process = Runtime.getRuntime().exec(compilerPath, params, file.getParentFile());
            StringWriter writer = new StringWriter();
            IOUtils.copy(new InputStreamReader(process.getInputStream()), writer);
            String compillerOuput = writer.toString();
            return process.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            log.error("", e);
            return false;
        }
    }
}
