<!--
Projekt ITU
Autori:
  xslesa01 (Michal Šlesár)
-->

<template>
  <b-container>
    <div v-if="meal != null">
        <div><strong>Názov:</strong> {{ meal.name }}</div>
        <div><strong>Zdroj:</strong> {{ meal.source }}</div>
        <div><strong>Typ:</strong> {{ meal.type == "1" ? "Reštaurácia" : "Polotovar" }}</div>
        <br>
        <div><strong>Počet recenzií: </strong>{{ reviews.length }}</div>
        <br>
        <p>{{ meal.description }}</p>
        <br>
        <div><strong>Priemerná cena: <font-awesome-icon icon="dollar-sign"></font-awesome-icon></strong> {{ meal.average.price == null ? "Neznáma" : meal.average.price + "Kč" }}</div>
        <div><strong>Priemerná hodnotenie:</strong> <font-awesome-icon icon="star"></font-awesome-icon> {{ meal.average.rating }} / 5</div>
    </div>
    <hr>
    <b-row v-if="my_review != null">
        <div class="col-sm-6">
            <div style="border: 1px solid #EFEFEF; padding: 10px;">
                <div class="row justify-content-between">
                    <div class="col-auto mr-auto">
                        <div><b-link>{{ $store.state.user.info.username }}</b-link></div>
                        <div style="font-size: 12px;">{{ my_review.date }}</div>
                        <div style="font-size: 14px;">
                            <font-awesome-icon icon="dollar-sign"></font-awesome-icon> 
                            {{ my_review.price == null ? "Nezadaná" : my_review.price + "Kč" }}
                        </div>
                    </div>
                    <div class="col-auto">
                        <div class="review-block-rate">
                            <b-button variant="primary" size="sm" v-b-modal.modal-update><font-awesome-icon icon="pencil-alt"></font-awesome-icon> Upraviť</b-button>
                            <font-awesome-icon icon="star" style="color: rgb(200, 150, 0);"></font-awesome-icon>
                            {{ my_review.rating }} / 5
                        </div>
                    </div>
                </div>
                <div class="row" style="padding: 15px;">
                    <div class="review-block-description">{{ my_review.description }}</div>
                </div>
                <div class="row">
                    <div class="col">
                        <div v-for="(item, index) in my_review.positive_points" :key="index">
                            <span style="color: green; font-weight: bold;">+</span> {{ item }}
                        </div>
                    </div>
                    <div class="col">
                        <div v-for="(item, index) in my_review.negative_points" :key="index">
                            <span style="color: red; font-weight: bold;">-</span> {{ item }}
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <b-modal id="modal-update" title="Upraviť recenziu" hide-footer>
            <b-alert variant="danger" v-show="updateError !== null" show>
                {{ updateError }}
            </b-alert>
            <b-form method="POST" @submit.prevent="updateReview()">
                <b-form-group>
                    <label>Popis:</label>
                    <b-form-textarea v-model="my_review_edit.description" type="text" placeholder="Popis" required></b-form-textarea>
                </b-form-group>
                <b-form-group>
                    <label>Cena [Kč]:</label>
                    <b-form-input v-model="my_review_edit.price" type="number" placeholder="Cena" required></b-form-input>
                </b-form-group>
                <b-form-group>
                    <label>Hodnotenie [0-5]:</label>
                    <b-form-input v-model="my_review_edit.rating" min="0" max="5" type="number" placeholder="Hodnotenie" required></b-form-input>
                </b-form-group>
                Pozitivne body:
                <b-input-group>
                    <b-form-input v-model="positive_point" placeholder="Text" type="text"></b-form-input>
                    <b-button variant="primary" type="button" @click="my_review_edit.positive_points.push(positive_point)"><font-awesome-icon icon="plus"></font-awesome-icon></b-button>
                </b-input-group>
                <hr>
                <b-form-group v-for="(value, index) in my_review_edit.positive_points" :key="index + 'p'">
                    <b-input-group>
                        <b-form-input v-model="my_review_edit.positive_points[index]" placeholder="Text" type="text"></b-form-input>
                        <b-button variant="danger" type="button" @click="my_review_edit.positive_points.splice(index, 1)"><font-awesome-icon icon="times"></font-awesome-icon></b-button>
                    </b-input-group>
                </b-form-group>
                <hr>
                Negativne body:
                <b-input-group>
                    <b-form-input v-model="negative_point" placeholder="Text" type="text"></b-form-input>
                    <b-button variant="primary" type="button" @click="my_review_edit.negative_points.push(negative_point)"><font-awesome-icon icon="plus"></font-awesome-icon></b-button>
                </b-input-group>
                <hr>
                <b-form-group v-for="(value, index) in my_review_edit.negative_points" :key="index + 'n'">
                    <b-input-group>
                        <b-form-input v-model="my_review_edit.negative_points[index]" placeholder="Text" type="text"></b-form-input>
                        <b-button variant="danger" type="button" @click="my_review_edit.negative_points.splice(index, 1)"><font-awesome-icon icon="times"></font-awesome-icon></b-button>
                    </b-input-group>
                </b-form-group>
                <hr>
                <b-form-group>
                    <b-button variant="primary" type="submit">Upraviť recenziu</b-button>
                </b-form-group>
            </b-form>
        </b-modal>
    </b-row>
    <div v-else>
        <b-alert show>
            Ešte ste nepridali recenziu
        </b-alert>
        <b-button variant="primary" v-b-modal.modal-create v-if="$store.state.user.info != null">Pridať recenziu</b-button>
        <b-modal id="modal-create" title="Vytvoriť recenziu" hide-footer>
            <b-alert variant="danger" v-show="insertError !== null" show>
                {{ insertError }}
            </b-alert>
            <b-form method="POST" @submit.prevent="createReview()">
                <b-form-group>
                    <label>Popis:</label>
                    <b-form-textarea v-model="formData.description" type="text" placeholder="Popis" required></b-form-textarea>
                </b-form-group>
                <b-form-group>
                    <label>Cena [Kč]:</label>
                    <b-form-input v-model="formData.price" type="number" placeholder="Cena" required></b-form-input>
                </b-form-group>
                <b-form-group>
                    <label>Hodnotenie [0-5]:</label>
                    <b-form-input v-model="formData.rating" min="0" max="5" type="number" placeholder="Hodnotenie" required></b-form-input>
                </b-form-group>
                Pozitívne body:
                <b-input-group>
                    <b-form-input v-model="positive_point" placeholder="Text" type="text"></b-form-input>
                    <b-button variant="primary" type="button" @click="formData.positive_points.push(positive_point)"><font-awesome-icon icon="plus"></font-awesome-icon></b-button>
                </b-input-group>
                <hr>
                <b-form-group v-for="(value, index) in formData.positive_points" :key="index + 'p'">
                    <b-input-group>
                        <b-form-input v-model="formData.positive_points[index]" placeholder="Text" type="text"></b-form-input>
                        <b-button variant="danger" type="button" @click="formData.positive_points.splice(index, 1)"><font-awesome-icon icon="times"></font-awesome-icon></b-button>
                    </b-input-group>
                </b-form-group>
                <hr>
                Negatívne body:
                <b-input-group>
                    <b-form-input v-model="negative_point" placeholder="Text" type="text"></b-form-input>
                    <b-button variant="primary" type="button" @click="formData.negative_points.push(negative_point)"><font-awesome-icon icon="plus"></font-awesome-icon></b-button>
                </b-input-group>
                <hr>
                <b-form-group v-for="(value, index) in formData.negative_points" :key="index + 'n'">
                    <b-input-group>
                        <b-form-input v-model="formData.negative_points[index]" placeholder="Text" type="text"></b-form-input>
                        <b-button variant="danger" type="button" @click="formData.negative_points.splice(index, 1)"><font-awesome-icon icon="times"></font-awesome-icon></b-button>
                    </b-input-group>
                </b-form-group>
                <hr>
                <b-form-group>
                    <b-button variant="primary" type="submit">Pridať recenziu</b-button>
                </b-form-group>
            </b-form>
        </b-modal>
    </div>
    <hr>
    <h3>Všetky recenzie ({{ reviews.length }}):</h3>
    <hr>
    <b-row v-for="review in reviews" :key="review.id">
        <div class="col-sm-6 mt-2">
            <div style="border: 1px solid #EFEFEF; padding: 10px;">
                <div class="row justify-content-between">
                    <div class="col-auto mr-auto">
                        <div><b-link>{{ review.user }}</b-link></div>
                        <div style="font-size: 12px;">{{ review.date }}</div>
                        <div style="font-size: 14px;">
                            <font-awesome-icon icon="dollar-sign"></font-awesome-icon> 
                            {{ review.price == null ? "Nezadaná" : review.price + "Kč" }}
                        </div>
                    </div>
                    <div class="col-auto">
                        <div class="review-block-rate">
                            <font-awesome-icon icon="star" style="color: rgb(200, 150, 0);"></font-awesome-icon>
                            {{ review.rating }} / 5
                        </div>
                    </div>
                </div>
                <div class="row" style="padding: 15px;">
                    <div class="review-block-description">{{ review.description }}</div>
                </div>
                <div class="row">
                    <div class="col">
                        <div v-for="(item, index) in review.positive_points" :key="index">
                            <span style="color: green; font-weight: bold;">+</span> {{ item }}
                        </div>
                    </div>
                    <div class="col">
                        <div v-for="(item, index) in review.negative_points" :key="index">
                            <span style="color: red; font-weight: bold;">-</span> {{ item }}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </b-row>
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
      // Fetch reviews and meal details
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
      // Create review
      createReview() {
        this.insertError = null;

        this.$axios.post("reviews/create", this.formData).then(response => {
            this.insertError = "Recenzia uspesne pridana";
            this.fetchData();
        }).catch(e => {
            this.insertError = e.response.data?.message ?? "Nepodarilo sa pridat recenziu";
        });
    },
    // Update review details
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