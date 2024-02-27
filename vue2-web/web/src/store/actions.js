export default {
    setLanguage({ commit }, language) {
        commit('SET_LANGUAGE', language)
    },
    setToken({commit},token){
        commit('SET_TOKEN', token)
    }
}
