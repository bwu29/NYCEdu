import React from 'react';
import {MapBox} from './Map';
import './css/App.css';
import { BrowserRouter, Route, RouterProvider, Routes } from "react-router-dom";
import { router } from './config/AllRoutes';

function App() {
  
  return (
    //  <div className="App">
    //    <h1 id="App-header">
    //     NYC Schooling Data
    //   </h1>
    //   <MapBox />
    // </div> 

    <RouterProvider router={router} />

  );
}

    

export default App;
