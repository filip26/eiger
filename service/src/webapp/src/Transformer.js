import React from 'react';

import {
    Button,
    Paper,
    Grid,
    Container,
    LinearProgress,
    } from '@material-ui/core/';
import MuiAlert from '@material-ui/lab/Alert';
import { withStyles } from '@material-ui/core/styles';

import Welcome from './Welcome';
import TypeSelector from './TypeSelector';
import TargetOptions from './TargetOptions';
import Editor from './Editor';
import Viewer from './Viewer';

const sourceTypes = [
            {model: "alps", format: "xml", label: "ALPS (XML)", mediaType: "application/alps+xml"},
            {model: "alps", format: "json", label: "ALPS (JSON)", mediaType: "application/alps+json"},
            {model: "oas", format: "yaml", label: "OpenAPI v3 (YAML)", mediaType: "application/vnd.oai.openapi"}
            ];

const targetTypes = [
            {model: "alps", format: "xml", label: "ALPS (XML)", mediaType: "application/alps+xml"},
            {model: "alps", format: "json", label: "ALPS (JSON)", mediaType: "application/alps+json"},
            {model: "alps", format: "yaml", label: "ALPS (YAML)", mediaType: "application/alps+yaml"}
            ];

const openApi = `openapi: 3.0.2
info:
  title: Eiger Transformer API
  version: 0.4.9
servers:
  - url: 'https://eiger.apicatalog.com'
paths:
  /transform:
    post:
      summary: Transforms ALPS and OAS documents
      operationId: transform
      parameters:
        - name: verbose
          in: query
          description: Adds implicit attributes
          schema:
            type: boolean
        - name: pretty
          in: query
          description: Enables indented output
          schema:
            type: boolean
      requestBody:
        description: An input document represention
        content:
          application/vnd.oai.openapi:
            schema:
              type: string
          application/alps+xml:
            schema:
              type: string
          application/alps+json:
            schema:
              type: string
      responses:
        '200':
          description: An output document representation
          content:
            application/alps+xml:
              schema:
                type: string
            application/alps+json:
              schema:
                type: string
            application/alps+yaml:
              schema:
                type: string
`;


const styles = theme => ({
    paper: {
        margin: `${theme.spacing(1)}px auto`,
        padding: theme.spacing(2),
    },
    mediaTypeSelector: {
        minWidth: theme.spacing(25),
    },
    control: {
        margin: `${theme.spacing(1)}px auto auto auto`,
    },
    second: {
        margin: `${theme.spacing(2)}px auto auto auto`,
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
            source: openApi
        }
    }

    handleTargetOptionsChange = state => {
        this.setState(state);
    }

    handleStateChange = (key, value) => {
        this.setState({[key]: value});
    }

    handleProcessing = () => {
        this.setState({processing: true}, () => {

            this.transform(this.state.sourceType, this.state.source, this.state.targetType, {verbose: this.state.verbose, pretty: this.state.pretty})

                .then(async response => {

                    let state = {error: null, processing: false};

                    if (response.status !== 200) {
                        state.error = response.status + " " + response.statusText;
                    }

                    return response.text().then(text => {

                        state.response = text;
                        state.responseMediaType = response.headers.get('content-type');

                        this.setState(state);
                    })

                }).catch(error => {
                    console.error(error);
                    this.setState({error: "An internal application error has occurred.", response: null, processing: false});
                });
        });
    }

    transform = async (sourceType, source, targetType, options) => {

        const url = '/transform?' + new URLSearchParams(options);

        return fetch(url, {
            method: 'POST',
            headers: {
                'Accept': targetType.mediaType + ";q=1,*/*",
                'Content-Type': sourceType.mediaType,
            },
            referrerPolicy: 'no-referrer',
            cache: 'no-cache',
            body: source,
        });
    }

    render() {
        const { classes } = this.props;

        return (
        <React.Fragment>
            <Welcome />

            <Container maxWidth="lg">

                <Paper className={classes.paper} elevation={1}>
                    <Grid>
                        <Grid item>
                            <div className={classes.control}>
                                <TypeSelector
                                    value={this.state.sourceType}
                                    onChange={this.handleStateChange.bind(this, "sourceType")}
                                    labelId="source-select-label"
                                    label="Source"
                                    options={sourceTypes}
                                    />
                            </div>
                        </Grid>
                        <Grid item>
                            <div className={classes.second}>
                                <Editor
                                    type={this.state.sourceType.format}
                                    value={this.state.source}
                                    onChange={this.handleStateChange.bind(this, "source")}
                                    />
                            </div>
                        </Grid>
                    </Grid>
                </Paper>

                <Paper className={classes.paper} elevation={1}>
                    <Grid container spacing={4}>
                        <Grid item>
                            <div className={classes.control}>
                                <TypeSelector
                                    value={this.state.targetType}
                                    onChange={this.handleStateChange.bind(this, "targetType")}
                                    labelId="target-select-label"
                                    label="Target"
                                    options={targetTypes}
                                    />
                            </div>
                        </Grid>
                        <Grid item>
                            <div className={classes.second}>
                                <TargetOptions pretty={this.state.pretty} verbose={this.state.verbose} onChange={this.handleTargetOptionsChange}/>
                            </div>
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
                    >{this.state.processing ? "Processing" : "Process"}</Button>

                {this.state.processing &&
                    <LinearProgress color="secondary"/>
                }

                {this.state.error &&
                    <MuiAlert
                        className={classes.paper}
                        elevation={1}
                        variant="filled"
                        severity="error"
                        >{this.state.error}</MuiAlert>
                    }

                {this.state.response &&
                    <Paper className={classes.paper} elevation={1}>
                        <Viewer mediaType={this.state.responseMediaType} readOnly value={this.state.response}/>
                    </Paper>
                    }

            </Container>
        </React.Fragment>
        );
    }
}

export default withStyles(styles)(Transformer);