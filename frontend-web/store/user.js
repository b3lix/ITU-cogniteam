export const state = () => ({
  info: null
});

export const mutations = {
  setInfo(state, info) {
    state.info = info;
  }
};