package ru.zuma;

import cc.blynk.server.core.model.enums.PinType;
import ru.zuma.interfaces.PinValueWriteHandler;

import java.lang.reflect.Constructor;

public class LoadPlugin {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        ProcedureClassLoader classLoader = new ProcedureClassLoader();
        Class<PinValueWriteHandler> clazz = classLoader.findClass("SimplePinValueWriteHandler");
        Constructor<PinValueWriteHandler> constructor = clazz.getConstructor();
        PinValueWriteHandler handler = clazz.newInstance();
        handler.onPinValueWrite((short)0, PinType.VIRTUAL, "10");
    }
}
