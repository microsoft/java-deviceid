// Copyright (c) Microsoft. All rights reserved.
//
// Licensed under the MIT license.
package com.microsoft.deviceid;

import java.io.IOException;

/**
 * DeviceId is an abstract class that provides a method to get the device ID.
 * The actual implementation of the device ID depends on the operating system.
 */
public abstract class DeviceId {

    private static final DeviceId INSTANCE = create();
    private static DeviceId create() {

        String impl = null;
        switch (OperatingSystem.current()) {
            case WINDOWS: 
                impl = "com.microsoft.deviceid.WindowsDeviceId";
                break;
            case LINUX:
                impl = "com.microsoft.deviceid.LinuxDeviceId";
                break;
            case MACOS:
                impl = "com.microsoft.deviceid.MacDeviceId";
                break;
            default:
                impl = "com.microsoft.deviceid.DefaultDeviceId";
                break;
        };
    
        try {
            Object result = Class.forName(impl, false, DeviceId.class.getClassLoader()).getDeclaredConstructor().newInstance();
            return (DeviceId) result;
        } catch (Exception e) {
            throw new InternalError(
                "Can't instantiate deviceid implementation "
                + impl, e);
        }
    }

    /**
     * Gets the device ID. The value is a UUID. In the case where the cached
     * id cannot be retrieved, or the newly generated id cannot be cached,
     * a value of {@code null} should be returned.
     * @return the device ID, or {@code null} if the ID cannot be retrieved
     */
    public static String deviceId() {
        return INSTANCE.getDeviceId();
    }

    /**
     * Operating system specific method to get the device ID. The value is a UUID
     * that follows the 8-4-4-4-12 format. The value shall be all lowercase and
     * only contain hyphens. No braces or brackets.
     * <p>
     * The implementation will create the ID if it does not exist. In the case
     * where the cached id cannot be retrieved, or the newly generated id cannot
     * be cached, a value of {@code null} should be returned.
     * @return the device ID, or {@code null} if the ID cannot be retrieved
     */
    protected abstract String getDeviceId();

    protected DeviceId() {}

}
