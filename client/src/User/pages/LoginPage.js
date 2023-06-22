import React, {useState} from 'react';
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import Logo from "../../logo.svg";
import {useDispatch, useSelector} from "react-redux";
import {loginRequest, signUpRequest} from "../UserActions";

const LoginPage = () => {
    const dispatch = useDispatch();
    const isLogged = useSelector(state => state.user.isLogged);
    const [state, setState] = useState({});

    const handleChange = (evt) => {
        const value = evt.target.value;
        setState({
            ...state,
            [evt.target.name]: value
        });
    };

    const submit = () => {
        console.log(state);
        dispatch(loginRequest(state));
    }

    return (
        <div className="container">
            <div className="row">
                <div className="col-12 d-flex align-items-center">
                    <img className="mx-3" src={Logo} alt="Logo" style={{height: '72px'}}/>
                    <h1 className="mt-4">
                        Alaya Blog
                    </h1>
                </div>
            </div>
            {isLogged?
                (
                    <div className="row">
                        <div className={`d-flex flex-column my-4 w-100 col-6 user-logged-in`}>
                            User is logged in
                        </div>
                    </div>
                )
            :
                (
                    <div className="row">
                        <div className={`d-flex flex-column my-4 w-100 col-6`}>
                            <h3>Log In</h3>
                            <TextField variant="filled" label="Username" name="username" onChange={handleChange}/>
                            <TextField variant="filled" type="password" label="Password" name="password"
                                       onChange={handleChange}/>
                            <Button className="mt-4" type="submit" variant="contained" color="primary" onClick={() => submit()}
                                    disabled={!state.username || !state.password}>
                                Submit
                            </Button>
                        </div>
                    </div>
                )
            }

        </div>
    );
};


export default LoginPage;
