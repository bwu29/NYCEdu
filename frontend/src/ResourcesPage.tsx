import React from "react";
import { Link } from "react-router-dom";
import './css/Resources.css'


export default function ResourcesPage(){
    return (
        <>
        <div className= "Resources" aria-label= "resources">
            <div id= "Resources-Label">
                <h1 id="Resources-Text">
                About/Resources
                </h1>
            </div> 
            <br></br>
            <span id= "Content;">
            <h2 id = "Overview">
            Overview + Motivation
            </h2>
            <text>
                Despite being one of the most diverse cities in the US, NYC public schools are one of the most racially and economically segregated. Research and data have demonstrated that this segregation has led to poorer outcomes for economically disadvantaged students, and minorities, particularly Black and LatinX students. Through our project, we hope to shed light on the pervasive problem of segregation in NYC schools. 
                We recognize that our project will not solve this comprehensive and multifaceted issue, but we hope to expand awareness of disparities in urban education generally, specifically through NYC schools. Our goal is to provide teachers, parents, and students with digestible information and data, while ultimately targeting policymakers and informing policy and action.
            </text>
            <br></br>
            <br></br>
            <br></br>
            <h2>
                Contributors
            </h2>
            <text>
                Kaitlyn Williams, Hunter Adrian, Brandon Wu, Anna Hurd
            </text>
            <br></br>
            <br></br>
            <br></br>
            <h2>
                Useful Definitions 
            </h2>
            <text>
               <b> NYC DOE:</b> New York City Department of Education
            <br></br>
            <b>English Language Learner:</b> According to the DOE, an English Language Learner (ELL) is “...a student whose home language is not English and needs support learning English.”
            <br></br>
            <b>Income segregation in schools: </b> 
            The Center for American Progress defines income segregation in schools in three important ways:
            <ul>
                <li>“Large concentrations of low-income students in high-poverty schools;”</li>
                <li>“Large concentrations of high-income students in low-poverty schools;”</li>
                <li>“The degree to which poverty rates of individual schools differ from the district average.”</li>
            </ul>
            <b>Funding:</b> While laws are in place to ensure equitable government funding across public schools, the amount of resources at each school is not equal. We gathered data on the money raised by Parent-Teacher Associations (PTAs) for a more realistic and comprehensive view of school resources.
            <br></br>
            <b>Graduation Rates:</b> 4-year graduation rates by August of the closing semester.
            </text>
            <br></br>
            <br></br>
            <br></br>
            <h2>
                Resources Used
            </h2>

            <text>
                The majority of our data was sourced from NYC OpenData, NYU: Steinhardt, NYC Council Data, and NYC DOE InfoHub. All these websites were instrumental in developing an informed understanding of the state of public school education in NYC. We highly recommend any and all of these resources to dive deeper into this issue. More specifically, we used the following resources for the data points below:
                <ul>
                <li>Chronic absenteeism data: <a href = "https://infohub.nyced.org/reports/school-quality/information-and-data-overview/end-of-year-attendance-and-chronic-absenteeism-dataNYC"
                target = "_blank"> NYC DOE InfoHub</a></li>
                <br></br>
                <li>Funding data: <a href = "https://infohub.nyced.org/reports/government-reports/local-law-171-of-2018"
                target = "_blank"> NYC DOE InfoHub</a></li>
                <br></br>
                <li>Graduation rates: <a href = "https://infohub.nyced.org/reports/academics/graduation-results"
                target = "_blank"> NYC DOE InfoHub</a></li>
                <br></br>
                <li>School Names and DBN by latitude and longitude: <a href = "https://data.cityofnewyork.us/Education/2017-2018-School-Locations/p6h4-mpyys"
                target = "_blank">NYC OpenData</a></li>
                <br></br>
                <li>School-Level Student Demographic Snapshot: <a href = "https://data.cityofnewyork.us/Education/2013-2018-Demographic-Snapshot-School/s52a-8aq6"
                target = "_blank">NYC OpenData</a></li>
                <br></br>
                <li>District-Level Student Demographic Snapshot: <a href = "https://infohub.nyced.org/reports/school-quality/information-and-data-overview"
                target = "_blank">NYC DOE InfoHub</a></li>
                <br></br>
                <li>Citywide Student Demographic Snapshot: <a href = "https://www.schools.nyc.gov/about-us/reports/doe-data-at-a-glance"
                target = "_blank">NYC Gov</a></li>
                <br></br>
                <li>School-Level Teacher Demographic Snapshot: <a href = "https://infohub.nyced.org/reports/government-reports/report-on-school-based-staff-demographics"
                target = "_blank">NYC DOE InfoHub</a></li>
                <br></br>
                <li>District-Level and Citywide Teacher Demographic Snapshot: <a href = "https://data.cityofnewyork.us/Education/2019-2020-School-Year-Local-Law-226-Report-for-the/2jg5-6hqv"
                target = "_blank">NYC OpenData</a></li>
                </ul>
            </text>
            <br></br>
            <br></br>
            <br></br>
            <text>
            ~"Why was the JavaScript developer sad? Because he didn't know how to 'null' his feelings."
            </text>
                </span> 
           
            </div>

            <div className="to-map">
                    <Link to="/">
                    <button className="to-map-button" aria-label="to-map-button">Go To Map
                    </button>
                    </Link> 
                </div>
        


        </>
        
        
    )
}