# Elanco-Task
Assigned task set by Elanco - Full Stack  
Recording -> https://youtu.be/r9LN9Wq8FGg

# Project
### Personal Reasoning:
For the task, I chose the full stack project to stretch myself. I'm much more confident with back end development and data manipulation than I am with front end UI and UX so I wanted to try my hand at front end development. In a short summary, I very much enjoyed the backend development, however I really did not enjoy the front end development in any way. My preferences still very much stand with backend development, however I don't regret trying and sticking with my choice.

### Overall Project and Technical Choices
The project is divided between front end and back end.

Back end:  
- Java 25
    - I used java as it's the programming language I'm most comfortable with and just generally enjoy programming in java.
- Spring Boot 4.1.2
    - Runs the application and provides the backend framework, out of all the frameworks, I've dabbled in Spring slightly so have a better idea on how to use it already.
- Spring MVC/ REST
    - Handles web pages and API routes, this is what deals with the GET and POST endpoints.
- Spring RestClient
    - This was used to just call the Elanco sightings API to get a hold of the tick data.
- Jackson
    - This was used in the ParseJson function to deal with the Json objects and convert it into java classes which can then be manipulated. Popular and easy dependancy to work with.
- Maven
    - Maven was used to manage dependancies and build/run the project. This can be thought of as the foundation of the whole project. 

Front end:  
- HTML
    - The loose files within resources/static are done in HTML and these are what make the web page look the way they do. Obviously every website is made using HTML and I'm not going to be the one to change that. There are mountains of documentation and support for HMTL web development which makes it a lot easier to program with 
- CSS
    - The CSS is responsible for the layout, colours, responsive styling and navigation menu. Most styling is written in each HTML pages <'style'> block. Once again lot of documentation and support for CSS.
- JavaScript
    - Same as HTML and CSS, lots of documentation for web development making it easier to work with. No front end framework was used such as React so all the code is just raw JavaScript
- Leaflet.js
    - This was used for the interactive map in map.html
- *Figma**
    - This has a '*' next to it because i was initially using this to make the web design but quick lost my patience with it and decided to pivot to something else which I will expand on later.

## How To Run  
I spent a bit of extra time containerising this to make it easier.  
1. Clone the repo using:
    > git clone https://github.com/Kai-McGinty/Elanco-Task.git
2. Cd into the Tick-Sighting folder:
    > cd Elanco-Task/Tick-Sighting  
3. Run the following command:
    > docker compose up --build
4. Open your browser of choice and go to http://localhost:8080 and the program should be good to play with.
