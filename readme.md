
# **PokeApp**
This is an app for ArchitectCoders course, developed by Albert Bonet and following the lessons of the course.

## How to use
PokeApp is very simple. It has two screens: List of pokemons (Main) and Detail of pokemon. The data on the main screen is retrieved from the **PokeApi**, and it consists of a simple three-row list of all known Pokémon. The data on the detail screen is also from the **PokeApi**, but it provides more detailed information. At this point, the app makes another request to the API to fetch additional information about the Pokémon.
This app has a **Bluetooth** feature too. Allows you to share Pokémon between two devices with the application using **Bluetooth**. Following the next steps we can share Pokémons:

 -   The receiving device must click the upper right icon on the main screen, which looks like an arrow pointing downwards.
 - The transmitting device must click the upper right icon on the detail screen, which looks like an arrow pointing upwards. Then, a dialog with a list of nearby Bluetooth devices will appear. When the user clicks on a device from the list, it will ask on both devices to pair them. If both accept, the Pokémon will be sent.

|  ![Main screen](images/main_screen.png?raw=true)|![Detail screen](https://raw.githubusercontent.com/Abv1206/pokeapp/master/images/detail_screen.png)  |
|--|--|

## Tech and library
- 100% Kotlin
- Gradle Version Catalogs
- Coroutines and Flow
- Lifecycle
- ViewModel
- DataBinding
- State Holder
- Glide
	- Glide Transformations
	- Ciberagent GPUImage
- Skydoves ProgressView
- Facebook Shimmer
- Room
- Turbine
- Dagger
	- Hilt
- OkHttp3 & Retrofit2
- Arrow KT
- Mockito
- Espresso
- JUnit

- Architecture
	- Based on the **MVVM** architecture as taught in the Architect Coders course.
- Modularization
	- Following the Architect Coders course:
		- :data
		- :domain
		- :usecases
		- :testShared
		- :appTestShared

## Testing
It has implemented simple tests that cover:
- Unit tests
- Integration tests
- Instrumentation tests

## License
Developed and designed by Albert Bonet, 2023
App for free use. Commercial purposes are banned.
