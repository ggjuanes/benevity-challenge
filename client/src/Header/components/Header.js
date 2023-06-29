import React from 'react';
import Logo from "../../logo.svg";

function Header() {
    return (
        <div className="container w-100 pt-5 mt-5">
            <div className="row">
                <div className="col-12 d-flex align-items-center">
                    <img className="mx-3" src={Logo} alt="Logo" style={{height: '72px'}}/>
                    <h1 className="mt-4">
                        Alaya Blog
                    </h1>
                </div>
            </div>
            <hr />
        </div>
    );

};

export default Header;
