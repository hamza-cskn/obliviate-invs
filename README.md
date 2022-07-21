[![](https://jitpack.io/v/Obliviated/obliviate-invs.svg)](https://jitpack.io/#Obliviated/obliviate-invs)

### Maven

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.Obliviated.ObliviateInvs</groupId>
    <artifactId>core</artifactId>
    <version>4.0.1</version>
</dependency>
```
### Gradle
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Obliviated.ObliviateInvs:core:4.0.1'
}
```

# ObliviateInvs

ObliviateInvs is an inventory GUI library for Bukkit servers.

## Setup

Visit [wiki](https://github.com/Obliviated/obliviate-invs/wiki/) page to see usage guide.

## Features

- Create, listen, manage GUIs in only one class.
- Create advanced slots to make completely interactive slots. (
  see [wiki](https://github.com/Obliviated/obliviate-invs/wiki/Advanced-Slots))
- Add pagination support to your GUIs easily.
- Create automatic update task for GUI
- Don't work hard work smart. These methods will handle your boring works: fillColumn(), fillRow(), fillGui(),
  sendTitleUpdate(), sendSizeUpdate()
- Feel safe. obliviate-invs tested in live. It is stable.

## Other useful codes for GUI development

* Snake slot iteration algorithm for
  GUIs: [Click to go gist](https://gist.github.com/Obliviated/67c241c099d26e933a7662ba906322ce)
* ItemBuilder class that is used in this
  project: [Click to go gist](https://gist.github.com/Obliviated/af71812e9235025be348f2600502d6cd)
* Gradient and TextAnimation algorithms for
  GUIs: [Click to go gist](https://gist.github.com/Obliviated/c741466e33bb359210de3a24bb52c7c6)
