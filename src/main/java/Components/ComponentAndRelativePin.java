package Components;

import java.io.Serial;
import java.io.Serializable;

public class ComponentAndRelativePin implements Serializable {
    @Serial
    private static final long serialVersionUID = 3408224211117670188L;
    Component component;
    int relativePin;
    public ComponentAndRelativePin(Component c,int pinConnected){
        this.component = c;
        this.relativePin = pinConnected;
    }

    public Component getComponent(){
        return component;
    }
    public int getPin(){
        return relativePin;
    }
}
