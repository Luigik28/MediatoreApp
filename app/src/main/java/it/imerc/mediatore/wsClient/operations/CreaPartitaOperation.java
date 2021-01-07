package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.wsClient.MediatoreOperation;
import it.imerc.mediatore.wsClient.operations.callback.StringCallback;

public class CreaPartitaOperation extends MediatoreOperation<String> {

    @Override
    public String getOperationName() {
        return "creaPartita";
    }

    public void doCall(String partita, String giocatore, StringCallback callback) {
        this.addProperty("partita", partita).addProperty("giocatore", giocatore);
        super.call(callback);
    }
}
