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

## 4. Screenshots

<img src="https://github.com/user-attachments/assets/d7123da5-6ce4-4c80-9d6a-7aa600739266" width="200" alt="Screenshot 1" />
<img src="https://github.com/user-attachments/assets/811aa939-cd1d-409f-a282-94975e357bb2" width="200" alt="Screenshot 2" />
<img src="https://github.com/user-attachments/assets/c2842289-7c9e-4a06-b1ab-9144ca559e8d" width="200" alt="Screenshot 3" />
<img src="https://github.com/user-attachments/assets/bbdc6f46-a49b-46f0-960f-4d8362af7998" width="200" alt="Screenshot 4" />
<img src="https://github.com/user-attachments/assets/965b0e90-07c3-4673-9334-40c15cd37325" width="200" alt="Screenshot 5" />
<img src="https://github.com/user-attachments/assets/9800224b-7a54-4009-93fe-5000e0047bfc" width="200" alt="Screenshot 6" />



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

## 10. License
This project is currently not licensed.  
Unauthorized use, reproduction, modification, or redistribution of the source code is strictly prohibited.  
If you wish to use any part of this project, please contact the author for permission.

## 11. Acknowledgement
This application uses TMDB and the TMDB APIs but is not endorsed, certified, or otherwise approved by TMDB.

## 12. Author
Developed by [@devtkms](https://github.com/devtkms)  
Twitter (developer): [@devtkms](https://twitter.com/devtkms)  
Twitter (project): [@MoviReco](https://twitter.com/MoviReco)
