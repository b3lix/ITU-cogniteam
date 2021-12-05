/*
Projekt ITU
Autori:
    xslesa01 (Michal Šlesár)
*/

export const state = () => ({
    filter: {
        value: null,
        type: 0
    },

    ascending: true,

    items: []
});
  
export const mutations = {
    // Add food into store
    add(state, food) {
        food.forEach(meal => state.items.push(meal));
    },
    // Clear loaded food
    clear(state) {
        state.items = [];
    },
    // Save current filter
    setFilter(state, filter) {
        state.filter = filter;
    },
    // Toggle favourite food
    favourite(state, id) {
        let item = state.items.find(x => x.id == id);

        if(item != null)
            item.favourite = (item.favourite == null ? true : null);
    },
    // Toggle sort type
    toggleSort(state) {
        state.ascending = !state.ascending;
    }
};