import React from 'react';
import { Link } from 'react-router-dom';
import Notification from "../Components/Notification/Notification";

export default class TouristList extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            items: []
        };

        this.notificationComponent = React.createRef();
    }

    render() {
        console.log("Render -->");

        const { items } = this.state;
        return (
            <div className="tourist-container">
                <div className="container-fluid px-5">
                    <div className="row">
                        <div className="col-12">
                            <div className="title-page">Tourists list</div>
                            <table className="table">
                                <thead className="table__head">
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">First name</th>
                                        <th scope="col">Last name</th>
                                        <th scope="col">Gender</th>
                                        <th scope="col">Country</th>
                                        <th scope="col">Date of birth</th>
                                        <th scope="col">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                {items.map((item, index) => (
                                    <tr key={item.id}>
                                        <th scope="row">{index + 1}</th>
                                        <td>
                                            <Link to={`/tourist/${item.id}`}>
                                                {item.firstName}
                                            </Link>
                                        </td>
                                        <td>{item.lastName}</td>
                                        <td>{item.gender}</td>
                                        <td>{item.country}</td>
                                        <td>{item.dateOfBirth}</td>
                                        <td>
                                            <Link to={`/tourist/edit/${item.id}`}>
                                                <button type="button" className="btn btn-sm btn-outline-success">Edit</button>
                                            </Link>
                                            <button type="button" className="btn btn-sm btn-outline-danger" onClick={this.deleteItem.bind(this, item.id, index)}>Delete</button>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <Notification ref={this.notificationComponent} text={this.state.notificationText} visible={this.state.notificationVisible}></Notification>
            </div>
        )
    }

    componentDidMount() {
        console.log("Did Mount -->");

        this.fetchData();
    }

    fetchData() {
        const url = "http://127.0.0.1:8080/api/tourist/all";
        fetch(url)
            .then(res => {
                return res.json()
            })
            .then(res => {
                console.log(res)
                this.setState({
                    items: res
                })
            })
    }

    deleteItem(userId, index) {
        console.log("Click delete -->");

        const urlRequest = "http://127.0.0.1:8080/api/tourist/" + userId;
        fetch(urlRequest, {
            method: 'DELETE',
        })
            .then(res => res.json())
            .then(res => {
                    let itemsUpdated = Object.assign([], this.state.items);
                    itemsUpdated.splice(index, 1);

                    this.setState({
                        items: itemsUpdated
                    });

                    this.notificationComponent.current.showMessage(res.message, res.success);
                });
    }
}
