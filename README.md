# Revix

Revix is an extension library for RecyclerView's Adapter to create an adapter with just few lines of code.

## Features:

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

## Contribution

* If you **find a bug**, open an issue or submit a fix via a pull request.
* If you **have a feature request**, open an issue or submit an implementation via a pull request
* If you **want to contribute**, submit a pull request.

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
