# 📱 Volume Control App v2.0

## नवीन Features:
✅ **Power Section** - Airplane Mode, Reboot, Shutdown buttons
✅ **Ringer Mode** - Ring 🔔 / Vibrate 📳 / Silent 🔕 / DND 🌙
✅ **Volume Sliders** - Ringer, Notification, Media, Alarm, In-Call, System
✅ **Navigation Bar Shortcut** - Accessibility button
✅ **Quick Settings Tile** - Notification panel मधून access

---

## 📲 Install & Setup Steps:

### Step 1: Android Studio मध्ये Build करा
1. Android Studio open करा
2. "Open Project" → `VolumeControl` folder select करा
3. Build → Build APK(s)
4. APK: `app/build/outputs/apk/debug/app-debug.apk`

### Step 2: APK Install करा
```
adb install app-debug.apk
```
किंवा APK file directly phone वर copy करून install करा.

---

## ⚙️ Navigation Bar Shortcut Setup:

### Android 9+ (Pie आणि वर):
1. **Settings** → **Accessibility** → **Volume Control Nav Button** → **Enable**
2. एकदा enable केल्यावर navigation bar मध्ये ⚙️ button दिसेल
3. त्या button ला press केल्यावर Volume Control dialog उघडेल

---

## 🔲 Quick Settings Tile Setup:
1. Notification panel खाली pull करा
2. Tiles edit करण्यासाठी pencil icon दाबा
3. "Volume Control" tile drag करून add करा
4. Tile press केल्यावर app उघडेल

---

## 🔋 Power Buttons:
- **Airplane** → Phone चे Airplane Mode settings उघडते
- **Reboot** → Device restart (Root किंवा system app आवश्यक)
- **Shutdown** → Device बंद (Root आवश्यक)

> **Note:** Reboot/Shutdown साठी root access किंवा system app म्हणून sign करणे आवश्यक आहे.

---

## 📁 Project Structure:
```
VolumeControl/
├── app/src/main/
│   ├── java/com/volumecontrol/
│   │   ├── MainActivity.java          ← Main UI
│   │   ├── VolumeTileService.java     ← Quick Settings Tile
│   │   └── NavBarAccessibilityService.java ← Nav Button
│   ├── res/
│   │   ├── layout/dialog_volume_control.xml
│   │   ├── drawable/ (backgrounds)
│   │   ├── values/ (strings, styles, colors)
│   │   └── xml/accessibility_service_config.xml
│   └── AndroidManifest.xml
└── app/build.gradle
```
