/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tools;

import java.util.Objects;

/**
 *
 * @author nsofias
 */
public class CustomerEvent {

    private final String MSISDN;
    private final String info;
    private final String activationDate;
    private final String status;
    private final double value;


    public CustomerEvent(String MSISDN, String activationDate, String status, String info, double value) {
        this.MSISDN = MSISDN;
        this.info = info;
        this.activationDate = activationDate;
        this.status = status;
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.MSISDN);
        hash = 89 * hash + Objects.hashCode(this.activationDate);
        hash = 89 * hash + Objects.hashCode(this.status);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CustomerEvent other = (CustomerEvent) obj;
        if (!Objects.equals(this.MSISDN, other.MSISDN)) {
            return false;
        }
        if (!Objects.equals(this.activationDate, other.activationDate)) {
            return false;
        }
        return Objects.equals(this.status, other.status);
    }

    @Override
    public String toString() {
        return MSISDN + ";" + activationDate + ";" + status + ";" + info+ ";" + value;
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
     * @return the activationDate
     */
    public String getActivationDate() {
        return activationDate;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

}
