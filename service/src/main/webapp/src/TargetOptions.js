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
            <FormControlLabel disabled={props.prettyDisabled}
                control={
                  <Checkbox
                    checked={props.pretty}
                    name="pretty"
                    color="primary"
                    onChange={event => handleChange("pretty", event)}
                  />}
                label="Pretty"
                />
            <FormControlLabel disabled={props.verboseDisabled}
                control={
                    <Checkbox
                        checked={props.verbose}
                        color="primary"
                        name="checkedA"
                        onChange={event => handleChange("verbose", event)}
                        />}
                label="Verbose"
                />
          </FormGroup>
        </FormControl>
        );
}
