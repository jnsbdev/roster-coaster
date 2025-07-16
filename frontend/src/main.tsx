import React from "react";
import {BrowserRouter} from "react-router-dom";
import {createRoot} from "react-dom/client";
// import 'antd/dist/reset.css'
import './index.css'
import App from "@/App.tsx";

createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
            <BrowserRouter>
                {/* <AppOld/> */}
                <App/>
            </BrowserRouter>
    </React.StrictMode>
);