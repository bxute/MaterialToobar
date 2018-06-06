## Material 2.0 Styled Toolbar [![](https://jitpack.io/v/bxute/MaterialToobar.svg)](https://jitpack.io/#bxute/MaterialToobar)
---
This is a small library for Toolbar. Recently Google announced Material Design 2.0.
There lots of design improvements were discussed. Among them, Toolbar is one of the components which received newer design guidelines.

A quick view of this library
<img src="https://user-images.githubusercontent.com/10809719/41021787-4bcac528-6985-11e8-9619-54f1da4ba052.gif" width="360px" height="640px">

### How to use this library


1.Add Module dependency in `app/build.gradle` file
```
	dependencies {
	        implementation 'com.github.bxute:MaterialToobar:v1.0'
	}

```

2.Add this XML to your activity
```xml
 <xute.materialtoolbar.BottomToolbar
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

And you are good to go!

### How to receive callbacks from Buttons of Toolbar

`BottomToolbar` class is equipped with a callback support for this.
A sample usage of this is:
```java
 BottomToolbar bottomToolbar = findViewById(R.id.bottom_toolbar);
        bottomToolbar.setButtonClickListener(new BottomToolbar.ButtonClickListener() {
            @Override
            public void onPlusButtonClicked() {
                Toast.makeText(MainActivity.this,"Button clicked!",Toast.LENGTH_LONG).show();
            }
        });
```

### Contributions

Any contributions are welcome. You can send PR or open issues.

