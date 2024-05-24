export default {
  namespaced: true,
  state: {
    id: '',
    name: '',
    shopsId: ''
  },
  mutations: {
    updateId (state, id) {
      state.id = id
    },
    updateName (state, name) {
      state.name = name
    },
    updateShopsId (state, shopsId) {
      state.shopsId = shopsId
    }
  }
}
