import React, {useState} from 'react';
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import {useDispatch, useSelector} from "react-redux";
import {loginRequest} from "../UserActions";

const LoginPage = () => {
    const dispatch = useDispatch();
    const logInSuccess = useSelector(state => state.user.login.success);
    const [state, setState] = useState({});

    const handleChange = (evt) => {
        const value = evt.target.value;
        setState({
            ...state,
            [evt.target.name]: value
        });
    };

    const submit = () => {
        dispatch(loginRequest(state));
    }

    return (
        <div className="container">
            {logInSuccess?
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
