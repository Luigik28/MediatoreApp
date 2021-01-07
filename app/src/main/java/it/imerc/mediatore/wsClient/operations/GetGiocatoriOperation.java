package it.imerc.mediatore.wsClient.operations;

import java.util.LinkedList;

import it.imerc.mediatore.Game.Giocatore;
import it.imerc.mediatore.wsClient.MediatoreOperation;
import it.imerc.mediatore.wsClient.operations.callback.GiocatoriCallback;

public class GetGiocatoriOperation extends MediatoreOperation<LinkedList<Giocatore>> {

    @Override
    public String getOperationName() {
        return "getGiocatori";
    }

    public void doCall(String id, GiocatoriCallback callback) {
        this.addProperty("id", id);
        super.call(callback);
    }
}
