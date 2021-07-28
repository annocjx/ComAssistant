//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mapsoft.bean;

import java.io.Serializable;

public class AssistBean implements Serializable {
    private static final long serialVersionUID = -5620661009186692227L;
    private boolean isTxt = true;
    private String SendTxtA = "COMA";
    private String SendTxtB = "COMB";
    private String SendTxtC = "COMC";
    private String SendTxtD = "COMD";
    private String SendHexA = "AA";
    private String SendHexB = "BB";
    private String SendHexC = "CC";
    private String SendHexD = "DD";
    public String sTimeA = "500";
    public String sTimeB = "500";
    public String sTimeC = "500";
    public String sTimeD = "500";

    public AssistBean() {
    }

    public boolean isTxt() {
        return this.isTxt;
    }

    public void setTxtMode(boolean isTxt) {
        this.isTxt = isTxt;
    }

    public String getSendA() {
        return this.isTxt ? this.SendTxtA : this.SendHexA;
    }

    public String getSendB() {
        return this.isTxt ? this.SendTxtB : this.SendHexB;
    }

    public String getSendC() {
        return this.isTxt ? this.SendTxtC : this.SendHexC;
    }

    public String getSendD() {
        return this.isTxt ? this.SendTxtD : this.SendHexD;
    }

    public void setSendA(String sendA) {
        if (this.isTxt) {
            this.SendTxtA = sendA;
        } else {
            this.SendHexA = sendA;
        }

    }

    public void setSendB(String sendB) {
        if (this.isTxt) {
            this.SendTxtB = sendB;
        } else {
            this.SendHexB = sendB;
        }

    }

    public void setSendC(String sendC) {
        if (this.isTxt) {
            this.SendTxtC = sendC;
        } else {
            this.SendHexC = sendC;
        }

    }

    public void setSendD(String sendD) {
        if (this.isTxt) {
            this.SendTxtD = sendD;
        } else {
            this.SendHexD = sendD;
        }

    }
}
