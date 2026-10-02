# Naveedify 🎵

An open-source, privacy-focused Spotify-style music streaming and library player crafted with **Jetpack Compose**, **Material 3**, and **Room Local Persistence**.

## Features

- 🎧 **Persistent Playback Bar**: Real-time progress indicator, play/pause, skip, like, and gesture support directly in the bottom navigation.
- 📱 **Adaptive UI**: True dark theme with Spotify’s signature green accents and responsive layouts.
- 🎛️ **Audio Engine**: Equalizer presets, streaming quality controls, sleep timer, and dynamic soundwave visualizer.
- 📂 **Local Persistence**: Powered by Room Database for custom playlists, favorites, and listening history with zero telemetry.
- 📄 **100% FOSS**: Licensed under Apache 2.0 without proprietary trackers or proprietary vendor binaries.

---

## Publishing to F-Droid

F-Droid is a community-maintained software repository for Android. Applications submitted to F-Droid are built from source by F-Droid's automated build system.

### Step 1: Push your Code to GitHub / GitLab
1. In AI Studio, open the top-right settings menu and choose **Export to GitHub** (or download the ZIP and push to your GitHub account `https://github.com/naveedalicodes1/naveedify`).
2. Create a git release tag:
   ```bash
   git tag -a v1.0 -m "Release v1.0"
   git push origin v1.0
   ```

### Step 2: Submit to Official F-Droid Repository
1. Fork the official F-Droid metadata repository on GitLab:  
   👉 [gitlab.com/fdroid/fdroiddata](https://gitlab.com/fdroid/fdroiddata)
2. Create a new file in your fork under `metadata/com.aistudio.naveedify.nvdkmp.yml` using the recipe provided in `/fdroid/com.aistudio.naveedify.nvdkmp.yml`.
3. Open a **Merge Request (MR)** titled:  
   `New app: Naveedify (com.aistudio.naveedify.nvdkmp)`.
4. F-Droid's CI bot will test the build recipe and reviewers will merge it into the index!

### Step 3: (Optional Fast-Track) IzzyOnDroid F-Droid Repo
While official F-Droid review takes a couple of weeks, you can publish on **IzzyOnDroid** (which is directly accessible in F-Droid client app) within 24–48 hours:
1. Go to [gitlab.com/IzzyOnDroid/repo/-/issues](https://gitlab.com/IzzyOnDroid/repo/-/issues)
2. Click **New Issue** -> select **Inclusion Request**.
3. Provide your GitHub release URL with the built APK.

---

## License

Copyright (c) 2026 Naveed Ali.  
Licensed under the [Apache License, Version 2.0](LICENSE).
