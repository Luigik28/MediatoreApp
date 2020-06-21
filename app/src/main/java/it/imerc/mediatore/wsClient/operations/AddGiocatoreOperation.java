package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.Game.Giocatore;
import it.imerc.mediatore.wsClient.MediatoreOperation;
import it.imerc.mediatore.wsClient.operations.callback.GiocatoreCallback;

public class AddGiocatoreOperation extends MediatoreOperation<Giocatore> {

    @Override
    public String getOperationName() {
        return "addGiocatore";
    }

    public void doCall(Integer id, String nome, GiocatoreCallback callback) {
        this.addProperty("id", id).addProperty("nome", nome);
        super.call(callback);
    }
}
