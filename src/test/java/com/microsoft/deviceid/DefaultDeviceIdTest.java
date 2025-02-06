// Copyright (c) Microsoft. All rights reserved.
//
// Licensed under the MIT license.
package com.microsoft.deviceid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultDeviceIdTest {

    private DefaultDeviceId defaultDeviceId;

    @BeforeEach
    public void setUp() throws IOException {
        defaultDeviceId = new DefaultDeviceId();
    }

    @Test
    public void testGetDeviceIdReturnsNull() {
        String deviceId = defaultDeviceId.getDeviceId();
        assertNull(deviceId);
    }
}
