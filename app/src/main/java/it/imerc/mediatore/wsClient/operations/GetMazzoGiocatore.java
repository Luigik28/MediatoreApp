package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.Game.Mazzo;
import it.imerc.mediatore.wsClient.MediatoreOperation;

public class GetMazzoGiocatore extends MediatoreOperation<Mazzo> {

    @Override
    public String getOperationName() {
        return "getMazzoGiocatore";
    }

    public void doCall(int id, String nome, MazzoCallback callback) {
        this.addProperty("idGame", id).addProperty("player", nome);
        super.call(callback);
    }
}
