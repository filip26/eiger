import React from 'react';

import { makeStyles } from '@material-ui/core/styles';
import FormControl from '@material-ui/core/FormControl';

import FormControlLabel from '@material-ui/core/FormControlLabel';

import FormGroup from '@material-ui/core/FormGroup';
import Checkbox from '@material-ui/core/Checkbox';

const useStyles = makeStyles((theme) => ({
}));

export default function TargetOptions() {
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
               <FormControl className={classes.formControl2}>

         <FormGroup row>
      <FormControlLabel


        control={
          <Checkbox

            className={classes.targetOption}
            checked={state.checkedB}
            onChange={handleChange}
            name="checkedB"
            color="primary"
          />
        }
        label="Pretty"
      />
            <FormControlLabel
        control={<Checkbox checked={state.checkedA}              className={classes.targetOption} onChange={handleChange}             color="primary" name="checkedA" />}
        label="Verbose"
      />

    </FormGroup>
      </FormControl>
  );
}
