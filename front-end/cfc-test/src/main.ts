import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
// Vuetify
import '@mdi/font/css/materialdesignicons.css';
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import { aliases, mdi } from 'vuetify/iconsets/mdi';
import "vuetify/styles";
// Translations provided by Vuetify
import { pt } from 'vuetify/locale';
import money from 'v-money3'

const vuetify = createVuetify({
  components,
  directives,
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: {
      mdi,
    },
  },
  locale: {
    locale: 'pt',
    messages: {pt}
  }
});

const app = createApp(App)
app.use(money)
app.use(router).use(vuetify).mount("#app");
