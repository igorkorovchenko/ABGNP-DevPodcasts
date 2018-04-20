package com.example.android.devpodcasts.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.devpodcasts.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Adapter for working with ListView in activity_channels.xml layout
 *
 * @package com.example.android.devpodcasts.models
 * (c) 2018, Igor Korovchenko.
 */
public class ChannelArrayAdapter extends ArrayAdapter<Channel> {

    /**
     * Keys for getting info from JSON for channels'
     */
    public static final String CHANNEL_KEY_FOR_ID = "id";
    public static final String CHANNEL_KEY_FOR_NAME = "name";
    public static final String CHANNEL_KEY_FOR_ICON = "icon";
    public static final String CHANNEL_KEY_FOR_LANGUAGE = "lang";
    public static final String CHANNEL_KEY_FOR_INFO = "info";
    public static final String CHANNEL_KEY_FOR_RATING = "rating";
    public static final String CHANNEL_KEY_FOR_SOURCE = "source";
    public static final String CHANNEL_KEY_FOR_SOURCE_ITUNES = "itunes";
    public static final String CHANNEL_KEY_FOR_SOURCE_GOOGLEPLAY = "googleplay";
    public static final String CHANNEL_KEY_FOR_SOURCE_SOUNDCLOUD = "soundcloud";
    public static final String CHANNEL_KEY_FOR_SOURCE_PODSTER = "podster";
    public static final String CHANNEL_KEY_FOR_SOURCE_SITE = "site";

    /**
     * Size of the channel's icon
     */
    public static final Integer ICON_WIDTH_HEIGHT = 48;

    /**
     * Context of the App
     */
    private final Context mContext;

    /**
     * Array of channels' info
     */
    private final Channel[] mValues;

    /**
     * Constructor of the class
     *
     * @param context context of the App
     * @param values array with Channel objects as elements
     */
    ChannelArrayAdapter(@NonNull Context context, Channel[] values) {
        super(context, R.layout.channel_info, values);
        this.mContext = context;
        this.mValues = values;
    }

    /**
     * Overriden method for inflating of the list item
     *
     * @param position position of the item
     * @param convertView the old view to reuse
     * @param parent ListView of element
     * @return view for the list item
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("ViewHolder")
        View rowView = inflater.inflate(R.layout.channel_info, parent, false);
        TextView channelName = rowView.findViewById(R.id.channel_name);
        TextView channelUpdated = rowView.findViewById(R.id.channel_updated);
        TextView channelLanguage = rowView.findViewById(R.id.channel_language);
        ImageView channelIcon = rowView.findViewById(R.id.channel_icon);
        String name = mValues[position].getName();
        if (!name.equals("")) {
            channelName.setText(name);
        } else {
            channelName.setText(mContext.getResources().getString(R.string.default_channel_name));
        }
        channelUpdated.setText(mValues[position].getUpdateDateAndTime());
        channelLanguage.setText(mValues[position].getLang());
        String iconUrl = mValues[position].getIconUrl();
        Resources r = mContext.getResources();
        float sizeInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ICON_WIDTH_HEIGHT, r.getDisplayMetrics());
        if (!iconUrl.equals("")) {
            Picasso.get()
                    .load(iconUrl)
                    .placeholder(R.drawable.channel_icon)
                    .error(R.drawable.channel_icon)
                    .resize((int) sizeInPixels, (int) sizeInPixels)
                    .into(channelIcon);
        } else {
            Picasso.get()
                    .load(R.drawable.channel_icon)
                    .into(channelIcon);
        }
        rowView.setTag(mValues[position]);
        return rowView;
    }

    public static class Builder {

        /**
         * TAG name for debugging
         */
        private static final String TAG = "ChannelArrayAdapterBuilder";

        /**
         * Messages for debugging
         */
        private static final String MSG_ERR_WRONG_JSON = "Wrong JSON.";

        /**
         * ChannelArrayAdapter object
         */
        private ChannelArrayAdapter mAdapter;

        /**
         * Search word
         */
        private final String mSearchRequest;

        /**
         * Constructor of the class
         *
         * @param context context of the app
         * @param jsonFileName file name in assets folder
         */
        public Builder(Context context, String jsonFileName, String searchRequest) {
            this.mSearchRequest = searchRequest;
            try {
                mAdapter = new ChannelArrayAdapter(
                        context,
                        parseJSON(new JSONObject(loadJSONFromAsset(context, jsonFileName)))
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * Getting ChannelArrayAdapter object
         *
         * @return ChannelArrayAdapter object
         */
        public ChannelArrayAdapter get() {
            return mAdapter;
        }

        /**
         * Getting JSON string from the file in assets folder
         *
         * @param context context of the app
         * @param jsonFileName file name in assets folder
         * @return JSON object as a string from the file
         */
        private String loadJSONFromAsset(Context context, String jsonFileName) {
            String jsonString;
            try {
                InputStream inputStream = context.getAssets().open(jsonFileName);
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                jsonString = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return jsonString;
        }

        /**
         * Parsing of the team model from the JSON string.
         * Convert JSON info to instances of class fields.
         *
         * @param channelsInfo JSON object with all information of the channel
         * @return Array of all channels from JSON file
         */
        @SuppressLint("LongLogTag")
        private Channel[] parseJSON(JSONObject channelsInfo) {
            ArrayList<Channel> channels = new ArrayList<>(0);

            try {
                Iterator<?> channelsKeys = channelsInfo.keys();
                while (channelsKeys.hasNext()) {
                    String channelCode = String.valueOf(channelsKeys.next());
                    if (channelsInfo.get(channelCode) instanceof JSONObject) {
                        JSONObject channelDetails = new JSONObject(channelsInfo.get(channelCode).toString());
                        String channelName = channelDetails.getString(CHANNEL_KEY_FOR_NAME);
                        if ((channelName.toUpperCase().contains(mSearchRequest.toUpperCase())) || (mSearchRequest.equals(""))) {
                            Integer channelId = Integer.valueOf(channelDetails.getString(CHANNEL_KEY_FOR_ID));
                            String channelIconUrl = channelDetails.getString(CHANNEL_KEY_FOR_ICON);
                            String channelLanguage = channelDetails.getString(CHANNEL_KEY_FOR_LANGUAGE);
                            String channelInfo = channelDetails.getString(CHANNEL_KEY_FOR_INFO);
                            Double channelRating = channelDetails.getDouble(CHANNEL_KEY_FOR_RATING);
                            JSONObject channelSourcesInfo = new JSONObject(channelDetails.get(CHANNEL_KEY_FOR_SOURCE).toString());
                            String channelSourceItunes = channelSourcesInfo.getString(CHANNEL_KEY_FOR_SOURCE_ITUNES);
                            String channelSourceGooglePlay = channelSourcesInfo.getString(CHANNEL_KEY_FOR_SOURCE_GOOGLEPLAY);
                            String channelSourceSoundcloud = channelSourcesInfo.getString(CHANNEL_KEY_FOR_SOURCE_SOUNDCLOUD);
                            String channelSourcePodster = channelSourcesInfo.getString(CHANNEL_KEY_FOR_SOURCE_PODSTER);
                            String channelSourceSite = channelSourcesInfo.getString(CHANNEL_KEY_FOR_SOURCE_SITE);
                            Map<String, String> channelSources = new HashMap<>();
                            channelSources.put(CHANNEL_KEY_FOR_SOURCE_ITUNES, channelSourceItunes);
                            channelSources.put(CHANNEL_KEY_FOR_SOURCE_GOOGLEPLAY, channelSourceGooglePlay);
                            channelSources.put(CHANNEL_KEY_FOR_SOURCE_SOUNDCLOUD, channelSourceSoundcloud);
                            channelSources.put(CHANNEL_KEY_FOR_SOURCE_PODSTER, channelSourcePodster);
                            channelSources.put(CHANNEL_KEY_FOR_SOURCE_SITE, channelSourceSite);
                            Calendar channelUpdated = Calendar.getInstance();
                            Channel channel = new Channel(
                                    channelId,
                                    channelName,
                                    channelIconUrl,
                                    channelUpdated,
                                    channelLanguage,
                                    channelInfo,
                                    channelRating,
                                    channelSources
                            );
                            channels.add(channel);
                        }
                    } else {
                        Log.d(TAG, MSG_ERR_WRONG_JSON);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return channels.toArray(new Channel[0]);
        }
    }
}
