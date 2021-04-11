import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Grid, Paper, IconButton } from '@material-ui/core';
import GitHubIcon from '@material-ui/icons/GitHub';
import GitHubButton from 'react-github-btn'

const useStyles = makeStyles((theme) => ({
  footer: {

   margin: theme.spacing(8, 0, 0, 0),
  backgroundColor: theme.palette.common.black,
    },
  links: {
     padding: theme.spacing(2, 0, 4, 0),

  },
icon: {
color: theme.palette.text.secondary,
}
}));

export default function Footer() {

    const classes = useStyles();

    return (
        <Paper component="footer" className={classes.footer} elevation={0} >

              <Grid container spacing={2} className={classes.links}>
                <Grid item xs={12} align="center">

                <IconButton
                    target="_blank"
                    href="https://github.com/filip26/eiger"
                    aria-label="GitHub Repository"
                    rel="noopener"
	            className={classes.icon}
                    >
                    <GitHubIcon fontSize="large" 
	    />
                </IconButton>


                </Grid>
                <Grid item xs={6} align="right">
                    <GitHubButton
                        href="https://github.com/filip26/eiger"
                        data-icon="octicon-star"
                        data-size="large"
                        data-show-count="true"
                        aria-label="Star filip26/eiger on GitHub"
                        >Star</GitHubButton>
                </Grid>
                <Grid item xs={6}>
                    <GitHubButton
                        href="https://github.com/sponsors/filip26"
                        data-icon="octicon-heart"
                        data-size="large"
                        aria-label="Sponsor @filip26 on GitHub"
                        >Sponsor</GitHubButton>
                </Grid>
              </Grid>
 
 
        </Paper>
        );
};
