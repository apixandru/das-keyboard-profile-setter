package com.apixandru.keyboard;

public interface ActualDevice extends AutoCloseable {

    void write(byte[] message);

    void open() throws Exception;

    String getDescriptor();

}
