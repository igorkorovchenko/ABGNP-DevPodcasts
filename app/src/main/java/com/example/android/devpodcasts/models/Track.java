package com.example.android.devpodcasts.models;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Track model
 *
 * @package com.example.android.devpodcasts.models
 * (c) 2018, Igor Korovchenko.
 */

public class Track {

    /**
     * Date format
     */
    static String CREATION_DATE_FORMAT_FOR_TRACK = "MM/dd/yy";

    /**
     * Date and time in POSIX format of creation of the track
     */
    private Long mCreated;

    /**
     * Name of the track
     */
    private String mName;

    /**
     * Duration of the track
     */
    private Integer mDuration;

    /**
     * Number of likes of the track
     */
    private Integer mLikes;

    /**
     * Number of plays of the track
     */
    private Integer mPlays;

    /**
     * Constructor of the class
     *
     * @param created date and time of track's creation
     * @param name name of the track
     * @param duration duration of the track
     * @param likes number of track's likes
     * @param plays number of track's plays
     */
    Track(Long created, String name, Integer duration, Integer likes, Integer plays) {
        setCreated(created);
        setName(name);
        setDuration(duration);
        setLikes(likes);
        setPlays(plays);
    }

    /**
     * Getting POSIX time of the track's creation
     *
     * @return date and time of creation of the track
     */
    public Long getCreated() {
        return mCreated;
    }

    /**
     * Getting time of the track's creation in string format
     *
     * @return creation time of the track in string format
     */
    @SuppressLint("SimpleDateFormat")
    public String getCreatedFormattedString() {
        Long createdInMilliseconds = mCreated * 1000;
        Date createdDate = new java.util.Date(createdInMilliseconds);
        return new SimpleDateFormat(CREATION_DATE_FORMAT_FOR_TRACK).format(createdDate);
    }

    /**
     * Getting of the track's name
     *
     * @return name of the track
     */
    public String getName() {
        return mName;
    }

    /**
     * Getting of the track's duration
     *
     * @return duration of the track
     */
    public Integer getDuration() {
        return mDuration;
    }

    /**
     * Getting of the track's duration in string format
     *
     * @return duration of the track in string format
     */
    public String getDurationFormattedString() {
        Integer value = getDuration();
        Integer hours = value / 3600;
        Integer remainder = value - hours * 3600;
        Integer minutes = remainder / 60;
        remainder = remainder - minutes * 60;
        Integer seconds = remainder;
        return String.valueOf(hours)
                + ":" + String.valueOf(minutes)
                + ":" + String.valueOf(seconds);
    }

    /**
     * Getting of the track's likes
     *
     * @return likes of the track
     */
    public Integer getLikes() {
        return mLikes;
    }

    /**
     * Getting of the track's plays
     *
     * @return plays of the track
     */
    public Integer getPlays() {
        return mPlays;
    }

    /**
     * Setting POSIX time of the track's creation
     *
     * @param created date and time of creation of the track
     */
    public void setCreated(Long created) {
        this.mCreated = created;
    }

    /**
     * Setting of the track's name
     *
     * @param name name of the track
     */
    public void setName(String name) {
        this.mName = name;
    }

    /**
     * Setting of the track's duration
     *
     * @param duration duration of the track
     */
    public void setDuration(Integer duration) {
        this.mDuration = duration;
    }

    /**
     * Setting of the track's likes
     *
     * @param likes likes of the track
     */
    public void setLikes(Integer likes) {
        this.mLikes = likes;
    }

    /**
     * Setting of the track's plays
     *
     * @param plays plays of the track
     */
    public void setPlays(Integer plays) {
        this.mPlays = plays;
    }
}
