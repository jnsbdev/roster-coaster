import {Navigate, Route, Routes} from "react-router-dom";
import {LoginForm} from "@/app/auth/LoginForm.tsx"
import {ThemeProvider} from "./components/ThemeProvider.tsx";
import {RegisterForm} from "@/app/auth/RegisterForm.tsx";
import {ForgotPasswordForm} from "@/app/auth/ForgotPasswordForm.tsx";
import ProtectedRoute from "@/app/auth/ProtectedRoute.tsx";
import {AuthProvider} from "@/context/AuthProvider.tsx";
import Dashboard from "@/app/dashboard/Dashboard.tsx";
import {useAuth} from "@/hooks/useAuth.ts";


function RootRedirect() {
    const {isAuthenticated} = useAuth();
    return (
        isAuthenticated
            ? <Navigate to="/home" replace/>
            : <Navigate to="/login" replace/>
    );
}


export default function App() {


    return (
        <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
            <AuthProvider>
                <Routes>
                    <Route path="/" element={<RootRedirect/>}/>

                    <Route path={"/login"} element={
                        <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
                            <div className="w-full max-w-sm">
                                <LoginForm/>
                            </div>
                        </div>
                    }/>

                    <Route path={"/register"} element={
                        <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
                            <div className="w-full max-w-sm">
                                <RegisterForm/>
                            </div>
                        </div>
                    }/>

                    <Route path={"/recover"} element={
                        <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
                            <div className="w-full max-w-sm">
                                <ForgotPasswordForm/>
                            </div>
                        </div>
                    }/>

                    <Route path="/home" element={
                        <ProtectedRoute>
                            <Dashboard/>
                        </ProtectedRoute>
                    }/>

                </Routes>
            </AuthProvider>
        </ThemeProvider>
    )
}