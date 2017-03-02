package com.hotb.pgmacdesign.californiaprototype.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
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
import com.hotb.pgmacdesign.californiaprototype.listeners.OnTaskCompleteListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.L;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.networking.APICalls;
import com.hotb.pgmacdesign.californiaprototype.pojos.CAUser;
import com.hotb.pgmacdesign.californiaprototype.utilities.AnimationUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.CaliforniaPrototypeCustomUtils;
import com.hotb.pgmacdesign.californiaprototype.utilities.DialogUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.PermissionUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.ProgressBarUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

/**
 * Created by pmacdowell on 2017-02-15.
 */

public class EmailLoginFragment extends Fragment implements OnTaskCompleteListener,
        View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

    public final static String TAG = "EmailLoginFragment";
    private APICalls api;

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
        this.api = new APICalls(getActivity(), this);
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
                ((CustomFragmentListener) getActivity()).setToolbarDetails(
                        getString(R.string.login_with_my_phone_number), null, null, null, null);

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
                ((CustomFragmentListener) getActivity()).setToolbarDetails(
                        getString(R.string.login_with_email), null, null, null, null);

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
                if(pwString.length() > 4){
                    passwordOk = true;
                } else {
                    passwordOk = false;
                }
            }

            if(passwordOk && emailOk){
                fragment_email_login_button.setEnabled(true);
                fragment_email_login_button.setTextColor(
                        ContextCompat.getColor(getActivity(), R.color.black));
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
                        ContextCompat.getColor(getActivity(), R.color.black));
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
                    if(fragment_email_login_pw_et.getText().toString().length() <= 4){
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

                if(whichActive == WhichActive.EMAIL) {
                    //Login with credentials
                    String email = fragment_email_login_email_et.getText().toString();
                    String pw = fragment_email_login_pw_et.getText().toString();

                    ProgressBarUtilities.showSVGProgressDialog(getActivity(),
                            false, Constants.PROGRESS_BAR_TIMEOUT);
                    api.loginWithEmail(email, pw);
                }
                if(whichActive == WhichActive.PHONE){
                    String phone = fragment_email_login_email_et_phone.getText().toString();
                    phone = StringUtilities.keepNumbersOnly(phone);
                    phone = phone.trim();
                    try{
                        if(!phone.startsWith("1")){
                            phone = "1" + phone;
                        }
                    } catch (Exception e){}
                    ProgressBarUtilities.showSVGProgressDialog(getActivity(),
                            false, Constants.PROGRESS_BAR_TIMEOUT);
                    api.phoneVerification(phone);
                }
                break;

            //Continue without signin
            case R.id.fragment_email_login_skip_login: //Re-purposed to register
                ((CustomFragmentListener)getActivity()).setToolbarDetails(
                        getString(R.string.register_for_account), null, true, null, null);
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

                String email = fragment_email_login_email_et.getText().toString();
                if(StringUtilities.isNullOrEmpty(email)){
                    L.Toast(getActivity(), getString(R.string.forgot_pw_instructions));
                    return;
                }
                if(!StringUtilities.isValidEmail(email)){
                    L.Toast(getActivity(), getString(R.string.forgot_pw_instructions));
                    return;
                }

                ProgressBarUtilities.showSVGProgressDialog(getActivity());
                api.forgotPassword(email);
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

    @Override
    public void onResume() {
        L.m("onResume in emailLoginFragment");
        if(((CustomFragmentListener)getActivity()).getCurrentFragment() ==
                Constants.FRAGMENT_EMAIL_LOGIN) {
            ((CustomFragmentListener) getActivity()).setToolbarDetails(
                    getString(R.string.login_with_email), null, true, null, null);
        }
        super.onResume();
    }


    @Override
    public void onTaskComplete(Object result, int customTag) {

        ProgressBarUtilities.dismissProgressDialog();
        switch(customTag){
            case Constants.TAG_FORGOT_PASSWORD:
                L.Toast(getActivity(), getString(R.string.forgot_pw_instructions2));
                break;

            case Constants.TAG_EMPTY_OBJECT:
                //This means that the sms verification text was successfully sent out
                String str2 = fragment_email_login_email_et_phone.getText().toString();
                if(StringUtilities.isNullOrEmpty(str2)){
                    L.Toast(getActivity(), getString(R.string.enter_a_valid_phone_number));
                } else {
                    try{
                        if(!str2.startsWith("1")){
                            str2 = "1" + str2;
                        }
                    } catch (Exception e){}
                    MyApplication.getSharedPrefsInstance().save(Constants.USER_PHONE_NUMBER, str2);
                    switchFragment(Constants.FRAGMENT_SMS_VERIFICATION);
                }
                break;

            case Constants.TAG_API_ERROR:
                //API Call error
                String str = CaliforniaPrototypeCustomUtils.checkErrorString(result);
                if(str.equalsIgnoreCase(getString(R.string.api_response_incorrect_credentials))){
                    L.toast(getActivity(), getString(R.string.username_pw_incorrect));
                } else {
                    Dialog dialog = DialogUtilities.buildOptionDialog(
                            MyApplication.getContext(),
                            new DialogUtilities.DialogFinishedListener() {
                                @Override
                                public void dialogFinished(Object object, int tag) {
                                    if(tag == DialogUtilities.SUCCESS_RESPONSE){
                                        boolean bool = (boolean) object;
                                        if(bool){
                                            if(whichActive == WhichActive.PHONE){
                                                //Phone registration
                                                String phone = fragment_email_login_email_et_phone
                                                        .getText().toString();
                                                phone = StringUtilities.keepNumbersOnly(phone);
                                                if(StringUtilities.isNullOrEmpty(phone)){
                                                    //Bail  here, no or bad phone #
                                                    L.toast(getActivity(), R.string.invalid_phone_number);
                                                    return;
                                                }
                                                phone = phone.trim();
                                                phone = "1" + phone;
                                                final String fPhone = phone;
                                                ProgressBarUtilities.showSVGProgressDialog(getActivity());
                                                api.phoneVerification(fPhone);

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        APICalls temp = new APICalls(getActivity(),
                                                                new OnTaskCompleteListener() {
                                                                    @Override
                                                                    public void onTaskComplete(Object result, int customTag) {
                                                                        //Do nothing, cannot interact with UI here
                                                                    }
                                                                });
                                                        temp.phoneVerification(fPhone);
                                                    }
                                                }, (int)(Constants.ONE_SECOND * 0.6));

                                            } else if (whichActive == WhichActive.EMAIL){
                                                //Email registration
                                                CAUser user = new CAUser();
                                                String email = fragment_email_login_email_et
                                                        .getText().toString();
                                                String pw = fragment_email_login_pw_et
                                                        .getText().toString();
                                                user.setEmail(email);
                                                user.setPassword(pw);
                                                ProgressBarUtilities.showSVGProgressDialog(getActivity());
                                                api.registerUser(user);
                                            }
                                        } else {
                                            //They declined, no need to act
                                        }
                                    } else {
                                        //They just dismissed it, no need to act
                                    }
                                }
                            }, getString(R.string.yes), getString(R.string.no),
                            getString(R.string.no_record_found),
                            getString(R.string.no_account_register_text)
                    );
                    dialog.show();
                }

                break;

            case Constants.TAG_API_CALL_FAILURE:
                //API Call error, unknown
                L.Toast(getActivity(), getString(R.string.generic_error_text));

                break;

            case Constants.TAG_CA_USER:
                //This means that login was successful Persist data and move on.
                if(whichActive == WhichActive.EMAIL){
                    String email = fragment_email_login_email_et.getText().toString();
                    String pw = fragment_email_login_pw_et.getText().toString();
                    MyApplication.getSharedPrefsInstance().save(Constants.USER_EMAIL, email);
                    MyApplication.getSharedPrefsInstance().save(Constants.USER_PW, pw);
                } else {
                    String phone = fragment_email_login_email_et_phone.getText().toString();
                    try{
                        if(!phone.startsWith("1")){
                            phone = "1" + phone;
                        }
                    } catch (Exception e){}
                    MyApplication.getSharedPrefsInstance().save(Constants.USER_PHONE_NUMBER, phone);
                }

                if(PermissionUtilities.PermissionsRequestShortcutReturn(getActivity(),
                        new PermissionUtilities.permissionsEnum[]{
                                PermissionUtilities.permissionsEnum.ACCESS_FINE_LOCATION})){
                    switchFragment(Constants.ACTIVITY_MAIN);
                } else {
                    switchFragment(Constants.FRAGMENT_PERMISSIONS_REQUEST);
                }

                break;

        }
    }
}
