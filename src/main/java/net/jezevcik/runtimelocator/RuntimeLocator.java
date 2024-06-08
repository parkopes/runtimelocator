package net.jezevcik.runtimelocator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RuntimeLocator {

    private static final String[] REGISTRY = new String[] {
            "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Runtime Environment::JavaHome",
            "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit::JavaHome",
            "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\JRE::JavaHome",
            "HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\JDK::JavaHome",

            "HKEY_LOCAL_MACHINE\\SOFTWARE\\AdoptOpenJDK\\JRE::Path::Hotspot\\MSI",
            "HKEY_LOCAL_MACHINE\\SOFTWARE\\AdoptOpenJDK\\JDK::Path::Hotspot\\MSI",

            "HKEY_LOCAL_MACHINE\\SOFTWARE\\Eclipse Foundation\\JRE::Path::Hotspot\\MSI",
            "HKEY_LOCAL_MACHINE\\SOFTWARE\\Eclipse Foundation\\JDK::Path::Hotspot\\MSI",

            "HKEY_LOCAL_MACHINE\\SOFTWARE\\Eclipse Adoptium\\JRE::Path::Hotspot\\MSI",
            "HKEY_LOCAL_MACHINE\\SOFTWARE\\Eclipse Adoptium\\JDK::Path::Hotspot\\MSI",

            "HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\JDK::Path::Hotspot\\MSI",

            "HKEY_LOCAL_MACHINE\\SOFTWARE\\Azul Systems\\Zulu::InstallationPath",

            "HKEY_LOCAL_MACHINE\\SOFTWARE\\BellSoft\\Liberica::InstallationPath",
    };

    public static List<String> get() throws IOException, InterruptedException {
        final List<String> paths = new ArrayList<>();

        for (String reg : REGISTRY) {
            final String[] split = reg.split("::");
            final String registryPath = split[0], key = split[1],
                    addPath = split.length < 3 ? null : split[2];

            for (String keyPath : RegistryUtils.list(registryPath)) {
                final String finalKeyPath = addPath == null ? keyPath : keyPath + "\\" + addPath;
                final String path = RegistryUtils.readRegistry(finalKeyPath, key);

                paths.add(path);
            }
        }

        return paths;
    }

}
