// Copyright (c) Microsoft. All rights reserved.
//
// Licensed under the MIT license.
package com.microsoft.deviceid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Linux implementation of DeviceId.
 * <ol>
 * <li>The folder path will be &lt;RootPath&gt;/Microsoft/DeveloperTools where &lt;RootPath&gt; 
 * is $XDG_CACHE_HOME if it is set and not empty, else use $HOME/.cache.</li>
 * <li>The file will be called 'deviceid'.</li>
 * <li>The value should be stored in plain text, UTF-8.</li> 
 * </ol>
 */
class LinuxDeviceId extends DeviceId {
    
    private static final String FOLDER = "Microsoft/DeveloperTools";

    @Override
    protected String getDeviceId() {
        try {
            String cacheHome = System.getenv("XDG_CACHE_HOME");
            if (cacheHome == null || cacheHome.isEmpty()) {
                cacheHome = System.getProperty("user.home");
            }
            Path rootPath = Paths.get(cacheHome, ".cache");
            return getDeviceId(rootPath);
        } catch (IOException e) {
            return null;
        }
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
