package it.imerc.mediatore.wsClient.operations.callback;

import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.SoapPrimitive;

public abstract class BooleanCallback implements SoapCallback<Boolean> {
    @Override
    abstract public void onResponse(Boolean response);

    @Override
    public void onError(String message) {

    }

    @Override
    public Boolean parseRequest(AttributeContainer attributeContainer) {
        SoapPrimitive soapPrimitive = (SoapPrimitive) attributeContainer;
        return Boolean.valueOf(soapPrimitive.getValue().toString());
    }
}