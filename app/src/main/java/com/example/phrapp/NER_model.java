package com.example.phrapp;

public class NER_model {
    String type;
    String subtype;
    String results;

    public NER_model(String type, String subtype, String results) {
        this.type=type;
        this.subtype=subtype;
        this.results=results;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }
    public void setSubtype(String subtype) {
        this.subtype=subtype;
    }
    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }
}
