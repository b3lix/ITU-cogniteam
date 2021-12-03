<template>
  <b-container>
    <b-alert variant="danger" v-show="error !== null" show>
      {{ error }}
    </b-alert>
    <b-form method="GET" @submit.prevent="search">
      <b-input-group>
        <b-form-input v-model="formData.value" type="text" placeholder="Vyhľadávanie podľa Názvu / Zdroju / Čiarového kódu" @keyup="search"></b-form-input>
        <b-select v-model="formData.type" @change="search">
            <option value="0">Polotovar</option>
            <option value="1">Reštaurácia</option>
        </b-select>
        <b-input-group-append>
          <b-button variant="primary" type="button" @click="$store.commit('food/toggleSort'); search()"><font-awesome-icon :icon="$store.state.food.ascending ? 'sort-amount-down' : 'sort-amount-up'"></font-awesome-icon></b-button>
          <b-button variant="primary" type="submit"><font-awesome-icon icon="search"></font-awesome-icon></b-button>
        </b-input-group-append>
      </b-input-group>
    </b-form>
    <hr>
    <template v-if="$store.state.food.items.length == 0">
      <b-alert variant="info" show>
        Nenašli sa žiadne položky
      </b-alert>
      <hr>
    </template>
    <div v-for="meal in ($store.state.food.ascending ? $store.state.food.items : _.clone($store.state.food.items).reverse())" :key="meal.id">
      <div><b-link :to="'/food/' + meal.id"><strong>{{meal.source}} - {{ meal.name }}</strong></b-link>
      <span @click="favourite(meal.id)" v-if="$store.state.user.info != null">
        <template v-if="meal.favourite == null">
          <font-awesome-icon icon="star" style="color: grey; float: right;"></font-awesome-icon>
        </template>
        <template v-else>
          <font-awesome-icon icon="star" style="color: yellow; float: right;"></font-awesome-icon>
        </template>
      </span>
      </div>
      <div>Recenzií: {{ meal.reviews }}</div>
      <div><i>Priemerná cena:</i> <font-awesome-icon icon="dollar-sign"></font-awesome-icon> <strong>{{ meal.average.price}} Kč</strong></div>
      <div><i>Priemerné hodnotenie:</i> <font-awesome-icon icon="star"></font-awesome-icon> <strong>{{ meal.average.rating }} / 5</strong></div>
      <hr>
    </div>
  </b-container>
</template>

<style lang="scss">
</style>

<script>
export default {
  layout: "default",
  data() {
    return {
      error: null,

      formData: this.$_.cloneDeep(this.$store.state.food.filter),

      ascending: true
    }
  },
  async beforeMount() {
    this.search();
  },
  methods: {
    search() {
      console.log("wtf");
      this.error = null;

      this.$store.commit("food/clear");
      this.$store.commit("food/setFilter", this.$_.cloneDeep(this.formData));
      this.$axios.get("/food/get", {params: this.formData}).then(response => {
        this.$store.commit("food/add", this.$_.cloneDeep(response.data.food));
      }).catch(e => {
        this.error = e.response.data?.message ?? "Nepodarilo sa získať polozky";
      });
    },
    favourite(id) {
      this.$axios.post(`/food/favourite/${id}`).then(response => {
        this.$store.commit("food/favourite", id);
      }).catch(e => {});
    }
  }
}
</script>