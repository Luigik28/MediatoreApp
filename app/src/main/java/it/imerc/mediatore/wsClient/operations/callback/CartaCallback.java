package it.imerc.mediatore.wsClient.operations.callback;

import com.google.gson.Gson;

import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.SoapPrimitive;

import it.imerc.mediatore.Game.Carta;

public abstract class CartaCallback implements SoapCallback<Carta> {
    @Override
    abstract public void onResponse(Carta response);

    @Override
    public void onError(String message) {

    }

    @Override
    public Carta parseRequest(AttributeContainer attributeContainer) {
        return new Gson().fromJson(((SoapPrimitive) attributeContainer).getValue().toString(), Carta.class);
    }
}