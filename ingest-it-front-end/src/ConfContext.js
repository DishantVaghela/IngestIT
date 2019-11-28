import React from "react";
export const ConfContext = React.createContext({
    user: '',
    confData: [],
    currentConf: {},
    currentInd: 0,
});

