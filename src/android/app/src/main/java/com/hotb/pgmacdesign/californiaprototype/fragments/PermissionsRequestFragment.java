package com.hotb.pgmacdesign.californiaprototype.fragments;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.listeners.OnTaskCompleteListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.L;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.PermissionUtilities;

/**
 * Created by pmacdowell on 2017-02-22.
 */

public class PermissionsRequestFragment extends Fragment implements OnTaskCompleteListener{

    public final static String TAG = "PermissionsRequestFragment";

    private TextView fragment_permissions_title_tv, fragment_permissions_explanation_tv;

    public PermissionsRequestFragment() {}

    public static PermissionsRequestFragment newInstance() {
        PermissionsRequestFragment fragment = new PermissionsRequestFragment();
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
        View view = inflater.inflate(R.layout.fragment_permissions_request, container, false);
        initUi(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((CustomFragmentListener)getActivity()).setCurrentFragment(Constants.FRAGMENT_PERMISSIONS_REQUEST);
        startFirstPermissionRequest();
    }

    private void initUi(View view) {
        this.fragment_permissions_title_tv = (TextView) view.findViewById(
                R.id.fragment_permissions_title_tv);
        this.fragment_permissions_explanation_tv = (TextView) view.findViewById(
                R.id.fragment_permissions_explanation_tv);

    }

    private void startFirstPermissionRequest(){

        fragment_permissions_title_tv.setText(R.string.access_location_perm_title);
        fragment_permissions_explanation_tv.setText(R.string.access_location_perm_message);

        if(Build.VERSION.SDK_INT >= 23){
            if(ContextCompat.checkSelfPermission(getContext(),
                    PermissionUtilities.permissionsEnum.ACCESS_FINE_LOCATION
                            .getPermissionManifestName()) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{PermissionUtilities.permissionsEnum
                        .ACCESS_FINE_LOCATION.getPermissionManifestName()},
                        PermissionUtilities.permissionsEnum
                                .ACCESS_FINE_LOCATION.getPermissionCode());
            } else {
                startSecondPermissionRequest();
            }
        } else {
            startSecondPermissionRequest();
        }

    }

    private void startSecondPermissionRequest(){

        //There is no secondary permission request right now, but if more is added, call it here
        moveToNextActivity();

    }

    private void moveToNextActivity(){
        onTaskComplete(null, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermissionUtilities.permissionsEnum
                .ACCESS_FINE_LOCATION.getPermissionCode()){
            startSecondPermissionRequest();
        } else if (requestCode == PermissionUtilities.permissionsEnum
                .ACCESS_WIFI_STATE.getPermissionCode()){
            moveToNextActivity();
        } else {
            moveToNextActivity();
        }
    }

    /**
     * Switch to a different fragment
     * @param x
     */
    private void switchFragment(int x){
        FragmentUtilities.switchFragments(x, ((CustomFragmentListener)getActivity()));
    }

    @Override
    public void onResume() {
        L.m("onResume in permissionrequestfragment");
        if(((CustomFragmentListener)getActivity()).getCurrentFragment() ==
                Constants.FRAGMENT_PERMISSIONS_REQUEST) {
            ((CustomFragmentListener) getActivity()).setToolbarDetails(
                    getString(R.string.permission_requests_fragment), null, false, null);
        }
        super.onResume();
    }

    @Override
    public void onTaskComplete(Object result, int customTag) {
        // TODO: 2017-02-24 insert check from server here
        L.Toast(getActivity(), getString(R.string.debug_popup_skipping));
        switchFragment(Constants.ACTIVITY_MAIN);
    }
}
