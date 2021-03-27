import React from 'react'

import { withStyles } from '@material-ui/core/styles';

import {Controlled as CodeMirror} from 'react-codemirror2'


import exampleC from './openapi.yaml';

require('codemirror/lib/codemirror.css');
require('codemirror/theme/material.css');
require('codemirror/theme/material-darker.css');
require('codemirror/theme/neat.css');
require('codemirror/mode/xml/xml.js');
require('codemirror/mode/yaml/yaml.js');
//require('codemirror/mode/json/json.js');

//const useStyles = makeStyles((theme) => ({
//
//}));

const styles = theme => ({


  root: {
    fontSize: '16px'
  }
});

const exampleCode = exampleC;



//const exampleCode = `openapi: 3.0.2
//info:
//  title: Swagger Petstore - OpenAPI 3.0
//`;

class SourceEditor extends React.Component {
state = { value: exampleCode }


  render() {
    
      const { classes } = this.props;
    
    return (
<CodeMirror
className={classes.root}
  value={this.state.value}
  options={{
    mode: 'yaml',
    theme: 'material-darker',
    lineNumbers: true
  }}
  onBeforeChange={(editor, data, value) => {
    this.setState({value});
  }}
  onChange={(editor, data, value) => {
  }}
/>
    )
  }
}

export default withStyles(styles)(SourceEditor);