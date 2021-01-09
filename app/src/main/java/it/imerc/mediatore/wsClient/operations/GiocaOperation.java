package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.wsClient.MediatoreOperation;
import it.imerc.mediatore.wsClient.operations.callback.IntegerCallback;

public class GiocaOperation extends MediatoreOperation<Integer> {

    @Override
    public String getOperationName() {
        return "gioca";
    }

    public void doCall(String id, int mossa, IntegerCallback callback) {
        this.addProperty("id", id).addProperty("mossa", mossa);
        super.call(callback);
    }
}
