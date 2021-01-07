package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.Game.Carta;
import it.imerc.mediatore.wsClient.MediatoreOperation;
import it.imerc.mediatore.wsClient.operations.callback.CartaCallback;

public class GetTrionfoOperation extends MediatoreOperation<Carta> {

    @Override
    public String getOperationName() {
        return "getTrionfo";
    }

    public void doCall(String id, CartaCallback callback) {
        this.addProperty("id", id);
        super.call(callback);
    }
}
