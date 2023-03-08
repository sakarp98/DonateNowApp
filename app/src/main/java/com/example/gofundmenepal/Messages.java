package com.example.gofundmenepal;

public class Messages {
    private String NeedyDetails , NeedyName , Needyimage , RequiredAmount ;

    public Messages(String needyDetails, String needyName, String needyimage, String requiredAmount) {
        NeedyDetails = needyDetails;
        NeedyName = needyName;
        Needyimage = needyimage;
        RequiredAmount = requiredAmount;
    }

    public String getNeedyDetails() {
        return NeedyDetails;
    }

    public void setNeedyDetails(String needyDetails) {
        NeedyDetails = needyDetails;
    }

    public String getNeedyName() {
        return NeedyName;
    }

    public void setNeedyName(String needyName) {
        NeedyName = needyName;
    }

    public String getNeedyimage() {
        return Needyimage;
    }

    public void setNeedyimage(String needyimage) {
        Needyimage = needyimage;
    }

    public String getRequiredAmount() {
        return RequiredAmount;
    }

    public void setRequiredAmount(String requiredAmount) {
        RequiredAmount = requiredAmount;
    }
    public Messages(){

    }
}
