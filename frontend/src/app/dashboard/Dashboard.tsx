import {AppSidebar} from "@/components/app-sidebar"
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbList,
    BreadcrumbPage,
    BreadcrumbSeparator,
} from "@/components/ui/breadcrumb"
import {Separator} from "@/components/ui/separator"
import {SidebarInset, SidebarProvider, SidebarTrigger,} from "@/components/ui/sidebar"
import {ModeToggle} from "@/components/ModeToggle.tsx";
import {useAuth} from "@/hooks/useAuth.ts";

export default function Dashboard() {

    const {user} = useAuth();

    return (
        <SidebarProvider>
            <AppSidebar/>
            <SidebarInset>
                <header
                    className="flex h-16 shrink-0 items-center justify-between gap-2 transition-[width,height] ease-linear group-has-[[data-collapsible=icon]]/sidebar-wrapper:h-12">
                    <div className="flex items-center gap-2 px-4">
                        <SidebarTrigger className="-ml-1"/>
                        <Separator orientation="vertical" className="mr-2 h-4"/>
                        <Breadcrumb>
                            <BreadcrumbList>
                                <BreadcrumbItem className="hidden md:block">
                                    <BreadcrumbItem>
                                        <BreadcrumbPage>Organisation</BreadcrumbPage>
                                    </BreadcrumbItem>
                                </BreadcrumbItem>
                                <BreadcrumbSeparator className="hidden md:block"/>
                                <BreadcrumbItem>
                                    <BreadcrumbPage>Events</BreadcrumbPage>
                                </BreadcrumbItem>
                            </BreadcrumbList>
                        </Breadcrumb>
                    </div>
                    <div className={"px-4"}>
                        <ModeToggle/>
                    </div>
                </header>
                <div className="flex flex-1 flex-col gap-4 p-4 pt-0">

                    <div className="grid auto-rows-min gap-4 md:grid-cols-3">
                        <div className="aspect-video rounded-xl bg-muted/50"/>
                        <div className="aspect-video rounded-xl bg-muted/50"/>
                        <div className="aspect-video rounded-xl bg-muted/50"/>
                    </div>
                    <p>{user?.name}</p>
                    <p>{user?.email}</p>
                    <div className="min-h-[100vh] flex-1 rounded-xl bg-muted/50 md:min-h-min">

                    </div>
                </div>
            </SidebarInset>
        </SidebarProvider>
    )
}
