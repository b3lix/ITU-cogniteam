<template>
  <b-container>
    <div v-if="meal != null">
        <div><strong>Nazov:</strong> {{ meal.name }}</div>
        <div><strong>Zdroj:</strong> {{ meal.source }}</div>
        <div><strong>Typ:</strong> {{ meal.type }}</div>
        <div><strong>Popis:</strong> {{ meal.description }}</div>
        <br>
        <div><strong>Priemerna cena:</strong> {{ meal.average.price }}e</div>
        <div><strong>Priemerne hodnotenie:</strong> {{ meal.average.rating }}/10</div>
    </div>
    <hr>
    <h2>Moja recenzia:</h2>
    <hr>
    <div v-if="my_review != null">
        <b-link v-b-modal.modal-update>Upravit</b-link>
        <div><strong>Datum:</strong> {{ my_review.date }}</div>
        <div><strong>Popis:</strong> {{ my_review.description }}</div>
        <div><strong>Cena:</strong> {{ my_review.price }}e</div>
        <div><strong>Hodnotenie:</strong> {{ my_review.rating }}/10</div>
        <br>
        <div v-for="(item, index) in my_review.positive_points" :key="index">
            + {{ item }}
        </div>
        <div v-for="(item, index) in my_review.negative_points" :key="index">
            - {{ item }}
        </div>
        <b-modal id="modal-update" title="Upravit recenziu" hide-footer>
            <b-alert variant="danger" v-show="updateError !== null" show>
                {{ updateError }}
            </b-alert>
            <b-form method="POST" @submit.prevent="updateReview()">
                <b-form-group>
                    <label>Popis:</label>
                    <b-form-input v-model="my_review_edit.description" type="text" placeholder="Popis" required></b-form-input>
                </b-form-group>
                <b-form-group>
                    <label>Cena:</label>
                    <b-form-input v-model="my_review_edit.price" type="number" placeholder="Cena" required></b-form-input>
                </b-form-group>
                <b-form-group>
                    <label>Hodnotenie:</label>
                    <b-form-input v-model="my_review_edit.rating" type="number" placeholder="Hodnotenie" required></b-form-input>
                </b-form-group>
                Pozitivne body:
                <b-input-group>
                    <b-form-input v-model="positive_point" placeholder="Text" type="text"></b-form-input>
                    <b-button variant="primary" type="button" @click="my_review_edit.positive_points.push(positive_point)">Pridať bod</b-button>
                </b-input-group>
                <hr>
                <b-form-group v-for="(value, index) in my_review_edit.positive_points" :key="index + 'p'">
                    <b-input-group>
                        <b-form-input v-model="my_review_edit.positive_points[index]" placeholder="Text" type="text"></b-form-input>
                        <b-button variant="primary" type="button">x</b-button>
                    </b-input-group>
                </b-form-group>
                <hr>
                Negativne body:
                <b-input-group>
                    <b-form-input v-model="negative_point" placeholder="Text" type="text"></b-form-input>
                    <b-button variant="primary" type="button" @click="my_review_edit.negative_points.push(negative_point)">Pridať bod</b-button>
                </b-input-group>
                <hr>
                <b-form-group v-for="(value, index) in my_review_edit.negative_points" :key="index + 'n'">
                    <b-input-group>
                        <b-form-input v-model="my_review_edit.negative_points[index]" placeholder="Text" type="text"></b-form-input>
                        <b-button variant="primary" type="button">x</b-button>
                    </b-input-group>
                </b-form-group>
                <hr>
                <b-form-group>
                    <b-button variant="primary" type="submit">Pridat recenziu</b-button>
                </b-form-group>
            </b-form>
        </b-modal>
    </div>
    <div v-else>
        <b-alert show>
            Este ste nepridali recenziu
        </b-alert>
        <b-button variant="primary" v-b-modal.modal-create v-if="$store.state.user.info != null">Pridat recenziu</b-button>
        <b-modal id="modal-create" title="Vytvoriť recenziu" hide-footer>
            <b-alert variant="danger" v-show="insertError !== null" show>
                {{ insertError }}
            </b-alert>
            <b-form method="POST" @submit.prevent="createReview()">
                <b-form-group>
                    <label>Popis:</label>
                    <b-form-input v-model="formData.description" type="text" placeholder="Popis" required></b-form-input>
                </b-form-group>
                <b-form-group>
                    <label>Cena:</label>
                    <b-form-input v-model="formData.price" type="number" placeholder="Cena" required></b-form-input>
                </b-form-group>
                <b-form-group>
                    <label>Hodnotenie:</label>
                    <b-form-input v-model="formData.rating" type="number" placeholder="Hodnotenie" required></b-form-input>
                </b-form-group>
                Pozitivne body:
                <b-input-group>
                    <b-form-input v-model="positive_point" placeholder="Text" type="text"></b-form-input>
                    <b-button variant="primary" type="button" @click="formData.positive_points.push(positive_point)">Pridať bod</b-button>
                </b-input-group>
                <hr>
                <b-form-group v-for="(value, index) in formData.positive_points" :key="index + 'p'">
                    <b-input-group>
                        <b-form-input v-model="formData.positive_points[index]" placeholder="Text" type="text"></b-form-input>
                        <b-button variant="primary" type="button">x</b-button>
                    </b-input-group>
                </b-form-group>
                <hr>
                Negativne body:
                <b-input-group>
                    <b-form-input v-model="negative_point" placeholder="Text" type="text"></b-form-input>
                    <b-button variant="primary" type="button" @click="formData.negative_points.push(negative_point)">Pridať bod</b-button>
                </b-input-group>
                <hr>
                <b-form-group v-for="(value, index) in formData.negative_points" :key="index + 'n'">
                    <b-input-group>
                        <b-form-input v-model="formData.negative_points[index]" placeholder="Text" type="text"></b-form-input>
                        <b-button variant="primary" type="button">x</b-button>
                    </b-input-group>
                </b-form-group>
                <hr>
                <b-form-group>
                    <b-button variant="primary" type="submit">Pridat recenziu</b-button>
                </b-form-group>
            </b-form>
        </b-modal>
    </div>
    <hr>
    <h2>Recenzie:</h2>
    <hr>
    <div v-for="review in reviews" :key="review.id">
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
  data() {
    return {
        id: this.$route.params.id,

        insertError: null,
        updateError: null,

        formData: {
            food: this.$route.params.id,
            description: null,
            positive_points: [],
            negative_points: [],
            price: null,
            rating: null
        },

        meal: null,
        my_review: null,
        my_review_edit: null,
        reviews: []
    }
  },
  async beforeMount() {
    this.fetchData();
  },
  methods: {
      async fetchData() {
        let result = await this.$axios.get(`/food/get/${this.id}`);
        this.meal = result.data;

        this.$axios.get(`/reviews/my/${this.id}`).then(response => {
            this.my_review = response.data;
            this.my_review_edit = this.$_.cloneDeep(response.data);
        }).catch(e => {});

        result = await this.$axios.get(`/reviews/get/${this.id}`);
        this.reviews = result.data.reviews;
      },
      createReview() {
        this.insertError = null;

        this.$axios.post("reviews/create", this.formData).then(response => {
            this.insertError = "Recenzia uspesne pridana";
            this.fetchData();
        }).catch(e => {
            this.insertError = e.response.data?.message ?? "Nepodarilo sa pridat recenziu";
        });
    },
    updateReview() {
        this.updateError = null;

        this.$axios.post("reviews/update", this.my_review_edit).then(response => {
            this.updateError = "Recenzia uspesne upravena";
            this.fetchData();
        }).catch(e => {
            this.updateError = e.response.data?.message ?? "Nepodarilo sa upravit recenziu";
        });
    }
  }
}
</script>