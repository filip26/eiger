import React from 'react';

import { Button, Paper, Grid, Container } from '@material-ui/core/';

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
            {model: "oas", format: "yaml", label: "OpenAPI v3 (YAML)", mediaType: ""}
            ];
            
const targetTypes = [
            {model: "alps", format: "xml", label: "ALPS (XML)", mediaType: "application/alps+xml"}, 
            {model: "alps", format: "json", label: "ALPS (JSON)", mediaType: "application/alpsjson"}, 
            {model: "alps", format: "yaml", label: "ALPS (YAML)", mediaType: "application/alps+yaml"}
            ];

class Transformer extends React.Component {

    constructor(props) {
        super(props);
        
        this.state = {
            verbose: false,
            pretty: true,
            sourceType: sourceTypes[2],
            targetType: targetTypes[0],
        }
    }
    
    handleTargetOptionsChange = state => {
        this.setState(state); 
    }
    
    handleTypeChange = (key, type) => {
        this.setState({[key]: type});
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
                                <Code type={this.state.sourceType}/>
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

                <Button variant="contained" color="primary" fullWidth size="large">Process</Button>

                <Paper className={classes.paper} elevation={1}>
                    <Code type={this.state.targetType} readOnly/>
                </Paper>

            </Container>
        </React.Fragment>
        );
    }
}

export default withStyles(styles)(Transformer);