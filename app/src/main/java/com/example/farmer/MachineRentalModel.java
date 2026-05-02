package com.example.farmer;

public class MachineRentalModel {
    public String personName, mobile, machine, rentPrice, address;
    public long timestamp;

    public MachineRentalModel() {}

    public MachineRentalModel(String personName, String mobile, String machine, String rentPrice, String address, long timestamp) {
        this.personName = personName;
        this.mobile = mobile;
        this.machine = machine;
        this.rentPrice = rentPrice;
        this.address = address;
        this.timestamp = timestamp;
    }
}
