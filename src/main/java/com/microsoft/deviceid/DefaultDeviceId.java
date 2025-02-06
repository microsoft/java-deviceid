// Copyright (c) Microsoft. All rights reserved.
//
// Licensed under the MIT license.
package com.microsoft.deviceid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Fallback implementation of DeviceId.
 */
public class DefaultDeviceId extends DeviceId {

    private static final String FOLDER = "Microsoft/DeveloperTools";

    public DefaultDeviceId() {
        super();
    }

    @Override
    protected String getDeviceId() {
        return null;
    }

}
