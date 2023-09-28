package com.example.softeng306plantasticapp.data;

public interface FirestoreDocumentCallback {

    /**
     * Method that allows for handling objects retrieved from the firestore database
     * @param customObject the object retrieved from firestore
     */
    void onDocumentFetched(Object customObject);

    /**
     * Method for handing failures when retrieving objects from the firestore database
     * @param e
     */
    void onFailure(Exception e);
}
