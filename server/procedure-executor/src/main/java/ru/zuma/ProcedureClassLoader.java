package ru.zuma;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.zuma.interfaces.Procedure;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class ProcedureClassLoader extends ClassLoader {
    protected static final Logger log = LogManager.getLogger(ProcedureClassLoader.class);

    public Procedure instance(Path filePath) {
        log.debug("Hello, zuma!");
        log.error("Hello, zuma!");
        try {
            Class clazz = findClass(filePath.toString());
            if (clazz.isAssignableFrom(Procedure.class)) {
                return ((Class<Procedure>)clazz).getConstructor().newInstance();
            } else return null;
        } catch (ReflectiveOperationException e) {
            log.error("", e);
            return null;
        }
    }

    @Override
    public Class findClass(String name) {
        byte[] b = loadClassFromFile(name);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassFromFile(String fileName)  {
        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            InputStream inputStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(fileName + ".class");
            int nextValue = 0;

            while ((nextValue = inputStream.read()) != -1) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            log.error("", e);
        }
        buffer = byteStream.toByteArray();
        return buffer;
    }
}

