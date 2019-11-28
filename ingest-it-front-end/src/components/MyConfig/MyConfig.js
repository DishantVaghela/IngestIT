import "./MyConfig.css";
import React from "react";
import { ConfContext } from "./../../ConfContext";

class CHover extends React.Component {
  constructor(props) {
    super(props);
    this.edit = this.edit.bind(this);
    this.view = this.view.bind(this);
  }

  render() {
    return (
      <div style={{ display: "inline-block", marginLeft: "20px" }}>
        <button className="button" onClick={this.view}>
          View
        </button>
        <button className="button" onClick={this.edit}>
          Edit
        </button>
      </div>
    );
  }

  view() {
    this.props.viewt(this.props.item, this.props.index);
  }

  edit() {
    this.props.editt(this.props.item, this.props.index);
  }
}

class MyConfig extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      viewt: true,
      configItem: "",
      config: [
        { port: "1234", url: "www.test0.com", log: "log1", user: "user1" },
        { port: "8080", url: "www.test1.com", log: "log2", user: "user2" },
        { port: "8182", url: "www.test2.com", log: "log3", user: "user1" },
        { port: "8183", url: "www.test3.com", log: "log4", user: "user2" },
        { port: "8184", url: "www.test4.com", log: "log5", user: "a" },
        { port: "8186", url: "www.test4.com", log: "log5", user: "a" }
      ]
    };
    this.gotoView = this.gotoView.bind(this);
    this.gotoEdit = this.gotoEdit.bind(this);
    this.gotoHome = this.gotoHome.bind(this);
    this.gotoAdd = this.gotoAdd.bind(this);
  }

  render() {
    if (!this.context.confData.length) {
      this.context.confData = this.state.config;
    }
    return (
      <div className="center-align">
        {/* <button className="button" onClick={this.gotoHome}>Home</button> */}
        <div>
          <h1 className="label">Configurations</h1>
          {this.state.config.map((item, index) => {
            return item.user === this.context.user ? (
              <div key={index} className="blockItem">
                <span className="item label">{item.port}</span>
                <span className="item label">{item.url}</span>
                <span className="item label">{item.log}</span>
                <span className="item label">{item.user}</span>
                <CHover
                  viewt={this.gotoView}
                  editt={this.gotoEdit}
                  item={item}
                  index={index}
                ></CHover>
              </div>
            ) : null;
          })}
        </div>
        <button className="button" onClick={this.gotoAdd}>
          Add
        </button>
      </div>
    );
  }

  componentDidMount() {
    if (this.context && this.context.confData) {
      this.setState({ config: this.context.confData });
    }
  }

  componentDidUpdate() {
    this.context.confData = this.state.config;
  }

  gotoView(e, i) {
    this.context.currentConf = e;
    this.context.currentInd = i;
    this.setState({ viewt: false, configItem: e });
    this.props.history.push("/view");
  }

  gotoEdit(e, i) {
    this.context.currentConf = e;
    this.context.currentInd = i;
    this.setState({ viewt: false, configItem: e });
    this.props.history.push("/edit");
  }

  gotoAdd() {
    this.context.currentConf = { port: "", url: "", log: "", user: "" };
    this.context.currentInd = this.context.confData.length;
    this.props.history.push("/add");
  }

  gotoHome(e) {
    this.setState({ viewt: true });
    this.props.history.push("/config");
  }
}

MyConfig.contextType = ConfContext;
export default MyConfig;
