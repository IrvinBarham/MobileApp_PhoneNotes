package com.example.androidnotes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.StringWriter;

public class Notes implements Parcelable {
    private String titleText;
    private String typed;


    public Notes() {
        this.titleText = "New Note";
        this.typed =  "";
    }

    public Notes(String title, String typedText){
        this.titleText = title;
        this.typed = typedText;
    }

    public String getTitle() {
        return titleText;
    }

    public String getBody() {
        return typed;
    }

    public void setTitle(String title){
        this.titleText = title;
    }

    public void setBody(String typedText){
        this.typed = typedText;
    }

    @NonNull
    @Override
    public String toString() {
        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("title").value(getTitle());
            jsonWriter.name("description").value(getBody());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Note Title: " + titleText;
    }

    @Override
    public int describeContents() { return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titleText);
        dest.writeString(typed);
    }

    private Notes(Parcel in){
        titleText = in.readString();
        typed = in.readString();
    }
}
