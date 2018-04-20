package com.example.android.devpodcasts.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.devpodcasts.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class
 *
 * @package com.example.android.devpodcasts.models
 * (c) 2018, Igor Korovchenko.
 */

public class TrackArrayAdapter extends ArrayAdapter<Track> {

    /**
     * Keys for getting info from JSON for tracks'
     */
    public static final String TRACK_KEY_FOR_NAME = "name";
    public static final String TRACK_KEY_FOR_DURATION = "duration";
    public static final String TRACK_KEY_FOR_LIKES = "likes";
    public static final String TRACK_KEY_FOR_PLAYS = "plays";

    /**
     * Context of the App
     */
    private final Context mContext;

    /**
     * Array of channels' info
     */
    private final Track[] mValues;

    /**
     * Constructor of the class
     *
     * @param context context of the App
     * @param values array with Channel objects as elements
     */
    public TrackArrayAdapter(@NonNull Context context, Track[] values) {
        super(context, R.layout.track_info, values);
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
        View rowView = inflater.inflate(R.layout.track_info, parent, false);
        TextView trackName = rowView.findViewById(R.id.track_name);
        TextView trackDuration = rowView.findViewById(R.id.track_duration);
        TextView trackLikes = rowView.findViewById(R.id.track_likes);
        TextView trackPlays = rowView.findViewById(R.id.track_plays);
        TextView trackCreated = rowView.findViewById(R.id.track_created);
        trackName.setText(mValues[position].getName());
        trackDuration.setText(mValues[position].getDurationFormattedString());
        trackLikes.setText(String.valueOf(mValues[position].getLikes()));
        trackPlays.setText(String.valueOf(mValues[position].getPlays()));
        trackCreated.setText(mValues[position].getCreatedFormattedString());
        return rowView;
    }

    public static class Builder {

        /**
         * TAG name for debugging
         */
        private static final String TAG = "TrackArrayAdapterBuilder";

        /**
         * Messages for debugging
         */
        private static final String MSG_ERR_WRONG_JSON = "Wrong JSON.";

        /**
         * ChannelArrayAdapter object
         */
        private TrackArrayAdapter mAdapter;

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
                mAdapter = new TrackArrayAdapter(
                        context,
                        parseJSON(new JSONObject(loadJSONFromAsset(context, jsonFileName)))
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * Getting TrackArrayAdapter object
         *
         * @return TrackArrayAdapter object
         */
        public TrackArrayAdapter get() {
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
         * @param tracksInfo JSON object with all information of the tracks
         * @return Array of all tracks from JSON file
         */
        @SuppressLint("LongLogTag")
        private Track[] parseJSON(JSONObject tracksInfo) {
            ArrayList<Track> tracks = new ArrayList<>(0);

            try {
                Iterator<?> channelKeys = tracksInfo.keys();
                while (channelKeys.hasNext()) {
                    String channelCode = String.valueOf(channelKeys.next());
                    if ((channelCode.equals(mSearchRequest)) || (mSearchRequest.equals(""))) {
                        if (tracksInfo.get(channelCode) instanceof JSONObject) {
                            JSONObject channelTracks = new JSONObject(tracksInfo.get(channelCode).toString());
                            Iterator<?> tracksKeys = channelTracks.keys();
                            while (tracksKeys.hasNext()) {
                                String trackCode = String.valueOf(tracksKeys.next());
                                JSONObject trackDetails = new JSONObject(channelTracks.get(trackCode).toString());
                                String trackName = trackDetails.getString(TRACK_KEY_FOR_NAME);
                                Integer trackDuration = trackDetails.getInt(TRACK_KEY_FOR_DURATION);
                                Integer trackLikes = trackDetails.getInt(TRACK_KEY_FOR_LIKES);
                                Integer trackPlays = trackDetails.getInt(TRACK_KEY_FOR_PLAYS);
                                Track track = new Track(
                                        Long.valueOf(trackCode),
                                        trackName,
                                        trackDuration,
                                        trackLikes,
                                        trackPlays
                                );
                                tracks.add(track);
                            }
                        } else {
                            Log.d(TAG, MSG_ERR_WRONG_JSON);
                        }
                    } else {
                        Log.d(TAG, MSG_ERR_WRONG_JSON);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return tracks.toArray(new Track[0]);
        }
    }
}
