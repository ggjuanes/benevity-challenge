import callApi from "../util/apiCaller";

export const ADD_TOKEN = 'ADD_TOKEN';

export function loginRequest(user) {
    return (dispatch) => {
        return callApi('login', 'post', {
            post: {
                username: user.username,
                password: user.password
            },
        }).then(res => {
            user.isLogged = true;
            return dispatch(addToken(res.token));
        });
    };
}

export function signUpRequest(user) {
    return (dispatch) => {
        return callApi('signup', 'post', {
            post: {
                username: user.username,
                password: user.password
            },
        }).then(res => {
            user.isLogged = true;
            return dispatch(addToken(res.token));
        });
    };
}

export function addToken(token) {
    return {
        type: ADD_TOKEN,
        token,
    };
}