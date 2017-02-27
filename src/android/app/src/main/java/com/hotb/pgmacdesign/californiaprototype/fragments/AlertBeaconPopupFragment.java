package com.hotb.pgmacdesign.californiaprototype.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.L;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.pojos.AlertBeacon;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;

/**
 * Created by pmacdowell on 2017-02-23.
 */

public class AlertBeaconPopupFragment extends Fragment {

    public final static String TAG = "AlertBeaconPopupFragment";

    //UI
    private TextView alert_beacon_popup_title, alert_beacon_popup_body;


    public AlertBeaconPopupFragment() {}

    public static AlertBeaconPopupFragment newInstance() {
        AlertBeaconPopupFragment fragment = new AlertBeaconPopupFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilize instanceState here
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert_beacon_popup, container, false);
        initUi(view);
        return view;
    }

    private void initUi(View view) {
        this.alert_beacon_popup_body = (TextView) view.findViewById(
                R.id.alert_beacon_popup_body);
        this.alert_beacon_popup_title = (TextView) view.findViewById(
                R.id.alert_beacon_popup_title);
    }


    /**
     * Switch to a different fragment
     * @param x
     */
    private void switchFragment(int x){
        FragmentUtilities.switchFragments(x, ((CustomFragmentListener)getActivity()));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((CustomFragmentListener)getActivity()).setCurrentFragment(Constants.FRAGMENT_ALERT_BEACON_POPUP);

        AlertBeacon beaconClicked = null;
        try {
            beaconClicked = (AlertBeacon) MyApplication.getDatabaseInstance()
                    .getPersistedObject(AlertBeacon.class);
        } catch (Exception e){
            e.printStackTrace();
        }

        if(beaconClicked == null){
            FragmentUtilities.switchFragments(Constants.FRAGMENT_MAP,
                    ((CustomFragmentListener)getActivity()));
            return;
        }

        // TODO: 2017-02-23 are we getting separate titles here?
        String title = "Emergency at this location: "
                + beaconClicked.getLat() + ", " + beaconClicked.getLng();
        String body = beaconClicked.getAlertInformation();

        alert_beacon_popup_title.setText(title);
        alert_beacon_popup_body.setText(body);
    }

    @Override
    public void onResume() {
        L.m("onResume in alertbeaconpopupfragment");
        if(((CustomFragmentListener)getActivity()).getCurrentFragment() ==
                Constants.FRAGMENT_ALERT_BEACON_POPUP) {
            ((CustomFragmentListener) getActivity()).setToolbarDetails(
                    " ", null, true, false);
        }
        super.onResume();
    }
}

