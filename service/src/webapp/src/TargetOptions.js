import React from 'react';

import { makeStyles } from '@material-ui/core/styles';
import FormControl from '@material-ui/core/FormControl';

import FormControlLabel from '@material-ui/core/FormControlLabel';

import FormGroup from '@material-ui/core/FormGroup';
import Checkbox from '@material-ui/core/Checkbox';

const useStyles = makeStyles((theme) => ({
}));

export default function TargetOptions(props) {
    
    const classes = useStyles();

    const handlePrettyChange = event => {
        props.onChange && props.onChange({pretty: event.target.checked});
    }

    const handleVerboseChange = event => {
        props.onChange && props.onChange({verbose: event.target.checked});
    }

    return (
        <FormControl className={classes.formControl}>
            <FormGroup row>
                <FormControlLabel
                    control={
                      <Checkbox            
                        className={classes.targetOption}
                        checked={props.pretty}
                        name="pretty"
                        color="primary"
                        onChange={handlePrettyChange}
                      />}
                    label="Pretty"
                    />
                <FormControlLabel
                    control={
                        <Checkbox
                            className={classes.targetOption}                        
                            checked={props.verbose} 
                            color="primary" 
                            name="checkedA"
                            onChange={handleVerboseChange}
                            />}
                    label="Verbose"
                    />
            </FormGroup>
        </FormControl>
        );
}
