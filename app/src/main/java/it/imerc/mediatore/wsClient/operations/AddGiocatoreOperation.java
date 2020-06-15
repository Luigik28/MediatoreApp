package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.wsClient.MediatoreOperation;

public class AddGiocatoreOperation extends MediatoreOperation<Boolean> {

    @Override
    public String getOperationName() {
        return "addGiocatore";
    }

    public void doCall(Integer id, String nome, BooleanCallback callback) {
        this.addProperty("id", id).addProperty("nome", nome);
        super.call(callback);
    }

}
