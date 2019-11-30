package ru.zuma;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.zuma.interfaces.Procedure;

import java.io.*;

public class ProcedureClassLoader extends ClassLoader {
    protected static final Logger log = LogManager.getLogger(ProcedureClassLoader.class);

    public Procedure instance(String filePath) {
        try {
            Class clazz = findClass(filePath);
            if (clazz.isAssignableFrom(Procedure.class)) {
                return ((Class<Procedure>)clazz).getConstructor().newInstance();
            } else return null;
        } catch (ReflectiveOperationException e) {
            log.error("", e);
            return null;
        }
    }

    @Override
    public Class findClass(String path) {
        byte[] b = loadClassFromFile(path);
        return defineClass(new File(path).getName(), b, 0, b.length);
    }

    private byte[] loadClassFromFile(String path)  {
        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            InputStream inputStream = new FileInputStream(new File(path + ".class"));
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

