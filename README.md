# NammaHasiru (Namma Hasiru)

An Android application to manage and track plants, view their details, add new plants with location and image, and set reminders.

## Screenshots

> Note: Images will render only after they are uploaded to GitHub under `/screenshots`.

- Home

  <img src="screenshots/home.png" alt="Home" width="260"/>

- Add Plant (Loading)

  <img src="screenshots/loadingscreen.png" alt="Add Plant Loading" width="260"/>
  <img src="screenshots/addplant.png" alt="Add Plant" width="260"/>

- Analytics

  <img src="screenshots/analytics.png" alt="Analytics" width="260"/>

- Impacts

  <img src="screenshots/impacts.png" alt="Impacts" width="260"/>

- Profile

  <img src="screenshots/profile.png" alt="Profile" width="260"/>

- Notification (Reminder)

  <img src="screenshots/notification.png" alt="Notification" width="260"/>

- View Plant / Details

  <img src="screenshots/viewplant.png" alt="View Plant" width="260"/>


## Features

- Add plants (species, notes, image, latitude/longitude, status)
- View plant list and individual details
- Update plant info
- Analytics and impact screens
- Location support (Google Maps)
- Reminders using WorkManager + Notifications

## Tech Stack

- Kotlin
- Android SDK
- Room (local database)
- WorkManager (background reminders)
- Google Maps / Location services
- Glide (image loading)

## Setup (Android Studio)

1. Open `NammaHasiru2` in Android Studio.
2. Build and run.

### Google Maps API Key
The app expects a Google Maps API key in `AndroidManifest.xml`:
- `com.google.android.geo.API_KEY` is currently set to `YOUR_GOOGLE_MAPS_API_KEY`.

Replace it with your real API key.

## Author

AADITYA PANDEY

## License

MIT


