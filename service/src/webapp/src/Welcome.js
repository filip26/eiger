import React from 'react';

import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { makeStyles } from '@material-ui/core/styles';
import GitHubButton from 'react-github-btn'

import Link from '@material-ui/core/Link';

const useStyles = makeStyles((theme) => ({
    content: {
        padding: theme.spacing(8, 0, 4),
    },
    name: {
        fontFamily: "'Montserrat', Roboto, sans-serif;",
    }
}));

export default function Welcome() {

  const classes = useStyles();

  return (
      <div className={classes.content}>
        <Container maxWidth="md">

            <Typography component="h1" variant="h3" align="center" color="textPrimary" className={classes.name} gutterBottom>Transformer</Typography>

            <Typography variant="h6" align="center" color="textSecondary" paragraph >
                <Link href="https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps" color="inherit" target="_default">
                    Application-Level Profile Semantics (ALPS)
                </Link>
                ,&nbsp;
                <Link href="https://www.openapis.org/" color="inherit" target="_default">OpenAPI Specification (OAS)</Link>
            </Typography>

            <div className={classes.heroButtons}>
              <Grid container spacing={2} justify="center">
                <Grid item>
                    <GitHubButton
                        href="https://github.com/filip26/eiger"
                        data-icon="octicon-star"
                        data-size="large"
                        data-show-count="true"
                        aria-label="Star filip26/eiger on GitHub"
                        >Star</GitHubButton>
                </Grid>
                <Grid item>
                    <GitHubButton
                        href="https://github.com/sponsors/filip26"
                        data-icon="octicon-heart"
                        data-size="large"
                        aria-label="Sponsor @filip26 on GitHub"
                        >Sponsor</GitHubButton>
                </Grid>
              </Grid>
            </div>
        </Container>
      </div>
  );
}
