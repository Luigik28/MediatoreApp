package it.imerc.mediatore.wsClient.operations.callback;

import com.google.gson.Gson;

import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.SoapPrimitive;

import it.imerc.mediatore.Game.Giocatore;

public abstract class GiocatoreCallback implements SoapCallback<Giocatore> {
    @Override
    abstract public void onResponse(Giocatore response);

    @Override
    public void onError(String message) {

    }

    @Override
    public Giocatore parseRequest(AttributeContainer attributeContainer) {
        return new Gson().fromJson(((SoapPrimitive) attributeContainer).getValue().toString(), Giocatore.class);
    }
}