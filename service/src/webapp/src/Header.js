import React from 'react';

import {
    IconButton,
    AppBar,
    Toolbar,
    Typography
    } from '@material-ui/core';

import { makeStyles } from '@material-ui/core/styles';
import grey from '@material-ui/core/colors/grey';

import GitHubIcon from '@material-ui/icons/GitHub';
import FilterHdrIcon from '@material-ui/icons/FilterHdr';

const useStyles = makeStyles((theme) => ({
    appBar: {
        backgroundColor: grey[900],
        color: grey[200],
    },
    icon: {
    },
    appName: {
        fontFamily: "'Montserrat', Roboto, sans-serif;",
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
        <AppBar position="fixed" className={classes.appBar}>
            <Toolbar variant="dense">
               <IconButton
                    target="_blank"
                    href="https://en.wikipedia.org/wiki/Eiger"
                    aria-label="Eiger Mountain"
                    rel="noopener"
                    >
                    <FilterHdrIcon className={classes.icon}/>
                </IconButton>
                
                <Typography variant="h6" color="inherit" noWrap className={classes.appName}>
                    Eiger <sup className={classes.version}>v0.4.9</sup>
                </Typography>

                <div className={classes.padding} />

                <IconButton
                    target="_blank"
                    href="https://github.com/filip26/eiger"
                    aria-label="GitHub Repository"
                    rel="noopener"
                    >
                    <GitHubIcon/>
                </IconButton>
            </Toolbar>
        </AppBar>
        );
};
