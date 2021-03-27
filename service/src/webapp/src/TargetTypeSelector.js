import React from 'react';

import { makeStyles } from '@material-ui/core/styles';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';

import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';


const useStyles = makeStyles((theme) => ({


}));

export default function TargetTypeSelector() {
    const classes = useStyles();

     const [age, setAge] = React.useState('');

      const handleChange = (event) => {
    setAge(event.target.value);
  };

   const [state, setState] = React.useState({
    checkedA: false,
    checkedB: true,
  });

  return (
               <FormControl className={classes.formControl} variant="outlined">
        <InputLabel id="target-select-label">Target</InputLabel>
        <Select
          labelId="target-select-label"
          id="target-select"
          value="10"
          onChange={handleChange}
            className={classes.mediaTypeSelector}

            label="Target"
        >
          <MenuItem value={10}>ALPS+YAML</MenuItem>
          <MenuItem value={20}>ALPS+XML</MenuItem>
          <MenuItem value={30}>ALPS+JSON</MenuItem>
        </Select>
      </FormControl>
  );
}
