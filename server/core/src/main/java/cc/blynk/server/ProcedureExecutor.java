package cc.blynk.server;

import cc.blynk.server.core.model.auth.User;
import cc.blynk.server.core.model.enums.PinType;

public interface ProcedureExecutor {
    void handleUpdatePinValue(User user, int deviceId, short pin, PinType pinType, String value);
}
