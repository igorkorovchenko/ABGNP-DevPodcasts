package com.example.android.devpodcasts.controllers.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.android.devpodcasts.R;
import com.example.android.devpodcasts.models.Channel;
import com.example.android.devpodcasts.models.ChannelArrayAdapter;

public class ChannelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);

        setTitle(getResources().getString(R.string.app_name)
                + " "
                + getResources().getString(R.string.channels_activity_name));

        ListView channelsListView = findViewById(R.id.channels_list);
        channelsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent itemIntent = new  Intent(
                        ChannelsActivity.this,
                        ChannelDetailActivity.class
                );
                Channel channelInfo = (Channel) view.getTag();
                itemIntent.putExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_ID, channelInfo.getId());
                itemIntent.putExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_ICON, channelInfo.getIconUrl());
                itemIntent.putExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_NAME, channelInfo.getName());
                itemIntent.putExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_INFO, channelInfo.getInfo());
                itemIntent.putExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_RATING, channelInfo.getRating());
                itemIntent.putExtra(ChannelArrayAdapter.CHANNEL_KEY_FOR_LANGUAGE, channelInfo.getLang());
                itemIntent.putExtra(
                        ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_ITUNES,
                        channelInfo.getSources().get(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_ITUNES)
                );
                itemIntent.putExtra(
                        ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_GOOGLEPLAY,
                        channelInfo.getSources().get(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_GOOGLEPLAY)
                );
                itemIntent.putExtra(
                        ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_PODSTER,
                        channelInfo.getSources().get(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_PODSTER)
                );
                itemIntent.putExtra(
                        ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_SITE,
                        channelInfo.getSources().get(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_SITE)
                );
                itemIntent.putExtra(
                        ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_SOUNDCLOUD,
                        channelInfo.getSources().get(ChannelArrayAdapter.CHANNEL_KEY_FOR_SOURCE_SOUNDCLOUD)
                );
                startActivity(itemIntent);
            }
        });

        setupChannelsListView("");
        SearchView searchInChannels = (SearchView) findViewById(R.id.channels_search);
        searchInChannels.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setupChannelsListView(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchInChannels.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setupChannelsListView("");
                return false;
            }
        });
    }

    /**
     * Setting up the list of channels
     *
     * @param searchQuery string contains search words or nothing for view all channels
     */
    private void setupChannelsListView(String searchQuery) {
        String channelJsonInfoFileName = "channels.json";
        ChannelArrayAdapter adapter = (
                new ChannelArrayAdapter.Builder(getBaseContext(), channelJsonInfoFileName, searchQuery)
        ).get();
        ListView channelsListView = findViewById(R.id.channels_list);
        channelsListView.setAdapter(adapter);
    }
}
