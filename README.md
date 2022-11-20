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

The library is available under MIT License:
```markdown
MIT License

Copyright (c) Developers at https://github.com/usefulness

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

the original library, which this fork is heavily based on is published under BSD License:

```markdown
For Shimmer-android software

Copyright (c) Meta Platforms, Inc. and affiliates. All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name Meta nor the names of its contributors may be used to
  endorse or promote products derived from this software without specific
  prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
```
