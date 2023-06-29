import callApi, {HTTP_METHODS} from "../util/apiCaller";

export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const SIGNUP_SUCCESS = 'SIGNUP_SUCCESS';

export function loginRequest(user) {
    return (dispatch) => {
        return callApi('login', HTTP_METHODS.POST, {
                username: user.username,
                password: user.password
        }).then(res => {
            // TODO: handle error case
            // TODO: handle when JWT is expired and reset user.login.token
            localStorage.setItem('token', res.token);
            return dispatch(logInSuccess(res.token));
        });
    };
}

export function signUpRequest(user) {
    return (dispatch) => {
        return callApi('signup', HTTP_METHODS.POST, {
                username: user.username,
                password: user.password
        }).then(() => {
            // TODO: handle error case
            return dispatch(signUpSuccess());
        });
    };
}

function signUpSuccess() {
    return {
        type: SIGNUP_SUCCESS,
    };
}

function logInSuccess(token) {
    return {
        type: LOGIN_SUCCESS,
        token,
    };
}