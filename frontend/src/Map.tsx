import Map, { Source, Layer, MapLayerMouseEvent, ViewState, PointLike, MapboxGeoJSONFeature, MapRef, Marker }from "react-map-gl"
import 'mapbox-gl/dist/mapbox-gl.css'
import { Dispatch, RefObject, SetStateAction, useEffect, useRef, useState } from "react"
import React from 'react';
import './css/Map.css'
import {mapbox} from "./private/mapbox"
import { districtLayer, fetchDistrictOverlay} from "./SchoolDistrictOverlay";
import schools from './datasources/schools.json'
import { Link } from "react-router-dom";

const lonNYC = -74.01
const latNYC = 40.7128;
const key = mapbox;

export const MapBox: React.FunctionComponent = () => {

    const [viewState, setViewState] = useState<ViewState>({
        longitude: lonNYC,
        latitude: latNYC,
        zoom: 9.5,
        bearing: 0,
        pitch: 0,
        padding: {top: 1, bottom: 20, left: 1, right: 1}
    });

    let [schoolLat, setSchoolLat] = useState<number | null>(null);
    let [schoolLon, setSchoolLon] = useState<number | null>(null);
    let [district, setDistrict] = useState('');

    interface SchoolData {
        [key: number]: Array<any>;
    }
    const schoolDataDict: SchoolData = {};

    let schoolDataArray: Array<any> = [];
    let [schoolObject, setSchoolObject] = useState<SchoolData>({});

    let [teacherDemographicsDistrict, setTeacherDemographicsDistrict] = useState({});
    let [studentDemographicsDistrict, setStudentDemographicsDistrict] = useState({});
    let [absencesDistrict, setAbsencesDistrict] = useState('');
    let [graduationRatesDistrict, setGraduationRatesDistrict] = useState('');
    let [fundingDistrict, setFundingDistrict] = useState('');

    let [NycTeacherDemographics, setNycTeacherDemographics] = useState({});
    let [NycStudentDemocgrphics, setNycStudentDemographics] = useState({});
    let [NycAbsences, setNycAbsences] = useState('');
    let [NycGraduationRates, setNycGraduationRates] = useState('');
    let [NycFunding, setNycFunding] = useState('');

    const fetchSchoolNames = async (schoolLat: any, schoolLon: any) => {
        const namerResponse = await fetch(`http://localhost:32/schoolName?x=${schoolLat}&y=${schoolLon}`);
        const schoolNameData = await namerResponse.json();
        const schoolNames = await schoolNameData.data;

        for (let i = 0; i < schoolNames.length; i++) {
                const teacherDemographicResponse = await fetch(`http://localhost:32/teacherDemographics?type=school&&school_name=${schoolNames[i]}`);
                const teacherData = await teacherDemographicResponse.json();
                const teacherDemographicData = await teacherData.data;

                const studentDemographicResponse = await fetch(`http://localhost:32/studentDemographics?type=school&&school_name=${schoolNames[i]}`);
                const studentData = await studentDemographicResponse.json();
                const studentDemographicData = await studentData.data;

                const absencesResponse = await fetch(`http://localhost:32/absences?type=school&&school_name=${schoolNames[i]}`);
                const absencesData = await absencesResponse.json();
                let studentAbsenceData = "No Data";
                if (absencesData.result == "success") {
                    studentAbsenceData = await absencesData.data;
                }

                const graduationRatesresponse = await fetch(`http://localhost:32/graduationRates?type=school&&school_name=${schoolNames[i]}`);
                const graduationRatesData = await graduationRatesresponse.json();
                let studentGraduationRatesData = "No Data";
                if (graduationRatesData.result == "success") {
                    studentGraduationRatesData = await graduationRatesData.data;
                }

                const Fundingesponse = await fetch(`http://localhost:32/funding?type=school&&school_name=${schoolNames[i]}`);
                const fundingData = await Fundingesponse.json();
                let studentFundingData = "No Data";
                if (fundingData.result == "success") {
                    studentFundingData = await fundingData.data;
                }

                schoolDataArray[0] = schoolNames[i];
                schoolDataArray[1] = teacherDemographicData;
                schoolDataArray[2] = studentDemographicData;
                schoolDataArray[3] = studentAbsenceData;
                schoolDataArray[4] = studentGraduationRatesData;
                schoolDataArray[5] = studentFundingData;
            schoolDataDict[i] = schoolDataArray;
            schoolDataArray = [];
        }
        setSchoolObject({})
        setSchoolObject(schoolDataDict)
    };

    const fetchDistrictData = async (district: string) => {

        const teacherDemographicResponse = await fetch(`http://localhost:32/teacherDemographics?type=district&&number=${district}`);
        const teacherData = await teacherDemographicResponse.json();
        const teacherDemographicData = await teacherData.data;
        setTeacherDemographicsDistrict(teacherDemographicData)

        const studentDemographicResponse = await fetch(`http://localhost:32/studentDemographics?type=district&&number=${district}`);
        const studentData = await studentDemographicResponse.json();
        const studentDemographicData = await studentData.data;
        setStudentDemographicsDistrict(studentDemographicData)

        const absencesResponse = await fetch(`http://localhost:32/absences?type=district&&number=${district}`);
        const absencesData = await absencesResponse.json();
        const studentAbsenceData = await absencesData.data;
        setAbsencesDistrict(studentAbsenceData)

        const graduationRatesresponse = await fetch(`http://localhost:32/graduationRates?type=district&&number=${district}`);
        const graduationRatesData = await graduationRatesresponse.json();
        const studentGraduationRatesData = await graduationRatesData.data;
        setGraduationRatesDistrict(studentGraduationRatesData)

        const Fundingesponse = await fetch(`http://localhost:32/funding?type=district&&number=${district}`);
        const fundingData = await Fundingesponse.json();
        const studentFundingData = await fundingData.data;
        setFundingDistrict(studentFundingData)
    }

    const fetchNycData = async () => {

        const teacherDemographicResponse = await fetch(`http://localhost:32/teacherDemographics?type=city`);
        const teacherData = await teacherDemographicResponse.json();
        const teacherDemographicData = await teacherData.data;
        setNycTeacherDemographics(teacherDemographicData)

        const studentDemographicResponse = await fetch(`http://localhost:32/studentDemographics?type=city`);
        const studentData = await studentDemographicResponse.json();
        const studentDemographicData = await studentData.data;
        setNycStudentDemographics(studentDemographicData)

        const absencesResponse = await fetch(`http://localhost:32/absences?type=city`);
        const absencesData = await absencesResponse.json();
        const studentAbsenceData = await absencesData.data;
        setNycAbsences(studentAbsenceData)

        const graduationRatesresponse = await fetch(`http://localhost:32/graduationRates?type=city`);
        const graduationRatesData = await graduationRatesresponse.json();
        const studentGraduationRatesData = await graduationRatesData.data;
        setNycGraduationRates(studentGraduationRatesData)

        const Fundingesponse = await fetch(`http://localhost:32/funding?type=city`);
        const fundingData = await Fundingesponse.json();
        const studentFundingData = await fundingData.data;
        setNycFunding(studentFundingData)
    }

    function displaySchoolsAll() {
        if (schoolObject != undefined) {
          let text = "";
          let objectLength = 0;
          if (schoolObject != undefined) {
            if (schoolObject[0] != undefined) {
              objectLength = Object.keys(schoolObject).length;
              for (let i = 0; i < objectLength; i++) {
                text += 
                
                `<h3> School: ${schoolObject[i][0]} </h3>

                <u>Teacher Demographic Breakdown (%):</u><br><br>

                White: ${schoolObject[i][1]['White'] || 'No Data'}<br>
                Black: ${schoolObject[i][1]['Black'] || 'No Data'}<br>
                Asian: ${schoolObject[i][1]['Asian'] || 'No Data'}<br>
                LatinX: ${schoolObject[i][1]['LatinX'] || 'No Data'}<br>
                Native American: ${schoolObject[i][1]['Native American'] || 'No Data'}<br>
                Hawaiian/ Pacific Islander: ${schoolObject[i][1]['Hawaiian/ Pacific Islander'] || 'No Data'}<br><br>
                
                <u>Student Demographic Breakdown (%):</u><br><br>

                Female: ${schoolObject[i][2]['Female'] || 'No Data'}<br>
                Male: ${schoolObject[i][2]['Male'] || 'No Data'}<br><br>

                Black: ${schoolObject[i][2]['Black'] || 'No Data'}<br>
                Asian: ${schoolObject[i][2]['Asian'] || 'No Data'}<br>
                LatinX: ${schoolObject[i][2]['Hispanic'] || 'No Data'}<br>
                White: ${schoolObject[i][2]['White'] || 'No Data'}<br><br>

                Students with Disabilities: ${schoolObject[i][2]['Students with Disabilities'] || 'No Data'}<br>
                English Language Learner: ${schoolObject[i][2]['English Language Learner'] || 'No Data'}<br>
                Students in Poverty: ${schoolObject[i][2]['Students in poverty'] || 'No Data'}<br><br>

                
                <u>Students Chronically Absent:</u> ${schoolObject[i][3] || 'No Data'}<br><br>

                <u>Graduation Rate:</u> ${schoolObject[i][4] || 'No Data'}<br><br>

                <u>School Funding (dollars raised):</u> ${schoolObject[i][5] || 'No Data'}<br><br>
                `;
              }
            }
          }
          return <div dangerouslySetInnerHTML={{ __html: text }} />;
        }
      }

    function displayTeacherDistrictData() {
        const keys = Object.keys(teacherDemographicsDistrict);
        const values = Object.values(teacherDemographicsDistrict);
        let paragraph = "";
        for (let i = 0; i < keys.length; i++) {
            paragraph = paragraph + " " + `${keys[i]}: ${values[i]}`+ "<br>" 
        }
        return <p dangerouslySetInnerHTML={{ __html: paragraph }} />;
    }


    function displayStudentDistrictData() {
        const keys = Object.keys(studentDemographicsDistrict);
        const values = Object.values(studentDemographicsDistrict);
        let paragraph = "";
        for (let i = 0; i < keys.length; i++) {
            paragraph = paragraph + `${keys[i]}: ${values[i]}`+ "<br>" 
        }
        return <p dangerouslySetInnerHTML={{ __html: paragraph }} />;
    }

    function displayTeacherNycData() {
        const keys = Object.keys(NycTeacherDemographics);
        const values = Object.values(NycTeacherDemographics);
        let paragraph = "";
        for (let i = 0; i < keys.length; i++) {
            paragraph = paragraph + " " + `${keys[i]}: ${values[i]}`+ "<br>" 
        }
        return <p dangerouslySetInnerHTML={{ __html: paragraph }} />;
    }

    function displayStudentNycData() {
        const keys = Object.keys(NycStudentDemocgrphics);
        const values = Object.values(NycStudentDemocgrphics);
        let paragraph = "";
        for (let i = 0; i < keys.length; i++) {
            paragraph = paragraph + " " + `${keys[i]} ${values[i]}`+ "<br>" 
        }
        return <p dangerouslySetInnerHTML={{ __html: paragraph }} />;
    }

    
    const mapRef: React.RefObject<MapRef> = useRef<MapRef>(null);
    const [areaData, setAreaData] = useState({
        district: "Not selected",
        lat: "",
        lon: ""
    });

    interface AreaDataType {
        district: string,
        lat: string,
        lon: string
    }
    
    const onMapClick = (e: MapLayerMouseEvent, mapRef: RefObject<MapRef>, setAreaData: Dispatch<SetStateAction<AreaDataType>>): void => {
        const bbox: [PointLike, PointLike] = [
          [e.point.x, e.point.y],
          [e.point.x, e.point.y],
        ];
        const clickedFeatures = mapRef.current?.queryRenderedFeatures(bbox);
        if (clickedFeatures !== undefined) {
          const feature: MapboxGeoJSONFeature | undefined = clickedFeatures[0];
          if (feature !== undefined) {
            setAreaData({
                district: feature.properties?.school_dist,
                lat: JSON.stringify(e.lngLat.lat),
                lon: JSON.stringify(e.lngLat.lng)
            });
          } 
        }
    }
    

    const [districtOverlay, setDistrictOverlay] = useState<GeoJSON.FeatureCollection | undefined>(undefined);
    useEffect(() => {
        fetchDistrictOverlay().then(data => setDistrictOverlay(data))
        fetchNycData()
      }, [schoolLat, schoolLon]);

    return (
        <div className="App">
            <div className="Map-Div" aria-label="Map-Div">
                <Map
                ref={mapRef}
                mapboxAccessToken={key}
                longitude={viewState.longitude}
                latitude={viewState.latitude}
                zoom={viewState.zoom}
                maxBounds={[ -74.68978783064304, 40.360482232298864, -73.26989666739372, 41  ]}
                onMove={event => setViewState(event.viewState)}
                onClick={(ev: MapLayerMouseEvent) => {                
                    onMapClick(ev, mapRef, setAreaData); 
                    fetchDistrictData(areaData.district);
                    setDistrict(areaData.district);
                }}

                style={{marginLeft: "400px", width: "500", height: "500px"}}
                mapStyle={'mapbox://styles/mapbox/dark-v11'}
                >
                {schools.map(school => (
                    <Marker
                        longitude={parseFloat(school.location_1.longitude)} 
                        latitude={parseFloat(school.location_1.latitude)}
                        onClick={() => {
                            setSchoolLat(parseFloat(school.location_1.latitude));
                            setSchoolLon(parseFloat(school.location_1.longitude));
                            setDistrict(school.geographical_district_code);
                            
                            async function getData() {
                                await fetchSchoolNames(school.location_1.latitude, school.location_1.longitude);
                                await fetchDistrictData(school.geographical_district_code);
                            }
                            getData()
                        }}
                    >
                        <img src="./pin.png" id = "marker-image"></img>
                    </Marker>
                ))}
                    <Source
                        id="district_data"
                        type="geojson"
                        data={districtOverlay} 
                    >
                        <Layer
                            id={districtLayer.id} type={districtLayer.type} paint={districtLayer.paint}
                        />
                    </Source>
                </Map>
            </div>
                <div className = "sideBar" id = "" aria-label= "side-bar">
                    <h1>NYC School Data</h1>
                    <h3> <u>NYC Teacher Demographics:</u></h3>
                    {displayTeacherNycData()}
                    <h3> <u>NYC Student Demographics:</u></h3>
                    {displayStudentNycData()}
                    <h3> <u>NYC Absences:</u> {NycAbsences}</h3>
                    <h3> <u>NYC Graduation Rates:</u> {NycGraduationRates}</h3>
                    <h3> <u>NYC Funding:</u> {NycFunding}</h3><br />
                </div>
                <div className="districtLabel" aria-label = "district-label">
                    <h3>District Number: {areaData.district}</h3>
                    <u>District Teacher Demographics: </u>
                    {displayTeacherDistrictData()}
                    <u>District Student Demographics: </u>
                    {displayStudentDistrictData()}
                    <u>District Absences:</u> <br />{absencesDistrict}<br /><br />
                    <u>District Graduation Rates: </u><br />{graduationRatesDistrict}<br /><br />
                    <u>District Funding:</u> <br />{fundingDistrict} 
                </div>

                <div className="schoolLabel" aria-label = "location-label">
                    {displaySchoolsAll()}
                </div>

                
                <div className="sources">
                    <Link to="/resources">
                    <button className="src-button" aria-label="source-button">Sources
                    </button>
                    </Link> 
                </div>
            </div>
    )
}

