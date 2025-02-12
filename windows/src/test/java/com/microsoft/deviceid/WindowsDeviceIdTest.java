package com.microsoft.deviceid;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;
import static com.sun.jna.platform.win32.WinNT.KEY_WRITE;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class WindowsDeviceIdTest {
    
    private WindowsDeviceId windowsDeviceId;

    @BeforeEach
    void setUp() {
        windowsDeviceId = new WindowsDeviceId();
    }

    @Test
    void testGetDeviceIdCreatesRegistryKeyAndValue() {
        try (MockedStatic<Advapi32Util> advapi32UtilMockedStatic = Mockito.mockStatic(Advapi32Util.class)) {
            advapi32UtilMockedStatic.when(() -> Advapi32Util.registryKeyExists(HKEY_CURRENT_USER, "Software\\Microsoft\\DeveloperTools"))
                    .thenReturn(false);
            advapi32UtilMockedStatic.when(() -> Advapi32Util.registryValueExists(HKEY_CURRENT_USER, "Software\\Microsoft\\DeveloperTools", "deviceid"))
                    .thenReturn(false);
            advapi32UtilMockedStatic.when(() -> Advapi32Util.registryGetStringValue(HKEY_CURRENT_USER, "Software\\Microsoft\\DeveloperTools", "deviceid"))
                    .thenReturn("");

            String deviceId = windowsDeviceId.getDeviceId("Software\\Microsoft\\DeveloperTools");

            advapi32UtilMockedStatic.verify(() -> Advapi32Util.registryCreateKey(HKEY_CURRENT_USER, "Software\\Microsoft\\DeveloperTools", KEY_WRITE));
            advapi32UtilMockedStatic.verify(() -> Advapi32Util.registrySetStringValue(HKEY_CURRENT_USER, "Software\\Microsoft\\DeveloperTools", "deviceid", deviceId));
        }
    }

    @Test
    void testGetDeviceIdReadsExistingUuid() {
        try (MockedStatic<Advapi32Util> advapi32UtilMockedStatic = Mockito.mockStatic(Advapi32Util.class)) {
            String existingUuid = "existing-uuid";
            advapi32UtilMockedStatic.when(() -> Advapi32Util.registryKeyExists(HKEY_CURRENT_USER, "Software\\Microsoft\\DeveloperTools"))
                    .thenReturn(true);
            advapi32UtilMockedStatic.when(() -> Advapi32Util.registryValueExists(HKEY_CURRENT_USER, "Software\\Microsoft\\DeveloperTools", "deviceid"))
                    .thenReturn(true);
            advapi32UtilMockedStatic.when(() -> Advapi32Util.registryGetStringValue(HKEY_CURRENT_USER, "Software\\Microsoft\\DeveloperTools", "deviceid"))
                    .thenReturn(existingUuid);

            String deviceId = windowsDeviceId.getDeviceId("Software\\Microsoft\\DeveloperTools");

            assertEquals(existingUuid, deviceId);
        }
    }

    @Test
    void testGetDeviceIdThrowsWin32Exception() {
        try (MockedStatic<Advapi32Util> advapi32UtilMockedStatic = Mockito.mockStatic(Advapi32Util.class)) {
            advapi32UtilMockedStatic.when(() -> Advapi32Util.registryKeyExists(any(), anyString()))
                    .thenThrow(new Win32Exception(1));

            assertThrows(Win32Exception.class, () -> windowsDeviceId.getDeviceId("Software\\Microsoft\\DeveloperTools"));
        }
    }
}