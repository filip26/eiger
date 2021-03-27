import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { IconButton, AppBar, Toolbar, Typography } from '@material-ui/core';
import GitHubIcon from '@material-ui/icons/GitHub';
import grey from '@material-ui/core/colors/grey';
import FilterHdrIcon from '@material-ui/icons/FilterHdr';

const useStyles = makeStyles((theme) => ({
    appBar: {
        backgroundColor: "#121212",
        color: grey[200],
    },
    icon: {
        marginRight: theme.spacing(1),
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
        <AppBar position="relative" className={classes.appBar}>
            <Toolbar>
                <FilterHdrIcon className={classes.icon}/>
                <Typography variant="h6" color="inherit" noWrap className={classes.appName}>
                
                    Eiger Service <sup className={classes.version}>v0.4.9</sup>
                </Typography>

                <div className={classes.padding} />

                <IconButton
                    target="_blank"
                    href="https://github.com/filip26/eiger"
                    >
                    <GitHubIcon />
                </IconButton>
            </Toolbar>
        </AppBar>
        );
};
