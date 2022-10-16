<p align="center">
    <a href="https://github.com/SkinCanOrg/SkinCan"><img src="https://github.com/SkinCanOrg/.github/raw/main/profile/shoot.png" alt="shoot" width="640"/></a>
</p>

<h1 align="center">SkinCan</h1>

<h3 align="center">A <b>libre</b> skin cancer detection android app.</h3>

<p id="badges" align="center">
    <a href="https://github.com/SkinCanOrg/SkinCan/actions/workflows/build_push.yml"><img alt="CI" src="https://github.com/SkinCanOrg/SkinCan/actions/workflows/build_push.yml/badge.svg"></a>
    <a href="https://github.com/SkinCanOrg/SkinCan/releases"><img alt="GitHub Releases" src="https://img.shields.io/github/v/release/SkinCanOrg/SkinCan?include_prereleases"></a>
    <a href="https://www.figma.com/file/slWftpKH8cNZX7r5luLFLD/Desain"><img alt="Figma: Prototype" src="https://img.shields.io/badge/figma-Prototype-black?logo=figma&style=flat"></a>
    <a href="/LICENSE"><img alt="License: MPL-2.0" src="https://img.shields.io/badge/license-MPL--2.0-blue.svg"></a>
</p>

## About
**SkinCan** is a **libre** skin cancer detection android app, was created for **Bangkit Product-based Capstone Project**.

## Features
- Check your skin for signs of skin cancer anytime, anywhere.
- Diagnose the type of skin cancer easily.
- Receive a risk indication of your skin spot within 60 seconds.
- Learn about your skin and get advice based on your skin risk profile.

## Download
Download our app from our [releases page](https://github.com/SkinCanOrg/SkinCan/releases).

## TODO
- [ ] New logo (Current logo is a placeholder)
- [x] Machine Learning
   - [ ] Fix ML scan result (Sometime gives random result)
   - [x] Wait for ML model to download on scan
- [x] Polishing Auth
   - [x] Add alert for registration fail
   - [x] Add input validation
   - [x] Add loading into button
   - [ ] Link/Unlink account
   - [ ] Password changer (especially for account registered with Google)
- [x] News List
   - [ ] News Detail
- [x] Result List (using SQLDelight)
   - [x] Move photo automatically to data/Files/results
   - [ ] Delete data when photo no longer exists
   - [ ] Delete photo when data being deleted
   - [x] Reverse current list (scannedAt DESC)
- [ ] Landscape/Desktop support (Low priority)
- [ ] Migrate to Compose (Low priority)
- [ ] Migrate to DataStore (Low priority)
- [ ] Update checker (Low priority)
- [ ] Migrate out of Firebase (to Supabase maybe?)

## After Bangkit 2022
What are we (MD team) going to do with the app after graduating from Bangkit 2022?

- [@null2264](https://github.com/null2264): I'm planning to at some point in the future complete all the TODO list that we unfortunately don't have enough time to complete during Bangkit 2022, and continue maintaining the app as long as I could.
- [@FATx64](https://github.com/FATx64): I’m planning too make a new design to make another feature, such as Consultation doctor,Hospital search, and another feature that can be useful for user.

## Mobile Team
- Ahmad Ansori Palembani (a2191f1821 - [@null2264](https://github.com/null2264))
- Muhammad Fharid Akbar (a7191f1820 - [@FATx64](https://github.com/FATx64))

Check out our [organization profile page](https://github.com/SkinCanOrg) to see our full team list.

## License
```
Copyright (C) 2022 SkinCan Project

This Source Code Form is subject to the terms of the Mozilla Public
License, v. 2.0. If a copy of the MPL was not distributed with this
file, You can obtain one at http://mozilla.org/MPL/2.0/.
```
