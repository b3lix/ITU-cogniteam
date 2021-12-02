<template>
  <b-container>
    <b-alert variant="danger" v-show="error !== null" show>
      {{ error }}
    </b-alert>
    <b-form method="POST" @submit.prevent="create">
      <template v-if="step == 0">
        <h2>Jedlo</h2>
        <hr>
        <b-form-group>
          <label>Názov jedla:</label>
          <b-form-input v-model="formData.name" type="text" placeholder="Názov jedla" required></b-form-input>
        </b-form-group>
        <b-form-group>
          <label v-if="formData.type == 0">Výrobca jedla:</label>
          <label v-else-if="formData.type == 1">Názov reštaurácie:</label>
          <label v-else>Zdroj jedla:</label>
          <b-form-input v-model="formData.source" type="text" placeholder="Zdroj jedla" required></b-form-input>
        </b-form-group>
        <b-form-group>
          <label>Type jedla:</label>
          <b-select v-model="formData.type">
              <option :value="null" disabled>Zvolte typ jedla</option>
              <option value="0">Polotovar</option>
              <option value="1">Restauracia</option>
          </b-select>
        </b-form-group>
        <b-form-group>
          <label>Popis:</label>
          <b-form-textarea v-model="formData.description" type="text" placeholder="Popis" required></b-form-textarea>
        </b-form-group>
        <b-form-group v-if="formData.type == 0">
          <label>Čiarový kód:</label>
          <b-form-input v-model="formData.barcode" type="text" placeholder="Čiarový kód" required></b-form-input>
        </b-form-group>
        <b-form-group>
          <b-button variant="primary" @click="step = 1;">Ďalej</b-button>
        </b-form-group>
      </template>
      <template v-else-if="step == 1">
        <h2>Recenzia</h2>
        <hr>
        <b-form-group>
          <label>Popis:</label>
          <b-form-textarea v-model="formData.review.description" type="text" placeholder="Popis" required></b-form-textarea>
        </b-form-group>
        <b-form-group>
          <label>Cena [Kč]:</label>
          <b-form-input v-model="formData.review.price" type="number" placeholder="Cena" required></b-form-input>
        </b-form-group>
        <b-form-group>
          <label>Hodnotenie [0-5]:</label>
          <b-form-input v-model="formData.review.rating" min="0" max="5" type="number" placeholder="Hodnotenie" required></b-form-input>
        </b-form-group>
        Pozitivne body:
        <b-input-group>
          <b-form-input v-model="positive_point" placeholder="Text" type="text"></b-form-input>
          <b-button variant="primary" type="button" @click="formData.review.positive_points.push(positive_point)"><font-awesome-icon icon="plus"></font-awesome-icon></b-button>
        </b-input-group>
        <hr>
        <b-form-group v-for="(value, index) in formData.review.positive_points" :key="index">
          <b-input-group>
            <b-form-input v-model="formData.review.positive_points[index]" placeholder="Text" type="text"></b-form-input>
            <b-button variant="danger" type="button" @click="formData.review.positive_points.splice(index, 1)"><font-awesome-icon icon="times"></font-awesome-icon></b-button>
          </b-input-group>
        </b-form-group>
        <hr>
        Negativne body:
        <b-input-group>
          <b-form-input v-model="negative_point" placeholder="Text" type="text"></b-form-input>
          <b-button variant="primary" type="button" @click="formData.review.negative_points.push(negative_point)"><font-awesome-icon icon="plus"></font-awesome-icon></b-button>
        </b-input-group>
        <hr>
        <b-form-group v-for="(value, index) in formData.review.negative_points" :key="index">
          <b-input-group>
            <b-form-input v-model="formData.review.negative_points[index]" placeholder="Text" type="text"></b-form-input>
            <b-button variant="danger" type="button" @click="formData.review.negative_points.splice(index, 1)"><font-awesome-icon icon="times"></font-awesome-icon></b-button>
          </b-input-group>
        </b-form-group>
        <hr>
        <b-form-group>
          <b-button variant="primary" @click="step = 0;">Späť</b-button>
          <b-button variant="primary" type="submit">Pridať jedlo</b-button>
        </b-form-group>
      </template>
    </b-form>
  </b-container>
</template>
  
<style lang="scss">
</style>
<script>
export default {
  layout: "default",
  middleware: "authenticated",
  data() {
    return {
        step: 0,
        positive_point: null,
        negative_point: null,

        formData: {
            name: null,
            source: null,
            description: null,
            type: null,
            barcode: null,
            review: {
                description: null,
                positive_points: [],
                negative_points: [],
                price: null,
                rating: null
            }
        },

        error: null
    }
  },
  async beforeUnmount() {
    this.step = 0
  },
  methods: {
    create() {
      this.$axios.post("food/create", this.formData).then(() => {
        //this.$router.push("/stations");
        this.error = "Jedlo bolo pridane do databazy";
      }).catch(e => {
        this.error = e.response.data?.message ?? "Nepodarilo sa pridať jedlo";
      });
    }
  }
}
</script>