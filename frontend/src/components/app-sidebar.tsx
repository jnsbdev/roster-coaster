import * as React from "react"
import {GalleryVerticalEnd, Plus,} from "lucide-react"
import {NavUser} from "@/components/nav-user"
import {OrganisationSwitcher} from "@/components/team-switcher"
import {
    Sidebar,
    SidebarContent,
    SidebarFooter,
    SidebarGroup,
    SidebarGroupContent,
    SidebarGroupLabel,
    SidebarHeader,
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem,
    SidebarRail,
} from "@/components/ui/sidebar"
import {useAuth} from "@/hooks/useAuth"
import {useLocation} from "react-router-dom";
import {Button} from "@/components/ui/button.tsx";

// This is sample & placeholder data.
const data = {
    organisations: [
        {
            name: "Personal",
            logo: GalleryVerticalEnd,
        },
    ],
    navMain: [
        {
            title: "Organisation",
            items: [
                {
                    title: "Events",
                    url: "/home",
                },
            ]
        },
        {
            title: "Persönlich",
            items: [
                {
                    title: "Meine Events",
                    url: "/",
                },
                {
                    title: "Meine Schichten",
                    url: "/",
                },
            ],
        },
    ],
}

export function AppSidebar({...props}: React.ComponentProps<typeof Sidebar>) {

    const {user} = useAuth();
    const location = useLocation();
    const pathname = location.pathname;


    return (
        <Sidebar collapsible="icon" {...props}>
            <SidebarHeader>
                <Button variant={"outline"}>
                    <Plus/>
                    Neue Organisation
                </Button>
                <OrganisationSwitcher organisations={data.organisations}/>
            </SidebarHeader>
            <SidebarContent>
                {data.navMain.map((item) => (
                    <SidebarGroup key={item.title}>
                        <SidebarGroupLabel>{item.title}</SidebarGroupLabel>
                        <SidebarGroupContent>
                            <SidebarMenu>
                                {item.items.map((item) => (
                                    <SidebarMenuItem key={item.title}>
                                        <SidebarMenuButton asChild isActive={pathname === item.url}>
                                            <a href={item.url}>{item.title}</a>
                                        </SidebarMenuButton>
                                    </SidebarMenuItem>
                                ))}
                            </SidebarMenu>
                        </SidebarGroupContent>
                    </SidebarGroup>
                ))}
            </SidebarContent>
            <SidebarFooter>
                {user && <NavUser user={user}/>}
            </SidebarFooter>
            <SidebarRail/>
        </Sidebar>
    )
}
