import React from 'react';
import { render, screen } from '@testing-library/react';
import App from '../App';
import '@testing-library/jest-dom'
import { createPublicKey } from 'crypto';
import userEvent from '@testing-library/user-event'

test('renders map page components correctly', () => {
    render(<App />)
  
    const mapDiv = screen.getByLabelText("Map-Div")
    expect(mapDiv).toBeInTheDocument()
  
    const sideBar = screen.getByLabelText("side-bar")
    expect(sideBar).toBeInTheDocument()
  
    const locationLabel = screen.getByLabelText("location-label")
    expect(locationLabel).toBeInTheDocument()

    const resourcesButton = screen.getByLabelText("source-button")
    expect(resourcesButton).toBeInTheDocument()
  });

  test('renders resource page components correctly', () => {
    render(<App />)
  
    const resourcesButton = screen.getByLabelText("source-button")
    userEvent.click(resourcesButton)

    const mapButton = screen.getByLabelText("to-map-button")
    expect(mapButton).toBeInTheDocument()

    const Resources = screen.getByLabelText("resources")
    expect(Resources).toBeInTheDocument()
  });