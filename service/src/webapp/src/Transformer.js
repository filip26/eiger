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
            {model: "alps", format: "xml", label: "ALPS (XML)"}, 
            {model: "alps", format: "json", label: "ALPS (JSON)"}, 
            {model: "oas", format: "yaml", label: "OpenAPI v3 (YAML)"}
            ];
            
const targetTypes = [
            {model: "alps", format: "xml", label: "ALPS (XML)"}, 
            {model: "alps", format: "json", label: "ALPS (JSON)"}, 
            {model: "alps", format: "yaml", label: "ALPS (YAML)"}
            ];

class Transformer extends React.Component {

    constructor(props) {
        super(props);
        
        this.state = {
            verbose: false,
            pretty: true,
            sourceType: { model: "oas", format: "yaml" },
            targetType: { model: "alps", format: "xml" },
        }
    }
    
    handleTargetOptionsChange = state => {
        this.setState(state); 
    }
    
    handleSourceTypeChange = type => {
        this.setState({sourceType: type});
    }

    handleTargetTypeChange = type => {
        this.setState({targetType: type});
    }

    render() {
        const { classes } = this.props;

        return (
        <React.Fragment>
            <Welcome />

            <Container maxWidth="md">

                <Paper className={classes.paper} elevation={1} square>
                    <Grid>
                        <Grid item>
                            <div className={classes.control}>
                                <TypeSelector 
                                    value={this.state.sourceType} 
                                    onChange={this.handleSourceTypeChange}
                                    labelId="source-select-label"
                                    label="Source"
                                    options={sourceTypes}
                                    />
                            </div>
                        </Grid>
                        <Grid item>
                            <div className={classes.control}>
                                <Code />
                            </div>
                        </Grid>
                    </Grid>
                </Paper>

                <Paper className={classes.paper} elevation={1} square>
                    <Grid container spacing={4}>
                        <Grid item>
                                <TypeSelector 
                                    value={this.state.targetType} 
                                    onChange={this.handleTargetTypeChange}
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

                <Paper className={classes.paper} elevation={1} square>
                    <Code />
                </Paper>

            </Container>
        </React.Fragment>
        );
    }
}

export default withStyles(styles)(Transformer);