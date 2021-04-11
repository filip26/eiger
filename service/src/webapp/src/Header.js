import React from 'react';

import {
    IconButton,
    AppBar,
    Toolbar,
    Typography
    } from '@material-ui/core';

import { makeStyles } from '@material-ui/core/styles';
import grey from '@material-ui/core/colors/grey';

import FilterHdrIcon from '@material-ui/icons/FilterHdr';

const useStyles = makeStyles((theme) => ({
    bar: {
      backgroundColor: theme.palette.background.default,
    },
    icon: {
      color: theme.palette.text.secondary,
    },
    name: {
      fontFamily: 'Montserrat, Roboto, sans-serif',
    },
    padding: {
        flexGrow: 1,
    },
    version: {
        fontSize: '10px',
        color: grey[400],
    },
}));

export default function Header() {

    const classes = useStyles();

    return (
        <AppBar position="fixed" className={classes.bar}>
            <Toolbar>
               <IconButton
                    target="_blank"
                    href="https://en.wikipedia.org/wiki/Eiger"
                    aria-label="Eiger Mountain"
                    rel="noopener"
                    >
                    <FilterHdrIcon className={classes.icon} fontSize="large"/>
                </IconButton>
                
                <Typography variant="h5" component="div" color="inherit" noWrap className={classes.name}>
                    EIGER <sup className={classes.version}>SNAPSHOT</sup>
                </Typography>

                <div className={classes.padding} />

           </Toolbar>
        </AppBar>
        );
};
