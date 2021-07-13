import React from 'react'

import { IconButton, Snackbar } from '@material-ui/core';

import MuiAlert from '@material-ui/lab/Alert';

import FileCopyIcon from '@material-ui/icons/FileCopy';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';

import { makeStyles } from '@material-ui/core/styles';

import {Controlled as CodeMirror} from 'react-codemirror2'


const useStyles = makeStyles((theme) => ({
  root: {
    fontSize: '16px',
    position: 'relative'
  },
  controls: {
    position: 'absolute',
    bottom: theme.spacing(1),
    right: theme.spacing(4),
    zIndex: 200,
  },
  action: { 
      color: theme.palette.text.secondary,
  },
}));

function Alert(props) {
  return <MuiAlert elevation={6} variant="filled" {...props} />;
}

export default function Viewer(props) {

    const classes = useStyles();

    const [open, setOpen] = React.useState(false);

    const { mediaType, value } = props;

    const typeToMode = contentType => {

        if (contentType.includes("json")) {
            return { name: "javascript", json: true };
        }
        if (contentType.includes("yaml") || contentType.includes("application/vnd.oai.openapi") || contentType.includes("application/x-asyncapi")) {
            return "yaml";
        }
        if (contentType.includes("xml")) {
            return "xml";
        }
        if (contentType.includes( "html")) {
            return "htmlmixed";
        }
        if (contentType.includes("application/protobuf") || contentType.includes("x-sdl")) {
          return "clike";
        }
        return "text";
    }

    const typeToFileExt = contentType => {

        if (contentType.includes("json")) {
            return ".json";
        }
        if (contentType.includes("yaml") || contentType.includes("application/vnd.oai.openapi") || contentType.includes("application/x-asyncapi")) {
            return ".yml";
        }
        if (contentType.includes("xml")) {
            return ".xml";
        }
        if (contentType.includes( "html")) {
            return ".html";
        }
        if (contentType.includes("application/protobuf") || contentType.includes("x-sdl")) {
          return ".c";
        }
        return "";
    }
    
    const handleCopy =  () => {
        navigator.clipboard.writeText(value);
        setOpen(true);
    };
    
    const handleClose = (event, reason) => {
      if (reason === 'clickaway') {
        return;
      }

      setOpen(false);
    };

    const handleDownload = () => {
      const element = document.createElement("a");
      const file = new Blob([value], {type: mediaType});
      element.href = URL.createObjectURL(file);
      element.download =  "eiger-export" + typeToFileExt(mediaType);
      document.body.appendChild(element);
      element.click();
    }

    return (
      <div>
        <div className={classes.root}>
          <div className={classes.controls}>
            <IconButton onClick={handleCopy} title="Copy the result to a clipboard" className={classes.action}>
              <FileCopyIcon/>
            </IconButton>
            <IconButton onClick={handleDownload} title="Download the result"  className={classes.action}>
              <CloudDownloadIcon/>
            </IconButton>            
          </div>
          <CodeMirror
              value={value}
              options={{
                  mode: typeToMode(mediaType),
                  theme: 'material-darker',
                  lineNumbers: false,
                  readOnly: 'nocursor',
                  }}
              />

        </div>
        <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
          <div>
             <Alert onClose={handleClose} severity="success">
                The result has been copied to a clipboard.
              </Alert>
          </div>
        </Snackbar>
      </div>
      );
}