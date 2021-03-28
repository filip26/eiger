import React from 'react';

import {
    FormControl,
    FormControlLabel,
    FormGroup,
    Checkbox,
    } from '@material-ui/core';

export default function TargetOptions(props) {

    const handleChange = (key, event) => {
        props.onChange && props.onChange({[key]: event.target.checked});
    }

    return (
        <FormControl>
            <FormGroup row>
                <FormControlLabel
                    control={
                      <Checkbox
                        checked={props.pretty}
                        name="pretty"
                        color="primary"
                        onChange={handleChange.bind(this, "pretty")}
                      />}
                    label="Pretty"
                    />
                <FormControlLabel
                    control={
                        <Checkbox
                            checked={props.verbose}
                            color="primary"
                            name="checkedA"
                            onChange={handleChange.bind(this, "verbose")}
                            />}
                    label="Verbose"
                    />
            </FormGroup>
        </FormControl>
        );
}
