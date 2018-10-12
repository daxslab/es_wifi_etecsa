package com.daxslab.eswifi_etecsa.connectivity;

import android.content.Context;
import android.os.AsyncTask;

import com.daxslab.eswifi_etecsa.R;
import com.daxslab.eswifi_etecsa.utils.NotificationUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.SSLHandshakeException;

/**
 * Background task for checking nauta captive portal address and certificate.
 */
class CheckPortal extends AsyncTask<String, Void, Boolean> {


    private Context mContext;
    private boolean isWifiOk;

    CheckPortal(Context mContext){
        super();
        this.mContext = mContext;
        this.isWifiOk = true;
    }

    /**
     * Background task for checking nauta captive portal address and certificate.
     * @param params array of Strings with an url in position 0
     * @return true if connection was successful
     */
    protected Boolean doInBackground(String... params) {
        boolean isConnectionOk =true;
        try {
            isConnectionOk = checkConnection(params[0]);
        }catch (SSLHandshakeException ex){
            NotificationUtils.createNotification(mContext, WifiReceiver.SSL_WARNING_NOTIFICATION_ID, mContext.getString(R.string.ssl_warning), mContext.getString(R.string.not_valid_ssl));
            this.isWifiOk = false;
        }catch (Exception ex){
            NotificationUtils.createNotification(mContext, WifiReceiver.CANT_VERIFY_SSL_WARNING_NOTIFICATION_ID, mContext.getString(R.string.cant_verify_ssl_warning), mContext.getString(R.string.cant_verify_ssl));
            this.isWifiOk = false;
        }

        return isConnectionOk;
    }

    /**
     * @param checkUrl
     * @return true if connection was successful
     * @throws InterruptedException
     * @throws IOException
     */
    private boolean checkConnection(String checkUrl) throws InterruptedException, IOException {
        HttpURLConnection response = null;
            URL url = new URL(checkUrl);
            response = (HttpURLConnection) url.openConnection();
            response.setRequestMethod("GET");
            response.connect();
            checkResponse(response);
            return true;
    }

    /**
     * @param response
     * @throws IOException
     */
    private void checkResponse(HttpURLConnection response) throws IOException {
        int statusCode = response.getResponseCode();
        if (HttpURLConnection.HTTP_OK != statusCode) {
            throw new IOException("HttpStatus: " + statusCode);
        }
    }

}
