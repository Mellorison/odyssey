/*
 * Copyright (C) 2020 Team Gateship-One
 * (Hendrik Borghorst & Frederik Luetkes)
 *
 * The AUTHORS.md file contains a detailed contributors list:
 * <https://github.com/gateship-one/odyssey/blob/master/AUTHORS.md>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.gateshipone.odyssey.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import org.gateshipone.odyssey.models.android.AndroidAlbumModel;

public abstract class AlbumModel implements GenericModel, Parcelable {
    private static final String TAG = AlbumModel.class.getSimpleName();
    /**
     * The name of the album
     */
    protected final String mAlbumName;

    /**
     * The url for the album cover
     */
    private final String mAlbumArtURL;

    /**
     * The name of the artist for the current album
     */
    protected final String mArtistName;

    /**
     * The date as an integer when this album was added to the device
     */
    private final int mDateAdded;

    private String mMBID;

    private boolean mImageFetching;

    public AlbumModel(String name, String albumArtURL, String artistName, int dateAdded) {
        if (name != null) {
            mAlbumName = name;
        } else {
            mAlbumName = "";
        }

        if (albumArtURL != null) {
            mAlbumArtURL = albumArtURL;
        } else {
            mAlbumArtURL = "";
        }

        if (artistName != null) {
            mArtistName = artistName;
        } else {
            mArtistName = "";
        }


        mDateAdded = dateAdded;
    }

    /**
     * Constructs a AlbumModel instance with the given parameters.
     */
    public AlbumModel(String name, String albumArtURL, String artistName) {
        this(name, albumArtURL, artistName, -1);
    }

    /**
     * Constructs a AlbumModel from a Parcel.
     * <p>
     * see {@link Parcelable}
     */
    protected AlbumModel(Parcel in) {
        // Class name
        in.readString();
        mAlbumName = in.readString();
        mAlbumArtURL = in.readString();
        mArtistName = in.readString();
        mDateAdded = in.readInt();
        mMBID = in.readString();
        mImageFetching = in.readByte() != 0;
    }


    /**
     * Return the name of the album
     */
    public String getAlbumName() {
        return mAlbumName;
    }

    /**
     * Return the url for the album cover
     */
    public String getAlbumArtURL() {
        return mAlbumArtURL;
    }

    /**
     * Return the name of the related artist
     */
    public String getArtistName() {
        return mArtistName;
    }

    public int getDateAdded() {
        return mDateAdded;
    }

    /**
     * Return the AlbumModel as a String for debugging purposes.
     */
    @NonNull
    @Override
    public String toString() {
        return "Album: " + getAlbumName() + " from: " + getArtistName();
    }

    @Override
    public boolean equals(Object album) {
        if (null == album) {
            return false;
        }
        if (album instanceof AlbumModel) {
            return mAlbumName.equals(((AlbumModel) album).mAlbumName)
                    && mArtistName.equals(((AlbumModel) album).mArtistName);
        } else {
            return false;
        }
    }

    /**
     * Return the section title for the AlbumModel
     * <p/>
     * The section title is the name of the album.
     */
    @Override
    public String getSectionTitle() {
        return mAlbumName;
    }

    public void setMBID(String mbid) {
        mMBID = mbid;
    }

    public String getMBID() {
        return mMBID;
    }


    public synchronized void setFetching(boolean fetching) {
        mImageFetching = fetching;
    }

    public synchronized boolean getFetching() {
        return mImageFetching;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     * <p/>
     * see {@link Parcelable}
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * <p/>
     * see {@link Parcelable}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getClass().getName());
        dest.writeString(mAlbumName);
        dest.writeString(mAlbumArtURL);
        dest.writeString(mArtistName);
        dest.writeInt(mDateAdded);
        dest.writeString(mMBID);
        dest.writeByte((byte) (mImageFetching ? 1 : 0));
    }

    public String getArtworkID() {
        return mAlbumName;
    }


    /**
     * Provide CREATOR field that generates a AlbumModel instance from a Parcel.
     * <p/>
     * see {@link Parcelable}
     */
    public static final Creator<AlbumModel> CREATOR = new Creator<AlbumModel>() {
        @Override
        public AlbumModel createFromParcel(Parcel in) {
            int pos_before = in.dataPosition();
            String className = in.readString();
            // Reset parcel
            in.setDataPosition(pos_before);
            if (className == null || className.isEmpty()) {
                return null;
            }
            if (className.equals(AndroidAlbumModel.class.getName())) {
                return AndroidAlbumModel.CREATOR.createFromParcel(in);
            }
            return null;
        }

        @Override
        public AlbumModel[] newArray(int size) {
            return new AlbumModel[size];
        }
    };
}
