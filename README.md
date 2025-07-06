# MoviReco

## 1. What is MoviReco?
**MoviReco** is a web application that recommends movies based on your mood by answering just **three simple questions**.  
Its swipe-based UI makes it easy to browse and discover the perfect movie for the moment.

## 2. Features
- Get 20 movie recommendations by answering just 3 simple questions
- Intuitive swipe-based UI for smooth browsing
- View detailed movie info and streaming provider availability at a glance
- Login support: Automatically get recommendations based on your favorite movies & save them to your favorites list

## 3. Live Demo
You can try the live app here:  
🎬 [https://movireco.com](https://movireco.com)

## 4. スクリーンショット

## 5. Tech Stack
| Layer        | Technologies                 |
|--------------|------------------------------|
| Frontend     | Nuxt 3, Vue 3, Composition API |
| Backend      | Spring Boot 3.4.3, Java 21, MyBatis |
| Database     | PostgreSQL                   |
| Infrastructure | Render, Docker               |
| Others       | TMDb API, JWT Authentication |

## 6. Project Structure

```plaintext
movie-recommendation/
├── nuxt/                     # Frontend (Nuxt 3)
│   ├── Dockerfile.dev        # Dockerfile for frontend development
│   └── Dockerfile.prod       # Dockerfile for frontend production
├── src/                      # Backend (Spring Boot)
│   ├── Dockerfile.dev        # Dockerfile for backend development
│   └── Dockerfile.prod       # Dockerfile for backend production
├── gradle/                   # Gradle configuration files
├── build.gradle              # Gradle build script
├── settings.gradle           # Gradle project settings
├── docker-compose.dev.yml    # Docker setup for local development
├── .gitignore                # Git ignore rules
├── LICENSE                   # License information
├── README.md                 # This file
└── Others (Gradle wrapper, IDE settings, etc.)
```
## 7. Getting Started
This section is currently under construction.  
Instructions for running the project locally will be added soon.

## 8. Environment Variables
This section is currently under construction.  
A list of required environment variables will be added soon.

## 9. Roadmap
- Add AI-powered recommendation logic
- Improve the accuracy of movie recommendations
- Design a responsive UI for desktop (currently mobile-only)
- Grow user base and improve service reach

## 10. ライセンス（License）

## 11. Acknowledgement
This application uses TMDB and the TMDB APIs but is not endorsed, certified, or otherwise approved by TMDB.

## 12. Author
Developed by [@devtkms](https://github.com/devtkms)  
Twitter (developer): [@devtkms](https://twitter.com/devtkms)  
Twitter (project): [@MoviReco](https://twitter.com/MoviReco)