<template>
  <b-container>
    <b-alert variant="danger" v-show="error !== null" show>
      {{ error }}
    </b-alert>
    <b-form method="GET" @submit.prevent="search">
      <b-form-group>
        <b-form-input v-model="formData.value" type="text" placeholder="Nazov/Zdroj/Ciarovy kod"></b-form-input>
      </b-form-group>
      <b-form-group>
        <label>Typ jedla:</label>
        <b-select v-model="formData.type">
            <option value="0">Polotovar</option>
            <option value="1">Restauracia</option>
        </b-select>
      </b-form-group>

      <b-button type="submit" variant="primary">Hladaj</b-button>
    </b-form>
    <hr>
    <div v-for="meal in $store.state.food.items" :key="meal.id">
      <div><b-link :to="'/food/' + meal.id">{{meal.source}} - {{ meal.name }}</b-link>
      <span @click="favourite(meal.id)">
        <template v-if="meal.favourite == null">
          <font-awesome-icon icon="star"></font-awesome-icon>
        </template>
        <template v-else>
          <font-awesome-icon icon="star" style="color: yellow;"></font-awesome-icon>
        </template>
      </span>
      </div>
      <div><strong>Priemerna cena:</strong> {{ meal.average.price}}e</div>
      <div><strong>Priemerne hodnotenie:</strong> {{ meal.average.rating }}/10</div>
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
    }
  },
  async beforeMount() {
    this.search();
  },
  methods: {
    search() {
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