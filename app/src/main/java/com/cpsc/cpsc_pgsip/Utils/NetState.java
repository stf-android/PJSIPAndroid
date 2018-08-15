package com.cpsc.cpsc_pgsip.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cpsc.cpsc_pgsip.MyApp;

/**
 * Created by stf on 2018-07-18.
 */

public class NetState {
    private String conn_name = "";
    private Context context;

    public NetState(Context context) {
        this.context = context;
    }

    public boolean isNetworkChange() {
        boolean network_changed = false;
        ConnectivityManager connectivity_mgr = ((ConnectivityManager) MyApp.content.getSystemService(Context.CONNECTIVITY_SERVICE));

        NetworkInfo net_info = connectivity_mgr.getActiveNetworkInfo();

        if (net_info != null && net_info.isConnectedOrConnecting() && !conn_name.equalsIgnoreCase("")) {

            String new_con = net_info.getExtraInfo();
            Log.i("stf", "--new_con-->" + new_con);

            if (new_con != null && !new_con.equalsIgnoreCase(conn_name))
                network_changed = true;

            conn_name = (new_con == null) ? "" : new_con;
        } else {
            if (conn_name.equalsIgnoreCase(""))
                conn_name = net_info.getExtraInfo();
            Log.i("stf", "--conn_name-->" + conn_name);
        }
        return network_changed;
    }
}
