package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.wsClient.MediatoreOperation;
import it.imerc.mediatore.wsClient.operations.callback.BooleanCallback;

public class SetMonteOperation extends MediatoreOperation<Boolean> {

    @Override
    public String getOperationName() {
        return "setMonte";
    }

    public void doCall(Integer id, Boolean monte, BooleanCallback callback) {
        this.addProperty("id", id).addProperty("monte", monte);
        super.call(callback);
    }

}
