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
import Code from './Code';

const styles = theme => ({
    paper: {
        margin: `${theme.spacing(1)}px auto`,
        padding: theme.spacing(2),
    },
    mediaTypeSelector: {
        minWidth: theme.spacing(25),
    },
    control: {
        margin: `${theme.spacing(1)}px auto`,
    },
});

const sourceTypes = [
            {model: "alps", format: "xml", label: "ALPS (XML)", mediaType: "application/alps+xml"},
            {model: "alps", format: "json", label: "ALPS (JSON)", mediaType: "application/alps+json"},
            {model: "oas", format: "yaml", label: "OpenAPI v3 (YAML)", mediaType: "application/vnd.oai.openapi"}
            ];

const targetTypes = [
            {model: "alps", format: "xml", label: "ALPS (XML)", mediaType: "application/alps+xml"},
            {model: "alps", format: "json", label: "ALPS (JSON)", mediaType: "application/alpsjson"},
            {model: "alps", format: "yaml", label: "ALPS (YAML)", mediaType: "application/alps+yaml"}
            ];

const exampleCode = `openapi: 3.0.2
info:
  title: Swagger Petstore - OpenAPI 3.0
`;

class Transformer extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            verbose: false,
            pretty: true,
            sourceType: sourceTypes[2],
            targetType: targetTypes[0],
        }

         this.sourceRef = React.createRef();
    }

    handleTargetOptionsChange = state => {
        this.setState(state);
    }

    handleTypeChange = (key, type) => {
        this.setState({[key]: type});
    }
    
    contentTypeToFormat = contentType => {
        if (contentType.endsWith("json")) {
            return "json";
        }
        if (contentType.endsWith("yaml")) {
            return "yaml";
        }
        if (contentType.endsWith("xml")) {
            return "xml";
        }
    }

    handleProcessing = event => {

        this.transform(this.state.sourceType, this.sourceRef.current.value(), this.state.targetType, {})
            .then(async response => {

                if (response.status !== 200) {
                    this.setState({error: "Service Error: " + response.statusText, response: null});
                    return;    
                }

                return response.text().then(text => {
                    this.setState({
                        response: text,
                        responseFormat: this.contentTypeToFormat(response.headers.get('content-type')),
                        error: null,
                    });                           
                })
                
            }).catch(error => {
                console.error(error);
                this.setState({error: "An internal application error has occurred.", response: null});
            });
    }
    
    transform = async (sourceType, source, targetType, options) => {
        return fetch('/transform', {
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
                                    onChange={this.handleTypeChange.bind(this, "sourceType")}
                                    labelId="source-select-label"
                                    label="Source"
                                    options={sourceTypes}
                                    />
                            </div>
                        </Grid>
                        <Grid item>
                            <div className={classes.control}>
                                <Code
                                    type={this.state.sourceType.format}
                                    value={exampleCode}
                                    ref={this.sourceRef}
                                    />
                            </div>
                        </Grid>
                    </Grid>
                </Paper>

                <Paper className={classes.paper} elevation={1}>
                    <Grid container spacing={4}>
                        <Grid item>
                                <TypeSelector
                                    value={this.state.targetType}
                                    onChange={this.handleTypeChange.bind(this, "targetType")}
                                    labelId="target-select-label"
                                    label="Target"
                                    options={targetTypes}
                                    />
                        </Grid>
                        <Grid item>
                            <div className={classes.control}>
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
                    >Process</Button>

                {this.state.response &&
                    <Paper className={classes.paper} elevation={1}>
                        <Code type={this.state.responseFormat} readOnly value={this.state.response}/>
                    </Paper>
                    }
                {this.state.error &&                    
                    <MuiAlert 
                        className={classes.paper} 
                        elevation={1} 
                        variant="filled" 
                        severity="error"
                        >{this.state.error}</MuiAlert>
                    }

            </Container>
        </React.Fragment>
        );
    }
}

export default withStyles(styles)(Transformer);