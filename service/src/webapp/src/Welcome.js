import React from 'react';

import {
    Typography,
    Container,
    } from '@material-ui/core';

import { makeStyles } from '@material-ui/core/styles';

import Link from '@material-ui/core/Link';

const useStyles = makeStyles((theme) => ({
    content: {
        padding: theme.spacing(14, 0, 4, 0),
    },
    name: {
        fontFamily: "'Montserrat', Roboto, sans-serif;",
        fontWeight: 800,
    }
}));

export default function Welcome() {

  const classes = useStyles();

  return (
      <div className={classes.content}>
        <Container maxWidth="md">

            <Typography component="h1" variant="h3" align="center" color="textSecondary" className={classes.name} gutterBottom>Transformer</Typography>

            <Typography variant="body1" align="center" color="textSecondary" paragraph>
                <Link href="https://tools.ietf.org/html/draft-amundsen-richardson-foster-alps" color="inherit" target="_default">
                    Application-Level Profile Semantics (ALPS)
                </Link>
                ,&nbsp;
                <Link href="https://www.openapis.org/" color="inherit" target="_default">OpenAPI Specification (OAS)</Link>
            </Typography>

        </Container>
      </div>
  );
}
