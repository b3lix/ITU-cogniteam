<template>
  <b-container v-if="loaded">
    <ul>
      <li><NuxtLink to="/">Domov</NuxtLink></li>
      <button v-if="$store.state.user.info" @click="logout">Logout</button>
    </ul>
    <div v-if="$store.state.user.info != null">
      Welcome {{ $store.state.user.info.username }}, role: {{ $store.state.user.info.type }}
    </div>
    <div v-else>
      <b-alert variant="danger" v-show="error !== null" show>
        {{ error }}
      </b-alert>
      <b-form method="POST" @submit.prevent="login" v-if="login_panel">
        <b-form-group>
          <b-form-input v-model="formData.username" type="text" name="username" placeholder="Username" required></b-form-input>
        </b-form-group>
          <b-form-group>
        <b-form-input v-model="formData.password" type="password" name="password" placeholder="Password" required></b-form-input>
          </b-form-group>
        <b-form-group>
          <b-button variant="primary" type="submit">Prihlásiť</b-button>
          <a href="#" @click="login_panel = false">Registrácia</a> 
        </b-form-group>
      </b-form>
      <b-form method="POST" @submit.prevent="register" v-else-if="!login_panel">
        <b-form-group>
          <b-form-input v-model="formData.username" type="text" name="username" placeholder="Username" required></b-form-input>
        </b-form-group>
        <b-form-group>
          <b-form-input v-model="formData.password" type="password" name="password" placeholder="Password" required></b-form-input>
        </b-form-group>
        <b-form-group>
          <b-button variant="primary" type="submit">Vytvoriť účet</b-button>
          <a href="#" @click="login_panel = true">Prihlásenie</a> 
        </b-form-group>
      </b-form>
    </div>
    <Nuxt/>
  </b-container>
</template>

<script>

export default {
  data() {
    return {
      login_panel: true, 

      formData: {
        username: null,
        password: null
      },

      error: null,

      loaded: false
    }
  },
  async created() {
    await this.fetchInfo();
  },
  methods: {
    async fetchInfo() {
      this.$axios.get("/auth/info").then(response => {
        this.$store.commit("user/setInfo", response.data);

        if(!this.loaded)
          this.loaded = true;
      }).catch(() => {
        if(!this.loaded)
          this.loaded = true; 
      })
    },
    async login() {
      this.error = null;

      this.$axios.post("/auth/login", this.formData).then(() => {
        this.fetchInfo();
      }).catch(e => {
        let status = e.response?.status;

        if(status == 401)
          this.error = "Nesprávne prihlasovacie údaje";
        else
          this.error = "Nepodarilo sa kontaktovať server";
      });
    },
    async register() {
      this.error = null;

      this.$axios.post("/auth/register", this.formData).then(() => {
        this.login_panel = true;
      }).catch(e => {
        this.error = "Nepodarilo sa vytvoriť účet";
      });
    },
    logout() {
      this.$axios.post("/auth/logout");
      this.$store.commit("user/setInfo", null);
      this.$router.push("/");
    }
  }
}
</script>
