import React from "react";
import {
  createBrowserRouter,
  RouterProvider,
  Route,
} from "react-router-dom";
import {MapBox} from "../Map"
import ResourcesPage from "../ResourcesPage";

export const router = createBrowserRouter([
    {
      path: "/",
      element: <MapBox/>,
      
    },
    {
      path: "/resources",
      element: <ResourcesPage/>
    }
  ]);