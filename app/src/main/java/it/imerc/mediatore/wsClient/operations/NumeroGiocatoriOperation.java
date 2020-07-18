package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.wsClient.MediatoreOperation;
import it.imerc.mediatore.wsClient.operations.callback.IntegerCallback;

public class NumeroGiocatoriOperation extends MediatoreOperation<Integer> {

    @Override
    public String getOperationName() {
        return "numeroGiocatori";
    }

    public void doCall(int id, IntegerCallback callback) {
        this.addProperty("id", id);
        super.call(callback);
    }
}
