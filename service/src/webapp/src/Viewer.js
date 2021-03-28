import React from 'react'

import { makeStyles } from '@material-ui/core/styles';

import {Controlled as CodeMirror} from 'react-codemirror2'


const useStyles = makeStyles((theme) => ({

    root: {
        fontSize: '16px'
    }
}));

export default function Viewer(props) {

    const classes = useStyles();
 
    const { type, value } = props;
    const mode = type === 'json' ? { name: "javascript", json: true } : type;

    return (
        <CodeMirror
            className={classes.root}
            value={value}
            options={{
                mode: mode,
                theme: 'material-darker',
                lineNumbers: true,
                readOnly: 'nocursor',
                }}
            />
            );
}