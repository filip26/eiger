import React from 'react'

import { withStyles } from '@material-ui/core/styles';

import {Controlled as CodeMirror} from 'react-codemirror2'


const styles = theme => ({
    root: {
        fontSize: '16px'
    }
});

class Editor extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            value: props.value
        }
    }

    value = () => {
        return this.state.value;
    }

    render() {
        const { classes, type } = this.props;
        const mode = type === 'json' ? { name: "javascript", json: true } : type;

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

export default withStyles(styles)(Editor);