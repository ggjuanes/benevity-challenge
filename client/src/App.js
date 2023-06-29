import React, {useState} from 'react';
import PropTypes from 'prop-types';
import { createTheme, ThemeProvider } from '@material-ui/core/styles';
import './App.css';
import { Route, BrowserRouter, Switch } from 'react-router-dom';
import PostListPage from './Post/pages/PostListPage/PostListPage';
import PostDetailPage from './Post/pages/PostDetailPage/PostDetailPage';
import {Provider, useSelector} from 'react-redux';

import 'bootstrap/dist/css/bootstrap.min.css';
import Navbar from './Nav/components/Navbar';
import SignUpPage from "./User/pages/SignUpPage";
import LoginPage from "./User/pages/LoginPage";
import Header from "./Header/components/Header";

const theme = createTheme({
    palette: {
        primary: {
            main: '#1ecde2',
        },
    },
});

function App(props) {
    const token = localStorage.getItem('token');
    return (
      <ThemeProvider theme={theme}>
          <div className="w-100">
              <Navbar />
              <Header/>
              <div className="w-100 pt-5 mt-5">
                  <Provider store={props.store}>
                    <BrowserRouter>
                      <Switch>
                          <Route path="/signup" exact component={SignUpPage} />
                          <Route path="/login" exact
                                 render={ () => <LoginPage/> }/>
                          <Route path="/home" exact
                                 render={ () => <PostListPage token={token}/> }/>
                          <Route path="/posts/:title" exact
                                 render={() => <PostDetailPage token={token} /> }/>
                      </Switch>
                    </BrowserRouter>
                  </Provider>
              </div>
          </div>
      </ThemeProvider>
);
}

App.propTypes = {
    store: PropTypes.object.isRequired,
};

export default App;
