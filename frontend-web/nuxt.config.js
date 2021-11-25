export default {
    build: {
        loaders: {
            vue: {
                compiler: require("vue-template-babel-compiler")
            }
        },
    },
    modules: [
        ["@nuxtjs/axios"],
        ["bootstrap-vue/nuxt"],
        ["cookie-universal-nuxt", { alias: "cookies" }]
    ],
    buildModules: [
        "@nuxtjs/fontawesome",
    ],
    css: [
        "@/assets/styles/main.scss"
    ],
    axios: {
        baseUrl: "https://localhost:8000",
        credentials: true
    },
    bootstrapVue: {
        icons: true,
        bootstrapCSS: false,
        bootstrapVueCSS: false,
    },
    fontawesome: {
        icons: {
            solid: ["faBars", "faHome", "faCamera"]
        }
    }
}