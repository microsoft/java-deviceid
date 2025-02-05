// Copyright (c) Microsoft. All rights reserved.
//
// Licensed under the MIT license.
package com.microsoft.deviceid;

import java.io.IOException;

import com.sun.jna.platform.win32.Advapi32Util;

import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;
import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;

/**
 * Windows implementation of DeviceId.
 */
class WindowsDeviceId implments DeviceId {
    
    private static final String REGISTRY_KEY = "Software\Microsoft\DeveloperTools";

    @Override
    protected String getDeviceId() throws IOException {
        return getDeviceId(REGISTRY_KEY);
    }

    // decouple getting the registry key from this logic to allow for testing
    String getDeviceId(String registryKey) throws IOException {
        try {
            if (!Advapi32Util.registryKeyExists(HKEY_CURRENT_USER, REGISTRY_KEY)) {
                Advapi32Util.registryCreateKey(HKEY_CURRENT_USER, REGISTRY_KEY, WinNT.KEY_WRITE);
            }

            if (!Advapi32Util.registryValueExists(HKEY_CURRENT_USER, REGISTRY_KEY, "deviceid")) {
                Advapi32Util.registryCreateKey(HKEY_CURRENT_USER, REGISTRY_KEY, "deviceid", WinNT.KEY_WRITE);
            }
            String uuid = Advapi32Util.registryGetStringValue(HKEY_CURRENT_USER, REGISTRY_KEY, "deviceid");
            if (uuid == null || uuid.isEmpty()) {
                Advapi32Util.registrySetStringValue(HKEY_CURRENT_USER, REGISTRY_KEY, "deviceid", java.util.UUID.randomUUID().toString());
            } 
            return uuid;
        } catch (Win32Exception e) {
            throw new IOException(e);
        }
        return null;
    }

}
