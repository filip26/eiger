import React from 'react';

import { makeStyles } from '@material-ui/core/styles';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';


const useStyles = makeStyles((theme) => ({

}));

export default function SourceTypeSelector() {
    const classes = useStyles();

     const [age, setAge] = React.useState('');

      const handleChange = (event) => {
    setAge(event.target.value);
  };

   const [state, setState] = React.useState({
    checkedA: false,
    checkedB: true,
  });

//  const handleChange = (event) => {
//    setState({ ...state, [event.target.name]: event.target.checked });
//  };

  return (
        <FormControl className={classes.formControl} variant="outlined">
        <InputLabel id="source-select-label">Source</InputLabel>
        <Select
          labelId="source-select-label"
          id="source-select"
          value="30"
          onChange={handleChange}
             label="Source"
          className={classes.mediaTypeSelector}
        >
          <MenuItem value={10}>ALPS+XML</MenuItem>
          <MenuItem value={20}>ALPS+JSON</MenuItem>
          <MenuItem value={30}>OpenAPI v3</MenuItem>
        </Select>
      </FormControl>
  );
}
