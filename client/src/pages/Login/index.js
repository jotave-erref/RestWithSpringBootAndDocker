import React from "react";
import './styles.css';
import logoImage from '../../assets/logojotave.png'
import loginImage from '../../assets/login.png'

export default function Login(){
    return ( 
        <div className="login-container">

            <section className="form">
                <img src={logoImage} alt="Logo Jotave" />
                <form>
                    <h1>Access your account</h1>
                    <input placeholder="Username" />
                    <input type="password" placeholder="Password" />

                    <button className="button" type="submit">Login</button>
                </form>
            </section>

            <img src={loginImage} alt="Login" />

         </div>
    )
}