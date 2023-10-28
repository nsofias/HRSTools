/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nsofias
 */
public class CustomerEvent {

    private final String MSISDN;
    private String info;
    private String activationDate;
    private String status;

    public CustomerEvent(String MSISDN, String activationDate, String status, String info) {
        this.MSISDN = MSISDN;
        this.info = info;
        this.activationDate = activationDate;
        this.status = status;
    }

    /**
     * @return the MSISDN
     */
    public String getMSISDN() {
        return MSISDN;
    }

    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * @return the activationDate
     */
    public String getActivationDate() {
        return activationDate;
    }

    /**
     * @param activationDate the activationDate to set
     */
    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
