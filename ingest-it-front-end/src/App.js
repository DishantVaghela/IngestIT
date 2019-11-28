import React from "react";
import { Link } from "react-router-dom";
import "./App.css";
import { ConfContext } from "./ConfContext";
const user = { user: "", confData: [], currentConf: {} };
function App() {
  return (
    <ConfContext.Provider value={user}>
      <div className="App">
        <header className="App-header">
          <button className="button">
            <Link to="/config" className="App-link">
              Config
            </Link>
          </button>
          <button className="button">
            <Link to="/login" className="App-link">
              Logout
            </Link>
          </button>
          {/* <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <p>
            Edit <code>src/App.js</code> and save to reload.
          </p>
          <a
            className="App-link"
            href="https://reactjs.org"
            target="_blank"
            rel="noopener noreferrer"
          >
            Learn React
          </a>
        </header> */}
        </header>
      </div>
    </ConfContext.Provider>
  );
}

export default App;
