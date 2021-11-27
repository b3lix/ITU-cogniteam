export const state = () => ({
    filter: {
        value: null,
        type: 0
    },

    items: []
});
  
export const mutations = {
    add(state, food) {
        food.forEach(meal => state.items.push(meal));
    },
    clear(state) {
        state.items = [];
    },
    setFilter(state, filter) {
        state.filter = filter;
    },
    favourite(state, id) {
        let item = state.items.find(x => x.id == id);

        if(item != null)
            item.favourite = (item.favourite == null ? true : null);
    }
};