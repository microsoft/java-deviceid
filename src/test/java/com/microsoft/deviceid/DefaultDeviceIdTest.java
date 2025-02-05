// Copyright (c) Microsoft. All rights reserved.
//
// Licensed under the MIT license.
package com.microsoft.deviceid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultDeviceIdTest {

    private DefaultDeviceId defaultDeviceId;
    private Path testPath;

    @BeforeEach
    public void setUp() throws IOException {
        defaultDeviceId = new DefaultDeviceId();
        testPath = Files.createTempDirectory("deviceid-test");
    }

    @Test
    public void testGetDeviceIdCreatesDirectoryAndFile() throws IOException {
        String deviceId = defaultDeviceId.getDeviceId(testPath);
        assertTrue(Files.exists(testPath));
        assertTrue(Files.exists(testPath.resolve("Microsoft/DeveloperTools/deviceid")));
        assertNotNull(deviceId);
        assertFalse(deviceId.isEmpty());
    }

    @Test
    public void testGetDeviceIdReturnsSameId() throws IOException {
        String deviceId1 = defaultDeviceId.getDeviceId(testPath);
        String deviceId2 = defaultDeviceId.getDeviceId(testPath);
        assertEquals(deviceId1, deviceId2);
    }

    @Test
    public void testGetDeviceIdGeneratesNewIdIfFileIsEmpty() throws IOException {
        Path deviceIdFile = testPath.resolve("Microsoft/DeveloperTools/deviceid");
        Files.createDirectories(deviceIdFile.getParent());
        Files.createFile(deviceIdFile);
        String deviceId = defaultDeviceId.getDeviceId(testPath);
        assertNotNull(deviceId);
        assertFalse(deviceId.isEmpty());
    }

    @Test
    public void testGetDeviceIdReadsExistingId() throws IOException {
        Path deviceIdFile = testPath.resolve("Microsoft/DeveloperTools/deviceid");
        Files.createDirectories(deviceIdFile.getParent());
        String expectedId = "test-uuid";
        Files.write(deviceIdFile, expectedId.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        String deviceId = defaultDeviceId.getDeviceId(testPath);
        assertEquals(expectedId, deviceId);
    }
}
