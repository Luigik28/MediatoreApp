package it.imerc.mediatore.wsClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import it.imerc.mediatore.R;
import it.imerc.mediatore.util.Utility;
import it.imerc.mediatore.wsClient.operations.callback.SoapCallback;

public abstract class MediatoreOperation<T> {

    private static final String WSDL_TARGET_NAMESPACE = "http://service.iMerc.it";

    private static String localServer = null;

    public static int timeout = 5000;

    private static final String SOAP_ADDRESS_NO_URL = "/services/GameService?wsdl";

    private final SoapObject request;

    private final SoapSerializationEnvelope envelope;

    public abstract String getOperationName();

    public static void setLocalServer(String serverUrl) {
        localServer = serverUrl;
    }

    public static String getServerUrl() {
        String serverUrl = "https://mediatore.herokuapp.com/";
        return localServer == null ? serverUrl : localServer;
    }

    public static String getSoapAddress() {
        return getServerUrl() + SOAP_ADDRESS_NO_URL;
    }

    public String getSoapAction() {
        return getServerUrl() + "/services/GameService/" + getOperationName();
    }

    public static void loadPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferencesKey), Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean(Utility.debugKey, false)) {
            localServer = "http://";
            localServer += sharedPreferences.getString(Utility.ipAddressKey, context.getString(R.string.defaultIp));
            localServer += ":" + sharedPreferences.getString(Utility.portAddressKey, context.getString(R.string.defaultPort));
            MediatoreOperation.setLocalServer(localServer);
        }
        timeout = sharedPreferences.getInt(Utility.timeoutKey, timeout);
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

    protected <Z extends SoapCallback<T>> void call(Z callback) {
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(getSoapAddress(), timeout);
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
            if (result instanceof SoapObject)
                callback.onError(result.getAttributeAsString("message"));
            else
                callback.onResponse(callback.parseRequest(result));
        }
    }
}
