package com.example.android.devpodcasts.models;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Channel model
 *
 * @package com.example.android.devpodcasts.models
 * (c) 2018, Igor Korovchenko.
 */
public class Channel {

    private final String DATE_TIME_FORMAT = "EEE, d MMM yyyy HH:mm:ss";
    private final String DATE_TIME_PREFIX = "Upd.: ";

    /**
     * ID of the channel
     */
    private Integer mId;

    /**
     * Name of the channel
     */
    private String mName;

    /**
     * URL of the channel's icon
     */
    private String mIconUrl;

    /**
     * Name of the channel
     */
    private String mLang;

    /**
     * Info of the channel
     */
    private String mInfo;

    /**
     * Rating of the channel
     */
    private Double mRating;

    /**
     * Date and time of the last update
     */
    private Calendar mUpdateDateAndTime;

    /**
     * All links for channel
     */
    private Map<String, String> mSources;

    /**
     * Constructor of the class
     *
     * @param id id of the channel
     * @param name name of the channel
     * @param iconUrl URL of the channel's icon
     * @param updateDateAndTime date and time of the last update
     * @param language content language of the channel
     * @param info short description of the channel
     * @param rating average rating of the channel inside the app
     * @param sources dictionary with all links to podcast channel
     */
    public Channel(Integer id,
            String name,
            String iconUrl,
            Calendar updateDateAndTime,
            String language,
            String info,
            Double rating,
            Map<String, String> sources) {

        setId(id);
        setName(name);
        setIconUrl(iconUrl);
        setUpdateDateAndTime(updateDateAndTime);
        setLanguage(language);
        setSources(sources);
        setInfo(info);
        setRating(rating);
    }

    /**
     * Getting ID of the channel
     *
     * @return channel ID
     */
    public Integer getId() {
        return mId;
    }

    /**
     * Getting the name of the channel
     *
     * @return name of the channel
     */
    public String getName() {
        return mName;
    }

    /**
     * Getting the icon URL of the channel
     *
     * @return icon of the channel
     */
    public String getIconUrl() {
        return mIconUrl;
    }

    /**
     * Getting the language of the channel
     *
     * @return language of the channel
     */
    public String getLang() {
        return mLang;
    }

    /**
     * Getting the info about channel
     *
     * @return short info of the channel
     */
    public String getInfo() {
        return mInfo;
    }

    /**
     * Getting the average user's rating inside the app
     *
     * @return rating as Double variable
     */
    public Double getRating() {
        return mRating;
    }

    /**
     * Getting the date and time of the channel
     *
     * @return date and time of the channel
     */
    public String getUpdateDateAndTime() {
        return updateDateAndTimeToString();
    }

    /**
     * Getting a map with sources (links to podcast)
     *
     * @return dictionary with links
     */
    public Map<String, String> getSources() {
        return mSources;
    }

    /**
     * Setting ID of the channel
     *
     * @param id new ID of the channel
     */
    public void setId(Integer id) {
        this.mId = id;
    }

    /**
     * Setting the name of the channel
     *
     * @param name new name of the channel
     */
    public void setName(String name) {
        this.mName = name;
    }

    /**
     * Setting the channel's icon URL
     *
     * @param iconUrl new icon of the channel
     */
    public void setIconUrl(String iconUrl) {
        this.mIconUrl = iconUrl;
    }

    /**
     * Setting the language of the channel
     *
     * @param language new language of the channel
     */
    public void setLanguage(String language) {
        this.mLang = language;
    }

    /**
     * Setting the info of the channel
     *
     * @param info short info of the channel
     */
    public void setInfo(String info) {
        this.mInfo = info;
    }

    /**
     * Setting the rating of the channel
     *
     * @param rating rating as Double value
     */
    public void setRating(Double rating) {
        this.mRating = rating;
    }

    /**
     * Setting the updating date of the channel
     *
     * @param dateAndTime new updating date of the channel
     */
    public void setUpdateDateAndTime(Calendar dateAndTime) {
        this.mUpdateDateAndTime = dateAndTime;
    }

    /**
     * Setting links for podcast channel
     *
     * @param sources dictionary with links
     */
    public void setSources(Map<String, String> sources) {
        this.mSources = sources;
    }

    /**
     * Getting date and time in corrected format due to DATE_TIME_FORMAT
     *
     * @return string of the channel's updating time
     */
    private String updateDateAndTimeToString() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
        return DATE_TIME_PREFIX + df.format(mUpdateDateAndTime.getInstance().getTime());
    }
}
