/*
Projekt ITU
Autori:
    xslesa01 (Michal Šlesár)
*/


// Redirect user to index if not logged in
export default function ({ store, redirect }) {
    if(store.state.user.info == null)
        return redirect("/");
}