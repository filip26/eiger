import React from 'react';

import {
    Button,
    Paper,
    Grid,
    Container,
    LinearProgress,
    TextField,
    Collapse,
    Fade,
    } from '@material-ui/core/';

import MuiAlert from '@material-ui/lab/Alert';
import { withStyles } from '@material-ui/core/styles';

import Welcome from './Welcome';
import TypeSelector from './TypeSelector';
import TargetOptions from './TargetOptions';
import Editor from './Editor';
import Viewer from './Viewer';

import transform from './transform';

import openApi from './api.oas.yaml';
import mediaTypes from './media-types';

const sourceTypes = [ mediaTypes[0], mediaTypes[1], mediaTypes[2] ];
const targetTypes = [ mediaTypes[0], mediaTypes[1], mediaTypes[3], mediaTypes[4], mediaTypes[2], mediaTypes[5], mediaTypes[6] ];

const styles = theme => ({
  paper: {
    margin: `${theme.spacing(1)}px auto`,
    padding: theme.spacing(1.75, 1, 1.25, 1),
  },
  code: {
    margin: `${theme.spacing(1)}px auto`,
    padding: theme.spacing(1, 0.5, 1, 1),
  },
  errorContainer: {
    margin: `${theme.spacing(1)}px auto`,
  },
  error: {
    padding: theme.spacing(1.5),
  },
  process: {
    padding: theme.spacing(1.85, 0),
  }
});

class Transformer extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            verbose: false,
            pretty: true,
            sourceType: sourceTypes[2],
            targetType: targetTypes[0],
            processing: false,
            source: "",
            base: "/",
        }
    }
    
    componentDidMount() {
        fetch(openApi, { method: 'GET', })
                  .then(async response => {
                      if (response.status === 200) {
                          const source = await response.text();
                          this.setState({ source: source });
                      }
                  });
    }

    handleTargetOptionsChange = state => {
        this.setState(state);
    }

    handleStateChange = (key, value) => {
        this.setState({[key]: value});
    }

    handleProcessing = () => {
        this.setState({processing: true}, () => {

            transform(this.state.sourceType, this.state.source, this.state.targetType, {verbose: this.state.verbose, pretty: this.state.pretty, base: this.state.base})

                .then(async response => {

                    let state = {error: null, processing: false};

                    if (response.status !== 200) {
                        state.error = response.status + " " + response.statusText;
                    }

                    return response.text().then(text => {

                        state.response = text;
                        state.responseMediaType = response.headers.get('content-type') || "text/plain";

                        this.setState(state);
                    })

                }).catch(error => {
                    console.error(error);
                    this.setState({error: "An internal application error has occurred.", response: null, processing: false});
                });
        });
    }

    render() {
        const { classes } = this.props;

        return (
          <React.Fragment>
            <Welcome />
            
            <Container maxWidth="lg">
              <Paper className={classes.paper} elevation={2}>
                <Grid container spacing={2} alignItems="center">
                  <Grid item md={3} sm={5} xs={12}>
                      <TypeSelector
                          value={this.state.sourceType}
                          onChange={this.handleStateChange.bind(this, "sourceType")}
                          labelId="source-select-label"
                          label="Source"
                          options={sourceTypes}
                          />
                  </Grid>
                  <Grid item md={9} sm={7} xs={12}>
                      <TextField
                          id="base-uri-input"
                          fullWidth
                          label="Base URI"
                          variant="outlined"
                          value={this.state.base}
                          onChange={event => this.handleStateChange("base", event.target.value)}
                          />
                  </Grid>
                </Grid>
              </Paper>
                <Paper className={classes.code} elevation={2}>
                  <Editor
                      type={this.state.sourceType.format}
                      value={this.state.source}
                      onChange={this.handleStateChange.bind(this, "source")}
                      />
                </Paper>

                <Paper className={classes.paper} elevation={2}>
                    <Grid container spacing={2} alignItems="center">
                        <Grid item md={3} sm={5} xs={12}>
                            <TypeSelector
                                value={this.state.targetType}
                                onChange={this.handleStateChange.bind(this, "targetType")}
                                labelId="target-select-label"
                                label="Target"
                                options={targetTypes}
                                />
                        </Grid>
                        <Grid item md={9} sm={7} xs={12}>
                            <TargetOptions
                                pretty={this.state.pretty || (this.state.targetType.prettyDisabled != null && this.state.targetType.prettyDisabled)}
                                prettyDisabled={this.state.targetType.prettyDisabled}
                                verbose={this.state.verbose}
                                verboseDisabled={this.state.targetType.verbose != null && !this.state.targetType.verbose}
                                onChange={this.handleTargetOptionsChange}
                                />
                        </Grid>
                    </Grid>
                </Paper>

                <Button
                    variant="contained"
                    color="primary"
                    fullWidth
                    size="large"
                    onClick={this.handleProcessing}
                    disabled={this.state.processing}
                    className={classes.process}
                    >{this.state.processing ? "Processing" : "Process"}</Button>

                <Collapse in={this.state.processing} timeout={250} mountOnEnter unmountOnExit>
                    <LinearProgress color="secondary"/>
                </Collapse>

                <Collapse in={this.state.error != null} timeout={750} className={classes.errorContainer} mountOnEnter unmountOnExit>
                    <MuiAlert
                        className={classes.error}
                        elevation={2}
                        variant="filled"
                        severity="error"
                        >{this.state.error}</MuiAlert>
                </Collapse>

                <Fade in={this.state.response != null} timeout={1000} mountOnEnter unmountOnExit>
                    <Paper elevation={2} className={classes.code}>
                        <Viewer mediaType={this.state.responseMediaType} readOnly value={this.state.response}/>
                    </Paper>
                </Fade>

            </Container>
        </React.Fragment>
        );
    }
}

export default withStyles(styles)(Transformer);
