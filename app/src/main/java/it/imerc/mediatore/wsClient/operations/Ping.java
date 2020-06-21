package it.imerc.mediatore.wsClient.operations;

import it.imerc.mediatore.wsClient.MediatoreOperation;
import it.imerc.mediatore.wsClient.operations.callback.SoapCallback;

public class Ping<U> extends MediatoreOperation<String> {

    public interface PingCallback extends SoapCallback<String> {
        @Override
        void onResponse(String response);
    }

    public Ping(String ping) {
        super();
        addProperty("n", ping);
    }

    @Override
    public String getOperationName() {
        return "ciao";
    }

    public void doCall(PingCallback callback) {
        super.call(callback);
    }
}
