package it.imerc.mediatore.wsClient.operations.callback;

import com.google.gson.Gson;

import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.SoapPrimitive;

import it.imerc.mediatore.Game.Mazzo;

public abstract class MazzoCallback implements SoapCallback<Mazzo> {
    @Override
    abstract public void onResponse(Mazzo response);

    @Override
    public void onError(String message) {

    }

    @Override
    public Mazzo parseRequest(AttributeContainer attributeContainer) {
        return new Gson().fromJson(((SoapPrimitive) attributeContainer).getValue().toString(), Mazzo.class);
    }
}