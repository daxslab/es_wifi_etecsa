package com.daxslab.eswifi_etecsa.connectivity;

import android.content.Context;
import android.os.AsyncTask;

import com.daxslab.eswifi_etecsa.R;
import com.daxslab.eswifi_etecsa.utils.NotificationUtils;
import com.daxslab.eswifi_etecsa.utils.TraceRouteUtils;
import com.qiniu.android.netdiag.Output;
import com.qiniu.android.netdiag.Task;
import com.qiniu.android.netdiag.TraceRoute;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Background task for checking unexpected packages jumps using traceroute.
 */
class TraceRoutes extends AsyncTask<String, Void, Void> {


    private Context mContext;

    TraceRoutes(Context mContext){
        super();
        this.mContext = mContext;
    }

    /**
     * Background task for checking unexpected packages jumps using traceroute.
     * @param params array of Strings with an url in position 0
     * @return null
     */
    protected Void doInBackground(String... params) {

        try {
            InetAddress.getByName(WifiActions.CAPTIVE_PORTAL_ADDRESS);
        } catch (UnknownHostException e) {
            // do not attempt traceroute if can't reach captive portal address
            return null;
        }

        final ArrayList<TraceRoute.Result> l = new ArrayList<>();
        final CountDownLatch c = new CountDownLatch(1);

        Task trace = TraceRoute.start(WifiActions.CAPTIVE_PORTAL_ADDRESS, new Output() {
            @Override
            public void write(String s) {
            }
        }, new TraceRoute.Callback() {
            @Override
            public void complete(TraceRoute.Result result) {
                l.add(result);
                c.countDown();
            }
        });

        // wait traceroute to finish or 10 seconds
        try {
            c.await(10, TimeUnit.SECONDS);
            trace.stop();
            c.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ups, it's dirty!!
        }

        if (l.size() > 0) {

            String traceOutput = l.get(0).content();
            ArrayList<String> pieces = new ArrayList<String>(Arrays.asList(traceOutput.split("\t")));
            int knownJumps = TraceRouteUtils.countKnownjumps(TraceRouteUtils.getJumps(pieces, 4));

            if (knownJumps > 1) {
                NotificationUtils.createNotification(this.mContext, this.mContext.getString(R.string.app_name), this.mContext.getString(R.string.app_name), WifiActions.TRACEROUTE_WARNING_NOTIFICATION_ID, this.mContext.getString(R.string.traceroute_warning), this.mContext.getResources().getQuantityString(R.plurals.unexpexted_route_jumps, knownJumps - 1, knownJumps - 1), "#alert-5");
            }
        }
    return null;
    }

}
