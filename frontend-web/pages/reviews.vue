<template>
	<b-container>
    <b-alert v-if="reviews.length == 0" show>
      Ešte ste nepridali žiadnu recenziu
    </b-alert>
		<b-row v-for="review in reviews" :key="review.id">
			<div class="col-sm-6">
				<div style="border: 1px solid #EFEFEF; padding: 10px;">
					<div class="row justify-content-between">
						<div class="col-auto mr-auto">
							<div><b-link :to="'/food/' + review.food.id">{{ review.food.source}} - {{ review.food.name }}</b-link></div>
							<div style="font-size: 12px;">{{ review.date }}</div>
							<div style="font-size: 14px;">
								<font-awesome-icon icon="dollar-sign"></font-awesome-icon> 
								{{ review.price }} Kč
							</div>
						</div>
						<div class="col-auto">
							<div class="review-block-rate">
								<b-button variant="primary" size="sm" v-b-modal.modal-update @click="formData = {
									id: review.id,
									description: review.description,
									positive_points: review.positive_points,
									negative_points: review.negative_points,
									price: review.price,
									rating: review.rating
								}"><font-awesome-icon icon="pencil-alt"></font-awesome-icon> Upraviť</b-button>
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
		<b-modal id="modal-update" title="Upravit recenziu" hide-footer>
      <b-alert variant="danger" v-show="updateError !== null" show>
          {{ updateError }}
      </b-alert>
      <b-form method="POST" @submit.prevent="updateReview()">
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
              <b-button variant="primary" type="submit">Upraviť recenziu</b-button>
          </b-form-group>
      </b-form>
    </b-modal>
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
      updateError: null,

      formData: {
          id: null,
          description: null,
          positive_points: [],
          negative_points: [],
          price: null,
          rating: null
      },

      reviews: []
    }
  },
  async beforeMount() {
    this.fetchData();
  },
  methods: {
	  async fetchData() {
      let result = await this.$axios.get(`/reviews/my`);
      this.reviews = result.data.reviews;
    },
	  updateReview() {
        this.updateError = null;

        this.$axios.post("reviews/update", this.formData).then(response => {
            this.updateError = "Recenzia úspešne upravená";
            this.fetchData();
        }).catch(e => {
            this.updateError = e.response.data?.message ?? "Nepodarilo sa upraviť recenziu";
        });
    }
  }
}
</script>