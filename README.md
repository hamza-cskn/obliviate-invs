[![](https://jitpack.io/v/hamza-cskn/obliviate-invs.svg)](https://jitpack.io/#hamza-cskn/obliviate-invs) <-- current version

## Maven

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.hamza-cskn.obliviate-invs</groupId>
    <artifactId>core</artifactId>
    <version>{INSERT_VERSION_HERE}</version>
</dependency>
```
## Gradle
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.hamza-cskn.obliviate-invs:core:{INSERT_VERSION_HERE}'
    //implementation 'com.github.hamza-cskn.obliviate-invs:configurablegui:{INSERT_VERSION_HERE}'
}
```

# ObliviateInvs

ObliviateInvs is an inventory GUI library for Bukkit servers.

## Setup

Visit [wiki](https://github.com/Obliviated/obliviate-invs/wiki/) page to see usage guide.

## Features

- Create, listen, manage GUIs in only one class.
- Create advanced slots to make completely interactive slots. (see wiki)
- Add pagination support to your GUIs easily.
- Create automatic repeat task for GUI. The task will be stopped when GUI closed.
- Fully, modular design.
- Make configurable your GUIs easily.
- Automatically caches configuration items. Doesn't deserializes over and over.
- Don't work hard work smart. These methods will handle your boring works: fillColumn(), fillRow(), fillGui(), sendTitleUpdate(), sendSizeUpdate()
- Allows players clicking their own inventory during they using a GUI.
- Feel safe. obliviate-invs tested in live. It is stable.

## Other useful codes for GUI development

* Snake slot iteration algorithm for
  GUIs: [Click to go gist](https://gist.github.com/hamza-cskn/67c241c099d26e933a7662ba906322ce)
* ItemBuilder class that is used in this
  project: [Click to go gist](https://gist.github.com/hamza-cskn/af71812e9235025be348f2600502d6cd)
* Gradient and TextAnimation algorithms for
  GUIs: [Click to go gist](https://gist.github.com/hamza-cskn/c741466e33bb359210de3a24bb52c7c6)
