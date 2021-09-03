
package com.hoarauthomas.go4lunchthp7;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Prediction {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("matched_substrings")
    @Expose
    private List<com.hoarauthomas.go4lunchthp7.MatchedSubstring> matchedSubstrings = null;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("structured_formatting")
    @Expose
    private com.hoarauthomas.go4lunchthp7.StructuredFormatting structuredFormatting;
    @SerializedName("terms")
    @Expose
    private List<com.hoarauthomas.go4lunchthp7.Term> terms = null;
    @SerializedName("types")
    @Expose
    private List<String> types = null;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<com.hoarauthomas.go4lunchthp7.MatchedSubstring> getMatchedSubstrings() {
        return matchedSubstrings;
    }

    public void setMatchedSubstrings(List<com.hoarauthomas.go4lunchthp7.MatchedSubstring> matchedSubstrings) {
        this.matchedSubstrings = matchedSubstrings;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public com.hoarauthomas.go4lunchthp7.StructuredFormatting getStructuredFormatting() {
        return structuredFormatting;
    }

    public void setStructuredFormatting(com.hoarauthomas.go4lunchthp7.StructuredFormatting structuredFormatting) {
        this.structuredFormatting = structuredFormatting;
    }

    public List<com.hoarauthomas.go4lunchthp7.Term> getTerms() {
        return terms;
    }

    public void setTerms(List<com.hoarauthomas.go4lunchthp7.Term> terms) {
        this.terms = terms;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

}
