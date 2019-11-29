import cc.blynk.server.core.model.enums.PinType;
import ru.zuma.interfaces.PinValueWriteHandler;

public class SimplePinValueWriteHandler implements PinValueWriteHandler {
    @Override
    public void onPinValueWrite(short pin, PinType pinType, String value) {
        System.out.printf("write: %s%d %s", pinType.pinTypeString, pin, value);
    }
}
