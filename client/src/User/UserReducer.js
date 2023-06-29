// Initial State
import {SIGNUP_SUCCESS, LOGIN_SUCCESS} from "./UserActions";

const initialState = { login: {success:false, token: null}, signup: {success: false} };

const UserReducer = (state = initialState, action) => {
  switch (action.type) {
    case LOGIN_SUCCESS:
      return {
        ...state,
        login: {
          success: true,
          token: action.token,
        },
      };
    case SIGNUP_SUCCESS:
      return {
        ...state,
        signup: {
            success: true
        }
      }
    default:
      return state;
  }
};

/* Selectors */

// Get all posts
export default UserReducer;
