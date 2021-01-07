package it.imerc.mediatore.wsClient.operations.callback;

import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.SoapPrimitive;

public abstract class StringCallback implements SoapCallback<String> {
    @Override
    abstract public void onResponse(String response);

    @Override
    public void onError(String message) {

    }

    @Override
    public String parseRequest(AttributeContainer attributeContainer) {
        SoapPrimitive soapPrimitive = (SoapPrimitive) attributeContainer;
        return soapPrimitive.getValue().toString();
    }
}