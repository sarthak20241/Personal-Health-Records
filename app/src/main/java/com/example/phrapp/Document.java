package com.example.phrapp;

import android.app.appsearch.StorageInfo;
import android.net.Uri;

import com.google.firebase.storage.StorageReference;



import java.io.Serializable;

public class Document implements Serializable {

    private String documentName, description , doctorName , category,reportType;
    private String date;
    private transient String image;
    private String firebaseStorageUri;
    private String loggedInUserId;
    private long epoc;
    private String drugs,diagnosis;


    public String getDrugs() {
        return drugs;
    }

    public void setDrugs(String drugs) {
        this.drugs = drugs;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public long getEpoc() {
        return epoc;
    }
    public void setEpoc(long epoc) {
        this.epoc = epoc;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getLoggedInUserId() {
        return loggedInUserId;
    }

    public void setLoggedInUserId(String loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirebaseStorageUri() {
        return firebaseStorageUri;
    }

    public void setFirebaseStorageUri(String firebaseStorageUri) {
        this.firebaseStorageUri = firebaseStorageUri;
    }

    public Document(){
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Document(String documentName, String description, String doctorName, String category, String reportType, String date, String image, String firebaseStorageUri, String loggedInUserId, long epoc,String drugs, String diagnosis) {
        this.documentName = documentName;
        this.description = description;
        this.doctorName = doctorName;
        this.category = category;
        this.reportType = reportType;
        this.date = date;
        this.image = image;
        this.firebaseStorageUri = firebaseStorageUri;
        this.loggedInUserId = loggedInUserId;
        this.epoc = epoc;
        this.drugs = drugs;
        this.diagnosis=diagnosis;
    }
}