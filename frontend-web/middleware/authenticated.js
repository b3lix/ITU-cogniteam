export default function ({ store, redirect }) {
    if(store.state.user.info == null)
        return redirect("/");
}