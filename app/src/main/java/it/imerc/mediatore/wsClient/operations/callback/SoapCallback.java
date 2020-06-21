package it.imerc.mediatore.wsClient.operations.callback;

import org.ksoap2.serialization.AttributeContainer;

public interface SoapCallback<T> {
    void onResponse(T response);

    T parseRequest(AttributeContainer attributeContainer);

    void onError(String message);
}