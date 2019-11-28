import React from "react";
import ReactDOM from "react-dom";
import Modal from "react-modal";
import { ConfContext } from "../../ConfContext";

const modelStyles = {
  content: {
    top: "50%",
    left: "50%",
    right: "auto",
    bottom: "auto",
    marginRight: "-50%",
    transform: "translate(-50%, -50%)"
  }
};

Modal.setAppElement("#root");

class ViewConfig extends React.Component {
  constructor() {
    super();

    this.state = {
      modalIsOpen: false
    };

    this.openModal = this.openModal.bind(this);
    this.afterOpenModal = this.afterOpenModal.bind(this);
    this.closeModal = this.closeModal.bind(this);
  }
  openModal() {
    this.setState({ modalIsOpen: true });
  }

  afterOpenModal() {
    // this.subtitle.style.color = '#f00';
  }

  closeModal() {
    this.setState({ modalIsOpen: false });
  }

  render() {
    return (
      <div className="center-align">
        <h2 className="label">View Config</h2>
        <div className="blockItem">
          <span className="item label">{this.context.currentConf.port}</span>
          <span className="item label">{this.context.currentConf.url}</span>
          <span className="item label">{this.context.currentConf.log}</span>
          <span className="item label">{this.context.currentConf.user}</span>
          <button className="button" onClick={this.openModal}>
            Open Modal
          </button>
          <Modal
            isOpen={this.state.modalIsOpen}
            onAfterOpen={this.afterOpenModal}
            onRequestClose={this.closeModal}
            style={modelStyles}
            contentLabel="Dashbaord"
          >
            <div className="center-align">
              <h2 className="label">Dashbaord</h2>
              <button className="button" onClick={this.closeModal}>
                close
              </button>
              <form>
                <div className="label">
                  Port: {this.context.currentConf.port}
                </div>
                <div className="label">Ur: {this.context.currentConf.url}</div>
                <div className="label">Log: {this.context.currentConf.log}</div>
                <div className="label">
                  User: {this.context.currentConf.user}
                </div>
              </form>
            </div>
          </Modal>
        </div>
      </div>
    );
  }

  componentDidMount() {
    // console.log(this.context);
  }
}

ViewConfig.contextType = ConfContext;
export default ViewConfig;
