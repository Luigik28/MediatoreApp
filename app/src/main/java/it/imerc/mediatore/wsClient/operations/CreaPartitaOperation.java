package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.wsClient.MediatoreOperation;

public class CreaPartitaOperation extends MediatoreOperation<Integer> {

    @Override
    public String getOperationName() {
        return "creaPartita";
    }

    public void doCall(String nome, IntegerCallback callback) {
        addProperty("nome", nome);
        super.call(callback);
    }

}
