package com.example.android.devpodcasts.controllers.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.devpodcasts.R;
import com.example.android.devpodcasts.models.Channel;
import com.example.android.devpodcasts.models.ChannelArrayAdapter;
import com.example.android.devpodcasts.models.TrackArrayAdapter;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChannelDetailActivity extends AppCompatActivity {

    private Channel mChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_details);

        setTitle(getResources().getString(R.string.app_name)
                + " "
                + getResources().getString(R.string.channel_detail_activity_name));

        setChannel();
        Resources r = getResources();
        float sizeInPixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ChannelArrayAdapter.ICON_WIDTH_HEIGHT,
                r.getDisplayMetrics()
        );
        ImageView channelIconImageView = findViewById(R.id.channel_detail_item);
        if (!mChannel.getIconUrl().equals("")) {
            Picasso.get()
                    .load(mChannel.getIconUrl())
                    .placeholder(R.drawable.channel_icon)
                    .error(R.drawable.channel_icon)
                    .resize((int) sizeInPixels,
                            (int) sizeInPixels)
                    .into(channelIconImageView);
        } else {
            Picasso.get()
                    .load(R.drawable.channel_icon)
                    .into(channelIconImageView);
        }
        TextView channelNameTextView = findViewById(R.id.channel_detail_name);
        channelNameTextView.setText(mChannel.getName());
        TextView channelInfoTextView = findViewById(R.id.channel_detail_description);
        channelInfoTextView.setText(mChannel.getInfo());
        RatingBar channelRatingRatingBar = findViewById(R.id.channel_detail_rating);
        channelRatingRatingBar.setRating(Float.valueOf(String.valueOf(mChannel.getRating())));
        setupIcon(
                (ImageView) findViewById(R.id.channel_detail_itunes),
                mChannel.getSources().get(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_ITUNES)
        );
        setupIcon(
                (ImageView) findViewById(R.id.channel_detail_soundcloud),
                mChannel.getSources().get(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_SOUNDCLOUD)
        );
        setupIcon(
                (ImageView) findViewById(R.id.channel_detail_podster),
                mChannel.getSources().get(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_PODSTER)
        );
        setupIcon(
                (ImageView) findViewById(R.id.channel_detail_googleplay),
                mChannel.getSources().get(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_GOOGLEPLAY)
        );
        setupIcon(
                (ImageView) findViewById(R.id.channel_detail_site),
                mChannel.getSources().get(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_SITE)
        );

        setupTracksListView();
    }

    /**
     * Setting all values for mChannel from the Intent
     */
    private void setChannel() {
        Intent itemIntent = getIntent();
        Integer channelId = itemIntent.getIntExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_ID, -1);
        String channelIconUrl = itemIntent.getStringExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_ICON);
        String channelName = itemIntent.getStringExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_NAME);
        String channelInfo = itemIntent.getStringExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_INFO);
        Double channelRating = itemIntent.getDoubleExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_RATING, 0.0);
        String channelLanguage = itemIntent.getStringExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_LANGUAGE);
        String channelSourceITunes = itemIntent.getStringExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_ITUNES);
        String channelSourceGooglePlay = itemIntent.getStringExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_GOOGLEPLAY);
        String channelSourcePodster = itemIntent.getStringExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_PODSTER);
        String channelSourceSite = itemIntent.getStringExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_SITE);
        String channelSourceSoundcloud = itemIntent.getStringExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_SOUNDCLOUD);
        Map<String, String> channelSource = new HashMap<>();
        channelSource.put(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_ITUNES, channelSourceITunes);
        channelSource.put(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_GOOGLEPLAY, channelSourceGooglePlay);
        channelSource.put(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_PODSTER, channelSourcePodster);
        channelSource.put(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_SITE, channelSourceSite);
        channelSource.put(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_SOUNDCLOUD, channelSourceSoundcloud);

        mChannel = new Channel(
                channelId,
                channelName,
                channelIconUrl,
                Calendar.getInstance(),
                channelLanguage,
                channelInfo,
                channelRating,
                channelSource
        );
    }

    /**
     * Setting up the ImageView for each icon
     *
     * @param iconImageView
     * @param iconUrl
     */
    private void setupIcon(ImageView iconImageView, final String iconUrl) {
        if (iconUrl.equals("")) {
            iconImageView.setVisibility(View.GONE);
        } else {
            iconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(iconUrl));
                    startActivity(i);
                }
            });
            iconImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(mChannel.getName(), iconUrl);
                    if (clipboard != null) {
                        clipboard.setPrimaryClip(clip);
                    }
                    Toast.makeText(getBaseContext(), R.string.toast_clipboard, Toast.LENGTH_LONG).show();
                    return false;
                }
            });
        }
    }

    /**
     * Setting up the list of tracks
     */
    private void setupTracksListView() {
        String channelJsonInfoFileName = "tracks.json";
        TrackArrayAdapter adapter = (
                new TrackArrayAdapter.Builder(getBaseContext(), channelJsonInfoFileName, String.valueOf(mChannel.getId()))
        ).get();
        ListView tracksListView = findViewById(R.id.tracks_list);
        tracksListView.setAdapter(adapter);
    }
}
