package com.hotb.pgmacdesign.californiaprototype.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.customui.StateSelectedEditText;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.utilities.AnimationUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

/**
 * Created by pmacdowell on 2017-02-15.
 */

public class EmailLoginFragment extends Fragment implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

    public final static String TAG = "EmailLoginFragment";

    private enum WhichActive {
        EMAIL, PHONE
    }
    private WhichActive whichActive;

    //UI
    private com.hotb.pgmacdesign.californiaprototype.customui.StateSelectedEditText
            fragment_email_login_email_et, fragment_email_login_pw_et,
            fragment_email_login_email_et_phone;
    private TextView fragment_email_login_email_error, fragment_email_login_pw_error,
            fragment_email_login_forgot_pw, fragment_email_login_with_phone,
            fragment_email_login_skip_login, fragment_email_login_email_error_phone,
            fragment_email_login_tv_1_phone, fragment_email_login_tv_1,
            fragment_email_login_tv_2;
    private Button fragment_email_login_button;
    private RelativeLayout fragment_email_login_input_layout_phone,
            fragment_email_login_input_layout;

    public EmailLoginFragment() {}

    public static EmailLoginFragment newInstance() {
        EmailLoginFragment fragment = new EmailLoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilize instanceState here
        this.whichActive = WhichActive.EMAIL;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email_login, container, false);
        //Initialize all UI Fields
        initUi(view);
        //Run this once to initialize the button state
        validateFields();
        return view;
    }

    private void initUi(View view) {

        //UI
        fragment_email_login_email_et = (StateSelectedEditText) view.findViewById(
                R.id.fragment_email_login_email_et);
        fragment_email_login_pw_et = (StateSelectedEditText) view.findViewById(
                R.id.fragment_email_login_pw_et);
        fragment_email_login_email_et_phone = (StateSelectedEditText) view.findViewById(
                R.id.fragment_email_login_email_et_phone);
        fragment_email_login_email_error = (TextView) view.findViewById(
                R.id.fragment_email_login_email_error);
        fragment_email_login_pw_error = (TextView) view.findViewById(
                R.id.fragment_email_login_pw_error);
        fragment_email_login_forgot_pw = (TextView) view.findViewById(
                R.id.fragment_email_login_forgot_pw);
        fragment_email_login_with_phone = (TextView) view.findViewById(
                R.id.fragment_email_login_with_phone);
        fragment_email_login_skip_login = (TextView) view.findViewById(
                R.id.fragment_email_login_skip_login);
        fragment_email_login_email_error_phone = (TextView) view.findViewById(
                R.id.fragment_email_login_email_error_phone);
        fragment_email_login_tv_1_phone = (TextView) view.findViewById(
                R.id.fragment_email_login_tv_1_phone);
        fragment_email_login_tv_1 = (TextView) view.findViewById(
                R.id.fragment_email_login_tv_1);
        fragment_email_login_tv_2 = (TextView) view.findViewById(
                R.id.fragment_email_login_tv_2);
        fragment_email_login_button = (Button) view.findViewById(
                R.id.fragment_email_login_button);
        fragment_email_login_input_layout = (RelativeLayout) view.findViewById(
                R.id.fragment_email_login_input_layout);
        fragment_email_login_input_layout_phone = (RelativeLayout) view.findViewById(
                R.id.fragment_email_login_input_layout_phone);
        //Defaults
        this.fragment_email_login_button.setTransformationMethod(null);

        //Click Listeners
        this.fragment_email_login_button.setOnClickListener(this);
        this.fragment_email_login_skip_login.setOnClickListener(this);
        this.fragment_email_login_with_phone.setOnClickListener(this);
        this.fragment_email_login_forgot_pw.setOnClickListener(this);

        //Text change listeners
        this.fragment_email_login_email_et.addTextChangedListener(this);
        this.fragment_email_login_pw_et.addTextChangedListener(this);
        this.fragment_email_login_email_et_phone.addTextChangedListener(this);

        //Focus change listeners
        this.fragment_email_login_email_et.setOnFocusChangeListener(this);
        this.fragment_email_login_pw_et.setOnFocusChangeListener(this);
        this.fragment_email_login_email_et_phone.setOnFocusChangeListener(this);
    }

    /**
     * Used for setting the phone or email entry to the respective active one.
     * Will also animate the dynamic views into place
     * @param toSet {@link WhichActive}, which one they are logging in with
     */
    private void setWhichActive(WhichActive toSet){
        whichActive = toSet;
        validateFields();
        switch(toSet){
            case PHONE:
                //May be needed
                //setPhoneNumberError(false);
                fragment_email_login_with_phone.setText(R.string.login_with_email);
                fragment_email_login_input_layout_phone.setVisibility(View.VISIBLE);
                fragment_email_login_input_layout.setVisibility(View.GONE);

                AnimationUtilities.animateMyView(
                        fragment_email_login_tv_1_phone, 400, Constants.IN_FADE_DOWN);
                AnimationUtilities.animateMyView(
                        fragment_email_login_email_et_phone, 400, Constants.IN_FADE_UP);
                break;

            case EMAIL:
                //May be needed
                //setEmailError(false);
                //setPWError(false);
                fragment_email_login_with_phone.setText(R.string.login_with_my_phone_number);
                fragment_email_login_input_layout_phone.setVisibility(View.GONE);
                fragment_email_login_input_layout.setVisibility(View.VISIBLE);

                AnimationUtilities.animateMyView(
                        fragment_email_login_tv_1, 400, Constants.IN_FADE_DOWN);
                AnimationUtilities.animateMyView(
                        fragment_email_login_email_et, 400, Constants.IN_FADE_UP);
                AnimationUtilities.animateMyView(
                        fragment_email_login_tv_2, 400, Constants.IN_FADE_DOWN);
                AnimationUtilities.animateMyView(
                        fragment_email_login_pw_et, 400, Constants.IN_FADE_UP);
                break;
        }
    }

    /**
     * Validate the 2 input fields and set the button availability respectfully
     */
    private void validateFields(){

        if(whichActive == WhichActive.EMAIL){
            boolean emailOk, passwordOk;

            String emailString = fragment_email_login_email_et.getText().toString();
            emailString = emailString.trim();
            if(StringUtilities.isNullOrEmpty(emailString)){
                emailOk = false;
            } else {
                emailOk = StringUtilities.isValidEmail(emailString);
            }

            String pwString = fragment_email_login_pw_et.getText().toString();
            pwString = pwString.trim();
            if(StringUtilities.isNullOrEmpty(pwString)){
                passwordOk = false;
            } else {
                passwordOk = StringUtilities.checkForComplicatedPassword(pwString);
            }

            if(passwordOk && emailOk){
                fragment_email_login_button.setEnabled(true);
                fragment_email_login_button.setTextColor(
                        ContextCompat.getColor(getActivity(), R.color.Black));
            } else {
                fragment_email_login_button.setEnabled(false);
                fragment_email_login_button.setTextColor(
                        ContextCompat.getColor(getActivity(), R.color.LightGrey));
            }

        } else if (whichActive == WhichActive.PHONE){
            boolean phoneOk;

            String phoneString = fragment_email_login_email_et_phone.getText().toString();
            if(StringUtilities.isNullOrEmpty(phoneString)){
                phoneOk = false;
            } else {
                phoneString = phoneString.trim();
                phoneString = StringUtilities.keepNumbersOnly(phoneString);
                if(phoneString.length() == 10){
                    phoneOk = true;
                } else {
                    if(phoneString.length() > 10){
                        //Check if first one is +1
                        phoneString = fragment_email_login_email_et_phone.getText().toString();
                        if(phoneString.startsWith("+1")){
                            //Just means it will need to be parsed out
                            phoneOk = true;
                        } else {
                            phoneOk = false;
                        }
                    } else {
                        phoneOk = false;
                    }
                }
            }

            if(phoneOk){
                fragment_email_login_button.setEnabled(true);
                fragment_email_login_button.setTextColor(
                        ContextCompat.getColor(getActivity(), R.color.Black));
            } else {
                fragment_email_login_button.setEnabled(false);
                fragment_email_login_button.setTextColor(
                        ContextCompat.getColor(getActivity(), R.color.LightGrey));
            }
        }

    }

    private void focusSetter(View view, boolean hasFocus){

        if(whichActive == WhichActive.EMAIL){
            //Hide error text fields
            setPWError(false);
            setEmailError(false);

            //Adjust back to normal views
            if(view == fragment_email_login_email_et){
                if(hasFocus){
                    fragment_email_login_email_et.setState(StateSelectedEditText.EditTextState.FOCUSED);
                    fragment_email_login_pw_et.setState(StateSelectedEditText.EditTextState.NOT_FOCUSED);
                } else {
                    fragment_email_login_email_et.setState(StateSelectedEditText.EditTextState.NOT_FOCUSED);
                    fragment_email_login_pw_et.setState(StateSelectedEditText.EditTextState.NOT_FOCUSED);
                }
            }
            if(view == fragment_email_login_pw_et){
                if(hasFocus){
                    fragment_email_login_email_et.setState(StateSelectedEditText.EditTextState.NOT_FOCUSED);
                    fragment_email_login_pw_et.setState(StateSelectedEditText.EditTextState.FOCUSED);
                } else {
                    fragment_email_login_email_et.setState(StateSelectedEditText.EditTextState.NOT_FOCUSED);
                    fragment_email_login_pw_et.setState(StateSelectedEditText.EditTextState.NOT_FOCUSED);
                }
            }

            //Add in 'error handling' for red backgrounds
            if(view == fragment_email_login_email_et && hasFocus){
                if(!StringUtilities.isNullOrEmpty(fragment_email_login_pw_et.getText().toString())){
                    if(!StringUtilities.checkForComplicatedPassword(fragment_email_login_pw_et.getText().toString())){
                        //Adjust accordingly. email focused, pw is incorrect
                        fragment_email_login_pw_et.setState(StateSelectedEditText.EditTextState.ERROR);
                        setPWError(true);
                    }
                }
            }
            if(view == fragment_email_login_pw_et && hasFocus){
                if(!StringUtilities.isNullOrEmpty(fragment_email_login_email_et.getText().toString())){
                    if(!StringUtilities.isValidEmail(fragment_email_login_email_et.getText().toString())){
                        //Adjust accordingly. pw focused, email is incorrect
                        fragment_email_login_email_et.setState(StateSelectedEditText.EditTextState.ERROR);
                        setEmailError(true);
                    }
                }
            }
        } else if (whichActive == WhichActive.PHONE){
            setPhoneNumberError(false);
            //Adjust back to normal views
            if(view == fragment_email_login_email_et_phone){
                if(hasFocus){
                    fragment_email_login_email_et_phone.setState(StateSelectedEditText.EditTextState.FOCUSED);
                } else {
                    fragment_email_login_email_et_phone.setState(StateSelectedEditText.EditTextState.NOT_FOCUSED);
                }
            }

            //Add in 'error handling' for red backgrounds
            if(!hasFocus){
                if(!StringUtilities.isNullOrEmpty(fragment_email_login_email_et_phone.getText().toString())){
                    String phoneNumber = fragment_email_login_email_et_phone.getText().toString();
                    phoneNumber = phoneNumber.trim();
                    if(phoneNumber.length() != 10){
                        fragment_email_login_pw_et.setState(StateSelectedEditText.EditTextState.ERROR);
                    } else {
                        //Nothing for now
                    }
                }
            }

        }

    }

    /**
     * Make the error email tv visible or not
     * @param bool true, visible, false, not
     */
    private void setPhoneNumberError(boolean bool){
        if(bool){
            fragment_email_login_email_error_phone.setVisibility(View.VISIBLE);
            fragment_email_login_email_error_phone.setText(R.string.invalid_phone_number);
        } else {
            fragment_email_login_email_error_phone.setVisibility(View.GONE);
        }
    }


    /**
     * Make the error email tv visible or not
     * @param bool true, visible, false, not
     */
    private void setEmailError(boolean bool){
        if(bool){
            fragment_email_login_email_error.setVisibility(View.VISIBLE);
            fragment_email_login_email_error.setText(R.string.invalid_email_address);
        } else {
            fragment_email_login_email_error.setVisibility(View.GONE);
        }
    }

    /**
     * Make the error pw tv visible or not
     * @param bool true, visible, false, not
     */
    private void setPWError(boolean bool){
        if(bool){
            fragment_email_login_pw_error.setVisibility(View.VISIBLE);
            fragment_email_login_pw_error.setText(R.string.password_error_help);
        } else {
            fragment_email_login_pw_error.setVisibility(View.GONE);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((CustomFragmentListener)getActivity()).setCurrentFragment(Constants.FRAGMENT_EMAIL_LOGIN);
        setWhichActive(WhichActive.EMAIL);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //Continue Button
            case R.id.fragment_email_login_button:
                //Login with credentials
                break;

            //Continue without signin
            case R.id.fragment_email_login_skip_login:
                //Skip login
                break;

            //Login with phone
            case R.id.fragment_email_login_with_phone:
                if(whichActive == WhichActive.EMAIL){
                    setWhichActive(WhichActive.PHONE);
                } else {
                    setWhichActive(WhichActive.EMAIL);
                }
                break;

            //Forgot password
            case R.id.fragment_email_login_forgot_pw:
                //Forgot password flow
                break;

        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void afterTextChanged(Editable s) {
        if(s != null){
            if(s == fragment_email_login_email_et_phone.getEditableText()){
                String ss = StringUtilities.formatStringLikePhoneNumber(s.toString());
                fragment_email_login_email_et_phone.removeTextChangedListener(this);
                fragment_email_login_email_et_phone.setText(ss);
                fragment_email_login_email_et_phone.setSelection(ss.length());
                fragment_email_login_email_et_phone.addTextChangedListener(this);
            }
            validateFields();
        }
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v == null){
            return;
        }
        focusSetter(v, hasFocus);
    }
}
