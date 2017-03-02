package com.hotb.pgmacdesign.californiaprototype.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.customui.StateSelectedEditText;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomFragmentListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.misc.L;
import com.hotb.pgmacdesign.californiaprototype.misc.MyApplication;
import com.hotb.pgmacdesign.californiaprototype.pojos.PlaceChosen;
import com.hotb.pgmacdesign.californiaprototype.utilities.FragmentUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.PermissionUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

/**
 * Created by pmacdowell on 2017-02-24.
 *   //Removed on 2017-02-27 As per meeting and AHA discussion
 */
@Deprecated
public class AddContactFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    public final static String TAG = "AddContactFragment";
    private PlaceChosen placeChosen;

    //UI
    private TextView fragment_add_contact_tv_1, fragment_add_contact_tv_2;
    private Button fragment_add_contact_button_from_contactlist,
            fragment_add_contact_button_from_ets;
    private StateSelectedEditText fragment_add_contact_et_name, fragment_add_contact_et_number;

    public AddContactFragment() {}

    public static AddContactFragment newInstance() {
        AddContactFragment fragment = new AddContactFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utilize instanceState here
        try {
            placeChosen = (PlaceChosen) MyApplication.getDatabaseInstance()
                    .getPersistedObject(PlaceChosen.class);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        initUi(view);
        return view;
    }

    private void initUi(View view) {

        this.fragment_add_contact_tv_1 = (TextView) view.findViewById(
                R.id.fragment_add_contact_tv_1);
        this.fragment_add_contact_tv_2 = (TextView) view.findViewById(
                R.id.fragment_add_contact_tv_2);
        this.fragment_add_contact_button_from_contactlist = (Button) view.findViewById(
                R.id.fragment_add_contact_button_from_contactlist);
        this.fragment_add_contact_button_from_ets = (Button) view.findViewById(
                R.id.fragment_add_contact_button_from_ets);
        this.fragment_add_contact_et_name = (StateSelectedEditText) view.findViewById(
                R.id.fragment_add_contact_et_name);
        this.fragment_add_contact_et_number = (StateSelectedEditText) view.findViewById(
                R.id.fragment_add_contact_et_number);

        this.fragment_add_contact_et_name.setState(
                StateSelectedEditText.EditTextState.NOT_FOCUSED);
        this.fragment_add_contact_et_number.setState(
                StateSelectedEditText.EditTextState.NOT_FOCUSED);

        this.fragment_add_contact_et_name.setOnFocusChangeListener(this);
        this.fragment_add_contact_et_number.setOnFocusChangeListener(this);

        this.fragment_add_contact_et_name.addTextChangedListener(this);
        this.fragment_add_contact_et_number.addTextChangedListener(this);

        this.fragment_add_contact_button_from_contactlist.setTransformationMethod(null);
        this.fragment_add_contact_button_from_ets.setTransformationMethod(null);

        this.fragment_add_contact_button_from_contactlist.setOnClickListener(this);
        this.fragment_add_contact_button_from_ets.setOnClickListener(this);
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
        ((CustomFragmentListener)getActivity()).setCurrentFragment(Constants.FRAGMENT_ADD_CONTACT);
        if(placeChosen == null){
            switchFragment(Constants.FRAGMENT_MAP);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_add_contact_button_from_ets:
                if(validateFields()){
                    addContact(fragment_add_contact_et_name.getText().toString(),
                            fragment_add_contact_et_number.getText().toString());
                }
                break;

            case R.id.fragment_add_contact_button_from_contactlist:
                if(PermissionUtilities.PermissionsRequestShortcutReturn(getActivity(),
                        new PermissionUtilities.permissionsEnum[]{
                                PermissionUtilities.permissionsEnum.READ_CONTACTS
                        })){
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.Contacts.CONTENT_URI);
                    this.startActivityForResult(intent,
                            PermissionUtilities.permissionsEnum.READ_CONTACTS.getPermissionCode());
                }
                break;


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PermissionUtilities.permissionsEnum.READ_CONTACTS.getPermissionCode()
                && data != null){
            String name = null, phoneNumber = null, userId = null;
            Uri contactData = data.getData();
            Cursor c =  getActivity().getContentResolver().query(
                    contactData, null, null, null, null);
            try {
                if (c.moveToFirst()){
                    try {
                        userId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        name = c.getString(c.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME));
                    } catch (Exception e){}
                }
            } catch (Exception e){}
            Cursor c2 =  getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                    new String[]{userId}, null);
            try {
                if (c2.moveToFirst()){
                    try {
                        int phoneNumberIndex = c2.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.NUMBER);
                        phoneNumber = c2.getString(phoneNumberIndex);
                    } catch (Exception e){}
                }
            } catch (Exception e){}
            try {
                c.close();
                c2.close();
            } catch (Exception e){}

            if(StringUtilities.isNullOrEmpty(name) || StringUtilities.isNullOrEmpty(phoneNumber)){
                L.toast(getActivity(), getString(R.string.contact_error_text));
            } else {
                addContact(name, phoneNumber);
            }
        }
    }

    private void addContact(String name, String number){
        if(StringUtilities.isNullOrEmpty(name) || StringUtilities.isNullOrEmpty(number)){
            return;
        }
        name = name.trim();
        number = StringUtilities.keepNumbersOnly(number);
        number = number.trim();


    }

    private boolean validateFields(){

        boolean toReturn = true;

        String name = fragment_add_contact_et_name.getText().toString();
        if(StringUtilities.isNullOrEmpty(name)){
            toReturn = false;
            fragment_add_contact_et_name.setState(
                    StateSelectedEditText.EditTextState.ERROR);
        } else {
            if(name.length() > 0){
                fragment_add_contact_et_name.setState(
                        StateSelectedEditText.EditTextState.NOT_FOCUSED);
            } else {
                fragment_add_contact_et_name.setState(
                        StateSelectedEditText.EditTextState.ERROR);
            }
        }

        String number = fragment_add_contact_et_number.getText().toString();
        if(StringUtilities.isNullOrEmpty(number)){
            toReturn = false;
            fragment_add_contact_et_number.setState(
                    StateSelectedEditText.EditTextState.ERROR);
        } else {
            number = StringUtilities.keepNumbersOnly(number);
            number = number.trim();
            if(number.length() == 10) {
                fragment_add_contact_et_number.setState(
                        StateSelectedEditText.EditTextState.NOT_FOCUSED);
            } else {
                fragment_add_contact_et_number.setState(
                        StateSelectedEditText.EditTextState.ERROR);
            }
        }

        return toReturn;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v == this.fragment_add_contact_et_name){
            if(hasFocus){
                fragment_add_contact_et_name.setState(
                        StateSelectedEditText.EditTextState.FOCUSED);
                fragment_add_contact_et_number.setState(
                        StateSelectedEditText.EditTextState.NOT_FOCUSED);
            } else {
                fragment_add_contact_et_name.setState(
                        StateSelectedEditText.EditTextState.NOT_FOCUSED);
                fragment_add_contact_et_number.setState(
                        StateSelectedEditText.EditTextState.NOT_FOCUSED);
            }
        }
        if(v == this.fragment_add_contact_et_number){
            if(hasFocus){
                fragment_add_contact_et_name.setState(
                        StateSelectedEditText.EditTextState.NOT_FOCUSED);
                fragment_add_contact_et_number.setState(
                        StateSelectedEditText.EditTextState.FOCUSED);
            } else {
                fragment_add_contact_et_name.setState(
                        StateSelectedEditText.EditTextState.NOT_FOCUSED);
                fragment_add_contact_et_number.setState(
                        StateSelectedEditText.EditTextState.NOT_FOCUSED);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {
        if(s == fragment_add_contact_et_name.getEditableText()){

        }
        if(s == fragment_add_contact_et_number.getEditableText()){
            String str = s.toString();
            str = StringUtilities.keepNumbersOnly(str);
            str = str.trim();
            str = StringUtilities.formatStringLikePhoneNumber(str);
            fragment_add_contact_et_number.removeTextChangedListener(this);
            fragment_add_contact_et_number.setText(str);
            fragment_add_contact_et_number.setSelection(str.length());
            fragment_add_contact_et_number.addTextChangedListener(this);
        }
    }

    @Override
    public void onResume() {
        if(((CustomFragmentListener)getActivity()).getCurrentFragment() ==
                Constants.FRAGMENT_ADD_CONTACT) {
            ((CustomFragmentListener) getActivity()).setToolbarDetails(
                    getString(R.string.add_a_contact), null, true, false, null);
        }
        super.onResume();
    }
}
