import React from "react";
import { ConfContext } from "./../../ConfContext";

class EditConfig extends React.Component {
  currentConf = {};
  constructor(props) {
    super(props);
    this.state = { port: "", url: "", log: "", user: "" };
  }

  render() {
    // this.currentConf = {...this.context.currentConf};
    return (
      <form onSubmit={this.editSubmitHandler}>
        <div className="center-align">
          <div>
            <h2 className="label">Add / Edit Configurations</h2>
          </div>
          <div>
            <input
              placeholder="Port"
              type="text"
              name="port"
              id="port"
              value={this.state.port}
              onChange={this.handlePortChange}
            />
          </div>
          <div>
            <input
              placeholder="Url"
              type="text"
              name="url"
              id="url"
              value={this.state.url}
              onChange={this.handleUrlChange}
            />
          </div>
          <div>
            <input
              placeholder="Log"
              type="text"
              name="log"
              id="log"
              value={this.state.log}
              onChange={this.handleLogChange}
            />
          </div>
          <div>
            <input
              placeholder="User"
              type="text"
              name="user"
              id="user"
              value={this.state.user}
              onChange={this.handleUserChange}
            />
          </div>
          <button className="button" type="Submit">
            Submit
          </button>
        </div>
      </form>
    );
  }

  componentDidMount() {
    this.setState({ port: this.context.currentConf.port });
    this.setState({ url: this.context.currentConf.url });
    this.setState({ log: this.context.currentConf.log });
    this.setState({ user: this.context.currentConf.user });
  }

  handleUserChange = e => {
    this.setState({ user: e.target.value });
    e.preventDefault();
  };

  handleLogChange = e => {
    this.setState({ log: e.target.value });
    e.preventDefault();
  };

  handleUrlChange = e => {
    this.setState({ url: e.target.value });
    e.preventDefault();
  };

  handlePortChange = e => {
    this.setState({ port: e.target.value });
    e.preventDefault();
  };

  editSubmitHandler = event => {
    this.context.confData[this.context.currentInd] = { ...this.state };
    console.log("this.context", this.context.confData.length, this.context, {
      ...this.state
    });
    this.props.history.push("/config");
    event.preventDefault();
  };
}

EditConfig.contextType = ConfContext;
export default EditConfig;
