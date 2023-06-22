import React from 'react';
import * as ReactDOM from 'react-dom';
import { combineReducers, legacy_createStore as createStore, compose, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';
import posts from './Post/PostReducer';
import user from './User/UserReducer';
import './index.css';
import App from './App';
import {Provider} from "react-redux";

// Middleware and store enhancers
const enhancers = [
    applyMiddleware(thunk),
];

const initialStore = createStore(combineReducers({ posts, user }), { }, compose(...enhancers));

ReactDOM.render(
    <Provider store={initialStore}>
        <App store={initialStore}/>
    </Provider>
    , document.getElementById('root'));
