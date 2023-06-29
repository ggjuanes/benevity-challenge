import React from 'react';
import { render } from '@testing-library/react';
import App from './App';
import {applyMiddleware, combineReducers, compose, legacy_createStore as createStore} from "redux";
import posts from "./Post/PostReducer";
import user from "./User/UserReducer";
import thunk from "redux-thunk";

test('renders Alaya Blog header', () => {
  const enhancers = [
    applyMiddleware(thunk),
  ];
  const initialStore = createStore(combineReducers({ posts, user }), { }, compose(...enhancers));

  const { getByText } = render(<App  store={initialStore}/>);
  const linkElement = getByText(/Alaya Blog/i);
  expect(linkElement).toBeInTheDocument();
});
