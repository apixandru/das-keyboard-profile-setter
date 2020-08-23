package com.apixandru.keyboard;

import java.util.function.Consumer;

public interface HidDeviceIo {

    void send(Consumer<byte[]> message) throws Exception;

}
