import React from 'react';
import { responsiveFontSizes, ThemeProvider } from '@material-ui/core/styles';
import { unstable_createMuiStrictModeTheme as createMuiTheme } from '@material-ui/core';
import CssBaseline from '@material-ui/core/CssBaseline';
import amber from '@material-ui/core/colors/amber';
import lightBlue from '@material-ui/core/colors/lightBlue';

import Header from './Header';
import Footer from './Footer';
import Transformer from './Transformer';

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
            default: "#121212"
        }
    }
}));

export default function App() {

    return (
        <React.Fragment>
            <ThemeProvider theme={theme}>
                <CssBaseline />
                <Header />
                <main>
                    <Transformer />
                </main>
                <Footer />
            </ThemeProvider>
        </React.Fragment>
  );
}
