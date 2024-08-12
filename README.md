# SBorkify Android app
---

A dummy spotify clone for architecture demonstration purposes.
Due to the small scale of the app the architecture is purposely not bloated with abstraction.
The design follows Material Design principles.

This app follows the clean architecture principles:
- ui - com.cborko.ui
  - Single activity, each screen has its own viewModel injected by koin-compose
  - Compose Navigation
  - Koin DI
- data - com.cborko.data
  - Models are taken from the Spotify API as-is. All other mappings are done in ViewModel (for simplicity, as the app is not large).
- domain & usecases not present since the app is too small

---
## Preview
![preview](https://github.com/Borkec/SBorkify/docs/img.png)




