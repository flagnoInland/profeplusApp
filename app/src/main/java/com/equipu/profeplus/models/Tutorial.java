package com.equipu.profeplus.models;

import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Herbert Caller on 6/28/2016.
 * Esta clase construye manuales que serán visualizados por medio de TutorialFragment.
 * Los títulos y contenidos son obtenidos del recurso arrays.xml
 * Los identificadores de las figuras están en el recurso integers.xml
 * Esta clase permite navegar a través de las páginas.
 */
public class Tutorial implements Parcelable {

    int pageNumber;
    String pageTitle;
    String pageContent;
    String[] content;
    String[] titles;
    int[] images;
    int pageImage;


    public Tutorial(String[] content, String[] titles, TypedArray imgs){
        setPageNumber(0);
        setPageTitle(titles[0]);
        setPageContent(content[0]);
        setTitles(titles);
        setContent(content);
        images = new int[imgs.length()];
        for(int i=0; i<imgs.length(); i++){
            images[i] = imgs.getResourceId(i,0);
        }
        setImages(images);
        setPageImage(images[0]);

    }

    protected Tutorial(Parcel in) {
        pageNumber = in.readInt();
        pageTitle = in.readString();
        pageContent = in.readString();
        content = in.createStringArray();
        titles = in.createStringArray();
        images = in.createIntArray();
        pageImage = in.readInt();
    }

    public static final Creator<Tutorial> CREATOR = new Creator<Tutorial>() {
        @Override
        public Tutorial createFromParcel(Parcel in) {
            return new Tutorial(in);
        }

        @Override
        public Tutorial[] newArray(int size) {
            return new Tutorial[size];
        }
    };

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public int getPageImage() {
        return pageImage;
    }

    public void setPageImage(int pageImage) {
        this.pageImage = pageImage;
    }

    public int[] getImages() {
        return images;
    }

    public void setImages(int[] images) {
        this.images = images;
    }

    public void nextPage() {
        pageNumber = pageNumber + 1;
        pageContent = content[pageNumber];
        pageTitle = titles[pageNumber];
        pageImage = images[pageNumber];
    }

    public void previousPage(){
        pageNumber = pageNumber - 1;
        if (pageNumber < 0){
            pageNumber = 0;
        }
        pageContent = content[pageNumber];
        pageTitle = titles[pageNumber];
        pageImage = images[pageNumber];
    }

    public boolean hasPage(){
        if (content.length == pageNumber+1){
            return false;
        } else {
            return true;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pageNumber);
        dest.writeString(pageTitle);
        dest.writeString(pageContent);
        dest.writeStringArray(content);
        dest.writeStringArray(titles);
        dest.writeIntArray(images);
        dest.writeInt(pageImage);
    }
}
