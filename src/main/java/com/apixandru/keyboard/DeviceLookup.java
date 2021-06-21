package com.apixandru.keyboard;

public interface DeviceLookup {

    ActualDevice findDevice(int vendorId, int productId) throws Exception;

}
