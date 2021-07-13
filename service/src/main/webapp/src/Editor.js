import React from 'react'

import { makeStyles } from '@material-ui/core/styles';

import {Controlled as CodeMirror} from 'react-codemirror2'


const useStyles = makeStyles((theme) => ({
  root: {
    fontSize: '16px'
  }
}));

const expandTabs = cm => {
    if (cm.somethingSelected()) {
        cm.indentSelection("add");
      
    } else {
      cm.replaceSelection(cm.getOption("indentWithTabs")? "\t": Array(cm.getOption("indentUnit") + 1).join(" "), "end", "+input");
    }
}

export default function Editor(props) {

    const classes = useStyles();

    const { type, readOnly, value, onChange } = props;

    const mode = type === 'json' ? { name: "javascript", json: true } : type;

    let options = {
            mode: mode,
            theme: 'material-darker',
            lineNumbers: true,
            extraKeys: {
              Tab: expandTabs,
            },
        };

    if (readOnly) {
        options['readOnly'] = "nocursor";
    }

    return (
        <CodeMirror
            className={classes.root}
            value={value}
            options={options}
            onBeforeChange={(editor, data, value) => {
                onChange && onChange(value);
            }}
            />
            );
}