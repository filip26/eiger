import React from 'react';

import {
    InputLabel,
    MenuItem,
    FormControl,
    Select,
    } from '@material-ui/core';

export default function TypeSelector(props) {

    const handleChange =  event => {

        const type = event.target.value.split("+");

        props.onChange && props.onChange(props.options.find(o => o.model === type[0] && o.format === type[1]));
    };

    return (
        <FormControl variant="outlined" fullWidth>
            <InputLabel id={props.labelId}>{props.label}</InputLabel>
            <Select
                labelId={props.labelId}
                value={props.value.model + "+" + props.value.format}
                onChange={handleChange}
                label={props.label}
                >
                {props.options && props.options.map(o => (
                    <MenuItem key={o.model + o.format} value={o.model + "+" + o.format}>{o.label}</MenuItem>
                ))}

            </Select>
        </FormControl>
    );
}
