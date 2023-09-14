/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nsofias
 */
public class Customer {

    public Customer(String MSISDN) {
        this.MSISDN = MSISDN;
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
    private final String MSISDN;
    private String info;

}
