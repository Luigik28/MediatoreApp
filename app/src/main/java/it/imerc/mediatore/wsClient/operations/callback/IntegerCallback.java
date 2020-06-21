package it.imerc.mediatore.wsClient.operations.callback;

import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.SoapPrimitive;

public abstract class IntegerCallback implements SoapCallback<Integer> {
    @Override
    abstract public void onResponse(Integer response);

    @Override
    public void onError(String message) {

    }

    @Override
    public Integer parseRequest(AttributeContainer attributeContainer) {
        SoapPrimitive soapPrimitive = (SoapPrimitive) attributeContainer;
        return Integer.valueOf(soapPrimitive.getValue().toString());
    }
}