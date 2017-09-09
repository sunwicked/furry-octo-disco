package app.com.application.main;

/**
 * Created by admin on 09/09/17.
 */


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataModel implements Parcelable, Comparable {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("TITLE")
    @Expose
    private String title;
    @SerializedName("URL")
    @Expose
    private String url;
    @SerializedName("PUBLISHER")
    @Expose
    private String publisher;
    @SerializedName("CATEGORY")
    @Expose
    private String category;
    @SerializedName("HOSTNAME")
    @Expose
    private String hostname;
    @SerializedName("TIMESTAMP")
    @Expose
    private String timestamp;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String uRL) {
        this.url = uRL;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.iD);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.publisher);
        dest.writeString(this.category);
        dest.writeString(this.hostname);
        dest.writeString(this.timestamp);
    }

    public DataModel() {
    }

    protected DataModel(Parcel in) {
        this.iD = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.url = in.readString();
        this.publisher = in.readString();
        this.category = in.readString();
        this.hostname = in.readString();
        this.timestamp = in.readString();
    }

    public static final Parcelable.Creator<DataModel> CREATOR = new Parcelable.Creator<DataModel>() {
        @Override
        public DataModel createFromParcel(Parcel source) {
            return new DataModel(source);
        }

        @Override
        public DataModel[] newArray(int size) {
            return new DataModel[size];
        }
    };


    @Override
    public int compareTo(@NonNull Object o) {
        DataModel dataModelTwo = (DataModel) o;
        if (Long.parseLong(this.getTimestamp()) > Long.parseLong(dataModelTwo.getTimestamp())) {
            return 1;
        } else if ((Long.parseLong(this.getTimestamp()) < Long.parseLong(dataModelTwo.getTimestamp()))) {
            return -1;
        } else
            return 0;
    }
}