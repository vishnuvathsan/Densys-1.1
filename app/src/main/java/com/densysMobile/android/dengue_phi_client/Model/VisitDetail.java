package com.densysMobile.android.dengue_phi_client.Model;

/**
 * Created by Sineth on 2/3/2017.
 */

public class VisitDetail {
    private String pTypeFever, pActionTaken, pComment, pVisitId;
    private ptDateAdd pDateInspect;
    public String getpTypeFever() {
        return pTypeFever;
    }

    public void setpTypeFever(String pTypeFever) {
        this.pTypeFever = pTypeFever;
    }

    public ptDateAdd getpDateInspect() {
        return pDateInspect;
    }

    public void setpDateInspect(ptDateAdd pDateInspect) {
        this.pDateInspect = pDateInspect;
    }

    public String getpActionTaken() {
        return pActionTaken;
    }

    public void setpActionTaken(String pActionTaken) {
        this.pActionTaken = pActionTaken;
    }

    public String getpComment() {
        return pComment;
    }

    public void setpComment(String pComment) {
        this.pComment = pComment;
    }

    public String getpVisitId() {
        return pVisitId;
    }

    public void setpVisitId(String pVisitId) {
        this.pVisitId = pVisitId;
    }

    @Override
    public String toString() {
        return "VisitDetail{" +
                "pTypeFever='" + pTypeFever + '\'' +
                ", pDateInspect='" + pDateInspect + '\'' +
                ", pActionTaken='" + pActionTaken + '\'' +
                ", comment='" + pComment + '\'' +
                ", pVisitId='" + pVisitId + '\'' +
                '}';
    }
}
