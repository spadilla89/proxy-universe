# Build Instructions for Universe Proxy Android App

## Prerequisites

### Required Software

1. **Android Studio**
   - Version: Hedgehog (2023.1.1) or newer recommended
   - Download: https://developer.android.com/studio
   - Install with default components

2. **Java Development Kit (JDK)**
   - Version: JDK 17 (required for Android Gradle Plugin 8.2.0)
   - Can be installed via Android Studio or separately

3. **Android SDK Components** (installed via Android Studio SDK Manager)
   - Android SDK Platform 34 (Android 14)
   - Android SDK Build-Tools 34.0.0
   - Android SDK Command-line Tools
   - Android Emulator (optional, for testing)

### System Requirements

- **Operating System**: Windows 10/11, macOS 10.14+, or Linux
- **RAM**: Minimum 8GB (16GB recommended)
- **Disk Space**: 10GB free space minimum
- **Internet Connection**: Required for downloading dependencies

## Step-by-Step Build Process

### Method 1: Using Android Studio (Recommended)

#### 1. Install Android Studio

1. Download Android Studio from https://developer.android.com/studio
2. Run the installer and follow the setup wizard
3. During setup, ensure these components are selected:
   - Android SDK
   - Android SDK Platform
   - Android Virtual Device (for emulator)

#### 2. Configure Android SDK

1. Open Android Studio
2. Go to: **File → Settings** (Windows/Linux) or **Android Studio → Preferences** (macOS)
3. Navigate to: **Appearance & Behavior → System Settings → Android SDK**
4. In the **SDK Platforms** tab:
   - Check **Android 14.0 (API 34)**
   - Click **Apply** to download
5. In the **SDK Tools** tab, ensure these are installed:
   - Android SDK Build-Tools 34.0.0
   - Android SDK Command-line Tools
   - Android Emulator
   - Android SDK Platform-Tools

#### 3. Clone the Repository

```bash
# Using HTTPS
git clone https://github.com/spadilla89/proxy-universe.git

# Or using SSH
git clone git@github.com:spadilla89/proxy-universe.git

# Navigate to project directory
cd proxy-universe
```

#### 4. Open Project in Android Studio

1. Launch Android Studio
2. Click **File → Open**
3. Navigate to the cloned `proxy-universe` directory
4. Click **OK**

#### 5. Sync Project with Gradle

1. Android Studio will show a banner: **"Gradle files have changed"**
2. Click **Sync Now**
3. Wait for Gradle sync to complete (may take several minutes on first run)
4. Check the **Build** tab at the bottom for any errors

#### 6. Resolve Any Sync Issues

If you encounter errors:

**Issue: "SDK location not found"**
```
Solution: Create local.properties file in project root:
sdk.dir=/path/to/your/Android/SDK

Example paths:
- Windows: C:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
- macOS: /Users/YourName/Library/Android/sdk
- Linux: /home/YourName/Android/Sdk
```

**Issue: "Failed to download dependencies"**
```
Solution: Check internet connection and try again
Or go to File → Invalidate Caches and Restart
```

#### 7. Build Debug APK

**Option A: Using Menu**
1. Click **Build → Build Bundle(s) / APK(s) → Build APK(s)**
2. Wait for build to complete (progress shown in Build tab)
3. When complete, a notification will show: **"APK(s) generated successfully"**
4. Click **locate** in the notification to find the APK

**Option B: Using Gradle Panel**
1. Open the Gradle panel: **View → Tool Windows → Gradle**
2. Expand: **proxy-universe → app → Tasks → build**
3. Double-click **assembleDebug**
4. Monitor progress in the Build tab

#### 8. Locate the Built APK

The debug APK will be at:
```
app/build/outputs/apk/debug/app-debug.apk
```

#### 9. Install APK on Device/Emulator

**For Physical Device:**
1. Enable Developer Options on your Android device:
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
   - Go back to Settings → Developer Options
   - Enable "USB Debugging"
2. Connect device via USB
3. In Android Studio: **Run → Run 'app'**
4. Select your device from the list

**For Emulator:**
1. Create AVD: **Tools → Device Manager → Create Device**
2. Select device definition (e.g., Pixel 6)
3. Select system image (API 34)
4. Finish and start emulator
5. In Android Studio: **Run → Run 'app'**

**Manual Installation via ADB:**
```bash
# Connect device and enable USB debugging
adb devices

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Or if multiple devices connected
adb -s DEVICE_SERIAL install app/build/outputs/apk/debug/app-debug.apk
```

---

### Method 2: Using Command Line (Advanced)

#### 1. Prerequisites

Ensure you have:
- JDK 17 installed and in PATH
- Android SDK installed
- ANDROID_HOME environment variable set

#### 2. Set Environment Variables

**Windows (Command Prompt):**
```cmd
set ANDROID_HOME=C:\Users\YourName\AppData\Local\Android\Sdk
set PATH=%PATH%;%ANDROID_HOME%\platform-tools;%ANDROID_HOME%\cmdline-tools\latest\bin
```

**Windows (PowerShell):**
```powershell
$env:ANDROID_HOME = "C:\Users\YourName\AppData\Local\Android\Sdk"
$env:PATH += ";$env:ANDROID_HOME\platform-tools;$env:ANDROID_HOME\cmdline-tools\latest\bin"
```

**macOS/Linux:**
```bash
export ANDROID_HOME=$HOME/Library/Android/sdk  # macOS
# or
export ANDROID_HOME=$HOME/Android/Sdk  # Linux

export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin
```

#### 3. Clone and Navigate

```bash
git clone https://github.com/spadilla89/proxy-universe.git
cd proxy-universe
```

#### 4. Make Gradle Wrapper Executable (macOS/Linux only)

```bash
chmod +x gradlew
```

#### 5. Build Commands

**Build Debug APK:**
```bash
# Windows
gradlew.bat assembleDebug

# macOS/Linux
./gradlew assembleDebug
```

**Build Release APK (unsigned):**
```bash
# Windows
gradlew.bat assembleRelease

# macOS/Linux
./gradlew assembleRelease
```

**Clean Build:**
```bash
# Windows
gradlew.bat clean assembleDebug

# macOS/Linux
./gradlew clean assembleDebug
```

**List All Tasks:**
```bash
# Windows
gradlew.bat tasks

# macOS/Linux
./gradlew tasks
```

#### 6. Install via Command Line

```bash
# List connected devices
adb devices

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Uninstall first if already installed
adb uninstall com.spadilla89.proxyuniverse

# Then install
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## Building Release APK (Signed)

For production releases, you need to sign the APK.

### 1. Generate Signing Key

```bash
keytool -genkey -v -keystore universe-proxy.keystore -alias universe_key -keyalg RSA -keysize 2048 -validity 10000
```

Follow the prompts to set:
- Keystore password
- Key alias password
- Your name, organization, etc.

### 2. Create keystore.properties

Create `keystore.properties` in project root (don't commit this file):

```properties
storePassword=YourStorePassword
keyPassword=YourKeyPassword
keyAlias=universe_key
storeFile=/path/to/universe-proxy.keystore
```

### 3. Update app/build.gradle.kts

Add signing config (already configured in the project):

```kotlin
android {
    signingConfigs {
        create("release") {
            val keystorePropertiesFile = rootProject.file("keystore.properties")
            if (keystorePropertiesFile.exists()) {
                val keystoreProperties = Properties()
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))
                
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}
```

### 4. Build Signed Release APK

```bash
# Windows
gradlew.bat assembleRelease

# macOS/Linux
./gradlew assembleRelease
```

Signed APK location:
```
app/build/outputs/apk/release/app-release.apk
```

---

## Troubleshooting

### Common Build Errors

#### 1. "Unsupported class file major version"
**Cause:** Wrong JDK version
**Solution:** Use JDK 17
```bash
# Check Java version
java -version

# Should show: openjdk version "17.x.x"
```

#### 2. "SDK location not found"
**Cause:** ANDROID_HOME not set or local.properties missing
**Solution:** Create local.properties with SDK path

#### 3. "Could not resolve dependencies"
**Cause:** Network issues or repository problems
**Solution:**
- Check internet connection
- Sync project again
- Clear Gradle cache: `./gradlew clean --refresh-dependencies`

#### 4. "Execution failed for task ':app:compileDebugKotlin'"
**Cause:** Syntax errors in Kotlin code
**Solution:** Check error messages for specific file and line

#### 5. "Insufficient memory for the Java Runtime Environment"
**Cause:** Not enough memory allocated to Gradle
**Solution:** Edit gradle.properties:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=1024m -XX:+HeapDumpOnOutOfMemoryError
```

### Clearing Gradle Cache

If build issues persist:

```bash
# Stop Gradle daemon
./gradlew --stop

# Clear Gradle cache
rm -rf ~/.gradle/caches/  # macOS/Linux
# or
rmdir /s %USERPROFILE%\.gradle\caches  # Windows

# Rebuild project
./gradlew clean build
```

### Android Studio Cache Issues

In Android Studio:
1. **File → Invalidate Caches → Invalidate and Restart**
2. Wait for IDE to restart
3. Sync project again

---

## Verification

### After Successful Build

1. **Check APK Exists:**
   ```bash
   ls -lh app/build/outputs/apk/debug/app-debug.apk
   ```

2. **Check APK Size:**
   - Should be approximately 15-25 MB

3. **Install and Test:**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   adb shell am start -n com.spadilla89.proxyuniverse/.MainActivity
   ```

4. **Check Logs:**
   ```bash
   adb logcat | grep "UniverseProxy"
   ```

---

## Building for Different Architectures

To build for specific architectures:

**Build for ARM64 only:**
```gradle
android {
    defaultConfig {
        ndk {
            abiFilters += listOf("arm64-v8a")
        }
    }
}
```

**Build for all architectures:**
```gradle
android {
    defaultConfig {
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }
}
```

---

## Additional Resources

- **Android Developer Documentation:** https://developer.android.com/
- **Gradle Documentation:** https://docs.gradle.org/
- **Jetpack Compose:** https://developer.android.com/jetpack/compose
- **Kotlin Documentation:** https://kotlinlang.org/docs/home.html

---

## Support

For issues or questions:
1. Check this documentation first
2. Review Android Studio error messages
3. Check GitHub Issues: https://github.com/spadilla89/proxy-universe/issues
4. Ensure all prerequisites are properly installed

---

**Last Updated:** January 2026
