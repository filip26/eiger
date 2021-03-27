import React from 'react'

import { withStyles } from '@material-ui/core/styles';

import {Controlled as CodeMirror} from 'react-codemirror2'

const styles = theme => ({


  root: {
    fontSize: '16px'
  }
});

const exampleCode = `
openapi: 3.0.2
info:
  title: Swagger Petstore - OpenAPI 3.0

`;

class TargetViewer extends React.Component {

state = { value: exampleCode };

    render() {

      const { classes } = this.props;

    return (
<CodeMirror
className={classes.root}
  value={this.state.value}
  options={{
    mode: 'yaml',
    theme: 'material-darker',
    lineNumbers: true,
    readOnly: 'nocursor',
  }}
/>
    )
    }
}

export default withStyles(styles)(TargetViewer);
