<template>
  <b-container>
    <div v-for="review in reviews" :key="review.id">
        <b-link :to="'/food/' + review.food.id"> {{ review.food.source}} - {{ review.food.name }}</b-link>
        <div><strong>Datum:</strong> {{ review.date }}</div>
        <div><strong>Popis:</strong> {{ review.description }}</div>
        <div><strong>Cena:</strong> {{ review.price }}e</div>
        <div><strong>Hodnotenie:</strong> {{ review.rating }}/10</div>
        <br>
        <div v-for="(item, index) in review.positive_points" :key="index">
            + {{ item }}
        </div>
        <div v-for="(item, index) in review.negative_points" :key="index">
            - {{ item }}
        </div>
        <hr>
    </div>
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
        reviews: []
    }
  },
  async beforeMount() {
    let result = await this.$axios.get(`/reviews/my`);
    this.reviews = result.data.reviews;
  },
  methods: {
  }
}
</script>