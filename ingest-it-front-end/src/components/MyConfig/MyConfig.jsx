import './MyConfig.css';
import React from "react";

let config = [
	{ port: '1234', url: 'www.test.com', log: 'log1' },
	{ port: '8080', url: 'www.test1.com', log: 'log2' },
	{ port: '8182', url: 'www.test2.com', log: 'log3' },
	{ port: '8183', url: 'www.test3.com', log: 'log4' }
];

class CHover extends React.Component {
	constructor(props) {
		super(props);
		this.edit = this.edit.bind(this);
		this.view = this.view.bind(this);
	}

	render() {
		return <div style={{ display: 'inline-block', marginLeft: '20px' }}>
			<button className="button" onClick={this.view}>View</button>
			<button className="button" onClick={this.edit}>Edit{this.props.item}</button>
		</div>;
	}
	view() {
		console.log('this.props', this.props);
		this.props.viewt(this.props.item);
	}
	edit() {
		this.props.editt(this.props.item);
	}
}

function template() {
	return (
		<div>
			{/* <button className="button" onClick={this.gotoHome}>Home</button> */}
			<div>
				<h1>Port</h1>
				{config.map((item, index) => {
					return <div key={index}>
						{item.port}
						<CHover viewt={this.gotoView} editt={this.gotoEdit} item={item.port}></CHover>
					</div>
				})}
			</div>
			<button className="button" onClick={this.gotoEdit}>Add</button>
		</div>
	)
}

export default template;
