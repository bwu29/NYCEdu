import {FeatureCollection} from "geojson";
import {FillLayer, LineLayer} from "react-map-gl";
import districts from './datasources/districts.json'

function isFeatureCollection(json: any): json is FeatureCollection {
    return json.type === "FeatureCollection"
}

export async function fetchDistrictOverlay() {

    if (isFeatureCollection(districts)) {
        return districts;
    }
    return undefined;
  }

export const districtLayer: FillLayer = {
    id: 'geo_data',
    type: 'fill',
    paint: {
        'fill-color': [
            'match',
            ['get', 'school_dist'],
            "1",
            "#FCB444",
            "2",
            "#04b8cc",
            "3",
            "#e9ed0e",
            "4",
            "#d11d1d",
            "5",
            "#5bcc04",
            "6",
            "#FCB444",
            "7",
            "#e9ed0e",
            "8",
            "#d11d1d",
            "9",
            "#5bcc04",
            "10",
            "#04b8cc",
            "11",
            "#5bcc04",
            "12",
            "#FCB444",
            "13",
            "#04b8cc",
            "14",
            "#e9ed0e",
            "15",
            "#5bcc04",
            "16",
            "#FCB444",
            "17",
            "#e9ed0e",
            "18",
            "#FCB444",
            "19",
            "#5bcc04",
            "20",
            "#d11d1d",
            "21",
            "#5bcc04",
            "22",
            "#04b8cc",
            "23",
            "#e9ed0e",
            "24",
            "#d11d1d",
            "25",
            "#e9ed0e",
            "26",
            "#04b8cc",
            "27",
            "#e9ed0e",
            "28",
            "#FCB444",
            "29",
            "#d11d1d",
            "30",
            "#FCB444",
            "31",
            "#5bcc04",
            "32",
            "#04b8cc",
            /* other */ '#ccc'
        ],

        'fill-outline-color': "green",
        'fill-opacity': 0.33
    }
}
