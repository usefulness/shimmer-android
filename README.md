<h1 align="center">
<img src="/shimmer.gif?raw=true" alt="Shimmer" /><br />
Shimmer for Android
</h1>


_Opinionated_ fork of https://github.com/facebook/shimmer-android

## Quickstart

1. Add dependency to `build.gradle`
```groovy
dependencies {
    implementation("io.github.usefulness:shimmer-android-core:${{ version }}")
}
```

2. Use library

a) via xml
```xml
 <io.github.usefulness.shimmer.android.ShimmerFrameLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:shimmer_duration="2000"
    >

      <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Content"
        />
</io.github.usefulness.shimmer.android.ShimmerFrameLayout>
```

b) via code
```kotlin
val shimmerFrameLayout : ShimmerFrameLayout 
shimmerFrameLayout.shimmer = Shimmer(
    duration = 2000,
)
```
3. Done 🎉

## Advanced
All options with default values
a) xml
```xml
 <io.github.usefulness.shimmer.android.ShimmerFrameLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:shimmer_direction="left_to_right"
    app:shimmer_base_alpha="0.3f"
    app:shimmer_highlight_alpha="1f"
    app:shimmer_shape="linear"
    app:shimmer_intensity="0f"
    app:shimmer_dropoff="0.5f"
    app:shimmer_tilt="20f"
    app:shimmer_clip_to_children="true"
    app:shimmer_auto_start="true"
    app:shimmer_repeat_count="1000"
    app:shimmer_repeat_mode="restart"
    app:shimmer_repeat_delay="0"
    app:shimmer_start_delay="0"
    app:shimmer_colored="false"
    >

      <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Content"
        />
</io.github.usefulness.shimmer.android.ShimmerFrameLayout>
```

b) code
```kotlin
val shimmer = Shimmer(
    style = Shimmer.Style.Alpha,
    direction = Shimmer.Direction.LeftToRight,
    baseAlpha = 0.3f,
    highlightAlpha = 1f,
    shape = Shimmer.Shape.Linear,
    intensity = 0f,
    dropoff = 0.5f,
    tilt = 20f,
    clipToChildren = true,
    autoStart = true,
    repeatCount = 1000,
    repeatMode = ValueAnimator.RESTART,
    animationDuration = 1000,
    repeatDelay = 0,
    startDelay = 0,
)
```
## License
The library is available under the mixed license https://github.com/usefulness/shimmer-android/blob/master/LICENSE
