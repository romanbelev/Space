import React from 'react'
import Notification from "../Components/Notification/Notification";

export default class TouristAdd extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            firstName: '',
            lastName: '',
            gender: '',
            country: '',
            dateOfBirth: '',
            shipIdentifier: '',
            ships: [],
            description: ''
        };

        this.notificationComponent = React.createRef();

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    render() {
        console.log("Render -->");
        const { ships,
            firstName,
            lastName,
            gender,
            country,
            dateOfBirth,
            shipIdentifier,
            description
        } = this.state;

        return (
            <div className="tourist-container">
                <form onSubmit={this.handleSubmit}>
                    <div className="container-fluid px-5">
                        <div className="row">
                            <div className="col-12">
                                <div className="title-page">Tourists add</div>
                                <div className="form-group">
                                    <input type="text" className="form-control" name="firstName" placeholder="First name"
                                        onChange={this.handleChange} value={firstName}></input>
                                </div>
                                <div className="form-group">
                                    <input type="text" className="form-control" name="lastName" placeholder="Last name"
                                        onChange={this.handleChange} value={lastName}></input>
                                </div>
                                <div className="form-group">
                                    <input type="text" className="form-control" name="gender" placeholder="Gender"
                                           onChange={this.handleChange} value={gender}></input>
                                </div>
                                <div className="form-group">
                                    <input type="text" className="form-control" name="country" placeholder="Country"
                                           onChange={this.handleChange} value={country}></input>
                                </div>
                                <div className="form-group">
                                    <input type="text" className="form-control" name="dateOfBirth" placeholder="Date of birth"
                                           onChange={this.handleChange} value={dateOfBirth}></input>
                                </div>
                                <div className="form-group">
                                    <label form="ship">Select the ship</label>
                                    <select className="form-control" id="ship" name="shipIdentifier" onChange={this.handleChange}>
                                        <option value="null">None</option>
                                        {ships.map(ship => {
                                            let sel = shipIdentifier === ship.id ? "selected" : "";
                                            return <option selected={sel} key={ship.id} value={ship.id}>{ship.direction} (Available seats: {ship.seatsAvailable})</option>
                                        })}
                                    </select>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="about">Information about tourist</label>
                                    <textarea type="about" className="form-control" id="about" name="description" rows="3" placeholder="Some information about passenger"
                                              onChange={this.handleChange} value={description}></textarea>
                                </div>
                                <button className="btn btn-outline-primary" type="submit">Add tourist</button>
                            </div>
                        </div>
                    </div>
                </form>
                <Notification ref={this.notificationComponent} text={this.state.notificationText} visible={this.state.notificationVisible}></Notification>
            </div>
        )
    }

    componentDidMount() {
        console.log("Did Mount -->");

        this.fetchShips();
    }

    fetchShips() {
        const url = "http://127.0.0.1:8080/api/ship/all";
        fetch(url)
            .then(res => res.json())
            .then(res => {
                this.setState({
                    ships: res
                })
            });
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value,
        });
    }

    handleSubmit(event) {
        console.log("Form submited -->");
        event.preventDefault();
        // console.log(JSON.stringify(this.state));

        const url = "http://127.0.0.1:8080/api/tourist/add";
        fetch(url, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            redirect: "follow",
            method: "POST",
            dataType: 'json',
            body: JSON.stringify(this.state)
        })
            .then(res => res.json())
            .then(res => {
                if (res.redirect) {
                    console.log(res);
                    this.props.history.push(res.redirect);
                } else {
                    console.log(res);
                    this.notificationComponent.current.showMessage(res.message, res.success);
                }
            });
    }
}