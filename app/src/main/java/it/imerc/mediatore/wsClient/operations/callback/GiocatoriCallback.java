package it.imerc.mediatore.wsClient.operations.callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.SoapPrimitive;

import java.lang.reflect.Type;
import java.util.LinkedList;

import it.imerc.mediatore.Game.Giocatore;

public abstract class GiocatoriCallback implements SoapCallback<LinkedList<Giocatore>> {
    @Override
    abstract public void onResponse(LinkedList<Giocatore> response);

    @Override
    public void onError(String message) {

    }

    @Override
    public LinkedList<Giocatore> parseRequest(AttributeContainer attributeContainer) {
        LinkedList<Giocatore> list = new LinkedList<>();
        Type listOfMyClassObject = new TypeToken<LinkedList<Giocatore>>() {}.getType();
        return new Gson().fromJson(((SoapPrimitive) attributeContainer).getValue().toString(), listOfMyClassObject);
    }
}