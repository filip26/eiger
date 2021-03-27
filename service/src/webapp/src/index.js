import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import reportWebVitals from './reportWebVitals';

require('codemirror/lib/codemirror.css');
require('codemirror/theme/material-darker.css');

require('codemirror/mode/xml/xml.js');
require('codemirror/mode/yaml/yaml.js');
require('codemirror/mode/javascript/javascript.js');


ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
