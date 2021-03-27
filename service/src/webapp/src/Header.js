import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { IconButton, AppBar, Toolbar, Typography } from '@material-ui/core';
import GitHubIcon from '@material-ui/icons/GitHub';
import grey from '@material-ui/core/colors/grey';

const useStyles = makeStyles((theme) => ({
    appBar: {
        backgroundColor: grey[900],
    },
    padding: {
        flexGrow: 1,
    },
}));

export default function Header() {

    const classes = useStyles();

    return (
        <AppBar position="relative" className={classes.appBar}>
            <Toolbar>
                <Typography variant="h6" color="inherit" noWrap>
                    Eiger Service
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
