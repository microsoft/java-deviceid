// Copyright (c) Microsoft. All rights reserved.
//
// Licensed under the MIT license.
package com.microsoft.deviceid;

import java.util.Locale;

enum OperatingSystem {
    WINDOWS,
    LINUX,
    MACOS;

    static OperatingSystem current() {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (osName.contains("windows")) {
            return WINDOWS;
        } else if (osName.contains("nux") || osName.contains("nix") || osName.contains("aix")) {
            return LINUX;
        } else if (osName.contains("mac")) {
            return MACOS;
        } else {
            return null;
        }
    }

}
