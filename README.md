<p align="center">
    <a href="https://github.com/SkinCanOrg/SkinCan"><img src="https://github.com/SkinCanOrg/.github/raw/main/profile/shoot.png" alt="shoot" width="640"/></a>
</p>

<h1 align="center">SkinCan</h1>

<h3 align="center">A <b>libre</b> skin cancer detection android app.</h3>

<p id="badges" align="center">
    <a href="/LICENSE"><img alt="License: MPL-2.0" src="https://img.shields.io/badge/license-MPL--2.0-blue.svg"></a>
    <a href="https://github.com/SkinCanOrg/SkinCan/actions/workflows/build_push.yml"><img alt="CI" src="https://github.com/SkinCanOrg/SkinCan/actions/workflows/build_push.yml/badge.svg"></a>
</p>

## About
**SkinCan** is a **libre** skin cancer detection android app, created for Bangkit Product-based Capstone Project.

### MVP (Minimum Viable Product)
- [x] Onboarding
- [x] Auth
- [x] Camera
- [x] Scan Result
- [x] Result List
- [x] News List (Placeholder)

### TODO
- [ ] Fix ML
   - [ ] Wait for ML model to download on scan
- [x] Polishing Auth
   - [x] Add alert for registration fail
   - [x] Add input validation
   - [x] Add loading into button
- [x] Result List (using SQLDelight)
   <!-- Reference: https://github.com/tachiyomiorg/tachiyomi/blob/master/app/src/main/java/eu/kanade/tachiyomi/data/cache/CoverCache.kt -->
   - [x] Move photo automatically to data/Files/results
   - [ ] Delete data when photo no longer exists
   - [ ] Delete photo when data being deleted
   - [ ] Reverse current list (scannedAt DESC)
- [ ] Landscape/Desktop support (Low priority)

### Mobile Team
- Ahmad Ansori Palembani (a2191f1821 - [@null2264](https://github.com/null2264))
- Muhammad Fharid Akbar (a7191f1820 - [@FATx64](https://github.com/FATx64))

## License
```
Copyright (C) 2022 SkinCan Project

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
```
