package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.wsClient.MediatoreOperation;
import it.imerc.mediatore.wsClient.operations.callback.BooleanCallback;

public class StartGameOperation extends MediatoreOperation<Boolean> {

    @Override
    public String getOperationName() {
        return "startGame";
    }

    public void doCall(String id, BooleanCallback callback) {
        this.addProperty("id", id);
        super.call(callback);
    }

}
