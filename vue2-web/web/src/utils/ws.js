import store from '@/store';

let wshelper = {}

wshelper.newWs = function (config) {
    if (config.url) {
        const ws = new WebSocket(config.url);
        const projectHeaderParams = store.getters.projectHeaderParams
        if (projectHeaderParams && projectHeaderParams.length > 0) {
            //FIXME may need add query params for project include...
            /*ws.addEventListener('beforeSend', function (event) {
                for (let index = 0; index < projectHeaderParams.length; ++index) {
                    const element = projectHeaderParams[index];
                    event.target.setRequestHeader(element['name'], element['value']);
                }
            });*/
        }
        return ws;
    }
    return null;
}
export default wshelper
