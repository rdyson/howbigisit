# howBigisit

Take a photo of anything. The app asks: **"Do you want to know how big is it?"**

- **No** → the app closes. Some things are better left unknown.
- **Yes** → you receive one of four verdicts, chosen completely at random and
  tied to no measurement whatsoever: **Little**, **Medium**, **Kind of big**, **Big**.

That's it. That's the app.

## Try it right now

A browser prototype of the exact same flow lives at
**https://rdyson.dev/howbigisit/** — open it on a phone and
"Take a photo" uses the real camera. No install required.

## Running it

This machine had no Android toolchain, so the project is scaffolded but not yet built.

1. Install [Android Studio](https://developer.android.com/studio) (free; bundles the
   Java runtime and Android SDK).
2. Open Android Studio → **Open** → select this `howbigisit` folder.
3. Let Gradle sync (first sync downloads dependencies; if it complains about a missing
   Gradle wrapper jar, accept the prompt to use Gradle 8.7 / regenerate the wrapper).
4. Plug in an Android phone with USB debugging enabled (or create an emulator via
   Device Manager — note: emulators fake the camera feed, which arguably improves the app).
5. Press **Run ▶**.

## Architecture

One activity, three screens, zero measurements:

```
Capture ──photo──▶ Asking ──Yes──▶ Verdict (random of 4)
                     │
                     No
                     ▼
                  finish()
```

Uses the system camera via `TakePicturePreview`, so it needs no camera permission,
no storage permission, and no FileProvider. The returned thumbnail is plenty —
this app does not require detail.
