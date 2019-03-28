# Revix

[![License](https://img.shields.io/badge/License-Apache%202.0-2196F3.svg?style=for-the-badge)](https://opensource.org/licenses/Apache-2.0)
[![language](https://img.shields.io/github/languages/top/BirjuVachhani/location-extension-android.svg?style=for-the-badge&colorB=f18e33)](https://kotlinlang.org/)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg?style=for-the-badge)](https://www.android.com/)
[![API](https://img.shields.io/badge/API-16%2B-F44336.svg?style=for-the-badge)](https://android-arsenal.com/api?level=16)

Revix is an extension library for RecyclerView's Adapter to create an adapter with just few lines of code.

Please note that this library is still under development. Stable version of the library will be released soon. Stay tuned!

| Current Version 	| 2.0.0-alpha03 	|
|-----------------	|---------------	|
| Platform        	| Android       	|
| Language        	| Kotlin        	|
| SDK Level       	| 16+           	|
| License         	| Apache 2.0    	|

## Features:

* No Boilerplate!
* Supports Kotlin DSL
* Supports filter
* supports empty view, loading view and error view
* Supports Data Binding

## Type of Adapters:

* Basic Adapter: Single View Type, No Data Binding
* Basic Binding Adapter: Single View Type, Supports DataBinding
* Smart Adapter: Multiple View Types, No Data Binding
* Smart Binding Adapter: Multiple View Types, Supports Data Binding

All of these adapter supports EmptyView, ErrorView and LoadingView types by default. see sample app source for usage guide.

## Gradle Dependency
Add the JitPack repository to your project's build.gradle file

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency in your app's build.gradle file

```groovy
dependencies {
    implementation 'com.github.BirjuVachhani:revix-android:2.0.0-alpha03'
}
```

### Pull Request
To generate a pull request, please consider following [Pull Request Template](https://github.com/iChintanSoni/RepositoryPattern/blob/master/PULL_REQUEST_TEMPLATE.md).

### Issues
To submit an issue, please check the [Issue Template](https://github.com/iChintanSoni/RepositoryPattern/blob/master/ISSUE_TEMPLATE.md).

Code of Conduct
---
[Code of Conduct](https://github.com/iChintanSoni/RepositoryPattern/blob/master/CODE_OF_CONDUCT.md)

## Contribution

You are most welcome to contribute to this project!

Please have a look at [Contributing Guidelines](https://github.com/BirjuVachhani/revix-android/blob/master/CONTRIBUTING.md), before contributing and proposing a change.

# License

```
   Copyright © 2019 BirjuVachhani

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
