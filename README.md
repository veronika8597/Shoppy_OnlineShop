# 🛍️ Shoppy Online Shop

Shoppy is a fully-featured Android e-commerce app built with Kotlin. It supports real-time search, user authentication with Google and Firebase, visual search using CameraX and ML Kit, a shopping bag, reviews, and much more.

## 📱 Features

- Google Sign-In authentication 🔐
- Firebase Realtime Database + Firestore integration 🔥
- Product search with live filtering 🔎
- Add to favorites and shopping bag ❤️🛒
- Submit and read product reviews 🌟
- Visual product search using CameraX + ML Kit 🤳🧠
- Beautiful XML-based UI with ViewBinding 🧩
- Modern architecture with ViewModel and LiveData 🧠
- Offline data handling for bag/favorites using local ViewModel cache 💾

## 🧰 Tech Stack

- **Language**: Kotlin
- **UI**: XML Layouts + ViewBinding
- **Architecture**: MVVM (ViewModel + LiveData)
- **Authentication**: Firebase Auth with Google Sign-In
- **Backend**: Firebase Realtime DB & Firestore
- **Image Loading**: Glide
- **Camera**: CameraX
- **ML**: Firebase ML Kit (Image Labeling)
- **Others**: Retrofit, Coroutines, Room, Shimmer

## 🚀 Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/shoppy-online-shop.git
   ```

2. Open the project in Android Studio.

3. Add your own `google-services.json` in the `app/` folder.

4. Make sure to replace the placeholder `GOOGLE_CLIENT_ID` in your `gradle.properties` or set it in your `local.properties`.

5. Run the app on your device or emulator.

## ✅ Prerequisites

- Android Studio Iguana or newer
- Kotlin 1.9+
- Firebase project with enabled:
  - Realtime Database
  - Firestore
  - Authentication (Google)
  - ML Kit (Image Labeling)

## 🧪 Testing

To skip tests when building:

```bash
./gradlew clean build -x test
```

## 🤖 SHA-1 Note

Make sure the SHA-1 of your signing key is added to the Firebase console for Google Sign-In to work.

## 📄 License

MIT License — feel free to use, modify, and contribute.
