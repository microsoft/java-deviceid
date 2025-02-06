// Copyright (c) Microsoft. All rights reserved.
//
// Licensed under the MIT license.
package com.microsoft.deviceid;

import java.io.IOException;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;

import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;
import static com.sun.jna.platform.win32.WinNT.KEY_WRITE;

/**
 * Windows implementation of DeviceId.
 * <ol>
 * <li>The value is cached in the 64-bit Windows Registry under HKeyCurrentUser\SOFTWARE\Microsoft\DeveloperTools.</li>
 * <li>The key should be named 'deviceid' and should be of type REG_SZ (String value).</li>
 * <li>The value should be stored in plain text.</li>
 * </ol>
 */
class WindowsDeviceId extends DeviceId {
    
    private static final String REGISTRY_KEY = "Software\\Microsoft\\DeveloperTools";

    @Override
    protected String getDeviceId() {
        // In case the cached id cannot be retrieved, or the newly generated
        // id cannot be cached, a value of null should be returned
        try {
            return getDeviceId(REGISTRY_KEY);
        } catch (Win32Exception e) {
            return null;
        }
    }

    // decouple getting the registry key from this logic to allow for testing
    String getDeviceId(String registryKey) throws Win32Exception {
        if (!Advapi32Util.registryKeyExists(HKEY_CURRENT_USER, REGISTRY_KEY)) {
            Advapi32Util.registryCreateKey(HKEY_CURRENT_USER, REGISTRY_KEY, KEY_WRITE);
        }

        if (!Advapi32Util.registryValueExists(HKEY_CURRENT_USER, REGISTRY_KEY, "deviceid")) {
            Advapi32Util.registryCreateKey(HKEY_CURRENT_USER, REGISTRY_KEY, "deviceid", KEY_WRITE);
        }
        String uuid = Advapi32Util.registryGetStringValue(HKEY_CURRENT_USER, REGISTRY_KEY, "deviceid");
        if (uuid == null || uuid.isEmpty()) {
            uuid = java.util.UUID.randomUUID().toString();
            Advapi32Util.registrySetStringValue(HKEY_CURRENT_USER, REGISTRY_KEY, "deviceid", uuid);
        } 
        return uuid;
    }

}
