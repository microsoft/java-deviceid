// Copyright (c) Microsoft. All rights reserved.
//
// Licensed under the MIT license.
package com.microsoft.deviceid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Dev Device ID for Mac OS X.
 */
class MacDeviceId extends DeviceId {
    
    private static final String FOLDER = "Library/Application Support/Microsoft/DeveloperTools";

    @Override
    protected String getDeviceId() throws IOException {
        Path rootPath = Paths.get(System.getProperty("user.home"));
        return getDeviceId(rootPath);
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
