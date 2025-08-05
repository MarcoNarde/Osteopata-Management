# Osteopath Management System

A desktop application built with Kotlin Multiplatform and Compose for managing osteopath patients and visits.

## Features

### Current Features
- **Patients Management**: View and manage patient information including name, contact details, and age
- **Visits Management**: Schedule and track patient visits with status tracking
- **Modern UI**: Clean, Material Design 3 interface with tabbed navigation
- **Desktop Application**: Native desktop experience with proper window management
- **Clean Architecture**: Well-organized code structure with separation of concerns
- **Internationalization**: Support for English and Italian languages with localized strings

### UI Components
- **Tab Navigation**: Switch between Patients and Visits views
- **Patient Cards**: Display patient information in organized cards
- **Visit Cards**: Show visit details with status indicators
- **Status Tracking**: Visual status chips for visit states (Scheduled, Completed)

## Running the Application

### Desktop (JVM)
```bash
./gradlew :composeApp:run
```

### Android
```bash
./gradlew :composeApp:androidDebug
```

### iOS
Open the `iosApp` folder in Xcode and run the project.

## Project Structure

### Clean Architecture Organization

```
composeApp/src/commonMain/kotlin/com/narde/gestionaleosteopatabetto/
├── App.kt                           # Main application entry point
├── data/                            # Data layer
│   ├── model/                       # Data models
│   │   ├── Patient.kt              # Patient data model
│   │   ├── Visit.kt                # Visit data model
│   │   └── VisitStatus.kt          # Visit status enumeration
│   └── sample/                      # Sample data
│       └── SampleData.kt           # Mock data for testing
├── ui/                              # Presentation layer
│   ├── App.kt                      # Main UI component
│   ├── components/                  # Reusable UI components
│   │   ├── PatientCard.kt          # Patient display component
│   │   ├── VisitCard.kt            # Visit display component
│   │   └── LanguageSwitcher.kt     # Language selection component
│   ├── resources/                   # Internationalization
│   │   ├── Strings.kt              # String constants and localization
│   │   └── StringResources.kt      # String resource manager
│   └── screens/                     # Screen components
│       ├── PatientsScreen.kt        # Patients list screen
│       └── VisitsScreen.kt         # Visits list screen
├── Greeting.kt                      # Sample greeting component
└── Platform.kt                      # Platform-specific implementations
```

### Architecture Benefits

- **Separation of Concerns**: Data models, UI components, and screens are clearly separated
- **Reusability**: Components can be easily reused across different screens
- **Maintainability**: Each file has a single responsibility
- **Testability**: Components can be tested independently
- **Scalability**: Easy to add new features and screens

## Data Models

### Patient
- `id`: Unique identifier
- `name`: Patient full name
- `phone`: Contact phone number
- `email`: Contact email address
- `age`: Patient age

### Visit
- `id`: Unique identifier
- `patientName`: Associated patient name
- `date`: Visit date
- `time`: Visit time
- `status`: Visit status (Scheduled, Completed, etc.)
- `notes`: Additional visit notes

## Development

This project uses:
- **Kotlin Multiplatform**: Cross-platform development
- **Compose Multiplatform**: Modern declarative UI framework
- **Material Design 3**: Consistent design system
- **Clean Architecture**: Well-organized code structure
- **Internationalization**: Multi-language support (English/Italian)
- **Gradle**: Build system with Kotlin DSL

## Code Organization Principles

### Data Layer (`data/`)
- **Models**: Pure data classes without business logic
- **Sample Data**: Mock data for testing and development

### UI Layer (`ui/`)
- **Components**: Reusable UI elements (cards, buttons, etc.)
- **Screens**: Complete screen implementations
- **App**: Main application composition

### Benefits of This Structure
- **Modularity**: Each component has a single responsibility
- **Reusability**: Components can be shared across screens
- **Maintainability**: Easy to locate and modify specific functionality
- **Testability**: Components can be tested in isolation
- **Scalability**: Easy to add new features without affecting existing code

## Next Steps

Future enhancements planned:
- Database integration for persistent data storage
- Add/Edit/Delete functionality for patients and visits
- Calendar view for visit scheduling
- Search and filtering capabilities
- Export functionality for reports
- User authentication and multi-user support
- State management with ViewModels
- Repository pattern for data access
- Dynamic language switching with UI updates
- Additional language support (Spanish, French, etc.)