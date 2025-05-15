// import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import Particles from '@tsparticles/vue3'
import { loadFull } from 'tsparticles'
const app = createApp(App)

app.use(Particles, {
  init: async (engine) => {
    await loadFull(engine) // you can load the full tsParticles library from "tsparticles" if you need it
    // loadSlim 轻量级的
    // await loadSlim(engine) // or you can load the slim version from "tsparticles-slim" if don't need Shapes or Animations
  }
})

const apiClient = axios.create({
    baseURL: 'http://localhost:8080'
  });
  app.provide('$axios',apiClient);

app.use(createPinia())
app.use(router)
app.use(ElementPlus)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }
app.mount('#app')

