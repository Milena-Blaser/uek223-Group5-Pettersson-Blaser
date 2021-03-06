# uek223-Group5-Pettersson-Blaser

## Description
This repository is a partial functionality of a bigger project. It implemented a list entry where every user can create personal list entries and manage them. Every user can look at all the other users list entries. Users that are not logged in can't use any of the endpoints. For the login the basic authentification is used. If you're an admin, a summary of the endpoints is available at the path /swagger-ui/ .

## Setup

Enter the following command into your command line

`docker run --name postgres-db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres`

Clone the repository and open it in IntelliJ. Make sure you're using JDK 11 in both the project settings and the settings. 
Shortcut for Project Settings:
`Ctrl + Shift + Alt + S`
Shortcut for Settings:
`Ctrl + Alt + S`
Gradle should also be set to  JDK 11. After completing that step run the project and you should be able to access the endpoints referenced in the documentation.
