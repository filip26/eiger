import React from 'react';

import { responsiveFontSizes, ThemeProvider } from '@material-ui/core/styles';
import { unstable_createMuiStrictModeTheme as createMuiTheme } from '@material-ui/core';
import NoSsr from '@material-ui/core/NoSsr'

import CssBaseline from '@material-ui/core/CssBaseline';
import amber from '@material-ui/core/colors/amber';
import lightBlue from '@material-ui/core/colors/lightBlue';

import Header from './Header';
import Footer from './Footer';
import Transformer from './Transformer';

import "@fontsource/roboto/300.css";
import "@fontsource/roboto/400.css";
import "@fontsource/roboto/500.css";

import "@fontsource/montserrat/200.css";
import "@fontsource/montserrat/400.css";
import "@fontsource/montserrat/500.css";
import "@fontsource/montserrat/800.css";

let theme = responsiveFontSizes(createMuiTheme( {
  palette: {
    type: 'dark',
    primary: {
      main: lightBlue[600],
    },
    secondary: {
      main: amber.A200,
    },
    background: {
      default: "#131312",
      paper: "#212121",
    },
  }
}));

export default function App() {
    return (
      <NoSsr>
        <React.Fragment>
          <ThemeProvider theme={theme}>
            <CssBaseline/>
            <Header/>
            <main>
              <Transformer/>
            </main>
            <Footer/>
          </ThemeProvider>
        </React.Fragment>
      </NoSsr>
  );
}
