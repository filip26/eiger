import React from 'react';

import { makeStyles } from '@material-ui/core/styles';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';

const useStyles = makeStyles((theme) => ({

}));

export default function TypeSelector(props) {

    const classes = useStyles();
    
    const handleChange = (event) => {
        
        const type = event.target.value.split("+");
        
        props.onChange && props.onChange({model: type[0], format: type[1]});        
    };

    return (
        <FormControl className={classes.formControl} variant="outlined">
            <InputLabel id={props.labelId}>{props.label}</InputLabel>
            <Select
                labelId={props.labelId}
                value={props.value.model + "+" + props.value.format}
                onChange={handleChange}
                label={props.label}
                className={classes.mediaTypeSelector}
                >
                {props.options.map((o) => (
                    <MenuItem value={o.model + "+" + o.format}>{o.label}</MenuItem>
                ))}

            </Select>
        </FormControl>
    );
}
