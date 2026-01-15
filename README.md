# SuperBrowser (Multi-Tab Android Browser)

## Project Overview
**SuperBrowser** is a multi-tab web browser for Android that utilizes a modern fragment-based architecture. It supports concurrent browsing sessions through a tabbed interface, providing independent navigation histories and persistent UI states for each tab. The application is built using **Kotlin** and adheres to the **MVVM (Model-View-ViewModel)** design pattern.

## Key Features
* **Multi-Tab Interface:** Employs `ViewPager2` and `FragmentStateAdapter` to manage a dynamic list of browsing tabs with fluid swipe navigation.
* **Independent Tab State:** Utilizes parent-scoped `ViewModels` to ensure each tab maintains its own unique URL, navigation history, and `WebView` state.
* **Dynamic Tab Creation:** Integrated "New Tab" functionality within the control bar to instantly instantiate and navigate to new browsing sessions.
* **Navigation Controls:** Full support for Back, Forward, and "Go" actions, along with an intelligent URL "fixer" that handles malformed addresses and search queries.
* **State Persistence:** Automatically restores open tabs and individual `WebView` histories following configuration changes or activity restarts.

## Tech Stack
* **Language:** Kotlin
* **UI Components:** Fragments, `ViewPager2`, `FragmentContainerView`, `WebView`
* **Jetpack Libraries:** `ViewModel`, `LiveData`, `FragmentStateAdapter`
* **Architecture:** MVVM with Parent-Child Fragment Scoping

## Architecture & Components



### 1. Tab Management (`MainActivity`)
The `MainActivity` acts as the primary host, containing a `ViewPager2` that fills the layout. It coordinates with a `MainActivityViewModel` to track the total number of active tabs and the currently selected index.

### 2. The Tab Container (`TabFragment`)
Each "page" in the `ViewPager2` is a `TabFragment`. This fragment serves as the parent container for:
* **ControlFragment:** Manages the URL entry bar and navigation buttons (Back, Next, New Tab).
* **PageFragment:** Hosts the `WebView` and handles the actual rendering of web content.

### 3. Data Synchronization (`PageDataViewModel`)
To prevent data "leaking" between tabs, a `PageDataViewModel` is scoped specifically to each `TabFragment`. This allows child fragments (`Control` and `Page`) to share data locally without affecting other open tabs.



## Installation

Follow these steps to set up the project in Android Studio:

1. **Clone the Repository**
   ```bash
   git clone https://github.com/kevnugget/BrowserApp.git
   ```

2. **Open the Project**
   * Launch **Android Studio**.
   * Select **File > Open** and navigate to the cloned directory.

3. **Sync Gradle**
   * Android Studio will automatically prompt you to sync Gradle files. Ensure you have an active internet connection.

## Execution

1. **Select a Device**
   * Choose an Android Emulator (API 30+) or connect a physical Android device via USB.

2. **Build and Run**
   * Click the **Run** icon (Green triangle) in the top toolbar.
   * The app will launch with a single active tab.

3. **App Usage**
   * **Navigate:** Enter a URL or search term in the address bar and tap the "Search" icon.
   * **Back and Forward Buttons:"** Tap the "Left" arrow to return to the previous page and the "Right" arrow to go to the next page in the current tab.
   * **New Tab:** Tap the "+" icon in the control bar to create a new session.
   * **Switch Tabs:** Swipe horizontally to move between open tabs.

---
