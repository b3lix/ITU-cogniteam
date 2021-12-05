/*
Projekt ITU
Autori:
    xslesa01 (Michal Šlesár)
*/

export const state = () => ({
  info: null
});

export const mutations = {
  // Set user info
  setInfo(state, info) {
    state.info = info;
  }
};