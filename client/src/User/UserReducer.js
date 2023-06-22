// Initial State
import {ADD_TOKEN} from "./UserActions";

const initialState = { isLogged: false, token: null };

const UserReducer = (state = initialState, action) => {
  switch (action.type) {
    case ADD_TOKEN:
      return {
        token: action.token,
        isLogged: true
      };
    default:
      return state;
  }
};

/* Selectors */

// Get all posts
export default UserReducer;
