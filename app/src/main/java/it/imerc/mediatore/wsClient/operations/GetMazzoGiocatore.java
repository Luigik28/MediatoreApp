package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.Game.Mazzo;
import it.imerc.mediatore.wsClient.MediatoreOperation;
import it.imerc.mediatore.wsClient.operations.callback.MazzoCallback;

public class GetMazzoGiocatore extends MediatoreOperation<Mazzo> {

    @Override
    public String getOperationName() {
        return "getMazzoGiocatore";
    }

    public void doCall(String id, String nome, MazzoCallback callback) {
        this.addProperty("idGame", id).addProperty("player", nome);
        super.call(callback);
    }
}
