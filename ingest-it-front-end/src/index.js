import React from "react";
import ReactDOM from "react-dom";
import { Route, BrowserRouter as Router } from "react-router-dom";
import "./index.css";
import App from "./App";
import * as serviceWorker from "./serviceWorker";

import MyConfig from "./components/MyConfig/MyConfig";
import Login from "./components/Login/Login";
import ViewConfig from "./components/ViewConfig/ViewConfig";
import EditConfig from "./components/EditConfig/EditConfig";

const routing = (
  <Router>
    <div>
      <Route path="/" component={App} />
      <Route path="/login" component={Login} />
      <Route path="/config" component={MyConfig} />
      <Route path="/edit" component={EditConfig} />
      <Route path="/add" component={EditConfig} />
      <Route path="/view" component={ViewConfig} />
    </div>
  </Router>
);
ReactDOM.render(routing, document.getElementById("root"));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
