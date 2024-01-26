package com.luma.testUtils.testObjects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDetails {
    private String email;
    private String firstName;
    private String lastName;
    private String company;
    private String[] streetAddress;
    private String city;
    private String stateProvince;
    private String zipPostalCode;
    private String country;
    private String phoneNumber;


    public AddressDetails(String email, String firstName, String lastName, String company, String[] streetAddress, String city, String stateProvince, String zipPostalCode, String country, String phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.streetAddress = streetAddress;
        this.city = city;
        this.stateProvince = stateProvince;
        this.zipPostalCode = zipPostalCode;
        this.country = country;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + "\n" +
                streetAddress[0] + ", " + streetAddress[1] + ", " + streetAddress[2] + "\n" +
                city + ", " + stateProvince + " " + zipPostalCode + "\n" +
                country + "\n" +
                phoneNumber;
    }
}
