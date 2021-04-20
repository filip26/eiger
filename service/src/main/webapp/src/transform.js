
import unified from 'alps-unified';

const remote = async (sourceType, source, targetType, options) => {

    const url = '/transform?' + new URLSearchParams(options);

    return fetch(url, {
        method: 'POST',
        headers: {
            'Accept': targetType.mediaType + ",*/*;q=0.1",
            'Content-Type': sourceType.mediaType + "; charset=" + document.characterSet,
        },
        referrerPolicy: 'no-referrer',
        cache: 'no-cache',
        body: source,
    });
}

const execute = async (sourceType, source, targetType, options, method) => {
  
    return remote(sourceType, source, {model: "alps", format: "json", label: "ALPS (JSON)", mediaType: "application/alps+json"}, options)
      
      .then(response => {

          if (response.status === 200 
//                && response.headers.get("content-length")
//                && response.headers.get("content-length") > 0
                ) {

            return response
                      .json()
                      .then(json => {

                          const r = method(json, { options: { file: options.base }});
  
                          return {
                            status: 200,
                            text: () => Promise.resolve(r),
                            headers: {
                              get: () => targetType.mediaType
                            }
                          };                    
                  });
          }
          return response;
      });
}

const transform = async (sourceType, source, targetType, options) => {

    switch (targetType.mediaType) {
    case "application/protobuf":
      return execute(sourceType, source, targetType, options, unified.toProto);
     
    case  "application/wsdl+xml":
      return execute(sourceType, source, targetType, options, unified.toWSDL);
            
    case  "application/x-sdl":
      return execute(sourceType, source, targetType, options, unified.toSDL);
        
    case  "application/x-asyncapi":
      return execute(sourceType, source, targetType, options, unified.toAsync);
    
    case  "application/vnd.oai.openapi":
      return execute(sourceType, source, targetType, options, unified.toOAS);        
           
    default:
      return remote(sourceType, source, targetType, options);
    }
}

export default transform;