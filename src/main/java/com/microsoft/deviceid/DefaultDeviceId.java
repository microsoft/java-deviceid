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
    protected String getDeviceId() throws IOException {
        Path rooPath = Paths.get(System.getProperty("user.home"), ".cache");
        return getDeviceId(rooPath);
    }

    // decouple getting the rootPath from this logic to allow for testing
    String getDeviceId(Path rootPath) throws IOException {
        Path path = rootPath.resolve(FOLDER);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Path deviceid = path.resolve("deviceid");
        if (!Files.exists(deviceid)) {
            Files.createFile(deviceid);
        }
        String uuid = new String(Files.readAllBytes(deviceid), java.nio.charset.StandardCharsets.UTF_8);
        if (uuid == null || uuid.isEmpty()) {
            uuid = java.util.UUID.randomUUID().toString();
            Files.write(deviceid, uuid.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
        return uuid;
    }
}
