# Pexels Photo Search - Kotlin Multiplatform App

A modern Kotlin Multiplatform application that allows users to search for high-quality photos from the Pexels API and download them. The app features an adaptive layout that works seamlessly across Android, iOS, and Desktop (JVM) platforms.

## 🌟 Features

- **Photo Search**: Search millions of high-quality photos from Pexels API
- **Photo Preview**: View photo details with large previews
- **Download Photos**: Download selected photos to your device
- **Adaptive Layout**: Responsive UI that adapts to different screen sizes and orientations
  - Mobile: Bottom navigation bar with single-pane layout
  - Tablet: Navigation rail with dual-pane layout for better multitasking
  - Desktop: Wide navigation rail with optimal space utilization
- **Theme Support**: Switch between Light, Dark, and System Default themes
- **Cross-Platform**: Runs on Android, iOS, and Desktop (JVM)

## 🏗️ Architecture

### Project Structure

* **[/composeApp](./composeApp/src)** - Shared code across all platforms
  - **[commonMain](./composeApp/src/commonMain/kotlin)** - Common Kotlin code for all targets
    - `presentation/` - UI components and ViewModels
    - `domain/` - Business logic and use cases
    - `data/` - Data layer (API client, DTOs)
    - `util/` - Utility functions and helpers
  - **[androidMain](./composeApp/src/androidMain/kotlin)** - Android-specific implementations
  - **[iosMain](./composeApp/src/iosMain/kotlin)** - iOS-specific implementations
  - **[jvmMain](./composeApp/src/jvmMain/kotlin)** - Desktop (JVM) specific implementations

* **[/iosApp](./iosApp)** - iOS application entry point and SwiftUI integration

### Tech Stack

- **UI Framework**: Compose Multiplatform
- **Navigation**: Jetpack Navigation Compose
- **Networking**: Ktor Client
- **Dependency Injection**: Koin
- **Image Loading**: Coil3
- **Data Persistence**: DataStore (Preferences)
- **Serialization**: Kotlinx Serialization
- **Theming**: Material 3 with Material Kolor for dynamic colors

## 🚀 Getting Started

### Prerequisites

- **JDK 17** or higher
- **Android Studio** (for Android development)
- **Xcode** (for iOS development, macOS only)
- **Pexels API Key** - Get one for free at [Pexels API](https://www.pexels.com/api/)

### API Key Configuration

The application requires a Pexels API key to function. You need to add your API key to the `local.properties` file to keep it secure and prevent it from being committed to version control.

1. Create or edit `local.properties` in the root directory of the project:
   ```properties
   API_KEY=YOUR_PEXELS_API_KEY_HERE
   ```

2. The `local.properties` file is already included in `.gitignore`, so your API key will not be committed to the repository.

3. For CI/CD or production builds, you can also provide the API key via:
   - Environment variable: `API_KEY`
   - Gradle property: `-PapiKey=YOUR_API_KEY`

### Build and Run

#### Android Application

To build and run the Android app:

```shell
# Windows
.\gradlew.bat :composeApp:assembleDebug

# macOS/Linux
./gradlew :composeApp:assembleDebug
```

Or use the Android run configuration in Android Studio.

#### Desktop (JVM) Application

To run the desktop app:

```shell
# Windows
.\gradlew.bat :composeApp:run

# macOS/Linux
./gradlew :composeApp:run
```

Or use the Desktop run configuration in your IDE.

#### iOS Application

To build and run the iOS app:

1. Open the `/iosApp` directory in Xcode
2. Select your target device or simulator
3. Run the project

Or use the iOS run configuration in your IDE.

## 📱 How to Use

1. **Home Screen**: View app information and battery level
2. **Search Screen**: 
   - Enter a search query (e.g., "nature", "technology", "sunset")
   - Click "Search" to fetch photos from Pexels
   - Browse results in a responsive grid layout
   - Click on any photo to view details
3. **Photo Details**: 
   - View full-size preview
   - Click "Download" to save the photo to your device
   - Click "Close" to return to search results
4. **Theme Toggle**: Click the theme icon in the top app bar to cycle through Light, Dark, and System Default themes

## 🔧 Configuration

### Gradle Tasks

- `generateApiKey` - Generates the ApiKeys.kt file with your API key (runs automatically during build)
- `:composeApp:assembleDebug` - Build debug APK for Android
- `:composeApp:assembleRelease` - Build release APK for Android
- `:composeApp:run` - Run desktop application

### Platform-Specific Features

- **Android**: Uses Ketch library for efficient file downloads with progress tracking
- **iOS**: Native iOS file system integration
- **Desktop**: AsyncHttpClient for fast file downloads

## 📦 Dependencies

Key dependencies include:

- Compose Multiplatform 1.9.1
- Kotlin 2.2.21
- Ktor 3.3.1
- Koin 4.1.1
- Coil3 3.3.0
- Material 3 (Expressive)
- DataStore 1.1.7

See [libs.versions.toml](./gradle/libs.versions.toml) for complete dependency list.

## 🔒 Security

- API keys are stored in `local.properties` which is excluded from version control
- The build system generates API key files at compile time
- Never commit `local.properties` or hardcode API keys in source files

## Screenshots

<img width="1920" height="1080" alt="Screenshot (115)" src="https://github.com/user-attachments/assets/318189bd-8634-4dfd-810a-b4bf06cd8487" />
<img width="1920" height="1080" alt="Screenshot (116)" src="https://github.com/user-attachments/assets/c1cf40bb-4902-46d6-8816-0e0ecc8f7dad" />
<img width="1920" height="1080" alt="Screenshot (117)" src="https://github.com/user-attachments/assets/01cc9b44-bd9f-4d87-8878-fff5e6139903" />
<img width="1920" height="1080" alt="Screenshot (119)" src="https://github.com/user-attachments/assets/05dd75de-e66b-4824-a561-33093f99b240" />
<img width="1920" height="1080" alt="Screenshot (120)" src="https://github.com/user-attachments/assets/54fa5d04-15fd-4324-b7b7-d024966ed7cf" />
<img width="1920" height="1080" alt="Screenshot (121)" src="https://github.com/user-attachments/assets/f2c6b39d-bcd1-4f18-97d5-ac18315fe760" />
<img width="1920" height="1080" alt="Screenshot (123)" src="https://github.com/user-attachments/assets/5cef29d4-79bb-4535-9da2-9fc17d3aa01f" />
<img width="1920" height="1080" alt="Screenshot (126)" src="https://github.com/user-attachments/assets/d55fa756-21e8-4336-9adc-b52b507235ae" />
<img width="1920" height="1080" alt="Screenshot (127)" src="https://github.com/user-attachments/assets/55a6938e-ac75-489f-88fa-4875b3bb2407" />
<img width="1920" height="1080" alt="Screenshot (128)" src="https://github.com/user-attachments/assets/c2810ed8-fe5a-4092-bc75-b420b728a1fd" />
<img width="1920" height="1080" alt="Screenshot (129)" src="https://github.com/user-attachments/assets/bf911a26-9532-45a8-99d8-d9c5178606a7" />

## 📄 License

This project is a demonstration application. Please refer to Pexels API terms of service when using the Pexels API.

## 🔗 Resources

- [Kotlin Multiplatform Documentation](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Pexels API Documentation](https://www.pexels.com/api/documentation/)
- [Material 3 Design](https://m3.material.io/)

---

Built with ❤️ using Kotlin Multiplatform and Compose Multiplatform
