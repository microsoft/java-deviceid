// Copyright (c) Microsoft. All rights reserved.
//
// Licensed under the MIT license.
package com.microsoft.deviceid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MacDeviceIdTest {

    @TempDir
    Path tempDir;

    private MacDeviceId macDeviceId;

    @BeforeEach
    void setUp() throws IOException {
        macDeviceId = new MacDeviceId();
        tempDir = Files.createTempDirectory("deviceid-test");
    }

    @Test
    void testGetDeviceIdCreatesDirectoryAndFile() throws IOException {
        String deviceId = macDeviceId.getDeviceId(tempDir);
        Path deviceIdPath = tempDir.resolve("Library/Application Support/Microsoft/DeveloperTools/deviceid");
        assertTrue(Files.exists(deviceIdPath));
        assertEquals(deviceId, new String(Files.readAllBytes(deviceIdPath), java.nio.charset.StandardCharsets.UTF_8));
    }

    @Test
    void testGetDeviceIdReadsExistingUuid() throws IOException {
        Path deviceIdPath = tempDir.resolve("Library/Application Support/Microsoft/DeveloperTools/deviceid");
        Files.createDirectories(deviceIdPath.getParent());
        String existingUuid = "existing-uuid";
        Files.write(deviceIdPath, existingUuid.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        String deviceId = macDeviceId.getDeviceId(tempDir);
        assertEquals(existingUuid, deviceId);
    }

    @Test
    void testGetDeviceIdGeneratesNewUuidIfFileIsEmpty() throws IOException {
        Path deviceIdPath = tempDir.resolve("Library/Application Support/Microsoft/DeveloperTools/deviceid");
        Files.createDirectories(deviceIdPath.getParent());
        Files.createFile(deviceIdPath);

        String deviceId = macDeviceId.getDeviceId(tempDir);
        assertTrue(deviceId != null && !deviceId.isEmpty());
        assertEquals(deviceId, new String(Files.readAllBytes(deviceIdPath), java.nio.charset.StandardCharsets.UTF_8));
    }
}