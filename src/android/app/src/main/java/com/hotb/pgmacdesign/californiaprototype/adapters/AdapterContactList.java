package com.hotb.pgmacdesign.californiaprototype.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hotb.pgmacdesign.californiaprototype.R;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomClickCallbackLink;
import com.hotb.pgmacdesign.californiaprototype.listeners.CustomClickListener;
import com.hotb.pgmacdesign.californiaprototype.misc.Constants;
import com.hotb.pgmacdesign.californiaprototype.utilities.AnimationUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.ContactUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.ImageUtilities;
import com.hotb.pgmacdesign.californiaprototype.utilities.StringUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pmacdowell on 2017-02-14.
 */

public class AdapterContactList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<ContactUtilities.Contact> mListContacts = new ArrayList<>();

    private LayoutInflater mInflater;
    private int mPreviousPosition = 0;
    private CustomClickCallbackLink link;
    private ColorDrawable defaultDrawable;

    public static final int TYPE_CONTACT_ITEM = 0;
    public static final int TYPE_ALPHABET_ITEM = 1;

    private boolean oneSelectedAnimate;

    private CustomClickListener clickListener;

    private Context context;

    public static enum dataShowing {
        EMAIL,
        PHONE_NUMBER
    }

    public static final int TAG_EMAIL = 100;
    public static final int TAG_PHONE = 101;

    private dataShowing type;

    /**
     * Constructor
     * @param context Context (Needed for some calls)
     * @param link Link to interact with the main activity
     */
    public AdapterContactList(Context context, CustomClickCallbackLink link, dataShowing type){
        this.mInflater = LayoutInflater.from(context);
        this.link = link;
        this.context = context;
        this.clickListener = null;
        this.type = type;
        this.oneSelectedAnimate = false;
        this.defaultDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.white));
    }

    /**
     * Update all contacts and change the dataset
     * @param mListContacts
     */
    public void setContacts(List<ContactUtilities.Contact> mListContacts) {
        this.mListContacts = mListContacts;
        notifyDataSetChanged();
    }
    /**
     * For updating ONE contact
     * @param mContact
     * @param position
     */
    public void updateOneContact(ContactUtilities.Contact mContact, int position) {
        mListContacts.set(position, mContact);
        oneSelectedAnimate = true;
        notifyItemChanged(position);

    }
    /**
     * For removing ONE contact
     * @param position
     */
    public void removeOneContact(int position) {
        mListContacts.remove(position);
        notifyItemChanged(position);
    }



    /**
     * Constructs a Holder object.
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if(viewType == TYPE_CONTACT_ITEM){
            //view = mInflater.inflate(R.layout.recycler_contact_tile_v3, parent, false);
            view = mInflater.inflate(R.layout.recycler_contact_tile, parent, false);
            ContactHolder viewHolder = new ContactHolder(view);
            return viewHolder;
        } else if (viewType == TYPE_ALPHABET_ITEM){
            view = mInflater.inflate(R.layout.recycler_alphabet_tile, parent, false);
            AlphabetHolder viewHolder = new AlphabetHolder(view);
            return viewHolder;
        } else {
            //Shouldn't get hit, but just in case
            view = mInflater.inflate(R.layout.recycler_contact_tile, parent, false);
            ContactHolder viewHolder = new ContactHolder(view);
            return viewHolder;
        }

    }

    /**
     * Manages all the drawerData to be inserted into the recyclerview.
     * @param holder1 The view holder being used (Of type gift_individual layout)
     * @param position The position in the list being traversed (IE 0, 1, 2, 3)
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {

        ContactUtilities.Contact currentContact = mListContacts.get(position);

        boolean isHeader = currentContact.isHeader();
        if(isHeader){

            AlphabetHolder holder = (AlphabetHolder) holder1;
            String str = currentContact.getHeaderString();
            if(str == null){
                str = "";
            } else {
                //str = str.substring(0, 1).toUpperCase();
                //This should already be done by now, removing this so I can add "Favorites" in there
            }
            holder.recycler_contact_tile_textview.setText(str);

        } else {
            ContactHolder holder = (ContactHolder) holder1;

            //Name
            String name = currentContact.getRawDisplayName();
            if(name != null){
                holder.recycler_contact_tile_name.setText(name);
            } else {
                holder.recycler_contact_tile_name.setText("Unknown");
            }

            //Image
            String imageUri = currentContact.getPhotoUri();
            if (!StringUtilities.isNullOrEmpty(imageUri)) {
                holder.recycler_contact_tile_image.setVisibility(View.VISIBLE);
                ImageUtilities.setCircularImageWithPicasso(imageUri,
                        holder.recycler_contact_tile_image, R.color.white);
            } else {
                holder.recycler_contact_tile_image.setVisibility(View.INVISIBLE);
            }

            int clickListenerTagFromConstants = -1;

            //Loop through the types and set the respective data

            if(type == dataShowing.EMAIL){
                clickListenerTagFromConstants = TAG_EMAIL;

                String combinedEmails = currentContact.getSimplifiedEmail();
                String combinedEmailTypes = currentContact.getSimplifiedEmailType();

                if(combinedEmails != null){

                    holder.recycler_contact_tile_dynamic_data.setText(combinedEmails);
                } else {
                    holder.recycler_contact_tile_dynamic_data.setText("No Email");
                }

                if(combinedEmailTypes != null){
                    holder.recycler_contact_tile_type.setText(combinedEmailTypes);
                } else {
                    holder.recycler_contact_tile_type.setText("");
                }
            }

            if(type == dataShowing.PHONE_NUMBER){
                clickListenerTagFromConstants = TAG_PHONE;

                List<ContactUtilities.Contact.Phone> phones = currentContact.getPhone();

                String combinedNumbers = currentContact.getSimplifiedPhoneNumber();
                String combinedNumberTypes = currentContact.getSimplifiedPhoneNumberType();

                if(combinedNumbers != null){

                    holder.recycler_contact_tile_dynamic_data.setText(combinedNumbers);
                } else {
                    holder.recycler_contact_tile_dynamic_data.setText("No Phone Numbers");
                }

                if(combinedNumberTypes != null){
                    holder.recycler_contact_tile_type.setText(combinedNumberTypes);
                } else {
                    holder.recycler_contact_tile_type.setText("");
                }

            }

            //Check if selected and alter background colors or not
            boolean isSelected = currentContact.isSelectedInList();
            if(isSelected){
                holder.recycler_contact_tile_main_layout0.setBackgroundColor(
                        ContextCompat.getColor(context, R.color.LightGrey));
                holder.recycler_contact_tile_checkmark.setVisibility(View.VISIBLE);
            } else{
                holder.recycler_contact_tile_main_layout0.setBackgroundColor(
                        ContextCompat.getColor(context, R.color.white));
                holder.recycler_contact_tile_checkmark.setVisibility(View.GONE);
            }

            if(clickListenerTagFromConstants != -1) {
                //Set Tags
                holder.recycler_contact_tile_type.setTag(clickListenerTagFromConstants);
                holder.recycler_contact_tile_name.setTag(clickListenerTagFromConstants);
                holder.recycler_contact_tile_main_layout0.setTag(clickListenerTagFromConstants);
                holder.recycler_contact_tile_dynamic_data.setTag(clickListenerTagFromConstants);

                clickListener = new CustomClickListener(
                        link, clickListenerTagFromConstants, currentContact, position);

                //Set click listeners
                holder.recycler_contact_tile_type.setOnClickListener(clickListener);
                holder.recycler_contact_tile_name.setOnClickListener(clickListener);
                holder.recycler_contact_tile_main_layout0.setOnClickListener(clickListener);
                holder.recycler_contact_tile_dynamic_data.setOnClickListener(clickListener);
            }

        }

        if(oneSelectedAnimate){
            oneSelectedAnimate = false;
            boolean isSelected = currentContact.isSelectedInList();
            if(isSelected) {
                AnimationUtilities.animateMyView(holder1, 900, Constants.IN_FLIP_X);
            } else {
                AnimationUtilities.animateMyView(holder1, 900, Constants.IN_RUBBERBAND);
            }
        }

        //Animation here
        if (position > mPreviousPosition) {
            //AnimationUtilities.animateMyView(holder1, 500, Constants.IN_FLIP_X);
        } else {
            //AnimationUtilities.animateMyView(holder1, 400, Constants.IN_RIGHT_SLIDE);
        }
        mPreviousPosition = position;
    }


    /**
     * @return Returns the size of the list of Contest Items
     */
    @Override
    public int getItemCount() {
        try {
            return mListContacts.size();
        } catch (Exception e){
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        ContactUtilities.Contact contact = mListContacts.get(position);
        if(contact == null){
            return TYPE_ALPHABET_ITEM;
        }

        boolean isHeader = contact.isHeader();
        if(isHeader){
            return TYPE_ALPHABET_ITEM;
        } else {
            return TYPE_CONTACT_ITEM;
        }
    }


    /**
     * This is for Contact Objects
     * Custom view holder class.
     */
    class ContactHolder extends RecyclerView.ViewHolder {

        private RelativeLayout recycler_contact_tile_main_layout,
                recycler_contact_tile_main_layout0,
                recycler_contact_tile_bottom_layout,
                recycler_contact_tile_top_layout;
        private TextView recycler_contact_tile_circle_divider;
        private ImageView recycler_contact_tile_checkmark, recycler_contact_tile_image,
                recycler_contact_tile_llverified_iv,
                recycler_contact_tile_background_left_button,
                recycler_contact_tile_background_right_button;
        private TextView recycler_contact_tile_name,
                recycler_contact_tile_type, recycler_contact_tile_dynamic_data;

        //Contact Holder class constructor
        public ContactHolder(View itemView) {
            super(itemView);

            recycler_contact_tile_background_left_button = (ImageView) itemView.findViewById(
                    R.id.recycler_contact_tile_background_left_button);
            recycler_contact_tile_background_right_button = (ImageView) itemView.findViewById(
                    R.id.recycler_contact_tile_background_right_button);
            recycler_contact_tile_main_layout0 = (RelativeLayout) itemView.findViewById(
                    R.id.recycler_contact_tile_main_layout0);
            recycler_contact_tile_main_layout = (RelativeLayout) itemView.findViewById(
                    R.id.recycler_contact_tile_main_layout);
            recycler_contact_tile_bottom_layout = (RelativeLayout) itemView.findViewById(
                    R.id.recycler_contact_tile_bottom_layout);
            recycler_contact_tile_top_layout = (RelativeLayout) itemView.findViewById(
                    R.id.recycler_contact_tile_top_layout);
            recycler_contact_tile_image = (ImageView) itemView.findViewById(
                    R.id.recycler_contact_tile_image);
            recycler_contact_tile_checkmark = (ImageView) itemView.findViewById(
                    R.id.recycler_contact_tile_checkmark);
            recycler_contact_tile_llverified_iv = (ImageView) itemView.findViewById(
                    R.id.recycler_contact_tile_llverified_iv);
            recycler_contact_tile_name = (TextView)
                    itemView.findViewById(R.id.recycler_contact_tile_name);
            recycler_contact_tile_dynamic_data = (TextView)
                    itemView.findViewById(R.id.recycler_contact_tile_dynamic_data);
        }
    }

    /**
     * For alphabet letter sorting
     */
    class AlphabetHolder extends RecyclerView.ViewHolder {
        private RelativeLayout recycler_contact_tile_alphabet_layout;
        private TextView recycler_contact_tile_textview;

        //Contact Holder class constructor
        public AlphabetHolder(View itemView) {
            super(itemView);
            recycler_contact_tile_textview = (TextView) itemView.findViewById(
                    R.id.recycler_contact_tile_textview);
            recycler_contact_tile_alphabet_layout = (RelativeLayout) itemView.findViewById(
                    R.id.recycler_contact_tile_alphabet_layout);
        }
    }

}
