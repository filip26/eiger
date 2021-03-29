import React from 'react';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles((theme) => ({
    footer: {
        margin: `${theme.spacing(8)}px auto`,

    },
}));

export default function Footer() {

    const classes = useStyles();

    return (
        <footer className={classes.footer}>

        </footer>
        );
};