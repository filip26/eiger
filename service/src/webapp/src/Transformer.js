import React from 'react';

import { Button, Paper, Grid, Container } from '@material-ui/core/';

import { withStyles } from '@material-ui/core/styles';

import Welcome from './Welcome';
import SourceTypeSelector from './SourceTypeSelector';
import TargetTypeSelector from './TargetTypeSelector';
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

class Transformer extends React.Component {

    constructor(props) {
        super(props);
        
        this.state = {
            verbose: false,
            pretty: true,
        }
    }
    
    handleTargetOptionsChange = (state) => {
        this.setState(state); 
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
                                <SourceTypeSelector />
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
                            <TargetTypeSelector />
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