import React from 'react';
import Toolbar from '@material-ui/core/Toolbar';
import AppBar from '@material-ui/core/AppBar';
import Typography from '@material-ui/core/Typography';
import Link from '@material-ui/core/Link';

function Navbar() {
    return (
        <AppBar position="fixed">
            <Toolbar>
                <Typography variant="h6" >
                    <Link href="/home" className="text-white p-2">Home</Link>
                    <Link href="/signup" className="text-white p-2">Sign up</Link>
                    <Link href="/login" className="text-white p-2">Log in</Link>
                </Typography>
            </Toolbar>
        </AppBar>
    );

};

export default Navbar;
