package it.imerc.mediatore.wsClient;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import it.imerc.mediatore.wsClient.operations.callback.SoapCallback;

public abstract class MediatoreOperation<T> {

    private static final String WSDL_TARGET_NAMESPACE = "http://service.iMerc.it";

    private static String serverUrl = "http://mediatore-sviluppo.ddns.net";

    private static final String SOAP_ADDRESS_NO_URL = "/Mediatore/services/GameService?wsdl";

    private SoapObject request;

    private SoapSerializationEnvelope envelope;

    public abstract String getOperationName();

    public static String getSoapAddress() {
        return serverUrl + SOAP_ADDRESS_NO_URL;
    }

    public String getSoapAction() {
        return serverUrl + "/Mediatore/services/GameService/" + getOperationName();
    }

    public MediatoreOperation() {
        request = new SoapObject(WSDL_TARGET_NAMESPACE,
                getOperationName());
        envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
    }

    protected SoapObject addProperty(String propertyName, Object propertyValue) {
        return request.addProperty(propertyName, propertyValue);
    }

    protected <R extends SoapCallback<T>> void call(R callback) {
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(getSoapAddress());
        new SoapCallTask(callback).execute(httpTransport);
    }

    @SuppressLint("StaticFieldLeak")
    private class SoapCallTask extends AsyncTask<HttpTransportSE, Void, AttributeContainer> {

        final SoapCallback<T> callback;

        private SoapCallTask(SoapCallback<T> callback) {
            this.callback = callback;
        }

        @Override
        protected AttributeContainer doInBackground(HttpTransportSE... httpTransport) {
            AttributeContainer result;
            try {
                httpTransport[0].call(getSoapAction(), envelope);
                result = (AttributeContainer) envelope.getResponse();
            } catch (SoapFault fault) {
                fault.printStackTrace();
                result = new SoapObject();
                String exception = fault.faultstring.split(":")[0];
                String message = fault.faultstring.split(":")[1];
                result.addAttribute("exception", exception);
                result.addAttribute("message", message);
            } catch (IOException e) {
                e.printStackTrace();
                result = new SoapObject();
                result.addAttribute("exception", e.getClass().toString());
                result.addAttribute("message", e.getMessage());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                result = new SoapObject();
                result.addAttribute("exception", e.getClass().toString());
                result.addAttribute("message", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(AttributeContainer result) {
            if (result instanceof  SoapObject)
                callback.onError(result.getAttributeAsString("message"));
            else
                callback.onResponse(callback.parseRequest(result));
        }
    }
}
