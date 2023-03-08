package com.example.gofundmenepal;

public class Details {
    public String messagename , messageamount ,messagedetails ;

    public Details(){


    }

    public Details(String messagename, String messageamount, String messagedetails) {
        this.messagename = messagename;
        this.messageamount = messageamount;
        this.messagedetails = messagedetails;
    }

    public String getMessagename() {
        return messagename;
    }

    public String getMessageamount() {
        return messageamount;
    }

    public String getMessagedetails() {
        return messagedetails;
    }
}
