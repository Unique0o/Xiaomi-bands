<br />
<div align="center">
   <a href="https://github.com/othneildrew/Best-README-Template">
       <img src="https://github.com/user-attachments/assets/d1843321-7c47-4ffd-8a48-18093a980e5d" />
   </a>
   
  <h1 align = "center">
    <b><i>LogifitApp</i></b>![Logo]()

  </h1>
  
  
</div>
LogifitApp is a application developed in Kotlin using the MVVM pattern, Jetpack Compose, and other modern Android technologies. The app focuses on extracting and analyzing data from Xiaomi Smart Bands 8 and 9.

## Key Features

- Connection and data extraction from Xiaomi Smart Bands 8 and 9
- Analysis and visualization of physical activity data
- Modern user interface with Jetpack Compose

## Technologies Used

- Kotlin
- MVVM (Model-View-ViewModel)
- Jetpack Compose
- Coroutines for asynchronous operations
- Room for data persistence
- Bluetooth Low Energy (BLE) for device connection

## Project Structure

The project follows a modular architecture, emphasizing separation of concerns:

- `core/`: Contains the core logic of the application
- `data/`: Data layer and repositories
- `di/`: Dependency injection
- `domain/`: Business logic and use cases
- `ui/`: User interface with Jetpack Compose
- `utils/`: General utilities
- `viewmodel/`: MVVM ViewModels

### Detailed Structure of the `core` Folder

The `core` folder is fundamental for the functionality of extracting data from Xiaomi bands:

* `wearables/`
   * `xiaomi/`
      * `XiaomiConnector`: Handles Bluetooth connection
      * `XiaomiDataExtractor`: Extracts raw data from bands 8 and 9
      * `XiaomiDataParser`: Processes the extracted data

### Data Flow

The process of obtaining and using data follows this flow:

1. Bluetooth connection (XiaomiConnector)
2. Raw data extraction (XiaomiDataExtractor)
3. Data parsing (XiaomiDataParser)
4. Storage in local database
5. Application use (visualization, analysis, etc.)

### Interaction with Other Parts of the App

- Processed data is passed to corresponding ViewModels, which act as intermediaries between the data layer and UI.
- Repositories in the data layer are updated with the most recent information obtained from the bands, ensuring data consistency throughout the application.

### Specific Features of Bands 8 and 9

Types of data that can be extracted:
- Steps
- Heart rate
- Sleep quality
- Blood oxygen level (SpO2)
- Stress level

Differences in data handling between band versions:
- Band 9 offers greater precision in heart rate and SpO2 measurement.
- Data extraction from Band 9 requires a more advanced authentication protocol.
- Band 9 provides additional data on heart rate variability (HRV).

## Setup and Usage

[Instructions for setting up and running the project]


# :handshake: Contributing :handshake:🔥

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request and enjoy! :D

# :scroll: License :scroll:
```
MIT License

Copyright (c) 2024 Logifit

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
