package com.hotb.pgmacdesign.californiaprototype.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.hotb.pgmacdesign.californiaprototype.animations.CircleTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pmacdowell on 2017-02-13.
 */
public class ImageUtilities {


    public static <T extends ImageView> void setCircularImageWithPicasso(String urlThumbnail,
                                                                         final T viewToSet,
                                                                         final int backupImageResourceId,
                                                                         Context context){
        if(context == null){
            context = viewToSet.getContext();
        }
        final Context fContext = context;

        if(StringUtilities.isNullOrEmpty(urlThumbnail)){
            try {
                Picasso.with(fContext).load(backupImageResourceId).
                        transform(new CircleTransform()).into(viewToSet);

            } catch (Exception e){}
        } else {

            final String innerUrlThumbnail = urlThumbnail;

            try {
                Picasso.with(fContext)
                        .load(innerUrlThumbnail)
                        .transform(new CircleTransform())
                        .into(viewToSet, new Callback() {
                            @Override
                            public void onSuccess() {
                                //Load the image into cache for next time
                                try {
                                    List<String> toCache = new ArrayList<String>();
                                    toCache.add(innerUrlThumbnail);
                                    ImageUtilities.LoadImagesIntoPicassoCache async = new
                                            ImageUtilities.LoadImagesIntoPicassoCache(toCache, fContext);
                                    async.execute();
                                } catch (Exception e2){}
                            }
                            @Override
                            public void onError() {
                                Picasso.with(fContext).load(innerUrlThumbnail)
                                        .transform(new CircleTransform())
                                        .into(viewToSet);
                                //Load the image into cache for next time
                                try {
                                    List<String> toCache = new ArrayList<String>();
                                    toCache.add(innerUrlThumbnail);
                                    ImageUtilities.LoadImagesIntoPicassoCache async = new
                                            ImageUtilities.LoadImagesIntoPicassoCache(toCache, fContext);
                                    async.execute();
                                } catch (Exception e2){}
                            }
                        });
            } catch (Exception e){
                try {
                    Picasso.with(fContext).load(backupImageResourceId).
                            transform(new CircleTransform()).into(viewToSet);
                } catch (Exception e1){}
            }
        }
    }

    /**
     * Loads images into the picasso cache by using the fetch() call. Reference:
     * http://stackoverflow.com/questions/23978828/how-do-i-use-disk-caching-in-picasso
     */
    public static class LoadImagesIntoPicassoCache extends AsyncTask<Void, Void, Void> {
        private List<String> imageURLs;
        private Context context;
        private int cacheSizeMax;

        /**
         * Load Images into cache constructor
         * @param imageURLs A list of the image URLs to set
         * @param context Context
         */
        public LoadImagesIntoPicassoCache(List<String> imageURLs, Context context){
            this.imageURLs = imageURLs;
            this.context = context;
            this.cacheSizeMax = 0;
        }
        /**
         * Overloaded Constructor
         * @param imageURLs A list of the image URLs to set
         * @param context Context
         * @param cacheSizeMax Int for max cache size. If left out or set to null, it will default
         *                     to the auto generated max (which is about 1/7 available ram)
         *                     Link: http://stackoverflow.com/questions/20090265/android-picasso-configure-lrucache-size
         */
        public LoadImagesIntoPicassoCache(List<String> imageURLs, Context context, Integer cacheSizeMax){
            this.imageURLs = imageURLs;
            this.context = context;
            if(cacheSizeMax == null){
                this.cacheSizeMax = 0;
            } else {
                this.cacheSizeMax = cacheSizeMax;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            Picasso p;

            if(cacheSizeMax > 0) {
                p = new Picasso.Builder(context)
                        .memoryCache(new LruCache(cacheSizeMax))
                        .build();
            } else {
                p = new Picasso.Builder(context)
                        .build();
            }

            for(String str : imageURLs){
                try {
                    if(isCancelled()){
                        return null;
                    }
                    p.with(context).load(str).fetch();
                    Thread.sleep(500);
                } catch (OutOfMemoryError e1){
                    //If we run out of memory, make sure to catch it!
                    p.with(context).invalidate(str);
                    return null;
                } catch (Exception e){}
            }

            return null;
        }
    }

}
