package ru.zuma.interfaces;

import cc.blynk.server.core.model.enums.PinType;

public interface PinValueWriteHandler extends Procedure {
    void onPinValueWrite(short pin, PinType pinType, String value);
}
