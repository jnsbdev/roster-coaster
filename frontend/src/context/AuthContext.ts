import { createContext } from "react";
import { UserDTO } from "@/types/UserDTO.ts";
import {LoginRequest} from "@/types/LoginRequest.ts";
import {RegisterRequest} from "@/types/RegisterRequest.ts";

export type AuthContextType = {
    user: UserDTO | null;
    register: (request: RegisterRequest) => Promise<void>;
    login: (request: LoginRequest) => Promise<void>;
    isAuthenticated: boolean;
    logout: () => void;
    loading: boolean;
};

export const AuthContext = createContext<AuthContextType | undefined>(undefined);
