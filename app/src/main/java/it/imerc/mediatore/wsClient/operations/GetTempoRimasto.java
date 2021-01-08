package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.wsClient.MediatoreOperation;
import it.imerc.mediatore.wsClient.operations.callback.IntegerCallback;

public class GetTempoRimasto extends MediatoreOperation<Integer> {

    @Override
    public String getOperationName() {
        return "getRemainingTime";
    }

    public void doCall(String id, IntegerCallback callback) {
        this.addProperty("id", id);
        super.call(callback);
    }
}
