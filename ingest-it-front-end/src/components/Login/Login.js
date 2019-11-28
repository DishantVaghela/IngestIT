import React from "react";
import { ConfContext } from "./../../ConfContext";
import "../../App.css";

class Login extends React.Component {
  user = [
    {
      id: "a",
      password: "a"
    },
    {
      id: "user1",
      password: "password"
    },
    {
      id: "user2",
      password: "password2"
    }
  ];
  render() {
    return (
      <form onSubmit={this.loginSubmitHandler}>
        <div className="center-align">
          <div>
            <h2 className="label">Login</h2>
          </div>
          <div>
            <input
              placeholder="Id"
              type="text"
              name="username"
              id="username"
              autoComplete="true"
            ></input>
          </div>
          <div>
            <input
              placeholder="Password"
              type="password"
              name="password"
              id="password"
              autoComplete="true"
            ></input>
          </div>
          <div>
            <button className="button" type="submit">
              Submit
            </button>
          </div>
        </div>
      </form>
    );
  }
  loginSubmitHandler = event => {
    event.preventDefault();
    let success = false;
    this.user.forEach((user, ind) => {
      if (
        user.id.toUpperCase() === event.target.username.value.toUpperCase() &&
        user.password.toUpperCase() ===
          event.target.password.value.toUpperCase()
      ) {
        this.props.history.push("/config");
        success = true;
        this.context.user = event.target.username.value;
        // console.log('loginConfContext', this.context);
      }
      return success;
    });
    if (!success) {
      alert("Username/Password is not authorized");
    }
  };
}
Login.contextType = ConfContext;
export default Login;
