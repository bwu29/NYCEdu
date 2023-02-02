# README
# Term Project: A Closer Look at Public School Education in NYC

## Project Details
**Team Members:** Kaitlyn Williams (kwilli43), Anna Hurd (ahurd2), Brandon Wu (bwu29), and Hunter Adrian (hadrian)

**Time Taken:** 50 hours 

**Repo Link:** https://github.com/cs0320-f2022/term-project-ahurd2-bwu29-hadrian-kwilli43

**Project Description**

This project shows a map of NYC, with the school districts shown, as well as markers for all the public high schools in the city. Users can click on each district and high school and 
find information about that school/district's teacher demographics, student demographics, chronic absences, graduation rates, and funding.


Despite being one of the most diverse cities in the US, NYC public schools are one of the most racially and economically segregated. Research and data have demonstrated that this segregation has led to poorer outcomes for economically disadvantaged students, and minorities, particularly Black and LatinX students. Through our project, we hope to shed light on the pervasive problem of segregation in NYC schools.
We recognize that our project will not solve this comprehensive and multifaceted issue, but we hope to expand awareness of disparities in urban education generally, specifically through NYC schools. Our goal is to provide teachers, parents, and students with digestible information and data, while ultimately targeting policymakers and informing policy and action.

## Design Choices

### Project Structure
The project is structured where the backend retrieves all the data we are collecting from various APIs and the frontend fetches that data to be displayed to the user.

In the backend, we have our `Server` class that creates 6 different endpoints, with handler classes for each endpoint. 
Each handler class access the correct API for the data they are trying to collect for the high school, district, and city. 
For each handler, we have a class for school and district Moshi accessors that help us to retrieve the data. We have a 
`utilities` package, that is called on by each handler, that contains classes that include defensive programing, storing responses, server status, and deserialization 
helper methods. 

In the frontend, we have the `App` class that renders the whole website. Our `SchoolDistrictOverlay` class adds the districts and their colors to the map.
The `Map` renders the map, as well as the organized information, onto the screen. The `Map` class accesses the backend data by using different fetch statements to display that information to the user. 
This is where the integration between the backend and the frontend happen. 
The `ResourcesPage` class contains the structure and content of the separate resources page. 
We have a `css` folder with the styling of each page contained. 

### Runtime/Space Optimizations
In order to improve runtime, we have decided to create some static variables for information that never changes, like the citywide data. 
Otherwise, we did not make many changes for the sake of runtime or space optimization. 

## Errors and Bugs
There are no known errors or bugs.

## Testing:
### Frontend testing:
Check that all map page and resource page elements are rendered properly. 

### Backend testing:
We tested each handler class in separate testing suites. Each testing suites contains tests that include standard success responses and accurate retrieval for the school-level, district-level, citywide, and "no data available" handling. 
They also test for error cases, including invalid and missing "types", invalid school and district names, invalid spelling, does not exist, and district numbers out of bounds.

### User testing:
Allowed NYC parents to interact with our webapp and provide feedback.



## How to…

### Run the tests
To run the front end testing, in VS Code, navigate to the `App.test` directory and run the command `npm test` in the terminal.

To run the back end testing, in Intellij, open the `TestAbsences`, `TestFunding`, `TestGradRates`, `TestSchoolNames`, `TestStudentDemographics`, or `TestTeacherDemographics` classes, then press the double green arrow at the top of the class. 
### Build and Run the program
To run the program, in VS Code, navigate to the `frontend` directory and run the command `npm start` in the terminal. In IntelliJ, click the green arrow in the `Server` class to run the backend server. Then a webpage with the loaded map and information should load.

## User Stories
1) As a user of the product, I am able to view urban disparity data between two different districts so that I can compare the differences in disparity across districts. The user is also able to see and compare data of public high schools in NYC, as well as the city itself.
2) As a user of the web app, I can view a map of the area I’m interested in at the zoom level I want so that I can easily navigate and learn about specific parts of New York City to further my understanding of urban education disparity.
3) As a back-end developer of your web app, I can add another data source relating to NYC that can easily be incorporated without much refactoring of code.
