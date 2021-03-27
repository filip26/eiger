import React from 'react';

import { Button, Paper, Grid, Container } from '@material-ui/core/';

import { withStyles } from '@material-ui/core/styles';

import Welcome from './Welcome';
import SourceTypeSelector from './SourceTypeSelector';
import SourceEditor from './SourceEditor';
import TargetTypeSelector from './TargetTypeSelector';
import TargetViewer from './TargetViewer';
import TargetOptions from './TargetOptions';

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
                                <SourceEditor />
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
                            <TargetOptions />
                            </div>
                        </Grid>
                    </Grid>
                </Paper>
                
                <Button variant="contained" color="primary" fullWidth size="large">Process</Button>
    
                <Paper className={classes.paper} elevation={1} square>
                    <TargetViewer />
                </Paper>
            
            </Container>
        </React.Fragment>
        );
    }
}

export default withStyles(styles)(Transformer);