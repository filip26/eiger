import React from 'react';
import { makeStyles } from '@material-ui/core/styles';

import { 
    Grid,  
    Typography, 
    Link, 
    Container 
    } from '@material-ui/core';
    
import GitHubButton from 'react-github-btn'

const useStyles = makeStyles((theme) => ({
  footer: {
    margin: theme.spacing(8, 0, 0, 0),
    backgroundColor: theme.palette.common.black,
  },
  links: {
    padding: theme.spacing(6, 0, 2, 0),
  },
  link: {
    padding: theme.spacing(0, 1, 0, 1),
  },
  powered: {

  }
}));

export default function Footer() {

    const classes = useStyles();

    return (
        <Container component="footer" className={classes.footer} elevation={0} >
          <Grid container spacing={3} className={classes.links}>


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
            
            <Grid item xs={12} align="center">
              <Typography paragraph className={classes.powered} color="textSecondary" variant="body1">
                Powered by
                              <Link
                              color="textPrimary"
                target="_blank"
                href="https://github.com/filip26/eiger"
                aria-label="Eiger GitHub Repository"
                rel="noopener"
                className={classes.link}
                >@filip26/eiger</Link>
                and 
               <Link
               color="textPrimary"
                target="_blank"
                href="https://github.com/mamund/alps-unified"
                aria-label="ALPS Unified GitHub Repository"
                rel="noopener"
                className={classes.link}
                >
                @mamund/alps-unified</Link>

              </Typography>

            </Grid>

          </Grid>
        </Container>
        );
};