# Universe Proxy - Android Application

A comprehensive Android application for obtaining, filtering, validating, and exporting proxy servers through web scraping and free APIs.

## 🚀 Features

### Core Functionality
- **Multi-Protocol Support**: HTTP, HTTPS, SOCKS4, and SOCKS5 proxies
- **Dual Data Sources**: Combines both API integration and web scraping
- **Advanced Filtering**: Filter by country (195+ countries) and anonymity level
- **Proxy Validation**: Real-time validation with connection testing
- **Export Options**: Export to file or copy to clipboard with flexible options
- **Modern UI**: Material Design 3 with Jetpack Compose

### Data Sources

#### APIs Integrated
1. **ProxyScrape API** - Free proxy API with protocol and country filters
2. **GeoNode API** - Comprehensive proxy list with detailed metadata
3. **PubProxy API** - Public proxy service with anonymity levels

#### Web Scrapers
1. **free-proxy-list.net** - HTTP/HTTPS proxies with country data
2. **sslproxies.org** - HTTPS-specific proxy list
3. **socks-proxy.net** - SOCKS4/SOCKS5 proxy collection
4. **hidemy.name** - Multi-protocol proxy listings
5. **proxynova.com** - International proxy database

### Filtering System

#### Country Filter
- Complete list of 195+ countries alphabetically sorted
- Search/filter by country name in real-time
- Multi-select with checkboxes
- "Select All" / "Clear All" options
- Persistent selection across sessions

#### Anonymity Filter
- **Elite**: Maximum anonymity (Level 1)
- **Anonymous**: Medium anonymity (Level 2)
- **Transparent**: No anonymity (Level 3)
- Multiple selection support

### Validation System
- Parallel validation (15 concurrent connections)
- 5-second timeout per proxy
- Real-time status updates
- Response time tracking
- Visual status indicators (✓ working, ✗ failed, ? not verified)

### Export & Copy Features

#### Export to File
- Save to `/storage/emulated/0/Download/`
- Format: `universe_proxy_YYYYMMDD_HHMMSS.txt`
- Includes header with date, protocol, and count
- Simple IP:PORT format per line

#### Copy to Clipboard
- Quick options: 10, 50, 100, All
- Custom amount input
- Copy selected proxies
- One proxy per line format

## 📱 Technical Specifications

### Requirements
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Language**: Kotlin
- **Build System**: Gradle with Kotlin DSL

### Architecture
- **Pattern**: MVVM (Model-View-ViewModel)
- **Repository Pattern**: Single source of truth for data
- **Reactive State**: StateFlow for UI state management
- **Coroutines**: Asynchronous operations and parallel processing

### Key Dependencies
```gradle
// Jetpack Compose
androidx.compose.ui:ui:1.6.0
androidx.compose.material3:material3:1.2.0
androidx.activity:activity-compose:1.8.2

// Networking
com.squareup.retrofit2:retrofit:2.9.0
com.squareup.okhttp3:okhttp:4.12.0

// Web Scraping
org.jsoup:jsoup:1.17.2

// Coroutines
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3

// ViewModel
androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0
```

## 🏗️ Project Structure

```
app/
├── src/main/
│   ├── java/com/spadilla89/proxyuniverse/
│   │   ├── MainActivity.kt                 # Main entry point with tab navigation
│   │   ├── data/
│   │   │   ├── model/                      # Data classes
│   │   │   │   ├── Proxy.kt
│   │   │   │   ├── ProxyProtocol.kt
│   │   │   │   ├── AnonymityLevel.kt
│   │   │   │   └── Country.kt
│   │   │   ├── api/                        # API interfaces and models
│   │   │   │   ├── ApiInterfaces.kt
│   │   │   │   ├── ApiModels.kt
│   │   │   │   └── ApiService.kt
│   │   │   ├── scraper/                    # Web scrapers
│   │   │   │   ├── BaseScraper.kt
│   │   │   │   ├── FreeProxyListScraper.kt
│   │   │   │   ├── SslProxiesScraper.kt
│   │   │   │   ├── SocksProxyScraper.kt
│   │   │   │   ├── HideMyNameScraper.kt
│   │   │   │   └── ProxyNovaScraper.kt
│   │   │   └── repository/
│   │   │       └── ProxyRepository.kt       # Data coordination
│   │   ├── viewmodel/
│   │   │   └── ProxyViewModel.kt            # UI state management
│   │   ├── ui/
│   │   │   ├── theme/                       # Material Design 3 theme
│   │   │   │   ├── Color.kt
│   │   │   │   ├── Theme.kt
│   │   │   │   └── Type.kt
│   │   │   ├── components/                  # Reusable UI components
│   │   │   │   ├── ProxyListItem.kt
│   │   │   │   ├── FilterSection.kt
│   │   │   │   ├── CountrySelector.kt
│   │   │   │   ├── AnonymityFilter.kt
│   │   │   │   └── ExportDialog.kt
│   │   │   └── screens/                     # Screen composables
│   │   │       ├── ProxyScreen.kt           # Shared screen logic
│   │   │       ├── HttpScreen.kt
│   │   │       ├── HttpsScreen.kt
│   │   │       ├── Socks4Screen.kt
│   │   │       └── Socks5Screen.kt
│   │   └── utils/                           # Utility classes
│   │       ├── ProxyValidator.kt            # Connection validation
│   │       ├── FileExporter.kt              # File export functionality
│   │       ├── ClipboardHelper.kt           # Clipboard operations
│   │       └── Constants.kt                 # App constants
│   ├── res/
│   │   ├── values/
│   │   │   ├── strings.xml
│   │   │   ├── colors.xml
│   │   │   └── themes.xml
│   │   └── xml/
│   │       └── network_security_config.xml  # HTTP cleartext support
│   └── AndroidManifest.xml                  # App configuration
└── build.gradle.kts                         # App-level dependencies
```

## 🛠️ Building the Application

### Prerequisites
1. **Android Studio**: Hedgehog (2023.1.1) or newer
2. **JDK**: Version 17 or higher
3. **Android SDK**: API 34 (Android 14)

### Build Instructions

#### Using Android Studio
1. Clone the repository:
   ```bash
   git clone https://github.com/spadilla89/proxy-universe.git
   cd proxy-universe
   ```

2. Open the project in Android Studio:
   - File → Open → Select project directory

3. Sync Gradle:
   - Android Studio will automatically prompt to sync
   - Or manually: File → Sync Project with Gradle Files

4. Build the APK:
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - APK location: `app/build/outputs/apk/debug/app-debug.apk`

#### Using Command Line
1. Make gradlew executable:
   ```bash
   chmod +x gradlew
   ```

2. Build debug APK:
   ```bash
   ./gradlew assembleDebug
   ```

3. Build release APK:
   ```bash
   ./gradlew assembleRelease
   ```

### Installing on Device
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 📖 Usage Guide

### Basic Workflow

1. **Select Protocol Tab**: Choose HTTP, HTTPS, SOCKS4, or SOCKS5

2. **Apply Filters** (Optional):
   - Tap "Filters" to expand filter options
   - Select countries from the list
   - Choose anonymity levels
   - Tap "Apply" to save filters

3. **Fetch Proxies**:
   - Tap the floating refresh button (bottom-right)
   - Wait for the app to fetch from all sources
   - Proxies will be displayed with status indicators

4. **Validate Proxies**:
   - Select specific proxies using checkboxes
   - Tap validate icon to check selected proxies
   - Or validate all proxies at once
   - Watch real-time validation progress

5. **Export or Copy**:
   - Select proxies to export (or export all)
   - Tap the export icon
   - Choose export to file or copy to clipboard
   - Select quantity (10, 50, 100, custom, or all)

### UI Elements

#### Proxy List Item
- **Checkbox**: Select for batch operations
- **IP:PORT**: Primary proxy identifier
- **Country**: Origin location
- **Anonymity**: Privacy level
- **Speed**: Response time in milliseconds
- **Status**: Color-coded indicator
  - 🟢 Green: Working
  - 🔴 Red: Not working
  - ⚫ Gray: Not verified
- **Copy Button**: Quick copy individual proxy

#### Bottom Actions
- **Validate Selected**: Check selected proxies
- **Validate All**: Check all proxies
- **Export/Copy**: Open export dialog

## 🔒 Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

## 🎨 Design Features

### Material Design 3
- **Dynamic Color**: Adapts to system theme
- **Dark Mode**: Automatic switching based on system settings
- **Modern Components**: Cards, FABs, Bottom App Bar
- **Smooth Animations**: Transitions and loading states

### Responsive Design
- Tablet-friendly layouts
- Landscape mode support
- Adaptive spacing and sizing

## 📝 Data Format

### Exported File Format
```
# Universe Proxy Export
# Date: 2026-01-22 15:30:00
# Protocol: HTTP
# Total: 150 proxies

192.168.1.1:8080
203.45.67.89:3128
104.25.63.12:80
```

### Clipboard Format
```
192.168.1.1:8080
203.45.67.89:3128
104.25.63.12:80
```

## 🔧 Configuration

### Network Security
The app includes `network_security_config.xml` to allow HTTP cleartext traffic (required for some scrapers):

```xml
<network-security-config>
    <base-config cleartextTrafficPermitted="true">
        <trust-anchors>
            <certificates src="system" />
            <certificates src="user" />
        </trust-anchors>
    </base-config>
</network-security-config>
```

### Validation Settings
Configurable in `Constants.kt`:
- `VALIDATION_TIMEOUT_MS`: 5000ms per proxy
- `VALIDATION_POOL_SIZE`: 15 concurrent connections
- `SCRAPER_TIMEOUT_MS`: 10000ms per website
- `API_TIMEOUT_MS`: 15000ms per API call

## 🚨 Error Handling

- **Network Errors**: Graceful fallback, continues with other sources
- **Scraper Failures**: Individual scraper errors don't stop others
- **API Failures**: Continues with remaining APIs
- **Validation Errors**: Marks proxy as failed, continues with others
- **Export Errors**: User-friendly error messages with toasts

## 📊 Performance

### Optimizations
- Parallel API calls and scraping
- Chunked validation (15 proxies at a time)
- Efficient deduplication (based on IP:PORT)
- Lazy loading for large lists
- StateFlow for reactive updates

### Memory Management
- Pagination for lists >500 proxies
- Efficient data structures
- Proper lifecycle handling

## 🧪 Testing

### Manual Testing Checklist
- [ ] All protocol tabs switch correctly
- [ ] Filters apply and persist
- [ ] Country selector search works
- [ ] Proxy fetching completes successfully
- [ ] Validation updates status correctly
- [ ] Export to file creates file in Downloads
- [ ] Copy to clipboard works
- [ ] All UI elements responsive
- [ ] Dark mode switches properly
- [ ] App handles network errors gracefully

## 🤝 Contributing

This is a complete, production-ready Android application. All core features are implemented:
- ✅ Complete MVVM architecture
- ✅ 195+ countries with search
- ✅ Multiple API integrations
- ✅ Web scraping (5 sources)
- ✅ Parallel validation
- ✅ Export and clipboard features
- ✅ Material Design 3 UI
- ✅ Dark mode support

## 📄 License

This project is created for educational and personal use.

## 👤 Author

Created by spadilla89

## 🙏 Acknowledgments

- Free proxy APIs: ProxyScrape, GeoNode, PubProxy
- Web sources: free-proxy-list.net, sslproxies.org, socks-proxy.net, hidemy.name, proxynova.com
- Libraries: Jetpack Compose, Retrofit, OkHttp, Jsoup

---

**Note**: This application fetches proxies from free public sources. The quality, speed, and availability of proxies may vary. Always validate proxies before use and respect the terms of service of the data sources.