package com.hotb.pgmacdesign.californiaprototype.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.customui.StateSelectedEditText;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.listeners.OnTaskCompleteListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.L;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

/**
 * Created by pmacdowell on 2017-02-16.
 */

public class SMSVerificationFragment extends Fragment implements TextWatcher, OnTaskCompleteListener, View.OnClickListener, View.OnFocusChangeListener {

    public final static String TAG = "SMSVerificationFragment";

    //UI
    private TextView fragment_smsverification_user_number_tv,
            fragment_smsverification_edit_number, fragment_smsverification_error_tv,
            fragment_smsverification_resent_sms;
    private StateSelectedEditText fragment_smsverification_code_box_1,
            fragment_smsverification_code_box_2, fragment_smsverification_code_box_3,
            fragment_smsverification_code_box_4, fragment_smsverification_code_box_5,
            fragment_smsverification_code_box_6;
    private Button fragment_smsverification_verify_button;


    public SMSVerificationFragment() {}

    public static SMSVerificationFragment newInstance() {
        SMSVerificationFragment fragment = new SMSVerificationFragment();
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
        View view = inflater.inflate(R.layout.fragment_smsverification, container, false);
        //Initialize all UI Fields
        initUi(view);
        //Run this once to initialize the button state / ET states
        validateFields();
        setDefaults();
        //Set the user's phone number
        setUserData();

        return view;
    }

    private void initUi(View view) {

        //UI
        this.fragment_smsverification_user_number_tv = (TextView) view.findViewById(
                R.id.fragment_smsverification_user_number_tv);
        this.fragment_smsverification_edit_number = (TextView) view.findViewById(
                R.id.fragment_smsverification_edit_number);
        this.fragment_smsverification_error_tv = (TextView) view.findViewById(
                R.id.fragment_smsverification_error_tv);
        this.fragment_smsverification_resent_sms = (TextView) view.findViewById(
                R.id.fragment_smsverification_resent_sms);
        this.fragment_smsverification_code_box_1 = (StateSelectedEditText) view.findViewById(
                R.id.fragment_smsverification_code_box_1);
        this.fragment_smsverification_code_box_2 = (StateSelectedEditText) view.findViewById(
                R.id.fragment_smsverification_code_box_2);
        this.fragment_smsverification_code_box_3 = (StateSelectedEditText) view.findViewById(
                R.id.fragment_smsverification_code_box_3);
        this.fragment_smsverification_code_box_4 = (StateSelectedEditText) view.findViewById(
                R.id.fragment_smsverification_code_box_4);
        this.fragment_smsverification_code_box_5 = (StateSelectedEditText) view.findViewById(
                R.id.fragment_smsverification_code_box_5);
        this.fragment_smsverification_code_box_6 = (StateSelectedEditText) view.findViewById(
                R.id.fragment_smsverification_code_box_6);
        this.fragment_smsverification_verify_button = (Button) view.findViewById(
                R.id.fragment_smsverification_verify_button);

        //Defaults
        this.fragment_smsverification_verify_button.setTransformationMethod(null);

        //Click Listeners
        this.fragment_smsverification_verify_button.setOnClickListener(this);
        this.fragment_smsverification_resent_sms.setOnClickListener(this);
        this.fragment_smsverification_edit_number.setOnClickListener(this);

        //Text change listeners
        this.fragment_smsverification_code_box_1.addTextChangedListener(this);
        this.fragment_smsverification_code_box_2.addTextChangedListener(this);
        this.fragment_smsverification_code_box_3.addTextChangedListener(this);
        this.fragment_smsverification_code_box_4.addTextChangedListener(this);
        this.fragment_smsverification_code_box_5.addTextChangedListener(this);
        this.fragment_smsverification_code_box_6.addTextChangedListener(this);

        //Focus change listeners
        this.fragment_smsverification_code_box_1.setOnFocusChangeListener(this);
        this.fragment_smsverification_code_box_2.setOnFocusChangeListener(this);
        this.fragment_smsverification_code_box_3.setOnFocusChangeListener(this);
        this.fragment_smsverification_code_box_4.setOnFocusChangeListener(this);
        this.fragment_smsverification_code_box_5.setOnFocusChangeListener(this);
        this.fragment_smsverification_code_box_6.setOnFocusChangeListener(this);

        //Custom IME listener
        fragment_smsverification_code_box_6.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if(validateFields()){
                        triggerSubmit();
                        return true;
                    } else {
                        // TODO: 2017-02-16 decide if warning should go here
                    }
                }
                return false;
            }
        });
    }

    private void setUserData(){
        String phoneNumber = MyApplication.getSharedPrefsInstance().getString(
                Constants.USER_PHONE_NUMBER, null);
        if(StringUtilities.isNullOrEmpty(phoneNumber)){
            fragment_smsverification_user_number_tv.setText("");
        } else {
            phoneNumber = StringUtilities.formatStringLikePhoneNumber(phoneNumber);
            fragment_smsverification_user_number_tv.setText(phoneNumber);
        }
    }

    private void triggerSubmit(){
        String code1 = fragment_smsverification_code_box_1.getText().toString();
        code1 = code1.trim();
        String code2 = fragment_smsverification_code_box_2.getText().toString();
        code2 = code2.trim();
        String code3 = fragment_smsverification_code_box_3.getText().toString();
        code3 = code3.trim();
        String code4 = fragment_smsverification_code_box_4.getText().toString();
        code4 = code4.trim();
        String code5 = fragment_smsverification_code_box_5.getText().toString();
        code5 = code5.trim();
        String code6 = fragment_smsverification_code_box_6.getText().toString();
        code6 = code6.trim();

        //todo setup link to server here once implemented
        onTaskComplete(null, 0);
    }

    /**
     * Validate the 6 input fields and set the button availability respectfully
     */
    private boolean validateFields(){
        boolean code1Ok, code2Ok, code3Ok, code4Ok, code5Ok, code6Ok;

        String code1 = fragment_smsverification_code_box_1.getText().toString();
        code1 = code1.trim();
        String code2 = fragment_smsverification_code_box_2.getText().toString();
        code2 = code2.trim();
        String code3 = fragment_smsverification_code_box_3.getText().toString();
        code3 = code3.trim();
        String code4 = fragment_smsverification_code_box_4.getText().toString();
        code4 = code4.trim();
        String code5 = fragment_smsverification_code_box_5.getText().toString();
        code5 = code5.trim();
        String code6 = fragment_smsverification_code_box_6.getText().toString();
        code6 = code6.trim();

        if(StringUtilities.isNullOrEmpty(code1)){
            code1Ok = false;
        } else {
            code1Ok = true;
        }
        if(StringUtilities.isNullOrEmpty(code2)){
            code2Ok = false;
        } else {
            code2Ok = true;
        }
        if(StringUtilities.isNullOrEmpty(code3)){
            code3Ok = false;
        } else {
            code3Ok = true;
        }
        if(StringUtilities.isNullOrEmpty(code4)){
            code4Ok = false;
        } else {
            code4Ok = true;
        }
        if(StringUtilities.isNullOrEmpty(code5)){
            code5Ok = false;
        } else {
            code5Ok = true;
        }
        if(StringUtilities.isNullOrEmpty(code6)){
            code6Ok = false;
        } else {
            code6Ok = true;
        }

        if(code1Ok && code2Ok && code3Ok && code4Ok && code5Ok && code6Ok){
            fragment_smsverification_verify_button.setEnabled(true);
            fragment_smsverification_verify_button.setTextColor(
                    ContextCompat.getColor(getActivity(), R.color.Black));
            return true;
        } else {
            fragment_smsverification_verify_button.setEnabled(false);
            fragment_smsverification_verify_button.setTextColor(
                    ContextCompat.getColor(getActivity(), R.color.LightGrey));
            return false;
        }
    }

    /**
     * Make the error tv visible or not
     * @param bool true, visible, false, not
     */
    private void setTVError(boolean bool){
        if(bool){
            fragment_smsverification_error_tv.setVisibility(View.VISIBLE);
            fragment_smsverification_error_tv.setText(R.string.invalid_verification_code);
        } else {
            fragment_smsverification_error_tv.setVisibility(View.GONE);
        }
    }

    /**
     * Switch to a different fragment
     * @param x
     */
    private void switchFragment(int x){
        FragmentUtilities.switchFragments(x, ((CustomFragmentListener)getActivity()));
    }

    /**
     * Used for setting the ET views to their defaults
     */
    private void setDefaults(){
        fragment_smsverification_code_box_1.setState(
                StateSelectedEditText.EditTextState.NOT_FOCUSED);
        fragment_smsverification_code_box_2.setState(
                StateSelectedEditText.EditTextState.NOT_FOCUSED);
        fragment_smsverification_code_box_3.setState(
                StateSelectedEditText.EditTextState.NOT_FOCUSED);
        fragment_smsverification_code_box_4.setState(
                StateSelectedEditText.EditTextState.NOT_FOCUSED);
        fragment_smsverification_code_box_5.setState(
                StateSelectedEditText.EditTextState.NOT_FOCUSED);
        fragment_smsverification_code_box_6.setState(
                StateSelectedEditText.EditTextState.NOT_FOCUSED);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((CustomFragmentListener)getActivity()).setCurrentFragment(Constants.FRAGMENT_SMS_VERIFICATION);

        //Give focus to the first box and popup the keyboard
        fragment_smsverification_code_box_1.setFocusableInTouchMode(true);
        fragment_smsverification_code_box_1.requestFocus();
        fragment_smsverification_code_box_1.requestFocusFromTouch();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        InputMethodManager lManager = (InputMethodManager)getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        lManager.showSoftInput(fragment_smsverification_code_box_1, 0);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //Verify Button
            case R.id.fragment_smsverification_verify_button:
                //verify sms
                triggerSubmit();
                break;

            //Resend SMS TV
            case R.id.fragment_smsverification_resent_sms:
                //resend sms
                L.Toast(getActivity(), "Resending text message");
                break;

            //Edit Number
            case R.id.fragment_smsverification_edit_number:
                //edit the user's number
                L.Toast(getActivity(), getString(R.string.debug_popup_skipping));
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        validateFields();
        String str = null;
        if(s == fragment_smsverification_code_box_1.getEditableText()){
            str = fragment_smsverification_code_box_1.getText().toString();
            if(!StringUtilities.isNullOrEmpty(str)){
                fragment_smsverification_code_box_2.requestFocus();
            }
        } else if(s == fragment_smsverification_code_box_2.getEditableText()){
            str = fragment_smsverification_code_box_2.getText().toString();
            if(!StringUtilities.isNullOrEmpty(str)){
                fragment_smsverification_code_box_3.requestFocus();
            }
        } else if(s == fragment_smsverification_code_box_3.getEditableText()){
            str = fragment_smsverification_code_box_3.getText().toString();
            if(!StringUtilities.isNullOrEmpty(str)){
                fragment_smsverification_code_box_4.requestFocus();
            }
        } else if(s == fragment_smsverification_code_box_4.getEditableText()){
            str = fragment_smsverification_code_box_4.getText().toString();
            if(!StringUtilities.isNullOrEmpty(str)){
                fragment_smsverification_code_box_5.requestFocus();
            }
        } else if(s == fragment_smsverification_code_box_5.getEditableText()){
            str = fragment_smsverification_code_box_5.getText().toString();
            if(!StringUtilities.isNullOrEmpty(str)){
                fragment_smsverification_code_box_6.requestFocus();
            }
        } else if(s == fragment_smsverification_code_box_6.getEditableText()){
            //Nothing for now
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //First set all to not focused to default to originals
        setDefaults();

        //Next check which has focus
        if(hasFocus) {
            if (v == fragment_smsverification_code_box_1) {
                fragment_smsverification_code_box_1.setState(
                        StateSelectedEditText.EditTextState.FOCUSED);
            } else if (v == fragment_smsverification_code_box_2) {
                fragment_smsverification_code_box_2.setState(
                        StateSelectedEditText.EditTextState.FOCUSED);
            } else if (v == fragment_smsverification_code_box_3) {
                fragment_smsverification_code_box_3.setState(
                        StateSelectedEditText.EditTextState.FOCUSED);
            } else if (v == fragment_smsverification_code_box_4) {
                fragment_smsverification_code_box_4.setState(
                        StateSelectedEditText.EditTextState.FOCUSED);
            } else if (v == fragment_smsverification_code_box_5) {
                fragment_smsverification_code_box_5.setState(
                        StateSelectedEditText.EditTextState.FOCUSED);
            } else if (v == fragment_smsverification_code_box_6) {
                fragment_smsverification_code_box_6.setState(
                        StateSelectedEditText.EditTextState.FOCUSED);
            }
        }

    }

    /**
     * Use this to set the errors in the ETs. Pings when a user enters an invalid code and the
     * server response indicates such
     */
    private void setErrors(){
        fragment_smsverification_code_box_1.setState(StateSelectedEditText.EditTextState.ERROR);
        fragment_smsverification_code_box_2.setState(StateSelectedEditText.EditTextState.ERROR);
        fragment_smsverification_code_box_3.setState(StateSelectedEditText.EditTextState.ERROR);
        fragment_smsverification_code_box_4.setState(StateSelectedEditText.EditTextState.ERROR);
        fragment_smsverification_code_box_5.setState(StateSelectedEditText.EditTextState.ERROR);
        fragment_smsverification_code_box_6.setState(StateSelectedEditText.EditTextState.ERROR);
    }


    @Override
    public void onResume() {
        L.m("onResume in smsverificationfragment");
        if(((CustomFragmentListener)getActivity()).getCurrentFragment() ==
                Constants.FRAGMENT_SMS_VERIFICATION) {
            ((CustomFragmentListener) getActivity()).setToolbarDetails(
                    getString(R.string.sms_verification), null, false, null);
        }
        super.onResume();
    }

    @Override
    public void onTaskComplete(Object result, int customTag) {
        // TODO: 2017-02-24 insert check from server here
        L.Toast(getActivity(), getString(R.string.debug_popup_skipping));
        switchFragment(Constants.FRAGMENT_PERMISSIONS_REQUEST);
    }
}

