import React from 'react'

import { withStyles } from '@material-ui/core/styles';

import {Controlled as CodeMirror} from 'react-codemirror2'


const styles = theme => ({
    root: {
        fontSize: '16px'
    }
});

const exampleCode = `openapi: 3.0.2
info:
  title: Swagger Petstore - OpenAPI 3.0
`;

class Code extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            value: exampleCode
        }
    }

    value = () => {
        return this.state.value;
    }

    render() {
        const { classes } = this.props;
        const mode = this.props.type.format === 'json' ? { name: "javascript", json: true } : this.props.type.format;

        let options = {
                mode: mode,
                theme: 'material-darker',
                lineNumbers: true,
            };

        if (this.props.readOnly) {
            options['readOnly'] = "nocursor";
        }

        return (
            <CodeMirror
                className={classes.root}
                value={this.state.value}
                options={options}
                onBeforeChange={(editor, data, value) => {
                    this.setState({value});
                }}
                onChange={(editor, data, value) => {
                }}
                />
                );
            }
}

export default withStyles(styles)(Code);