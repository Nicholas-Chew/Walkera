package model;

/**
 * Created by ChewZhijie on 3/5/2017.
 */

class user_Address {
    private String address;
    private String postalCode;
    private String address_name;

    user_Address(String address, String postalCode, String address_name)
    {
        this.address = address;
        this.postalCode = postalCode;
        this.address_name = address_name;
    }

    public void setAddress(String address) {this.address = address;}
    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}
    public void setAddress_name(String address_name) {this.address_name = address_name;}

    public String getAddress() {return this.address;}
    public String getPostalCode() {return this.postalCode;}
    public String getAddress_name() {return this.address_name;}

}
